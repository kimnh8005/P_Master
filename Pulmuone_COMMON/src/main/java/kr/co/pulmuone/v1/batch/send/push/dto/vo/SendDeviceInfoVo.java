package kr.co.pulmuone.v1.batch.send.push.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SendDeviceInfoVo {

    @ApiModelProperty(value = "회원별푸시발송정보 PK")
    private Long snPushSendId;

    @ApiModelProperty(value = "APP OS 유형")
    private String osType;

    @ApiModelProperty(value = "푸쉬 서비스 키값")
    private String pushKey;

    @ApiModelProperty(value = "디바이스 아이디")
    private String deviceId;
}