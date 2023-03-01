package kr.co.pulmuone.v1.policy.benefit.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeBrandGroupVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "임직원 할인율 브랜드 그룹 관리 Dto")
public class PolicyBenefitEmployeeBrandGroupDto extends BaseRequestDto{

	@ApiModelProperty(value = "임직원 할인율 브랜드 그룹 관리 목록 리스트")
	private	List<PolicyBenefitEmployeeBrandGroupVo> rows;

	@ApiModelProperty(value = "수정일시")
	private String modifyDt;

}
