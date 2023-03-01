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
@Alias("GetMenuGroupResultVo")
@ApiModel(description = "GetMenuGroupResultVo")
public class GetMenuGroupResultVo
{

	@ApiModelProperty(value = "")
	private String inputStMenuGroupId;

	@ApiModelProperty(value = "")
	private String inputMenuGroupName;

	@ApiModelProperty(value = "")
	private String inputSort;

	@ApiModelProperty(value = "")
	private String inputProgramName;

	@ApiModelProperty(value = "")
	private String inputGbDicMstId;

	@ApiModelProperty(value = "")
	private String inputStProgramId;

	@ApiModelProperty(value = "")
	private String inputUseYn;



}
