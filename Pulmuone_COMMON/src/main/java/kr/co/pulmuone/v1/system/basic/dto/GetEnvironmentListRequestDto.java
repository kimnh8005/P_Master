package kr.co.pulmuone.v1.system.basic.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetEnvironmentListRequestDto")
public class GetEnvironmentListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "환경설정키명", required = false)
    String searchEnvironmentKeyName;

    @ApiModelProperty(value = "환경설정키", required = false)
    String searchEnvironmentKey;

}
