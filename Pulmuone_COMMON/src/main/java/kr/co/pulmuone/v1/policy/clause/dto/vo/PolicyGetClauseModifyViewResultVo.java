package kr.co.pulmuone.v1.policy.clause.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetClauseModifyViewResultVo")
public class PolicyGetClauseModifyViewResultVo {


	@ApiModelProperty(value = "")
	private String psClauseId;

	@ApiModelProperty(value = "")
	private String psClauseGroupCd;

	@ApiModelProperty(value = "")
	private String clauseGroupName;

	@ApiModelProperty(value = "")
	private String executeDate;

	@ApiModelProperty(value = "")
	private String clauseDescription;

	@ApiModelProperty(value = "")
	private String content;

	@ApiModelProperty(value = "")
	private String mandatoryYn;

	@ApiModelProperty(value = "")
	private String clauseInfo;

	@ApiModelProperty(value = "")
	private String createUserInfo;

	@ApiModelProperty(value = "")
	private String modifyUserInfo;



}
