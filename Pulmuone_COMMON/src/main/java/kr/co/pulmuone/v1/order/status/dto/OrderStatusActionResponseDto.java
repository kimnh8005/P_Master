package kr.co.pulmuone.v1.order.status.dto;

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
@ApiModel(description = " 주문상태실행 상세조회 ResponseDto")
public class OrderStatusActionResponseDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "레코드")
	private OrderStatusActionVo rows;

}
