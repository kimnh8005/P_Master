package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니getCartData에 상품금액 정보")
public class GetCartDataGoodsSummeryResposeDto {
	int goodsQtyByShipping;
	int goodsRecommendedPrice;
	int goodsSalePrice;
	int goodsPaymentPrice;
	int goodsDiscountPrice;
	int goodsTaxFreePaymentPrice;
	int goodsTaxPaymentPrice;
}
