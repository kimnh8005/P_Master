package kr.co.pulmuone.v1.policy.clause.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PolicyGetClauseListResponseDto")
public class PolicyGetClauseListResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "")
	private	List<PolicyGetClauseListResultVo> rows;

	@ApiModelProperty(value = "")
	private int total;

}
