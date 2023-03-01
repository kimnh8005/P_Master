package kr.co.pulmuone.v1.user.dormancy.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetUserDormantListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUserDormantListResponseDto")
public class GetUserDormantListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	List<GetUserDormantListResultVo> rows;

	@ApiModelProperty(value = "")
	private int total;

	@Builder
	public GetUserDormantListResponseDto(int total, List<GetUserDormantListResultVo> rows) {
		this.total = total;
		this.rows = rows;
	}
}
