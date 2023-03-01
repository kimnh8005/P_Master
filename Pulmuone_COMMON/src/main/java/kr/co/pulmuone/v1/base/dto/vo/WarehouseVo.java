package kr.co.pulmuone.v1.base.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "출고처 Vo")
public class WarehouseVo {

	@ApiModelProperty(value = "출고처 ID")
	private Long warehouseId;

    @ApiModelProperty(value = "출고처명")
    private String warehouseName;

    @ApiModelProperty(value = "공급처 거래처 ID")
    private Long supplierWarehouseId;

    @ApiModelProperty(value = "공급처ID")
    private Long supplierId;

	@ApiModelProperty(value = "공급처명")
	private String supplierName;

	@ApiModelProperty(value = "출고처 그룹코드")
	private String warehouseGrpCd;

	@ApiModelProperty(value = "출고처 그룹명")
	private String warehouseGroupName;


}
