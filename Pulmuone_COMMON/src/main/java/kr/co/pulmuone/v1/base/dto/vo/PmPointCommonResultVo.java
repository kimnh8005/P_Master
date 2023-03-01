package kr.co.pulmuone.v1.base.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PmPointCommonResultVo")
public class PmPointCommonResultVo {

	@ApiModelProperty(value = "적립금_고유값	Id")
	private long pmPointId;

	@ApiModelProperty(value = "적립금_타입(이용권(난수번호 발급), 후기, 구매, 자동지급, 관리자 지급/차감)")
	private String pointType;

	@ApiModelProperty(value = "적립금 명")
	private String pointName;

	@ApiModelProperty(value = "발급기간_시작일")
	private String issueStartDate;

	@ApiModelProperty(value = "발급기간종료일")
	private String issueEndDate;

	@ApiModelProperty(value = "발급수량")
	private int issueQuantity;

	@ApiModelProperty(value = "발급예산")
	private long issueBudget;

	@ApiModelProperty(value = "유효기간_종료일")
	private String validityEndDate;

	@ApiModelProperty(value = "유효기간_유효일")
	private int validityDay;

	@ApiModelProperty(value = "유효기간")
	private String validityDate;

	@ApiModelProperty(value = "발급수량제한(1인당 지급제한 건수")
	private int issueQuantityLimit;

	@ApiModelProperty(value = "적립금_세부_타입(자동지급 : 이벤트, 추천인 / 관리자 지급,차감 : 이벤트, 관리자)")
	private String pointDetailType;

	@ApiModelProperty(value = "적립금_세부_타입(자동지급 : 이벤트, 추천인 / 관리자 지급,차감 : 이벤트, 관리자)")
	private String pointDetailTypeName;

	@ApiModelProperty(value = "난수번호타입(자동생성, 엑셀업로드, 고정값사용)")
	private String serialNumberType;

	@ApiModelProperty(value = "고정난수번호")
	private String fixSerialNumber;

	@ApiModelProperty(value = "발급타입(적립,차감)")
	private String paymentType;

	@ApiModelProperty(value = "유효기간 설정타입(기간설정, 유효일설정)")
	private String validityType ;

	@ApiModelProperty(value = "발급기준(배송완료, 구매완료, 구매확정)")
	private String paymentStandardType;

	@ApiModelProperty(value = "발급기준 상세(즉시, 설정기간)")
	private String paymentStandardDetailType;

	@ApiModelProperty(value = "발급방식(정률, 정액)")
	private String issueType;

	@ApiModelProperty(value = "발급_값")
	private int issueValue;

	@ApiModelProperty(value = "적립금 처리 유형")
	private String pointProcessTp;

	@ApiModelProperty(value = "적립금 정산 유형")
	private String pointSettlementTp;

}

