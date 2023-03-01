package kr.co.pulmuone.v1.order.order.dto;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.IfDateListDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "주문I/F 출고지시일 목록 조회 ResponseDto")
public class OrderDetailIfDateListResponseDto extends BaseRequestDto {

	@ApiModelProperty(value = "출고지시일 리스트")
	private List<IfDateListDto> deliveryList;

	@ApiModelProperty(value = "새벽배송 출고지시일 리스트")
	private List<IfDateListDto> dawnDeliveryList;

	@ApiModelProperty(value = "상품별 일반배송 출고지시일 리스트")
	private Map<Long, List<ArrivalScheduledDateDto>> goodsDeliveryDateMap;

	@ApiModelProperty(value = "상품별 새벽배송 출고지시일 리스트")
	private Map<Long,List<ArrivalScheduledDateDto>> goodsDawnDeliveryDateMap;
}