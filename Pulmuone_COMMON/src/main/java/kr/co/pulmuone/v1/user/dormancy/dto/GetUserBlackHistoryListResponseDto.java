package kr.co.pulmuone.v1.user.dormancy.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetUserBlackHistoryListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUserBlackHistoryListResponseDto")
public class GetUserBlackHistoryListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	List<GetUserBlackHistoryListResultVo> rows;

	@Builder
	public GetUserBlackHistoryListResponseDto(List<GetUserBlackHistoryListResultVo> rows) {
		this.rows = rows;
	}
//	@ApiModelProperty(value = "")
//	private int total;
}
