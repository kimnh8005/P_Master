package kr.co.pulmuone.v1.goods.goods.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문상세 주문I/F 출고지시일 Dto")
public class IfDateListDto {

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
	 * 출고 예정 일자(mm-dd(요일))
	 */
	private String forwardingScheduledDateStr;

	/**
	 * 도착 예정일자(mm-dd(요일))
	 */
	private String arrivalScheduledDateStr;


	public IfDateListDto(ArrivalScheduledDateDto arrivalScheduledDateDto) {
		this.setOrderDate(arrivalScheduledDateDto.getOrderDate());
		this.setOrderWeekCode(arrivalScheduledDateDto.getOrderWeekCode());
		this.setForwardingScheduledDate(arrivalScheduledDateDto.getForwardingScheduledDate());
		this.setArrivalScheduledDate(arrivalScheduledDateDto.getArrivalScheduledDate()) ;
		this.setStock(arrivalScheduledDateDto.getStock());
	}


}
