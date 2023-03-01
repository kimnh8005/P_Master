package kr.co.pulmuone.v1.user.buyer.dto.vo;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingAccountNumber;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommonGetRefundBankResultVo {

	//환불계좌정보 PK
	private String urRefundBankId;

	//은행코드
	private String bankCode;

	//은행명
	private String bankName;

	//예금주
	private String holderName;

	//계좌번호
	@UserMaskingAccountNumber
	private String accountNumber;

}
