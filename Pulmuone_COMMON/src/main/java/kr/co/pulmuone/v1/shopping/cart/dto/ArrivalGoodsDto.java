package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품 정보 DTO")
public class ArrivalGoodsDto {

	@ApiModelProperty(value = "장바구니 PK")
	private Long spCartId;

	@ApiModelProperty(value = "배송장소 코드")
	private String storeDeliveryTypeCode;

}
