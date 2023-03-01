package kr.co.pulmuone.v1.batch.system.cache.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class CallApiResponseDto {

    @ApiModelProperty(value = "캐쉬 배치 에러")
    private MessageCommEnum responseEnum;

    @ApiModelProperty(value = "메시지")
    private String message;

}
