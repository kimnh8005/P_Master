package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "GetMenuGroupListRequestDto")
public class GetMenuGroupListRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "")
	private String menuGroupName;

	@ApiModelProperty(value = "")
	private String id;

	@ApiModelProperty(value = "")
	private String useYn;

	@Builder
	public GetMenuGroupListRequestDto(String menuGroupName, String id, String useYn) {
		this.menuGroupName = menuGroupName;
		this.id = id;
		this.useYn = useYn;
	}
}
