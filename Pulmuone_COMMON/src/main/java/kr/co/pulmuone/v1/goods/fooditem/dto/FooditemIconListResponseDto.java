package kr.co.pulmuone.v1.goods.fooditem.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.fooditem.dto.vo.FooditemIconVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "식단품목아이콘리스트 ResponseDto")
public class FooditemIconListResponseDto {

	@ApiModelProperty(value = "식단품목아이콘리스트")
	private	List<FooditemIconVo> rows;

	@ApiModelProperty(value = "총 갯수")
	private long total;
}
