package kr.co.pulmuone.v1.send.push.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@ApiModel(description = "푸시발송정보 등록 Param")
public class AddPushManualParamVo extends BaseRequestDto {

    @ApiModelProperty(value = "푸시발송정보 ID")
    private int manualPushId;

    @ApiModelProperty(value = "관리자용 제목")
    private String administratorTitle;

    @ApiModelProperty(value = "광고/공지타입")
    private String advertAndNoticeType;

    @ApiModelProperty(value = "푸시 제목 Android")
    private String pushTitleAndroid;

    @ApiModelProperty(value = "푸시 부 제목 Android")
    private String pushSubTitleAndroid;

    @ApiModelProperty(value = "푸시 제목 IOS")
    private String pushTitleIos;

    @ApiModelProperty(value = "예약여부")
    private String reserveYn;

    @ApiModelProperty(value = "예약일시")
    private String reservationDate;

    @ApiModelProperty(value = "푸시발송타입")
    private String pushSendType;

    @ApiModelProperty(value = "이미지 URL")
    private String imageUrl;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "푸시링크")
    private String pushLink;

    @ApiModelProperty(value = "등록자ID")
    private String createId;

    @ApiModelProperty(value = "회원 ID")
    private String userId;

    @ApiModelProperty(value = "기기타입")
    private String deviceType;
}
