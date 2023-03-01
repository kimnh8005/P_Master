package kr.co.pulmuone.v1.policy.clause.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "PolicyDelClauseRequestDto")
public class PolicyDelClauseRequestDto extends BaseRequestDto{


	@ApiModelProperty(value = "")
	private String psClauseId;

	@ApiModelProperty(value = "")
	private String histType;

}
