package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CouponIssueVo")
public class CouponIssueVo {

	@ApiModelProperty(value = "쿠폰발급 PK")
	private Long pmCouponIssueId;

	@ApiModelProperty(value = "개별난수번호 PK")
	private Long pmSerialnumberId;

	@ApiModelProperty(value = "회원 PK")
	private Long urUserId;

	@ApiModelProperty(value = "쿠폰발행타입(일반, 재발급)")
	private String couponIssueType;

	@ApiModelProperty(value = "상태(미사용, 사용, 취소)")
	private String status;

	@ApiModelProperty(value = "쿠폰 발급 사용가능 시작일")
	private LocalDateTime issueValidityStartDate;

	@ApiModelProperty(value = "쿠폰 만료일")
	private LocalDateTime expirationDate;

	@ApiModelProperty(value = "등록일")
	private LocalDateTime createDate;

	@ApiModelProperty(value = "사용일")
	private LocalDateTime useDate;

	@ApiModelProperty(value = "취소일")
	private LocalDateTime cancelDate;


	@ApiModelProperty(value = "쿠폰 PK")
	private Long pmCouponId;

	@ApiModelProperty(value = "쿠폰종류(상품, 장바구니, 배송비, 판매가 지정)")
	private String couponType;

	@ApiModelProperty(value = "발급방법")
	private String issueType ;

	@ApiModelProperty(value = "관리자 쿠폰명")
	private String bosCouponName;

	@ApiModelProperty(value = "전시 쿠폰명")
	private String displayCouponName;

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

	@ApiModelProperty(value = "발급수량제한")
	private String issueQtyLimit;

	@ApiModelProperty(value = "발급예산")
	private String issueBudget;

	@ApiModelProperty(value = "발급수량")
	private int issueQty;

	@ApiModelProperty(value = "발급목적")
	private String issuePurpose;

	@ApiModelProperty(value = "발급사유")
	private String issueReason;

	@ApiModelProperty(value = "적용범위")
	private String coverageType;

	@ApiModelProperty(value = "사용범위_PC")
	private String usePcYn;

	@ApiModelProperty(value = "사용범위_Web")
	private String useMoWebYn;

	@ApiModelProperty(value = "사용범위_Mobile")
	private String useMoAppYn;

	@ApiModelProperty(value = "할인방식")
	private String discountType;

	@ApiModelProperty(value = "할인값")
	private String discountValue;

	@ApiModelProperty(value = "정률할인 최대할인금액")
	private String percentageMaxDiscountAmount;

	@ApiModelProperty(value = "최소결재금액")
	private String minPaymentAmount;

	@ApiModelProperty(value = "제휴구분")
	private String pgPromotionYn;

	@ApiModelProperty(value = "제휴구분 PG")
	private String pgPromotionPayConfigId;

	@ApiModelProperty(value = "제휴구분 결제수단")
	private String pgPromotionPayGroupId;

	@ApiModelProperty(value = "제휴구분 결제수단 상세")
	private String pgPromotionPayId;

	@ApiModelProperty(value = "쿠폰 마스터 상태 공통코드")
	private String couponMasterStat;

	@ApiModelProperty(value = "쿠폰 승인 상태 공통코드")
	private String apprStat;

	@ApiModelProperty(value = "장바구니쿠폰적용 구분")
	private String cartCouponApplyYn;

	@ApiModelProperty(value = "이용권 수금 여부")
	private String ticketCollectYn;

	@ApiModelProperty(value = "이용권 수금완료 확인 ID")
	private String ticketCollectUserId;

	@ApiModelProperty(value = "이용권 수금완료 확인일")
	private String ticketCollectDate;

}
