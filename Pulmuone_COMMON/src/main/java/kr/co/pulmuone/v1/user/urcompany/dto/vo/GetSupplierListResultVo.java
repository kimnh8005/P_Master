package kr.co.pulmuone.v1.user.urcompany.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetSupplierListResultVo")
public class GetSupplierListResultVo {

	@ApiModelProperty(value = "거래처 ID")
	private String urSupplierId;

	@ApiModelProperty(value = "거래처 명")
	private String supplierName;


}
