package kr.co.pulmuone.v1.base.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "MenuOperLogRequestDto")
public class MenuOperLogRequestDto extends BaseRequestPageDto{


	@ApiModelProperty(value = "ST_MENU PK")
	String systemMenuId;

	@ApiModelProperty(value = "ST_MENU_URL PK")
	String systemMenuUrlId;

	@ApiModelProperty(value = "메뉴명")
	String menuName;

	@ApiModelProperty(value = "URL명(시스템명)")
	String urlName;

	@ApiModelProperty(value = "IP")
	String ip;

	@ApiModelProperty(value = "검색구분")
	String searchType;

	@ApiModelProperty(value = "검색값")
	String searchValue;

	@ApiModelProperty(value = "관리자명")
	String loginName;

	@ApiModelProperty(value = "시작일")
	String startCreateDate;

	@ApiModelProperty(value = "종료일")
	String endCreateDate;


}
