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
public class FeedbackEachCountDto {

    @ApiModelProperty(value = "상품 후기.총 갯수")
    private int feedbackTotalCount;

    @ApiModelProperty(value = "상품 후기.점수")
    private float satisfactionScore;

    @ApiModelProperty(value = "상품 후기. 글쓴 사람 총 수")
    private int satisfactionCount;

    @ApiModelProperty(value = "상품문의. 총갯수")
    private int qnaTotalCount;

}
