package kr.co.pulmuone.v1.system.menu.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@Alias("GetMenuListResultVo")
@ApiModel(description = "GetMenuListResultVo")
public class GetMenuListResultVo {

	@ApiModelProperty(value = "")
	private String stMenuId;

	@ApiModelProperty(value = "")
	private String menuName;

	@ApiModelProperty(value = "")
	private String menuType;

	@ApiModelProperty(value = "")
	private String menuTypeName;

	@ApiModelProperty(value = "")
	private String url;

	@ApiModelProperty(value = "")
	private String parentMenuId;

	@ApiModelProperty(value = "")
	private String parentMenuName;

	@ApiModelProperty(value = "")
	private String sort;

	@ApiModelProperty(value = "")
	private String popYn;

	@ApiModelProperty(value = "")
	private String useYn;

	@ApiModelProperty(value = "")
	private String treeMenuName;

	@ApiModelProperty(value = "")
	private String level;

}
