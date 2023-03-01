package kr.co.pulmuone.v1.base.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "공급처 Vo")
public class SupplierVo {

    @ApiModelProperty(value = "공급처ID")
    private Long supplierId;

	@ApiModelProperty(value = "공급처명")
	private String supplierName;

	@ApiModelProperty(value = "회사구분코드")
	private String companyType;

	@ApiModelProperty(value = "공급처 코드")
	private String supplierCode;

	@ApiModelProperty(value = "ERP 연동여부")
	private String erpSendYn;


}
