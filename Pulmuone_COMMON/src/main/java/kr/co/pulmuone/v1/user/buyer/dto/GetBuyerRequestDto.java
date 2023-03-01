package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBuyerRequestDto")
public class GetBuyerRequestDto extends BaseRequestPageDto
{
	
	@ApiModelProperty(value = "")
	private String urUserId;
	
	@ApiModelProperty(value = "")
	private String personalInformationAccessYn;
	
}
