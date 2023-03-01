package kr.co.pulmuone.v1.user.buyer.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " 회원별 적립급 정보 List Result")
public class GetPointInfoVo {

	@ApiModelProperty(value = "rowNumber")
	private int    rowNumber;

	@ApiModelProperty(value = "구분")
	private String paymentTypeName;

	@ApiModelProperty(value = "적립금 처리 유형")
    private String pointProcessType;

	@ApiModelProperty(value = "적립내역")
    private String pointTypeName;

	@ApiModelProperty(value = "포인트")
	private String point;

	@ApiModelProperty(value = "유효기간시작일")
	private String validityStartDay;

	@ApiModelProperty(value = "유효기간만료일")
	private String validityEndDay;

	@ApiModelProperty(value = "발생일")
	private String createDate;

	@ApiModelProperty(value = "로그인정보")
	private String loginInfo;

	@ApiModelProperty(value = "역할코드")
	private String erpOrganizationCd;

	@ApiModelProperty(value = "역할명")
	private String roleName;

	@ApiModelProperty(value = "적립가능금액")
	private int amount;

	@ApiModelProperty(value = "사용가능 포인트")
	private int totalPoint;

	@ApiModelProperty(value = "비고")
	private String cmnt;

	@ApiModelProperty(value = "주문번호")
	private String odid;
}
