package kr.co.pulmuone.v1.user.buyer.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetRefundBankResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "환불계좌 조회 ResponseDto")
public class GetRefundBankResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "환불계좌 조회 결과 vo")
	private	CommonGetRefundBankResultVo rows;

}