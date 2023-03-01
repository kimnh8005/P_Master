package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CouponApplicationListRequestDto {

	/**
	 * 회원 PK
	 */
	private Long urUserId;

	/**
	 * 상품 PK
	 */
	private Long ilGoodsId;

	/**
	 * 표준 브랜드 PK
	 */
	private Long urBrandId;

	/**
	 * 전시 브랜드 PK
	 */
	private Long dpBrandId;

	/**
	 * 출고처 PK
	 */
	private Long urWareHouseId;

	/**
	 * 카테고리 PK
	 */
	private Long ilCtgryId;

	/**
	 * 쿠폰발행 PK
	 */
	private Long pmCouponIssueId;

	/**
	 * 쿠폰타입
	 */
	private List<String> couponType;

}
