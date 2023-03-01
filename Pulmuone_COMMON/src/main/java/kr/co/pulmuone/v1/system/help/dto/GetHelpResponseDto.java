package kr.co.pulmuone.v1.system.help.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.help.dto.vo.GetHelpResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetHelpResponseDto")
public class GetHelpResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "도움말 상세 조회 데이터")
	private	GetHelpResultVo rows;

}
