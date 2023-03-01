package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ApprovalCsRefundListDto")
public class ApprovalCsRefundListDto {

	@ApiModelProperty(value = "CS 환불 정보 PK")
	private Long odCsId;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "환불금액")
	private String refundPrice;

	@ApiModelProperty(value = "CS 환불구분")
	private String csRefundTp;

	@ApiModelProperty(value = "승인상태")
	private String apprStat;

	@ApiModelProperty(value = "승인상태 명")
	private String apprStatName;

	@ApiModelProperty(value = "승인요청일")
	private String approvalRequestDt;

	@ApiModelProperty(value = "승인요청자명")
	private String approvalRequestUserName;

	@ApiModelProperty(value = "승인요청자 ID")
	private String approvalRequestUserId;

	@ApiModelProperty(value = "1차 승인처리일")
	private String approvalSubChangeDt;

	@ApiModelProperty(value = "1차 승인처리자명")
	private String approvalSubChangeUserName;

	@ApiModelProperty(value = "1차 승인처리자 ID")
	private String approvalSubChangeUserId;

	@ApiModelProperty(value = "1차 승인담당자명")
	private String approvalSubUserName;

	@ApiModelProperty(value = "1차 승인담당자 ID")
	private String approvalSubUserId;

	@ApiModelProperty(value = "승인처리일")
	private String approvalChangeDt;

	@ApiModelProperty(value = "승인처리자명")
	private String approvalChangeUserName;

	@ApiModelProperty(value = "승인처리자 ID")
	private String approvalChangeUserId;

	@ApiModelProperty(value = "승인담당자명")
	private String approvalUserName;

	@ApiModelProperty(value = "승인담당자 ID")
	private String approvalUserId;

	@ApiModelProperty(value = "1차 승인권한위임자ID")
	private String approvalSubGrantUserId;

	@ApiModelProperty(value = "1차 승인권한위임자명")
	private String approvalSubGrantUserName;

	@ApiModelProperty(value = "승인권한위임자ID")
	private String approvalGrantUserId;

	@ApiModelProperty(value = "승인권한위임자명")
	private String approvalGrantUserName;

	@ApiModelProperty(value = "승인상태변경 메시지")
	private String apprStatCmnt;

}
