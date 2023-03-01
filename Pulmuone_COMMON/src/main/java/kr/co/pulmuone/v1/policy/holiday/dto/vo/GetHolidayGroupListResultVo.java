package kr.co.pulmuone.v1.policy.holiday.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetHolidayGroupListResultVo {

	@JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "휴일그룹 ID")
    String psHolidayGroupId;

	@JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "휴일그룹 그룹명")
    String holidayGroupName;

	@JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "공통휴일 사용 구분")
    String commonHolidayYn;

	@JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "추가휴일 사용 구분")
    String addHolidayYn;

	@JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "추가휴일 카운트")
    String addHolidayCount;

	@JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "수정일자")
    String modifyDate;

	@JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "등록일자")
    String createDate;
}
