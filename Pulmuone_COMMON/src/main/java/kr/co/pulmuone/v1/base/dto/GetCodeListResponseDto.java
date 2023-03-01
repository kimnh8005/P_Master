package kr.co.pulmuone.v1.base.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCodeListResponseDto")
public class GetCodeListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	List<GetCodeListResultVo> rows;

}
