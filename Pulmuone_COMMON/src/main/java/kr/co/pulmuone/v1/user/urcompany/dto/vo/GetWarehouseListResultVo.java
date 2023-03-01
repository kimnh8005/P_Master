package kr.co.pulmuone.v1.user.urcompany.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetWarehouseListResultVo")
public class GetWarehouseListResultVo {

	@ApiModelProperty(value = "출고처 그룹명")
	private String warehouseGroupName;

	@ApiModelProperty(value = "공급처명")
	private String supplierCompanyName;

	@ApiModelProperty(value = "출고가능일")
	private String warehouseDay;

	@ApiModelProperty(value = "출고처명")
	private String warehouseName;

	@ApiModelProperty(value = "공급처ID")
	private String urSupplierId;

	@ApiModelProperty(value = "공급처 거래처 ID")
	private String urSupplierWarehouseId;

	@ApiModelProperty(value = "근무일")
	private String workingDay;

	@ApiModelProperty(value = "출고처 ID")
	private String urWarehouseId;

	@ApiModelProperty(value = "no")
	private int no;


}
