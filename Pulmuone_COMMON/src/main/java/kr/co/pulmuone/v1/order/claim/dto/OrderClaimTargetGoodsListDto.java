package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 대상 상품 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 03. 20.    김명진            최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 대상 상품 결과 Dto")
public class OrderClaimTargetGoodsListDto {

	@ApiModelProperty(value = "주문 pk")
	private long odOrderId;

	@ApiModelProperty(value = "주문상세 PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "주문상세 순번 주문번호에 대한 순번")
	private int odOrderDetlSeq;

	@ApiModelProperty(value = "주문상세 정렬 키")
	private long odOrderDetlStepId;

	@ApiModelProperty(value = "주문상세 뎁스")
	private long odOrderDetlDepthId;

	@ApiModelProperty(value = "주문상세 부모 ID")
	private long odOrderDetlParentId;

	@ApiModelProperty(value = "주문배송비PK")
	private long odShippingPriceId;

	@ApiModelProperty(value = "주문상태")
	private String orderStatusNm;

	@ApiModelProperty(value = "클레임상태")
	private String claimStatusCd;

	@ApiModelProperty(value = "출고처ID")
	private long urWarehouseId;

	@ApiModelProperty(value = "상품코드")
	private long ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "배송정책PK")
	private long ilShippingTmplId;

	@ApiModelProperty(value = "배송정책PK")
	private long shippingTmplId;

	@ApiModelProperty(value = "합배송여부")
	private String bundleYn;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "실제상품주문수량")
	private int orgOrderCnt;

	@ApiModelProperty(value = "주문취소수량")
	private int cancelCnt;

	@ApiModelProperty(value = "과세 여부")
	private String taxYn;

	@ApiModelProperty(value = "상품 전체 취소 여부")
	private String cancelYn;

	@ApiModelProperty(value = "정상가")
	private int recommendedPrice;

	@ApiModelProperty(value = "판매가")
	private int salePrice;

	@ApiModelProperty(value = "판매가총합")
	private int totSalePrice;

	@ApiModelProperty(value = "상품쿠폰할인")
	private int goodsCouponPrice;

	@ApiModelProperty(value = "장바구니쿠폰할인")
	private int cartCouponPrice;

	@ApiModelProperty(value = "상품 / 장바구니 제외 할인금액")
	private int directPrice;

	@ApiModelProperty(value = "결제금액")
	private int paidPrice;

    @ApiModelProperty(value = "배송비")
	private int shippingPrice;

    @ApiModelProperty(value = "주문시상품배송비")
	private int orderShippingPrice;

    @ApiModelProperty(value = "도서산간결제배송비")
	private int jejuIslandShippingPrice;

    @ApiModelProperty(value = "클레임배송비")
    private int claimShippingPrice;

    @ApiModelProperty(value = "추가결제배송비")
    private int addPaymentShippingPrice;

    @ApiModelProperty(value = "수령인우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "배송비쿠폰발급PK")
	private long deliveryCouponIssueId;

	@ApiModelProperty(value = "배송비쿠폰명")
	private String pmCouponNm;

	@ApiModelProperty(value = "배송비쿠폰혜택정보")
	private String pmCouponBenefit;

	@ApiModelProperty(value = "배송비할인금액")
	private int shippingDiscountPrice;

    @ApiModelProperty(value = "groupby 배송지 컬럼")
    private String shippingZone;

	@ApiModelProperty(value = "주문 상세 상태")
	private String orderStatusCd;

	@ApiModelProperty(value = "BOS json")
	private String bosJson;

	@ApiModelProperty(value = "주문자유형")
	private String buyerTypeCd;

	@ApiModelProperty(value = "재배송여부")
	private String redeliveryYn;

	@ApiModelProperty(value = "상품타입코드")
	private String goodsTpCd;

	@ApiModelProperty(value = "배송타입")
	private String conditionTp;

	@ApiModelProperty(value = "임직원 지원금")
	private int employeeDiscountPrice;
}
