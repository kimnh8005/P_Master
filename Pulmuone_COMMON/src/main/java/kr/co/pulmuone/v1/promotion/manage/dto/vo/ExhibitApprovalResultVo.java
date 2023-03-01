package kr.co.pulmuone.v1.promotion.manage.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ExhibitApprovalResultVo")
public class ExhibitApprovalResultVo {


	@ApiModelProperty(value = "기획전 PK")
	private String evExhibitId;

	@ApiModelProperty(value = "기획전 유형")
	private String exhibitTp;

	@ApiModelProperty(value = "기획전 유형명")
	private String exhibitTpName;

	@ApiModelProperty(value = "몰구분")
	private String mallDiv;

	@ApiModelProperty(value = "몰구분명")
	private String mallDivName;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "삭제여부")
	private String delYn;

	@ApiModelProperty(value = "제목")
	private String title;

	@ApiModelProperty(value = "설명")
	private String description;

	@ApiModelProperty(value = "진행기간시작일시")
	private String startDt;

	@ApiModelProperty(value = "진행기간종료일시")
	private String endDt;

	@ApiModelProperty(value = "진행상태")
	private String statusSe;

	@ApiModelProperty(value = "진행상태명")
	private String statusName;

	@ApiModelProperty(value = "기획전 상태")
	private String exhibitStatus;

	@ApiModelProperty(value = "기획전상태명")
	private String exhibitStatusName;

	@ApiModelProperty(value = "승인상태")
	private String apprStat;

	@ApiModelProperty(value = "승인상태 명")
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
	private String approvalChangeUserName;

	@ApiModelProperty(value = "승인처리자ID")
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
}
