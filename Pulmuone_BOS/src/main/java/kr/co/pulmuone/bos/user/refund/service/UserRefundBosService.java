package kr.co.pulmuone.bos.user.refund.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.GetRefundBankResponseDto;

public interface UserRefundBosService {
	GetRefundBankResponseDto getRefundBank(CommonGetRefundBankRequestDto dto) throws Exception;

	int putRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception;

	int addRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception;

	ApiResult<?> isValidationBankAccountNumber(String bankCode, String accountNumber, String holderName) throws Exception;
}
