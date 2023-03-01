package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingEmail;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingMobile;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "BuyerInfoListResultVo")
public class BuyerInfoListResultVo {

	@ApiModelProperty(value = "회원 ID")
	private String urUserId;

	@ApiModelProperty(value = "회원명")
	@UserMaskingUserName
	private String userName ;

	@ApiModelProperty(value = "Login 아이디")
	@UserMaskingLoginId
	private String loginId;

	@ApiModelProperty(value = "Email")
	@UserMaskingEmail
	private String email;

	@ApiModelProperty(value = "휴대폰")
	@UserMaskingMobile
	private String mobile;

	@ApiModelProperty(value = "최근방문일")
	private String lastLoginDate;

	@ApiModelProperty(value = "최근구매일")
	private String lastOrderDate;

	@ApiModelProperty(value = "회원가입일")
	private String userCreateDate;

	@ApiModelProperty(value = "쿠폰 발급 ID")
	private String pmCouponIssueId;

	@ApiModelProperty(value = "no")
	private int no;

	@ApiModelProperty(value = "회원그룹 PK")
	private String urGroupId;

	@ApiModelProperty(value = "회원명")
	private String orgUserName;

	@ApiModelProperty(value = "Login 아이디")
	private String orgLoginId;

	@ApiModelProperty(value = "Email")
	private String orgEmail;

	@ApiModelProperty(value = "휴대폰")
	private String orgMobile;
}
