package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * CS 환불 상품 정보 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "CS 환불 상품 정보 Dto")
public class OrderCSRefundInfoDto {

	@ApiModelProperty(value = "주문 PK")
	private long odOrderId;

	@ApiModelProperty(value = "CS환불정보PK")
	private long odCsId;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "CS환불구분(CS_REFUND_TP.PAYMENT_PRICE_REFUND : 예치금 환불, CS_REFUND_TP.POINT_PRICE_REFUND : 적립금 환불)")
	private String csRefundTp;

	@ApiModelProperty(value = "CS환불승인상태(CS_REFUND_APPR_CD.REQUEST : 승인요청, CS_REFUND_APPR_CD.CANCEL: 승인취소, CS_REFUND_APPR_CD.DENIED:승인반려, CS_REFUND_APPR_CD.APPROVED: 승인완료)")
	private String csRefundApproveCd;

	@ApiModelProperty(value = "승인상태(APPR_STAT.SAVE : 저장, APPR_STAT.REQUEST : 승인요청, APPR_STAT.APPROVED : 승인완료, APPR_STAT.DENIED : 승인반려)")
	private String apprStat;

	@ApiModelProperty(value = "클레임사유코드")
	private String psClaimMallId;

	@ApiModelProperty(value = "귀책구분 B: 구매자, S: 판매자")
	private String targetTp;

	@ApiModelProperty(value = "상세사유")
	private String claimReasonMsg;

	@ApiModelProperty(value = "환불금액")
	private int refundPrice;

	@ApiModelProperty(value = "은행코드")
	private String bankCd;

	@ApiModelProperty(value = "예금주")
	private String accountHolder;

	@ApiModelProperty(value = "계좌번호")
	private String accountNumber;

	@ApiModelProperty(value = "CS환불 정상 여부")
	private String odCsYn;
}

