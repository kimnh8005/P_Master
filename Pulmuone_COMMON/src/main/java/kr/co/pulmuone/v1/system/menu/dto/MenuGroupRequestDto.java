package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "MenuGroupRequestDto")
public class MenuGroupRequestDto extends BaseRequestDto
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
	private int inputGbDicMstId;

	@ApiModelProperty(value = "")
	private String inputUseYn;

	@ApiModelProperty(value = "")
	private String inputStProgramId;
}
