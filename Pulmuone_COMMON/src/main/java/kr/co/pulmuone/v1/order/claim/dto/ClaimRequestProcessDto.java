package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 철회 Response Dto
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

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 철회 Dto")
public class ClaimRequestProcessDto {

	@ApiModelProperty(value = "주문 PK")
	private long odOrderId;

	@ApiModelProperty(value = "클레임 PK")
	private long odClaimId;

	@ApiModelProperty(value = "클레임상태")
	private String status;

	@ApiModelProperty(value = "클레임 처리 결과 상태")
	private OrderEnums.OrderRegistrationResult claimResult;

	@ApiModelProperty(value = "클레임 처리 결과 매세지")
	private String message;

}
