package kr.co.pulmuone.v1.policy.fee.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "OmLogisticsFeeListDto")
public class OmLogisticsFeeListDto {

    @ApiModelProperty(value = "물류수수료 pk")
    private long omLogisticsFeeId;

    @ApiModelProperty(value = "출고처 pk")
    private long urWarehouseId;

    @ApiModelProperty(value = "출고처명")
    private String warehouseNm;

    @ApiModelProperty(value = "공급처 PK")
    private long urSupplierId;

    @ApiModelProperty(value = "공급처명")
    private String supplierNm;

    @ApiModelProperty(value = "정산방식")
    private String calcType;

    @ApiModelProperty(value = "수수료")
    private int fee;

    @ApiModelProperty(value = "시작일자")
    private String startDt;

    @ApiModelProperty(value = "등록일")
    private String createDt;

}
