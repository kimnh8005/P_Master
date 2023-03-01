package kr.co.pulmuone.v1.base.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetMenuGroupListResultVo;
import kr.co.pulmuone.v1.base.dto.vo.GetMenuListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetMenuListResponseDto")
public class GetMenuListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	List<GetMenuGroupListResultVo> rows;

	@ApiModelProperty(value = "")
	private	List<GetMenuListResultVo> menuList;

}
