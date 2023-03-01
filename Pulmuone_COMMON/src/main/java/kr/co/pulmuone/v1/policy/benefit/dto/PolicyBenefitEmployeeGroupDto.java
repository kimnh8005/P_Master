package kr.co.pulmuone.v1.policy.benefit.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "임직원 혜택 관리 Dto")
public class PolicyBenefitEmployeeGroupDto extends BaseRequestDto{

	@ApiModelProperty(value = "임직원 혜택 그룹 목록 리스트")
	private	List<PolicyBenefitEmployeeVo> rows;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;

}
