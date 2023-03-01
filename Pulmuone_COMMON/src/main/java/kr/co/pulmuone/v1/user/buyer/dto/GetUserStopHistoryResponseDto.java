package kr.co.pulmuone.v1.user.buyer.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerStopLogResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUserStopHistoryResponseDto")
public class GetUserStopHistoryResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	GetBuyerStopLogResultVo rows;

}
