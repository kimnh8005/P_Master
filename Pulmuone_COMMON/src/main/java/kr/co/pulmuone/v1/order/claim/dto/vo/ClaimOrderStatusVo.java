package kr.co.pulmuone.v1.order.claim.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * 클레임 주문 상태값 VO
 * </PRE>
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "클레임 주문 상태값 VO")
public class ClaimOrderStatusVo {

	@ApiModelProperty(value = "주문 PK")
	private long odOrderId;

    @ApiModelProperty(value = "주문상세 PK")
    private long odOrderDetlId;

    @ApiModelProperty(value = "주문상태코드")
    private String orderStatusCd;

    @ApiModelProperty(value = "클레임상태코드")
    private String claimStatusCd;

}
