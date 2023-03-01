package kr.co.pulmuone.v1.policy.claim.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimSupplyCtgryVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "BOS 클레임 공급업체별 사유 목록 응답 DTO")
public class PolicyClaimSupplyCtgryResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "쇼핑몰 클레임 사유 VO")
	private List<PolicyClaimSupplyCtgryVo> rows;

}
