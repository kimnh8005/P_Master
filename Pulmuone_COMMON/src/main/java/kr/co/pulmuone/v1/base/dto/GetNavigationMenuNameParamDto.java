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
@ApiModel(description = " GetNavigationMenuNameParamDto")
public class GetNavigationMenuNameParamDto extends BaseDto {

	@ApiModelProperty(value = "", required = false)
	String stMenuId;

}
