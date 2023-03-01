package kr.co.pulmuone.v1.promotion.serialnumber.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingSerialNumber;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetClauseResultVo")
public class GetSerialNumberListResultVo
{

	@ApiModelProperty(value = "이용권 Sequence")
	private int no;

	@ApiModelProperty(value = "Excel 이용권 Sequence")
	private String noExcel;

	@ApiModelProperty(value = "이용권 PK")
	private String pmSerialNumberId;

	@ApiModelProperty(value = "이용권 번호")
	@UserMaskingSerialNumber
	private String serialNumber;

	@ApiModelProperty(value = "이용권 번호 문자사이즈")
	private String serialSize;

	@ApiModelProperty(value = "이용권 Front 번호 ")
	private String serialFront;

	@ApiModelProperty(value = "이용권 Back 번호 ")
	private String serialBack;

	@ApiModelProperty(value = "등록일자")
	private String createDate;

	@ApiModelProperty(value = "이용권 상태")
	private String status;

	@ApiModelProperty(value = "이용권 상태명")
	private String statusName;

	@ApiModelProperty(value = "사용 회원명")
	private String loginId;

	@ApiModelProperty(value = "사용 ID")
	private String userName;

	@ApiModelProperty(value = "등록가능 시작기간")
	private String issueStartDate;

	@ApiModelProperty(value = "등록가능 종료기간")
	private String issueEndDate;

	@ApiModelProperty(value = "사용일시")
	private String useDate;

	@ApiModelProperty(value = "사용ID")
	private String urUserId;

	@ApiModelProperty(value = "순번")
	private String rowNumber;

	@ApiModelProperty(value = "등록가능기간")
	private String issuePeriod;
}
