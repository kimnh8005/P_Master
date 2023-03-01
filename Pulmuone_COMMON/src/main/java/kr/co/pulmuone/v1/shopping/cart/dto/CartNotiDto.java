package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 알림 DTO")
public class CartNotiDto {

	/**
	 * 임직원 한도 초가 안내 여부
	 */
	private boolean isEmployeeDiscountExceedingLimit;

	/**
	 * 정기배송 추가 주문 안내
	 */
	private boolean isRegularShippingAdditionalOrder;
}
