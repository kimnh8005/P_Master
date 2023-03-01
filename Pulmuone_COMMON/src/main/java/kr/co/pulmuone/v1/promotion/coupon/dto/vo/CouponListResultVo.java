package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCouponListResultVo")
public class CouponListResultVo {

	@ApiModelProperty(value = "No")
	private int no;

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

	@ApiModelProperty(value = "할인방식 몀")
	private String discountTypeName;

	@ApiModelProperty(value = "할인상세")
	private String discountValue;

	@ApiModelProperty(value = "발급건수")
	private String issueCount;

	@ApiModelProperty(value = "사용건수")
	private String useCount;

	@ApiModelProperty(value = "사용건수")
	private String usePercent;

	@ApiModelProperty(value = "발급상태")
	private String approvalStatus;

	@ApiModelProperty(value = "변경 쿠폰 마스터 상태")
	private String couponMasterStat;

	@ApiModelProperty(value = "변경 쿠폰 승인 상태")
	private String apprStat;

	@ApiModelProperty(value = "발급상태 명")
	private String approvalStatusName;

	@ApiModelProperty(value = "쿠폰 ID")
	private int pmCouponId;

	@ApiModelProperty(value = "버튼구분")
	private String buttonStatus;

	@ApiModelProperty(value = "발급방법")
	private String issueType;

	@JsonProperty("NAME")
	@ApiModelProperty(value = "코드명")
	private String name;

	@JsonProperty("CODE")
	@ApiModelProperty(value = "코드ID")
	private String code;

	@ApiModelProperty(value = "분담조직코드 ")
	private String erpOrganizationCode;

	@ApiModelProperty(value = "분담조직명 ")
	private String erpOrganizationName;

	@ApiModelProperty(value = "이용권 수금 여부")
	private String ticketCollectYn;

	@ApiModelProperty(value = "난수번호타입")
	private String serialNumberTp;
}
