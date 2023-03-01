package kr.co.pulmuone.bos.policy.payment;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentGatewayDto;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentPromotionRequestDto;
import kr.co.pulmuone.v1.policy.payment.service.PolicyPaymentGatewayBiz;
import lombok.RequiredArgsConstructor;

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
 *  1.0		20201019		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
public class PolicyPaymentGatewayController {

	@Autowired
	private PolicyPaymentGatewayBiz policyPaymentGatewayBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
     * PG사 기본설정 정보 목록 조회
     *
     * @param
     * @return PolicyPaymentGatewayDto
     */
	@GetMapping(value = "/admin/policy/payment/getPolicyPaymentGatewayList")
	@ApiOperation(value = "PG사 기본설정 정보 목록 조회", httpMethod = "GET")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyPaymentGatewayDto.class)
	})
	public ApiResult<?> getPolicyPaymentGatewayInfo(){
		return policyPaymentGatewayBiz.getPolicyPaymentGatewayList();
	}
	/**
     * PG사 이중화 비율 정보 목록 조회
     *
     * @param
     * @return PolicyPaymentGatewayDto
     */
	@PostMapping(value = "/admin/policy/payment/getPolicyPaymentGatewayRatioList")
	@ApiOperation(value = "PG사 이중화 비율 정보 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = PolicyPaymentGatewayDto.class)
	})
	public ApiResult<?> getPolicyPaymentGatewayList() {
		return policyPaymentGatewayBiz.getPolicyPaymentGatewayRatioList();
	}

	/**
     * PG사 기본설정 정보 수정
     *
     * @param PolicyPaymentGatewayDto
     * @return int
     */
	@RequestMapping(value = "/admin/policy/payment/putPolicyPaymentGateway")
	@ApiOperation(value = "PG사 기본설정 정보 수정", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putPolicyPaymentGateway(@RequestBody PolicyPaymentGatewayDto dto) {
		return policyPaymentGatewayBiz.putPolicyPaymentGateway(dto);
	}

	/**
     * PG 결제수단 관리 목록 조회
     *
     * @param psPayCd
     * @return PolicyPaymentGatewayDto
     */
	@PostMapping(value = "/admin/policy/payment/getPolicyPaymentGatewayMethodList")
	@ApiOperation(value = "PG사 이중화 비율 정보 목록 조회", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "psPayCd", value = "PG 결제방법 공통코드", required = true, dataType = "String")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = PolicyPaymentGatewayDto.class)
	})
	public ApiResult<?> getPolicyPaymentGatewayMethodList(@RequestParam(value = "psPayCd", required = true) String psPayCd) {
		return policyPaymentGatewayBiz.getPolicyPaymentGatewayMethodList(psPayCd);
	}

	/**
     * PG 결제수단 정보 수정
     *
     * @param PolicyPaymentGatewayDto
     * @return int
     */
	@RequestMapping(value = "/admin/policy/payment/putPolicyPaymentGatewayMethod")
	@ApiOperation(value = "PG사 기본설정 정보 수정", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putPolicyPaymentGatewayMethod(@RequestBody PolicyPaymentGatewayDto dto) {
		return policyPaymentGatewayBiz.putPolicyPaymentGatewayMethod(dto);
	}

	/**
	 * PG 결제은행 List
	 */
	@GetMapping(value = "/admin/comn/payment/getPgBankCodeList")
	@ApiOperation(value = "PG 가상계좌 결제은행 목록 조회", notes = "PG 결제은행 목록 DropDown")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyPaymentGatewayDto.class)
	})
	public ApiResult<?> getPgBankCodeList(PolicyPaymentPromotionRequestDto dto) {
		return policyPaymentGatewayBiz.getPgBankCodeList(dto);
	}

}

