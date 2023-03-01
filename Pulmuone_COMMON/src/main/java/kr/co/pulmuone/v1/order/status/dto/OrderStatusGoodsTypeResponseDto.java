package kr.co.pulmuone.v1.order.status.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.status.dto.vo.OrderStatusGoodsTypeVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문상태 유형 ResponseDto")
public class OrderStatusGoodsTypeResponseDto {

    @ApiModelProperty(value = "레코드")
    private OrderStatusGoodsTypeVo rows;

}
