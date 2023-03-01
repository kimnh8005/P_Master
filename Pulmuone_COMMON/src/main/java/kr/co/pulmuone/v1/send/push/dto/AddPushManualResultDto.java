package kr.co.pulmuone.v1.send.push.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@ApiModel(description = "푸시발송정보 등록 Result")
public class AddPushManualResultDto extends BaseRequestDto {

    @ApiModelProperty(value = "푸시발송정보 ID")
    private int manualPushId;

    @ApiModelProperty(value = "기기타입")
    private String deviceType;
}
