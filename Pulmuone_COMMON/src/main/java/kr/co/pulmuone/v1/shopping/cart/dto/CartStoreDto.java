package kr.co.pulmuone.v1.shopping.cart.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문 생성 매장배송/픽업 정보")
public class CartStoreDto {
	/**
	 * 매장 배송 유형
	 */
	private String storeDeliveryType;

	/**
	 * 매장 도착예정일
	 */
	private LocalDate storeArrivalScheduledDate;

	/**
	 * 매장배송 회차 PK
	 */
	private Long urStoreScheduleId;
}
