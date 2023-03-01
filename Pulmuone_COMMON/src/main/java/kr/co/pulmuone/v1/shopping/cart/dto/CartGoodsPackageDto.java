package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.PackageGoodsListDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlDiscountVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 상품 DTO")
public class CartGoodsPackageDto {

	public CartGoodsPackageDto(PackageGoodsListDto vo, int buyQty) {
		this.setIlGoodsId(vo.getIlGoodsId());
		this.setUrBrandId(vo.getUrBrandId());
		this.setGoodsName(vo.getGoodsName());
		this.setGoodsQty(vo.getGoodsQty());
		this.setRecommendedPrice(vo.getRecommendedPrice());
		this.setSalePrice(vo.getSalePrice());
		this.setSaleTotalPrice(vo.getSaleTotalPrice() * buyQty);
		this.setTaxYn(vo.getTaxYn());
		this.setGift(vo.isGift());
		this.setDeliveryQty(buyQty * getGoodsQty());
		this.setArrivalScheduledDateDtoList(vo.getArrivalScheduledDateDtoList());
		this.setArrivalScheduledDateDto(vo.getArrivalScheduledDateDto());
	}

	/**
	 * 묶음 상품 PK
	 */
	private Long ilGoodsId;

	/**
	 * 표준브랜드 PK
	 */
	private Long urBrandId;

	/**
	 * 묶음 상품명
	 */
	private String goodsName;

	/**
	 * 묶음 구성 수량
	 */
	private int goodsQty;

	/**
	 * 묶음상품 정상 단가
	 */
	private int recommendedPrice;

	/**
	 * 묶음상품 판매 단가
	 */
	private int salePrice;

	/**
	 * 묶음상품 판매가
	 */
	private int saleTotalPrice;

	/**
	 * 과세 여부 (Y:과세)
	 */
	private String taxYn;

	/**
	 * 묶음 증정품 여부
	 */
	private boolean isGift;

	/**
	 * 재고
	 */
	private int stockQty;

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
	 * 묶음 상품별 할인 정보
	 */
	List<CartGoodsDiscountDto> discount;

	/**
	 * 상품 결제 금액
	 */
	private int paymentPrice;
}
