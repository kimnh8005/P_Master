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
@ApiModel(description = "배송지 단건 조회RequestDto")
public class CommonGetShippingAddressRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "배송지정보 PK")
	private String urShippingAddrId;

}
