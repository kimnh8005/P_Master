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
@ApiModel(description = "PolicyGetClauseModifyViewRequestDto")
public class PolicyGetClauseModifyViewRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "")
	String psClauseId;

	@ApiModelProperty(value = "")
	String psClauseGroupCd;
}
