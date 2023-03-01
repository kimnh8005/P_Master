package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.base.dto.GetCodeListResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.api.constant.LegalTypes;
import kr.co.pulmuone.v1.comm.api.constant.ProductTypes;
import kr.co.pulmuone.v1.comm.api.constant.SalesTypes;
import kr.co.pulmuone.v1.comm.api.constant.SupplierTypes;
import kr.co.pulmuone.v1.comm.api.dto.basic.*;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDiscountMethodType;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDiscountType;
import kr.co.pulmuone.v1.comm.enums.ItemEnums;
import kr.co.pulmuone.v1.comm.enums.ItemEnums.*;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.UploadFileDto;
import kr.co.pulmuone.v1.comm.mapper.goods.item.GoodsItemRegisterMapper;
import kr.co.pulmuone.v1.comm.util.ImageUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.service.GoodsRegistBiz;
import kr.co.pulmuone.v1.goods.item.dto.*;
import kr.co.pulmuone.v1.goods.item.dto.vo.*;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeAuthVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * 마스터 품목 등록 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 10. 26.               박주형         최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsItemRegisterService {

    @Autowired
    private final GoodsItemRegisterMapper goodsItemRegisterMapper;

    // ERP API 조회 로직의 JUnit 테스트를 위해 서비스단에서 ErpApiExchangeService 클래스를 @Autowired 로 가져옴
    @Autowired
    private final ErpApiExchangeService erpApiExchangeService;

	@Autowired
	private GoodsRegistBiz goodsRegistBiz;

    // ERP API 에서 품목 정보 조회 인터페이스 ID
    private static final String ITEM_SEARCH_INTERFACE_ID = "IF_GOODS_SRCH";

    // ERP API 에서 올가매장정보 품목 조회 인터페이스 ID
    private static final String IF_ORGASHOP_STOCK_SRCH = "IF_ORGASHOP_STOCK_SRCH";

    // ERP API 에서 영양 정보 조회 인터페이스 ID
    private static final String NUTRITION_SEARCH_INTERFACE_ID = "IF_NUTRI_SRCH";

    // ERP API 에서 가격 정보 조회 인터페이스 ID
    private static final String PRICE_SEARCH_INTERFACE_ID = "IF_PRICE_SRCH";

    // ERP API 에서 품목조회 완료 업데이트 인터페이스 ID
    private static final String ITEM_UPDATE_INTERFACE_ID = "IF_GOODS_UPD";

    // ERP API 에서 올가공급체 발주 정보 조회
    private static final String ITEM_ORGA_PO_INTERFACE_ID = "IF_PURCHASESCH_SRCH";

    /*
     * ERP API 연동 관련 로직 Start
     */

    /**
     * @Desc ( ERP 품목 조회 API ) ERP 품목 검색 팝업에 출력할 데이터를 ERP 연동 API 를 통해 호출/조회한다.
     *
     * @param ErpItemSearchOptionEnum : ERP 품목 검색옵션 Enum
     * @param String                  : 화면에서 전송한 검색값
     * @param String                  : 화면에서 선택한 마스터 품목유형
     *
     * @return ErpItemPopupResponseDto : ERP 연동 API 를 통해 조회된 Data ResponsedTO
     * @throws BaseException
     */
    protected ErpItemPopupResponseDto getErpItemApiList(ErpItemSearchOption searchOptionEnum, String searchValue, MasterItemTypes masterItemType) throws BaseException {

        /*
         * ERP 연동 API 호출 : 현재는 별도 분기 처리 / 필터링 없이 SampleData 전체 조회
         */
        List<ErpIfGoodSearchResponseDto> erpItemApiList = null;

        switch (searchOptionEnum) {

        case SEARCH_BY_CODE: // 품목 코드로 조회시 : searchValue 는 조회할 품목코드 값

            // ERP 연동 API 로 품목코드 검색 전 BOS 상에 이미 등록된 품목코드인지 확인
            if (checkIfRegisterdItemCode(searchValue)) { // 이미 등록된 품목코드인 경우

                MasterItemListVo masterItem = goodsItemRegisterMapper.getMasterItem(searchValue);

                return ErpItemPopupResponseDto.builder() //
                        .isRegisterdItemCode(true) // 이미 등록된 품목 코드
                        .masterItem(masterItem) // 품목 기초 정보
                        .build();

            }

            // 이미 등록된 품목코드가 아닌 경우 ERP API 호출
            erpItemApiList = getErpItemApiListByItemCode(searchValue, masterItemType);

            break;

        case SEARCH_BY_BARCODE: // 품목 바코드로 조회시 : searchValue 는 조회할 품목바코드 값

            // ERP 연동 API 로 품목바코드 검색 전 BOS 상에 이미 등록된 품목바코드인지 확인
            if (checkIfRegisterdItemBarCode(searchValue)) { // 이미 등록된 품목바코드인 경우

                MasterItemListVo masterItem = goodsItemRegisterMapper.getMasterItemBarcode(searchValue);

                return ErpItemPopupResponseDto.builder() //
                        .isRegisterdItemCode(true) // 이미 등록된 품목 바코드
                        .masterItem(masterItem) // 품목 기초 정보
                        .build();
            }

            // 이미 등록된 품목코드가 아닌 경우 ERP API 호출
            erpItemApiList = getErpItemApiListByItemBarCode(searchValue, masterItemType);

            break;
        case SEARCH_BY_NAME: // 품목명으로 조회시
        	erpItemApiList = getErpItemApiListByItemName(searchValue, masterItemType);

        default:

        }

        return ErpItemPopupResponseDto.builder() //
                .erpItemApiList(erpItemApiList) //
                .build();

    }

    /**
     * @Desc ( ERP 품목 조회 API ) 품목정보 ERP API 에서 품목 코드로 ERP 품목 정보 조회
     *
     * @param String : 화면에서 전송한 품목 코드
     * @param String : 화면에서 선택한 마스터 품목유형
     *
     * @return List<ErpIfGoodSearchResponseDto> : ERP 연동 API 를 통해 조회된 품목 목록 : 품목 코드로 조회시 0건 또는 1건 반환
     * @throws BaseException
     */
    public List<ErpIfGoodSearchResponseDto> getErpItemApiListByItemCode(String searchValue, MasterItemTypes masterItemType) throws BaseException {

        List<ErpIfGoodSearchResponseDto> erpItemApiList = null; // 품목코드로 erp api 로 조회한 목록
        List<ErpIfGoodSearchResponseDto> returnItemApiList = null; // 최종 반환할 erp 품목 목록

        Map<String, String> parameterMap = new HashMap<>();

        parameterMap.put("itmNo", searchValue);
        parameterMap.put("updFlg", "Y");

        try {

            // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
            BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.get(parameterMap, ITEM_SEARCH_INTERFACE_ID);

            if (!baseApiResponseVo.isSuccess()) { // ERP API 통신 실패시
                throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
            }

            // baseApiResponseVo => List<T> 역직렬화
            erpItemApiList = baseApiResponseVo.deserialize(ErpIfGoodSearchResponseDto.class);

        } catch (Exception e) { // ERP API 통신 실패 or List<T> 역직렬화 실패시
            throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
        }

        if (erpItemApiList.isEmpty()) { // 데이터 미조회시 : 빈 배열 반환
            return new ArrayList<>();

        } else if (erpItemApiList.size() >= 2) { // 해당 품목 코드로 2건 이상 데이터 조회시 예외 처리
            throw new BaseException(ItemEnums.Item.MORE_THAN_TWO_DATA_SEARCHED_BY_ITEM_CODE);

        }

        if (!erpItemApiList.isEmpty()) { // 조회 결과 존재시 : 반드시 1건 조회되어야 함

            // 화면에서 선택한 마스터 품목유형 / ERP 법인 유형별 품목 데이터 가공
            returnItemApiList = processErpItemApiList(erpItemApiList, masterItemType);

            // ERP 조회결과에서 가격정보 조회
            returnItemApiList = processErpItemPriceaApiList(returnItemApiList, masterItemType);

            // getErpItemApiList 에서 기등록된 품목코드가 아닌 경우에만 현재 메서드 호출 => 등록 가능여부 true 로 지정
//            if (!returnItemApiList.isEmpty()) {
//                returnItemApiList.get(0).setRegistrationAvailable(true);
//            }

        }

        return returnItemApiList;

    }

    /**
     * @Desc ( ERP 품목 조회 API ) 품목정보 ERP API 에서 품목 코드로 ERP 품목 정보 조회
     *
     * @param String : 화면에서 전송한 품목 바코드
     * @param String : 화면에서 선택한 마스터 품목유형
     *
     * @return List<ErpIfGoodSearchResponseDto> : ERP 연동 API 를 통해 조회된 품목 목록 : 품목 코드로 조회시 0건 또는 1건 반환
     * @throws BaseException
     */
    private List<ErpIfGoodSearchResponseDto> getErpItemApiListByItemBarCode(String searchValue, MasterItemTypes masterItemType) throws BaseException {

        List<ErpIfGoodSearchResponseDto> erpItemApiList = null; // 품목코드로 erp api 로 조회한 목록
        List<ErpIfGoodSearchResponseDto> returnItemApiList = null; // 최종 반환할 erp 품목 목록

        Map<String, String> parameterMap = new HashMap<>();

        parameterMap.put("kanCd", searchValue);
        parameterMap.put("updFlg", "Y");

        try {

            // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
            BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.get(parameterMap, ITEM_SEARCH_INTERFACE_ID);

            if (!baseApiResponseVo.isSuccess()) { // ERP API 통신 실패시
                throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
            }

            // baseApiResponseVo => List<T> 역직렬화
            erpItemApiList = baseApiResponseVo.deserialize(ErpIfGoodSearchResponseDto.class);

        } catch (Exception e) { // ERP API 통신 실패 or List<T> 역직렬화 실패시
            throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
        }

        if (erpItemApiList.isEmpty()) { // 데이터 미조회시 : 빈 배열 반환
            return new ArrayList<>();

        } else if (erpItemApiList.size() >= 2) { // 해당 품목 코드로 2건 이상 데이터 조회시 예외 처리
            throw new BaseException(ItemEnums.Item.MORE_THAN_TWO_DATA_SEARCHED_BY_ITEM_CODE);

        }

        if (!erpItemApiList.isEmpty()) { // 조회 결과 존재시 : 반드시 1건 조회되어야 함

            // 화면에서 선택한 마스터 품목유형 / ERP 법인 유형별 품목 데이터 가공
            returnItemApiList = processErpItemApiList(erpItemApiList, masterItemType);

            // ERP 조회결과에서 가격정보 조회
            returnItemApiList = processErpItemPriceaApiList(returnItemApiList, masterItemType);

            // getErpItemApiList 에서 기등록된 품목코드가 아닌 경우에만 현재 메서드 호출 => 등록 가능여부 true 로 지정
//            if (!returnItemApiList.isEmpty()) {
//                returnItemApiList.get(0).setRegistrationAvailable(true);
//            }

        }

        return returnItemApiList;

    }

    /**
     * @Desc ( ERP 품목 조회 API ) 품목정보 ERP API 에서 품목명으로 ERP 품목 정보 조회
     *
     * @param String : 화면에서 전송한 품목명 ( 3글자 이상, 양방향 검색 )
     * @param String : 화면에서 선택한 마스터 품목유형
     *
     * @return List<ErpIfGoodSearchResponseDto> : ERP 연동 API 를 통해 조회된 품목 목록
     * @throws BaseException
     */
    private List<ErpIfGoodSearchResponseDto> getErpItemApiListByItemName(String searchValue, MasterItemTypes masterItemType) throws BaseException {

        Map<String, String> parameterMap = new HashMap<>();

        parameterMap.put("itmNam", "%" + searchValue + "%"); // 품목명 조회 조건 추가 : 양방향 검색
        parameterMap.put("updFlg", "Y");

        /*
         * 품목명으로 조회시 여러 페이지가 검색될 수 있음 : 최초 1페이지 조회 후 전체 페이지가 1 보다 큰 경우 각 페이지별로 추가 조회
         */
        BaseApiResponseVo baseApiResponseVo = null;

        List<ErpIfGoodSearchResponseDto> eachPageDtoList = null; // 각 페이지별 품목 dto 목록
        List<ErpIfGoodSearchResponseDto> erpItemApiList = new ArrayList<>(); // 전체 취합된 품목 dto 목록
        List<ErpIfGoodSearchResponseDto> returnItemApiList = null; // 최종 반환할 erp 품목 목록

        try {

            // 최초 1페이지 조회
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, ITEM_SEARCH_INTERFACE_ID); // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체

            if (!baseApiResponseVo.isSuccess()) { // 최초 1페이지 조회에서 ERP API 통신 실패시
                throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
            }

            // 최초 1페이지는 전체 데이터 취합 목록에 바로 반영
            eachPageDtoList = baseApiResponseVo.deserialize(ErpIfGoodSearchResponseDto.class); // baseApiResponseVo => List<T> 역직렬화
            erpItemApiList.addAll(eachPageDtoList);

            if (erpItemApiList.isEmpty()) { // 해당 품목명으로 데이터 미조회시 빈 배열 반환
                return new ArrayList<>();
            }

            if (baseApiResponseVo.getTotalPage() != null && baseApiResponseVo.getTotalPage() > 1) { // 전체 페이지 수가 1 보다 큰 경우

                for (int page = 2; page <= baseApiResponseVo.getTotalPage(); page++) { // 2 페이지부터 전체 페이지 수까지 추가 조회

                    parameterMap.put("page", String.valueOf(page)); // 페이지 조회조건 추가

                    baseApiResponseVo = erpApiExchangeService.get(parameterMap, ITEM_SEARCH_INTERFACE_ID);

                    if (!baseApiResponseVo.isSuccess()) { // 각 페이지별 조회에서 ERP API 통신 실패시
                        throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
                    }

                    eachPageDtoList = baseApiResponseVo.deserialize(ErpIfGoodSearchResponseDto.class); // 각 페이지별 dto 변환
                    erpItemApiList.addAll(eachPageDtoList); // 전체 품목 dto 목록에 취합

                }

            }

        } catch (Exception e) { // ERP API 통신 실패 or List<T> 역직렬화 실패시
            throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);

        }

        /*
         * 화면에서 선택한 마스터 품목유형 / ERP 법인 유형별 품목 데이터 가공
         */
        returnItemApiList = processErpItemApiList(erpItemApiList, masterItemType);

        returnItemApiList = processErpItemPriceaApiList(returnItemApiList, masterItemType);

        if (returnItemApiList.isEmpty()) { // 조회 / 가공 결과 없는 경우 빈 배열 반환
            return new ArrayList<>();
        }

        Collections.sort(returnItemApiList, (a, b) -> a.getErpItemNo().compareTo(b.getErpItemNo())); // 품목코드 오름차순 정렬

        /*
         * BOS 상에 등록된 품목 코드인 경우 등록 가능여부 false ("선택 불가") 처리
         */

        // API 로 조회한 ERP 품목 코드 목록을 List<String> 으로 추출
        Set<String> ilItemCodeSet = new HashSet<>(); // 품목코드 중복 제거
        List<String> ilItemCodeList = new ArrayList<>();

        for (ErpIfGoodSearchResponseDto erpIfGoodSearchResponseDto : returnItemApiList) {
            ilItemCodeSet.add(erpIfGoodSearchResponseDto.getErpItemNo());
        }

        ilItemCodeList.addAll(ilItemCodeSet);

        // 조회한 ERP 품목 코드 목록 중에서 BOS 상에 등록된 품목코드 목록 조회
        List<ErpLinkMasterItemRegisterVo> registeredIlItemCodeList = goodsItemRegisterMapper.getRegisteredIlItemCodeList(ilItemCodeList);
        ErpIfGoodSearchResponseDto erpIfGoodSearchResponseDto = null;

        for (int i = 0; i < returnItemApiList.size(); i++) {

            erpIfGoodSearchResponseDto = returnItemApiList.get(i);

            // 이미 등록된 품목코드인 경우 : 등록 가능여부 false ("선택 불가")
            // 미등록된 품목코드인 경우 : 등록 가능여부 true ("선택"))
            for(int j = 0; j < registeredIlItemCodeList.size(); j++) {
            	ErpLinkMasterItemRegisterVo itemRegisterVo = registeredIlItemCodeList.get(j);

            	if(erpIfGoodSearchResponseDto.getErpItemNo().equals(itemRegisterVo.getItemCd())) {
            		erpIfGoodSearchResponseDto.setRegistrationAvailable(false);
            		erpIfGoodSearchResponseDto.setRegisterationAvailableMsg(Item.ITEM_REGIST.getMessage());
            		break;
            	}
            }

//            boolean isRegistrationAvailable = !registeredIlItemCodeList.contains(erpIfGoodSearchResponseDto.getErpItemNo());
//            erpIfGoodSearchResponseDto.setRegistrationAvailable(isRegistrationAvailable);

        }

        return returnItemApiList;

    }

    /**
     * @Desc ( ERP 품목 조회 API ) 품목정보 ERP API 에서 올가매장상품 ERP 품목 정보 조회
     *
     * @param String : 화면에서 전송한 품목 코드
     * @param String : 화면에서 선택한 마스터 품목유형
     *
     * @return List<ErpIfGoodSearchResponseDto> : ERP 연동 API 를 통해 조회된 품목 목록 : 품목 코드로 조회시 0건 또는 1건 반환
     * @throws BaseException
     */
    public List<ErpIfOrgaShopGoodsSearchResponseDto> getErpItemOrgaShopApiListByItemCode(String searchValue, MasterItemTypes masterItemType) throws BaseException {

    	List<ErpIfOrgaShopGoodsSearchResponseDto> eachPageDtoList = null; // 각 페이지별 품목 dto 목록
        List<ErpIfOrgaShopGoodsSearchResponseDto> erpItemApiList = new ArrayList<>(); // 전체 취합된 품목 dto 목록
        List<ErpIfOrgaShopGoodsSearchResponseDto> returnItemApiList = null; // 최종 반환할 erp 품목 목록

        BaseApiResponseVo baseApiResponseVo = null;

        Map<String, String> parameterMap = new HashMap<>();

//        parameterMap.put("itmNo", searchValue);
//        parameterMap.put("updFlg", "Y");

        try {

            // 최초 1페이지 조회
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, IF_ORGASHOP_STOCK_SRCH); // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체

            if (!baseApiResponseVo.isSuccess()) { // 최초 1페이지 조회에서 ERP API 통신 실패시
                throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
            }

            // 최초 1페이지는 전체 데이터 취합 목록에 바로 반영
            eachPageDtoList = baseApiResponseVo.deserialize(ErpIfOrgaShopGoodsSearchResponseDto.class); // baseApiResponseVo => List<T> 역직렬화

            erpItemApiList.addAll(eachPageDtoList);

            if (erpItemApiList.isEmpty()) { // 해당 품목명으로 데이터 미조회시 빈 배열 반환
                return new ArrayList<>();
            }

            if (baseApiResponseVo.getTotalPage() != null && baseApiResponseVo.getTotalPage() > 1) { // 전체 페이지 수가 1 보다 큰 경우

                for (int page = 2; page <= baseApiResponseVo.getTotalPage(); page++) { // 2 페이지부터 전체 페이지 수까지 추가 조회

                    parameterMap.put("page", String.valueOf(page)); // 페이지 조회조건 추가

                    baseApiResponseVo = erpApiExchangeService.get(parameterMap, IF_ORGASHOP_STOCK_SRCH);

                    if (!baseApiResponseVo.isSuccess()) { // 각 페이지별 조회에서 ERP API 통신 실패시
                        throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
                    }

                    eachPageDtoList = baseApiResponseVo.deserialize(ErpIfOrgaShopGoodsSearchResponseDto.class); // 각 페이지별 dto 변환
                    erpItemApiList.addAll(eachPageDtoList); // 전체 품목 dto 목록에 취합

                }

            }

        } catch (Exception e) { // ERP API 통신 실패 or List<T> 역직렬화 실패시
            throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);

        }

        returnItemApiList = erpItemApiList;

        return returnItemApiList;

    }

    /**
     * @Desc ( ERP 품목 조회 API ) 품목정보 ERP API 에서 품목명으로 ERP 품목 정보 조회
     *
     * @param String : 화면에서 전송한 품목명 ( 3글자 이상, 양방향 검색 )
     * @param String : 화면에서 선택한 마스터 품목유형
     *
     * @return List<ErpIfGoodSearchResponseDto> : ERP 연동 API 를 통해 조회된 품목 목록
     * @throws BaseException
     */
    public List<ErpIfGoodsOrgaPoResponseDto> getOrgaPoApi(String ilItemCode) throws BaseException {

        Map<String, String> parameterMap = new HashMap<>();

        parameterMap.put("itmNo", ilItemCode); // 품목명 조회 조건 추가 : 양방향 검색

        /*
         * 품목명으로 조회시 여러 페이지가 검색될 수 있음 : 최초 1페이지 조회 후 전체 페이지가 1 보다 큰 경우 각 페이지별로 추가 조회
         */
        BaseApiResponseVo baseApiResponseVo = null;

        List<ErpIfGoodsOrgaPoResponseDto> eachPageDtoList = null; // 각 페이지별 품목 dto 목록
        List<ErpIfGoodsOrgaPoResponseDto> erpItemApiList = new ArrayList<>(); // 전체 취합된 품목 dto 목록

        try {

            // 최초 1페이지 조회
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, ITEM_ORGA_PO_INTERFACE_ID); // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체

            if (!baseApiResponseVo.isSuccess()) { // 최초 1페이지 조회에서 ERP API 통신 실패시
                throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
            }

            // 최초 1페이지는 전체 데이터 취합 목록에 바로 반영
            eachPageDtoList = baseApiResponseVo.deserialize(ErpIfGoodsOrgaPoResponseDto.class); // baseApiResponseVo => List<T> 역직렬화
            erpItemApiList.addAll(eachPageDtoList);

            if (erpItemApiList.isEmpty()) { // 해당 품목명으로 데이터 미조회시 빈 배열 반환
                return new ArrayList<>();
            }

            if (baseApiResponseVo.getTotalPage() != null && baseApiResponseVo.getTotalPage() > 1) { // 전체 페이지 수가 1 보다 큰 경우

                for (int page = 2; page <= baseApiResponseVo.getTotalPage(); page++) { // 2 페이지부터 전체 페이지 수까지 추가 조회

                    parameterMap.put("page", String.valueOf(page)); // 페이지 조회조건 추가

                    baseApiResponseVo = erpApiExchangeService.get(parameterMap, ITEM_SEARCH_INTERFACE_ID);

                    if (!baseApiResponseVo.isSuccess()) { // 각 페이지별 조회에서 ERP API 통신 실패시
                        throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
                    }

                    eachPageDtoList = baseApiResponseVo.deserialize(ErpIfGoodsOrgaPoResponseDto.class); // 각 페이지별 dto 변환
                    erpItemApiList.addAll(eachPageDtoList); // 전체 품목 dto 목록에 취합

                }

            }

        } catch (Exception e) { // ERP API 통신 실패 or List<T> 역직렬화 실패시
            throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);

        }

        return erpItemApiList;

    }

    /**
     * @Desc ( ERP 품목 조회 API ) 품목정보 ERP API 로 조회된 데이터를 해당 공급업체 / 마스터 품목 유형 검색조건 / 해당 ERP 유형별 가공
     *
     * @param List<ErpIfGoodSearchResponseDto> : ERP 연동 API 를 통해 조회된 품목 목록
     *
     * @return List<ErpIfGoodSearchResponseDto> : ERP 유형별 가공된 품목 목록
     */
    private List<ErpIfGoodSearchResponseDto> processErpItemApiList(List<ErpIfGoodSearchResponseDto> erpItemApiList, MasterItemTypes masterItemType) {

        // 품목 데이터 중에서 조회 결과에 포함하지 않아야 되는 항목 1차 삭제 : 마스터 품목 유형 검색 조건과 무관
        removeUnmanagingErpItemInfo(erpItemApiList);

        /*
         * 마스터 품목 유형 / ERP 유형별 가공
         *
         * (1) 공통품목유형 선택시 : 올가 ERP 품목 중 매장전용품목 유형이 사간거래, 손질상품, 직제조일 경우 조회 결과에서 제외
         * => 올가 ERP 품목 중 미대상(일반), 미지정 만 포함, 다른 ERP 품목은 그대로 조회 결과에 포함
         *
         * (2) 매장전용품목유형 선택시 : 올가 ERP 품목 중 매장전용품목 유형이 사간거래, 손질상품, 직제조가 아닌 경우 조회 결과에서 제외
         * => 올가 ERP 품목 중 사간거래, 손질상품, 직제조만 조회되고 다른 ERP 의 품목도 모두 삭제
         *
         * (3) 올가 ERP 의 경우 원산지명이 '001', '002' 로 조회되는 경우 => 원산지 상세명으로 대신 세팅
         *
         */
        List<Integer> indexList = new ArrayList<>(); // 조회 결과에서 제외할 index 목록

        for (int i = erpItemApiList.size() - 1; i >= 0; i--) { // 성능을 위해 목록의 마지막 index 부터 반복문 시작

            ErpIfGoodSearchResponseDto erpIfGoodSearchResponseDto = erpItemApiList.get(i);

            erpIfGoodSearchResponseDto.setRegistrationAvailable(true);

            // 해당 ERP 연동 품목 정보 추가 가공 : 올가 ERP 의 경우 원산지명이 '001', '002' 로 조회되는 경우 => 원산지 상세명으로 대신 세팅
            processErpItemInfo(erpIfGoodSearchResponseDto);

            // 공통품목유형 or 매장전용품목유형 선택시 : 올가 ERP 품목 중 매장전용품목 유형에 따른 조회 결과 포함 / 제외

            if ((masterItemType == MasterItemTypes.COMMON || masterItemType == MasterItemTypes.SHOP_ONLY) //
                    && erpIfGoodSearchResponseDto.getErpLegalType() == LegalTypes.ORGA) {

                if (erpIfGoodSearchResponseDto.getO2oExposureType() == null) {

                    indexList.add(i); // 해당 품목 index 를 조회 제외할 목록에 추가


                    erpIfGoodSearchResponseDto.setRegistrationAvailable(false);
                    erpIfGoodSearchResponseDto.setRegisterationAvailableMsg(Item.ITEM_O2O_EMPTY.getMessage());
                }else {
                	erpIfGoodSearchResponseDto.setRegistrationAvailable(true);
                }

                switch (erpIfGoodSearchResponseDto.getO2oExposureType()) {
                case O2O_01: // 저울코드(손질)
                case O2O_02: // 직제조(FRM)
                case O2O_03: // 사간거래

                    // 공통품목유형 선택시 : 올가 ERP 품목 중 매장전용품목 유형이 사간거래, 손질상품, 직제조일 경우 조회 결과에서 제외
                    // => 올가 ERP 품목 중 미대상(일반), 미지정만 조회
                    if (masterItemType == MasterItemTypes.COMMON) {

                        indexList.add(i); // 해당 품목 index 를 조회 제외할 목록에 추가

                        erpIfGoodSearchResponseDto.setRegistrationAvailable(false);
                        erpIfGoodSearchResponseDto.setRegisterationAvailableMsg(Item.ITEM_SHOPONLY.getMessage());
                    }else {
                    	erpIfGoodSearchResponseDto.setRegistrationAvailable(true);
                    }

                    break;

                case O2O_04: // 미대상(일반)
                    if (masterItemType == MasterItemTypes.SHOP_ONLY) {
                        erpIfGoodSearchResponseDto.setRegistrationAvailable(false);
                        erpIfGoodSearchResponseDto.setRegisterationAvailableMsg(Item.ITEM_COMMON.getMessage());
                    }
                	break;
                case NONE: // 미지정
                	erpIfGoodSearchResponseDto.setRegistrationAvailable(false);
                    erpIfGoodSearchResponseDto.setRegisterationAvailableMsg(Item.ITEM_020_NONE.getMessage());
                    break;
                default:

                    // 매장전용품목유형 선택시 : 올가 ERP 품목 중 매장전용품목 유형이 사간거래, 손질상품, 직제조가 아닌 경우 조회 결과에서 제외
                    // => 올가 ERP 품목 중 사간거래, 손질상품, 직제조만 조회
                    if (masterItemType == MasterItemTypes.SHOP_ONLY) {

                        indexList.add(i); // 해당 품목 index 를 조회 제외할 목록에 추가

                        erpIfGoodSearchResponseDto.setRegistrationAvailable(false);
                        erpIfGoodSearchResponseDto.setRegisterationAvailableMsg(Item.ITEM_COMMON.getMessage());
                    }else {
                    	erpIfGoodSearchResponseDto.setRegistrationAvailable(true);
                    }

                    break;
                }

            }

            /*
             * 매장전용품목유형 선택시에는 올가 ERP 품목 중 사간거래, 손질상품, 직제조만 조회되어야 함 => 다른 ERP 품목 유형은 모두 조회결과에서 제외
             */
            if (masterItemType == MasterItemTypes.SHOP_ONLY && erpIfGoodSearchResponseDto.getErpLegalType() != LegalTypes.ORGA) {

                indexList.add(i); // 해당 품목 index 를 조회 제외할 목록에 추가
                erpIfGoodSearchResponseDto.setRegistrationAvailable(false);
                erpIfGoodSearchResponseDto.setRegisterationAvailableMsg(Item.ITEM_COMMON.getMessage());
            }



        }

//        for (int index : indexList) { // 조회 제외 목록 indexList 에 취합된 index 의 품목 정보 삭제
//            erpItemApiList.remove(index);
//        }

        return erpItemApiList;

    }

    /**
     * @Desc ( ERP 품목 조회 API ) 가격정보
     *
     * @param List<ErpIfGoodSearchResponseDto> : ERP 연동 API 를 통해 조회된 품목 목록
     *
     * @return List<ErpIfGoodSearchResponseDto> : ERP 유형별 가공된 품목 목록
     */
    public List<ErpIfGoodSearchResponseDto> processErpItemPriceaApiList(List<ErpIfGoodSearchResponseDto> erpItemApiList, MasterItemTypes masterItemType) {

    	List<ErpIfPriceSearchResponseDto> erpIfPriceSearchResponseDtoList = null;

        for (int i = erpItemApiList.size() - 1; i >= 0; i--) { // 성능을 위해 목록의 마지막 index 부터 반복문 시작

        	ErpIfGoodSearchResponseDto erpIfGoodSearchResponseDto = erpItemApiList.get(i);

        	String ilItemCode = erpIfGoodSearchResponseDto.getErpItemNo();
        	String erpLegalTypeCode = erpIfGoodSearchResponseDto.getErpLegalType().getCode();
        	String erpSalesType = SalesTypes.NORMAL.getCode();
        	String supplierType = erpIfGoodSearchResponseDto.getSupplierType().getCode();
        	String productType = erpIfGoodSearchResponseDto.getProductType().getCode();
        	String taxInvoiceType = erpIfGoodSearchResponseDto.getTaxInvoiceType().getCode();

        	try {
				erpIfPriceSearchResponseDtoList = getErpPriceApi(ilItemCode, erpLegalTypeCode, erpSalesType);
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        	if(erpIfPriceSearchResponseDtoList.size() < 1) {
            	// ERP Type = PFF , OGH, FDM 가격정보가 없으면  필수가격정보 누락.
        		// ERP Type = PGS && 상품일경우 가격정보 없으면 필수가격정보 누락.
        		// ERP Type = PGS && 제품이며 공급처가 풀무원녹즙(FDD) 일경우 가격정보 없으면 필수가격정보누락.
        		if(erpLegalTypeCode.equals("PFF") ||  erpLegalTypeCode.equals("OGH")
        				|| erpLegalTypeCode.equals("FDM")
        				||  ( erpLegalTypeCode.equals("PGS") && taxInvoiceType.equals("녹즙정품") && supplierType.equals("4") && productType.equals("상품") )
        				||  ( erpLegalTypeCode.equals("PGS") && taxInvoiceType.equals("이유식") && supplierType.equals("5") && productType.equals("상품") )
        				||  ( erpLegalTypeCode.equals("PGS") && taxInvoiceType.equals("그린체정품") && supplierType.equals("7") && productType.equals("상품") )
        				||  ( erpLegalTypeCode.equals("PGS") && taxInvoiceType.equals("월합") && supplierType.equals("6") && productType.equals("상품") )
        				|| ( erpLegalTypeCode.equals("PGS") && productType.equals("제품") &&  supplierType.equals("4"))  ) {
        			erpIfGoodSearchResponseDto.setRegistrationAvailable(false);
                	erpIfGoodSearchResponseDto.setRegisterationAvailableMsg(Item.ITEM_PRICE_EMPTY.getMessage());
        		}
        	}else {
        		for(ErpIfPriceSearchResponseDto priceDto : erpIfPriceSearchResponseDtoList) {
        			if(ilItemCode.equals(priceDto.getErpItemNo())) {
        				DecimalFormat df=new DecimalFormat("#.##");
        				if (priceDto.getErpStandardPrice() != null && priceDto.getErpStandardPrice() != "")
        					erpIfGoodSearchResponseDto.setErpStandardPrice(df.format(Math.floor(Double.parseDouble(priceDto.getErpStandardPrice()))));
        				if (priceDto.getErpRecommendedPrice() != null && priceDto.getErpRecommendedPrice() != "")
        					erpIfGoodSearchResponseDto.setErpRecommendedPrice(df.format(Math.floor(Double.parseDouble(priceDto.getErpRecommendedPrice()))));
            			break;
            		}
        		}


        		if(erpLegalTypeCode.equals("PFF") ||  erpLegalTypeCode.equals("OGH") ) {
        			if(erpIfGoodSearchResponseDto.getErpStandardPrice() == null || erpIfGoodSearchResponseDto.getErpRecommendedPrice() == null) {
        				erpIfGoodSearchResponseDto.setRegistrationAvailable(false);
                    	erpIfGoodSearchResponseDto.setRegisterationAvailableMsg(Item.ITEM_PRICE_EMPTY.getMessage());
        			}
        		}


        		if(erpLegalTypeCode.equals("FDM")
        				||  ( erpLegalTypeCode.equals("PGS") && taxInvoiceType.equals("녹즙정품") && supplierType.equals("4") && productType.equals("상품") )
        				||  ( erpLegalTypeCode.equals("PGS") && taxInvoiceType.equals("이유식") && supplierType.equals("5") && productType.equals("상품") )
        				||  ( erpLegalTypeCode.equals("PGS") && taxInvoiceType.equals("그린체정품") && supplierType.equals("7") && productType.equals("상품") )
        				||  ( erpLegalTypeCode.equals("PGS") && taxInvoiceType.equals("월합") && supplierType.equals("6") && productType.equals("상품") )
        				|| ( erpLegalTypeCode.equals("PGS") && productType.equals("제품") &&  supplierType.equals("4"))) {
        			if(erpIfGoodSearchResponseDto.getErpStandardPrice() == null) {
        				erpIfGoodSearchResponseDto.setRegistrationAvailable(false);
                    	erpIfGoodSearchResponseDto.setRegisterationAvailableMsg(Item.ITEM_PRICE_EMPTY.getMessage());
        			}
        		}
        	}
        }

        return erpItemApiList;
    }



    /**
     * @Desc ( ERP 품목 조회 API ) 마스터 품목 유형 조회 조건에 상관없이 공급업체 미분류 품목 등을 조회 결과에서 삭제
     *
     * @param List<ErpIfGoodSearchResponseDto> : ERP 품목 조회 목록
     */
    private void removeUnmanagingErpItemInfo(List<ErpIfGoodSearchResponseDto> erpItemApiList) {

        List<Integer> indexList = new ArrayList<>(); // 조회 결과에서 제외할 index 목록

        for (int i = erpItemApiList.size() - 1; i >= 0; i--) { // 성능을 위해 목록의 마지막 index 부터 반복문 시작

            ErpIfGoodSearchResponseDto erpIfGoodSearchResponseDto = erpItemApiList.get(i);

            if (erpIfGoodSearchResponseDto.getErpLegalType() == LegalTypes.LOHAS) { // 건강생활 ERP 인 경우

                switch (erpIfGoodSearchResponseDto.getTaxInvoiceType()) {

                case GREEN_JUICE: // 녹즙정품
                case GREENCHE: // 그린체정품
                case CAF: // 월합
                case BABY_FOOD: // 이유식

                    break;

                // 건강생활 ERP 에서 세금계산서 발행구분 (미정의, POS/홈쇼핑, "") 인 품목은 무시
                case NONE: //
                case POS: // POS/홈쇼핑
//                case TASTING: // 시식/시음/기타
                case UNDEFINED: // 미정의

                    indexList.add(i); // 해당 품목 index 를 조회 제외할 목록에 추가

                }

                // 공급업체 미분류 품목인 경우 조회 결과에서 제외 - 푸드머스 추가
            } else if (erpIfGoodSearchResponseDto.getSupplierType() == null || erpIfGoodSearchResponseDto.getSupplierType() == SupplierTypes.NONE || erpIfGoodSearchResponseDto.getSupplierType() == SupplierTypes.FOODMERCE) {

                indexList.add(i); // 해당 품목 index 를 조회 제외할 목록에 추가

            }
        }

        for (int index : indexList) { // 조회 제외 목록 indexList 에 취합된 index 의 품목 정보 삭제
            erpItemApiList.remove(index);
        }
    }

    /**
     * @Desc ( ERP 품목 조회 API ) 해당 ERP 연동 품목 정보 추가 가공
     *
     * @param ErpIfGoodSearchResponseDto : ERP 품목 조회 dto
     */
    private void processErpItemInfo(ErpIfGoodSearchResponseDto erpIfGoodSearchResponseDto) {

        // 올가 ERP 의 경우 원산지명이 '001', '002' 로 조회되는 case 있음 : 원산지 상세명으로 대신 세팅
        if (erpIfGoodSearchResponseDto != null && erpIfGoodSearchResponseDto.getErpOriginName() != null) {

            switch (erpIfGoodSearchResponseDto.getErpOriginName()) {

            case "001":
            case "002":

                erpIfGoodSearchResponseDto.setErpOriginName(erpIfGoodSearchResponseDto.getErpOriginDetailName());

                break;

            default:

            }

        }

    }

    /**
     * @Desc ( ERP 품목 조회 API ) ERP 연동 API 로 품목코드 검색 전 BOS 상에 이미 등록된 품목코드인지 확인
     *
     * @param String : 품목 코드
     *
     * @return boolean : 데이터 1건 조회시 true ( 등록된 품목 코드 ) , 아닌 경우 false ( 미등록 ) 반환
     */
    private boolean checkIfRegisterdItemCode(String ilItemCode) {

        List<String> ilItemCodeList = new ArrayList<>();
        ilItemCodeList.add(ilItemCode);

        // 조회할 품목코드가 BOS 상에 이미 등록된 경우 데이터 1건 조회
        List<ErpLinkMasterItemRegisterVo> registeredIlItemCodeList = goodsItemRegisterMapper.getRegisteredIlItemCodeList(ilItemCodeList);

        // 데이터 1건 조회시 true ( 등록된 품목 코드 ) , 아닌 경우 false ( 미등록 ) 반환
        return (registeredIlItemCodeList.size() == 1);

    }

    /**
     * @Desc ( ERP 품목 조회 API ) ERP 연동 API 로 품목코드 검색 전 BOS 상에 이미 등록된 품목바코드인지 확인
     *
     * @param String : 품목 바코드
     *
     * @return boolean : 데이터 1건 조회시 true ( 등록된 품목 바코드 ) , 아닌 경우 false ( 미등록 ) 반환
     */
    private boolean checkIfRegisterdItemBarCode(String ilItemBarCode) {

        List<String> ilItemBarCodeList = new ArrayList<>();
        ilItemBarCodeList.add(ilItemBarCode);

        // 조회할 품목코드가 BOS 상에 이미 등록된 경우 데이터 1건 조회
        List<ErpLinkMasterItemRegisterVo> registeredIlItemCodeList = goodsItemRegisterMapper.getRegisteredIlItemBarCodeList(ilItemBarCodeList);

        // 데이터 1건 조회시 true ( 등록된 품목 코드 ) , 아닌 경우 false ( 미등록 ) 반환
        return (registeredIlItemCodeList.size() == 1);

    }


    /**
     * @Desc ( ERP 영양정보 조회 API ) ERP API 영양정보 조회 인터페이스 호출
     *
     * @param String : 품목 코드
     *
     * @return ErpIfNutritionSearchResponseDto : ERP API 품목 영양 정보 조회 dto
     * @throws BaseException
     */
    protected ErpIfNutritionSearchResponseDto getErpNutritionApi(String ilItemCode) throws BaseException {

        BaseApiResponseVo baseApiResponseVo = null;
        List<ErpIfNutritionSearchResponseDto> erpIfNutritionSearchResponseDtoList = null;

        Map<String, String> parameterMap = new HashMap<>();

        parameterMap.put("itmNo", ilItemCode); // 품목 코드
        parameterMap.put("useSysCd", "Y");

        try {

            // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, NUTRITION_SEARCH_INTERFACE_ID);

            if (!baseApiResponseVo.isSuccess()) { // ERP API 통신 실패시
                throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
            }

            // baseApiResponseVo => List<T> 역직렬화
            erpIfNutritionSearchResponseDtoList = baseApiResponseVo.deserialize(ErpIfNutritionSearchResponseDto.class);

        } catch (Exception e) { // ERP API 통신 실패 or List<T> 역직렬화 실패시
            throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
        }

        if (erpIfNutritionSearchResponseDtoList.size() == 1) { // 정상적으로 한 건 조회시
            return erpIfNutritionSearchResponseDtoList.get(0);

        } else if (erpIfNutritionSearchResponseDtoList.isEmpty()) { // 0 건 조회시
            return new ErpIfNutritionSearchResponseDto(); // 0건 조회시 값 없는 dto 반환

        } else { // 해당 품목 코드로 2건 이상 데이터 조회시 예외 처리
            throw new BaseException(ItemEnums.Item.MORE_THAN_TWO_DATA_SEARCHED_BY_ITEM_CODE);

        }

    }

    /**
     * @Desc ( ERP 가격정보 조회 API ) ERP API 가격정보 조회 인터페이스 호출
     *
     * @param String : 품목 코드
     * @param String : ERP 법인코드 ( "PFF" ( 풀무원식품 ), "OGH" ( 올가 ), "FDM" ( 푸드머스 ), "PGS" ( 건강생활 ) )
     * @param String : ERP 행사구분 ( "정상", "행사" )
     *
     * @return List<ErpIfPriceSearchResponseDto> : ERP API 품목 가격 정보 조회 목록
     * @throws BaseException
     */
    protected List<ErpIfPriceSearchResponseDto> getErpPriceApi(String ilItemCode, String erpLegalTypeCode, String erpSalesType) throws BaseException {

        // 서울 기준 현재 날짜 ( yyyyMMdd ) 계산 : 날짜 비교를 쉽게 하기 위해 int 변환
        int currentDate = Integer.parseInt(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyyMMdd")));

        BaseApiResponseVo baseApiResponseVo = null;

        List<ErpIfPriceSearchResponseDto> eachPageList = null;
        List<ErpIfPriceSearchResponseDto> erpIfPriceSearchResponseDtoList = new ArrayList<>();

        Map<String, String> parameterMap = new HashMap<>();

        parameterMap.put("itmNo", ilItemCode);
        parameterMap.put("useSysCd", "Y");
        parameterMap.put("_order", "header-stDat-asc"); // 올가 ERP : 가격적용 시작일 오름차순 정렬

        if (erpLegalTypeCode != null) { // ERP 법인 코드
            parameterMap.put("legCd", erpLegalTypeCode);
        }

        if (erpSalesType != null) { // ERP 행사구분
            parameterMap.put("salTyp", erpSalesType);
        }

        try {

            // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, PRICE_SEARCH_INTERFACE_ID);

            if (!baseApiResponseVo.isSuccess()) { // ERP API 통신 실패시
                throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
            }

            eachPageList = baseApiResponseVo.deserialize(ErpIfPriceSearchResponseDto.class); // 최초 1페이지 조회
            erpIfPriceSearchResponseDtoList.addAll(eachPageList);

            if (baseApiResponseVo.getTotalPage() != null && baseApiResponseVo.getTotalPage() > 1) { // 2 페이지 이상인 경우 각 페이지별로 추가 호출

                // 최초 조회한 페이지의 다음 페이지부터 조회
                for (int page = 2; page <= baseApiResponseVo.getTotalPage(); page++) {

                    parameterMap.put("page", String.valueOf(page));

                    baseApiResponseVo = erpApiExchangeService.exchange(parameterMap, PRICE_SEARCH_INTERFACE_ID);

                    if (!baseApiResponseVo.isSuccess()) { // ERP API 통신 실패시
                        throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
                    }

                    eachPageList = baseApiResponseVo.deserialize(ErpIfPriceSearchResponseDto.class);


                    erpIfPriceSearchResponseDtoList.addAll(eachPageList);

                }

            }

        } catch (Exception e) { // ERP API 통신 실패 or List<T> 역직렬화 실패시
            throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
        }

        DecimalFormat df=new DecimalFormat("#.##");
        for(ErpIfPriceSearchResponseDto temp : erpIfPriceSearchResponseDtoList) {
        	if (temp.getErpStandardPrice() != null && temp.getErpStandardPrice() != "")
        		temp.setErpStandardPrice(df.format(Math.floor(Double.parseDouble(temp.getErpStandardPrice()))));
        	if (temp.getErpRecommendedPrice() != null && temp.getErpRecommendedPrice() != "")
        		temp.setErpRecommendedPrice(df.format(Math.floor(Double.parseDouble(temp.getErpRecommendedPrice()))));
        }

        // 올가 ERP 품목 : 여러 건 조회되고 가격 적용 시작일, 가격 적용 종료일 조회됨
        if (erpIfPriceSearchResponseDtoList.size() > 1 && erpIfPriceSearchResponseDtoList.get(0).getLegalType() == LegalTypes.ORGA) {

            int erpPriceApplyEndDate = 0; // 가격 적용 종료일
            ErpIfPriceSearchResponseDto erpIfPriceSearchResponseDto = null;

            List<Integer> indexList = new ArrayList<>(); // 조회 결과에서 제외할 index 목록

            for (int i = erpIfPriceSearchResponseDtoList.size() - 1; i >= 0; i--) {

                erpIfPriceSearchResponseDto = erpIfPriceSearchResponseDtoList.get(i);

                // 가격 적용 종료일 : null 인 경우 '29991231' 로 세팅
                erpPriceApplyEndDate = Integer.parseInt((erpIfPriceSearchResponseDto.getErpPriceApplyEndDate() != null ? erpIfPriceSearchResponseDto.getErpPriceApplyEndDate() : "29991231"));

                // 과거 가격정보 이력은 조회결과에서 제외 : 가격 적용 종료일이 현재 일자보다 이전인 데이터는 삭제
                if (erpPriceApplyEndDate < currentDate) {
                    indexList.add(i); // 해당 가격 정보 index 를 조회 제외할 목록에 추가
                }

            }

            for (int index : indexList) { // 조회 제외 목록 indexList 에 취합된 index 의 가격 정보 삭제
                erpIfPriceSearchResponseDtoList.remove(index);
            }

            // 품목 가격정보의 가격적용 시작일 오름차순 정렬
            Collections.sort(erpIfPriceSearchResponseDtoList, (a, b) -> a.getErpPriceApplyStartDate().compareTo(b.getErpPriceApplyStartDate()));

        }

        return erpIfPriceSearchResponseDtoList;

    }

    /**
     * @Desc ( ERP 품목 조회 완료 API ) BOS 상에 ERP 연동 품목 등록 후 ERP API 의 품목 조회 완료 업데이트 API 호출
     *
     *       해당 API 호출 성공시 : 이후 해당 ERP 연동 품목의 ERP 상의 useOshYn ( 온라인 통합몰 취급 상품 여부 ) 값이 "Y" 로 변경됨
     *
     * @param String : BOS 상에 등록한 ERP 연동 품목 코드
     *
     * @throws BaseException
     */
    protected void putErpItemUpdateApi(String ilItemCode) throws BaseException {

        ErpIfGoodsUpdateRequestDto dto = new ErpIfGoodsUpdateRequestDto(); // 품목 조회 완료 업데이트 request dto 생성
        dto.setIlItemCode(ilItemCode); // BOS 상에 등록한 ERP 연동 품목 코드 세팅
        dto.setUseOnlineShopYn("Y");

        List<ErpIfGoodsUpdateRequestDto> paramList = new ArrayList<>(); // 품목 조회 완료 업데이트 request body 에 추가할 paramList 생성
        paramList.add(dto);

        // baseApiResponseVo : restTemplate 으로 해당 API 를 PUT 방식으로 호출 후 받은 응답 결과 객체
        BaseApiResponseVo baseApiResponseVo = erpApiExchangeService.put(paramList, ITEM_UPDATE_INTERFACE_ID);

        if (!baseApiResponseVo.isSuccess()) { // 품목 조회 완료 업데이트 API 호출 실패시 BaseException 처리

            throw new BaseException(ItemEnums.Item.ERP_ITEM_UPDATE_INTERFACE_API_CALL_FAILED);

        }

    }

    /*
     * ERP API 연동 관련 로직 End
     */

    /*
     * 마스터 품목 복사 : ERP 연동 or 미연동 품목의 원본 정보 조회 로직 Start
     */

    /**
     * @Desc ( 마스터 품목 복사 ) EPR 연동 품목의 원본 정보 조회
     *
     * @param String : 품목 코드
     *
     * @return ErpLinkMasterItemVo : EPR 연동 마스터 품목 VO
     */
    protected ErpLinkMasterItemVo getErpLinkMasterItem(String ilItemCode) {

        return goodsItemRegisterMapper.getErpLinkMasterItem(ilItemCode);

    }

    /**
     * @Desc ( 마스터 품목 복사 ) EPR 미연동 품목의 원본 정보 조회
     *
     * @param String : 품목 코드
     *
     * @return ErpLinkMasterItemVo : EPR 미연동 마스터 품목 VO
     */
    protected ErpNotLinkMasterItemVo getErpNotLinkMasterItem(String ilItemCode) {

        return goodsItemRegisterMapper.getErpNotLinkMasterItem(ilItemCode);

    }

    /*
     * 마스터 품목 복사 : ERP 연동 or 미연동 품목의 원본 정보 조회 로직 End
     */

    /*
     * 상품정보제공고시 조회 로직 Start
     */

    /**
     * @Desc ( 상품정보제공고시 조회 ) 상품정보 제공고시 상품군 코드 목록 조회
     *
     * @return List<GetCodeListResultVo> : 상품군 코드 목록
     */
    protected List<GetCodeListResultVo> getItemSpecCode() {

        return goodsItemRegisterMapper.getItemSpecCode();

    }

    /**
     * @Desc ( 상품정보제공고시 조회 ) 상품정보 제공고시 상품군별 분류 항목 전체 조회 => 특수 항목에 대한 세부 메시지 및 수정 가능여부 등 설정
     *
     * @return List<ItemSpecDto> : 상품정보 제공고시 상품군별 분류 항목 전체 조회 목록
     */
    protected List<ItemSpecDto> getItemSpecList() {

        // 상품정보 제공고시 상품군별 분류 항목 전체 조회
        List<ItemSpecVo> getItemSpecList = goodsItemRegisterMapper.getItemSpecList();

        // 상품정보 제공고시 상세 항목 중 "유통년월일 및 제조연월일 (포장 또는 생산연도)", "소비자상담 관련 전화번호" 와 같은
        // 특수 항목에 대한 세부 메시지 설정 && 추가로 Vo => Dto 변환
        return setSpecDetailMessage(getItemSpecList);

    }

    /**
     * @Desc ( 상품정보제공고시 조회 ) 상품정보 제공고시 조회 항목 중 "유통년월일 및 제조연월일 (포장 또는 생산연도)", "소비자상담 관련 전화번호" 와 같은
     *
     *       특수 항목에 대한 세부 메시지 설정 && 추가로 Vo => Dto 변환
     *
     * @return List<ItemSpecDto> : 상품정보 제공고시 분류 / 항목 조회 Dto 목록
     */
    private List<ItemSpecDto> setSpecDetailMessage(List<ItemSpecVo> getItemSpecList) {

        List<ItemSpecDto> getItemSpecDtoList = new ArrayList<>();
        ItemSpecDto getItemSpecDto = null;

        for (ItemSpecVo getItemSpecVo : getItemSpecList) {

            // 해당 항목에 specFieldCode 값 존재시 해당 코드에 따른 상세 기본 메시지 설정
            if (!StringUtil.isEmpty(getItemSpecVo.getSpecFieldCode())) {

                // specFieldCode 값으로 SpecFieldCodeEnum 조회
                SpecFieldCode specFieldCodeEnum = SpecFieldCode.valueOf(getItemSpecVo.getSpecFieldCode());

                // SpecFieldCode enum 에 수정 가능여부 true 로 세팅된 경우
                if (specFieldCodeEnum.isCanModified()) {

                    getItemSpecDto = ItemSpecDto.toDto( //
                            getItemSpecVo // Vo => Dto 변환
                            , specFieldCodeEnum.getSpecFieldDetailMessage() // 기본 상세 메시지
                            , true // 해당 상세 항목은 화면에서 수정 가능
                    );

                } else {

                    /*
                     * "소비자상담 관련 전화번호" 와 같이 화면에서 수정 불가능한 항목인 경우 여기서 일괄 세팅함
                     */
                    if (specFieldCodeEnum == SpecFieldCode.SPEC_FIELD_02) { // 소비자상담 관련 전화번호

                        // UR_COMPANY 테이블에 저장된 고객센터 전화번호 조회
                        String phoneNumber = goodsItemRegisterMapper.getItemSpecFieldFixedMessage("SPEC_FIELD_02");

                        // SpecFieldCode enum 의 getModifiedDetailMessage 메서드가 반환하는 수정 메시지 세팅
                        getItemSpecDto = ItemSpecDto.toDto( //
                                getItemSpecVo // Vo => Dto 변환
                                , specFieldCodeEnum.getModifiedDetailMessage(Arrays.asList(String.valueOf(phoneNumber))) //
                                , false // 해당 상세 항목은 화면에서 수정 불가 : 해당 input 자체를 비활성화함
                        );

                    }

                }

            } else { // 해당 항목에 specFieldCode 값 없는 경우 : Vo => Dto 변환만 수행

                getItemSpecDto = ItemSpecDto.toDto(getItemSpecVo, null, true);

            }

            getItemSpecDtoList.add(getItemSpecDto);

        }

        return getItemSpecDtoList;

    }

    /**
     * @Desc ( 상품정보제공고시 조회 ) 해당 상품정보제공고시 항목 코드의 수정 메시지 조회
     *
     * @param ItemSpecFieldModifiedMessageRequestDto : 해당 상품정보제공고시 항목 코드의 수정 메시지 조회 Request dto
     *
     * @return String : 해당 상품정보제공고시 항목 코드의 수정 메시지
     */
    protected String getItemSpecFieldDetailMessage(ItemSpecFieldModifiedMessageRequestDto itemSpecFieldModifiedMessageRequestDto) {

        // specFieldCode 값으로 SpecFieldCodeEnum 조회
        SpecFieldCode specFieldCodeEnum = SpecFieldCode.valueOf(itemSpecFieldModifiedMessageRequestDto.getSpecFieldCode());

        String modifiedMessage = null; // 수정 메시지

        /*
         * SpecFieldCode enum 에 정의된 수정 가능 항목들에 따라 수정 메시지 조회
         */
        if (specFieldCodeEnum == SpecFieldCode.SPEC_FIELD_01) { // "제조연월일/유통기한 또는 품질유지기한"

            // 공급업체 PK, 유통기한으로 해당 품목의 출고기한 조회
            Integer itemDelivery = goodsItemRegisterMapper.getItemDeliveryBySupplierAndDistributionPeriod( //
                    itemSpecFieldModifiedMessageRequestDto.getUrSupplierId() // 화면에서 선택한 공급업체 PK
                    , itemSpecFieldModifiedMessageRequestDto.getDistributionPeriod() // 화면에서 입력한 유통기한
            );

            if (itemDelivery != null) { // 출고기한 조회시

                // "제조연월일/유통기한 또는 품질유지기한" 수정 메시지 생성 : 출고기한 반영
                modifiedMessage = specFieldCodeEnum.getModifiedDetailMessage(Arrays.asList(String.valueOf(itemDelivery)))
                        + " / 유통기한 : " + itemSpecFieldModifiedMessageRequestDto.getDistributionPeriod() + "일";

            } else { // 출고기한 미조회시

                modifiedMessage = specFieldCodeEnum.getSpecFieldDetailMessage(); // 기본 상세 메시지

            }

        }

        return modifiedMessage;

    }

    /**
     * @Desc ( 상품정보제공고시 조회 ) 품목별 상품정보제공고시 세부 항목 조회
     *
     * @param String : 품목 코드
     *
     * @return List<GetItemSpecValueVo> : 품목별 상품정보제공고시 세부 항목 목록
     */
    protected List<ItemSpecValueVo> getItemSpecValueList(String ilItemCode, String ilItemApprId) {

    	if (ilItemApprId != null && !"".equals(ilItemApprId)) { // 승인 요청 내역의 품목 상품 인증정보 리스트

    	    List<ItemSpecValueVo> itemSpecValueList = new ArrayList<>();
    	    List<ItemSpecValueVo> itemApprSpecValueList = new ArrayList<>();

    	    itemSpecValueList = goodsItemRegisterMapper.getItemSpecValueList(ilItemCode);           // 품목별 상품정보제공고시 세부 항목
    	    itemApprSpecValueList = goodsItemRegisterMapper.getItemApprSpecValueList(ilItemApprId); // 품목 승인 요청 상품정보제공고시 세부 항목

            if(itemSpecValueList != null && itemApprSpecValueList != null && itemApprSpecValueList.size() > 0) {
                // 리스트 크기가 다를 시 true로
                if(itemSpecValueList.size() != itemApprSpecValueList.size()) {
                    for (ItemSpecValueVo apprVo : itemApprSpecValueList) {
                        apprVo.setChangedSpecValue(true);
                    }
                }

                for (ItemSpecValueVo apprVo : itemApprSpecValueList) {
                    for(ItemSpecValueVo originVo : itemSpecValueList){
                        // 상품정보제공고시항목 PK가 같을 때
                        if(apprVo.getIlSpecFieldId().equals(originVo.getIlSpecFieldId())) {
                            if(!StringUtil.nvl(apprVo.getSpecFieldValue()).equals(StringUtil.nvl(originVo.getSpecFieldValue()))) { //상품정보제공고시 상세 항목 정보
                                apprVo.setChangedSpecValue(true);
                            } else {
                                apprVo.setChangedSpecValue(false);
                            }
                        }
                    }
                }
            }

    		return itemApprSpecValueList;
    	}
    	else {
    		return goodsItemRegisterMapper.getItemSpecValueList(ilItemCode);
    	}
    }

    /**
     * @Desc ( 상품정보제공고시 조회 ) 품목별 상품정보제공고시 세부 항목  승인요청 항목과 비교
     *
     * @param String : 품목 코드
     *
     * @return List<GetItemSpecValueVo> : 품목별 상품정보제공고시 세부 항목 목록
     */
    protected boolean compareItemSpecValueList(String ilItemCode, String ilItemApprId) {

        boolean changedSpecMaster = false;

    	if (ilItemApprId != null && !"".equals(ilItemApprId)) { // 승인 요청 내역의 품목 상품 인증정보 리스트
    	    List<ItemSpecValueVo> itemSpecValueList = new ArrayList<>();
    	    List<ItemSpecValueVo> itemApprSpecValueList = new ArrayList<>();

    	    itemSpecValueList = goodsItemRegisterMapper.getItemSpecValueList(ilItemCode);           // 품목별 상품정보제공고시 세부 항목
    	    itemApprSpecValueList = goodsItemRegisterMapper.getItemApprSpecValueList(ilItemApprId); // 품목 승인 요청 상품정보제공고시 세부 항목

    	    if(itemSpecValueList != null && itemApprSpecValueList != null && itemApprSpecValueList.size() > 0) {

                if(itemSpecValueList.size() == itemApprSpecValueList.size()) {
                      for (ItemSpecValueVo apprVo : itemApprSpecValueList) {
                        for(ItemSpecValueVo originVo : itemSpecValueList){
                            // 상품정보제공고시항목 PK가 같을 때
                            if(apprVo.getIlSpecFieldId() == originVo.getIlSpecFieldId()) {
                                if(!StringUtil.nvl(apprVo.getSpecFieldValue()).equals(StringUtil.nvl(originVo.getSpecFieldValue()))) { //상품정보제공고시 상세 항목 정보
                                  changedSpecMaster = true;
                                }
                            }
                        }
                    }
                } else {
                   changedSpecMaster = true;
                }
            }
    	}
    	return changedSpecMaster;

    }


    /*
     * 상품정보제공고시 조회 로직 End
     */

    /*
     * 상품 영양정보 조회 로직 Start
     */

    /**
     * @Desc ( 상품 영양정보 조회 ) 등록 가능한 영양정보 분류 코드 조회
     *
     * @return List<AvailableNutritionVo> 영양정보 분류코드 목록
     */
    protected List<AvailableNutritionVo> getAddAvailableNutritionCodeList() {

        return goodsItemRegisterMapper.getAddAvailableNutritionCodeList();

    }

    /**
     * @Desc ( 상품 영양정보 조회 ) 해당 품목코드로 BOS 상에 등록된 영양정보 세부항목 조회
     *
     * @param String : 품목 코드
     *
     * @return List<ItemNutritionDetailVo> 해당 품목코드로 BOS 상에 등록된 영양정보 세부항목 목록
     */
    protected List<ItemNutritionDetailVo> getItemNutritionDetailList(String ilItemCode, String ilItemApprId) {

    	if (ilItemApprId != null && !"".equals(ilItemApprId)) { // 승인 요청 내역의 상품 영양정보 리스트

    	    List<ItemNutritionDetailVo> itemNutritionDetailList = new ArrayList<>();
    	    List<ItemNutritionDetailVo> itemApprNutritionDetailList = new ArrayList<>();

    	    itemNutritionDetailList = goodsItemRegisterMapper.getItemNutritionDetailList(ilItemCode);           // 상품 영양정보 세부 항목
    	    itemApprNutritionDetailList = goodsItemRegisterMapper.getItemApprNutritionDetailList(ilItemApprId); // 품목 승인 요청 상품 영양정보 세부 항목
            List<String> nutritionCodeList = itemNutritionDetailList.stream()
                    .map(ItemNutritionDetailVo::getNutritionCode)
                    .collect(Collectors.toList());

            if(itemNutritionDetailList != null && itemApprNutritionDetailList != null && itemApprNutritionDetailList.size() > 0) {
                // 리스트 크기가 다를 시 true로
                if(itemNutritionDetailList.size() != itemApprNutritionDetailList.size()) {
                    for (ItemNutritionDetailVo apprVo : itemApprNutritionDetailList) {
                        boolean nutritionCodeCheck = false;
                        if(!nutritionCodeList.contains(apprVo.getNutritionCode())){
                            nutritionCodeCheck = true;
                        }
                        apprVo.setChangedNutritionCode(nutritionCodeCheck);
                    }
                }

                for (ItemNutritionDetailVo apprVo : itemApprNutritionDetailList) {
                    for(ItemNutritionDetailVo originVo : itemNutritionDetailList){
                        // 영양정보 분류코드 PK가 같을 때
                        if (StringUtil.nvl(originVo.getNutritionCode()).equals(StringUtil.nvl(apprVo.getNutritionCode()))) {
                            apprVo.setChangedNutritionCode(false);
                            
                            if((originVo.getNutritionQuantity() == null && apprVo.getNutritionQuantity() != null)
                                || (originVo.getNutritionQuantity() != null  && apprVo.getNutritionQuantity() == null)
                                || (originVo.getNutritionQuantity() != null && apprVo.getNutritionQuantity() != null)
                                                                            && Double.compare(originVo.getNutritionQuantity(), apprVo.getNutritionQuantity()) != 0) { // BOS 영양성분량
                                  apprVo.setChangedNutritionCode(true);
                            } else if((originVo.getNutritionPercent() == null && apprVo.getNutritionPercent() != null)
                                    || (originVo.getNutritionPercent() != null  && apprVo.getNutritionPercent() == null)
                                    || (originVo.getNutritionPercent() != null && apprVo.getNutritionPercent() != null)
                                                                                && Double.compare(originVo.getNutritionPercent(), apprVo.getNutritionPercent()) != 0) { // BOS 영양성분 기준치대비 함량
                                apprVo.setChangedNutritionCode(true);
                            }

                        }
                    }
                }
            }

    		return itemApprNutritionDetailList;
    	}
    	else {
    		return goodsItemRegisterMapper.getItemNutritionDetailList(ilItemCode);
    	}

    }
    
      /**
     * @Desc ( 상품 영양정보 조회 ) 해당 품목코드로 BOS 상에 등록된 영양정보 세부항목 승인요청 세부항목과 비교
     *
     * @param String : 품목 코드
     *
     * @return List<ItemNutritionDetailVo> 해당 품목코드로 BOS 상에 등록된 영양정보 세부항목 목록
     */
    protected boolean compareItemNutritionDetailList(String ilItemCode, String ilItemApprId) {

    	boolean changedItemNutrition = false;

    	if (ilItemApprId != null && !"".equals(ilItemApprId)) { // 승인 요청 내역의 품목 이미지 리스트
    	    List<ItemNutritionDetailVo>  itemNutritionDetailList = goodsItemRegisterMapper.getItemNutritionDetailList(ilItemCode);          // 영양정보 세부항목
    	    List<ItemNutritionDetailVo> itemApprNutritionDetailList = goodsItemRegisterMapper.getItemApprNutritionDetailList(ilItemApprId); // 품목승인 영양정보 세부항목

   		if(itemNutritionDetailList != null && itemApprNutritionDetailList != null && itemApprNutritionDetailList.size() > 0) {

                if((itemNutritionDetailList.size() == itemApprNutritionDetailList.size())) {
                      for (ItemNutritionDetailVo apprVo : itemApprNutritionDetailList) {
                        for(ItemNutritionDetailVo originVo : itemNutritionDetailList){
                            // 영양정보 분류코드 PK가 같을 때
                            if(!StringUtil.nvl(apprVo.getNutritionCode()).equals(StringUtil.nvl(originVo.getNutritionCode()))) {

                                if((originVo.getNutritionPercent() == null && apprVo.getNutritionPercent() != null)
                                        || (originVo.getNutritionPercent() != null  && apprVo.getNutritionPercent() == null)
                                            || (originVo.getNutritionPercent() != null) && apprVo.getNutritionPercent() != null
                                                                                        && Double.compare(originVo.getNutritionPercent(), apprVo.getNutritionPercent()) != 0) { // BOS 영양성분 기준치대비 함량
                                     changedItemNutrition = true;
                                }

                                if((originVo.getNutritionQuantity() == null && apprVo.getNutritionQuantity() != null)
                                        || (originVo.getNutritionQuantity() != null  && apprVo.getNutritionQuantity() == null)
                                            || (originVo.getNutritionQuantity() != null) && apprVo.getNutritionQuantity() != null
                                                                                        && Double.compare(originVo.getNutritionQuantity(), apprVo.getNutritionQuantity()) != 0) { // BOS 영양성분량
                                     changedItemNutrition = true;
                                }

                            }

                        }
                    }
                } else {
                   changedItemNutrition = true;
                }
            }
    	}
    	return changedItemNutrition;

    }
    /*
     * 상품 영양정보 조회 로직 End
     */

    /*
     * 상품 인증정보 조회 로직 Start
     */

    /**
     * @Desc ( 상품 인증정보 조회 ) 상품 인증정보 코드 조회
     *
     * @return List<ItemCertificationCodeVo> 상품 인증정보 코드 목록
     */
    protected List<ItemCertificationCodeVo> getItemCertificationCode() {

        return goodsItemRegisterMapper.getItemCertificationCode();

    }

    /**
     * @Desc ( 상품 인증정보 조회 ) 품목별 상품 인증정보 목록 조회
     *
     * @param String : 품목 코드
     * @param String : 품목 승인 코드
     *
     * @return List<ItemCertificationListVo> 품목별 상품 인증정보 목록
     */
    protected List<ItemCertificationListVo> getItemCertificationList(String ilItemCode, String ilItemApprId) {

    	if (ilItemApprId != null && !"".equals(ilItemApprId)) { // 승인 요청 내역의 품목 상품 인증정보 리스트
    		return goodsItemRegisterMapper.getItemApprCertificationList(ilItemApprId);
    	}
    	else { // 승인요청 내역이 아닌경우, 품목의 상품 인증정보 리스트 return
    		return goodsItemRegisterMapper.getItemCertificationList(ilItemCode);
    	}
    }


    /**
     * @Desc 상품 인증증보 변경여부 확인
     *
     * @param String : 품목 코드
     * @param String : 품목 승인 코드
     *
     * @return  인증정보 변경여부
     */
    protected boolean checkItemCertification(String ilItemCode, String ilItemApprId) {

        List<ItemCertificationListVo> itemApprCertificationList = goodsItemRegisterMapper.getItemApprCertificationList(ilItemApprId);  // 승인 요청 내역의 품목 상품 인증정보 리스트
        List<ItemCertificationListVo> itemCertificationList = goodsItemRegisterMapper.getItemCertificationList(ilItemCode);            // 승인요청 내역이 아닌경우, 품목의 상품 인증정보 리스트

        boolean changedItemCertification = false;

        // 상품 인증 정보
        if(itemApprCertificationList.size() != itemCertificationList.size()) {
            changedItemCertification = true;
        }else {
            for(ItemCertificationListVo certificationVo : itemApprCertificationList) {  //123
                String ilCertificationId = certificationVo.getIlCertificationId();
                boolean isSame = false;

                for(ItemCertificationListVo apprCertificationVo : itemCertificationList) {  // 45
                    String apprIlCertificationId = apprCertificationVo.getIlCertificationId();

                    if(ilCertificationId.equals(apprIlCertificationId) ) {
                        isSame = true;
                        break;
                    }
                }

                if (isSame == false) {
                    changedItemCertification = true;
                    break;
                }
            }
        }

        return changedItemCertification;

    }

    /*
     * 상품 인증정보 조회 로직 End
     */

    /*
     * 공급업체 / 출고처 조회 로직 Start
     */

    /**
     * @Desc ( 공급업체 / 출고처 조회 ) 공급업체 코드 목록 조회 : 공급업체 SupplierTypes Enum 의 code, name 목록을 코드로 반환
     *
     * @return GetCodeListResponseDto : 공급업체 코드 목록 dto
     */
    protected GetCodeListResponseDto getSupplierCodeList() {

        List<GetCodeListResultVo> supplierTypeCodeList = new ArrayList<>();

        for (final SupplierTypes supplierType : SupplierTypes.values()) { // SupplierTypes Enum 반복문 시작

            GetCodeListResultVo getCodeListResultVo = new GetCodeListResultVo();

            getCodeListResultVo.setCode(supplierType.getCode());
            getCodeListResultVo.setName(supplierType.getCodeName());

            supplierTypeCodeList.add(getCodeListResultVo);

        }

        GetCodeListResponseDto getCodeListResponseDto = new GetCodeListResponseDto();
        getCodeListResponseDto.setRows(supplierTypeCodeList);

        return getCodeListResponseDto;

    }

    /**
     * @Desc ( 공급업체 / 출고처 조회 ) 해당 공급업체 PK 값에 해당하는 출고처 그룹, 출고처 코드 정보 조회
     *
     * @param String : 공급업체 PK
     *
     * @return ItemWarehouseCodeReponseDto : 해당 공급업체 PK 값에 해당하는 출고처 그룹, 출고처 코드 정보 Response dto
     */
    protected List<ItemWarehouseCodeVo> getItemWarehouseCode(String urSupplierId, String masterItemType) {

        return goodsItemRegisterMapper.getItemWarehouseCodeList(urSupplierId, masterItemType);

    }

    /**
     * @Desc ( 공급업체 / 출고처 조회 ) 해당 품목코드로 등록된 출고처 정보 조회
     *
     * @param String : 품목 코드
     *
     * @return List<ItemWarehouseVo> : 해당 품목코드로 등록된 출고처 정보 목록
     */
    protected List<ItemWarehouseVo> getItemWarehouseList(String ilItemCode) {

        return goodsItemRegisterMapper.getItemWarehouseList(ilItemCode);

    }

    /*
     * 공급업체 / 출고처 조회 로직 End
     */

    /*
     * 배송 / 발주 정보 조회 로직 Start
     */

    /**
     * @Desc ( 배송 / 발주 정보 조회 ) 해당 공급업체 PK 값에 해당하는 발주유형 정보 조회
     *
     * @param urSupplierId : 공급업체 PK
     *
     * @return List<ItemPoTypeCodeVo> : 해당 공급업체 PK 값에 해당하는 발주유형 목록
     */
    protected List<ItemPoTypeCodeVo> getItemPoTypeCode(String urSupplierId) {
        return goodsItemRegisterMapper.getItemPoTypeCode(urSupplierId);
    }

    /**
     * @Desc ( 배송 / 발주 정보 조회 ) 해당 공급업체 PK 값에 해당하는 발주유형 정보 조회
     *
     * @param urSupplierId : 공급업체 PK
     *
     * @return List<ItemPoTypeCodeVo> : 해당 공급업체 PK 값에 해당하는 발주유형 목록 - 올가
     */
    protected List<ItemPoTypeCodeVo> getItemPoTypeOrgaCode(String urSupplierId) {
        return goodsItemRegisterMapper.getItemPoTypeOrgaCode(urSupplierId);
    }

    /**
     * @Desc ( 배송 / 발주 정보 조회 ) 해당 공급업체 PK 값에 해당하는 발주유형 정보 조회
     *
     * @param urSupplierId : 공급업체 PK
     *
     * @return List<ItemPoTypeCodeVo> : 해당 공급업체 PK 값에 해당하는 발주유형 목록 - 전체조회
     */
    protected List<ItemPoTypeCodeVo> getItemPoTypeAllCode(String urSupplierId) {
        return goodsItemRegisterMapper.getItemPoTypeAllCode(urSupplierId);
    }


    /**
     * @Desc ( 배송 / 발주 정보 조회 ) 해당 품목 정보의 표준 카테고리, 보관방법에 따른 반품 가능여부, 반품 가능기간 계산
     *
     * @param String : 표준 카테고리 PK
     *
     * @param String : 보관방법 ( 공통코드 ERP_STORAGE_TYPE )
     *
     * @return ItemReturnPeriodVo : 반품 가능여부, 반품가능 기간 Vo
     */
    protected ItemReturnPeriodVo getReturnPeriod(String ilCategoryStandardId, String storageMethodType) {

        return goodsItemRegisterMapper.getReturnPeriod(ilCategoryStandardId, storageMethodType);

    }

    /*
     * 배송 / 발주 정보 조회 로직 End
     */

    /*
     * 품목별 상품 상세 이미지 조회 로직 Start
     */

    /**
     * @Desc ( 품목별 상품 상세 이미지 조회 ) 해당 품목의 기등록된 상품 상세 이미지 목록 조회
     *
     * @param String : 품목 코드
     * @param String : 품목 승인 코드
     *
     * @return List<ItemImageVo> : 해당 품목의 기등록된 상품 상세 이미지 목록
     */
    protected List<ItemImageVo> getItemImage(String ilItemCode, String ilItemApprId) {

    	if (ilItemApprId != null && !"".equals(ilItemApprId)) { // 승인 요청 내역의 품목 이미지 리스트
    		List<ItemImageVo> itemApprImageList = goodsItemRegisterMapper.getItemApprImage(ilItemApprId);
    		if (itemApprImageList != null && itemApprImageList.size() > 0) { // 승인 요청 시 품목 이미지가 변경된 경우
    			return itemApprImageList;
    		}
    		else { // 승인 요청 시 품목 이미지가 변경되지 않았으면 itemApprImageList가 없다. 이 경우에는 품목의 이미지 리스트 return.
        		return goodsItemRegisterMapper.getItemImage(ilItemCode);
    		}
    	}
    	else { // 승인요청 내역이 아닌경우, 품목의 이미지 리스트 return
        	return goodsItemRegisterMapper.getItemImage(ilItemCode);
    	}

    }
    
     /**
     * @Desc ( 품목별 상품 상세 이미지 조회 ) 해당 품목의 기등록된 상품 상세 이미지 목록 승인요청 목록과 비교
     *
     * @param String : 품목 코드
     * @param String : 품목 승인 코드
     *
     * @return List<ItemImageVo> : 해당 품목의 기등록된 상품 상세 이미지 목록
     */
    protected boolean compareItemImage(String ilItemCode, String ilItemApprId) {

        boolean changedItemImage = false;

    	if (ilItemApprId != null && !"".equals(ilItemApprId)) { // 승인 요청 내역의 품목 이미지 리스트
    		List<ItemImageVo> itemImageList = goodsItemRegisterMapper.getItemImage(ilItemCode);       // 품목 이미지
    		List<ItemImageVo> itemApprImageList = goodsItemRegisterMapper.getItemApprImage(ilItemApprId); // 승인요청 품목 이미지

    		if(itemImageList != null && itemApprImageList != null && itemApprImageList.size() > 0) {

                if((itemImageList.size() == itemApprImageList.size())) {
                      for (ItemImageVo apprVo : itemApprImageList) {
                        for(ItemImageVo originVo : itemImageList){
                            if(!StringUtil.nvl(apprVo.getImagePath()).equals(StringUtil.nvl(originVo.getImagePath()))) { // 이미지 파일 원본 경로 ( 물리적 파일명 포함 )
                                  changedItemImage = true;
                            }
                        }
                    }
                } else {
                   changedItemImage = true;
                }
            }
    	}
    	return changedItemImage;

    }

    /*
     * 품목별 상품 상세 이미지 조회 로직 End
     */

    /*
     * 마스터 품목 등록 관련 로직 Start
     */

    /**
     * @Desc ( 마스터 품목 등록 ) 품목 등록 전 Validation 체크 : 실패시 false 반환
     *
     * @param ItemRegisterRequestDto : 마스터 품목 등록 request dto
     *
     */
    protected void validationCheckBeforeAddItem(ItemRegisterRequestDto itemRegisterRequestDto) throws BaseException {

        // 영양정보 정렬 순서 배열의 길이와 실제 영양정보 상세 항목 목록의 길이가 다른 경우
        if (itemRegisterRequestDto.getItemNutritionDetailOrderList() != null //
                && itemRegisterRequestDto.getItemNutritionDetailOrderList().size() != itemRegisterRequestDto.getAddItemNutritionDetailList().size()) {

            throw new BaseException(ItemEnums.Item.INVALID_ITEM_NUTRITION_DETAIL_ORDER);

        }

        // 가격정보 유무 Check : 가격정보 시작일, 원가, 정상가 모두 존재해야 함
        if (StringUtil.isEmpty(itemRegisterRequestDto.getPriceApplyStartDate())) { // 가격정보 시작일 없음

            throw new BaseException(ItemEnums.Item.PRICE_APPLY_START_DATE_NOT_EXIST);

        }

        if (StringUtil.isEmpty(itemRegisterRequestDto.getStandardPrice())) { // 원가 없음

            throw new BaseException(ItemEnums.Item.STANDARD_PRICE_NOT_EXIST);

        }

        if (StringUtil.isEmpty(itemRegisterRequestDto.getRecommendedPrice())) { // 정상가 없음

            throw new BaseException(ItemEnums.Item.RECOMMENDED_PRICE_NOT_EXIST);

        }

    }

    /**
     * @Desc ( 마스터 품목 등록 ) IL_GOODS 품목 테이블에 EPR 미연동 품목 등록 전, BOS 자체 Rule 에 따른 품목 코드 생성
     *
     * @param ItemRegisterRequestDto : 마스터 품목 등록 request dto
     *
     * @return ItemRegisterRequestDto : 마스터 품목 등록 request dto
     */
    protected ItemRegisterRequestDto setErpNotLinkItemCode(ItemRegisterRequestDto itemRegisterRequestDto) {

        itemRegisterRequestDto.setIlItemCode(goodsItemRegisterMapper.getErpNotLinkItemCode(itemRegisterRequestDto));

        return itemRegisterRequestDto;

    }

    /**
     * @Desc ( 마스터 품목 등록 ) 품목코드, 등록자 ID 를 각 상세 항목 정보 ( 상품 영양 정보, 출고처 .. ) 에 일괄 세팅
     *
     *       - ERP 연동 품목 : 화면에서 EPR 품목 검색시 조회된 품목코드를 각 상세 항목 정보에 세팅
     *
     *       - ERP 미연동 품목 : getErpNotLinkItemCode 메서드에서 생성 후 세팅된 품목코드를 각 상세 항목 정보에 세팅
     *
     * @param ItemRegisterRequestDto : 마스터 품목 등록 request dto
     *
     * @return ItemRegisterRequestDto : 마스터 품목 등록 request dto
     */
    protected ItemRegisterRequestDto setItemCodeInDetailInfo(ItemRegisterRequestDto itemRegisterRequestDto) {

        // 품목별 상품정보 제공고시 상세항목에 품목 코드, 등록자 ID 세팅
        for (ItemSpecValueRequestDto addItemSpecValueRequestDto : itemRegisterRequestDto.getAddItemSpecValueList()) {
            addItemSpecValueRequestDto.setIlItemCode(itemRegisterRequestDto.getIlItemCode());
            addItemSpecValueRequestDto.setCreateId(itemRegisterRequestDto.getCreateId());
        }

        // 품목별 상품 영양정보 상세항목에 품목 코드, 등록자 ID 세팅
        for (ItemNutritionDetailDto addItemNutritionDetailDto : itemRegisterRequestDto.getAddItemNutritionDetailList()) {
            addItemNutritionDetailDto.setIlItemCode(itemRegisterRequestDto.getIlItemCode());
            addItemNutritionDetailDto.setCreateId(itemRegisterRequestDto.getCreateId());
        }

        // 품목별 상품 인증정보 목록에 품목 코드, 등록자 ID 세팅
        for (ItemCertificationDto addItemCertificationDto : itemRegisterRequestDto.getAddItemCertificationList()) {
            addItemCertificationDto.setIlItemCode(itemRegisterRequestDto.getIlItemCode());
            addItemCertificationDto.setCreateId(itemRegisterRequestDto.getCreateId());
        }

        // 품목별 출고처 목록에 품목 코드, 등록자 ID 세팅
        for (ItemWarehouseDto addItemWarehouseDto : itemRegisterRequestDto.getAddItemWarehouseList()) {
            addItemWarehouseDto.setIlItemCode(itemRegisterRequestDto.getIlItemCode());
            addItemWarehouseDto.setCreateId(itemRegisterRequestDto.getCreateId());
        }

        return itemRegisterRequestDto;

    }

    /**
     * @Desc ( 마스터 품목 등록 ) IL_GOODS 품목 테이블에 품목 정보 Insert 쿼리 실행
     *
     * @param ItemRegisterRequestDto : 마스터 품목 등록 request dto
     *
     * @return int
     */
    protected int addItem(ItemRegisterRequestDto itemRegisterRequestDto) {

        return goodsItemRegisterMapper.addItem(itemRegisterRequestDto);

    }

    /**
     * @Desc ( 마스터 품목 등록 ) 품목별 가격정보 원본 Insert 쿼리 실행
     *
     * @param ItemRegisterRequestDto : 마스터 품목 등록 request dto
     * @throws BaseException
     */
    protected void addItemPriceOrigin(ItemRegisterRequestDto itemRegisterRequestDto) throws BaseException {

        // 올가 ERP 품목인 경우 : 가격정보 조회 API 추가 호출 후 별도 로직으로 가격정보 Insert
        if (itemRegisterRequestDto.getErpLegalTypeCode().equals(LegalTypes.ORGA.getCode())) {

            addOrgaErpItemPrice(itemRegisterRequestDto); // 해당 올가 ERP 폼목의 정상 / 행사 가격 등록

        } else { // 기타 ERP 연동 품목 또는 ERP 미연동 품목인 경우

            ItemPriceOriginVo itemPriceOriginVo = ItemPriceOriginVo.builder() //
                    .ilItemCode(itemRegisterRequestDto.getIlItemCode()) //
                    .priceApplyStartDate(itemRegisterRequestDto.getPriceApplyStartDate()) //
                    .standardPrice(itemRegisterRequestDto.getStandardPrice()) //
                    .recommendedPrice(itemRegisterRequestDto.getRecommendedPrice()) //
                    .createId(itemRegisterRequestDto.getCreateId()) //
                    .systemUpdateYn("Y")    // 품목 최초 생성 가격 추가는 system, manager 모두 Y
                    .managerUpdateYn("Y")
                    .build();

            goodsItemRegisterMapper.addItemPriceOrigin(itemPriceOriginVo);

            String priceManageTp = "";

            if (itemRegisterRequestDto.isErpLinkIfYn()) {
                if (LegalTypes.LOHAS.getCode().equals(itemRegisterRequestDto.getErpLegalTypeCode())){
                    if (ProductTypes.GOODS.getCode().equals(itemRegisterRequestDto.getErpProductType())) priceManageTp = ItemEnums.priceManageType.R.getCode();
                    else if (ProductTypes.PRODUCT.getCode().equals(itemRegisterRequestDto.getErpProductType())) {
                        if (SupplierTypes.FDD.getCode().equals(itemRegisterRequestDto.getUrSupplierId())) priceManageTp = ItemEnums.priceManageType.R.getCode();
                        else priceManageTp = ItemEnums.priceManageType.A.getCode();
                    }
                }
            } else priceManageTp = ItemEnums.priceManageType.A.getCode();

            if (!priceManageTp.isEmpty()) {
                UserVo userVo = SessionUtil.getBosUserVO();
                Long userId = Long.parseLong(userVo.getUserId());					// USER ID
                goodsItemRegisterMapper.setItemPriceAppr(ItemPriceApprovalRequestDto.builder()    // system 자동 insert 도 approve 내역 필요함_2021.07.28
                        .ilItemCode(itemPriceOriginVo.getIlItemCode())
                        .priceApplyStartDate(itemPriceOriginVo.getPriceApplyStartDate())
                        .standardPrice(itemPriceOriginVo.getStandardPrice())
                        .standardPriceChange(itemPriceOriginVo.getStandardPrice())
                        .recommendedPrice(itemPriceOriginVo.getRecommendedPrice())
                        .recommendedPriceChange(itemPriceOriginVo.getRecommendedPrice())
                        .priceManageTp(priceManageTp)
                        .approvalStatus(ApprovalEnums.ApprovalStatus.APPROVED_BY_SYSTEM.getCode())
                        .approvalRequestUserId(userId)
                        .build());
            }
        }

    }

    /**
     * @Desc ( 마스터 품목 등록 ) 해당 올가 ERP 품목의 품목 코드로 가격정보 조회 API 호출 후 정상 / 행사 가격 등록
     *
     * @param ItemRegisterRequestDto : 마스터 품목 등록 request dto
     * @throws BaseException
     */
    protected void addOrgaErpItemPrice(ItemRegisterRequestDto itemRegisterRequestDto) throws BaseException {

        // 올가 행사 가격 등록시 상품할인 유형 공통코드 값
        String orgaDiscountType = GoodsDiscountType.ERP_EVENT.getCode();

        // 올가 행사 가격 등록시 상품할인 방법 유형 공통코드 값
        String orgaDiscountMethodType = GoodsDiscountMethodType.FIXED_PRICE.getCode();

        // 해당 올가 ERP 품목의 정상 / 행사 가격을 ERP API 에서 조회
        List<ErpIfPriceSearchResponseDto> orgaPriceList = getErpPriceApi(itemRegisterRequestDto.getIlItemCode(), null, null);

        ItemPriceOriginVo itemPriceOriginVo = null; // IL_ITEM_PRICE_ORIG 등록 Vo
        ItemDiscountVo itemDiscountVo = null; // IL_ITEM_DISCOUNT 등록 VO

        for (ErpIfPriceSearchResponseDto erpIfPriceSearchResponseDto : orgaPriceList) {

            switch (erpIfPriceSearchResponseDto.getErpSalesType()) {

            case NORMAL: // "정상" 가격

                itemPriceOriginVo = ItemPriceOriginVo.builder() //
                        .ilItemCode(itemRegisterRequestDto.getIlItemCode()) // 품목 코드
                        .priceApplyStartDate(erpIfPriceSearchResponseDto.getErpPriceApplyStartDate()) // 가격적용 시작일
                        .standardPrice(erpIfPriceSearchResponseDto.getErpStandardPrice()) // 원가
                        .recommendedPrice(erpIfPriceSearchResponseDto.getErpRecommendedPrice()) // 정상가
                        .createId(itemRegisterRequestDto.getCreateId()) // 생성자 ID
                        .systemUpdateYn("Y")
                        .managerUpdateYn("Y")
                        .build();

                goodsItemRegisterMapper.addItemPriceOrigin(itemPriceOriginVo);

                break;

            case EVENT: // "행사" 가격

                // 품목 행사 가격 등록
                itemPriceOriginVo = ItemPriceOriginVo.builder() //
                        .ilItemCode(itemRegisterRequestDto.getIlItemCode()) // 품목 코드
                        .priceApplyStartDate(erpIfPriceSearchResponseDto.getErpPriceApplyStartDate()) // 가격적용 시작일
                        .standardPrice(erpIfPriceSearchResponseDto.getNormalPurPrice()) // 행사가격 조회시 최근 정상항목의 공급가 데이터
                        .recommendedPrice(erpIfPriceSearchResponseDto.getNormalSelPrice()) // 행사가격 조회시 최근 정상항목의 판매가 데이터
                        .createId(itemRegisterRequestDto.getCreateId()) // 생성자 ID
                        .systemUpdateYn("Y")
                        .managerUpdateYn("Y")
                        .build();

                goodsItemRegisterMapper.addItemPriceOrigin(itemPriceOriginVo);

                // 품목 할인 등록
                itemDiscountVo = new ItemDiscountVo();
                itemDiscountVo.setIlItemCode(itemRegisterRequestDto.getIlItemCode());
                itemDiscountVo.setDiscountType(orgaDiscountType);
                itemDiscountVo.setDiscountStartDate(erpIfPriceSearchResponseDto.getErpPriceApplyStartDate());
                itemDiscountVo.setDiscountEndDate(erpIfPriceSearchResponseDto.getErpPriceApplyEndDate());
                itemDiscountVo.setDiscountMethodType(orgaDiscountMethodType);
                itemDiscountVo.setDiscountSalePrice(erpIfPriceSearchResponseDto.getErpRecommendedPrice());
                itemDiscountVo.setUseYn(true);
                itemDiscountVo.setCreateId(itemRegisterRequestDto.getCreateId());

//                itemDiscountVo = ItemDiscountVo.builder() //
//                        .ilItemCode(itemRegisterRequestDto.getIlItemCode()) // 품목 코드
//                        .discountType(orgaDiscountType) // 상품할인 유형 공통코드
//                        .discountStartDate(erpIfPriceSearchResponseDto.getErpPriceApplyStartDate()) // 할인 시작일
//                        .discountEndDate(erpIfPriceSearchResponseDto.getErpPriceApplyEndDate()) // 할인 종료일
//                        .discountMethodType(orgaDiscountMethodType) // 상품할인 방법 유형 공통코드
//                        .discountSalePrice(erpIfPriceSearchResponseDto.getErpRecommendedPrice()) // 할인 판매가 : 행사 가격 정보의 판매가
//                        .useYn(true) // 사용 여부
//                        .createId(itemRegisterRequestDto.getCreateId()) // 생성자 ID
//                        .build();

                goodsItemRegisterMapper.addItemDiscount(itemDiscountVo);

                break;

            default:

            }

        }

    }

    /**
     * @Desc ( 마스터 품목 등록 ) 품목별 상품정보 제공고시 상세 항목 Insert 쿼리 실행
     *
     * @param ItemRegisterRequestDto : 마스터 품목 등록 request dto
     */
    protected void addItemSpecValue(ItemRegisterRequestDto itemRegisterRequestDto) {

        for (ItemSpecValueRequestDto itemSpecValueRequestDto : itemRegisterRequestDto.getAddItemSpecValueList()) {

            goodsItemRegisterMapper.addItemSpecValue(itemSpecValueRequestDto);

        }

    }

    /**
     * @Desc ( 마스터 품목 등록 ) 품목별 상품 영양정보 상세 항목 Insert 쿼리 실행
     *
     * @param ItemRegisterRequestDto : 마스터 품목 등록 request dto
     */
    protected void addItemNutritionDetail(ItemRegisterRequestDto itemRegisterRequestDto) {

        // 화면에서 전송한 영양정보 정렬 순서 배열 : [ "영양정보 분류코드", .. ] 형식의 List, List 의 index 가 정렬 순서가 됨
        List<String> itemNutritionDetailOrderList = itemRegisterRequestDto.getItemNutritionDetailOrderList();

        for (ItemNutritionDetailDto itemNutritionDetailDto : itemRegisterRequestDto.getAddItemNutritionDetailList()) {

            // 해당 영양정보 분류코드에 해당하는 정렬 순서 지정
            itemNutritionDetailDto.setSort(itemNutritionDetailOrderList.indexOf(itemNutritionDetailDto.getNutritionCode()));

            // 영양정보 상세항목 Insert
            goodsItemRegisterMapper.addItemNutritionDetail(itemNutritionDetailDto);

        }

    }

    /**
     * @Desc ( 마스터 품목 등록 ) 품목별 상품 인증정보 상세 항목 Insert 쿼리 실행
     *
     * @param ItemRegisterRequestDto : 마스터 품목 등록 request dto
     */
    protected void addItemCertification(ItemRegisterRequestDto itemRegisterRequestDto) {

        for (ItemCertificationDto addItemCertificationDto : itemRegisterRequestDto.getAddItemCertificationList()) {

            goodsItemRegisterMapper.addItemCertification(addItemCertificationDto);

        }

    }

    /**
     * @Desc ( 마스터 품목 등록 ) 품목별 출고처 Insert 쿼리 실행
     *
     * @param ItemRegisterRequestDto : 마스터 품목 등록 request dto
     */
    protected void addItemWarehouse(ItemRegisterRequestDto itemRegisterRequestDto) {

    	String ilPoTpId = "";
    	String erpPoType = itemRegisterRequestDto.getErpPoType();

    	// 올가 공급업체 일경우 처리
    	if("2".equals(itemRegisterRequestDto.getUrSupplierId()) && erpPoType != null && (erpPoType.indexOf("센터(R2)") != -1)){

    		try {
				List<ErpIfGoodsOrgaPoResponseDto> orgaPoApiList = this.getOrgaPoApi(itemRegisterRequestDto.getIlItemCode());
				Boolean mergeFlag = false;
				ItemPoTypeVo itemPoTypeVo = new ItemPoTypeVo();

				if(orgaPoApiList.size()	> 0	) {
					for(ErpIfGoodsOrgaPoResponseDto orgaPoApi : orgaPoApiList) {
						int orgCnt = 0;
						int targetCnt = 0;
						int calCnt = 0;
						int resultCnt = 0;
						int poTargetCnt = 0;
						int poCalCnt = 0;
						int poResultCnt = 0;

						//품목코드
						String ilItemCd = orgaPoApi.getErpItemNo();

						//발주일 N로 초기세팅
						itemPoTypeVo.setPoSunYn("");
						itemPoTypeVo.setPoMonYn("");
						itemPoTypeVo.setPoTueYn("");
						itemPoTypeVo.setPoWedYn("");
						itemPoTypeVo.setPoThuYn("");
						itemPoTypeVo.setPoFriYn("");
						itemPoTypeVo.setPoSatYn("");

						//입고예정일 및 이동요청일 0으로 세팅
						itemPoTypeVo.setMoveReqSun(0);
						itemPoTypeVo.setMoveReqMon(0);
						itemPoTypeVo.setMoveReqTue(0);
						itemPoTypeVo.setMoveReqWed(0);
						itemPoTypeVo.setMoveReqThu(0);
						itemPoTypeVo.setMoveReqFri(0);
						itemPoTypeVo.setMoveReqSat(0);
						itemPoTypeVo.setScheduledSun(0);
						itemPoTypeVo.setScheduledMon(0);
						itemPoTypeVo.setScheduledTue(0);
						itemPoTypeVo.setScheduledWed(0);
						itemPoTypeVo.setScheduledThu(0);
						itemPoTypeVo.setScheduledFri(0);
						itemPoTypeVo.setScheduledSat(0);
						itemPoTypeVo.setPoReqSun(0);
						itemPoTypeVo.setPoReqMon(0);
						itemPoTypeVo.setPoReqTue(0);
						itemPoTypeVo.setPoReqWed(0);
						itemPoTypeVo.setPoReqThu(0);
						itemPoTypeVo.setPoReqFri(0);
						itemPoTypeVo.setPoReqSat(0);

						// 발주일 날짜계산
						if (orgaPoApi.getReqDat().equals(ItemEnums.PoDay.PO_DAY_MONDAY.getCodeName())) {
							if ("Y".equals(orgaPoApi.getOrderStopYn())) {
								itemPoTypeVo.setPoMonYn("N");
							} else {
								orgCnt = 1;
								itemPoTypeVo.setPoMonYn("Y");
							}
						} else if (orgaPoApi.getReqDat().equals(ItemEnums.PoDay.PO_DAY_TUESDAY.getCodeName())) {
							if ("Y".equals(orgaPoApi.getOrderStopYn())) {
								itemPoTypeVo.setPoTueYn("N");
							} else {
								orgCnt = 2;
								itemPoTypeVo.setPoTueYn("Y");
							}
						} else if (orgaPoApi.getReqDat().equals(ItemEnums.PoDay.PO_DAY_WEDNESDAY.getCodeName())) {
							if ("Y".equals(orgaPoApi.getOrderStopYn())) {
								itemPoTypeVo.setPoWedYn("N");
							} else {
								orgCnt = 3;
								itemPoTypeVo.setPoWedYn("Y");
							}
						} else if (orgaPoApi.getReqDat().equals(ItemEnums.PoDay.PO_DAY_THURSDAY.getCodeName())) {
							if ("Y".equals(orgaPoApi.getOrderStopYn())) {
								itemPoTypeVo.setPoThuYn("N");
							} else {
								orgCnt = 4;
								itemPoTypeVo.setPoThuYn("Y");
							}
						} else if (orgaPoApi.getReqDat().equals(ItemEnums.PoDay.PO_DAY_FRIDAY.getCodeName())) {
							if ("Y".equals(orgaPoApi.getOrderStopYn())) {
								itemPoTypeVo.setPoFriYn("N");
							} else {
								orgCnt = 5;
								itemPoTypeVo.setPoFriYn("Y");
							}
						} else if (orgaPoApi.getReqDat().equals(ItemEnums.PoDay.PO_DAY_SATURDAY.getCodeName())) {
							if ("Y".equals(orgaPoApi.getOrderStopYn())) {
								itemPoTypeVo.setPoSatYn("N");
							} else {
								orgCnt = 6;
								itemPoTypeVo.setPoSatYn("Y");
							}
						} else if (orgaPoApi.getReqDat().equals(ItemEnums.PoDay.PO_DAY_SUNDAY.getCodeName())) {
							if ("Y".equals(orgaPoApi.getOrderStopYn())) {
								itemPoTypeVo.setPoSunYn("N");
							} else {
								orgCnt = 7;
								itemPoTypeVo.setPoSunYn("Y");
							}
						}

						// 입고예정일 및 이동요청일 계산
						if (orgaPoApi.getInDat().equals(ItemEnums.PoDay.PO_DAY_MONDAY.getCodeName())) {
							targetCnt = 1;
						} else if (orgaPoApi.getInDat().equals(ItemEnums.PoDay.PO_DAY_TUESDAY.getCodeName())) {
							targetCnt = 2;
						} else if (orgaPoApi.getInDat().equals(ItemEnums.PoDay.PO_DAY_WEDNESDAY.getCodeName())) {
							targetCnt = 3;
						} else if (orgaPoApi.getInDat().equals(ItemEnums.PoDay.PO_DAY_THURSDAY.getCodeName())) {
							targetCnt = 4;
						} else if (orgaPoApi.getInDat().equals(ItemEnums.PoDay.PO_DAY_FRIDAY.getCodeName())) {
							targetCnt = 5;
						} else if (orgaPoApi.getInDat().equals(ItemEnums.PoDay.PO_DAY_SATURDAY.getCodeName())) {
							targetCnt = 6;
						} else if (orgaPoApi.getInDat().equals(ItemEnums.PoDay.PO_DAY_SUNDAY.getCodeName())){
							targetCnt = 7;
						}

						// PO요청일 계산
						if (orgaPoApi.getOutDat().equals(ItemEnums.PoDay.PO_DAY_MONDAY.getCodeName())) {
							poTargetCnt = 1;
						} else if (orgaPoApi.getOutDat().equals(ItemEnums.PoDay.PO_DAY_TUESDAY.getCodeName())) {
							poTargetCnt = 2;
						} else if (orgaPoApi.getOutDat().equals(ItemEnums.PoDay.PO_DAY_WEDNESDAY.getCodeName())) {
							poTargetCnt = 3;
						} else if (orgaPoApi.getOutDat().equals(ItemEnums.PoDay.PO_DAY_THURSDAY.getCodeName())) {
							poTargetCnt = 4;
						} else if (orgaPoApi.getOutDat().equals(ItemEnums.PoDay.PO_DAY_FRIDAY.getCodeName())) {
							poTargetCnt = 5;
						} else if (orgaPoApi.getOutDat().equals(ItemEnums.PoDay.PO_DAY_SATURDAY.getCodeName())) {
							poTargetCnt = 6;
						} else if (orgaPoApi.getOutDat().equals(ItemEnums.PoDay.PO_DAY_SUNDAY.getCodeName())){
							poTargetCnt = 7;
						}

						// 날짜차이 계산 로직수행
						if (orgCnt > 0) {
							calCnt = orgCnt - targetCnt;
							if (calCnt == 0) {
								resultCnt = 7;
							} else if (calCnt < 0) {
								resultCnt = calCnt * -1;
							} else {
								resultCnt = 7 - calCnt;
							}

							// PO요청일 계산
							poCalCnt = orgCnt - poTargetCnt;
							if (poCalCnt == 0) {
								poResultCnt = 7;
							} else if (poCalCnt < 0) {
								poResultCnt = poCalCnt * -1;
							} else {
								poResultCnt = 7 - poCalCnt;
							}
						}

						// 입고예정일 및 이동요청일 및 PO요청일 계산값 SET
						if (orgaPoApi.getReqDat().equals(ItemEnums.PoDay.PO_DAY_MONDAY.getCodeName())) {
							itemPoTypeVo.setScheduledMon(resultCnt);
							itemPoTypeVo.setMoveReqMon(resultCnt);
							itemPoTypeVo.setPoReqMon(poResultCnt);
						} else if (orgaPoApi.getReqDat().equals(ItemEnums.PoDay.PO_DAY_TUESDAY.getCodeName())) {
							itemPoTypeVo.setScheduledTue(resultCnt);
							itemPoTypeVo.setMoveReqTue(resultCnt);
							itemPoTypeVo.setPoReqTue(poResultCnt);
						} else if (orgaPoApi.getReqDat().equals(ItemEnums.PoDay.PO_DAY_WEDNESDAY.getCodeName())) {
							itemPoTypeVo.setScheduledWed(resultCnt);
							itemPoTypeVo.setMoveReqWed(resultCnt);
							itemPoTypeVo.setPoReqWed(poResultCnt);
						} else if (orgaPoApi.getReqDat().equals(ItemEnums.PoDay.PO_DAY_THURSDAY.getCodeName())) {
							itemPoTypeVo.setScheduledThu(resultCnt);
							itemPoTypeVo.setMoveReqThu(resultCnt);
							itemPoTypeVo.setPoReqThu(poResultCnt);
						} else if (orgaPoApi.getReqDat().equals(ItemEnums.PoDay.PO_DAY_FRIDAY.getCodeName())) {
							itemPoTypeVo.setScheduledFri(resultCnt);
							itemPoTypeVo.setMoveReqFri(resultCnt);
							itemPoTypeVo.setPoReqFri(poResultCnt);
						} else if (orgaPoApi.getReqDat().equals(ItemEnums.PoDay.PO_DAY_SATURDAY.getCodeName())) {
							itemPoTypeVo.setScheduledSat(resultCnt);
							itemPoTypeVo.setMoveReqSat(resultCnt);
							itemPoTypeVo.setPoReqSat(poResultCnt);
						} else if (orgaPoApi.getReqDat().equals(ItemEnums.PoDay.PO_DAY_SUNDAY.getCodeName())){
							itemPoTypeVo.setScheduledSun(resultCnt);
							itemPoTypeVo.setMoveReqSun(resultCnt);
							itemPoTypeVo.setPoReqSun(poResultCnt);
						}

						String poTpNm = "품목별상이("+orgaPoApi.getErpItemNo()+")";
						itemPoTypeVo.setPoTpNm(poTpNm);
						itemPoTypeVo.setIlItemCd(orgaPoApi.getErpItemNo());

						goodsItemRegisterMapper.addPoSearch(itemPoTypeVo);

						mergeFlag = true;
					}

					if(mergeFlag == true) {
						goodsItemRegisterMapper.addPoSearchMerge(itemPoTypeVo);
						ilPoTpId = itemPoTypeVo.getIlPoTpId();
						goodsItemRegisterMapper.delPoSearch();
					}
				}else {
					ilPoTpId = null;
				}
			} catch (BaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


    	}

        for (ItemWarehouseDto addItemWarehouseDto : itemRegisterRequestDto.getAddItemWarehouseList()) {
        	if("2".equals(itemRegisterRequestDto.getUrSupplierId()) && erpPoType != null && (erpPoType.indexOf("센터(R2)") != -1)) {
        		if(ilPoTpId != null && ilPoTpId != "") {
        			addItemWarehouseDto.setIlPoTpId(ilPoTpId);
        		}
        	}

            goodsItemRegisterMapper.addItemWarehouse(addItemWarehouseDto);

        }

    }

    /**
     * @Desc ( 마스터 품목 등록 ) 품목별 이미지 Insert 쿼리 실행
     *
     * @param ItemRegisterRequestDto : 마스터 품목 등록 request dto
     *
     */
    protected void addItemImage(ItemRegisterRequestDto itemRegisterRequestDto) {

        // 화면에서 전송한 상품 이미지 정렬 순서 배열 : [ "파일명", .. ] 형식의 List, List 의 index 가 정렬 순서가 됨
        List<String> itemImageOrderList = itemRegisterRequestDto.getItemImageOrderList();

        // 상품 이미지 정렬 순서 배열에 따른 이미지 sort 값 지정
        if (itemImageOrderList != null && itemImageOrderList.size() == itemRegisterRequestDto.getItemImageUploadResultList().size()) {

            for (UploadFileDto uploadFileDto : itemRegisterRequestDto.getItemImageUploadResultList()) {

                // 품목 이미지 VO 생성
                ItemImageRegisterVo addItemImageVo = ItemImageRegisterVo.builder() //
                        .ilItemCode(itemRegisterRequestDto.getIlItemCode()) // 품목 코드
                        .imagePath(uploadFileDto.getServerSubPath() + uploadFileDto.getPhysicalFileName()) // 물리적 파일명 ( 저장경로 포함 )
                        .imageOriginalName(uploadFileDto.getOriginalFileName()) // 원본 파일명
                        .createId(itemRegisterRequestDto.getCreateId()) // 등록자
                        .build();

                // 해당 품목 이미지의 리사이징 이미지 파일들 생성 / 각 리사이징 파일명 VO 에 세팅
                resizeItemImage(addItemImageVo, uploadFileDto, itemRegisterRequestDto.getImageRootStoragePath());

                // 해당 이미지의 원본 파일명이 대표 이미지명에 해당하는 경우 기본 이미지 여부를 true 로 세팅
                addItemImageVo.setBasicYn(itemRegisterRequestDto.getRepresentativeImageName());

                // 해당 이미지의 정렬순서 지정 : 대표 이미지인 경우 setBasicYn 내에서 0 으로 세팅, 아닌 경우 itemImageOrder 에 지정된 index 값으로 세팅
                if (!addItemImageVo.isBasicYn()) { // 대표 이미지가 아닌 경우
                    addItemImageVo.setSort(itemImageOrderList.indexOf(uploadFileDto.getOriginalFileName()));
                }

                goodsItemRegisterMapper.addItemImage(addItemImageVo); // 품목별 이미지 Insert 쿼리 실행

            }

        } else { // itemImageOrder 의 값 미전송 등으로 유효하지 않은 경우

            int sortValue = 1; // 대표 이미지를 제외한 이미지 정렬순서

            for (UploadFileDto uploadFileDto : itemRegisterRequestDto.getItemImageUploadResultList()) {

                // 품목 이미지 VO 생성
                ItemImageRegisterVo addItemImageVo = ItemImageRegisterVo.builder() //
                        .ilItemCode(itemRegisterRequestDto.getIlItemCode()) // 품목 코드
                        .imagePath(uploadFileDto.getServerSubPath() + uploadFileDto.getPhysicalFileName()) // 물리적 파일명 ( 저장경로 포함 )
                        .imageOriginalName(uploadFileDto.getOriginalFileName()) // 원본 파일명
                        .createId(itemRegisterRequestDto.getCreateId()) // 등록자
                        .build();

                // 해당 품목 이미지의 리사이징 이미지 파일들 생성 / 각 리사이징 파일명 VO 에 세팅
                resizeItemImage(addItemImageVo, uploadFileDto, itemRegisterRequestDto.getImageRootStoragePath());

                // 해당 이미지의 원본 파일명이 대표 이미지명에 해당하는 경우 기본 이미지 여부를 true 로 세팅
                addItemImageVo.setBasicYn(itemRegisterRequestDto.getRepresentativeImageName());

                // 해당 이미지의 정렬순서 지정 : 대표 이미지인 경우 setBasicYn 내에서 0 으로 세팅, 아닌 경우 1 부터 증감된 값으로 지정
                if (!addItemImageVo.isBasicYn()) { // 대표 이미지가 아닌 경우
                    addItemImageVo.setSort(sortValue++);
                }

                goodsItemRegisterMapper.addItemImage(addItemImageVo); // 품목별 이미지 Insert 쿼리 실행

            }
        }

    }

    /**
     * @Desc ( 마스터 품목 등록 ) 신규 품목 이미지 등록시 해당 품목 이미지의 리사이징 이미지 파일들 생성 / 각 리사이징 파일명 VO 에 세팅
     *
     * @param ItemImageRegisterVo : Insert 할 품목 이미지 Vo
     * @param UploadFileDto       : 해당 품목 이미지 업로드 결과 Dto
     * @param String              : 품목 이미지를 저장할 Public 저장소의 최상위 저장 디렉토리 경로
     */
    protected void resizeItemImage(ItemImageRegisterVo addItemImageVo, UploadFileDto uploadFileDto, String publicRootStoragePath) {

        // 리사이징 원본 파일의 전체 경로 : 최상위 저장 디렉토리 경로 + 하위 경로
        String imageFilePath = publicRootStoragePath + uploadFileDto.getServerSubPath();

        for (ItemImagePrefixBySize itemImagePrefixBySize : ItemImagePrefixBySize.values()) { // 품목 이미지 Size / Prefix 반복문 시작

            switch (itemImagePrefixBySize) {

            case PREFIX_640:

                // 640 X 640 리사이즈 이미지 생성
                ImageUtil.createResizedImage(imageFilePath, uploadFileDto.getPhysicalFileName(), ItemImagePrefixBySize.PREFIX_640.getPrefix(), ItemImagePrefixBySize.PREFIX_640.getImageSize());

                // 640*640 파일명 ( 저장경로 포함 )
                addItemImageVo.setSize640imagePath(uploadFileDto.getServerSubPath() + ItemImagePrefixBySize.PREFIX_640.getPrefix() + uploadFileDto.getPhysicalFileName());

                break;

            case PREFIX_320:

                // 320 X 320 리사이즈 이미지 생성
                ImageUtil.createResizedImage(imageFilePath, uploadFileDto.getPhysicalFileName(), ItemImagePrefixBySize.PREFIX_320.getPrefix(), ItemImagePrefixBySize.PREFIX_320.getImageSize());

                // 320*320 파일명 ( 저장경로 포함 )
                addItemImageVo.setSize320imagePath(uploadFileDto.getServerSubPath() + ItemImagePrefixBySize.PREFIX_320.getPrefix() + uploadFileDto.getPhysicalFileName());

                break;

            case PREFIX_216:

                // 216 X 216 리사이즈 이미지 생성
                ImageUtil.createResizedImage(imageFilePath, uploadFileDto.getPhysicalFileName(), ItemImagePrefixBySize.PREFIX_216.getPrefix(), ItemImagePrefixBySize.PREFIX_216.getImageSize());

                // 216*216 파일명 ( 저장경로 포함 )
                addItemImageVo.setSize216imagePath(uploadFileDto.getServerSubPath() + ItemImagePrefixBySize.PREFIX_216.getPrefix() + uploadFileDto.getPhysicalFileName());

                break;

            case PREFIX_180:

                // 180 X 180 리사이즈 이미지 생성
                ImageUtil.createResizedImage(imageFilePath, uploadFileDto.getPhysicalFileName(), ItemImagePrefixBySize.PREFIX_180.getPrefix(), ItemImagePrefixBySize.PREFIX_180.getImageSize());

                // 180*180 파일명 ( 저장경로 포함 )
                addItemImageVo.setSize180imagePath(uploadFileDto.getServerSubPath() + ItemImagePrefixBySize.PREFIX_180.getPrefix() + uploadFileDto.getPhysicalFileName());

                break;

            case PREFIX_75:

                // 75 X 75 리사이즈 이미지 생성
                ImageUtil.createResizedImage(imageFilePath, uploadFileDto.getPhysicalFileName(), ItemImagePrefixBySize.PREFIX_75.getPrefix(), ItemImagePrefixBySize.PREFIX_75.getImageSize());

                // 75*75 파일명 ( 저장경로 포함 )
                addItemImageVo.setSize75imagePath(uploadFileDto.getServerSubPath() + ItemImagePrefixBySize.PREFIX_75.getPrefix() + uploadFileDto.getPhysicalFileName());

                break;

            }

        } // 품목 이미지 Size / Prefix 반복문 끝

    }

    /**
     * @Desc IL_ITEM_PRICE_ORIG
     *
     * @param ItemRegisterRequestDto : : 마스터 품목 저장 request dto
     *
     * @throws BaseException
     */
    protected void addItemPriceOrig(ItemRegisterRequestDto itemRegisterRequestDto, List<ItemPriceVo> itemPriceList) throws Exception {

    	for(ItemPriceVo itemPriceVo : itemPriceList) {

    		if(itemPriceVo.getApprovalStatusCode().equals("APPR_STAT.NONE")) {

    			itemPriceVo.setCreateId(SessionUtil.getBosUserVO().getUserId());
    			goodsItemRegisterMapper.addItemPriceOrig(itemPriceVo);

    			goodsRegistBiz.spGoodsPriceUpdateWhenItemPriceChanges(itemPriceVo.getIlItemCode()); // 품목가격 업데이트 프로시저 호출
    		}
    	}

    	goodsRegistBiz.spPackageGoodsPriceUpdateWhenItemPriceChanges(); // 묶음상품 가격 정보 업데이트 프로시저 호출
    }

    /**
	* @Desc 상품 변경내역 저장
	* @param GoodsRegistRequestDto
	* @return int
	*/
	protected int addItemChangeLog(ItemChangeLogVo itemChangeLogVo) {
		return goodsItemRegisterMapper.addItemChangeLog(itemChangeLogVo);
	}

	/**
	* @Desc  품목 승인 내역 확인, 승인 내역 존재시 요청 자격 확인
	* @param String
	* @return GoodsRegistApprVo
	*/
	protected ItemRegistApprVo itemApprInfo(String userId, String ilItemCd, String ilItemApprid, String apprKindTp)  throws Exception {
		return goodsItemRegisterMapper.itemApprInfo(userId, ilItemCd, ilItemApprid, apprKindTp);
	}

	/**
	* @Desc  품목 승인 내역 저장
	* @param ItemRegistApprVo
	* @return int
	*/
	protected int addItemAppr(ItemRegistApprVo itemRegistApprVo) {
		return goodsItemRegisterMapper.addItemAppr(itemRegistApprVo);
	}

	/**
	* @Desc  품목 승인 상태 이력 저장
	* @param ItemRegistApprVo
	* @return int
	*/
	protected int addItemApprStatusHistory(ItemRegistApprVo itemRegistApprVo) {
		return goodsItemRegisterMapper.addItemApprStatusHistory(itemRegistApprVo);
	}

	/**
	* @Desc  품목 상태변경
	* @param ItemRegistApprVo
	* @return int
	*/
	protected int updateItemRequestStatus(String ilItemCd, String itemStatusTp) {
		return goodsItemRegisterMapper.updateItemRequestStatus(ilItemCd, itemStatusTp);
	}

	/**
	* @Desc  품목 승인 인증정보 저장
	* @param ItemRegistApprVo
	* @return int
	*/
	protected int addItemCertificationAppr(ItemCertificationApprVo itemCertificationApprVo) {
		return goodsItemRegisterMapper.addItemCertificationAppr(itemCertificationApprVo);
	}

	/**
	* @Desc  품목 승인 인증정보 저장
	* @param ItemRegistApprVo
	* @return int
	*/
	protected int addItemImageAppr(ItemImageRegisterVo itemImageRegisterVo) {
		return goodsItemRegisterMapper.addItemImageAppr(itemImageRegisterVo);
	}

	/**
	* @Desc  품목 승인 인증정보 저장
	* @param ItemRegistApprVo
	* @return int
	*/
	protected int addItemNutritionAppr(ItemNutritionApprVo itemNutritionApprVo) {
		return goodsItemRegisterMapper.addItemNutritionAppr(itemNutritionApprVo);
	}

	protected int addItemSpecAppr(ItemSpecApprVo itemSpecApprVo) {
		return goodsItemRegisterMapper.addItemSpecAppr(itemSpecApprVo);
	}







    /*
     * 마스터 품목 등록 관련 로직 End
     */

}
