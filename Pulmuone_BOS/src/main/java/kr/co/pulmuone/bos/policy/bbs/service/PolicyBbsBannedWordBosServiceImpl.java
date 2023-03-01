package kr.co.pulmuone.bos.policy.bbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsBannedWordDto;
import kr.co.pulmuone.v1.policy.bbs.service.PolicyBbsBannedWordBiz;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0		20200916	박승현		최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class PolicyBbsBannedWordBosServiceImpl implements PolicyBbsBannedWordBosService {

	@Autowired
    private PolicyBbsBannedWordBiz policyBbsBannedWordBiz;

	/**
	 * 게시판금칙어 설정 조회
	 * @param PolicyBbsBannedWordDto
	 * @return PolicyBbsBannedWordDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getPolicyBbsBannedWord(PolicyBbsBannedWordDto dto) {
		return policyBbsBannedWordBiz.getPolicyBbsBannedWord(dto);
	}

	/**
	 * 게시판금칙어 설정 저장
	 * @param PolicyBbsBannedWordDto
	 * @return BaseResponseDto
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = BaseException.class)
	public ApiResult<?> putPolicyBbsBannedWord(PolicyBbsBannedWordDto dto) {
		return policyBbsBannedWordBiz.putPolicyBbsBannedWord(dto);
	}
}

