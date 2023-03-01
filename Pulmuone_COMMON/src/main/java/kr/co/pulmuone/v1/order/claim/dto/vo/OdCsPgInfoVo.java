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
 * CS 환불 PG 정보 VO
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
@ApiModel(description = "CS 환불 PG 정보 VO")
public class OdCsPgInfoVo {

    @ApiModelProperty(value = "CS환불PG PK")
    private long odRefundPgId;

    @ApiModelProperty(value = "CS환불정보PK")
    private long odCsId;

    @ApiModelProperty(value = "결제타입 (G : 결제, F : 환불 , A : 추가)")
    private String type;

	@ApiModelProperty(value = "결제상태(IR:입금예정,IC:입금완료)")
	private String status;

	@ApiModelProperty(value = "거래번호(trade_seq)")
	private String tid;

	@ApiModelProperty(value = "CS환불금액")
	private int refundPrice;

	@ApiModelProperty(value = "응답데이터")
	private String responseData;

    @ApiModelProperty(value = "승인일자")
    private LocalDateTime approvalDt;

    @ApiModelProperty(value = "생성일자")
    private LocalDateTime createDt;
}
