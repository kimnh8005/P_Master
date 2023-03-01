package kr.co.pulmuone.v1.policy.holiday.dto;

import java.util.ArrayList;
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
@ApiModel(description = " AddHolidayResponseDto")
public class SaveHolidayResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "휴일 리스트")
    private List<GetHolidayListResultVo> rows = new ArrayList<GetHolidayListResultVo>();

	@ApiModelProperty(value = "카운트")
    private int total;

}
