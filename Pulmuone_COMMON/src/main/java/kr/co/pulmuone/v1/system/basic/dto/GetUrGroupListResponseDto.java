package kr.co.pulmuone.v1.system.basic.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetUrGroupResultResultVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUrGroupListResponseDto")
public class GetUrGroupListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "리스트")
	private	 List<GetUrGroupResultResultVo> rows;

	@ApiModelProperty(value = "총 개수")
	private int total;

}
