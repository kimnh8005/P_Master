package kr.co.pulmuone.v1.goods.goods.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;

import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsShippingTemplateMapper;
import kr.co.pulmuone.v1.goods.goods.dto.ShippingDataResultDto;
import kr.co.pulmuone.v1.goods.goods.dto.ShippingPriceResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingAreaVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200824   	 천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsShippingTemplateService {

	private final GoodsShippingTemplateMapper goodsShippingTemplateMapper;

	@Autowired
	private PolicyConfigBiz policyConfigBiz;

	/**
	 * 배송비 탬플릿
	 *
	 * @param ilGoodsId, urWareHouseId
	 * @return ShippingDataResultDto
	 * @throws Exception
	 */
	protected ShippingDataResultDto getShippingInfo(String saleType, Long ilGoodsId, Long urWareHouseId)
			throws Exception {
		ShippingDataResultDto result = new ShippingDataResultDto();

		ShippingDataResultVo shippingDataResult = goodsShippingTemplateMapper.getShippingInfo(ilGoodsId, urWareHouseId);

		// 배송정보 타입,타입명 세팅
		// 판매유형:일일 -> 일일배송
		if (saleType.equals(GoodsEnums.SaleType.DAILY.getCode())) {
			result.setDeliveryType(GoodsEnums.DeliveryType.DAILY.getCode());
			result.setDeliveryTypeName(GoodsEnums.DeliveryType.DAILY.getCodeName());

		// 판매유형:일반,정기,예약 -> 일반배송
		}else if(saleType.equals(GoodsEnums.SaleType.NORMAL.getCode()) || saleType.equals(GoodsEnums.SaleType.REGULAR.getCode()) || saleType.equals(GoodsEnums.SaleType.RESERVATION.getCode())){
			result.setDeliveryType(GoodsEnums.DeliveryType.NORMAL.getCode());
			result.setDeliveryTypeName(GoodsEnums.DeliveryType.NORMAL.getCodeName());
		}

		result.setShippingPriceText(getShippingPriceText(shippingDataResult));
		result.setAreaShippingYn(shippingDataResult.getAreaShippingYn());
		result.setJejuShippingPrice(shippingDataResult.getJejuShippingPrice());
		result.setIslandShippingPrice(shippingDataResult.getIslandShippingPrice());

		return result;
	}

	/**
	 * 배송 정책 조회
	 *
	 * @param ilShippingTmplId
	 * @return
	 * @throws Exception
	 */
	protected ShippingDataResultVo getShippingInfoByShippingTmplId(Long ilShippingTmplId) throws Exception {
		return goodsShippingTemplateMapper.getShippingInfoByShippingTmplId(ilShippingTmplId);
	}

	/**
	 * 배송비 금액 노출 문구
	 * @param shippingDataResultVo
	 * @return
	 * @throws Exception
	 */
	protected String getShippingPriceText(ShippingDataResultVo shippingDataResultVo) throws Exception {
		String shippingPriceText = "";
		DecimalFormat formatter = new DecimalFormat("###,###");
		String conditionType = shippingDataResultVo.getConditionType();
		String conditionValue = formatter.format(shippingDataResultVo.getConditionValue());
		String shippingPrice = formatter.format(shippingDataResultVo.getShippingPrice());

		// 배송정보 금액 세팅
		if (conditionType.equals(GoodsEnums.ConditionType.TYPE1.getCode())) { // 무료배송
			shippingPriceText = "무료배송";
		} else if (conditionType.equals(GoodsEnums.ConditionType.TYPE2.getCode())) { // 고정배송비
			shippingPriceText = "배송비 " + shippingPrice + "원";
		} else if (conditionType.equals(GoodsEnums.ConditionType.TYPE3.getCode())) { // 결제금액당 배송비
			shippingPriceText = conditionValue + "원 미만 구매시 " + shippingPrice + "원";
		} else if (conditionType.equals(GoodsEnums.ConditionType.TYPE5.getCode())) { // 상품1개단위별 배송비
			shippingPriceText = "상품 1개당 배송비 " + shippingPrice + "원";
		}

		return shippingPriceText;
	}

	/**
	 * 배송비 구하기
	 *
	 * @param ilShippingTmplId
	 * @param goodsPrice
	 * @param goodsQty
	 * @return
	 * @throws Exception
	 */
	protected ShippingPriceResponseDto getShippingPrice(ShippingDataResultVo shippingDataResultVo, int goodsPrice,
			int goodsQty) throws Exception {

		int baiscShippingPrice = 0;
		int freeShippingForNeedGoodsPrice = 0;
		int regionShippingPrice = 0;

		String conditionType = shippingDataResultVo.getConditionType();

		// 기본 배송비 정보 조회
		if (conditionType.equals(GoodsEnums.ConditionType.TYPE1.getCode())) { // 무료배송
			baiscShippingPrice = 0;
			freeShippingForNeedGoodsPrice = 0;
		} else if (conditionType.equals(GoodsEnums.ConditionType.TYPE2.getCode())) { // 고정배송비
			baiscShippingPrice = shippingDataResultVo.getShippingPrice();
			freeShippingForNeedGoodsPrice = 0;
		} else if (conditionType.equals(GoodsEnums.ConditionType.TYPE3.getCode())) { // 결제금액당 배송비
			if (goodsPrice >= shippingDataResultVo.getConditionValue()) {
				baiscShippingPrice = 0;
				freeShippingForNeedGoodsPrice = 0;
			} else {
				baiscShippingPrice = shippingDataResultVo.getShippingPrice();
				freeShippingForNeedGoodsPrice = shippingDataResultVo.getConditionValue() - goodsPrice;
			}
		} else if (conditionType.equals(GoodsEnums.ConditionType.TYPE5.getCode())) { // 상품1개단위별 배송비
			baiscShippingPrice = goodsQty * shippingDataResultVo.getShippingPrice();
			freeShippingForNeedGoodsPrice = 0;
		} else {
			new Exception();
		}

		int shippingPrice = baiscShippingPrice + regionShippingPrice;

		ShippingPriceResponseDto resDto = new ShippingPriceResponseDto();
		resDto.setBaiscShippingPrice(baiscShippingPrice);
		resDto.setFreeShippingForNeedGoodsPrice(freeShippingForNeedGoodsPrice);
		resDto.setRegionShippingPrice(regionShippingPrice);
		resDto.setShippingPrice(shippingPrice);
		return resDto;
	}

	/**
	 * 배송비 구하기 (지역별 배송비)
	 *
	 * @param ilShippingTmplId
	 * @param goodsPrice
	 * @param goodsQty
	 * @param zipCode
	 * @return
	 * @throws Exception
	 */
	protected ShippingPriceResponseDto getShippingPrice(ShippingDataResultVo shippingDataResultVo, int goodsPrice,
			int goodsQty, String zipCode) throws Exception {

		ShippingPriceResponseDto resDto = getShippingPrice(shippingDataResultVo, goodsPrice, goodsQty);

		ShippingAreaVo shippingAreaVo = getShippingArea(zipCode);

		// 지역별 배송비 사용 여부(Y: 사용, N: 미사용)
		if ("Y".equals(shippingDataResultVo.getAreaShippingYn())) {
			if (shippingAreaVo != null) {
				if ("Y".equals(shippingAreaVo.getIslandYn())) {
					resDto.setRegionShippingPrice(shippingDataResultVo.getIslandShippingPrice());
				} else if ("Y".equals(shippingAreaVo.getJejuYn())) {
					resDto.setRegionShippingPrice(shippingDataResultVo.getJejuShippingPrice());
				}
				resDto.setShippingPrice(resDto.getShippingPrice() + resDto.getRegionShippingPrice());
			}
		}

		return resDto;
	}

	/**
	 * 도서산관 및 제주 배송 정보 조회
	 * @param zipCode
	 * @return
	 * @throws Exception
	 */
	protected ShippingAreaVo getShippingArea(String zipCode) throws Exception {

		// isApplyDeliveryAreaPolicy 정책키의 적용 날짜를 비교하여 배송권역정책(도서산간, 배송불가권역 신규 테이블 로직)을 적용
		return policyConfigBiz.isApplyDeliveryAreaPolicy() ? goodsShippingTemplateMapper.getAdditionalShippingAmountArea(zipCode) : goodsShippingTemplateMapper.getShippingArea(zipCode);
	}

	/**
	 * 주소기반 배송 가능여부
	 *
	 * @param undeliverableAreaType, receiverZipCode
	 * @return boolean
	 * @throws Exception
	 */
	protected boolean isUnDeliverableArea(String undeliverableAreaType, ShippingAreaVo shippingAreaVo) throws Exception {
		if (shippingAreaVo != null) {

			// 품목의 배송불가지역이 1권역(도서산간)일 때
			if (undeliverableAreaType.equals(GoodsEnums.UndeliverableAreaType.A1.getCode())) {
				if ("Y".equals(shippingAreaVo.getIslandYn()))
					return true;

				// 품목의 배송불가지역이 2권역(제주)일 때
			} else if (undeliverableAreaType.equals(GoodsEnums.UndeliverableAreaType.A2.getCode())) {
				if ("Y".equals(shippingAreaVo.getJejuYn()))
					return true;

				// 품목의 배송불가지역이 1,2권역일 때
			} else if (undeliverableAreaType.equals(GoodsEnums.UndeliverableAreaType.A1_A2.getCode())) {
				if ("Y".equals(shippingAreaVo.getIslandYn()) || "Y".equals(shippingAreaVo.getJejuYn())) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 배송불가 배송정책 정보
	 * @param ilGoodsId
	 * @param urWareHouseId
	 * @return
	 * @throws Exception
	 */
	protected ShippingDataResultVo getShippingUndeliveryInfo(Long ilGoodsId, Long urWareHouseId) throws Exception {
		return goodsShippingTemplateMapper.getShippingInfo(ilGoodsId, urWareHouseId);
	}
}
