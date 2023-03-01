package kr.co.pulmuone.bos.policy.bbs.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsBannedWordDto;

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
 *  1.0    20200916			박승현		최초작성
 * =======================================================================
 * </PRE>
 */

public interface PolicyBbsBannedWordBosService {

	ApiResult<?> getPolicyBbsBannedWord(PolicyBbsBannedWordDto dto);

	ApiResult<?> putPolicyBbsBannedWord(PolicyBbsBannedWordDto dto);

}
