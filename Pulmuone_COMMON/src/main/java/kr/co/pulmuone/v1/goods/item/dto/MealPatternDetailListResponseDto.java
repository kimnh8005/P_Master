package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternDetailListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternGoodsListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "식단 패턴 패턴정보 리스트 ResponseDto")
public class MealPatternDetailListResponseDto extends BaseResponseDto {

	 @ApiModelProperty(value = "목록 total")
	 private long total;

	 @ApiModelProperty(value = "식단 패턴 패턴정보 Vo")
	 private List<MealPatternDetailListVo> rows;

}

