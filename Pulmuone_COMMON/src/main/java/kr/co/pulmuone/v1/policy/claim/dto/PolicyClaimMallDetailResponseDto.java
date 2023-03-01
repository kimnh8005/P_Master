package kr.co.pulmuone.v1.policy.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimMallVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "쇼핑몰 클레임 사유 상세 응답 DTO")
public class PolicyClaimMallDetailResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "쇼핑몰 클레임 사유 VO")
	private PolicyClaimMallVo rows;

}
