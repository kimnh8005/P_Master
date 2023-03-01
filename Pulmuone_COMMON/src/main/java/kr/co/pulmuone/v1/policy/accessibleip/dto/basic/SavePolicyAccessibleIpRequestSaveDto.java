package kr.co.pulmuone.v1.policy.accessibleip.dto.basic;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " SavePolicyAccessibleIpRequestSaveDto")
public class SavePolicyAccessibleIpRequestSaveDto extends BaseRequestDto {

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String psAccessibleIpId;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String ipAddress;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String startApplyDate;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String endApplyDate;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "")
    String delYn;

}
