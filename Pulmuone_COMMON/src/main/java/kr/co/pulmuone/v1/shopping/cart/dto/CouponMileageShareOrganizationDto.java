package kr.co.pulmuone.v1.shopping.cart.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CouponMileageShareOrganizationDto {

	/**
	 * 쿠폰 분담 조직코드
	 */
	private String urErpOrganizationCode;

	/**
	 * 쿠폰 분담률 적용금액
	 */
	private int couponShareAppliedPrice;

}
