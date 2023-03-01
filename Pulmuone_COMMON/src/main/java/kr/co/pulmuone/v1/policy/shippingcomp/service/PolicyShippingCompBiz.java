package kr.co.pulmuone.v1.policy.shippingcomp.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.shippingcomp.dto.PolicyShippingCompRequestDto;

public interface PolicyShippingCompBiz {

	ApiResult<?> getPolicyShippingCompInfo(Long psShippingCompId);

	ApiResult<?> getPolicyShippingCompList(PolicyShippingCompRequestDto dto);

	ApiResult<?> addPolicyShippingComp(PolicyShippingCompRequestDto dto);

	ApiResult<?> putPolicyShippingComp(PolicyShippingCompRequestDto dto);

	ApiResult<?> delPolicyShippingComp(PolicyShippingCompRequestDto dto);

	ApiResult<?> getPolicyShippingCompUseAllList();

	ApiResult<?> getDropDownPolicyShippingCompList();

}
