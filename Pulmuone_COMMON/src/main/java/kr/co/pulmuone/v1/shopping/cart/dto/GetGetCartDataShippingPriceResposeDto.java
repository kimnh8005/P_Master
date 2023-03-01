package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니getCartData에 배송비 정보")
public class GetGetCartDataShippingPriceResposeDto {
	// 기본 배송비
	@ApiModelProperty(value = "기본 배송비")
	private int baiscShippingPrice;
	
	// 지역별 배송비
	@ApiModelProperty(value = "지역별 배송비")
	private int regionShippingPrice;
	
	// 기본 배송비 + 지역밸 배송비 (고객 결제 배송비)
	@ApiModelProperty(value = "기본 배송비 + 지역밸 배송비 (고객 결제 배송비)")
	int shippingRecommendedPrice;
	
	// 기본 배송비 + 지역밸 배송비 (정산 배송비금액)
	@ApiModelProperty(value = "기본 배송비 + 지역밸 배송비 (정산 배송비금액)")
	int originShippingPrice;
	
	// 무료 배송을 위한 추가 금액
	@ApiModelProperty(value = "무료 배송을 위한 추가 금액")
	int freeShippingForNeedGoodsPrice;
}
