package kr.co.pulmuone.v1.send.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ApiModel(description = "선택회원 PUSH 발송 Request")
public class AddPushIssueSelectRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "발송대상옵션")
	private String sendTargetOption;

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

    @ApiModelProperty(value = "발송구분")
    private String sendClassification;

    @ApiModelProperty(value = "예약일시")
    private String reservationDate;

    @ApiModelProperty(value = "푸시발송타입")
    private String pushSendType;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "푸시링크")
    private String pushLink;

    @ApiModelProperty(value = "파일정보")
    private String addFile;

    @ApiModelProperty(value = "파일 리스트")
    private List<FileVo> addFileList;

    @ApiModelProperty(value = "회원ID정보")
    private String buyerDevice;

    @ApiModelProperty(value = "회원ID 리스트")
    private List<PushSendListRequestDto> buyerDeviceList;
}
