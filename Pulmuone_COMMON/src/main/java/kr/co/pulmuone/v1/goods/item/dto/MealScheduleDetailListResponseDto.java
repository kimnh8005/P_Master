package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealSchedulelDetailListVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealSchedulelListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "식단 스케쥴 상세 리스트 ResponseDto")
public class MealScheduleDetailListResponseDto extends BaseResponseDto {

	 @ApiModelProperty(value = "목록 total")
	 private long total;

	 @ApiModelProperty(value = "식단 스케쥴 정보 Vo")
	 private List<MealSchedulelDetailListVo> rows;

}

