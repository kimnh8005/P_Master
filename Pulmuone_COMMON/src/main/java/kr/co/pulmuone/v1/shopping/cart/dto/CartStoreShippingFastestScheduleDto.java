package kr.co.pulmuone.v1.shopping.cart.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 매장 배송 가장 빠른 스케줄 DTO")
public class CartStoreShippingFastestScheduleDto {

	/**
	 * 매장 배송 가능 일자
	 */
	private LocalDate arrivalScheduledDate;

	/**
	 * 매장배송 배송시작시간
	 */
	private String startTime;

	/**
	 * 매장배송 배송종료시간
	 */
	private String endTime;
}
