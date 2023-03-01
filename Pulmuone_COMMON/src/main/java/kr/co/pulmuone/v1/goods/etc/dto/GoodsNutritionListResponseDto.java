package kr.co.pulmuone.v1.goods.etc.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.etc.dto.vo.GoodsNutritionVo;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GoodsNutritionListResponseDto")
public class GoodsNutritionListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "레코드 목록")
	private	List<GoodsNutritionVo> rows;

	@ApiModelProperty(value = "총 count")
	private long total;

}
