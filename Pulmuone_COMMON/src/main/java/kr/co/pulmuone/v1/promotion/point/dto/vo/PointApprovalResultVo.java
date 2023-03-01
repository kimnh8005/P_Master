package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PointApprovalResultVo")
public class PointApprovalResultVo {


	@ApiModelProperty(value = "적립금 ID")
    private String pmPointId;

    @ApiModelProperty(value = "적립금설정")
    private String pointType;

    @ApiModelProperty(value = "적립금설정명")
    private String pointTypeName;

    @ApiModelProperty(value = "적립금명")
    private String pointName;

    @ApiModelProperty(value = "기간")
    private String issueDate;

    @ApiModelProperty(value = "분담조직 명")
    private String organizationName;

    @ApiModelProperty(value = "적립금")
    private int issueValue;

    @ApiModelProperty(value = "적립금 합계")
	private int sumIssueVal;

    @ApiModelProperty(value = "지급 방법")
	private String payMethodType;

	@ApiModelProperty(value = "적립금 설정 그룹화 번호")
	private String grPmPointId;

    @ApiModelProperty(value = "적립금 ID 목록")
	private String groupPmPointIdList;

    @ApiModelProperty(value = "적립금 :일반")
    private int normalAmount;

    @ApiModelProperty(value = "적립금 :포토")
    private int photoAmount;

    @ApiModelProperty(value = "적립금 :프리미엄")
    private int premiumAmount;

    @ApiModelProperty(value = "유효기간")
    private String validityDay;

    @ApiModelProperty(value = "유효기간 :일반")
    private String normalValidityDay;

    @ApiModelProperty(value = "유효기간 :포토")
    private String photoValidityDay;

    @ApiModelProperty(value = "유효기간 :프리미엄")
    private String premiumValidityDay;

    @ApiModelProperty(value = "지급예산")
    private int issueBudget;

    @ApiModelProperty(value = "비고")
    private String comment;

    @ApiModelProperty(value = "설정상태 코드")
    private String status;

    @ApiModelProperty(value = "설정상태 명")
    private String statusName;

    @ApiModelProperty(value = "유효기간 설정")
    private String validityType;

	@ApiModelProperty(value = "유효기간 기간설정일")
    private String validityEndDate;

	@ApiModelProperty(value = "적립금마스터상태")
    private String pointMasterStat;

	@ApiModelProperty(value = "적립금마스터상태 명")
	private String pointMasterStatName;

	@ApiModelProperty(value = "적립금승인상태")
    private String apprStat;

	@ApiModelProperty(value = "적립금승인상태 명")
	private String apprStatName;

	@ApiModelProperty(value = "승인요청일")
	private String approvalRequestDt;

	@ApiModelProperty(value = "승인요청자명")
	private String approvalRequestUserName;

	@ApiModelProperty(value = "승인요청자ID")
	private String approvalRequestUserId;

	@ApiModelProperty(value = "1차 승인처리일")
	private String approvalSubChangeDt;

	@ApiModelProperty(value = "1차 승인처리자명")
	private String approvalSubChangeUserName;

	@ApiModelProperty(value = "1차 승인처리자ID")
	private String approvalSubChangeUserId;

	@ApiModelProperty(value = "1차 승인담당자명")
	private String approvalSubUserName;

	@ApiModelProperty(value = "1차 승인담당자ID")
	private String approvalSubUserId;

	@ApiModelProperty(value = "승인처리일")
	private String approvalChangeDt;

	@ApiModelProperty(value = "승인처리자명")
//	@UserMaskingUserName
	private String approvalChangeUserName;

	@ApiModelProperty(value = "승인처리자ID")
//	@UserMaskingLoginId
	private String approvalChangeUserId;

	@ApiModelProperty(value = "승인담당자명")
	private String approvalUserName;

	@ApiModelProperty(value = "승인담당자ID")
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

	@ApiModelProperty(value = "등록자ID")
	private String createId;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;

	@ApiModelProperty(value = "총 지급 건수")
	private int totalPayCnt;

	@ApiModelProperty(value = "평균 금액")
	private int avgPayIssueVal;

	@ApiModelProperty(value = "최대 금액")
	private int maxPayIssueVal;
}
