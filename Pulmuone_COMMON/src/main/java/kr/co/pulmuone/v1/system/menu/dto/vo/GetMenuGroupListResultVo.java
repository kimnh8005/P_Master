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
@Alias("GetMenuGroupListResultVo")
@ApiModel(description = "GetMenuGroupListResultVo")
public class GetMenuGroupListResultVo {

	@ApiModelProperty(value = "")
	private String stMenuGroupId;

	@ApiModelProperty(value = "")
	private String menuGroupName;

	@ApiModelProperty(value = "")
	private String programName;

	@ApiModelProperty(value = "")
	private String sort;

	@ApiModelProperty(value = "")
	private String useYn;

}
