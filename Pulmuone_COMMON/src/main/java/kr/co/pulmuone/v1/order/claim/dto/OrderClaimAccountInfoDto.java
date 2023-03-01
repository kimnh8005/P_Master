package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingAccountNumber;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 환불계좌 조회 결과  Dto
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
@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 환불계좌 조회 결과 Dto")
public class OrderClaimAccountInfoDto {

	@ApiModelProperty(value = "주문클레임 환불계좌 PK")
	private long odClaimAccountId;

	@ApiModelProperty(value = "주문 클레임 PK")
	private long odClaimId;

	@ApiModelProperty(value = "은행코드")
	private String bankCd;

	@ApiModelProperty(value = "예금주")
	private String accountHolder;

	@ApiModelProperty(value = "계좌번호")
	private String accountNumber;
}
