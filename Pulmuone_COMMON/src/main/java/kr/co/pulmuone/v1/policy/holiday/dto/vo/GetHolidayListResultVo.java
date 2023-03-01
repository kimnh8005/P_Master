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
public class GetHolidayListResultVo {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "휴무일자")
    String holidayDate;

}
