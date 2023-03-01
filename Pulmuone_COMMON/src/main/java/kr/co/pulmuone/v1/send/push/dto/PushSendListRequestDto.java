package kr.co.pulmuone.v1.send.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "PUSH 발송 회원 목록 Request")
public class PushSendListRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "회원 ID")
	private String userId;

    @ApiModelProperty(value = "기기타입")
    private String deviceType;

    @ApiModelProperty(value = "모바일디바이스정보 ID")
    private String deviceInfoId;
}
