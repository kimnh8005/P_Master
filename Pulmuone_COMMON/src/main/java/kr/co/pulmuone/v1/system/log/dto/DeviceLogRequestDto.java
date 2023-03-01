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
public class DeviceLogRequestDto {

    @ApiModelProperty(value = "디바이스 로그 유형")
    private SystemEnums.DeviceLogType deviceLogType;

    @ApiModelProperty(value = "사용자 환경 정보")
    private String urPcidCd;

    @ApiModelProperty(value = "회원 PK")
    private Long urUserId;

}
