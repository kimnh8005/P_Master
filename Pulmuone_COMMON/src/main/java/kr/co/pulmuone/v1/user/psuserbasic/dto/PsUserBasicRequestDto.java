package kr.co.pulmuone.v1.user.psuserbasic.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveEnvironmentRequestSaveDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PsUserBasicRequestDto")
public class PsUserBasicRequestDto extends BaseResponseDto {

	@ApiModelProperty(value = "비밀번호 변경주기")
	private String urPwCycleDay;

	@ApiModelProperty(value = "비밀번호 실패 제한 횟수")
	private String urLoginFailCount;

	@ApiModelProperty(value = "비밀번호 변경주기 key")
	private long urPwCycleDayStEnvId;

	@ApiModelProperty(value = "비밀번호 실패 제한 횟수 key")
	private long urLoginFailCountStEnvId;

    @ApiModelProperty(value = "")
    long stEnvId;

    @ApiModelProperty(value = "")
    String environmentValue;

    private List<SaveEnvironmentRequestSaveDto> saveEnvironmentRequestSaveDto;

}
