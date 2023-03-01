package kr.co.pulmuone.v1.user.buyer.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBuyerResponseDto")
public class GetBuyerResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	GetBuyerResultVo rows;

}
