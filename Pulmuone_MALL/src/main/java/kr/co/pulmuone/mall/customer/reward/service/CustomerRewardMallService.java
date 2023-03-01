package kr.co.pulmuone.mall.customer.reward.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.reward.dto.RewardApplyRequestDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardGoodsRequestDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardRequestDto;

public interface CustomerRewardMallService {

    ApiResult<?> getRewardPageInfo() throws Exception;

    ApiResult<?> getRewardGoods(RewardGoodsRequestDto dto) throws Exception;

    ApiResult<?> getRewardInfo(Long csRewardId) throws Exception;

    ApiResult<?> addRewardApply(RewardRequestDto dto);

    ApiResult<?> getRewardApplyInfo(RewardApplyRequestDto dto);

    ApiResult<?> getRewardApplyList(RewardApplyRequestDto dto);

    ApiResult<?> getRewardApply(Long csRewardApplyId);

    ApiResult<?> delRewardApply(Long csRewardApplyId);

    ApiResult<?> putRewardApplyDelYn(Long csRewardApplyId);

    ApiResult<?> getRewardOrderInfo(Long csRewardId);

    ApiResult<?> putRewardUserCheckYn(Long csRewardApplyId);

}
