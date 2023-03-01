package kr.co.pulmuone.v1.policy.clause.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.clause.dto.vo.PolicyGetClauseGroupListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PolicyGetClauseGroupListResponseDto")
public class PolicyGetClauseGroupListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	List<PolicyGetClauseGroupListResultVo> rows;

	@ApiModelProperty(value = "")
	private int total;
}
