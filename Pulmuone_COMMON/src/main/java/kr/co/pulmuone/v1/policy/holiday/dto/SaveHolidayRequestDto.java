package kr.co.pulmuone.v1.policy.holiday.dto;

import java.util.ArrayList;
import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " AddHolidayRequestDto")
public class SaveHolidayRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "등록할 휴일 데이터", required = false)
    String insertHolidayData;

    @ApiModelProperty(value = "등록할 휴일 데이터 리스트", hidden = true)
    List<SaveholidayDateListRequestDto> insertHolidayRequestDtoList = new ArrayList<SaveholidayDateListRequestDto>();

}
