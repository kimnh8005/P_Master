package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문상세 리스트 Response Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 20.	이규한		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 상담원 결제 결제 Request DTO")
public class MallApplyPaymentDirectOrderRequestDto {

	@ApiModelProperty(value = "주문 PK", required = true)
	private Long odOrderId;

	@ApiModelProperty(value = "결제 정보 PK", required = true)
	private String psPayCd;

	@ApiModelProperty(value = "카드 정보 PK")
	private String cardCode;

	@ApiModelProperty(value = "할부 기간")
	private String installmentPeriod;

	@ApiModelProperty(value = "현재 결제 요청 URL")
	private String orderInputUrl;
}