package kr.co.pulmuone.v1.shopping.cart.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.order.regular.dto.RegularShippingCycleDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularShippingCycleTermDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 정기배송 DTO")
public class CartRegularShippingDto {

	/**
	 * 정기배송 신청 PK
	 */
	private Long odRegularReqId;

	/**
	 * 정기배송 첫 도착 예정일자
	 */
	private LocalDate firstArrivalScheduledDate;

	/**
	 * 도착 예정일 선택일
	 */
	private List<LocalDate> choiceArrivalScheduledDateList;

	/**
	 * 정기배송 주기 리스트
	 */
	private List<RegularShippingCycleDto> cycle;

	/**
	 * 정기배송 기간 리스트
	 */
	private List<RegularShippingCycleTermDto> cycleTerm;

	/**
	 * 정기배송 추가 주문 여부
	 */
	private boolean isAdditionalOrder;

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
	 * 정기배송 종료일자
	 */
	private LocalDate endDate;

	/**
	 * 정기배송 다음 도착 예정일
	 */
	private LocalDate nextArrivalScheduledDate;

}
