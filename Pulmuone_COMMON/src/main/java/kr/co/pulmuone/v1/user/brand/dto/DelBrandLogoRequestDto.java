package kr.co.pulmuone.v1.user.brand.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "DelBrandLogoRequestDto")
public class DelBrandLogoRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "")
	private String psAttcId;

}
