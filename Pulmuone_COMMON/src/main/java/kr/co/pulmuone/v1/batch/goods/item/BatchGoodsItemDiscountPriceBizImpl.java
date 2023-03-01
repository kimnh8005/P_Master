package kr.co.pulmuone.v1.batch.goods.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.batch.goods.item.dto.ErpGoodsPriceHeaderRequestDto;
import kr.co.pulmuone.v1.batch.goods.item.vo.ErpGoodsPriceTermResultVo;
import kr.co.pulmuone.v1.comm.api.constant.LegalTypes;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfPriceSearchResponseDto;
import kr.co.pulmuone.v1.comm.api.dto.vo.BaseApiResponseVo;
import kr.co.pulmuone.v1.comm.api.service.ErpApiExchangeService;
import kr.co.pulmuone.v1.comm.constants.ApiConstants;
import kr.co.pulmuone.v1.comm.enums.ItemEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.goods.itemprice.dto.vo.ItemPriceDiscountIfTempVo;

/**
 * <PRE>
 * Forbiz Korea
 * ERP API 조회 후 ERP 품목 할인 가격정보 업데이트
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0      2021-03-11       정형진         최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class BatchGoodsItemDiscountPriceBizImpl implements BatchGoodsItemDiscountPriceBiz{

    @Autowired
    private BatchGoodsItemDiscountPriceService batchGoodsItemDiscountPriceService;

	@Autowired
    ErpApiExchangeService erpApiExchangeService;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void modifyGoodsItemDiscountPriceJob() throws BaseException {

		Map<String, String> parameterMap = new HashMap<>(); //배치 Erp 데이터 param

		batchGoodsItemDiscountPriceService.delItemPriceDiscountIfTemp(); // 품목가격 연동용 임시 테이블 데이터 삭제(batch 오류에 의해 찌꺼기 데이터가 남아 있을수 있음)

		//조회 기간
		String fromDate = DateUtil.getCurrentDate() + "000000";
		String toDate = DateUtil.getCurrentDate() + "235959";
		String fromEndDate = DateUtil.getCurrentDate();
		String toEndDate = "29991231";

		parameterMap.put("target", "header-ITF");
		parameterMap.put("updFlg", "Y");    //정보 업데이트 여부(Y / N)
		parameterMap.put("salTyp", "행사");    //행사구분 (정상 / 행사)
		parameterMap.put("onlYn", "Y");    // 온라인 가격 여부
		parameterMap.put("legCd", LegalTypes.ORGA.getCode());
		parameterMap.put("endDat", fromEndDate +"~" + toEndDate);
//      parameterMap.put("updDat", fromDate +"~" + toDate);   //수정날짜(당일기준)
//      parameterMap.put("itfFlg", "N");   //인터페이스 Flag 20200929 현재 ERP 인터페이스 중계서버에서 미작업 상태
//      parameterMap.put("itmNo", "0934410"); //품목번호 : 테스트용

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

			if(priceInfo.getErpPriceApplyStartDate() == null || "".equals(priceInfo.getErpPriceApplyStartDate())) { // 할인 시작일 validation
				continue;
			}

			if(priceInfo.getErpPriceApplyEndDate() == null || "".equals(priceInfo.getErpPriceApplyEndDate())) { // 할인 종료일 validation
				continue;
			}
			else if (Integer.parseInt(DateUtil.getCurrentDate()) > Integer.parseInt(priceInfo.getErpPriceApplyEndDate())) { // 할인 종료일이 과거이면 무시
				continue;
			}

			if(priceInfo.getErpStandardPrice() == null || "".equals(priceInfo.getErpStandardPrice())) { // 할인 원가 validation
				continue;
			}

			if(priceInfo.getErpRecommendedPrice() == null || "".equals(priceInfo.getErpRecommendedPrice()) || "0".equals(priceInfo.getErpRecommendedPrice())) { // 할인 판메가 validation
				continue;
			}

			if(priceInfo.getNormalSelPrice() == null || "".equals(priceInfo.getNormalSelPrice()) || "0".equals(priceInfo.getNormalSelPrice())) { // 정상 판메가 validation
				continue;
			}

			ItemPriceDiscountIfTempVo priceDiscountTempVo = new ItemPriceDiscountIfTempVo();
			priceDiscountTempVo.setIfSeq(priceInfo.getIfSeq());
			priceDiscountTempVo.setIlItemCd(priceInfo.getErpItemNo());
			priceDiscountTempVo.setStartDt(priceInfo.getErpPriceApplyStartDate());
			priceDiscountTempVo.setEndDt(priceInfo.getErpPriceApplyEndDate());
			priceDiscountTempVo.setStandardPrice(priceInfo.getErpStandardPrice());
			priceDiscountTempVo.setRecommendedPrice(priceInfo.getErpRecommendedPrice());
			priceDiscountTempVo.setNormalStandardPrice(priceInfo.getNormalPurPrice());
			priceDiscountTempVo.setNormalRecommendedPrice(priceInfo.getNormalSelPrice());
			priceDiscountTempVo.setSaleType(priceInfo.getErpSalesType().getCode());
			batchGoodsItemDiscountPriceService.addItemPriceDiscountIfTemp(priceDiscountTempVo);
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
					if(priceInfo.getErpPriceApplyStartDate() == null || "".equals(priceInfo.getErpPriceApplyStartDate())) { // 할인 시작일 validation
						continue;
					}

					if(priceInfo.getErpPriceApplyEndDate() == null || "".equals(priceInfo.getErpPriceApplyEndDate())) { // 할인 종료일 validation
						continue;
					}
					else if (Integer.parseInt(DateUtil.getCurrentDate()) > Integer.parseInt(priceInfo.getErpPriceApplyEndDate())) { // 할인 종료일이 과거이면 무시
						continue;
					}

					if(priceInfo.getErpStandardPrice() == null || "".equals(priceInfo.getErpStandardPrice())) { // 할인 원가 validation
						continue;
					}

					if(priceInfo.getErpRecommendedPrice() == null || "".equals(priceInfo.getErpRecommendedPrice()) || "0".equals(priceInfo.getErpRecommendedPrice())) { // 할인 판메가 validation
						continue;
					}

					if(priceInfo.getNormalSelPrice() == null || "".equals(priceInfo.getNormalSelPrice()) || "0".equals(priceInfo.getNormalSelPrice())) { // 정상 판메가 validation
						continue;
					}

					ItemPriceDiscountIfTempVo priceDiscountTempVo = new ItemPriceDiscountIfTempVo();
					priceDiscountTempVo.setIfSeq(priceInfo.getIfSeq());
					priceDiscountTempVo.setIlItemCd(priceInfo.getErpItemNo());
					priceDiscountTempVo.setStartDt(priceInfo.getErpPriceApplyStartDate());
					priceDiscountTempVo.setEndDt(priceInfo.getErpPriceApplyEndDate());
					priceDiscountTempVo.setStandardPrice(priceInfo.getErpStandardPrice());
					priceDiscountTempVo.setRecommendedPrice(priceInfo.getErpRecommendedPrice());
					priceDiscountTempVo.setNormalStandardPrice(priceInfo.getNormalPurPrice());
					priceDiscountTempVo.setNormalRecommendedPrice(priceInfo.getNormalSelPrice());
					priceDiscountTempVo.setSaleType(priceInfo.getErpSalesType().getCode());
					batchGoodsItemDiscountPriceService.addItemPriceDiscountIfTemp(priceDiscountTempVo);
				}

				erpItemPriceApiList.addAll(eachPageDtoList); // 전체 품목 dto 목록에 취합]
			}
		}

		int addCount = 0;
		List<ItemPriceDiscountIfTempVo> priceDiscountIfTempList = batchGoodsItemDiscountPriceService.getItemPriceDiscountIfTempList();
		if (priceDiscountIfTempList.size() > 0) {
			addCount = batchGoodsItemDiscountPriceService.putGoodsDiscountWithItemDiscount(priceDiscountIfTempList);
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
			}
		}
	}

}
