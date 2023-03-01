package kr.co.pulmuone.v1.user.psuserbasic.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PsUserBasicResponseDto")
public class PsUserBasicResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "시스템환경설정 Vo")
	private List<GetEnvironmentListResultVo> getEnvironmentListResultVo;

}
