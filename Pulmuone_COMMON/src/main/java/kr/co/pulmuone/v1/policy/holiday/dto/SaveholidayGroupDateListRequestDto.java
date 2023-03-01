package kr.co.pulmuone.v1.policy.holiday.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " AddHolidayRequestSaveDto")
public class SaveholidayGroupDateListRequestDto extends BaseRequestDto {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "사용자 선택 일자 리스트")
    String holidayDateList;

}
