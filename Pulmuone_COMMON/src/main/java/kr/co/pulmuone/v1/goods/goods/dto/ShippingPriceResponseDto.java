package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(description = "배송정보 가격 Dto")
public class ShippingPriceResponseDto {

	@ApiModelProperty(value = "기본 배송비")
	private int baiscShippingPrice;

	@ApiModelProperty(value = "무료 배송을 위한 추가 금액")
	private int freeShippingForNeedGoodsPrice;

	@ApiModelProperty(value = "지역별 배송비")
	private int regionShippingPrice;

	@ApiModelProperty(value = "기본 배송비 + 지역밸 배송비")
	private int shippingPrice;
}
