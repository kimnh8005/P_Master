package kr.co.pulmuone.v1.promotion.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CouponIssueParamDto")
public class CouponIssueParamDto extends BaseRequestDto{

	@ApiModelProperty(value = "쿠폰발급/사용 ID")
	String pmCouponId;

	@ApiModelProperty(value = "쿠폰발급/사용 ID")
	String pmCouponIssueId;

	@ApiModelProperty(value = "쿠폰발급타입")
	String couponIssueType;

	@ApiModelProperty(value = "쿠폰상태")
	String couponStatus;

	@ApiModelProperty(value = "쿠폰발급대상 회원 ID")
	String urUserId;

	@ApiModelProperty(value = "쿠폰발급대상 회원 LoginID")
	String loginId;

	@ApiModelProperty(value = "쿠폰발급 사유")
	String statusComment;

	@ApiModelProperty(value = "쿠폰 사용가능 시작일")
	String validityStartDate;

	@ApiModelProperty(value = "쿠폰만료일")
	String expirationDate;

	@ApiModelProperty(value = "개별난수고유값")
	String pmSerialNumberId;

	@ApiModelProperty(value = "개별난수")
	String serialNumber;

	@ApiModelProperty(value = "등록일")
	String createDate;

	@ApiModelProperty(value = "사용일")
	String useDate;

	@ApiModelProperty(value = "취소일")
	String cancelDate;

	@ApiModelProperty(value = "등록 Id")
	String createId;

	@ApiModelProperty(value = "적립/차감액")
	String uploadIssueValue;

	@ApiModelProperty(value = "개별 적립금")
	String issueVal;
}
