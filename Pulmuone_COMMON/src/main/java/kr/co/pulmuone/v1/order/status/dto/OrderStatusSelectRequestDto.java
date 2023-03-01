package kr.co.pulmuone.v1.order.status.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = " 주문상태 업데이트 관련 RequestDto")
public class OrderStatusSelectRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "주문상세 Pk 리스트")
    List<Long> detlIdList;

}
