package kr.co.pulmuone.v1.policy.clause.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetClauseListResultVo")
public class PolicyGetClauseListResultVo {

	@ApiModelProperty(value = "")
	String psClauseId;

	@ApiModelProperty(value = "")
	String psClauseGroupCd;

	@ApiModelProperty(value = "")
	String createName;

	@ApiModelProperty(value = "")
	String modifyName;

	@ApiModelProperty(value = "")
	String executeDate;

	@ApiModelProperty(value = "")
	String modifyDate;

	@ApiModelProperty(value = "")
	String executeType;

	@ApiModelProperty(value = "")
	String content;

	@ApiModelProperty(value = "")
	String clauseVersion;

	@ApiModelProperty(value = "")
	String mandatoryYn;

	@ApiModelProperty(value = "")
	String clauseInfo;
}
