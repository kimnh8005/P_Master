package kr.co.pulmuone.v1.base.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " ConvertNavigationResultDto")
public class ConvertNavigationResultDto {

	@ApiModelProperty(value = "", required = false)
	String title;

	@ApiModelProperty(value = "", required = false)
	String pageNavi;

}
