package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "IssueListResultVo")
public class IssueListResultVo {

	@ApiModelProperty(value = "No")
	private int no;

	@ApiModelProperty(value = "쿠폰명")
	private String bosCouponName;

	@ApiModelProperty(value = "할인정보")
	private String discountValue ;

	@ApiModelProperty(value = "유효기간")
	private String validityPeroid;

	@ApiModelProperty(value = "지급일자")
	private String issueDate;

	@ApiModelProperty(value = "잔여기간")
	private String remainingPeriod;

	@ApiModelProperty(value = "사용일")
	private String couponUseDate;

	@ApiModelProperty(value = "사용시각")
	private String couponUseTime;

	@ApiModelProperty(value = "회원정보")
	private String userInfo;

	@ApiModelProperty(value = "사유")
	private String statusComment;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "적용상품코드")
	private String ilGoodsId;

	@ApiModelProperty(value = "쿠폰 적용 상품명")
	private String goodsNm;

	@ApiModelProperty(value = "쿠폰 발급/사용  ID")
	private String pmCouponIssueId;

	@ApiModelProperty(value = "회원명")
	@UserMaskingUserName
	private String userNm;

	@ApiModelProperty(value = "로그인 ID")
	@UserMaskingLoginId
	private String loginId;

	@ApiModelProperty(value = "유저 상태")
	private int userStatus;

	@ApiModelProperty(value = "상태")
	private String status;

}
