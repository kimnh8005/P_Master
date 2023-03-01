package kr.co.pulmuone.v1.policy.clause.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetLatestJoinClauseListResultVo")
public class GetLatestJoinClauseListResultVo {

	@ApiModelProperty(value = "약관코드")
	private String psClauseGrpCd;

	@ApiModelProperty(value = "약관명")
	private String clauseGrpName;

	@ApiModelProperty(value = "약관 버전 이름")
	private String clauseTitle;

	@ApiModelProperty(value = "약관 적용 시작일")
	private String executeDate;

	@ApiModelProperty(value = "필수여부")
	private String mandatoryYn;

	@ApiModelProperty(value = "약관 내용")
	private String clauseContent;

	@ApiModelProperty(value = "약관 변경 안내")
	private String clauseDescription;

}
