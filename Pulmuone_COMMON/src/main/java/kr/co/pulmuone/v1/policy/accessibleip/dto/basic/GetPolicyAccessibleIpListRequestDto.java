package kr.co.pulmuone.v1.policy.accessibleip.dto.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetPolicyAccessibleIpListRequestDto")
public class GetPolicyAccessibleIpListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "", required = false)
    String searchIpAddress;

}
