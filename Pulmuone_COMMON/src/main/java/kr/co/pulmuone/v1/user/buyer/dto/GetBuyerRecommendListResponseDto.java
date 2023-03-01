package kr.co.pulmuone.v1.user.buyer.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerRecommendListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBuyerRecommendListResponseDto")
public class GetBuyerRecommendListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	List<GetBuyerRecommendListResultVo> rows;
}
