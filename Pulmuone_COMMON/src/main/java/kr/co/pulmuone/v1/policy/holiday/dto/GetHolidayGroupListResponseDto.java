package kr.co.pulmuone.v1.policy.holiday.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.holiday.dto.vo.GetHolidayGroupListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetHolidayGroupListResponseDto")
public class GetHolidayGroupListResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "리스트")
	private List<GetHolidayGroupListResultVo> rows;

	@ApiModelProperty(value = "카운트")
	int total;
}
