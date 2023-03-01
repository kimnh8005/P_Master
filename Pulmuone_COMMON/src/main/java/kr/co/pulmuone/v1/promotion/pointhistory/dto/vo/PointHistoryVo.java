package kr.co.pulmuone.v1.promotion.pointhistory.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " 적립금 내역 정보 List Result")
public class PointHistoryVo {

	@ApiModelProperty(value = "적립금 사용 ID")
    private String pmPointUsedId;

    @ApiModelProperty(value = "회원명")
    private String urUserName;

    @ApiModelProperty(value = "회원 LOGIN ID")
    private String urUserId;

    @ApiModelProperty(value = "구분")
    private String paymentType;

    @ApiModelProperty(value = "구분명")
    private String paymentTypeName;

    @ApiModelProperty(value ="분담조직")
    private String organizationNm;

    @ApiModelProperty(value = "적립금 설정")
    private String pointType;

    @ApiModelProperty(value = "적립금 설정 명")
    private String pointTypeName;

    @ApiModelProperty(value = "내역")
    private String amount;

    @ApiModelProperty(value = "적립/차감액")
    private int usedAmount;

    @ApiModelProperty(value = "상세구분")
    private String pointDetailType;

    @ApiModelProperty(value = "상세구분 명")
    private String pointDetailTypeName;

    @ApiModelProperty(value = "지급차감일")
    private String createDate;

    @ApiModelProperty(value = "유효기간")
    private String validityDay;

    @ApiModelProperty(value = "지급/차감일자")
    private String calculateDay;

    @ApiModelProperty(value = "내역 count")
    private int detailCount;

    @ApiModelProperty(value = "사유")
    private String pointUsedMsg;

    @ApiModelProperty(value = "유효기간만료일")
    private String expirationDt;

    @ApiModelProperty(value = "올가 회원번호")
    private String orgaMemberNo;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "현업 ID")
    private String createId;

    @ApiModelProperty(value = "현업 이름")
    private String createNm;

    @ApiModelProperty(value = "적립금 PK")
    private String pmPointId;

    @ApiModelProperty(value = "적립금명")
    private String pointName;
}
