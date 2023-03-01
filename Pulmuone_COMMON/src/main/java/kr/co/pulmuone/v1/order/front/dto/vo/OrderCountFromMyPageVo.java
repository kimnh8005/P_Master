package kr.co.pulmuone.v1.order.front.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문 개수 Result")
public class OrderCountFromMyPageVo {

    @ApiModelProperty(value = "주문 입금대기 수량")
    private int depositReadyCount;

    @ApiModelProperty(value = "주문 결제완료 수량")
    private int depositCompleteCount;

    @ApiModelProperty(value = "주문 배송준비중 수량")
    private int deliveryReadyCount;

    @ApiModelProperty(value = "주문 배송중 수량")
    private int deliveryDoingCount;

    @ApiModelProperty(value = "주문 배송완료 수량")
    private int deliveryCompleteCount;

    @ApiModelProperty(value = "주문 취소 수량")
    private int orderCancelCount;

    @ApiModelProperty(value = "주문 반품/환불 수량")
    private int orderReturnRefundCount;

    @ApiModelProperty(value = "일반 배송 수량")
    private int normalCount;

    @ApiModelProperty(value = "일일 배송 수량")
    private int dailyCount;

    @ApiModelProperty(value = "정기 배송 수량")
    private int regularCount;

    @ApiModelProperty(value = "매장 배송 수량")
    private int shopCount;

}
