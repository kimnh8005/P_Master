package kr.co.pulmuone.v1.order.claim.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


/**
 * <PRE>
 * Forbiz Korea
 * CS 환불 계좌 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 13.            김명진         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "CS 환불 계좌 VO")
public class OdCsAccountVo {

    @ApiModelProperty(value = "CS환불계좌PK")
    private long odCsAccountId;

    @ApiModelProperty(value = "CS환불정보PK")
    private long odCsId;

    @ApiModelProperty(value = "은행코드")
    private String bankCd;

	@ApiModelProperty(value = "예금주명")
	private String accountHolder;

	@ApiModelProperty(value = "계좌번호")
	private String accountNumber;
}
