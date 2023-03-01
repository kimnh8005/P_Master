package kr.co.pulmuone.v1.policy.bbs.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums.EnumSiteType;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsBannedWordDto;

import java.util.List;

public interface PolicyBbsBannedWordBiz {

	ApiResult<?> getPolicyBbsBannedWord(PolicyBbsBannedWordDto dto);

	ApiResult<?> putPolicyBbsBannedWord(PolicyBbsBannedWordDto dto);

	List<String> getSpam(String siteType);

	String filterSpamWord(String contents, EnumSiteType site);
}
