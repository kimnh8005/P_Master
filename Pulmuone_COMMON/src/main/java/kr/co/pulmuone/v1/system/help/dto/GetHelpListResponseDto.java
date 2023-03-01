package kr.co.pulmuone.v1.system.help.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.help.dto.vo.GetHelpListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetHelpListResponseDto")
public class GetHelpListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "도움말 조회 리스트")
	private	List<GetHelpListResultVo> rows;

	@ApiModelProperty(value = "도움말 조회 총 Count")
	private long total;
}
