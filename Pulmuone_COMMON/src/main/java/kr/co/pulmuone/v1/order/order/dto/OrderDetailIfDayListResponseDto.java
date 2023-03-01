package kr.co.pulmuone.v1.order.order.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "주문I/F 출고지시일 목록 조회 ResponseDto")
public class OrderDetailIfDayListResponseDto extends BaseRequestDto {

	@ApiModelProperty(value = "출고지시일 리스트")
	private List<ArrivalScheduledDateDto> deliveryList;

	@ApiModelProperty(value = "새벽배송 출고지시일 리스트")
	private List<ArrivalScheduledDateDto> dawnDeliveryList;
}