package kr.co.pulmuone.v1.policy.holiday.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetHolidayListResponseDto")
public class GetHolidayGroupListRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "공통휴일 사용 구분")
	private String commonHolidayCondition;

	@ApiModelProperty(value = "추가휴일 사용 구분")
	private String addHolidayCondition;

	@ApiModelProperty(value = "휴일그룹 그룹명")
	private String groupName;

}
