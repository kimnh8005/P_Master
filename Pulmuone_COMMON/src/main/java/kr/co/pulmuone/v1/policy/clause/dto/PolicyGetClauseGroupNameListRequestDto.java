package kr.co.pulmuone.v1.policy.clause.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PolicyGetClauseGroupNameListRequestDto")
public class PolicyGetClauseGroupNameListRequestDto extends BaseRequestDto {

}
