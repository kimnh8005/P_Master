package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 상품 수량 변경 조회  Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 상품 수량 변경 조회  Dto")
public class OrderClaimSearchGoodsDto {

	@ApiModelProperty(value = "주문 PK")
	private long odOrderId;

	@ApiModelProperty(value = "주문상세 PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "주문클레임 상세 PK")
	private long odClaimDetlId;

	@ApiModelProperty(value = "클레임수량")
	private int claimCnt;

	@ApiModelProperty(value = "주문취소수량")
	private int cancelCnt;

	@ApiModelProperty(value = "주문수량")
	private int orderCnt;

	@ApiModelProperty(value = "주문금액")
	private int orderPrice;

	@ApiModelProperty(value = "쿠폰할인금액")
	private int goodsCouponPrice;

	@ApiModelProperty(value = "결제금액")
	private int paidPrice;

	@ApiModelProperty(value = "출고처 PK : UR_WAREHOUSE.UR_WAREHOUSE_ID")
	private long urWarehouseId;

	@ApiModelProperty(value = "클레임수량 변경 상품 유무 Y :예, N : 아니오")
	private String claimGoodsYn;

	@ApiModelProperty(value = "상품ID(상품PK)")
	private Long ilGoodsId;

	@ApiModelProperty(value = "상품배송타입")
	private String goodsDeliveryType;

	@ApiModelProperty(value = "일일 배송주기코드")
	private String goodsCycleTp;

	@ApiModelProperty(value = "요일코드")
	private String weekDayCd;
}
