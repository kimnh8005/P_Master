package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.vo.GetMenuGroupResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetMenuGroupResponseDto")
public class GetMenuGroupResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "")
	private GetMenuGroupResultVo rows;

}
