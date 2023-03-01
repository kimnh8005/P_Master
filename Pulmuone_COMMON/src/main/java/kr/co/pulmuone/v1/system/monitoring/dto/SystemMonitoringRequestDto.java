package kr.co.pulmuone.v1.system.monitoring.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SystemMonitoringRequestDto {

    @ApiModelProperty(value = "모니터링 건수")
    private String count;

    @ApiModelProperty(value = "모니터링 시간-분")
    private String minute;

}
