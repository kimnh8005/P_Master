package kr.co.pulmuone.v1.order.order.dto.vo;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 출고처 일자별 출고예정 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021.05.18.             홍진영         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "출고처 일자별 출고예정 VO")
public class WareHouseDailyShippingVo {
	/**
	 * 출고처 PK
	 */
	private Long urWarehouseId;

	/**
	 * 출고예정일일자
	 */
	private LocalDate shippingDate;

	/**
	 * 새벽배송여부
	 */
	private String dawnDeliveryYn;

	/**
	 * 출고예정수량
	 */
	private int shippingCount;
}
