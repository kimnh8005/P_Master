package kr.co.pulmuone.v1.policy.clause.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "PolicyAddClauseResponseDto")
public class PolicyAddClauseResponseDto extends BaseResponseDto{

    @ApiModelProperty(value = "")
    private String validationCode;
}
