package kr.co.pulmuone.v1.policy.shiparea.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.shiparea.dto.vo.GetBackCountryResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBackCountryResponseDto")
public class GetBackCountryResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "")
	private	GetBackCountryResultVo rows;

}
