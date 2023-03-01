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
 * 주문 클레임 환불정보에서 상품금액 조회 결과 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 04. 09.            김명진         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 상품 별 클레임 금액정보 리스트 Dto")
public class OrderClaimGoodsPriceInfoDto {

	@ApiModelProperty(value = "주문상세 PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "상품 PK")
	private long ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "클레임수")
	private int claimCnt;

	@ApiModelProperty(value = "판매가")
	private int salePrice;

	@ApiModelProperty(value = "판매가총합")
	private int totSalePrice;

	@ApiModelProperty(value = "장바구니쿠폰")
	private int cartCouponPrice;

	@ApiModelProperty(value = "상품쿠폰")
	private int goodsCouponPrice;

	@ApiModelProperty(value = "즉시할인 금액")
	private int directPrice;

	@ApiModelProperty(value = "결제금액")
	private int paidPrice;
}
