package kr.co.pulmuone.v1.order.status.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusActionVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " 주문상태실행 리스트 조회 ResponseDto")
public class OrderStatusActionListResponseDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "주문상태실행 Vo")
	private List<OrderStatusActionVo> rows;
	private int total;

}
