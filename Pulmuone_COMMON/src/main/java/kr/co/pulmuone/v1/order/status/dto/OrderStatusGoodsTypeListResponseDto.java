package kr.co.pulmuone.v1.order.status.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusGoodsTypeVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " 주문상태 유형 리스트 조회 ResponseDto")
public class OrderStatusGoodsTypeListResponseDto {

	@ApiModelProperty(value = "주문상태 유형 카운트")
	private int total;

	@ApiModelProperty(value = "주문상태 유형 Vo")
	private List<OrderStatusGoodsTypeVo> rows;

}
