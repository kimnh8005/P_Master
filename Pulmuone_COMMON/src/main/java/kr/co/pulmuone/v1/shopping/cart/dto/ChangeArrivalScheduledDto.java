package kr.co.pulmuone.v1.shopping.cart.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송 스케쥴 변경 DTO")
public class ChangeArrivalScheduledDto {
	/**
	 * 배송 스케쥴 변경 도착 예정일자(yyyy-mm-dd)
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate arrivalScheduledDate;

	/**
	 * 새벽배송 여부
	 */
	private String dawnDeliveryYn;
}
