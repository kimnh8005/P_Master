package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.vo.GetParentMenuListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
@ApiModel(description = "GetParentMenuListResponseDto")
public class GetParentMenuListResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "리스트")
	private	List<GetParentMenuListResultVo> rows;

	@ApiModelProperty(value = "6")
	private int total;

}
