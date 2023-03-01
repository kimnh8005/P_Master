package kr.co.pulmuone.v1.system.basic.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetLangListResultVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetLangListResponseDto")
public class GetLangListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "리스트")
	private	 List<GetLangListResultVo> rows;

	@ApiModelProperty(value = "총 개수")
	private int total;

}