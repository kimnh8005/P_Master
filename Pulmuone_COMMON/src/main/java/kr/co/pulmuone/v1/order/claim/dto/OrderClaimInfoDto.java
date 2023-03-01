package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 파일첨부 정보 조회 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 04. 02.            김명진         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 파일첨부 정보 조회 결과 Dto")
public class OrderClaimInfoDto {

	@ApiModelProperty(value = "주문클레임PK")
	private long odClaimId;

	@ApiModelProperty(value = "주문PK")
	private long odOrderId;

	@ApiModelProperty(value = "주문상태코드")
	private String orderStatusCd;

	@ApiModelProperty(value = "클레임상태코드")
	private String claimStatusCd;

	@ApiModelProperty(value = "클레임상태타입")
	private String claimStatusTp;

	@ApiModelProperty(value = "재배송타입")
	private String redeliveryType;

	@ApiModelProperty(value = "CS환불타입")
	private String csRefundTp;

	@ApiModelProperty(value = "CS환불승인코드")
	private String csRefundApproveCd;

	@ApiModelProperty(value = "클레임사유")
	private long psClaimMallId;

	@ApiModelProperty(value = "클레임사유코드")
	private String claimReasonCd;

	@ApiModelProperty(value = "클레임상세메시지")
	private String claimReasonMsg;

	@ApiModelProperty(value = "거부상세메시지")
	private String rejectReasonMsg;

	@ApiModelProperty(value = "반품회수여부")
	private String returnsYn;

	@ApiModelProperty(value = "환불타입")
	private String refundType;

	@ApiModelProperty(value = "상품명")
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

	@ApiModelProperty(value = "환불포인트금액")
	private int refundPointPrice;

	@ApiModelProperty(value = "결제마스터PK")
	private long odPaymentMasterId;

	@ApiModelProperty(value = "귀책구분")
	private String targetTp;

	@ApiModelProperty(value = "직접결제여부")
	private String directPaymentYn;

	@ApiModelProperty(value = "생성자ID")
	private long createId;

	@ApiModelProperty(value = "생성일자")
	private String createDt;

	@ApiModelProperty(value = "수정자ID")
	private long modifyId;

	@ApiModelProperty(value = "수정일자")
	private String modifyDt;

	@ApiModelProperty(value = "승인자ID")
	private long approvalId;

	@ApiModelProperty(value = "승인일자")
	private String approvalDt;
}
