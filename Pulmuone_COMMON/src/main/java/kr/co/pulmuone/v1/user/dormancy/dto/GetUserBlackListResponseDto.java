package kr.co.pulmuone.v1.user.dormancy.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetUserBlackListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUserBlackListResponseDto")
public class GetUserBlackListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	List<GetUserBlackListResultVo> rows;

	@ApiModelProperty(value = "")
	private int total;

	@Builder
	public GetUserBlackListResponseDto(int total, List<GetUserBlackListResultVo> rows){
		this.total = total;
		this.rows = rows;
	}

}
