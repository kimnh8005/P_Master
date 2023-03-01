package kr.co.pulmuone.v1.send.template.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApiModel(description = " GetEmailSendRequestDto")
public class GetEmailSendRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "자동발송키값")
    String snAutoSendId;

}
