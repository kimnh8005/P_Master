package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.vo.GetMenuAssignUrlListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
@ApiModel(description = "GetMenuAssignUrlListResponseDto")
public class GetMenuAssignUrlListResponseDto extends BaseResponseDto{


	@ApiModelProperty(value = "리스트")
	private	List<GetMenuAssignUrlListResultVo> rows;

	@ApiModelProperty(value = "총 갯수")
	private int total;
}
