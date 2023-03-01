package kr.co.pulmuone.v1.goods.item.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfGoodsOrgaPoResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfNutritionSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfPriceSearchResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.CompanyEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.ItemEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.ItemEnums.ErpItemSearchOption;
import kr.co.pulmuone.v1.comm.enums.ItemEnums.ItemColumnComment;
import kr.co.pulmuone.v1.comm.enums.ItemEnums.ItemEtcColumnComment;
import kr.co.pulmuone.v1.comm.enums.ItemEnums.MasterItemTypes;
import kr.co.pulmuone.v1.comm.enums.ItemEnums.SpecFieldCode;
import kr.co.pulmuone.v1.comm.enums.ItemEnums.UndeliverableAreaTypes;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.UploadFileDto;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.EnumUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.item.dto.AvailableNutritionCodeResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ErpIfGoodsOrgaPoListResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ErpIfPriceListResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ErpItemPopupResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemCertificationCodeResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemCertificationDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemCertificationListResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemImageResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemModifyResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemNutritionDetailDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemPoTypeCodeResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemRegistApprRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemRegisterRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemRegisterResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemReturnPeriodResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemSpecCodeResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemSpecFieldModifiedMessageRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemSpecResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemSpecValueRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemWarehouseCodeReponseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemWarehouseDto;
import kr.co.pulmuone.v1.goods.item.dto.ItemWarehouseResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.MasterItemResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemChangeLogVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemImageRegisterVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemPriceVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemRegistApprVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemWarehouseCodeVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 마스터 품목 등록 BizImpl
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
public class GoodsItemRegisterBizImpl implements GoodsItemRegisterBiz {

    @Autowired
    private final GoodsItemRegisterService goodsItemRegisterService;

    int changeTranNum = 0;								//상품 변경이력 Transaction 갯수
	int sameValue = 0;									//beforeData, afterData가 동일한 갯수

	ItemEtcColumnComment ItemEtcColumnComment = null;	//테이블별 ID , DATA , COLUMN COMMENT 컬럼을 지정

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
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getErpItemApiList(String searchOption, String searchValue, String masterItemType) {

        ErpItemSearchOption erpItemSearchOptionEnum = null;
        MasterItemTypes masterItemTypeEnum = null;

        ErpItemPopupResponseDto erpItemPopupResponseDto = null;

        // 화면에서 넘김 searchOption 값으로 ErpItemSearchOptionEnum 에 등록된 Enum 검색
        try {

            erpItemSearchOptionEnum = ErpItemSearchOption.valueOf(searchOption);

        } catch (IllegalArgumentException e) {// ErpItemSearchOptionEnum 내에 존재하지 않는 검색 옵션인 경우 : IllegalArgumentException 발생
            log.error("search option not matched : No enum constant == {}", e);
            return ApiResult.result(ItemEnums.Item.SEARCH_OPTION_NOT_MATCHED);

        }

        // 화면에서 넘김 masterItemType 값으로 MasterItemTypes 에 등록된 Enum 검색
        masterItemTypeEnum = EnumUtil.getEnum(MasterItemTypes.class, masterItemType);

        if (masterItemTypeEnum == null) { // MasterItemTypes 내에 존재하지 않는 검색 옵션인 경우

            log.error("master item type not matched : No enum constant == {}");
            return ApiResult.result(ItemEnums.Item.MASTER_ITEM_TYPE_NOT_MATCHED);

        }

        try {

            // ErpItemSearchOption 에 지정된 검색 옵션으로 ERP 품목 조회 API 검색
            erpItemPopupResponseDto = goodsItemRegisterService.getErpItemApiList(erpItemSearchOptionEnum, searchValue, masterItemTypeEnum);

            return ApiResult.success(erpItemPopupResponseDto);

        } catch (BaseException be) { // getErpItemApiList 서비스 내에서 BaseException 발생시
            log.error(be.getMessage(), be);
            return ApiResult.result(be.getMessageEnum());

        } catch (Exception e) { // 기타 예외 발생시
            log.error(e.getMessage(), e);
            throw e;

        }

    }

    /**
     * @Desc ( ERP 영양정보 조회 API ) ERP API 영양정보 조회 인터페이스 호출
     *
     * @param String : 품목 코드
     *
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getErpNutritionApi(String ilItemCode) {

        ErpIfNutritionSearchResponseDto erpIfNutritionSearchResponseDto = null;

        try {

            erpIfNutritionSearchResponseDto = goodsItemRegisterService.getErpNutritionApi(ilItemCode);

            return ApiResult.success(erpIfNutritionSearchResponseDto);

        } catch (BaseException be) { // getErpNutritionApi 서비스 내에서 BaseException 발생시
            log.error(be.getMessage(), be);
            return ApiResult.result(be.getMessageEnum());

        } catch (Exception e) { // 기타 예외 발생시
            log.error(e.getMessage(), e);
            throw e;

        }

    }

    /**
     * @Desc ( ERP 가격정보 조회 API ) ERP API 가격정보 조회 인터페이스 호출
     *
     * @param String : 품목 코드
     * @param String : ERP 법인코드 ( "PFF" ( 풀무원식품 ), "OGH" ( 올가 ), "FDM" ( 푸드머스 ), "PGS" ( 건강생활 ) )
     * @param String : ERP 행사구분 ( "정상", "행사" )
     *
     * @return ApiResult
     * @throws BaseException
     */
    @Override
    public ApiResult<?> getErpPriceApi(String ilItemCode, String erpLegalTypeCode, String erpSalesType) {

        List<ErpIfPriceSearchResponseDto> erpIfPriceSearchResponseDtoList = null;
        ErpIfPriceListResponseDto erpIfPriceListResponseDto = null;

        try {

            erpIfPriceSearchResponseDtoList = goodsItemRegisterService.getErpPriceApi(ilItemCode, erpLegalTypeCode, erpSalesType);

            erpIfPriceListResponseDto = ErpIfPriceListResponseDto.builder() //
                    .erpIfPriceSearchResponseDtoList(erpIfPriceSearchResponseDtoList) //
                    .build();

            return ApiResult.success(erpIfPriceListResponseDto);

        } catch (BaseException be) { // getErpPriceApi 서비스 내에서 BaseException 발생시
            log.error(be.getMessage(), be);
            return ApiResult.result(be.getMessageEnum());

        } catch (Exception e) { // 기타 예외 발생시
            log.error(e.getMessage(), e);
            throw e;

        }

    }

    /*
     * ERP API 연동 관련 로직 End
     */

    /*
     * 마스터 품목 복사 : ERP 연동 or 미연동 품목의 원본 정보 조회 로직 Start
     */

    /**
     * @Desc ( 마스터 품목 복사 ) EPR 연동 or 미연동 품목의 원본 정보 조회
     *
     * @param String : 품목 코드
     *
     * @param String : ERP 연동 여부
     *
     * @return ApiResult : ERP 연동 or 미연동 품목의 원본 정보 ApiResult
     */
    @Override
    public ApiResult<?> getMasterItem(String ilItemCode, String isErpItemLink) {

        MasterItemResponseDto masterItemResponseDto = null;

        // ERP 연동 여부 isErpItemLink 의 Validation 체크 : "true", "false" 만 허용
        switch (isErpItemLink) {

        case "true": // ERP 연동 마스터 품목 검색

            masterItemResponseDto = MasterItemResponseDto.builder() //
                    .getErpLinkMasterItemVo(goodsItemRegisterService.getErpLinkMasterItem(ilItemCode)) //
                    .build();
            break;

        case "false": // ERP 미연동 마스터 품목 검색

            masterItemResponseDto = MasterItemResponseDto.builder() //
                    .getErpNotLinkMasterItemVo(goodsItemRegisterService.getErpNotLinkMasterItem(ilItemCode)) //
                    .build();

            break;

        default: // isErpItemLink 가 "true", "false" 가 아닌 다른 값인 경우

            log.error("invalid ErpItemLink value");
            return ApiResult.result(ItemEnums.Item.INVALID_ERP_ITEM_LINK_VALUE);

        }

        return ApiResult.success(masterItemResponseDto);

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
     * @return ApiResult : 상품정보 제공고시 상품군 코드 목록 ApiResult
     */
    @Override
    public ApiResult<?> getItemSpecCode() {

        ItemSpecCodeResponseDto itemSpecCodeResponseDto = ItemSpecCodeResponseDto.builder() //
                .itemSpecCodeList(goodsItemRegisterService.getItemSpecCode()) //
                .build();

        return ApiResult.success(itemSpecCodeResponseDto);

    }

    /**
     * @Desc ( 상품정보제공고시 조회 ) 상품정보 제공고시 상품군별 분류 항목 전체 조회 => 특수 항목에 대한 세부 메시지 및 수정 가능여부 등 설정
     *
     *       품목 코드 param 전달시에는 해당 품목 코드의 상품정보 제공고시 상세 항목을 같이 조회
     *
     * @param String : 품목 코드
     *
     * @return ApiResult : 상품정보 제공고시 상품군별 분류 항목 ApiResult
     */
    @Override
    public ApiResult<?> getItemSpecList(String ilItemCode, String ilItemApprId) {

        ItemSpecResponseDto itemSpecResponseDto = null;

        if (StringUtil.isEmpty(ilItemCode)) { // 품목 코드 Parameter 미전달시

            itemSpecResponseDto = ItemSpecResponseDto.builder() //
                    .itemSpecList(goodsItemRegisterService.getItemSpecList()) // 상품정보 제공고시 분류 항목 전체 조회
                    .itemSpecValueList(null) // 상품정보 제공고시 품목별 세부항목은 조회하지 않음
                    .build();

        } else { // 품목 코드 Parameter 전달시

            itemSpecResponseDto = ItemSpecResponseDto.builder() //
                    .itemSpecList(goodsItemRegisterService.getItemSpecList()) // 상품정보 제공고시 분류 항목 전체 조회
                    .itemSpecValueList(goodsItemRegisterService.getItemSpecValueList(ilItemCode, ilItemApprId)) // 해당 품목 코드의 상품정보 제공고시 상세 항목 조회
                    .build();

        }

        return ApiResult.success(itemSpecResponseDto);

    }

    /**
     * @Desc ( 상품정보제공고시 조회 ) 해당 상품정보제공고시 항목 코드의 수정 메시지 조회
     *
     * @param ItemSpecFieldModifiedMessageRequestDto : 해당 상품정보제공고시 항목 코드의 수정 메시지 조회 Request dto
     *
     * @return ApiResult : 해당 상품정보제공고시 항목 코드의 수정 메시지 ApiResult
     */
    @Override
    public ApiResult<?> getItemSpecFieldDetailMessage(ItemSpecFieldModifiedMessageRequestDto itemSpecFieldModifiedMessageRequestDto) {

        // specFieldCode 값으로 SpecFieldCodeEnum 조회
        SpecFieldCode specFieldCodeEnum = SpecFieldCode.valueOf(itemSpecFieldModifiedMessageRequestDto.getSpecFieldCode());

        String modifiedMessage = null; // 수정 메시지

        /*
         * SpecFieldCode enum 에 정의된 수정 가능 항목들에 따라 수정 메시지 조회
         */
        if (specFieldCodeEnum == SpecFieldCode.SPEC_FIELD_01) { // "제조연월일/유통기한 또는 품질유지기한"

            // 공급업체 PK, 유통기한으로 해당 품목의 출고기한 조회
            String itemDelivery = goodsItemRegisterService.getItemSpecFieldDetailMessage(itemSpecFieldModifiedMessageRequestDto);

            if (itemDelivery != null) { // 출고기한 조회시

                // "제조연월일/유통기한 또는 품질유지기한" 수정 메시지 생성 : 출고기한 반영
                modifiedMessage = itemDelivery;

            } else { // 출고기한 미조회시

                modifiedMessage = specFieldCodeEnum.getSpecFieldDetailMessage(); // 기본 상세 메시지

            }

        }

        ItemSpecResponseDto itemSpecResponseDto = ItemSpecResponseDto.builder() //
                .itemSpecModifiedMessage(modifiedMessage) // 수정 메시지
                .build();

        return ApiResult.success(itemSpecResponseDto);

    }

    /*
     * 상품정보제공고시 조회 로직 End
     */

    /*
     * 상품 영양정보 조회 로직 Start
     */

    /**
     * @Desc ( 상품 영양정보 조회 ) 해당 품목코드로 등록 가능한 영양정보 분류 코드 조회
     *
     *       ERP 미연동 품목인 경우, BOS 상에 등록된 해당 품목 코드의 영양정보 세부항목 조회 후 같이 반환 : 마스터 품목 복사 기능에서 사용함
     *
     * @param String : 품목 코드
     *
     * @param String : ERP 연동 여부, 단, 마스터 품목 수정 화면에서 품목 정보 수정시에는 화면에서 항상 false 전달함
     *
     * @return ApiResult : 해당 품목코드로 등록 가능한 영양정보 분류 코드 ApiResult
     */
    @Override
    public ApiResult<?> getAddAvailableNutritionCodeList(String ilItemCode, String isErpItemLink, String ilItemApprId) {

        AvailableNutritionCodeResponseDto availableNutritionCodeResponseDto = null;

        // ERP 연동 여부 isErpItemLink 의 Validation 체크 : "true", "false" 만 허용
        switch (isErpItemLink) {

        case "true": // ERP 연동 품목인 경우

            availableNutritionCodeResponseDto = AvailableNutritionCodeResponseDto.builder() //
                    .addAvailableNutritionCodeList(goodsItemRegisterService.getAddAvailableNutritionCodeList()) //
                    .itemNutritionDetailList(null) // ERP 연동 품목 : 영양정보 세부항목은 조회하지 않음
                    .build();
            break;

        case "false": // ERP 미연동 품목 or 품목 정보 수정시

            availableNutritionCodeResponseDto = AvailableNutritionCodeResponseDto.builder() //
                    .addAvailableNutritionCodeList(goodsItemRegisterService.getAddAvailableNutritionCodeList()) //
                    .itemNutritionDetailList(goodsItemRegisterService.getItemNutritionDetailList(ilItemCode, ilItemApprId)) //
                    .changedItemNutrition(goodsItemRegisterService.compareItemNutritionDetailList(ilItemCode, ilItemApprId)) //
                    .build();

            break;

        default: // isErpItemLink 가 "true", "false" 가 아닌 다른 값인 경우

            log.error("invalid ErpItemLink value");
            return ApiResult.result(ItemEnums.Item.INVALID_ERP_ITEM_LINK_VALUE);

        }

        return ApiResult.success(availableNutritionCodeResponseDto);

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
     * @return ApiResult : 상품 인증정보 코드 조회 ApiResult
     */
    @Override
    public ApiResult<?> getItemCertificationCode() {

        ItemCertificationCodeResponseDto itemCertificationCodeResponseDto = ItemCertificationCodeResponseDto.builder() //
                .itemCertificationCodeList(goodsItemRegisterService.getItemCertificationCode()) //
                .build();

        return ApiResult.success(itemCertificationCodeResponseDto);

    }

    /**
     * @Desc ( 상품 인증정보 조회 ) 품목별 상품 인증정보 목록 조회
     *
     * @param String : 품목 코드
     *
     * @return ApiResult : 품목별 상품 인증정보 목록 조회 ApiResult
     */
    @Override
    public ApiResult<?> getItemCertificationList(String ilItemCode, String ilItemApprId) {

        ItemCertificationListResponseDto itemCertificationListResponseDto = ItemCertificationListResponseDto.builder() //
                .itemCertificationList(goodsItemRegisterService.getItemCertificationList(ilItemCode, ilItemApprId)) //
                .changedItemCertification(goodsItemRegisterService.checkItemCertification(ilItemCode, ilItemApprId))
                .build();

        return ApiResult.success(itemCertificationListResponseDto);

    }

    /*
     * 상품 인증정보 조회 로직 End
     */

    /*
     * 공급업체 / 출고처 조회 로직 Start
     */

    /**
     * @Desc ( 공급업체 / 출고처 조회 ) 해당 공급업체 PK 값에 해당하는 출고처 그룹, 출고처 코드 정보 조회
     *
     * @param String : 공급업체 PK
     *
     * @return ApiResult : 해당 공급업체 PK 값에 해당하는 출고처 그룹, 출고처 코드 조회 ApiResult
     */
    @Override
    public ApiResult<?> getItemWarehouseCode(String urSupplierId, String masterItemType) {

        // 해당 공급업체 PK 값에 해당하는 출고처 그룹, 출고처 정보 조회
        List<ItemWarehouseCodeVo> itemWarehouseCodeList = goodsItemRegisterService.getItemWarehouseCode(urSupplierId, masterItemType);

        // 임시 로직 : 매장 재고가 하나 이상 연동되었을 경우 출고처 목록에서 삭제 불가 => 현재는 모두 "삭제 가능"으로 세팅
        for (ItemWarehouseCodeVo itemWarehouseCodeVo : itemWarehouseCodeList) {
            itemWarehouseCodeVo.setCanDeleted(true);
        }

        // 조회된 출고처 목록에서 출고처 그룹 코드 목록 추출 : 출고처 그룹 코드 중복 제거용 Map 사용
        Map<String, GetCodeListResultVo> warehouseGroupMap = new HashMap<>();

        for (ItemWarehouseCodeVo itemWarehouseCodeVo : itemWarehouseCodeList) {

            // 조회된 출고처 정보에서 ( 출고처 그룹 코드 : 출고처 그룹 코드명 ) 중복 제거 위해 Map 에 취합
            if (!warehouseGroupMap.containsKey(itemWarehouseCodeVo.getWarehouseGroupCode())) {
                GetCodeListResultVo getCodeListResultVo = new GetCodeListResultVo();

                getCodeListResultVo.setCode(itemWarehouseCodeVo.getWarehouseGroupCode());
                getCodeListResultVo.setName(itemWarehouseCodeVo.getWarehouseGroupName());

                warehouseGroupMap.put(itemWarehouseCodeVo.getWarehouseGroupCode(), getCodeListResultVo);
            }

        }

        ItemWarehouseCodeReponseDto itemWarehouseCodeReponseDto = ItemWarehouseCodeReponseDto.builder() //
                .itemWarehouseCodeList(itemWarehouseCodeList) // 해당 품목 코드의 출고처 그룹, 출고처 정보 조회 목록
                .warehouseGroupCodeList( // 출고처 그룹 코드 목록
                        warehouseGroupMap.values().stream().collect(Collectors.toList()) // List<GetCodeListResultVo> 타입으로 취합
                ) //
                .build();

        return ApiResult.success(itemWarehouseCodeReponseDto);

    }

    /**
     * @Desc ( 공급업체 / 출고처 조회 ) 해당 품목코드로 등록된 출고처 정보 조회
     *
     * @param String : 품목 코드
     *
     * @return ApiResult : 해당 품목코드로 등록된 출고처 정보 조회 ApiResult
     */
    @Override
    public ApiResult<?> getItemWarehouseList(String ilItemCode) {

        ItemWarehouseResponseDto itemWarehouseResponseDto = ItemWarehouseResponseDto.builder() //
                .itemWarehouseList(goodsItemRegisterService.getItemWarehouseList(ilItemCode)) //
                .build();

        return ApiResult.success(itemWarehouseResponseDto);

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
     * @return ApiResult : 해당 공급업체 PK 값에 해당하는 발주유형 목록 ApiResult
     */
    @Override
    public ApiResult<?> getItemPoTypeCode(String urSupplierId, String erpPoType, String selectType) {

    	ItemPoTypeCodeResponseDto itemPoTypeCodeResponseDto = null;

    	if(selectType != null && selectType.equals("all")) {
    		itemPoTypeCodeResponseDto = ItemPoTypeCodeResponseDto.builder() //
                    .rows(goodsItemRegisterService.getItemPoTypeAllCode(urSupplierId)) //
                    .build();
    		return ApiResult.success(itemPoTypeCodeResponseDto);
    	}
    	// 올가이면서 ERP 발주유형이 R2 인경우
    	if("2".equals(urSupplierId) && erpPoType != null && (erpPoType.indexOf("센터(R2)") != -1)	) {

    		itemPoTypeCodeResponseDto = ItemPoTypeCodeResponseDto.builder() //
                    .rows(goodsItemRegisterService.getItemPoTypeOrgaCode(urSupplierId)) //
                    .build();
    	}else {
    		itemPoTypeCodeResponseDto = ItemPoTypeCodeResponseDto.builder() //
                    .rows(goodsItemRegisterService.getItemPoTypeCode(urSupplierId)) //
                    .build();


    	}

    	return ApiResult.success(itemPoTypeCodeResponseDto);

    }

    /**
     * @Desc ( 배송 / 발주 정보 조회 ) 해당 품목 정보의 표준 카테고리, 보관방법에 따른 반품 가능여부, 반품 가능기간 계산
     *
     * @param String : 표준 카테고리 PK
     *
     * @param String : 보관방법 ( 공통코드 ERP_STORAGE_TYPE )
     *
     * @return ApiResult : 반품 가능여부, 반품가능 기간 계산 ApiResult
     */
    @Override
    public ApiResult<?> getReturnPeriod(String ilCategoryStandardId, String storageMethodType) {

        ItemReturnPeriodResponseDto itemReturnPeriodResponseDto = ItemReturnPeriodResponseDto.builder() //
                .itemReturnPeriodVo(goodsItemRegisterService.getReturnPeriod(ilCategoryStandardId, storageMethodType)) //
                .build();

        return ApiResult.success(itemReturnPeriodResponseDto);

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
     * @param ilItemApprId : 품목 승인 코드
     *
     * @return ApiResult : 해당 품목의 기등록된 상품 상세 이미지 목록 조회 ApiResult
     */
    @Override
    public ApiResult<?> getItemImage(String ilItemCode, String ilItemApprId) {

        ItemImageResponseDto itemImageResponseDto = ItemImageResponseDto.builder() //
                .itemImageList(goodsItemRegisterService.getItemImage(ilItemCode, ilItemApprId)) //
                .changedItemImage(goodsItemRegisterService.compareItemImage(ilItemCode, ilItemApprId))
                .build();

        return ApiResult.success(itemImageResponseDto);

    }

    /*
     * 품목별 상품 상세 이미지 조회 로직 End
     */

    /*
     * 마스터 품목 등록 관련 로직 Start
     */

    /**
     * @Desc ( 마스터 품목 등록 ) 마스터 품목 등록
     *
     * @param ItemRegisterRequestDto : 마스터 품목 등록 request dto
     *
     * @return ApiResult : 마스터 품목 등록 ApiResult
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BaseException.class, Exception.class })
    public ApiResult<?> addItem(ItemRegisterRequestDto itemRegisterRequestDto) throws BaseException, Exception {

        // 품목 등록 전 validation 체크 : validation 체크 실패시 BaseException 발생
        goodsItemRegisterService.validationCheckBeforeAddItem(itemRegisterRequestDto);

        // 품목 등록시 USER ID 세팅
        itemRegisterRequestDto.setCreateId((Long.valueOf((SessionUtil.getBosUserVO()).getUserId())));

        /*
         * ERP 미연동 품목인 경우 BOS 자체 품목코드 생성
         */
        if (!itemRegisterRequestDto.isErpLinkIfYn()) {

            goodsItemRegisterService.setErpNotLinkItemCode(itemRegisterRequestDto);

        }

        /*
         * 배송불가 지역 코드 세팅
         */
        itemRegisterRequestDto.setUndeliverableAreaType( //
                UndeliverableAreaTypes.getUndeliverableAreaTypeCode( //
                        itemRegisterRequestDto.isIslandShippingYn() // 도서산간지역 (1권역) 배송여부 ( true : 배송가능 )
                        , itemRegisterRequestDto.isJejuShippingYn()) // 제주지역 (2권역) 배송여부 ( true : 배송가능 )
        );

        /*
         * 품목 코드, 등록자 ID 를 각 상세 항목 정보 ( 상품 영양 정보, 출고처 ... ) 에 일괄 세팅
         */
        goodsItemRegisterService.setItemCodeInDetailInfo(itemRegisterRequestDto);

        itemRegisterRequestDto.setErpStockIfYn(true); // ERP 재고 연동 여부 : 추후 확인 필요

        goodsItemRegisterService.addItem(itemRegisterRequestDto); // IL_GOODS 테이블에 마스터 품목 등록 Insert 쿼리 실행

        /*
         * 품목별 가격정보 원본 Insert 쿼리 실행
         */
        goodsItemRegisterService.addItemPriceOrigin(itemRegisterRequestDto);

        /*
         * 품목별 상품정보 제공고시 상세 항목 Insert 쿼리 실행
         */
        goodsItemRegisterService.addItemSpecValue(itemRegisterRequestDto);

        /*
         * 품목별 상품 영양정보 상세 항목 Insert 쿼리 실행
         */
        goodsItemRegisterService.addItemNutritionDetail(itemRegisterRequestDto);

        /*
         * 품목별 상품 인증정보 Insert 쿼리 실행
         */
        goodsItemRegisterService.addItemCertification(itemRegisterRequestDto);

        /*
         * 품목별 출고처 Insert 쿼리 실행
         */
        goodsItemRegisterService.addItemWarehouse(itemRegisterRequestDto);

        /*
         * 품목별 이미지 Insert 쿼리 실행
         */
        goodsItemRegisterService.addItemImage(itemRegisterRequestDto);

        /*
         * ERP 연동 품목 신규 등록시 : ERP API 의 품목 조회 완료 업데이트 API 호출
         *
         * 해당 API 호출 성공시 : 이후 해당 ERP 연동 품목의 ERP 상의 useOshYn ( 온라인 통합몰 취급 상품 여부 ) 값이 "Y" 로 변경됨
         */
        if (itemRegisterRequestDto.isErpLinkIfYn()) { // ERP 연동 품목 신규 등록인 경우

            goodsItemRegisterService.putErpItemUpdateApi(itemRegisterRequestDto.getIlItemCode());

        }

		//승인관련 처리
		itemApprProc(itemRegisterRequestDto);

        /*
         * 품목 변경내역 저장.
         *
         */
		//상품변경 내역
		int chgLogNum = 0;
		try {
			chgLogNum = itemChangeAssemble("INSERT", itemRegisterRequestDto);
		} catch (BaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


        ItemRegisterResponseDto itemRegisterResponseDto = ItemRegisterResponseDto.builder() //
                .ilItemCode(itemRegisterRequestDto.getIlItemCode()) // 등록된 품목 코드
                .build();

        return ApiResult.success(itemRegisterResponseDto);

    }

  //모든 상품 > 상품 승인 처리
  	private ApiResult<?> itemApprProc(ItemRegisterRequestDto itemRegisterRequestDto) throws Exception {
  		MessageCommEnum enums = BaseEnums.Default.SUCCESS;
  		UserVo userVo = SessionUtil.getBosUserVO();
  		String userId = userVo.getUserId();					// USER ID
  		String companyType = userVo.getCompanyType();		// 회사타입
  		String clientType = userVo.getClientType();			// 거래처 타입

  		if(!itemRegisterRequestDto.getItemApprList().isEmpty()) {

  			ItemRegistApprVo itemRegistApprVo = new ItemRegistApprVo();

  			itemRegistApprVo.setItemNm(itemRegisterRequestDto.getItemName());
  			itemRegistApprVo.setErpIfYn(itemRegisterRequestDto.isErpLinkIfYn());
  			itemRegistApprVo.setErpStockIfYn(itemRegisterRequestDto.getErpStockIfYn());
  			itemRegistApprVo.setIlCtgryStdId(itemRegisterRequestDto.getIlCategoryStandardId());
  			itemRegistApprVo.setUrSupplierId(itemRegisterRequestDto.getUrSupplierId());
  			itemRegistApprVo.setUrBrandId(itemRegisterRequestDto.getUrBrandId());
  			itemRegistApprVo.setStorageMethodTp(itemRegisterRequestDto.getStorageMethodType());
  			itemRegistApprVo.setOriginTp(itemRegisterRequestDto.getOriginType());
  			itemRegistApprVo.setDistributionPeriod(itemRegisterRequestDto.getDistributionPeriod());
  			itemRegistApprVo.setSizePerPackage(itemRegisterRequestDto.getSizePerPackage());
  			itemRegistApprVo.setSizeUnit(itemRegisterRequestDto.getSizeUnit());
  			itemRegistApprVo.setIlSpecMasterId(itemRegisterRequestDto.getIlSpecMasterId());
  			itemRegistApprVo.setItemGrp(itemRegisterRequestDto.getBosItemGroup());

  			for(ItemRegistApprRequestDto itemRegistApprRequestDto : itemRegisterRequestDto.getItemApprList()) {
  				if(itemRegistApprRequestDto.getApprManagerTp().equals(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_1ST.getCode())) {
  					itemRegistApprVo.setApprSubUserId(itemRegistApprRequestDto.getApprUserId());
  				}

  				if(itemRegistApprRequestDto.getApprManagerTp().equals(ApprovalEnums.ApprovalAuthType.APPR_MANAGER_TP_2ND.getCode())) {
  					itemRegistApprVo.setIlItemCd(itemRegisterRequestDto.getIlItemCode());
  					itemRegistApprVo.setApprKindTp(itemRegistApprRequestDto.getApprKindTp());
  					itemRegistApprVo.setApprStat(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
  					itemRegistApprVo.setApprReqUserId(userId);
  					itemRegistApprVo.setApprUserId(itemRegistApprRequestDto.getApprUserId());
  				}
  			}

  			if(companyType.equals(CompanyEnums.CompanyType.HEADQUARTERS.getCode())) {	//관리자 권한이라면
  				itemRegisterRequestDto.setApprKindTp(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ITEM_REG.getCode());
  				ItemRegistApprVo itemApprInfo = goodsItemRegisterService.itemApprInfo(userId, itemRegisterRequestDto.getIlItemCode(), null, itemRegisterRequestDto.getApprKindTp());

  				if(itemApprInfo == null || itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())
  						|| itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode())
  						|| itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DISPOSAL.getCode())) {		//승인 내역이 없거나, 반려/요청철회/폐기 상태일때

  					goodsItemRegisterService.addItemAppr(itemRegistApprVo);

  					itemRegistApprVo.setPrevApprStat(ApprovalEnums.ApprovalStatus.NONE.getCode());
  					itemRegistApprVo.setStatusCmnt(null);

  					goodsItemRegisterService.addItemApprStatusHistory(itemRegistApprVo);

  				}
  				else {
  					enums = GoodsEnums.GoodsApprProcStatus.APPR_DUPLICATE;
  				}
  			}
//  			else if(companyType.equals(CompanyEnums.CompanyType.CLIENT.getCode()) && clientType.equals(CompanyEnums.ClientType.CLIENT.getCode())) {	//거래처 권한이라면
//  				itemRegisterRequestDto.setApprKindTp(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode());
//  				itemRegistApprVo itemApprInfo = goodsRegistService.goodsApprInfo(userId, itemRegisterRequestDto.getIlGoodsId(), null, itemRegisterRequestDto.getApprKindTp());
//
//  				GoodsRegistVo goodsDetail = goodsRegistService.goodsDetail(itemRegisterRequestDto);	//상품 내역
//  				int differentCount = 0;																//거래처 상품 변경 카운트
//
//  				if(goodsDetail != null) {
//
//  					int dateCompare = DateUtil.string2Date(goodsDetail.getModifyDate(), "yyyy-MM-dd HH:mm:ss").compareTo(DateUtil.string2Date(itemRegisterRequestDto.getLoadDateTime(), "yyyy-MM-dd HH:mm:ss"));
//
//  					if(dateCompare > 0
//  							&& !goodsDetail.getSaleStatus().equals(itemRegisterRequestDto.getSaleStatus())) {	//DB의 상품 마스터 수정 시간이 거래처에서 페이지 Load한 시간 보다 크고, 거래처에서 입력한 판매 상태 값과 DB의 판매 상태 값이 다르다면
//  						enums = GoodsEnums.GoodsApprProcStatus.ADMIN_DIFFERENT_GOODS;
//  						itemRegisterRequestDto.setLoadDateTime(null);
//  					}
//  					else {
//  						if(!goodsDetail.getGoodsName().equals(itemRegisterRequestDto.getGoodsName())) {
//  							differentCount++;
//  							itemRegistApprVo.setGoodsName(itemRegisterRequestDto.getGoodsName());
//  						}
//
//  						if(!GoodsEnums.GoodsType.ADDITIONAL.getCode().equals(itemRegisterRequestDto.getGoodsType())
//  								&& !GoodsEnums.GoodsType.GIFT.getCode().equals(itemRegisterRequestDto.getGoodsType())) {		//추가/증정 상품은 아래 항목들이 존재하지 않으므로 제외
//  							if(!StringUtil.nvl(goodsDetail.getGoodsDesc()).equals(StringUtil.nvl(itemRegisterRequestDto.getGoodsDesc()))) {
//  								differentCount++;
//  								itemRegistApprVo.setGoodsDesc(itemRegisterRequestDto.getGoodsDesc());
//  							}
//
//  							if(!StringUtil.nvl(goodsDetail.getSearchKeyword()).equals(StringUtil.nvl(itemRegisterRequestDto.getSearchKeyword()))) {
//  								differentCount++;
//  								itemRegistApprVo.setSearchKeyword(itemRegisterRequestDto.getSearchKeyword());
//  							}
//
//  							if(!goodsDetail.getDisplayYn().equals(itemRegisterRequestDto.getDisplayYn())) {
//  								differentCount++;
//  								itemRegistApprVo.setDisplayYn(itemRegisterRequestDto.getDisplayYn());
//  							}
//
//  							if(!goodsDetail.getSaleStartDate().equals(itemRegisterRequestDto.getSaleStartDate()+":00")) {
//  								differentCount++;
//  								itemRegistApprVo.setSaleStartDate(itemRegisterRequestDto.getSaleStartDate());
//  							}
//
//  							if(!goodsDetail.getSaleEndDate().equals(itemRegisterRequestDto.getSaleEndDate()+":59")) {
//  								differentCount++;
//  								itemRegistApprVo.setSaleEndDate(itemRegisterRequestDto.getSaleEndDate());
//  							}
//  						}
//
//  						if(!goodsDetail.getSaleStatus().equals(itemRegisterRequestDto.getSaleStatus())) {
//  							differentCount++;
//  							itemRegistApprVo.setSaleStatus(itemRegisterRequestDto.getSaleStatus());
//  						}
//
//  						if(!goodsDetail.getGoodsMemo().equals(itemRegisterRequestDto.getGoodsMemo())) {
//  							differentCount++;
//  							itemRegistApprVo.setGoodsMemo(itemRegisterRequestDto.getGoodsMemo());
//  						}
//
//  						if(differentCount == 0) {
//  							enums = GoodsEnums.GoodsApprProcStatus.NOT_DIFFERENT_GOODS;
//  						}
//  						else if(differentCount > 0){
//  							if(itemApprInfo == null || itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DENIED.getCode())
//  									|| itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.CANCEL.getCode())
//  									|| itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.DISPOSAL.getCode())
//  									|| itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.APPROVED.getCode())) {	//승인 내역이 없거나, 반려/요청철회/폐기/승인완료 상태일때
//  								goodsRegistService.addGoodsAppr(itemRegistApprVo);
//
//  								itemRegistApprVo.setPrevApprStat(ApprovalEnums.ApprovalStatus.NONE.getCode());
//  								itemRegistApprVo.setStatusCmnt(null);
//
//  								goodsRegistService.addGoodsApprStatusHistory(itemRegistApprVo);
//
//  								if(goodsDetail.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode())) {		//승인 요청 시에 상품의 판매 상태가 '판매중'이라면
//
//  									itemRegistApprVo goodsApprSaleStatusVo = new itemRegistApprVo();
//
//  									goodsApprSaleStatusVo.setIlGoodsId(itemRegistApprVo.getIlGoodsId());
//  									goodsApprSaleStatusVo.setSaleStatus(GoodsEnums.SaleStatus.WAIT.getCode());						//판매 상태를 '판매 대기'로 변경
//  									goodsApprSaleStatusVo.setSavedSaleStatus(GoodsEnums.SaleStatus.ON_SALE.getCode());				//현재 판매 상태를 저장
//  									goodsRegistService.updateGoodsRequestSaleStatus(itemRegistApprVo);
//  								}
//  								enums = GoodsEnums.GoodsApprProcStatus.CLINET_APPR_REQUEST;
//  							}
//  							else {
//  								enums = GoodsEnums.GoodsApprProcStatus.APPR_DUPLICATE;
//  							}
//  						}
//  					}
//  				}
//  				else {
//  					enums = GoodsEnums.GoodsApprProcStatus.NONE_GOODS_ID;
//  				}
//  			}
  		}
  		else {
  			if(companyType.equals(CompanyEnums.CompanyType.HEADQUARTERS.getCode())) {	//관리자 권한이라면
  				itemRegisterRequestDto.setApprKindTp(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_ITEM_CLIENT.getCode());
  				ItemRegistApprVo itemApprInfo = goodsItemRegisterService.itemApprInfo(userId, itemRegisterRequestDto.getIlItemCode(), null, itemRegisterRequestDto.getApprKindTp());

  				if(itemApprInfo != null && itemApprInfo.getApprStat().equals(ApprovalEnums.ApprovalStatus.REQUEST.getCode())) {		//거래처 상품 수정 승인 요청 내역이 있다면
  					enums = ItemEnums.ItemApprProcStatus.CLIENT_APPR_DUPLICATE;
  				}
  			}
  			else if(companyType.equals(CompanyEnums.CompanyType.CLIENT.getCode()) && clientType.equals(CompanyEnums.ClientType.CLIENT.getCode())) {	//거래처 권한이라면
  				enums = ItemEnums.ItemApprProcStatus.NONE_APPR;
  			}
  		}

  		return ApiResult.result(enums);
  	}

    //품목변경내역 취합
  	private int itemChangeAssemble(String tranMode, ItemRegisterRequestDto afterItemDatas) throws BaseException, Exception {

  		int chgLogNum = 0;

  		//상품 마스터 변경 내역
  		MasterItemVo beforeItemDatas = new MasterItemVo();

  		Map<String, String> beforeItemData = BeanUtils.describe(beforeItemDatas);
  		Map<String, String> afterItemData = BeanUtils.describe(afterItemDatas);

  		String timestamp = DateUtil.getCurrentDate("yyyyMMddHHmmss");

  		chgLogNum += addItemMasterChangeLog(tranMode, afterItemDatas.getIlItemCode(), beforeItemData, afterItemData, timestamp);

  		// 품목 정보제공고시
  		List<ItemSpecValueRequestDto> beforeItemSpecList = new ArrayList<ItemSpecValueRequestDto>();
  		List<ItemSpecValueRequestDto> afterItemSpecList = new ArrayList<ItemSpecValueRequestDto>();
  		afterItemSpecList.addAll(afterItemDatas.getAddItemSpecValueList());

  		chgLogNum += addItemSubChangeLog(tranMode, "IL_ITEM_SPEC_VALUE", afterItemDatas.getIlItemCode(), beforeItemSpecList, afterItemSpecList, timestamp);

  		// 인증정보
  		List<ItemCertificationDto> beforeItemCertList = new ArrayList<ItemCertificationDto>();
  		List<ItemCertificationDto> afterItemCertList = new ArrayList<ItemCertificationDto>();
  		afterItemCertList.addAll(afterItemDatas.getAddItemCertificationList());

  		chgLogNum += addItemSubChangeLog(tranMode, "IL_ITEM_CERTIFICATION", afterItemDatas.getIlItemCode(), beforeItemCertList, afterItemCertList, timestamp);

  		// 출고처 정보
  		List<ItemWarehouseDto> beforeItemWarehouseList = new ArrayList<ItemWarehouseDto>();
  		List<ItemWarehouseDto> afterItemWarehouseList = new ArrayList<ItemWarehouseDto>();
  		afterItemWarehouseList.addAll(afterItemDatas.getAddItemWarehouseList());

  		chgLogNum += addItemSubChangeLog(tranMode, "IL_ITEM_WAREHOUSE", afterItemDatas.getIlItemCode(), beforeItemWarehouseList, afterItemWarehouseList, timestamp);

  		// 영양정보 단위 저장
//  		List<ItemNutritionDetailDto> beforeItemNutritionList_1 = new ArrayList<ItemNutritionDetailDto>();
//  		List<ItemNutritionDetailDto> afterItemNutritionList_1 = new ArrayList<ItemNutritionDetailDto>();
//  		afterItemNutritionList_1.addAll(afterItemDatas.getAddItemNutritionDetailList());
//
//  		System.out.println("###### nurt Size --->"+ afterItemNutritionList_1.size());
//
//  		chgLogNum += addItemSubChangeLog(tranMode, "IL_ITEM_NUTRITION_UNIT", afterItemDatas.getIlItemCode(), beforeItemNutritionList_1, afterItemNutritionList_1);


  		return chgLogNum;
  	}

  	/**
	 * 품목 마스터 변경내역 저장
	 * tranMode : INSERT, UPDATE 구분
	 * beforeGoodsDatas : 현재 DB에 저장되어 있는 값
	 * afterGoodsDatas : 화면에서 가져온 RequestDto 값
	 **/
	private int addItemMasterChangeLog(String tranMode, String ilItemCode, Map<String, String> beforeItemDatas, Map<String, String> afterItemDatas, String timestamp) throws BaseException, Exception {

		changeTranNum = 0;
		UserVo userVo = SessionUtil.getBosUserVO();
		String userId = userVo.getUserId();					// USER ID
		if(!afterItemDatas.isEmpty()) {
			afterItemDatas.forEach((afterKey, afterValue)-> {
				ItemColumnComment itemColumnComment = ItemColumnComment.findByComment(afterKey);

				if("INSERT".equals(tranMode) && afterValue != null && itemColumnComment != null) {
					ItemChangeLogVo itemChangeLogVo = ItemChangeLogVo.builder()
						.ilItemCd(ilItemCode)
						.tableNm("IL_ITEM")
						.tableIdOrig("0")
						.tableIdNew(ilItemCode)
						.beforeData("")		//저장시에는 beforeData는 없음
						.afterData(afterValue)
						.columnNm(afterKey)
						.columnLabel(itemColumnComment.getCodeName())
						.createId(userId)
						.createDt(timestamp)
						.build();

					//람다식 사용으로 인해서 Class 단위 전역변수로 선언 해야함
					changeTranNum += goodsItemRegisterService.addItemChangeLog(itemChangeLogVo);
				}
				else if("UPDATE".equals(tranMode) && itemColumnComment != null) {

					beforeItemDatas.forEach((beforeKey, beforeValue)-> {

						if(afterValue != null && afterKey.equals(beforeKey) && !afterValue.equals(beforeValue)) {

							log.info("beforeKey : " + beforeKey + "  beforeValue : " + beforeValue + "// afterKey : " + afterKey + "  afterValue : " + afterValue);

							ItemChangeLogVo itemChangeLogVo = ItemChangeLogVo.builder()
								.ilItemCd(ilItemCode)
								.tableNm("IL_ITEM")
								.tableIdOrig(ilItemCode)
								.tableIdNew(ilItemCode)
								.beforeData(beforeValue)
								.afterData(afterValue)
								.columnNm(afterKey)
								.columnLabel(itemColumnComment.getCodeName())
								.createId(userId)
								.createDt(timestamp)
								.build();

							//람다식 사용으로 인해서 Class 단위 전역변수로 선언 해야함
							changeTranNum += goodsItemRegisterService.addItemChangeLog(itemChangeLogVo);
						}
					});
				}
			});
		}
		return changeTranNum;
	}

	/**
	 * 품목서브 항목 변경내역 저장
	 * tranMode : INSERT, UPDATE 구분
	 * tableKind : 변경내역 저장 테이블명
	 * beforeGoodsDatas : 현재 DB에 저장되어 있는 값
	 * afterGoodsDatas : 화면에서 가져온 RequestDto 값
	 **/
	private int addItemSubChangeLog(String tranMode, String tableKind, String ilItemCode, List<?> beforeItemDatas, List<?> afterItemDatas, String timestamp) throws BaseException, Exception {
		//테이블별 ID , DATA , COLUMN COMMENT 컬럼을 지정
		ItemEtcColumnComment = ItemEtcColumnComment.findByInfo(tableKind);
		sameValue = 0;

		if(tableKind.equals("IL_ITEM_NUTRITION_UNIT")) {
			tableKind = "IL_ITEM_NUTRITION";
		}

		UserVo userVo = SessionUtil.getBosUserVO();
		String userId = userVo.getUserId();					// USER ID

		if("INSERT".equals(tranMode) && afterItemDatas != null) {
			for (Object afterGoodsValue : afterItemDatas) {
				Map<String, String> afterDataMap = BeanUtils.describe(afterGoodsValue);

				ItemChangeLogVo itemChangeLogVo = new ItemChangeLogVo();
				itemChangeLogVo.setIlItemCd(ilItemCode);
				itemChangeLogVo.setTableNm(tableKind);
				itemChangeLogVo.setColumnLabel(ItemEtcColumnComment.getComment());
				itemChangeLogVo.setCreateId(userId);
				itemChangeLogVo.setCreateDt(timestamp);
				afterDataMap.forEach((afterKey, afterValue)-> {
					if(ItemEtcColumnComment != null) {
						if(ItemEtcColumnComment.getIdColumn().equals(afterKey)){
							itemChangeLogVo.setTableIdOrig("0");
							itemChangeLogVo.setTableIdNew(afterValue);
							itemChangeLogVo.setColumnNm(afterKey);
						}

						if(ItemEtcColumnComment.getDataColumn().equals(afterKey)){
							itemChangeLogVo.setBeforeData("");
							itemChangeLogVo.setAfterData(afterValue);
						}
					}
				});

				changeTranNum += goodsItemRegisterService.addItemChangeLog(itemChangeLogVo);
			}
		}

		return changeTranNum;
	}

    /**
     * @Desc ( 마스터 품목 등록 ) 신규 품목 이미지 등록시 해당 품목 이미지의 리사이징 이미지 파일들 생성 / 각 리사이징 파일명 VO 에 세팅
     *
     *       마스터 품목 수정시에도 사용할 수 있도록 Biz 에 등록함
     *
     * @param ItemImageRegisterVo : Insert 할 품목 이미지 Vo
     * @param UploadFileDto       : 해당 품목 이미지 업로드 결과 Dto
     * @param String              : 품목 이미지를 저장할 Public 저장소의 최상위 저장 디렉토리 경로
     */
    @Override
    public void resizeItemImage(ItemImageRegisterVo addItemImageVo, UploadFileDto uploadFileDto, String publicRootStoragePath) {

        goodsItemRegisterService.resizeItemImage(addItemImageVo, uploadFileDto, publicRootStoragePath);

    }

	@Override
	public ApiResult<?> getOrgaPoApi(String ilItemCode) {
		// TODO Auto-generated method stub
		List<ErpIfGoodsOrgaPoResponseDto> erpIfGoodsOrgaPoResponseList = null;
        ErpIfGoodsOrgaPoListResponseDto erpIfGoodsOrgaPoListResponseDto = null;

        try {

        	erpIfGoodsOrgaPoResponseList = goodsItemRegisterService.getOrgaPoApi(ilItemCode);

        	erpIfGoodsOrgaPoListResponseDto = ErpIfGoodsOrgaPoListResponseDto.builder() //
                    .erpIfGoodsOrgaPoListDto(erpIfGoodsOrgaPoResponseList)
                    .build();

            return ApiResult.success(erpIfGoodsOrgaPoListResponseDto);

        } catch (BaseException be) { // getErpPriceApi 서비스 내에서 BaseException 발생시
            log.error(be.getMessage(), be);
            return ApiResult.result(be.getMessageEnum());

        } catch (Exception e) { // 기타 예외 발생시
            log.error(e.getMessage(), e);
            throw e;

        }
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BaseException.class, Exception.class })
	public ApiResult<?> addItemPriceOrig(ItemRegisterRequestDto itemRegisterRequestDto) throws Exception {
		// TODO Auto-generated method stub
		List<ItemPriceVo> itemPrice = itemRegisterRequestDto.getPriceList();

		goodsItemRegisterService.addItemPriceOrig(itemRegisterRequestDto, itemPrice);

        // 추후 확장성을 위해 response dto 사용
        ItemModifyResponseDto itemModifyResponseDto = ItemModifyResponseDto.builder() //
                .build();

    	return ApiResult.success(itemModifyResponseDto);
	}

    /*
     * 마스터 품목 등록 관련 로직 End
     */

}
