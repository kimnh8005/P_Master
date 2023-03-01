package kr.co.pulmuone.v1.policy.holiday.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetHolidayGroupRequestDto")
public class GetHolidayGroupRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "휴일그룹 ID")
	private String psHolidayGroupId;

}
