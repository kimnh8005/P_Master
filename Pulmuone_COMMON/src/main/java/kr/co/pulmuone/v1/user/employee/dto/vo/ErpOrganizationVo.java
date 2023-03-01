package kr.co.pulmuone.v1.user.employee.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ERP 조직정보")
public class ErpOrganizationVo {

	@ApiModelProperty(value = "조직코드")
	private String erpOrganizationCode;

	@ApiModelProperty(value = "조직명")
	private String erpOrganizationName;

	@ApiModelProperty(value = "법인코드")
	private String erpRegalCode;

	@ApiModelProperty(value = "법인명")
	private String erpRegalName;

	@ApiModelProperty(value = "사용유무")
	private String useYn;

	@ApiModelProperty(value = "등록자 ID")
	private Long createId;
}
