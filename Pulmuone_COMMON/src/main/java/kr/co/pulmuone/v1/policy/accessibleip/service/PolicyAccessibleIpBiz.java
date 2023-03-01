package kr.co.pulmuone.v1.policy.accessibleip.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.accessibleip.dto.basic.GetPolicyAccessibleIpListRequestDto;
import kr.co.pulmuone.v1.policy.accessibleip.dto.basic.SavePolicyAccessibleIpRequestDto;

public interface PolicyAccessibleIpBiz {

    ApiResult<?> getPolicyAccessibleIpList(GetPolicyAccessibleIpListRequestDto getPolicyAccessibleIpListRequestDto) ;
    ApiResult<?> savePolicyAccessibleIp(SavePolicyAccessibleIpRequestDto savePolicyAccessibleIpRequestDto) throws Exception;
}
