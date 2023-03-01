package kr.co.pulmuone.v1.batch.user.noti.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotiBatchVo {

    @ApiModelProperty(value = "알림타입")
    private String userNotiType;

    @ApiModelProperty(value = "알림 대상 PK")
    private String notiTargetId;

    @ApiModelProperty(value = "제목")
    private String targetTitle;

    @ApiModelProperty(value = "알림 대상 유형")
    private String targetType;

    @ApiModelProperty(value = "알림 내용")
    private String notiMessage;

}
