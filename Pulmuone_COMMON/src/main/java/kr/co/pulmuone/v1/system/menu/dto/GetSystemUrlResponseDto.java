package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.vo.GetSystemUrlResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetSystemUrlResponseDto")
public class GetSystemUrlResponseDto extends BaseResponseDto{

	private GetSystemUrlResultVo rows;
}
