package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "적립금 소멸예정 목록 조회 List Result")
public class CommonGetPointExpectExpireListResultVo {

    @ApiModelProperty(value = "유효기간")
    private String expirationDate;

    @ApiModelProperty(value = "금액")
    private String amount;
}
