package kr.co.pulmuone.v1.od.order.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.od.order.dto.vo.GetOutmallOrderDetailListResultVo;
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
public class GetOutmallOrderDetailListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	List<GetOutmallOrderDetailListResultVo> rows;

	@ApiModelProperty(value = "")
	private int total;

	@Builder
	public GetOutmallOrderDetailListResponseDto(int total, List<GetOutmallOrderDetailListResultVo> rows) {
		this.total = total;
		this.rows = rows;
	}
}
