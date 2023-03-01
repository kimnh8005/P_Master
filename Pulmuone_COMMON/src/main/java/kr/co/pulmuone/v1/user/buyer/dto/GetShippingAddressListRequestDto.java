package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetShippingAddressListRequestDto")
public class GetShippingAddressListRequestDto
{
	@ApiModelProperty(value = "암호화키")
	private String databaseEncryptionKey;
	
	@ApiModelProperty(value = "유저유저아이디")
	private String urUserId;
	
	@ApiModelProperty(value = "조회대상건수")
	private int limitCount;
	
}
