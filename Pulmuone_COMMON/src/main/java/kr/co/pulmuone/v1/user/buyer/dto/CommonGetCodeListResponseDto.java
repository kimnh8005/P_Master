package kr.co.pulmuone.v1.user.buyer.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CommonGetShippingAddressInfoResponseDto")
public class CommonGetCodeListResponseDto extends BaseResponseDto
{

	@ApiModelProperty(value = "CommonGetShippingAddressInfoResultVo")
	private List<CodeInfoVo> rows;
}
