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
public class GetMenuListRequestDto extends BaseRequestPageDto{


	@ApiModelProperty(value = "메뉴 그룹 PK")
	private String stMenuGroupId;

	@ApiModelProperty(value = "사용 여부")
	private String useYn;

	@ApiModelProperty(value = "팝업 여부")
	private String popYn;

	@ApiModelProperty(value = "메뉴 PK")
	private String setMenuId;

	@ApiModelProperty(value = "메뉴 명")
	private String menuName;

	@ApiModelProperty(value = "메뉴 타입")
	private String menuType;

}
