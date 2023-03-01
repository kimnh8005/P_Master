package kr.co.pulmuone.v1.display.dictionary.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCategoryListResponseDto")
public class BoostingTargetCategoryListDto extends BaseResponseDto
{
	private List<BoostingTargetCategoryDto> rows;

}