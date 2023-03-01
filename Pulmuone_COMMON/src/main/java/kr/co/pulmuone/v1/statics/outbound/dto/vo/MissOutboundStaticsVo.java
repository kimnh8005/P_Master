package kr.co.pulmuone.v1.statics.outbound.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MissOutboundStaticsVo {

    @ApiModelProperty(value = "구분")
    private String dt;

    @ApiModelProperty(value = "출고 지시상품")
    private int deliveryGoodsCnt;

    @ApiModelProperty(value = "미출 발생상품")
    private int missGoodsCnt;

    @ApiModelProperty(value = "상품 미출률")
    private double missGoodsRate;

    @ApiModelProperty(value = "상품 미출률")
    private String missGoodsRateName;

    @ApiModelProperty(value = "미출 발생금액")
    private int missGoodsPrice;

    @ApiModelProperty(value = "미출 발생금액")
    private String missGoodsPriceName;

    @ApiModelProperty(value = "출고 지시주문")
    private int deliveryOrderCnt;

    @ApiModelProperty(value = "미출 발생주문")
    private int missOrderCnt;

    @ApiModelProperty(value = "주문 미출률%")
    private double missOrderRate;

    @ApiModelProperty(value = "주문 미출률%")
    private String missOrderRateName;

}
