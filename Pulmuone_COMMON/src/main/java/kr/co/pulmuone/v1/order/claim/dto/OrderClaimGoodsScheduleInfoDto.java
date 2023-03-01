package kr.co.pulmuone.v1.order.claim.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 상품 스케쥴 정보 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 05. 03.            김명진         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 상품 스케쥴 정보 Dto")
public class OrderClaimGoodsScheduleInfoDto {

	@ApiModelProperty(value = "주문상세PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "주문상세일일스케쥴PK")
	private long odOrderDetlDailySchId;

	@ApiModelProperty(value = "클레임수량")
	private int claimCnt;

	@ApiModelProperty(value = "출고처ID(출고처PK)")
	private Long urWarehouseId;

	@ApiModelProperty(value = "상품ID(상품PK)")
	private Long ilGoodsId;

	@ApiModelProperty(value = "상품배송타입")
	private String goodsDeliveryType;

	@ApiModelProperty(value = "일일 배송주기코드")
	private String goodsCycleTp;

	@ApiModelProperty(value = "도착예정일자")
	private LocalDate deliveryDt;
}
