package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니getCartData에 배송비 쿠본 적용 정보")
public class GetCartDataShippingCouponResposeDto {
	int shippingDiscountPrice;
	int shippingPaymentPrice;
}
