package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니getCartData 고객선택정보(배송) 응답DTO")
public class GetCartDataBuyerChoiceShippingDataResposeDto {
	// 주문 결제시 고객이 선택 배송정보 DTo
	ChangeArrivalScheduledDto changeArrivalScheduledDto;
	// 배송정책 index
	int shippingTemplateDataListIndex;
}
