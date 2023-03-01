package kr.co.pulmuone.v1.batch.send.push.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SendManualPushVo {

    @ApiModelProperty(value = "푸시발송정보 PK")
    private Long snManualPushId;

    @ApiModelProperty(value = "APP OS 유형")
    private String osType;

    @ApiModelProperty(value = "푸쉬 유형")
    private String pushType;

    @ApiModelProperty(value = "IOS 푸쉬 제목")
    private String iosTitle;

    @ApiModelProperty(value = "안드로이드 푸쉬 제목")
    private String androidTitle;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "이미지")
    private String image;

    @ApiModelProperty(value = "링크")
    private String link;

    @ApiModelProperty(value = "발송 유형")
    private String pushSendType;
}