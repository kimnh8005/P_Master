package kr.co.pulmuone.v1.system.log.dto;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBatchLogListResponseDto")
public class GetBatchLogListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "배치 로그 리스트")
	private List<GetBatchLogListResultVo> rows;

	@ApiModelProperty(value = "총 개수")
	private int total;

}
