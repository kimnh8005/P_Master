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
@ApiModel(description = "환불계좌 추가&수정 RequestDto")
public class CommonSaveRefundBankRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "회원 ID")
	private String urUserId;

	@ApiModelProperty(value = "계좌 CODE")
	private String bankCode;

	@ApiModelProperty(value = "예금주명")
	private String holderName;

	@ApiModelProperty(value = "계좌번호")
	private String accountNumber;

	@ApiModelProperty(value = "환불계좌 PK")
	private String urRefundBankId;

	@ApiModelProperty(value = "회원상태변경값 배열")
	private String changeLogArray;
}
