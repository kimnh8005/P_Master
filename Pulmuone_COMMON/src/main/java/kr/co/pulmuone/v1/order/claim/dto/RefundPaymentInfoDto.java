package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * PG 또는 BANK 환불을 위한 결제정보 조회 결과 Dto
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
@ApiModel(description = "PG 또는 BANK 환불을 위한 결제정보 조회 결과 Dto")
public class RefundPaymentInfoDto {

	@ApiModelProperty(value = "결제방법 공통코드(PAY_TP)")
	private String payTp;

	@ApiModelProperty(value = "PG 종류 공통코드(PG_ACCOUNT_TYPE)")
	private String pgService;

	@ApiModelProperty(value = "거래 ID")
	private String tid;

	@ApiModelProperty(value = "결제과세금액")
	private int taxablePrice;

	@ApiModelProperty(value = "결제비과세금액")
	private int nonTaxablePrice;

	@ApiModelProperty(value = "취소후 남는 금액")
	private int remaindPrice;

	@ApiModelProperty(value = "결제금액")
	private int paymentPrice;

	@ApiModelProperty(value = "과세금액 잔액")
	private int remaindTaxablePrice;

	@ApiModelProperty(value = "비과세금액 잔액")
	private int remaindNonTaxablePrice;

	@ApiModelProperty(value = "적립금 결제금액")
	private int pointPrice;

	@ApiModelProperty(value = "에스크로결제여부")
	private String escrowYn;

	@ApiModelProperty(value = "부분취소가능여부")
	private String partCancelYn;
}
