package kr.co.pulmuone.v1.order.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 현금영수증 OD_ORDER_CASH_RECEIPT VO")
public class OrderCashReceiptVo {
    @ApiModelProperty(value = "주문 현금영수증 PK")
    private long odOrderCashReceiptId;

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

    @ApiModelProperty(value = "주문 결제 마스터 PK")
    private long odPaymentMasterId;

    @ApiModelProperty(value = "신청번호")
    private String cashReceiptNo;

    @ApiModelProperty(value = "승인번호")
    private String cashReceiptAuthNo;

    @ApiModelProperty(value = "현금영수증 발급구분")
    private String cashReceiptType;

    @ApiModelProperty(value = "현금영수증 발급번호(암호화)")
    private String cashNo;

    @ApiModelProperty(value = "현금영수증 발급금액")
    private int cashPrice;
}
