package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.SaleStatus;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.WeekCodeByGreenJuice;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartPickGoodsVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 상품 DTO")
public class CartPickGoodsDto {

	public CartPickGoodsDto(SpCartPickGoodsVo vo) {
		this.setIlGoodsId(vo.getIlGoodsId());
		this.setIlShippingTmplId(vo.getIlShippingTmplId());
		this.setGoodsQty(vo.getQty());
		this.setGoodsDailyCycleWeekTypeCode(vo.getGoodsDailyCycleWeekType());
		if (goodsDailyCycleWeekTypeCode != null) {
			this.setGoodsDailyCycleWeekTypeName(
					WeekCodeByGreenJuice.findByCode(goodsDailyCycleWeekTypeCode).getCodeName());
		}
		this.setPickableYn(vo.getPickableYn());
	}

	public void setByBasicSelectGoodsVo(BasicSelectGoodsVo vo, int buyQty) {
		this.setUrBrandId(vo.getUrBrandId());
		this.setGoodsName(vo.getGoodsName());
		this.setRecommendedPrice(vo.getRecommendedPrice());
		this.setSalePrice(vo.getSalePrice());
		this.setSaleTotalPrice(vo.getSalePrice() * getGoodsQty() * buyQty);
		this.setPaymentPrice(vo.getSalePrice() * getGoodsQty() * buyQty);
		if("N".equals(this.getPickableYn())) {
			this.setSaleStatus(SaleStatus.STOP_SALE.getCode());
		} else {
			this.setSaleStatus(vo.getSaleStatus());
		}
		this.setTaxYn(vo.getTaxYn());
		this.setStockQty(vo.getStockQty());
		this.setArrivalScheduledDateDtoList(vo.getArrivalScheduledDateDtoList());
		this.setArrivalScheduledDateDto(vo.getArrivalScheduledDateDto());
		this.setDiscountType(vo.getDiscountType());
		this.setDiscountTypeName(vo.getDiscountTypeName());
		this.setDeliveryQty(buyQty * getGoodsQty());
	}

	/**
	 * 골라담기 상품 PK
	 */
	private Long ilGoodsId;

	/**
	 * 표준브랜드 PK
	 */
	private Long urBrandId;

	/**
	 * 배송 탬플릿 PK
	 */
	private Long ilShippingTmplId;

	/**
	 * 골라담기 상품명
	 */
	private String goodsName;

	/**
	 * 골라담기 구성 수량
	 */
	private int goodsQty;

	/**
	 * 골라담기 상품 정가
	 */
	private int recommendedPrice;

	/**
	 * 상품 적용된 할인유형
	 */
	private String discountType;

	/**
	 * 상품 적용된 할인유형명
	 */
	private String discountTypeName;

	/**
	 * 골라담기 판매 단가
	 */
	private int salePrice;

	/**
	 * 골라담기 판매가
	 */
	private int saleTotalPrice;

	/**
	 * 상품 상태
	 */
	private String saleStatus;

	/**
	 * 과세 여부 (Y:과세)
	 */
	private String taxYn;

	/**
	 * 재고
	 */
	private int stockQty;

	/**
	 * 골라담기 일일배송 요일코드- 녹즙 내맘대로 주문시
	 */
	private String goodsDailyCycleWeekTypeCode;

	/**
	 * 골라담기 일일배송 요일명- 녹즙 내맘대로 주문시
	 */
	private String goodsDailyCycleWeekTypeName;

	/**
	 * 배송 가능 도착 예정일 DTO 리스트
	 */
	private List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList;

	/**
	 * 상품 배송 도착 예정일 DTO
	 */
	private ArrivalScheduledDateDto arrivalScheduledDateDto;

	/**
	 * 배송 수량
	 */
	private int deliveryQty;

	/**
	 * 골라담기 상품별 할인 정보
	 */
	List<CartGoodsDiscountDto> discount;

	/**
	 * 골라담기 상품 결제 금액
	 */
	private int paymentPrice;

	/**
	 * 녹즙 골라담기 허용여부(Y:허용)
	 */
	private String pickableYn;
}
