package kr.co.pulmuone.v1.user.employee.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.employee.dto.vo.ErpOrganizationVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ERP 조직정보 Response")
public class ErpOrganizationResponseDto{

	@ApiModelProperty(value = "ERP 조직정보 리스트")
	private	List<ErpOrganizationVo> rows;
}
