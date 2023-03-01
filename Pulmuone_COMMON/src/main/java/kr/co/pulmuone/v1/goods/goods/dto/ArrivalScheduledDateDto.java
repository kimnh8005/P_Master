package kr.co.pulmuone.v1.goods.goods.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "도착예정일 Dto")
public class ArrivalScheduledDateDto {

	/**
	 * 주문일자 (주문 I/F 일자)
	 */
	private LocalDate orderDate;

	/**
	 * 주문일자 요일 코드
	 */
	private String orderWeekCode;

	/**
	 * 출고 예정 일자
	 */
	private LocalDate forwardingScheduledDate;

	/**
	 * 도착 예정일자
	 */
	private LocalDate arrivalScheduledDate;

	/**
	 * 출고 예정 일자에 대한 재고 수량
	 */
	private int stock;

	/**
	 * 출고 불가 여부 (매장 상품만 사용 일반상품은 해당 Dto 자체가 없음)
	 */
	private boolean isUnDelivery;

	public void copy(ArrivalScheduledDateDto dto) {
		orderDate = dto.getOrderDate();
		orderWeekCode = dto.getOrderWeekCode();
		forwardingScheduledDate = dto.getForwardingScheduledDate();
		arrivalScheduledDate = dto.getArrivalScheduledDate();
		stock = dto.getStock();
		isUnDelivery = dto.isUnDelivery();
	}
}


