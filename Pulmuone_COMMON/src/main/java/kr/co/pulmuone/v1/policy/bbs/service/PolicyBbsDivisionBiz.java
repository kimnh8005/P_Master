package kr.co.pulmuone.v1.policy.bbs.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsDivisionDto;

public interface PolicyBbsDivisionBiz {

	ApiResult<?> getPolicyBbsDivisionInfo(Long csCategoryId);

	ApiResult<?> getPolicyBbsDivisionList(PolicyBbsDivisionDto dto);

	ApiResult<?> getPolicyBbsDivisionParentCategoryList(String bbsTp);

	ApiResult<?> addPolicyBbsDivision(PolicyBbsDivisionDto dto);

	ApiResult<?> putPolicyBbsDivision(PolicyBbsDivisionDto dto);

	ApiResult<?> delPolicyBbsDivision(Long csCategoryId);
}
