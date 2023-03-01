package kr.co.pulmuone.v1.policy.claim.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimCtgryVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "BOS 클레임 사유 카테고리 목록 응답 DTO")
public class PolicyClaimCtgryResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "BOS 클레임 사유 카테고리 VO 리스트")
	private List<PolicyClaimCtgryVo> rows;

	@ApiModelProperty(value = "")
	private int total;

	@Builder
	public PolicyClaimCtgryResponseDto(int total, List<PolicyClaimCtgryVo> rows) {
		this.total = total;
		this.rows = rows;
	}
}
