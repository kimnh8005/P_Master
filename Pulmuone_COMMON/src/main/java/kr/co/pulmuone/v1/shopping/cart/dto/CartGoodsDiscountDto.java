package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 상품 할인 DTO")
public class CartGoodsDiscountDto {

	/**
	 * 할인 타입
	 */
	private String discountType;

	/**
	 * 할인 타입명
	 */
	private String discountTypeName;

	/**
	 * 할인 금액
	 */
	private int discountPrice;

	/**
	 * 할인 한도 초과 금액
	 */
	private int exceedingLimitPrice;

	/**
	 * 쿠폰 발급 PK
	 */
	private Long pmCouponIssueId;

	/**
	 * 임직원 혜택 그룹
	 */
	private Long psEmplDiscGrpId;
}
