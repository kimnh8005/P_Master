package kr.co.pulmuone.v1.order.delivery.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 클레임번호조회 vo
 * </PRE>
 */

@Getter
@Builder
@ApiModel(description = "ClaimNumberSearchVo")
public class ClaimNumberSearchVo {

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문상세 PK")
	private String odOrderDetlId;

	@ApiModelProperty(value = "주문클레임 PK")
	private long odClaimId;

	@ApiModelProperty(value = "주문클레임상세 PK")
	private long odClaimDetlId;

}
