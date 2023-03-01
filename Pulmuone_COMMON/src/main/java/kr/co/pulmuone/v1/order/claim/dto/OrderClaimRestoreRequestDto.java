package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 철회 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 03. 12.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 철회 Request Dto")
public class OrderClaimRestoreRequestDto {

	@ApiModelProperty(value = "주문 클레임 PK")
	private long odClaimId;

	@ApiModelProperty(value = "결제타입")
	private String type;

	@ApiModelProperty(value = "환불타입")
	private String refundType;

	@ApiModelProperty(value = "결제상태")
	private String status;
}
