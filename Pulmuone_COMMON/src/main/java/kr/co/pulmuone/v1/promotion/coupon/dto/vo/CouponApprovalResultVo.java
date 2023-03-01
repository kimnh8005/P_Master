package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CouponApprovalResultVo")
public class CouponApprovalResultVo {

	@ApiModelProperty(value = "쿠폰타입")
	private String couponType ;

	@ApiModelProperty(value = "쿠폰타입명")
	private String couponTypeName;

	@ApiModelProperty(value = "전시 쿠폰명")
	private String displayCouponName;

	@ApiModelProperty(value = "관리자 쿠폰명")
	private String bosCouponName;

	@ApiModelProperty(value = "발급기간")
	private String issueDate;

	@ApiModelProperty(value = "유효기간")
	private String validityDate;

	@ApiModelProperty(value = "유효기간 설정타입")
	private String validityTp;

	@ApiModelProperty(value = "할인방식")
	private String discountType;

	@ApiModelProperty(value = "할인방식 명")
	private String discountTypeName;

	@ApiModelProperty(value = "할인상세")
	private String discountValue;

	@ApiModelProperty(value = "발급건수")
	private String issueCount;

	@ApiModelProperty(value = "사용건수")
	private String useCount;

	@ApiModelProperty(value = "사용건수")
	private String usePercent;

	@ApiModelProperty(value = "쿠폰상태")
	private String couponMasterStat;

	@ApiModelProperty(value = "쿠폰상태 명")
	private String couponMasterStatName;

	@ApiModelProperty(value = "승인상태")
	private String apprStat;

	@ApiModelProperty(value = "승인상태 명")
	private String apprStatName;

	@ApiModelProperty(value = "쿠폰 ID")
	private String pmCouponId;

	@ApiModelProperty(value = "버튼구분")
	private String buttonStatus;

	@ApiModelProperty(value = "발급방법")
	private String issueType;

	@ApiModelProperty(value = "승인요청일")
	private String approvalRequestDt;

	@ApiModelProperty(value = "승인요청자명")
//	@UserMaskingUserName
	private String approvalRequestUserName;

	@ApiModelProperty(value = "승인요청자ID")
//	@UserMaskingLoginId
	private String approvalRequestUserId;

	@ApiModelProperty(value = "1차 승인처리일")
	private String approvalSubChangeDt;

	@ApiModelProperty(value = "1차 승인처리자명")
//	@UserMaskingUserName
	private String approvalSubChangeUserName;

	@ApiModelProperty(value = "1차 승인처리자ID")
//	@UserMaskingLoginId
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

	@ApiModelProperty(value = "수정자명")
//	@UserMaskingUserName
	private String modifyUserName;

	@ApiModelProperty(value = "수정자ID")
//	@UserMaskingLoginId
	private String modifyId;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;
}
