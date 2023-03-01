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
@ApiModel(description = "환불계좌 조회 RequestDto")
public class CommonGetRefundBankRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "회원 ID")
	private String urUserId;

}
