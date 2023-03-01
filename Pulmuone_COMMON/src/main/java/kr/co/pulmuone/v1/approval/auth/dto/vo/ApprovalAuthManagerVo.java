package kr.co.pulmuone.v1.approval.auth.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "승인권한.승인관리자 VO")
public class ApprovalAuthManagerVo extends BaseVo{

	@ApiModelProperty(value = "승인권한.승인권한 PK (SEQ)")
	private String urApprAuthId;

	@ApiModelProperty(value = "승인권한.승인종류 유형 공통코드(APPR_KIND_TP)")
	private String apprKindType;

	@ApiModelProperty(value = "승인권한.승인관리자 유형 공통코드(APPR_MANAGER_TP)")
	private String apprManagerType;

	@ApiModelProperty(value = "승인권한.승인관리자 SEQ")
	private String apprUserId;

	@ApiModelProperty(value = "승인권한.승인관리자 Login ID")
//	@UserMaskingLoginId
	private String apprLoginId;

	@ApiModelProperty(value = "승인권한.승인관리자 명")
//	@UserMaskingUserName
	private String apprUserName;

	@ApiModelProperty(value = "승인권한.노출순서")
	private String sort;

	@ApiModelProperty(value = "승인권한 메모")
	private String memo;

	@ApiModelProperty(value = "관리자유형명")
    private String adminTypeName;

	@ApiModelProperty(value = "조직정보명")
    private String organizationName;

	@ApiModelProperty(value = "회원상태")
	private String userStatus;

	@ApiModelProperty(value = "회원상태명")
    private String userStatusName;

	@ApiModelProperty(value = "조직장 여부")
	private String teamLeaderYn;

	@ApiModelProperty(value = "권한 위임 적용 여부")
	private String grantAuthYn;

	@ApiModelProperty(value = "권한 위임 시작일")
	private String grantAuthStartDt;

	@ApiModelProperty(value = "권한 위임 종료일")
	private String grantAuthEndDt;

	@ApiModelProperty(value = "권한 위임 중지 여부(Y: 중지)")
	private String grantAuthStopYn;

	@ApiModelProperty(value = "권한 위임자 Login ID")
	private String grantLoginId;

	@ApiModelProperty(value = "권한 위임자명")
	private String grantUserName;

	@ApiModelProperty(value = "권한 위임자 회원상태")
	private String grantUserStatus;

	@ApiModelProperty(value = "권한 위임자 회원상태명")
	private String grantUserStatusName;

}
