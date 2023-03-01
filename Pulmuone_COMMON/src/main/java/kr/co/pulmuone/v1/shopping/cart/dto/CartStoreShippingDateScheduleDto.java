package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 매장 배송 스케줄 DTO")
public class CartStoreShippingDateScheduleDto {

	/**
	 * 매장배송 회차 PK
	 */
	private Long urStoreScheduleId;

	/**
	 * 매장배송 회차 선택 가능 여부(true:선택가능, false:마감)
	 */
	private boolean isPossibleSelect;

	/**
	 * 매장배송 배송시작시간
	 */
	private String startTime;

	/**
	 * 매장배송 배송종료시간
	 */
	private String endTime;
}
