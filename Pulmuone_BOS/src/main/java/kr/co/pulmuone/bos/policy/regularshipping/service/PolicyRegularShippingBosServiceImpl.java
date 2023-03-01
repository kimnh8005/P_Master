package kr.co.pulmuone.bos.policy.regularshipping.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.policy.regularshipping.dto.PolicyRegularShippingDto;
import kr.co.pulmuone.v1.policy.regularshipping.service.PolicyRegularShippingBiz;
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
 *  1.0    20200902    박승현              최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class PolicyRegularShippingBosServiceImpl implements PolicyRegularShippingBosService {

	@Autowired
    private PolicyRegularShippingBiz policyRegularShippingBiz;

	/**
	 * 정기배송 기본설정 데이터 조회
	 * @param PolicyRegularShippingDto
	 * @return PolicyRegularShippingDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getPolicyRegularShipping(PolicyRegularShippingDto dto) {
		return policyRegularShippingBiz.getPolicyRegularShipping(dto);
	}

	/**
	 * 정기배송 기본설정 저장
	 * @param PolicyRegularShippingDto
	 * @return BaseResponseDto
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = BaseException.class)
	public ApiResult<?> putPolicyRegularShipping(PolicyRegularShippingDto dto) {
		return policyRegularShippingBiz.putPolicyRegularShipping(dto);
	}
}

