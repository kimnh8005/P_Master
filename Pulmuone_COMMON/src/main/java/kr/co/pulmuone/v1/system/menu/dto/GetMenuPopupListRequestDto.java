package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetMenuListRequestDto")
public class GetMenuPopupListRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "")
	private String menuName;

	@ApiModelProperty(value = "")
	private String stMenuGroupId;

	@ApiModelProperty(value = "")
	private String menuType;
}
