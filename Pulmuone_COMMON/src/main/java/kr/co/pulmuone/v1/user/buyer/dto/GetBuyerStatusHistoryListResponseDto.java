package kr.co.pulmuone.v1.user.buyer.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerStatusHistoryListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBuyerStatusHistoryListResponseDto")
public class GetBuyerStatusHistoryListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	List<GetBuyerStatusHistoryListResultVo> rows;

	@Builder
	public GetBuyerStatusHistoryListResponseDto(List<GetBuyerStatusHistoryListResultVo> rows){
		this.rows = rows;
	}

}
