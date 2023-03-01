package kr.co.pulmuone.v1.order.status.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " 주문유형별 상태실행관리 노출상태관리 수정 RequestDto")
public class OrderStatusDisplayRequestDto {

	@ApiModelProperty(value = "주문상태코드")
    private String statusCd;

    @ApiModelProperty(value = "유형별 상태 코드명")
    private String actionStatusNm;

    @ApiModelProperty(value = "유형코드")
    private String typeCd;

    @ApiModelProperty(value = "사용구분")
    private String useType;

}
