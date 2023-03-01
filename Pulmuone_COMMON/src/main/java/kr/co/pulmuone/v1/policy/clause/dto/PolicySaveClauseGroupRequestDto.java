package kr.co.pulmuone.v1.policy.clause.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PolicySaveClauseGroupRequestDto")
public class PolicySaveClauseGroupRequestDto extends BaseRequestDto {
	@ApiModelProperty(value = "", required = false)
    String insertData;

    @ApiModelProperty(value = "", required = false)
    String updateData;

    @ApiModelProperty(value = "", hidden = true)
    List<PolicySaveClauseGroupRequestSaveDto> insertRequestDtoList;

    @ApiModelProperty(value = "", hidden = true)
    List<PolicySaveClauseGroupRequestSaveDto> updateRequestDtoList;

}
