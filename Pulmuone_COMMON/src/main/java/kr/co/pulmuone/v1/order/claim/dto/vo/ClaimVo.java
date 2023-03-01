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
 * 주문 클레임 관련 VO
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
@ApiModel(description = "주문클레임 OD_CLAIM VO")
public class ClaimVo {

    @ApiModelProperty(value = "주문클레임 PK")
    private long odClaimId;

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

    @ApiModelProperty(value = "클레임상태구분 클레임상태구분 (CANCEL : 취소, RETURN : 반품, CS_REFUND: CS환불, RETURN_DELIVERY: 재배송 )")
    private String claimStatusTp;

	@ApiModelProperty(value = "CS환불구분(CS_REFUND_TP.PAYMENT_PRICE_REFUND : 결제금액 환불, CS_REFUND_TP.POINT_PRICE_REFUND : 적립금 환불)")
	private String csRefundTp;

	@ApiModelProperty(value = "CS환불승인상태(APPR_STAT.SAVE : 저장, APPR_STAT.REQUEST : 승인요청, APPR_STAT.APPROVED : 승인완료, APPR_STAT.DENIED : 승인반려)")
	private String csRefundApproveCd;

    @ApiModelProperty(value = "MALL 클레임 사유 PK")
    private long psClaimMallId;

    @ApiModelProperty(value = "상세사유")
    private String claimReasonMsg;

    @ApiModelProperty(value = "반품회수여부")
    private String returnsYn;

    @ApiModelProperty(value = "환불타입")
    private String refundType;

	@ApiModelProperty(value = "대표 상품명")
	private String goodsNm;

    @ApiModelProperty(value = "상품금액")
    private int goodsPrice;

    @ApiModelProperty(value = "환불예정상품금액(환불 상품금액 - 할인금액)")
    private int refundGoodsPrice;

    @ApiModelProperty(value = "상품쿠폰금액")
    private int goodsCouponPrice;

    @ApiModelProperty(value = "장바구니쿠폰금액")
    private int cartCouponPrice;

    @ApiModelProperty(value = "주문시 부과 배송비")
    private int orderShippingPrice;

    @ApiModelProperty(value = "배송비")
    private int shippingPrice;

    @ApiModelProperty(value = "환불금액")
    private int refundPrice;

    @ApiModelProperty(value = "환불적립금")
    private int refundPointPrice;

    @ApiModelProperty(value = "추가결제 결제마스터 PK")
    private long odPaymentMasterId;

    @ApiModelProperty(value = "직접결제여부")
    private String directPaymentYn;

    @ApiModelProperty(value = "추가결제방법")
    private String addPaymentTp;

    @ApiModelProperty(value = "등록자")
    private long createId;

    @ApiModelProperty(value = "등록일")
    private LocalDateTime createDt;

    @ApiModelProperty(value = "수정자")
    private long modifyId;

    @ApiModelProperty(value = "수정일")
    private LocalDateTime modifyDt;

    @ApiModelProperty(value = "승인자")
    private long approvalId;

    @ApiModelProperty(value = "승인일")
    private LocalDateTime approvalDt;

    @ApiModelProperty(value = "거부사유")
    private String rejectReasonMsg;

    @ApiModelProperty(value = "귀책구분 B: 구매자, S: 판매자")
    private String targetTp;

    @ApiModelProperty(value = "재배송구분 재배송 : R, 대체상품 : S")
    private String redeliveryType;

    @ApiModelProperty(value = "대표상품PK")
    private long ilGoodsId;

    @ApiModelProperty(value = "사용여부(거부,철회시 'N')")
    private String claimYn;
}
