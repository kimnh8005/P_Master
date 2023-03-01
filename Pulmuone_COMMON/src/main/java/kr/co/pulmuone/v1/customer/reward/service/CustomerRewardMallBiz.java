package kr.co.pulmuone.v1.customer.reward.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.reward.dto.*;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyInfoVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardPageInfoVo;

import java.util.List;

public interface CustomerRewardMallBiz {

    List<RewardPageInfoVo> getRewardPageInfo(String deviceType) throws Exception;

    RewardGoodsResponseDto getRewardGoods(RewardGoodsRequestDto dto) throws Exception;

    RewardInfoResponseDto getRewardInfo(String deviceType, Long csRewardId) throws Exception;

    ApiResult<?> addRewardApply(RewardRequestDto dto);

    RewardApplyInfoVo getRewardApplyInfo(RewardApplyRequestDto dto);

    RewardApplyResponseDto getRewardApplyList(RewardApplyRequestDto dto);

    ApiResult<?> getRewardApply(Long csRewardApplyId, Long urUserId);

    ApiResult<?> delRewardApply(Long csRewardApplyId, Long urUserId);

    ApiResult<?> putRewardApplyDelYn(Long csRewardApplyId, Long urUserId);

    RewardOrderResponseDto getRewardOrderInfo(Long csRewardId, Long urUserId);

    void putRewardUserCheckYn(Long csRewardApplyId);

}
