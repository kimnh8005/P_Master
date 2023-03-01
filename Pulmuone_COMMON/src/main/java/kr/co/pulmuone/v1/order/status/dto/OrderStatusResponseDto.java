package kr.co.pulmuone.v1.order.status.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문상태 ResponseDto")
public class OrderStatusResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "레코드")
    private OrderStatusVo rows;


}
