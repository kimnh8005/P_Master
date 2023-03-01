package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetMenuRequestDto")
public class GetMenuRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "")
	private String stMenuId;

	public GetMenuRequestDto(String stMenuId) {
		this.stMenuId = stMenuId;
	}
}
