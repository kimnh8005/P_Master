package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import java.util.List;

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
@ApiModel(description = "GetCouponDetailVo")
public class CouponDetailVo {


	@ApiModelProperty(value = "쿠폰Id")
	private String pmCouponId;

	@ApiModelProperty(value = "발급방법")
	private String issueType ;

	@ApiModelProperty(value = "전시 쿠폰명")
	private String displayCouponName;

	@ApiModelProperty(value = "관리자 쿠폰명")
	private String bosCouponName;

	@ApiModelProperty(value = "발급기간 시작일")
	private String issueStartDate;

	@ApiModelProperty(value = "발급기간 종료일")
	private String issueEndDate;

	@ApiModelProperty(value = "유효기간 설정타입")
	private String validityType;

	@ApiModelProperty(value = "유효기간 유효일")
	private String validityDay;

	@ApiModelProperty(value = "유효기간 시작일")
	private String validityStartDate;

	@ApiModelProperty(value = "유효기간 종료일")
	private String validityEndDate;

	@ApiModelProperty(value = "난수번호타입")
	private String issueDetailType;

	@ApiModelProperty(value = "발급수량제한")
	private String issueQtyLimit;

	@ApiModelProperty(value = "발급예산")
	private String issueBudget;

	@ApiModelProperty(value = "발급수량")
	private String issueQty;

	@ApiModelProperty(value = "사용범위_PC")
	private String usePcYn;

	@ApiModelProperty(value = "사용범위_Web")
	private String useMoWebYn;

	@ApiModelProperty(value = "사용범위_Mobile")
	private String useMoAppYn;

	@ApiModelProperty(value = "쿠폰종류")
	private String couponType;

	@ApiModelProperty(value = "할인방식")
	private String discountType;

	@ApiModelProperty(value = "할인값")
	private String discountValue;

	@ApiModelProperty(value = "정률할인 최대할인금액")
	private String percentageMaxDiscountAmount;

	@ApiModelProperty(value = "최소결재금액")
	private String minPaymentAmount;

	@ApiModelProperty(value = "발급목적")
	private String issuePurposeType;

	@ApiModelProperty(value = "쿠폰상태")
	private String status;

	@ApiModelProperty(value = "이전 쿠폰마스터 상태")
	private String prevCouponMasterStat;

	@ApiModelProperty(value = "이전 쿠폰 승인 상태")
	private String prevCouponApprStat;

	@ApiModelProperty(value = "변경 쿠폰 마스터 상태")
	private String couponMasterStat;

	@ApiModelProperty(value = "변경 쿠폰 승인 상태")
	private String apprStat;

	@ApiModelProperty(value = "쿠폰상태명")
	private String statusName;

	@ApiModelProperty(value = "쿠폰 발급처리자 정보")
	private String issueInfoUser;

	@ApiModelProperty(value = "쿠폰 중지처리자 정보")
	private String stopIssueUserInfo;


	@ApiModelProperty(value = "상태변경 사유")
	private String statusCmnt;

	@ApiModelProperty(value = "등록정보")
	private String createInfo;

	@ApiModelProperty(value = "수정정보")
	private String modifyInfo;

	@ApiModelProperty(value = "상품 적용범위")
	private String goodsCoverageType;

	@ApiModelProperty(value = "장바구니 적용범위")
	private String cartCoverageType;

	@ApiModelProperty(value = "배송비 적용범위")
	private String shippingCoverageType;

	@ApiModelProperty(value = "분담조직 리스트")
	private	List<OrganizationListResultVo> organizationList;

	@ApiModelProperty(value = "조직코드")
	private String urErpOrganizationCode;

	@ApiModelProperty(value = "조직명")
	private String urErpOrganizationName;

	@ApiModelProperty(value = "적용범위 리스트")
	private	List<CoverageVo> coverageList;

	@ApiModelProperty(value = "발급사유")
	private String issueReason;

	@ApiModelProperty(value = "회원 ID 리스트")
	private	List<AccountInfoVo> userList;

	@ApiModelProperty(value = "개별난수번호 리스트")
	private	List<AccountInfoVo> serialNumberList;

	@ApiModelProperty(value = "난수번호 타입")
	private String ticketIssueType;

	@ApiModelProperty(value = "난수번호")
	private String serialNumber;

	@ApiModelProperty(value = "장바구니쿠폰적용 구분")
	private String cartCouponApplyYn;

	@ApiModelProperty(value = "제휴구분")
	private String pgPromotionYn;

	@ApiModelProperty(value = "제휴구분 PG")
	private String pgPromotionPayConfigId;

	@ApiModelProperty(value = "제휴구분 결제수단")
	private String pgPromotionPayGroupId;

	@ApiModelProperty(value = "제휴구분 결제수단 상세")
	private String pgPromotionPayId;

	@ApiModelProperty(value = "승인 반려 사유")
	private String statusComment;

	@ApiModelProperty(value = "발급방법 세부")
	private String autoIssueType;

	@ApiModelProperty(value = "등록자명")
	@UserMaskingUserName
	private String createName;

	@ApiModelProperty(value = "등록자 ID")
	private String createLoginId;

	@ApiModelProperty(value = "등록일")
	private String createDate;

	@ApiModelProperty(value = "수정자 명")
	@UserMaskingUserName
	private String modifyName;

	@ApiModelProperty(value = "수정자 ID")
	private String modifyLoginId;

	@ApiModelProperty(value = "수정일")
	private String modifyDate;

	@ApiModelProperty(value = "1차 승인 담당자 ID")
	private String apprSubUserId;

	@ApiModelProperty(value = "2차 승인 담당자 ID")
	private String apprUserId;

	@ApiModelProperty(value = "승인 관리자 리스트")
	private List<ApprovalAuthManagerVo> apprUserList;

	@ApiModelProperty(value = "이용권 수금 여부")
	private String ticketCollectYn;

	@ApiModelProperty(value = "이용권 수금 정보")
	private String ticketCollectInfo;

	@ApiModelProperty(value = "승인요청ID")
	private String apprReqUserId;

	@ApiModelProperty(value = "암호화 쿠폰 ID")
	private String pmCouponIdEncrypt;
}
