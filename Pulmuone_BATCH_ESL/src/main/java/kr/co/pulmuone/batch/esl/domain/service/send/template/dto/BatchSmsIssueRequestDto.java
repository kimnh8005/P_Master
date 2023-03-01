package kr.co.pulmuone.batch.esl.domain.service.send.template.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BatchSmsIssueRequestDto {

    @ApiModelProperty(value = "받는이 user_id")
    private Long urUserId;

    @ApiModelProperty(value = "받는이 전화번호")
    private String mobile;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "보내는이전화번호")
    private String senderTelephone;

}
