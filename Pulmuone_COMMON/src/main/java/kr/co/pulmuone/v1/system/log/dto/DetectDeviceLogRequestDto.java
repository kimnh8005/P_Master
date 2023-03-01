package kr.co.pulmuone.v1.system.log.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class DetectDeviceLogRequestDto {

    @ApiModelProperty(value = "디바이스 로그 유형")
    private SystemEnums.DeviceLogType deviceLogType;

    @ApiModelProperty(value = "조회 시작 일시")
    private String startDateTime;

    @ApiModelProperty(value = "조회 종료 일시")
    private String endDateTime;

    @ApiModelProperty(value = "탐지 기준 수량")
    private Integer detectCount;

}
