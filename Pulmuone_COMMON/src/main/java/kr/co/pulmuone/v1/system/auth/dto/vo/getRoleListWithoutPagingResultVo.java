package kr.co.pulmuone.v1.system.auth.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "getRoleListWithoutPagingResultVo")
public class getRoleListWithoutPagingResultVo {

	@ApiModelProperty(value = "")
	private String roleName;

	@ApiModelProperty(value = "")
	private String stRoleTypeId;

}
