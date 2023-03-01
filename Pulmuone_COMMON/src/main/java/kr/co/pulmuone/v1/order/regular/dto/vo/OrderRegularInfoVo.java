package kr.co.pulmuone.v1.order.regular.dto.vo;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderRegularInfoVo {

	/**
	 * 정기배송 신청 PK
	 */
	private Long odRegularReqId;

	/**
	 * 정기배송 신청 번호
	 */
	private String reqId;

	/**
	 * 정기배송 첫 도착일
	 */
	private LocalDate firstArrivalScheduledDate;

	/**
	 * 정기배송 주기 코드
	 */
	private String cycleType;

	/**
	 * 정기배송 기간 코드
	 */
	private String cycleTermType;

	/**
	 * 정기배송 시작일자
	 */
	private LocalDate startDate;

	/**
	 * 정기배송 마지막 일자
	 */
	private LocalDate endDate;

	/**
	 * 정기배송 다음 예정일
	 */
	private LocalDate nextArrivalScheduledDate;
}
