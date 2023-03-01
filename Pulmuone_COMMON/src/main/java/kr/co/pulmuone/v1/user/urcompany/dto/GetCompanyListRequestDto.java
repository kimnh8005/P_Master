package kr.co.pulmuone.v1.user.urcompany.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCompanyListRequestDto")
public class GetCompanyListRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "거래처 항목구분")
	private String companyTypeValue;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "타입코드")
	private String tpCode;

	@ApiModelProperty(value = "회사타입")
	private String companyType;

	@ApiModelProperty(value = "회사타입")
	private String warehouseGroupCode;


	@ApiModelProperty(value = "공급업체ID")
	private String supplierCompany;


}
