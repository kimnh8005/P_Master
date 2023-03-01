package kr.co.pulmuone.v1.base.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetMenuNameResultVo")
public class GetMenuNameResultVo {

	@ApiModelProperty(value = "")
	private String menuId;

	@ApiModelProperty(value = "")
	private String parentMenuId;

	@ApiModelProperty(value = "")
	private String menuName;

	@ApiModelProperty(value = "매뉴 도움말 설정 PK")
	private Long stHelpId;
}
