package kr.co.pulmuone.v1.policy.clause.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseModifyViewResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PolicyGetClauseModifyViewResponseDto")
public class PolicyGetClauseModifyViewResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "")
	private	PolicyGetClauseModifyViewResultVo rows;
}
