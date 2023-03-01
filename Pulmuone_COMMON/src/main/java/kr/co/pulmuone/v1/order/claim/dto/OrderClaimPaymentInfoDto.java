package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 결제정보 조회 결과 Dto
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
@ApiModel(description = "주문 클레임 결제정보 조회 결과 Dto")
public class OrderClaimPaymentInfoDto {

	@ApiModelProperty(value = "주문금액")
	private int paidPrice;

	@ApiModelProperty(value = "결제방법")
	private String payTp;

	@ApiModelProperty(value = "결제방법명")
	private String payTpNm;

	@ApiModelProperty(value = "환불방법")
	private String refundPayTpNm;

	@ApiModelProperty(value = "결제금액")
	private int paymentPrice;

	@ApiModelProperty(value = "적립금")
	private int pointPrice;

	@ApiModelProperty(value = "odPaymentMasterId")
	private long odPaymentMasterId;

	@ApiModelProperty(value = "부분취소 가능여부")
	private String partCancelYn;

	@ApiModelProperty(value = "과세금액")
	private int taxablePrice;

	@ApiModelProperty(value = "비과세금액")
	private int nonTaxablePrice;
}
