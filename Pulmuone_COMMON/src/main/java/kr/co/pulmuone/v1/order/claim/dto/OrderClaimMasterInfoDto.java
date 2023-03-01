package kr.co.pulmuone.v1.order.claim.dto;

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
 * 주문 클레임 조회 결과 Dto
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
@Setter
@Getter
@ToString
@ApiModel(description = "주문 클레임 조회 결과 Dto")
public class OrderClaimMasterInfoDto {

    @ApiModelProperty(value = "주문클레임 PK")
    private long odClaimId;

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

    @ApiModelProperty(value = "주문상태")
    private String orderStatusCd;

    @ApiModelProperty(value = "클레임상태")
    private String claimStatusCd;

    @ApiModelProperty(value = "클레임사유코드")
    private String claimReasonCd;

    @ApiModelProperty(value = "MALL 클레임 사유pk")
    private String psClaimMallId;

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

    @ApiModelProperty(value = "상품쿠폰금액")
    private int goodsCouponPrice;

    @ApiModelProperty(value = "장바구니쿠폰금액")
    private int cartCouponPrice;

    @ApiModelProperty(value = "배송비")
    private int shippingPrice;

    @ApiModelProperty(value = "환불금액")
    private int refundPrice;

    @ApiModelProperty(value = "환불적립금")
    private int refundPointPrice;

    @ApiModelProperty(value = "추가결제 결제마스터 PK")
    private long odPaymentMasterId;

    @ApiModelProperty(value = "CS환불 등록자")
    private String csId;

    @ApiModelProperty(value = "CS환불 일자")
    private LocalDateTime csDt;

    @ApiModelProperty(value = "환불요청 등록자")
    private String faId;

    @ApiModelProperty(value = "환불요청 일자")
    private LocalDateTime faDt;

    @ApiModelProperty(value = "환불완료 등록자")
    private String fcId;

    @ApiModelProperty(value = "환불완료 일자")
    private LocalDateTime fcDt;

    @ApiModelProperty(value = "클레임요청 등록자")
    private String crId;

    @ApiModelProperty(value = "클레임요청 일자")
    private LocalDateTime crDt;

    @ApiModelProperty(value = "클레임승인 등록자")
    private String ceId;

    @ApiModelProperty(value = "클레임승인 일자")
    private LocalDateTime ceDt;

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
}
