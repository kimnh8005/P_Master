package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.OrderClaimEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문 클레임 추가결제 정보 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 05. 14.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "Mall 주문 클레임 추가결제 정보 Response Dto")
public class MallOrderClaimAddPaymentValidResult {

	@ApiModelProperty(value ="유효성체크 결과")
	private OrderClaimEnums.AddPaymentShippingPriceError validResult;

	@ApiModelProperty(value = "결제대상 정보 결과")
	private MallOrderClaimAddPaymentResult addShippingInfo;
}
