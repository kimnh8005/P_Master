package kr.co.pulmuone.v1.batch.send.template.service.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsSendBatchResultVo {

    @ApiModelProperty(value = "SMS PK")
    private String snSmsSendId;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "받는이 전화번호")
    private String mobile;

}
