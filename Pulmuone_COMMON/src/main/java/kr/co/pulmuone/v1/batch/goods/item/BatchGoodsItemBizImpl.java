package kr.co.pulmuone.v1.batch.goods.item;

import kr.co.pulmuone.v1.batch.goods.item.dto.ErpGoodsItemFlagHeaderCondRequestDto;
import kr.co.pulmuone.v1.batch.goods.item.dto.ErpGoodsItemFlagHeaderReqeustDto;
import kr.co.pulmuone.v1.comm.api.constant.LegalTypes;
import kr.co.pulmuone.v1.comm.api.constant.PurchaseOrderTypes;
import kr.co.pulmuone.v1.comm.api.constant.TaxTypes;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfGoodSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfNutritionSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.constants.ApiConstants;
import kr.co.pulmuone.v1.comm.enums.ItemEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <PRE>
 * Forbiz Korea
 * ERP API 조회 후 ERP 연동 품목 정보 일괄 업데이트 Biz
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0      20201109            박주형         최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class BatchGoodsItemBizImpl implements BatchGoodsItemBiz {

    // ERP API 에서 품목 정보 조회 인터페이스 ID
    private static final String ITEM_SEARCH_INTERFACE_ID = "IF_GOODS_SRCH";

    // ERP API 에서 영양 정보 조회 인터페이스 ID
    private static final String NUTRITION_SEARCH_INTERFACE_ID = "IF_NUTRI_SRCH";

    @Autowired
    private BatchGoodsItemService batchGoodsItemService;

    @Autowired
    private ErpApiExchangeService erpApiExchangeService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
    public void modifyErpItemJob() throws BaseException {

        BaseApiResponseVo baseApiResponseVo = null;
        List<ErpIfGoodSearchResponseDto> eachPageList = null;
        List<ErpIfGoodSearchResponseDto> totalErpGoodsList = new ArrayList<>();
        Map<String, String> parameterMap = new HashMap<>();

        List<MasterItemVo> masterItemVoList = new ArrayList<>();
        List<ErpIfNutritionSearchResponseDto> nutritionInfoList = null;

        // 한국 기준 현재 일자 / 익일 일자 계산
        LocalDateTime currentNow = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        String today = currentNow.format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 현재 일자
        // 조회조건 : 현재일-1 ~ 현재일
        String startDate = currentNow.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 검색 시작 일자
        String endDate = currentNow.plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 검색 종료 일자

        // 온라인 통합몰 취급상품 중 현재 일자로 업데이트된 품목 정보 목록 조회
        parameterMap.put("useOshYn", "Y"); // 온라인 통합몰 취급 상품 여부 : "Y"
        parameterMap.put("updDat", startDate + "~" + endDate); // 업데이트 일시 : 현재 일자 ~ 익일 일자    // 임시로 주석처리
//        parameterMap.put("itmNam", "%미역국%");
//        parameterMap.put("legCd", "OGH");
//        parameterMap.put("itmNo", "0900079");
        // 현재일자 기준으로 수정된 품목 정보 최초 페이지 조회
        baseApiResponseVo = erpApiExchangeService.get(parameterMap, ITEM_SEARCH_INTERFACE_ID);

        if (!baseApiResponseVo.isSuccess()) { // ERP API 통신 실패시
            throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
        }

        eachPageList = baseApiResponseVo.deserialize(ErpIfGoodSearchResponseDto.class);

        totalErpGoodsList.addAll(eachPageList);

        int startPage = baseApiResponseVo.getCurrentPage(); // 최초 조회한 페이지 ( 1 페이지 )
        int totalPage = baseApiResponseVo.getTotalPage(); // 해당 검색조건으로 조회시 전체 페이지 수

        // 다음 페이지 존재시
        if(totalPage > 1) {
            // 최초 조회한 페이지의 다음 페이지부터 조회
            for (int page = startPage+1; page <= totalPage; page++) {
            	parameterMap.put("page", String.valueOf(page));

            	try {
            	    baseApiResponseVo = erpApiExchangeService.get(parameterMap, ITEM_SEARCH_INTERFACE_ID);

                    if (!baseApiResponseVo.isSuccess()) { // ERP API 통신 실패시
                        throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
                    }

                    eachPageList = baseApiResponseVo.deserialize(ErpIfGoodSearchResponseDto.class);
                } catch (Exception e) { // ERP API 통신 실패시 : BaseException 으로 변환 후 throw 처리
                    throw new BaseException(e.getMessage());
                }
                totalErpGoodsList.addAll(eachPageList);
            }

        }

        for (ErpIfGoodSearchResponseDto erpIfGoodSearchResponseDto : totalErpGoodsList) {

            // ERP 품목 수정 Vo 생성
            MasterItemVo itemVo = MasterItemVo.builder() //
                    .ilItemCode(erpIfGoodSearchResponseDto.getErpItemNo()) //
                    .erpItemName(erpIfGoodSearchResponseDto.getErpItemName()) //
                    .erpItemBarcode(erpIfGoodSearchResponseDto.getErpItemBarcode()) //
                    .erpCategoryLevel1Id(erpIfGoodSearchResponseDto.getErpCategoryLevel1Id()) //
                    .erpCategoryLevel2Id(erpIfGoodSearchResponseDto.getErpCategoryLevel2Id()) //
                    .erpCategoryLevel3Id(erpIfGoodSearchResponseDto.getErpCategoryLevel3Id()) //
                    .erpCategoryLevel4Id(erpIfGoodSearchResponseDto.getErpCategoryLevel4Id()) //
                    .erpBrandName(erpIfGoodSearchResponseDto.getErpBrandName()) //
                    .erpItemGroup(erpIfGoodSearchResponseDto.getErpItemGroup()) //
                    .erpOriginName(erpIfGoodSearchResponseDto.getErpOriginName()) //
                    .erpSupplierName(erpIfGoodSearchResponseDto.getErpSupplierName())//
                    .erpDistributionPeriod(erpIfGoodSearchResponseDto.getErpDistributionPeriod()) //
                    .erpBoxWidth(erpIfGoodSearchResponseDto.getErpBoxWidth()) //
                    .erpBoxHeight(erpIfGoodSearchResponseDto.getErpBoxHeight()) //
                    .erpBoxDepth(erpIfGoodSearchResponseDto.getErpBoxDepth()) //
                    .erpItemWidth(erpIfGoodSearchResponseDto.getErpItemWidth()) //
                    .erpItemDepth(erpIfGoodSearchResponseDto.getErpItemDepth()) //
                    .erpItemHeight(erpIfGoodSearchResponseDto.getErpItemHeight()) //
                    .erpPiecesPerBox(erpIfGoodSearchResponseDto.getErpPiecesPerBox()) //
                    .erpCanPoYn(erpIfGoodSearchResponseDto.getErpCanPoYn()) //
                    .modifyId(Long.valueOf("0")) // 수정자 ID : 추후 관련 배치 ID 로 변경 예정
                    .build();

            // ERP 보관방법
            if (erpIfGoodSearchResponseDto.getErpStorageMethod() != null) {
                itemVo.setErpStorageMethod(erpIfGoodSearchResponseDto.getErpStorageMethod().getCode()); //
            }

            // ERP 과세구분
            if (erpIfGoodSearchResponseDto.getErpTaxType() != null) {
                itemVo.setErpTaxYn(TaxTypes.hasTax(erpIfGoodSearchResponseDto.getErpTaxType())); //
            }

            // ERP 법인코드
            if (erpIfGoodSearchResponseDto.getErpLegalType() != null) {
                itemVo.setErpLegalTypeCode(erpIfGoodSearchResponseDto.getErpLegalType().getCode()); //
            }

            // ERP 발주유형 : PurchaseOrderTypes enum 에 선언된 올가 / 푸드머스 관련 코드인 경우에만 세팅
            if (erpIfGoodSearchResponseDto.getErpPoType() != null) {
                itemVo.setErpPoType(erpIfGoodSearchResponseDto.getErpPoType().getCode());
            }

            // 올가 ERP : 매장 품목 유형 세팅
            if (erpIfGoodSearchResponseDto.getErpLegalType() == LegalTypes.ORGA && erpIfGoodSearchResponseDto.getO2oExposureType() != null) {
                itemVo.setErpO2oExposureType(erpIfGoodSearchResponseDto.getO2oExposureType().getCodeName());
            }

            // 건강생활 ERP : 상품 판매 유형 세팅
            if (erpIfGoodSearchResponseDto.getErpLegalType() == LegalTypes.LOHAS && erpIfGoodSearchResponseDto.getProductType() != null) {
                itemVo.setErpProductType(erpIfGoodSearchResponseDto.getProductType().getCode());

            }

            /*
             * 영양정보 API 조회
             */
            parameterMap.clear();
            parameterMap.put("itmNo", erpIfGoodSearchResponseDto.getErpItemNo());

            baseApiResponseVo = erpApiExchangeService.get(parameterMap, NUTRITION_SEARCH_INTERFACE_ID);

            if (!baseApiResponseVo.isSuccess()) { // ERP API 통신 실패시
                throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
            }

            nutritionInfoList = baseApiResponseVo.deserialize(ErpIfNutritionSearchResponseDto.class);

            if (!nutritionInfoList.isEmpty()) {

                itemVo.setErpNutritionQtyPerOnce(nutritionInfoList.get(0).getServingsize()); // ERP 영양분석단위 (1회분량)
                itemVo.setErpNutritionQtyTotal(nutritionInfoList.get(0).getServingContainer()); // ERP 영양분석단위 (총분량)
                itemVo.setErpNutritionEtc(nutritionInfoList.get(0).getErpNutritionEtc()); // ERP 영양성분 기타

            }

            masterItemVoList.add(itemVo);

        }

        /*
         * ERP 품목 정보 수정
         */
        for (MasterItemVo erpItemVo : masterItemVoList) {
        	MasterItemVo curItemVo = batchGoodsItemService.getItemByItemCd(erpItemVo.getIlItemCode());
        	if (curItemVo != null && curItemVo.getIlItemCode() != null) {
            	batchGoodsItemService.modifyErpItem(erpItemVo);

            	// 올가 품목의 경우 발주유형이 R1, R2인 경우 발주유형을 현행화한다.
            	String curErpPoType = curItemVo.getErpPoType();
            	if (curErpPoType == null) curErpPoType = "";
            	String curErpCategoryLevel1Id = curItemVo.getErpCategoryLevel1Id();
            	if (curErpCategoryLevel1Id == null) curErpCategoryLevel1Id = "";
            	String udpErpPoType = erpItemVo.getErpPoType();
            	if (udpErpPoType == null) udpErpPoType = "";
            	String udpErpCategoryLevel1Id = erpItemVo.getErpCategoryLevel1Id();
            	if (udpErpCategoryLevel1Id == null) udpErpCategoryLevel1Id = "";

            	if (
            		"OG".equals(curItemVo.getSupplierCd()) // 올가
            		&& (
            			PurchaseOrderTypes.OGH_R1.getCode().equals(curItemVo.getErpPoType()) // 현재 발주유형이 R1
            			|| PurchaseOrderTypes.OGH_R2.getCode().equals(curItemVo.getErpPoType()) // 현재 발주유형이 R2
            			|| PurchaseOrderTypes.OGH_R1.getCode().equals(erpItemVo.getErpPoType()) // 업데이트 발주유형이 R1
            			|| PurchaseOrderTypes.OGH_R2.getCode().equals(erpItemVo.getErpPoType()) // 업데이트 발주유형이 R2
            		)
            		&& (
        				!curErpPoType.equals(udpErpPoType) // 현재 발주유형과 업데이트 발주유형이 다를 경우
        				|| (curErpPoType.equals(udpErpPoType) && !curErpCategoryLevel1Id.equals(udpErpCategoryLevel1Id)) // 현재 발주유형과 업데이트 발주유형은 같으나 ERP대카테고리가 서로 다를 경우
					)
            	) {
            		String ilPoTpId;
        			String preOrderYn = "N";
        			String preOrderTp = "";
            		// 업데이트 발주유형이 R1, R2인 경우 새로이 셋팅할 발주유형 ID를 가져온다. 그렇지 않으면 null로 초기화
            		if (PurchaseOrderTypes.OGH_R1.getCode().equals(udpErpPoType) || PurchaseOrderTypes.OGH_R2.getCode().equals(udpErpPoType)) {
            			String poTp;
            			if (PurchaseOrderTypes.OGH_R1.getCode().equals(udpErpPoType)) {
            				poTp = "ERP_PO_TP.R1"; // R1 공통코드
            				if (!"".equals(udpErpCategoryLevel1Id) && !"채소".equals(udpErpCategoryLevel1Id)) {
            					preOrderYn = "Y";
            					if ("과일".equals(udpErpCategoryLevel1Id)) {
            						preOrderTp = "PRE_ORDER_TP.UNLIMITED_ALLOWED";
            					} else {
            						preOrderTp = "PRE_ORDER_TP.LIMITED_ALLOWED";
            					}
            				}
            			} else {
            				poTp = "ERP_PO_TP.R2"; // R2 공통코드
            			}
                    	ilPoTpId = batchGoodsItemService.getPoTpIdByErpPoTp(poTp);
            		} else {
            			ilPoTpId = null;
            		}
                	batchGoodsItemService.updatePoInfo(erpItemVo.getIlItemCode(), ilPoTpId, preOrderYn, preOrderTp);
            	}
        	}
        }

        /*
         * ERP API 전송.
         */
        if(masterItemVoList.size() > 0) {
    		ErpGoodsItemFlagHeaderReqeustDto requestDto = ErpGoodsItemFlagHeaderReqeustDto.builder()
					.totalPage(1)
					.currentPage(1)
					.build();

    		List<ErpGoodsItemFlagHeaderCondRequestDto> headerList = new ArrayList<>();
    		for(MasterItemVo erpItemVo : masterItemVoList) {

    			ErpGoodsItemFlagHeaderCondRequestDto goodsTermVo = new ErpGoodsItemFlagHeaderCondRequestDto();
    			goodsTermVo.setItmNo(erpItemVo.getIlItemCode());
    			headerList.add(goodsTermVo);

                requestDto.setHeader(headerList);
                baseApiResponseVo = erpApiExchangeService.put(requestDto, ApiConstants.IF_GOODS_FLAG);
    		}
    	}
    }
}
