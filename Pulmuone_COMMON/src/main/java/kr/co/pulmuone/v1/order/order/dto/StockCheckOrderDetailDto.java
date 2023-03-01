package kr.co.pulmuone.v1.order.order.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 재고 체크 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 04.            홍진영         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 재고 체크 Dto")
public class StockCheckOrderDetailDto {

	// 주문 상세 PK
	private long odOrderDetlId;

	// 주문 배송비 PK
	private long odShippingPriceId;

	// 출고처 PK
	private long urWarehouseId;

	// 상품 코드 PK
	private long ilGoodsId;

	// 판매유형
	private String saleType;

	// 배송유형
	private String goodsDeliveryType;

	// 주문수량
	private int orderCnt;

	// 배송주기 공통코드:GOODS_CYCLE_TP
	private String goodsCycleTp;

	// 출고예정일자
	private LocalDate shippingDt;

	// 도착예정일일자
	private LocalDate deliveryDt;

	// 예약 정보 PK
	private Long ilGoodsReserveOptionId;

	// 매장PK
	private String urStoreId;

	// 품목PK
	private String ilItemCd;

	// 매장배송 회차 PK
	private Long urStoreScheduleId;

	// 도착예정일일자 스케줄
	private List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList;

	private String goodsDailyBulkYn;

	private int monCnt;

	private int tueCnt;

	private int wedCnt;

	private int thuCnt;

	private int friCnt;

	private String promotionTp;

}
