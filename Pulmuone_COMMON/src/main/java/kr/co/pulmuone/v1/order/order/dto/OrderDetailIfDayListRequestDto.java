package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문I/F 출고지시일 목록 조회 RequestDto")
public class OrderDetailIfDayListRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "출고처ID(출고처PK)")
	private Long urWarehouseId;

	@ApiModelProperty(value = "상품ID(상품PK)")
	private Long ilGoodsId;

	@ApiModelProperty(value = "새벽배송여부")
	private Boolean isDawnDelivery;

	@ApiModelProperty(value = "일일 배송주기코드")
	private String goodsDailyCycleType;
}