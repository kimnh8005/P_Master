package kr.co.pulmuone.v1.system.basic.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetPSKeyTypeListResultVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetPSKeyTypeListResponseDto")
public class GetPSKeyTypeListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "리스트")
	private List<GetPSKeyTypeListResultVo> rows;

	@ApiModelProperty(value = "총 개수")
	private int total;

}
