package kr.co.pulmuone.v1.batch.goods.item;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.goods.item.dto.ItemPriceApprovalRequestDto;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.batch.goods.item.dto.ErpGoodsPriceHeaderRequestDto;

import kr.co.pulmuone.v1.batch.goods.item.vo.ErpGoodsPriceTermResultVo;
import kr.co.pulmuone.v1.comm.api.constant.LegalTypes;
import kr.co.pulmuone.v1.comm.api.constant.ProductTypes;
import kr.co.pulmuone.v1.comm.api.constant.SupplierTypes;
import kr.co.pulmuone.v1.comm.api.constant.TaxInvoiceTypes;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfGoodSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfPriceSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.constants.ApiConstants;
import kr.co.pulmuone.v1.comm.enums.ItemEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemVo;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceIfTempVo;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceOrigVo;

/**
 * <PRE>
 * Forbiz Korea
 * ERP API 조회 후 ERP 품목 가격정보 업데이트
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0      2021-02-24       박주형         최초작성
 *  1.1      2021-07-20       남기승   		건강생활 시식/증정/기타 품목등록가능하게 수정
 * =======================================================================
 * </PRE>
 */

@Service
public class BatchGoodsItemPriceBizImpl implements BatchGoodsItemPriceBiz{

	private static final Logger log = LoggerFactory.getLogger(BatchGoodsItem3PLBizImpl.class);

    @Autowired
    private BatchGoodsItemPriceService batchGoodsItemPriceService;

    @Autowired
    ErpApiExchangeService erpApiExchangeService;


	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void modifyGoodsItemPriceJob() throws BaseException {

		Map<String, String> parameterMap = new HashMap<>();

    	batchGoodsItemPriceService.delItemPriceIfTemp(); // 품목가격 연동용 임시 테이블 데이터 삭제(batch 오류에 의해 찌꺼기 데이터가 남아 있을수 있음)

		parameterMap.put("updFlg", "Y");

		LocalDate now = LocalDate.now();//현재일짜
        String baseDt = now.toString().replaceAll("-", "");//조회기간
        String updDat = baseDt + "000000~" + baseDt + "235959";
        parameterMap.put("updDat", updDat);
//        parameterMap.put("itmNo", "0040102"); // PFF
//        parameterMap.put("itmNo", "0053977");  // PGS - 제품
//        parameterMap.put("itmNo", "0060092");  // PGS - 상품
//        parameterMap.put("itmNo", "0800011");  // PGS - 상품
//        parameterMap.put("itmNo", "0130160");  // PGS - 상품
//        parameterMap.put("itmNo", "0922301");  // PGS - 상품
//        parameterMap.put("itmNo", "0060614");  // PGS - 상품
//		parameterMap.put("itmNo", "0300059");  // 품목만 존재. 상품 없음.
        parameterMap.put("target", "header-ITF");
		parameterMap.put("salTyp", "정상");

        /*
         * 품목명으로 조회시 여러 페이지가 검색될 수 있음 : 최초 1페이지 조회 후 전체 페이지가 1 보다 큰 경우 각 페이지별로 추가 조회
         */
        BaseApiResponseVo baseApiResponseVo = null;

        List<ErpIfPriceSearchResponseDto> eachPageDtoList = null;
        List<ErpIfPriceSearchResponseDto> erpItemPriceApiList = new ArrayList<>();

        // 최초 1페이지 조회
        baseApiResponseVo = erpApiExchangeService.get(parameterMap, ApiConstants.IF_PRICE_SRCH); // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체

        if (!baseApiResponseVo.isSuccess()) { // 최초 1페이지 조회에서 ERP API 통신 실패시
            throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
        }

        // 최초 1페이지는 전체 데이터 취합 목록에 바로 반영
        eachPageDtoList = baseApiResponseVo.deserialize(ErpIfPriceSearchResponseDto.class); // baseApiResponseVo => List<T> 역직렬화
        for(ErpIfPriceSearchResponseDto priceInfo : eachPageDtoList) {

        	if(priceInfo.getErpPriceApplyStartDate() == null || "".equals(priceInfo.getErpPriceApplyStartDate())) {
        		continue;
        	}

        	if(priceInfo.getErpStandardPrice() == null || "".equals(priceInfo.getErpStandardPrice())) {
        		continue;
        	}

        	if(priceInfo.getErpRecommendedPrice() == null || "".equals(priceInfo.getErpRecommendedPrice()) || "0".equals(priceInfo.getErpRecommendedPrice())) {
        		continue;
        	}

    		ItemPriceIfTempVo priceTempVo = new ItemPriceIfTempVo();
    		priceTempVo.setIfSeq(priceInfo.getIfSeq());
    		priceTempVo.setIlItemCd(priceInfo.getErpItemNo());
    		priceTempVo.setStartDt(priceInfo.getErpPriceApplyStartDate());
    		priceTempVo.setStandardPrice(priceInfo.getErpStandardPrice());
    		priceTempVo.setRecommendedPrice(priceInfo.getErpRecommendedPrice());
    		priceTempVo.setSaleType(priceInfo.getErpSalesType().getCode());
    		batchGoodsItemPriceService.addItemPriceIfTemp(priceTempVo);
    	}

        erpItemPriceApiList.addAll(eachPageDtoList);

        if (erpItemPriceApiList.isEmpty()) { // 해당 품목명으로 데이터 미조회시 빈 배열 반환
        	return;
        }

        if (baseApiResponseVo.getTotalPage() != null && baseApiResponseVo.getTotalPage() > 1) { // 전체 페이지 수가 1 보다 큰 경우
            for (int page = 2; page <= baseApiResponseVo.getTotalPage(); page++) { // 2 페이지부터 전체 페이지 수까지 추가 조회
                parameterMap.put("page", String.valueOf(page)); // 페이지 조회조건 추가

                baseApiResponseVo = erpApiExchangeService.get(parameterMap, ApiConstants.IF_PRICE_SRCH);

                if (!baseApiResponseVo.isSuccess()) { // 각 페이지별 조회에서 ERP API 통신 실패시
                    throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
                }

                eachPageDtoList = baseApiResponseVo.deserialize(ErpIfPriceSearchResponseDto.class); // 각 페이지별 dto 변환

                for(ErpIfPriceSearchResponseDto priceInfo : eachPageDtoList) {
                	if(priceInfo.getErpPriceApplyStartDate() == null || "".equals(priceInfo.getErpPriceApplyStartDate())) {
                		continue;
                	}

                	if(priceInfo.getErpStandardPrice() == null || "".equals(priceInfo.getErpStandardPrice())) {
                		continue;
                	}

                	if(priceInfo.getErpRecommendedPrice() == null || "".equals(priceInfo.getErpRecommendedPrice()) || "0".equals(priceInfo.getErpRecommendedPrice())) {
                		continue;
                	}

            		ItemPriceIfTempVo priceTempVo = new ItemPriceIfTempVo();
            		priceTempVo.setIfSeq(priceInfo.getIfSeq());
            		priceTempVo.setIlItemCd(priceInfo.getErpItemNo());
            		priceTempVo.setStartDt(priceInfo.getErpPriceApplyStartDate());
            		priceTempVo.setStandardPrice(priceInfo.getErpStandardPrice());
            		priceTempVo.setRecommendedPrice(priceInfo.getErpRecommendedPrice());
            		priceTempVo.setSaleType(priceInfo.getErpSalesType().getCode());
            		batchGoodsItemPriceService.addItemPriceIfTemp(priceTempVo);
            	}

                erpItemPriceApiList.addAll(eachPageDtoList); // 전체 품목 dto 목록에 취합

            }
        }

        List<ItemPriceIfTempVo> priceTempList = batchGoodsItemPriceService.getItemPriceIfTempList();

        if(priceTempList.size() > 0) {
        	for(ItemPriceIfTempVo priceInfo : priceTempList) {

        		// 미래 품목가격 보정 S
    			// batchGoodsItemPriceService.getItemPriceIfTempList()에서 가격 정보 연동이 필요한 품목만 가지고 온다.(조건은 해당 쿼리 참조)
            	DecimalFormat df=new DecimalFormat("#.##");
    			if (priceInfo.getStandardPrice() != null && !"".equals(priceInfo.getStandardPrice())) {
    				priceInfo.setStandardPrice(df.format(Math.floor(Double.parseDouble(priceInfo.getStandardPrice()))));
    			}
    			else
    				continue;

    			if (priceInfo.getRecommendedPrice() != null && !"".equals(priceInfo.getRecommendedPrice())) {
    				priceInfo.setRecommendedPrice(df.format(Math.floor(Double.parseDouble(priceInfo.getRecommendedPrice()))));
    				if (Integer.parseInt(priceInfo.getRecommendedPrice()) <= 0) // 정상가는 0원보다 클때만.
    					continue;
    			}
    			else
    				continue;

				ItemPriceOrigVo itemPriceOrigVo = new ItemPriceOrigVo();

				itemPriceOrigVo.setIlItemCode(priceInfo.getIlItemCd());
				itemPriceOrigVo.setStartDate(priceInfo.getStartDt());
				itemPriceOrigVo.setStandardPrice(priceInfo.getStandardPrice());
				itemPriceOrigVo.setRecommendedPrice(priceInfo.getRecommendedPrice());
				itemPriceOrigVo.setCreateId(0);
				itemPriceOrigVo.setModifyId(0);
				itemPriceOrigVo.setSystemUpdateYn("Y");
    			if(!ItemEnums.priceManageType.N.getCode().equals(priceInfo.getPriceManageTp())) { // 'N'인경우는 값을 저장안함.
    				itemPriceOrigVo.setPriceManageTp(priceInfo.getPriceManageTp());
        		}

    			// system 자동 insert 도 approve 내역 필요함_2021.07.28 S
    			ItemPriceOrigVo reItemPriceOrigVo = batchGoodsItemPriceService.getItemPriceOrigLastly(itemPriceOrigVo.getIlItemCode()); // 현재 가격 조회
    			ItemPriceApprovalRequestDto itemPriceAppr = ItemPriceApprovalRequestDto.builder()
						.ilItemCode(itemPriceOrigVo.getIlItemCode())
						.priceApplyStartDate(itemPriceOrigVo.getStartDate())
						.standardPrice(itemPriceOrigVo.getStandardPrice())
						.standardPriceChange(reItemPriceOrigVo.getStandardPrice())
						.recommendedPrice(itemPriceOrigVo.getRecommendedPrice())
						.recommendedPriceChange(reItemPriceOrigVo.getRecommendedPrice())
						.priceManageTp(itemPriceOrigVo.getPriceManageTp())
						.approvalStatus(ApprovalEnums.ApprovalStatus.APPROVED_BY_SYSTEM.getCode())
						.build();
    			batchGoodsItemPriceService.setItemPriceAppr(itemPriceAppr);

    			batchGoodsItemPriceService.setItemPriceApprStatusHistory(
    				ApprovalStatusVo.builder()
						.taskPk(itemPriceAppr.getIlItemPriceApprId())
						.apprStat(ApprovalEnums.ApprovalStatus.APPROVED_BY_SYSTEM.getCode())
						.build()
				);
    			// system 자동 insert 도 approve 내역 필요함_2021.07.28 E

    			ItemPriceOrigVo itemPriceOrigInfo = batchGoodsItemPriceService.getItemPriceOrigInfo(itemPriceOrigVo);
    			if(null == itemPriceOrigInfo) {
    				itemPriceOrigVo.setManagerUpdateYn("N");
					batchGoodsItemPriceService.addItemPriceOrig(itemPriceOrigVo);
				}
    			else {
					itemPriceOrigVo.setIlItemPriceOriginalId(itemPriceOrigInfo.getIlItemPriceOriginalId());
					itemPriceOrigVo.setManagerUpdateYn(itemPriceOrigInfo.getManagerUpdateYn());
					batchGoodsItemPriceService.putItemPriceOrig(itemPriceOrigVo);
				}

    			if(ItemEnums.priceManageType.R.getCode().equals(itemPriceOrigVo.getPriceManageTp())) { // 'R'인경우는 관리자에 의해 입력된 미래 가격이 있으면 원가 현행화
					batchGoodsItemPriceService.updateItemPriceOrigOfFutureByErp(itemPriceOrigVo);
        		}

    			if (batchGoodsItemPriceService.getGoodsCountByItemCode(itemPriceOrigVo.getIlItemCode()) > 0) { // 품목에 해당하는 상품 개수 확인. 상품이 존재할 때만 프로시저 호출
    				batchGoodsItemPriceService.spGoodsPriceUpdateWhenItemPriceChanges(itemPriceOrigVo.getIlItemCode()); // 상품 가격 정보 업데이트 프로시저 호출
				}
    			// 미래 품목가격 보정 E

        	}

        	batchGoodsItemPriceService.spPackageGoodsPriceUpdateWhenItemPriceChanges(); // 묶음상품 가격 정보 업데이트 프로시저 호출
        }

        // API 조회 완료
    	if(erpItemPriceApiList.size() > 0) {
    		ErpGoodsPriceHeaderRequestDto requestDto = ErpGoodsPriceHeaderRequestDto.builder()
					.totalPage(1)
					.currentPage(1)
					.build();

    		for(ErpIfPriceSearchResponseDto priceInfo : erpItemPriceApiList) {
    			List<ErpGoodsPriceTermResultVo> headerList = new ArrayList<>();
    			ErpGoodsPriceTermResultVo goodsTermVo = new ErpGoodsPriceTermResultVo();
    			goodsTermVo.setIfSeq(priceInfo.getIfSeq());
    			goodsTermVo.setItmNo(priceInfo.getErpItemNo());
    			headerList.add(goodsTermVo);

    			requestDto.setHeader(headerList);

    			baseApiResponseVo = erpApiExchangeService.put(requestDto, ApiConstants.IF_PRICE_FLAG);
    	        log.info("addGoodsPurchaseOrderJob baseApiResponseVo:" + ToStringBuilder.reflectionToString(baseApiResponseVo));
    		}
    	}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void runItemPriceOrigCorrection() {
		batchGoodsItemPriceService.runItemPriceOrigCorrection();
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
    protected List<ErpIfGoodSearchResponseDto> getErpItemApi(String ilItemCode) throws BaseException {
        BaseApiResponseVo baseApiResponseVo = null;

        List<ErpIfGoodSearchResponseDto> eachPageDtoList = null; // 각 페이지별 품목 dto 목록
        List<ErpIfGoodSearchResponseDto> erpItemApiList = new ArrayList<>(); // 전체 취합된 품목 dto 목록

        Map<String, String> parameterMap = new HashMap<>();

        parameterMap.put("itmNo", ilItemCode);
        parameterMap.put("updFlg", "Y");

        try {
            // baseApiResponseVo : restTemplate 으로 조회한 응답 결과 객체
            baseApiResponseVo = erpApiExchangeService.get(parameterMap, ApiConstants.IF_GOODS_SRCH);

            if (!baseApiResponseVo.isSuccess()) { // ERP API 통신 실패시
                throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
            }

            eachPageDtoList = baseApiResponseVo.deserialize(ErpIfGoodSearchResponseDto.class); // 최초 1페이지 조회
            erpItemApiList.addAll(eachPageDtoList);

            if (baseApiResponseVo.getTotalPage() != null && baseApiResponseVo.getTotalPage() > 1) { // 2 페이지 이상인 경우 각 페이지별로 추가 호출

                // 최초 조회한 페이지의 다음 페이지부터 조회
                for (int page = 2; page <= baseApiResponseVo.getTotalPage(); page++) {

                    parameterMap.put("page", String.valueOf(page));

                    baseApiResponseVo = erpApiExchangeService.exchange(parameterMap, ApiConstants.IF_GOODS_SRCH);

                    if (!baseApiResponseVo.isSuccess()) { // ERP API 통신 실패시
                        throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
                    }

                    eachPageDtoList = baseApiResponseVo.deserialize(ErpIfGoodSearchResponseDto.class);

                    erpItemApiList.addAll(eachPageDtoList);
                }

            }

        } catch (Exception e) { // ERP API 통신 실패 or List<T> 역직렬화 실패시
            throw new BaseException(ItemEnums.Item.ERP_API_COMMUNICATION_FAILED);
        }

        return erpItemApiList;

    }


}
