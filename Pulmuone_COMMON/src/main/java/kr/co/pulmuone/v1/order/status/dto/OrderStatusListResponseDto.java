package kr.co.pulmuone.v1.order.status.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " 주문상태 리스트 조회 RequestDto")
public class OrderStatusListResponseDto{

	@ApiModelProperty(value = "주문상태 카운트")
	private int total;

	@ApiModelProperty(value = "주문상태 목록")
	private List<OrderStatusVo> rows;



}
