package kr.co.pulmuone.v1.policy.shoppingsetting.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.GetPolicyShopSettingListRequestDto;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.PutPolicyShopSettingRequestDto;


public interface PolicyShopSettingBiz {

    ApiResult<?> getPolicyShopSettingList(GetPolicyShopSettingListRequestDto getPolicyShopSettingListRequestDto) throws Exception;
    ApiResult<?> putPolicyShopSetting(PutPolicyShopSettingRequestDto putPolicyShopSettingRequestDto) throws Exception;
}
