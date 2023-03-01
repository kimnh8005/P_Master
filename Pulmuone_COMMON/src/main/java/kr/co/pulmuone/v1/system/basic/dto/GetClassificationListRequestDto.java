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
@ApiModel(description = " GetClassificationListRequestDto")
public class GetClassificationListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "타입", required = false)
    String type;

    @ApiModelProperty(value = "타입명", required = false)
    String typeName;

    @ApiModelProperty(value = "사용여부", required = false)
    String useYn;

    @ApiModelProperty(value = "길이", required = false)
    String depthS;
}
