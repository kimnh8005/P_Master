package kr.co.pulmuone.v1.user.buyer.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetShippingAddressResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송지 단건 조회 ResponseDto")
public class CommonGetShippingAddressResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	CommonGetShippingAddressResultVo rows;

}
