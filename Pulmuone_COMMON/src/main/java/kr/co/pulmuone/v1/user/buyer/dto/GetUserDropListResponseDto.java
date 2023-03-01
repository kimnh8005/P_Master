package kr.co.pulmuone.v1.user.buyer.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetUserDropListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUserDropListResponseDto")
public class GetUserDropListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	List<GetUserDropListResultVo> rows;

	@ApiModelProperty(value = "")
	private int total;

	@Builder
	public GetUserDropListResponseDto(int total, List<GetUserDropListResultVo> rows) {
		this.total = total;
		this.rows = rows;
	}

}
