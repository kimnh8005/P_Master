package kr.co.pulmuone.v1.base.dto;


import kr.co.pulmuone.v1.comm.base.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " ConvertNavigationParamDto")
public class ConvertNavigationParamDto extends BaseDto {

	@ApiModelProperty(value = "", required = false)
	String stMenuId;

	@ApiModelProperty(value = "", required = false)
	String title;

	@ApiModelProperty(value = "", required = false)
	String pageNavi;

}
