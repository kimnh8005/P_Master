package kr.co.pulmuone.v1.policy.fee.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.*;

@Getter
@Setter
@ToString
@ApiModel(description = "OmBasicFeeRequestDto")
public class OmBasicFeeRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "기본수수료 pk")
    private long omBasicFeeId;

}
