package kr.co.pulmuone.v1.policy.accessibleip.dto.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = " SavePolicyAccessibleIpRequestDto")
public class SavePolicyAccessibleIpRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "", required = false)
    String insertData;

    @ApiModelProperty(value = "", required = false)
    String updateData;

    @ApiModelProperty(value = "", required = false)
    String deleteData;

    @ApiModelProperty(value = "", hidden = true)
    List<SavePolicyAccessibleIpRequestSaveDto> insertRequestDtoList = new ArrayList<SavePolicyAccessibleIpRequestSaveDto>();

    @ApiModelProperty(value = "", hidden = true)
    List<SavePolicyAccessibleIpRequestSaveDto> updateRequestDtoList = new ArrayList<SavePolicyAccessibleIpRequestSaveDto>();

    @ApiModelProperty(value = "", hidden = true)
    List<SavePolicyAccessibleIpRequestSaveDto> deleteRequestDtoList = new ArrayList<SavePolicyAccessibleIpRequestSaveDto>();

}
