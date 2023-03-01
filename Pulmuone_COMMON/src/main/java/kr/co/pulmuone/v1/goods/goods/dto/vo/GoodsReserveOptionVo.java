package kr.co.pulmuone.v1.goods.goods.dto.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsReserveOptionVo {

	/**
	 * 예약 정보 PK
	 */
	private Long ilGoodsReserveOptionId;

	/**
	 * 예약 정보 배송회차
	 */
	private int saleSeq;

	/**
	 * 예약 주문 시작 시간
	 */
	private LocalDateTime reserveStartDate;

	/**
	 * 예약 주문 마감 시간
	 */
	private LocalDateTime reserveEndDate;

	/**
	 * 예약 정보 주문 가능 수량
	 */
	private int stockQty;

	/**
	 * 주문 수집일 IF일
	 */
	private LocalDate orderIfDate;

	/**
	 * 출고 예정일
	 */
	private LocalDate releaseDate;

	/**
	 * 예약 정보 도착일자
	 */
	private LocalDate arriveDate;
}
