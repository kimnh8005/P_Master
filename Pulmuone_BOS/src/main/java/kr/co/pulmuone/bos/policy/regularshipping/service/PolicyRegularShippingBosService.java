package kr.co.pulmuone.bos.policy.regularshipping.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.regularshipping.dto.PolicyRegularShippingDto;

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
 *  1.0    20200902    박승현              최초작성
 * =======================================================================
 * </PRE>
 */

public interface PolicyRegularShippingBosService {

	ApiResult<?> getPolicyRegularShipping(PolicyRegularShippingDto dto);

	ApiResult<?> putPolicyRegularShipping(PolicyRegularShippingDto dto);

}

