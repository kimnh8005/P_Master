package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetRecentlyShippingAddressListRequestDto")
public class GetRecentlyShippingAddressListRequestDto extends BaseRequestDto
{
	@ApiModelProperty(value = "유저유저아이디")
	private String urUserId;
	
	@ApiModelProperty(value = "조회대상건수")
	private int limitCount;
	
}
