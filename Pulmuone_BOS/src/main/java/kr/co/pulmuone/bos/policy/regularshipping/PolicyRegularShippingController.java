package kr.co.pulmuone.bos.policy.regularshipping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.bos.policy.regularshipping.service.PolicyRegularShippingBosService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.regularshipping.dto.PolicyRegularShippingDto;
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
 *  1.0		20200902		박승현              최초작성
 *  1.1		20200914		박승현		코드 신규 개발 가이드 적용
 *
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/policy/regularshipping")
public class PolicyRegularShippingController {


	@Autowired
	private PolicyRegularShippingBosService policyRegularShippingBosService;
	/**
	 * 정기배송 기본설정
	 * @param PolicyRegularShippingDto
	 * @return PolicyRegularShippingDto
	 * @throws Exception
	 */
	@PostMapping(value = "/getPolicyRegularShipping")
	@ApiOperation(value = "정기배송 기본설정", httpMethod = "POST")
	public ApiResult<?> getPolicyRegularShipping(PolicyRegularShippingDto dto) {
		return policyRegularShippingBosService.getPolicyRegularShipping(dto);
	}

	/**
	 * 정기배송 기본설정 저장
	 * @param PolicyRegularShippingDto
	 * @return BaseResponseDto
	 * @throws Exception
	 */
	@RequestMapping(value = "/putPolicyRegularShipping")
	@ApiOperation(value = "정기배송 기본설정 저장", httpMethod = "GET")
	public ApiResult<?> putPolicyRegularShipping(PolicyRegularShippingDto dto) {
		return policyRegularShippingBosService.putPolicyRegularShipping(dto);
	}
}

