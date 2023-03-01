package kr.co.pulmuone.v1.base.dto.vo;

import kr.co.pulmuone.v1.comm.base.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetMenuNameParamVo")
public class GetMenuNameParamVo extends BaseDto{

	@ApiModelProperty(value = "")
	private String stMenuId;

}
