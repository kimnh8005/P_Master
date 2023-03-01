package kr.co.pulmuone.v1.shopping.cart.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문 생성 장바구니 정보")
public class CartRegularDto {

	/**
	 * 정기배송 배송주기 코드
	 */
	private String cycleType;

	/**
	 * 정기배송 배송기간 코드
	 */
	private String cycleTermType;

	/**
	 * 정기배송 시작일자
	 */
	private LocalDate arrivalScheduledDate;
}
