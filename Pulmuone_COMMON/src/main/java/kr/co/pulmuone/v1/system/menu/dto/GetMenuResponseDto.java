package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.vo.GetMenuResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetMenuResponseDto")
public class GetMenuResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private GetMenuResultVo rows;
}
