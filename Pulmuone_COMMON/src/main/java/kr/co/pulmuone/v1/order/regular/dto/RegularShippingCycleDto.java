package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 주기 DTO")
public class RegularShippingCycleDto {
	/**
	 * 정기배송 주기 코드
	 */
	private String cycleType;

	/**
	 * 정기배송 주기 코드 명
	 */
	private String cycleTypeName;
}
