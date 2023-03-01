package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 정보 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 18.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "신용카드 비인증 결제 금액 정보 dto")
public class PaymentInfoDto {
	@ApiModelProperty(value = "상품결제금액")
	private int paidPrice;

    @ApiModelProperty(value = "과세결제금액")
    private int taxablePrice;

    @ApiModelProperty(value = "비과세결제금액")
    private int nonTaxablePrice;

    @ApiModelProperty(value = "결제금액")
	private int paymentPrice;
}
