package kr.co.pulmuone.v1.shopping.cart.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 체크 요청 DTO")
public class CheckBuyPossibleCartRequestDto extends GetCartDataRequestDto {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(value = "매장 도착예정일")
	private LocalDate storeArrivalScheduledDate;

	@ApiModelProperty(value = "매장배송 회차 PK")
	private Long urStoreScheduleId;
}
