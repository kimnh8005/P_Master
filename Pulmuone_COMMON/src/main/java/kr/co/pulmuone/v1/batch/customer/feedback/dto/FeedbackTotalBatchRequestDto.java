package kr.co.pulmuone.v1.batch.customer.feedback.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class FeedbackTotalBatchRequestDto {

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "만족도 점수")
    private Double satisfactionScore;

}