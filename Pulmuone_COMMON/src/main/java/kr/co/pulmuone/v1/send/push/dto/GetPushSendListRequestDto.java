package kr.co.pulmuone.v1.send.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "모바일 푸시발송이력 조회 Request")
public class GetPushSendListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "관리제목")
	private String managementTitle;

    @ApiModelProperty(value = "광고/공지타입")
    private String advertAndNoticeType;

    @ApiModelProperty(value = "푸시발송일 시작")
    private String pushSendDateStart;

    @ApiModelProperty(value = "푸시발송일 종료")
    private String pushSendDateEnd;

    @ApiModelProperty(value = "발송플랫폽유형")
    private String sendGroup;

}
