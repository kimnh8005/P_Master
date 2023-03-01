package kr.co.pulmuone.v1.policy.regularshipping.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.regularshipping.dto.PolicyRegularShippingDto;

public interface PolicyRegularShippingBiz {

	ApiResult<?> getPolicyRegularShipping(PolicyRegularShippingDto dto);

	ApiResult<?> putPolicyRegularShipping(PolicyRegularShippingDto dto);
}
