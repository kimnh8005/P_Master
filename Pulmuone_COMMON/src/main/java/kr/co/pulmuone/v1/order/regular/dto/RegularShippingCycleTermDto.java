package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 기간 DTO")
public class RegularShippingCycleTermDto {
	/**
	 * 정기배송 기간 코드
	 */
	private String cycleTermType;

	/**
	 * 정기배송 기간 코드 명
	 */
	private String cycleTermTypeName;
}
