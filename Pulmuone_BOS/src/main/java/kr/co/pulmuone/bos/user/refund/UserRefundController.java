package kr.co.pulmuone.bos.user.refund;

import io.swagger.annotations.*;
import kr.co.pulmuone.bos.user.refund.service.UserRefundBosService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.buyer.dto.CommonGetRefundBankRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.CommonSaveRefundBankRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetRefundBankResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200708		   	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class UserRefundController {

	@Autowired
	private UserRefundBosService userRefundBosService;


	/**
	 * 환불계좌 조회
	 * @param	CommonGetRefundBankRequestDto
	 * @return	GetRefundBankResponseDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/userRefund/getRefundBank")
	@ApiOperation(value = "환불계좌 조회", httpMethod = "POST", notes = "환불계좌 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetRefundBankResponseDto.class),
	})
	public ApiResult<?> getRefundBank(CommonGetRefundBankRequestDto dto) throws Exception {

		return ApiResult.success(userRefundBosService.getRefundBank(dto));
	}


	/**
	 * 환불계좌 수정
	 * @param	CommonSaveRefundBankRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/userRefund/putRefundBank")
	@ApiOperation(value = "환불계좌 수정", httpMethod = "POST", notes = "환불계좌 수정")
	public ApiResult<?> putRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception {

		return ApiResult.success(userRefundBosService.putRefundBank(dto));
	}


	/**
	 * 환불계좌 추가
	 * @param	CommonSaveRefundBankRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/ur/userRefund/addRefundBank")
	@ApiOperation(value = "환불계좌 추가", httpMethod = "POST", notes = "환불계좌 추가")
	public ApiResult<?> addRefundBank(CommonSaveRefundBankRequestDto dto) throws Exception {

		return ApiResult.success(userRefundBosService.addRefundBank(dto));
	}


	/**
	 * 계좌 유효 인증 체크
	 * @param bankCode String
	 * @param accountNumber String
	 * @param holderName String
	 * @return ApiResult<?>
	 * @throws Exception Exception
	 */
	@PostMapping(value = "/admin/ur/userRefund/isValidationBankAccountNumber")
	@ApiOperation(value = "유효계좌 인증", httpMethod = "POST", notes = "유효계좌 인증")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "bankCode", value = "은행코드", required = true, dataType = "String"),
			@ApiImplicitParam(name = "accountNumber", value = "계좌번호", required = true, dataType = "String"),
			@ApiImplicitParam(name = "holderName", value = "계좌주", required = true, dataType = "String")
	})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = boolean.class)
	})
	@ResponseBody
	public ApiResult<?> isValidationBankAccountNumber(String bankCode, String accountNumber, String holderName) throws Exception {
		return userRefundBosService.isValidationBankAccountNumber(bankCode, accountNumber, holderName);
	}

}



