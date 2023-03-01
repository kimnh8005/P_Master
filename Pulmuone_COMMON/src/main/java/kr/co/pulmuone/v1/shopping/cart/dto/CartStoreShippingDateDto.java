package kr.co.pulmuone.v1.shopping.cart.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 매장 배송 가능 일자 DTO")
public class CartStoreShippingDateDto {

	/**
	 * 매장 배송 가능 일자
	 */
	private LocalDate arrivalScheduledDate;

	/**
	 * 매장 배송 스케줄 정보
	 */
	private List<CartStoreShippingDateScheduleDto> schedule;
}
