package kr.co.pulmuone.v1.statics.data.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DataDownloadStaticsVo {

    @ApiModelProperty(value = "OD_ORDER_ID")
    private String odOrderId;

    @ApiModelProperty(value = "SALE_PRICE")
    private int salePrice;

    @ApiModelProperty(value = "CREATE_DT")
    private String createDt;

}
