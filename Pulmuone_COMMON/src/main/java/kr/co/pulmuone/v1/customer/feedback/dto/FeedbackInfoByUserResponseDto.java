package kr.co.pulmuone.v1.customer.feedback.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedbackInfoByUserResponseDto {

    @ApiModelProperty(value = "후기 작성 대상 수")
    private int feedbackCount;

    @ApiModelProperty(value = "적립금 존재여부")
    private String existPointYn;

    @ApiModelProperty(value = "적립금 - 일반후기")
    private Long normalAmount;

    @ApiModelProperty(value = "적립금 - 포토후기")
    private Long photoAmount;

    @ApiModelProperty(value = "적립금 - 프리미엄후기")
    private Long premiumAmount;

}
