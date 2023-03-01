package kr.co.pulmuone.v1.user.urcompany.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.urcompany.dto.vo.GetClientResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetClientResponseDto")
public class GetClientResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "")
	private	GetClientResultVo rows;
}
