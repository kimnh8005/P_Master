package kr.co.pulmuone.v1.order.present.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "선물하기 배송 가능 체크 요청 DTO")
public class IsShippingPossibilityRequestDto {

	@ApiModelProperty(value = "선물하기 ID")
	private String presentId;

	@ApiModelProperty(value = "주문 PK")
	private Long odOrderId;

	@ApiModelProperty(value = "주소DTO")
	private OrderPresentShippingZoneDto shippingZone;

	@ApiModelProperty(value = "도착예정일 스케줄")
	private List<OrderPresentArrivalScheduledDto> arrivalScheduled;

	public OrderPresentArrivalScheduledDto getOrderPresentArrivalScheduledDto(Long odShippingPriceId) {
		return arrivalScheduled.stream()
				.filter(dto -> dto.getOdShippingPriceId().equals(odShippingPriceId))
	            .findAny()
	            .orElse(null);
	}
}
