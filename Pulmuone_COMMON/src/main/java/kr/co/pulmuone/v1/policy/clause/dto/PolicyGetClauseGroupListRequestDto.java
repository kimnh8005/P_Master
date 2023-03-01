package kr.co.pulmuone.v1.policy.clause.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseGroupListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PolicyGetClauseGroupListRequestDto")
public class PolicyGetClauseGroupListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "")
	private	List<PolicyGetClauseGroupListResultVo> rows;

	@ApiModelProperty(value = "")
	private int total;
}
