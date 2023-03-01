package kr.co.pulmuone.v1.approval.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "업무별 직전 승인관리자 이력 Dto")
public class ApprovalAuthManagerHistoryByTaskDto{

	@ApiModelProperty(value = "직전 1차 승인관리자 SEQ")
	private String approvalSubUserId;

	@ApiModelProperty(value = "직전 최종 승인관리자 SEQ")
	private String approvalUserId;

}
