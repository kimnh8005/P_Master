package kr.co.pulmuone.v1.system.code.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeMasterNameListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCodeMasterNameListResponseDto")
public class GetCodeMasterNameListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "코드 마스터명 리스트")
	private	List<GetCodeMasterNameListResultVo> rows;

	@ApiModelProperty(value = "총 개수")
	private int total;
}
