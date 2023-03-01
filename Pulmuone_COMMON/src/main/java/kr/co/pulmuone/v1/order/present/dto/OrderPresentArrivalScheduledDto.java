package kr.co.pulmuone.v1.order.present.dto;

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
@ApiModel(description = "배송 도착일 선택 DTO")
public class OrderPresentArrivalScheduledDto {

	@ApiModelProperty(value = "배송정책 PK")
	private Long odShippingPriceId;

	@ApiModelProperty(value = "새벽배송 여부")
	private String dawnDeliveryYn;

	@ApiModelProperty(value = "배송 스케쥴 변경 도착 예정일자(yyyy-mm-dd)")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate arrivalScheduledDate;
}
