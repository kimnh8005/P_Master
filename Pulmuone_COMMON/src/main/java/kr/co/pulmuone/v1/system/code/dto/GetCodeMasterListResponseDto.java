package kr.co.pulmuone.v1.system.code.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeMasterListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCodeMasterListResponseDto")
public class GetCodeMasterListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "코드 마스터 리스트")
	private	List<GetCodeMasterListResultVo> rows;

	@ApiModelProperty(value = "총개수")
	private int total;
}
