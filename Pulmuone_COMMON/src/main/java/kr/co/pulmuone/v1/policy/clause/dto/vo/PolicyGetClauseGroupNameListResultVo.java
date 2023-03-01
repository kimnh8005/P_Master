package kr.co.pulmuone.v1.policy.clause.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetClauseGroupNameListResultVo")
public class PolicyGetClauseGroupNameListResultVo {

	@ApiModelProperty(value = "")
	String psClauseGroupCd;

	@ApiModelProperty(value = "")
	String clauseGroupName;

}
