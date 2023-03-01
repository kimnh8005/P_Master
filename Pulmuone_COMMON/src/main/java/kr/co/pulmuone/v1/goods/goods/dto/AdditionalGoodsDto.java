package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "추가구성상품 Dto")
public class AdditionalGoodsDto
{
	@ApiModelProperty(value = "추가 상품 PK")
	private Long ilGoodsId;

	@ApiModelProperty(value = "표준브랜드 PK")
	private Long urBrandId;

	@ApiModelProperty(value = "추가 상품명")
	private String goodsName;

	@ApiModelProperty(value = "추가 상품 정가")
	private int recommendedPrice;

	@ApiModelProperty(value = "추가 상품 판매가")
	private int salePrice;

	@ApiModelProperty(value = "추가 상품 재고")
	private int stockQty;

	@ApiModelProperty(value = "추가 상품 상태")
	private String saleStatus;

}
