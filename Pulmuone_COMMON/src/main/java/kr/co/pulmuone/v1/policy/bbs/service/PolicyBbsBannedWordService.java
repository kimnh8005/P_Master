package kr.co.pulmuone.v1.policy.bbs.service;

import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.policy.bbs.PolicyBbsMapper;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsBannedWordDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyBbsBannedWordService {

    private final PolicyBbsMapper policyBbsMapper;

    /**
     * 게시판금칙어 설정 조회
     *
     * @param PolicyBbsBannedWordDto
     * @return PolicyBbsBannedWordDto
     * @throws 	Exception
     */
    protected PolicyBbsBannedWordDto getPolicyBbsBannedWord(PolicyBbsBannedWordDto dto) {
    	return policyBbsMapper.getPolicyBbsBannedWord(dto);
    }
    /**
     * 게시판금칙어 설정 저장
     *
     * @param PolicyBbsBannedWordDto
     * @return int
     * @throws 	Exception
     */
    protected int putPolicyBbsBannedWord(PolicyBbsBannedWordDto dto) {
    	int result = 0;
    	PolicyBbsBannedWordDto rtnDto = policyBbsMapper.getPolicyBbsBannedWord(dto);
    	if(rtnDto == null) {
    		result = policyBbsMapper.addPolicyBbsBannedWord(dto);
    	}else {
    		result = policyBbsMapper.putPolicyBbsBannedWord(dto);

    	}
		return result;
    }

	/**
	 * 금칙어 조회
	 *
	 * @param siteType String
	 * @return List<String>
	 */
	protected List<String> getSpam(String siteType) {
		return policyBbsMapper.getSpam(siteType);
	}
}
