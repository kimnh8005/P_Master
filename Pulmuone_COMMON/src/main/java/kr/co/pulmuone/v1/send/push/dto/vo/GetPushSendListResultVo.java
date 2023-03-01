package kr.co.pulmuone.v1.send.push.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "모바일 푸시발송이력 조회 Result")
public class GetPushSendListResultVo{

    @ApiModelProperty(value = "광고 공지타입명")
    private String advertAndNoticeTypeName;

    @ApiModelProperty(value = "기기타입명")
    private String deviceTypeName;

    @ApiModelProperty(value = "푸시 관리 제목")
    private String pushManagementTitle;

    @ApiModelProperty(value = "푸시내용")
    private String pushContent;

    @ApiModelProperty(value = "푸시링크")
    private String pushLink;

    @ApiModelProperty(value = "푸시전송결과 (발송)")
    private int pushSendTotal;

    @ApiModelProperty(value = "푸시전송결과 (전체)")
    private int pushTotal;

    @ApiModelProperty(value = "푸시발송일")
    private String pushSendDate;

    @ApiModelProperty(value = "읽음여부Y")
    private int openTotal;

    @ApiModelProperty(value = "읽음여부N")
    private int closeTotal;

    @ApiModelProperty(value = "발송관리자")
    private String senderId;
}
