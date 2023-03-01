package kr.co.pulmuone.v1.policy.clause.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetClauseGroupListResultVo")
public class PolicyGetClauseGroupListResultVo {

	@ApiModelProperty(value = "약관 그룹 PK")
	String psClauseGroupCd;

	@ApiModelProperty(value = "약관 그룹명")
	String clauseGroupName;

	@ApiModelProperty(value = "약관 제목")
	String clauseTitle;

	@ApiModelProperty(value = "사용여부")
	String useYn;

	@ApiModelProperty(value = "약관필수 동의여부")
	String mandatoryYn;

	@ApiModelProperty(value = "노출 순번")
	String sort;

	@ApiModelProperty(value = "기존 데이터 여부")
	String existYn;
}
