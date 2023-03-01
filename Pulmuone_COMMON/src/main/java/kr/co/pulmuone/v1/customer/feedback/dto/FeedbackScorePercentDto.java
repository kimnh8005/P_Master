package kr.co.pulmuone.v1.customer.feedback.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class FeedbackScorePercentDto {

    @ApiModelProperty(value = "별점점수")
    private int score;

    @ApiModelProperty(value = "별점비율")
    private int percent;

}
