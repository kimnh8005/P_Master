package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "공통 배송지 리스트 조회 RequestDto")
public class CommonGetShippingAddressListRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "회원 ID")
	private String urUserId;

	@ApiModelProperty(value = "주문상세 pk")
	private Long odOrderDetlId;

}
