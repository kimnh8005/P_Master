package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <PRE>
 * Forbiz Korea
 * 추가결제 후 클레임 정보 담기 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 20.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "추가결제 후 클레임 정보 담기  Dto")
public class EtcDataClaimDto {

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "추가결제요청정보PK")
	private long odAddPaymentReqInfoId;

	@ApiModelProperty(value = "주문클레임PK")
	private long odClaimId;

	@ApiModelProperty(value = "결제 정보 코드")
	private String paymentType;
}
