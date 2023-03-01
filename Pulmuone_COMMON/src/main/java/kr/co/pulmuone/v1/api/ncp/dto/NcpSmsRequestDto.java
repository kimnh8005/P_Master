package kr.co.pulmuone.v1.api.ncp.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NcpSmsRequestDto {

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "받는이 전화번호")
    private String mobile;

    @ApiModelProperty(value = "보내는이전화번호")
    private String senderTelephone;

}
