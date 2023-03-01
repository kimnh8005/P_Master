package kr.co.pulmuone.v1.promotion.point.dto.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalAuthManagerVo;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "적립금 설정 정보 List Result")
public class PointSettingResultVo {

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

    @ApiModelProperty(value = "분담조직 ID")
    private String organizationId;

    @ApiModelProperty(value = "분담조직 명")
    private String organizationName;

    @ApiModelProperty(value = "적립금")
    private int issueValue;

    @ApiModelProperty(value = "적립금 합계")
    private int sumIssueVal;

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

    @ApiModelProperty(value = "관리버튼 상태")
    private String buttonStatus;

    @ApiModelProperty(value = "적립금 설정 자동타입")
    private String autoIssueSelect;

    @ApiModelProperty(value = "적립금 설정 자동타입")
    private String issueStartDate;

    @ApiModelProperty(value = "적립금 설정 자동타입")
    private String issueEndDate;

    @ApiModelProperty(value = "발급수량제한")
    private String issueQtyLimit;

    @ApiModelProperty(value = "분담조직코드")
    private String erpOrganizationCode;

    @ApiModelProperty(value = "분담조직명")
    private String erpOrganizationName;

    @ApiModelProperty(value = "지급타입")
    private String pointPaymentType;

    @ApiModelProperty(value = "적립금 세부 타입")
    private String pointPaymentDetailType;

    @ApiModelProperty(value = "발급 사유 타입")
    private String issueReasonType;

    @ApiModelProperty(value = "발급 사유 상세")
    private String issueReason;

    @ApiModelProperty(value = "유효기간 설정")
    private String validityType;

    @ApiModelProperty(value = "유효기간 종료일")
    private String validityDate;

    @ApiModelProperty(value = "지급제한")
    private String issueDayCount;

    @ApiModelProperty(value = "발급수량")
    private String issueQty;

    @ApiModelProperty(value = "난수번호 타입")
    private String serialNumberType;

    @ApiModelProperty(value = "난수번호 타입")
    private String randNumTypeSelect;

    @ApiModelProperty(value = "단일코드")
    private String fixSerialNumber;

    @ApiModelProperty(value = "등록정보")
    private String createInfo;

    @ApiModelProperty(value = "수정정보")
    private String modifyInfo;

    @ApiModelProperty(value = "지급 처리자 정보")
    private String issueInfoUser;

    @ApiModelProperty(value = "중지 처리자 정보")
    private String stopIssueInfoUser;

    @ApiModelProperty(value = "중지 처리자 정보")
    private String stopIssueUserInfo;

    @ApiModelProperty(value = "회원별 관리자 포인트 지급 여부")
    private String pointAdminYn;

    @ApiModelProperty(value = "반려사유")
    private String statusCmnt;

	@ApiModelProperty(value = "분담조직 리스트")
	private	List<OrganizationListResultVo> organizationList;

	@ApiModelProperty(value = "회원 ID 리스트")
	private	List<AccountInfoVo> userList;

	@ApiModelProperty(value = "개별난수번호 리스트")
	private	List<AccountInfoVo> serialNumberList;

	@ApiModelProperty(value = "회원등급별 리스트")
	private	List<PointUserGradeVo> userGradeList;

	@ApiModelProperty(value = "적립금 지급 정보")
    private List<PointPayInfoVo> pointPayInfoList;

	@ApiModelProperty(value = "유효기간 기간설정일")
    private String validityEndDate;

	@ApiModelProperty(value = "적립금마스터상태")
    private String pointMasterStat;

	@ApiModelProperty(value = "적립금승인상태")
    private String apprStat;

	@ApiModelProperty(value = "승인요청일")
    private String apprReqDate;

	@ApiModelProperty(value = "승인요청자")
    private String apprReqUserId;

	@ApiModelProperty(value = "승인처리일")
    private String apprChgDate;

	@ApiModelProperty(value = "승인처리자")
    private String apprChgUserId;

	@ApiModelProperty(value = "등록자명")
	@UserMaskingUserName
	private String createName;

	@ApiModelProperty(value = "등록자 ID")
	private String createLoginId;

	@ApiModelProperty(value = "등록일")
	private String createDate;

	@ApiModelProperty(value = "등록자 UserId")
    private String createId;

	@ApiModelProperty(value = "수정자 명")
	@UserMaskingUserName
	private String modifyName;

	@ApiModelProperty(value = "수정자 ID")
	private String modifyLoginId;

	@ApiModelProperty(value = "수정일")
	private String modifyDate;

	@JsonProperty("NAME")
	@ApiModelProperty(value = "코드명")
	private String name;

	@JsonProperty("CODE")
	@ApiModelProperty(value = "코드ID")
	private String code;

	@ApiModelProperty(value = "이용권 수금 여부")
	private String ticketCollectYn;

	@ApiModelProperty(value = "이용권 수금 정보")
	private String ticketCollectInfo;

	@ApiModelProperty(value = "1차 승인 담당자 ID")
	private String apprSubUserId;

	@ApiModelProperty(value = "2차 승인 담당자 ID")
	private String apprUserId;

	@ApiModelProperty(value = "승인 관리자 리스트")
	private List<ApprovalAuthManagerVo> apprUserList;

	@ApiModelProperty(value = "난수번호 타입")
	private String serialNumberTp;

    @ApiModelProperty(value = "로그인한 관리자 롤 ID 리스트")
    private List<String> listRoleId;

    @ApiModelProperty(value = "풀무원_플랫폼기획팀 롤 ID")
    private String masterRoleId;

    @ApiModelProperty(value = "지급방법 ( 단일지급 /엑셀 대량 지급")
    private String payMethodType;

}
