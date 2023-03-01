package kr.co.pulmuone.v1.system.auth.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.vo.GetAuthUserResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetAuthUserOutListResponseDto")
public class GetAuthUserOutListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "리스트")
	private List<GetAuthUserResultVo> rows;

	@ApiModelProperty(value = "총개수")
	private int total;
}