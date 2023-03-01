package kr.co.pulmuone.v1.policy.claim.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimMallVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "쇼핑몰 클레임 사유 목록 응답 DTO")
public class PolicyClaimMallResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "쇼핑몰 클레임 사유 VO 리스트")
	private List<PolicyClaimMallVo> rows;

	@ApiModelProperty(value = "")
	private int total;

	@Builder
	public PolicyClaimMallResponseDto(int total, List<PolicyClaimMallVo> rows) {
		this.total = total;
		this.rows = rows;
	}
}
