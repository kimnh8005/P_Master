package kr.co.pulmuone.v1.user.brand.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.vo.GetBrandResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBrandResponseDto")
public class GetBrandResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "")
	private	GetBrandResultVo rows;

}
