package kr.co.pulmuone.v1.policy.bbs.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsAuthDto;

public interface PolicyBbsAuthBiz {

	ApiResult<?> getPolicyBbsAuthInfo(Long csBbsConfigId);

	ApiResult<?> getPolicyBbsAuthList(PolicyBbsAuthDto dto);

	ApiResult<?> getPolicyBbsAuthCategoryList(String bbsTp);

	ApiResult<?> addPolicyBbsAuth(PolicyBbsAuthDto dto);

	ApiResult<?> putPolicyBbsAuth(PolicyBbsAuthDto dto);

	ApiResult<?> delPolicyBbsAuth(Long csBbsConfigId);
}
