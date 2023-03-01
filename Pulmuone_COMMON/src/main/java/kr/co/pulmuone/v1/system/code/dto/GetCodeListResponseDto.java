package kr.co.pulmuone.v1.system.code.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeListResultVo;
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

	@ApiModelProperty(value = "코드 결과 리스트")
	private	List<GetCodeListResultVo> rows;

}
