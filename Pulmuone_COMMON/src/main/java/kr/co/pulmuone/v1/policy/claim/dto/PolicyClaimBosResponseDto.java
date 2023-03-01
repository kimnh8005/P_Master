package kr.co.pulmuone.v1.policy.claim.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimBosVo;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimCtgryVo;
import kr.co.pulmuone.v1.user.buyer.dto.GetBuyerListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerListResultVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "BOS 클레임 사유 목록 응답 DTO")
public class PolicyClaimBosResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "BOS 클레임 사유 VO 리스트")
	private List<PolicyClaimBosVo> rows;

	@ApiModelProperty(value = "")
	private int total;

	@Builder
	public PolicyClaimBosResponseDto(int total, List<PolicyClaimBosVo> rows) {
		this.total = total;
		this.rows = rows;
	}
}
