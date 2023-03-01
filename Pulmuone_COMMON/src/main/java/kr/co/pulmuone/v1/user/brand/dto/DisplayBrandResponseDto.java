package kr.co.pulmuone.v1.user.brand.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.brand.dto.vo.DisplayBrandResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "DisplayBrandResponseDto")
public class DisplayBrandResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "")
	private	DisplayBrandResultVo rows;

}
