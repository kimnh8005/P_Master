package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.promotion.point.dto.vo.PointUserGradeVo;
import kr.co.pulmuone.v1.promotion.point.dto.vo.UploadInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "적립금 설정 저장 RequestDto")
public class PointSettingMgmRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "적립금 ID")
	private String pmPointId;

	@ApiModelProperty(value = "적립금 설정")
	private String pointType;

	@ApiModelProperty(value = "적립금 자동지급 타입")
	private String autoIssueSelect;

	@ApiModelProperty(value = "적립금 명")
	private String pointName;

	@ApiModelProperty(value = "적립금 설정 기간 시작 일자")
	private String issueStartDate;

	@ApiModelProperty(value = "적립금 설정 기간  종료 일자")
	private String issueEndDate;

	@ApiModelProperty(value = "분담조직 코드")
	private String erpOrganizationCode;

	@ApiModelProperty(value = "법인 코드")
	private String erpRegalCode;

	@ApiModelProperty(value = "분담조직 명")
	private String erpOrganizationName;

	@ApiModelProperty(value = "법인명")
	private String erpRegalName;

	@ApiModelProperty(value = "지급예산")
	private String issueBudget;

	@ApiModelProperty(value = "발급수량")
	private int issueQty;

	@ApiModelProperty(value = "지급수량 제한")
	private String issueQtyLimit;

	@ApiModelProperty(value = "지급방법 ( 단일지급 /엑셀 대량 지급")
	private String payMethodType;

	@ApiModelProperty(value = "적립금 설정 그룹화 번호")
	private String grPmPointId;

	@ApiModelProperty(value = "이전 적립금 설정 그룹화 번호")
	private String beforeGrPmPointId;

	@ApiModelProperty(value = "적립구분")
	private String pointPaymentType;

	@ApiModelProperty(value = "적립구분 상세")
	private String pointPaymentDetailType;

	@ApiModelProperty(value = "적립금")
	private String issueValue;

	@ApiModelProperty(value = "1인 최대 적립금")
	private String maxIssueValue;

	@ApiModelProperty(value = "적립금 일반후기 금액")
	private String normalAmount;

	@ApiModelProperty(value = "적립금 포토후기 금액")
	private String photoAmount;

	@ApiModelProperty(value = "적립금 프리미엄후기 금액")
	private String premiumAmount;

	@ApiModelProperty(value = "적립금 일반후기 제한일")
	private String normalValidityDay;

	@ApiModelProperty(value = "적립금 포토후기 제한일")
	private String photoValidityDay;

	@ApiModelProperty(value = "적립금 프리미엄후기 제한일")
	private String premiumValidityDay;

	@ApiModelProperty(value = "지급 차감 타입")
	private String issueReasonType;

	@ApiModelProperty(value = "지급 차감 상세 사유")
	private String issueReason;

	@ApiModelProperty(value = "지급 기준일")
	private String issueDayCount;

	@ApiModelProperty(value = "유효기간 타입")
	private String validityType;

	@ApiModelProperty(value = "유효일")
	private String validityDay;

	@ApiModelProperty(value = "기간설정일")
	private String validityDate;

	@ApiModelProperty(value = "고정난수번호")
	private String fixSerialNumber;

	@ApiModelProperty(value = "난수번호 타입")
	private String serialNumberType;

	@ApiModelProperty(value = "상태")
	private String status;

	@ApiModelProperty(value = "승인상태 Y/N (엑셀 대량업로드 선체크)")
	private String autoApprStatus;

	@ApiModelProperty(value = "난수생성 구분타입")
	private String randNumType;

	@ApiModelProperty(value = "난수 자동 생성 타입")
	private String randNumTypeSelect;

	@ApiModelProperty(value = "승인채크 구분")
	private String approvalCheck;

	@ApiModelProperty(value = "상태변경 메세지")
	private String statusContent;

	@ApiModelProperty(value = "개별난수번호 사용타입")
	private String serialNumberUseType;

	@ApiModelProperty(value = "개별난수번호 상태")
	private String serialNumberStatus;

	@ApiModelProperty(value = "개별난수번호 발급일")
	private String createDate;

	@ApiModelProperty(value = "등록자 ID")
	private String createId;

	@ApiModelProperty(value = "개별난수번호 사용일")
	private String useDate;

	@ApiModelProperty(value = "사용자 Login Id")
	private String urUserId;

	@ApiModelProperty(value = "사용자 Id")
	private String userId;

	@ApiModelProperty(value = "회원설정 Login Id")
	private String loginId;

	@ApiModelProperty(value = "회원별 관리자지급 구분")
	private String pointAdmin;

	@ApiModelProperty(value = "계정발급 회원")
	private String uploadUser;

	@ApiModelProperty(value = "관리자 지급차감 유효일")
	private String pointPaymentAmount;

	@ApiModelProperty(value = "반려 사유" ,required = false)
	private String statusComment;

	@ApiModelProperty(value = "계정발급 회원 Data List", hidden = true)
	List<UploadInfoVo> uploadUserList = new ArrayList<UploadInfoVo>();

	@ApiModelProperty(value = "이용권난수 엑셀 정보")
	private String uploadTicket;

	@ApiModelProperty(value = "계정발급 회원 Data List", hidden = true)
	List<UploadInfoVo> uploadTicketList = new ArrayList<UploadInfoVo>();

	@ApiModelProperty(value = "이용권난수 엑셀 정보")
	private String userGradeList;

	@ApiModelProperty(value = "적립금 등급별 정보")
    List<PointUserGradeVo> PointUserGradeVoList = new ArrayList<PointUserGradeVo>();

	@ApiModelProperty(value = "후기 유효일")
	private String feedbackValidityDay;

	@ApiModelProperty(value = "이전 적립금마스터 상태")
	private String prevPointMasterStat;

	@ApiModelProperty(value = "이전 적립금 승인 상태")
	private String prevApprStat;

	@ApiModelProperty(value = "변경 적립금 마스터 상태")
	private String pointMasterStat;

	@ApiModelProperty(value = "변경 적립금 승인 상태")
	private String apprStat;

	@ApiModelProperty(value = "회원마스트 ID")
	private long userMaster;

	@ApiModelProperty(value = "회원그룹 명")
	private String userGroup;

	@ApiModelProperty(value = "1차 승인 관리자")
	private String apprSubUserId;

	@ApiModelProperty(value = "2차 승인 관리자")
	private String apprUserId;

}
