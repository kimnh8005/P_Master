package kr.co.pulmuone.v1.goods.etc.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GoodsNutritionVo")
public class GoodsNutritionVo {

	@ApiModelProperty(value = "순번")
	private int rowNumber;

	@ApiModelProperty(value = "상품 영양정보 분류(ERP 분류정보) 코드 PK")
	private String ilNutritionCode;

	@ApiModelProperty(value = "분류명")
	private String nutritionName;

	@ApiModelProperty(value = "분류단위")
	private String nutritionUnit;

	@ApiModelProperty(value = "영양소 기준치 사용여부(Y:사용)")
	private String nutritionPercentYn;

	@ApiModelProperty(value = "노출 순서")
	private int sort;

}
