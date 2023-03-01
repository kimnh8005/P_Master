package kr.co.pulmuone.v1.order.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 03. 31.            홍진영         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 환불계좌 VO")
public class OrderAccountVo {
	@ApiModelProperty(value = "주문 환불계좌 PK")
	private long odOrderAccountId;

	@ApiModelProperty(value = "주문 PK")
	private long odOrderId;

	@ApiModelProperty(value = "은행코드")
	private String bankCd;

	@ApiModelProperty(value = "예금주")
	private String accountHolder;

	@ApiModelProperty(value = "계좌번호")
	private String accountNumber;

}
