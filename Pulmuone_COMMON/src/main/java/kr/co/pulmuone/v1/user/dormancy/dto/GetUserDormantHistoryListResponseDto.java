package kr.co.pulmuone.v1.user.dormancy.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetUserDormantHistoryListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUserDormantHistoryListResponseDto")
public class GetUserDormantHistoryListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	List<GetUserDormantHistoryListResultVo> rows;

	@ApiModelProperty(value = "")
	private int total;

	@Builder
	public GetUserDormantHistoryListResponseDto(int total, List<GetUserDormantHistoryListResultVo> rows) {
		this.total = total;
		this.rows = rows;
	}
}
