package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 재배송 주문 상품별 금액 정보  Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 03. 18.     김명진         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 재배송 주문 상품별 금액 정보 Dto")
public class OrderClaimReShippingGoodsPaymentInfoDto {

	@ApiModelProperty(value = "주문상세PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "상품PK")
	private long ilGoodsId;

	@ApiModelProperty(value = "출고처PK")
	private long urWarehouseId;

	@ApiModelProperty(value = "출고처명")
	private String warehouseNm;

	@ApiModelProperty(value = "상품타입")
	private String goodsTpCd;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "클레임수량")
	private int claimCnt;

	@ApiModelProperty(value = "정상가")
	private int recommendedPrice;

	@ApiModelProperty(value = "판매가")
	private int salePrice;

	@ApiModelProperty(value = "쿠폰할인금액")
	private int discountPrice;

	@ApiModelProperty(value = "주문금액")
	private int orderPrice;

	@ApiModelProperty(value = "결제금액")
	private int paidPrice;

	@ApiModelProperty(value = "판매가총합")
	private int totSalePrice;

	@ApiModelProperty(value = "할인비율")
	private int discountRate;

	@ApiModelProperty(value = "상품배송타입 ")
	private String goodsDeliveryType;

	@ApiModelProperty(value = "주문상태 배송유형 공통코드: ORDER_STATUS_DELI_TP")
	private String orderStatusDeliTp;
}
