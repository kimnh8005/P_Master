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
@ApiModel(description = "PolicySaveClauseGroupRequestSaveDto")
public class PolicySaveClauseGroupRequestSaveDto extends BaseRequestDto {

	@ApiModelProperty(value = "약관그룹 PK")
	String psClauseGroupCd;

	@ApiModelProperty(value = "약관그룹명")
	String clauseGroupName;

	@ApiModelProperty(value = "약관제목")
	String clauseTitle;

	@ApiModelProperty(value = "사용여부")
	String useYn;

	@ApiModelProperty(value = "약관필수 동의여부")
	String mandatoryYn;

	@ApiModelProperty(value = "노출 순번")
	String sort;
}
