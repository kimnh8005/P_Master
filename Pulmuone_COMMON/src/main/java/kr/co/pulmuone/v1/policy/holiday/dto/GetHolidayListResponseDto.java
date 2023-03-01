package kr.co.pulmuone.v1.policy.holiday.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.holiday.dto.vo.GetHolidayListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetHolidayListResponseDto")
public class GetHolidayListResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "리스트")
	private List<GetHolidayListResultVo> rows;

}
