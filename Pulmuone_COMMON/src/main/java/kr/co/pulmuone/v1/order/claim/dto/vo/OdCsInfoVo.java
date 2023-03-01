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
 * CS 환불 정보 VO
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
@ApiModel(description = "CS 환불 정보 VO")
public class OdCsInfoVo {

    @ApiModelProperty(value = "CS환불정보PK")
    private long odCsId;

    @ApiModelProperty(value = "주문PK")
    private long odOrderId;

    @ApiModelProperty(value = "CS환불구분(CS_REFUND_TP.PAYMENT_PRICE_REFUND : 결제금액 환불, CS_REFUND_TP.POINT_PRICE_REFUND : 적립금 환불)")
    private String csRefundTp;

	@ApiModelProperty(value = "CS환불승인상태(CS_REFUND_APPR_CD.REQUEST : 승인요청, CS_REFUND_APPR_CD.CANCEL: 승인취소, CS_REFUND_APPR_CD.DENIED:승인반려, CS_REFUND_APPR_CD.APPROVED: 승인완료)")
	private String csRefundApproveCd;

	@ApiModelProperty(value = "승인상태(APPR_STAT.SAVE : 저장, APPR_STAT.REQUEST : 승인요청, APPR_STAT.APPROVED : 승인완료, APPR_STAT.DENIED : 승인반려)")
	private String apprStat;

	@ApiModelProperty(value = "환불금액(CS환불상세 REFUND_PRICE 합계)")
	private int refundPrice;

	@ApiModelProperty(value = "MALL 클레임 사유 PK")
	private long psClaimMallId;

	@ApiModelProperty(value = "상세사유")
	private String csReasonMsg;

	@ApiModelProperty(value = "귀책구분(B: 구매자귀책, S: 판매자귀책)")
	private String targetTp;

	@ApiModelProperty(value = "승인요청자")
	private long apprReqUserId;

	@ApiModelProperty(value = "승인요청일")
	private LocalDateTime apprReqDt;

	@ApiModelProperty(value = "승인1차담당자")
	private Long apprSubUserId;

	@ApiModelProperty(value = "승인1차처리자")
	private Long apprSubChgUserId;

    @ApiModelProperty(value = "승인1차처리일")
    private LocalDateTime apprSubChgDt;

    @ApiModelProperty(value = "승인2차담당자")
    private Long apprUserId;

    @ApiModelProperty(value = "승인2차처리자")
    private Long apprChgUserId;

    @ApiModelProperty(value = "승인2차처리일")
    private LocalDateTime apprChgDt;

    @ApiModelProperty(value = "CS환불 정상 여부")
	private String odCsYn;
}
