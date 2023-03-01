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
@Alias("GetMenuGroupNameListResultVo")
@ApiModel(description = "GetMenuGroupNameListResultVo")
public class GetMenuGroupNameListResultVo {

	@ApiModelProperty(value = "")
	private String stMenuGroupId;

	@ApiModelProperty(value = "")
	private String menuGroupName;

	@ApiModelProperty(value = "")
	private String sort;

	@ApiModelProperty(value = "")
	private String useYn;

	@ApiModelProperty(value = "권한있는 메뉴 갯수")
	private String authMenuCount;
}
