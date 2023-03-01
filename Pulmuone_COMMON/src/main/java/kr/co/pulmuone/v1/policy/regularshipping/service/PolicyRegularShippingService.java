package kr.co.pulmuone.v1.policy.regularshipping.service;

import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.policy.regularshipping.PolicyRegularShippingMapper;
import kr.co.pulmuone.v1.policy.regularshipping.dto.PolicyRegularShippingDto;
import lombok.RequiredArgsConstructor;
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
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0		20200902		박승현              최초작성
 *  1.1		20200914		박승현		코드 신규 개발 가이드 적용
 *
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyRegularShippingService {

    private final PolicyRegularShippingMapper policyRegularShippingMapper;

    /**
     * 정기배송 기본설정
     *
     * @param PolicyRegularShippingDto
     * @return PolicyRegularShippingDto
     * @throws 	Exception
     */
    protected PolicyRegularShippingDto getPolicyRegularShipping(PolicyRegularShippingDto dto) {
    	return policyRegularShippingMapper.getPolicyRegularShipping(dto);
    }
    /**
     * 정기배송 기본설정 저장
     *
     * @param PolicyRegularShippingDto
     * @return int
     * @throws 	Exception
     */
    protected int putPolicyRegularShipping(PolicyRegularShippingDto dto) {
    	int result = 0;
    	PolicyRegularShippingDto rtnDto = policyRegularShippingMapper.getPolicyRegularShipping(dto);
    	if(rtnDto == null) {
    		result = policyRegularShippingMapper.addPolicyRegularShipping(dto);
    	}else {
    		result = policyRegularShippingMapper.putPolicyRegularShipping(dto);

    	}
		return result;
    }
}
