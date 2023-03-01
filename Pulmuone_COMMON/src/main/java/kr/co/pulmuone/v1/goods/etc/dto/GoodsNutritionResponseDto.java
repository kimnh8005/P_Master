package kr.co.pulmuone.v1.goods.etc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.etc.dto.vo.GoodsNutritionVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GoodsNutritionResponseDto")
public class GoodsNutritionResponseDto {

	@ApiModelProperty(value = "레코드 상세")
	private	GoodsNutritionVo rows;

	@ApiModelProperty(value = "총 count")
	private long total;
}
