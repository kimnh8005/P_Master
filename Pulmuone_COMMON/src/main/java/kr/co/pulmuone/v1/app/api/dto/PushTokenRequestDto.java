package kr.co.pulmuone.v1.app.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "addPushService API")
public class PushTokenRequestDto {

    @ApiModelProperty(value = "단말기 아이디")
    private String deviceId;

    @ApiModelProperty(value = "FCM 푸시 토큰")
    private String pushKey;

    @ApiModelProperty(value = "등록&수정&삭제 타입")
    private String writeType;

    @ApiModelProperty(value = "로그인 유저 코드")
    private String userCode;

    @ApiModelProperty(value = "푸시 수신 여부")
    private String pushAllowed;

}
