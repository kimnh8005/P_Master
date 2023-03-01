package kr.co.pulmuone.bos.policy.payment;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.payment.dto.PolicyPaymentCardBenefitDto;
import kr.co.pulmuone.v1.policy.payment.dto.vo.PolicyPaymentCardBenefitVo;
import kr.co.pulmuone.v1.policy.payment.service.PolicyPaymentCardBenefitBiz;
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
 *  1.0		20201007		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
public class PolicyPaymentCardBenefitController {


	@Autowired
	private PolicyPaymentCardBenefitBiz policyPaymentCardBenefitBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	/**
	 * 신용카드혜택안내 조회
	 * @param psCardBenefitId
	 * @return ApiResult
	 * @throws Exception
	 */
	@GetMapping(value = "/admin/policy/payment/getPolicyPaymentCardBenefitInfo")
	@ApiOperation(value = "신용카드혜택안내 조회", httpMethod = "GET")
    @ApiImplicitParams({ @ApiImplicitParam(name = "psCardBenefitId", value = "신용카드혜택안내 PK", required = true, dataType = "Long")})
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PolicyPaymentCardBenefitVo.class)
	})
	public ApiResult<?> getPolicyPaymentCardBenefitInfo(@RequestParam(value = "psCardBenefitId", required = true) Long psCardBenefitId){
		return policyPaymentCardBenefitBiz.getPolicyPaymentCardBenefitInfo(psCardBenefitId);
	}
	/**
	 * 신용카드혜택안내 목록 조회
	 * @param PolicyPaymentCardBenefitDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@PostMapping(value = "/admin/policy/payment/getPolicyPaymentCardBenefitList")
	@ApiOperation(value = "신용카드혜택안내 목록 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data List<>", response = PolicyPaymentCardBenefitDto.class)
	})
	public ApiResult<?> getPolicyPaymentCardBenefitList(PolicyPaymentCardBenefitDto dto) throws Exception {
		return policyPaymentCardBenefitBiz.getPolicyPaymentCardBenefitList((PolicyPaymentCardBenefitDto)BindUtil.convertRequestToObject(request, PolicyPaymentCardBenefitDto.class));
	}

	/**
	 * 신용카드혜택안내 신규 등록
	 * @param PolicyPaymentCardBenefitDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/payment/addPolicyPaymentCardBenefit")
	@ApiOperation(value = "신용카드혜택안내 신규 등록", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> addPolicyPaymentCardBenefit(PolicyPaymentCardBenefitDto dto) {
		return policyPaymentCardBenefitBiz.addPolicyPaymentCardBenefit(dto);
	}

	/**
	 * 신용카드혜택안내 수정
	 * @param PolicyPaymentCardBenefitDto
	 * @return ApiResult
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/payment/putPolicyPaymentCardBenefit")
	@ApiOperation(value = "신용카드혜택안내 수정", httpMethod = "POST")
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> putPolicyPaymentCardBenefit(PolicyPaymentCardBenefitDto dto) {
		return policyPaymentCardBenefitBiz.putPolicyPaymentCardBenefit(dto);
	}
	/**
	 * 신용카드혜택안내 삭제
	 * @param psCardBenefitId
	 * @return ApiResult
	 * @throws Exception
	 */
	@RequestMapping(value = "/admin/policy/payment/delPolicyPaymentCardBenefit")
	@ApiOperation(value = "신용카드혜택안내 삭제", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "psCardBenefitId", value = "신용카드혜택안내 PK", required = true, dataType = "Long")})
	@ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data : Integer.class"),
    })
	public ApiResult<?> delPolicyPaymentCardBenefit(@RequestParam(value = "psCardBenefitId", required = true) Long psCardBenefitId) {
		return policyPaymentCardBenefitBiz.delPolicyPaymentCardBenefit(psCardBenefitId);
	}
}

