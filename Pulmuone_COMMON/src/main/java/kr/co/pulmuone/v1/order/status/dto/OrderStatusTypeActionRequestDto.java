package kr.co.pulmuone.v1.order.status.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " 주문유형별 상태실행 등록RequestDto")
public class OrderStatusTypeActionRequestDto {

	@ApiModelProperty(value = "주문상태코드")
    private String statusCd;

    @ApiModelProperty(value = "주문상태 사용구분")
    private String useType;

    @ApiModelProperty(value = "유형코드")
    private String typeCd;

    @ApiModelProperty(value = "노출버튼명")
    private String actionNm;

    @ApiModelProperty(value = "실행ID")
    private Long actionId;

    @ApiModelProperty(value = "실행순번")
    private Long actionSeq;
}
