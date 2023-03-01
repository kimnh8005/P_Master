package kr.co.pulmuone.v1.user.employee.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "조직정보 Request")
public class ErpOrganizationRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "법인코드")
	private String erpRegalCode;
}
