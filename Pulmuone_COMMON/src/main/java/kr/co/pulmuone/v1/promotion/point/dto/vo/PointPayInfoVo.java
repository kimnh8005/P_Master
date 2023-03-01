package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PointPayInfoVo")
public class PointPayInfoVo {

    @ApiModelProperty(value = "적립금_고유값	Id")
    private long pmPointId;

    @ApiModelProperty(value = "적립금 합계")
    private int sumIssueVal;

    @ApiModelProperty(value = "적립금 평균")
    private int avgIssueVal;

    @ApiModelProperty(value = "1인 적립금 최대값")
    private int maxIssueVal;

    @ApiModelProperty(value = "총 건수")
    private int issueCnt;
}
