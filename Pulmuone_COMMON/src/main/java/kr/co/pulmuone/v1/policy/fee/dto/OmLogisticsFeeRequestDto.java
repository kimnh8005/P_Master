package kr.co.pulmuone.v1.policy.fee.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "OmLogisticsFeeRequestDto")
public class OmLogisticsFeeRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "물류수수료 pk")
    private long omLogisticsFeeId;

}
