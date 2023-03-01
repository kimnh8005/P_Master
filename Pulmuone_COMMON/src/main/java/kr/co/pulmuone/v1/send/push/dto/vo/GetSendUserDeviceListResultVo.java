package kr.co.pulmuone.v1.send.push.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ApiModel(description = "모바일 푸시 발송 (전체) 발송회원 기기정보 조회 Result")
public class GetSendUserDeviceListResultVo{

    @ApiModelProperty(value = "모바일디바이스정보 ID")
    private String deviceInfoId;

    @ApiModelProperty(value = "기기타입")
    private String deviceType;
}
