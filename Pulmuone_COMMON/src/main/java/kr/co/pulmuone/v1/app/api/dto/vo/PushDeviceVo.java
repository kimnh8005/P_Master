package kr.co.pulmuone.v1.app.api.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "단말기의 푸시 토큰 정보 VO")
public class PushDeviceVo {

    @ApiModelProperty(value = "앱 OS 타입")
    private String osType;

    @ApiModelProperty(value = "FCM 푸시 토큰")
    private String pushKey;

    @ApiModelProperty(value = "단말기 아이디")
    private String deviceId;

    @ApiModelProperty(value = "로그인 유저 코드")
    private long userCode;

    @ApiModelProperty(value = "푸시 수신 여부")
    private String pushAllowed;

    @ApiModelProperty(value = "Write Type")
    private String writeType;

    @ApiModelProperty(value = "야간 푸시 허용 여부")
    private String nightAllowed;

    @ApiModelProperty(value = "쿠키 urPcidCd 값")
    private String urPcidCd;

    @ApiModelProperty(value = "허용 일자")
    private String allowDate;

    @ApiModelProperty(value = "등록 일자")
    private String createDate;

    @ApiModelProperty(value = "변경 일자")
    private String modifyDate;
}
