package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 적립금 환불, 환불금액 대상 조회 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "적립금 환불, 환불금액 대상 조회 결과 Dto")
public class OrderClaimRefundDetailTargetDto {

	@ApiModelProperty(value = "주문 클레임 PK")
	private long odClaimId;

	@ApiModelProperty(value = "주문 클레임 상세 PK")
	private long odClaimDetlId;

	@ApiModelProperty(value = "주문상세 PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "클레임 처리 수량")
	private int claimCnt;

	@ApiModelProperty(value = "추문 수량")
	private int orderCnt;
}
