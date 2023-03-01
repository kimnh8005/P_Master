package kr.co.pulmuone.bos.policy.bbs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.bos.policy.bbs.service.PolicyBbsBannedWordBosService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsBannedWordDto;
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
 *  1.0		20200916		박승현              최초작성
 *
 * =======================================================================
 * </PRE>
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/policy/bbs")
public class PolicyBbsBannedWordController {


	@Autowired
	private PolicyBbsBannedWordBosService policyBbsBannedWordBosService;
	/**
	 * 게시판금칙어 설정 조회
	 * @param PolicyBbsBannedWordDto
	 * @return PolicyBbsBannedWordDto
	 * @throws Exception
	 */
	@PostMapping(value = "/getPolicyBbsBannedWord")
	@ApiOperation(value = "게시판금칙어 설정 조회", httpMethod = "POST")
	public ApiResult<?> getPolicyBbsBannedWord(PolicyBbsBannedWordDto dto) {
		return policyBbsBannedWordBosService.getPolicyBbsBannedWord(dto);
	}

	/**
	 * 게시판금칙어 설정 저장
	 * @param PolicyBbsBannedWordDto
	 * @return BaseResponseDto
	 * @throws Exception
	 */
	@RequestMapping(value = "/putPolicyBbsBannedWord")
	@ApiOperation(value = "게시판금칙어 설정 저장", httpMethod = "GET")
	public ApiResult<?> putPolicyBbsBannedWord(PolicyBbsBannedWordDto dto) {
		return policyBbsBannedWordBosService.putPolicyBbsBannedWord(dto);
	}
}

