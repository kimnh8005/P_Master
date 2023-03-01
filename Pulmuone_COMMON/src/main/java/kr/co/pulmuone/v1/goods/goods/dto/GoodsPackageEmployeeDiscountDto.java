package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "묶음 상품 할인정보 Dto")
public class GoodsPackageEmployeeDiscountDto {
	@ApiModelProperty(value = "묶음상품 ID")
	private Long ilGoodsId;

	@ApiModelProperty(value = "할인율")
	private int discountRatio;
}
