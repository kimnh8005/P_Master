package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 클레임 validation Dto
 * </PRE>
 */

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "클레임 validation Dto")
public class ClaimValidationDto {

	@ApiModelProperty(value = "실패건수")
	private int failCount;

	@ApiModelProperty(value = "클레임 validation 결과 상태")
	private ClaimEnums.ClaimValidationResult claimResult;

}
