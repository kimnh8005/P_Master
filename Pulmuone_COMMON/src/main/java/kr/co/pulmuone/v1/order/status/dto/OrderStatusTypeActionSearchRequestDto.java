package kr.co.pulmuone.v1.order.status.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " 주문유형별 상태실행 검색조건 RequestDto")
public class OrderStatusTypeActionSearchRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "주문상태ID")
    private String statusCdSelect;

    @ApiModelProperty(value = "노출영역")
    private String useTypeSelect;

    @ApiModelProperty(value = "상품유형ID")
    private String typeCdSelect;

}
