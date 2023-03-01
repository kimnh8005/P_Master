package kr.co.pulmuone.v1.order.email.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "직접배송 미등록 송장 알림 DTO")
public class UnregistetedInvoiceDto {

    @ApiModelProperty(value = "no")
    private String no;

    @ApiModelProperty(value = "출고처")
    private String warehouseNm;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "품목바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "품목번호")
    private String ilItemCd;

    @ApiModelProperty(value = "상품번호")
    private String ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "주문수량")
    private int orderCnt;

    @ApiModelProperty(value = "배송일")
    private String deliveryDt;
}
