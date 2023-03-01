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
@ApiModel(description = "PolicyAddClauseRequestDto")
public class PolicyAddClauseRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "")
	private String inputPsClauseGroupCd;

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


}
