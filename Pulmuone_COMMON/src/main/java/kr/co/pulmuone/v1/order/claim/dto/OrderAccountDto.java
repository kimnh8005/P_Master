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
 * 주문리스트 관련 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 03. 15.    천혜현        최초작성
 *
 *
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 환불계좌 정보 Dto")
public class OrderAccountDto {

	//환불계좌정보 PK
	private String odOrderAccountId;

	//은행코드
	private String bankCode;

	//은행명
	private String bankName;

	//예금주
	private String holderName;

	//계좌번호
	private String accountNumber;
}

