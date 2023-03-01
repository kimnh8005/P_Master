package kr.co.pulmuone.v1.policy.holiday.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "SaveHolidayGroupRequestDto")
public class SaveHolidayGroupRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "휴일그룹 ID")
    String psHolidayGroupId;

    @ApiModelProperty(value = "휴일그룹 그룹명")
    String holidayGroupName;

    @ApiModelProperty(value = "공통휴일 사용 구분")
    String commonHolidayYn;

    @ApiModelProperty(value = "추가휴일 사용 구분")
    String addHolidayYn;

    @ApiModelProperty(value = "사용자 선택 일자 리스트")
    String holidayDateList;

    @ApiModelProperty(value = "사용자 삭제 일자 리스트")
    String deletedDateList;

    @ApiModelProperty(value = "입력데이터DTO리스트", hidden = true)
    List<SaveholidayGroupDateListRequestDto> insertRequestDtoList = new ArrayList<SaveholidayGroupDateListRequestDto>();

    @ApiModelProperty(value = "삭제데이터DTO리스트", hidden = true)
    List<SaveholidayGroupDateListRequestDto> deleteRequestDtoList = new ArrayList<SaveholidayGroupDateListRequestDto>();
}
