package kr.co.pulmuone.v1.order.status.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusTypeActionVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " 주문상태실행 리스트 조회 ResponseDto")
public class OrderStatusTypeActionListResponseDto {

	@ApiModelProperty(value = "주문상태실행 Vo")
	private List<OrderStatusTypeActionVo> rows;

}
