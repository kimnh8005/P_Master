package kr.co.pulmuone.v1.customer.feedback.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedbackSetBestRequestDto {

    @ApiModelProperty(value = "fbFeedbackId", required = true)
    private Long fbFeedbackId;

    @ApiModelProperty(value = "회원 ID", hidden = true)
    private Long urUserId;

}
