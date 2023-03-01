package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "적립금 등록 검증 정보 조회 Result")
public class CommonGetAddPointValidationInfoVo {

    @ApiModelProperty(value = "적립 발급 상태")
    private String pointMasterStat;

    @ApiModelProperty(value = "적립금 결재 상태")
    private String pointApprStat;

    @ApiModelProperty(value = "발급수량")
    private int issueQty;

    @ApiModelProperty(value = "발급수량제한(1인당 지급제한 건수)")
    private int issueQtyLimit;

    @ApiModelProperty(value = "발급기간 시작일")
    private String issueStartDate;

    @ApiModelProperty(value = "발급기간 종료일")
    private String issueEndDate;

    @ApiModelProperty(value = "적립금 발급 갯수")
    private int issueCnt;

    @ApiModelProperty(value = "적립금 유저 발급 갯수")
    private int userIssueCnt;

    @ApiModelProperty(value = "적립금 PK")
    private Long pmPointId;

    @ApiModelProperty(value = "적립금 타입")
    private String pointType;

    @ApiModelProperty(value = "발급타입")
    private String paymentType;

    @ApiModelProperty(value = "발급 값")
    private String issueValue;

    @ApiModelProperty(value = "유효기간 설정타입")
    private String validityType;

    @ApiModelProperty(value = "유효기간 종료일")
    private String validityEndDate;

    @ApiModelProperty(value = "유효기간 유효일")
    private int validityDay;
}
