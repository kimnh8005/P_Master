package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 CS환불 정보 조회 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 04. 10.   			  김명진        최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 CS환불 정보 조회 Response Dto")
public class OrderClaimCSRefundResponseDto {

	@ApiModelProperty(value = "주문 클레임 PK")
	private long odClaimId;

	@ApiModelProperty(value = "주문 PK")
	private long odOrderId;

	@ApiModelProperty(value = "CS환불 요청 금액")
	private int refundPrice;

	@ApiModelProperty(value = "CS환불 요청 적립금")
	private int refundPointPrice;

	@ApiModelProperty(value = "CS환불 승인 코드")
	private String csRefundApproveCd;
}
