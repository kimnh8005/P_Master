package kr.co.pulmuone.v1.comm.api.dto.basic;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ErpStockReCalQtyResponseDto {

    @ApiModelProperty(value = "품목별 출고처ID")
    private long ilItemWarehouseId;

    @ApiModelProperty(value = "품목연동재고ID")
    private long ilItemErpStockId;

    @ApiModelProperty(value = "재고수량")
    private int stockQty;

    @ApiModelProperty(value = "기준일")
    private String baseDt;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "품목 유통기간")
    private int distributionPeriod;
}