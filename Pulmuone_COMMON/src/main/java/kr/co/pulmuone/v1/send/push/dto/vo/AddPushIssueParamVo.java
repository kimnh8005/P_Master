package kr.co.pulmuone.v1.send.push.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ApiModel(description = "회원별푸시발송정보 등록 Param")
public class AddPushIssueParamVo extends BaseRequestDto {

    @ApiModelProperty(value = "회원별푸시발송정보 ID")
    private int pushSendId;

    @ApiModelProperty(value = "푸시발송정보 ID")
    private int manualPushId;

    @ApiModelProperty(value = "모바일디바이스정보 ID")
    private String deviceInfoId;
}