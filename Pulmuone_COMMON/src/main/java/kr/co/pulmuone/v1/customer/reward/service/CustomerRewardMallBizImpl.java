package kr.co.pulmuone.v1.customer.reward.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.CustomerEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.customer.reward.dto.*;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyInfoVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyListVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardPageInfoVo;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromMypageRewardRequestDto;
import kr.co.pulmuone.v1.order.front.dto.vo.OrderInfoFromMypageRewardVo;
import kr.co.pulmuone.v1.order.front.service.OrderFrontBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CustomerRewardMallBizImpl implements CustomerRewardMallBiz {

    @Autowired
    private CustomerRewardMallService customerRewardMallService;

    @Autowired
    private OrderFrontBiz orderFrontBiz;

    @Override
    public List<RewardPageInfoVo> getRewardPageInfo(String deviceType) throws Exception {
        return customerRewardMallService.getRewardPageInfo(deviceType);
    }

    @Override
    public RewardGoodsResponseDto getRewardGoods(RewardGoodsRequestDto dto) throws Exception {
        return customerRewardMallService.getRewardGoods(dto);
    }

    @Override
    public RewardInfoResponseDto getRewardInfo(String deviceType, Long csRewardId) throws Exception {
        return customerRewardMallService.getRewardInfo(deviceType, csRewardId);
    }

    @Override
    public ApiResult<?> addRewardApply(RewardRequestDto dto) {

        if (StringUtil.isEmpty(dto.getCsRewardApplyId())) { // 추가
            // validation
            Integer validationInfo = customerRewardMallService.getRewardValidation(dto);
            MessageCommEnum addValidationResult = customerRewardMallService.addRewardValidation(validationInfo);
            if (!BaseEnums.Default.SUCCESS.equals(addValidationResult)) {
                return ApiResult.result(addValidationResult);
            }

            // 추가처리
            customerRewardMallService.addRewardApply(dto);
        } else {    // 수정
            // validation
            RewardApplyVo rewardApplyInfo = customerRewardMallService.getRewardApply(dto.getCsRewardApplyId());
            rewardApplyInfo.setRequestUrUserId(dto.getUrUserId());
            MessageCommEnum validationResult = customerRewardMallService.rewardApplyValidation(rewardApplyInfo, CustomerEnums.RewardValidationType.NORMAL);
            if (!BaseEnums.Default.SUCCESS.equals(validationResult)) {
                return ApiResult.result(validationResult);
            }

            // 수정처리
            customerRewardMallService.putRewardApply(dto);
        }

        return ApiResult.success();
    }

    @Override
    public RewardApplyInfoVo getRewardApplyInfo(RewardApplyRequestDto dto) {
        return customerRewardMallService.getRewardApplyInfo(dto);
    }

    @Override
    public RewardApplyResponseDto getRewardApplyList(RewardApplyRequestDto dto) {
        RewardApplyResponseDto result = customerRewardMallService.getRewardApplyList(dto);
        List<RewardApplyListVo> rewardList = result.getReward();
        for (RewardApplyListVo vo : rewardList) {
            OrderInfoFromMypageRewardRequestDto orderRequestDto = null;
            if (CustomerEnums.RewardApplyStandard.ORDER_GOODS.getCode().equals(vo.getRewardApplyStandard())) {
                List<Long> goodsIdList = null;
                if (CustomerEnums.RewardGoodsType.TARGET_GOODS.getCode().equals(vo.getRewardGoodsType())) {
                    goodsIdList = customerRewardMallService.getRewardTargetGoods(vo.getCsRewardId());
                }
                orderRequestDto = OrderInfoFromMypageRewardRequestDto.builder()
                        .odOrderId(vo.getOdOrderId())
                        .odOrderDetlId(vo.getOdOrderDetlId())
                        .goodsIdList(goodsIdList)
                        .build();
            } else if (CustomerEnums.RewardApplyStandard.ORDER_NUMBER.getCode().equals(vo.getRewardApplyStandard())) {
                orderRequestDto = OrderInfoFromMypageRewardRequestDto.builder()
                        .odOrderId(vo.getOdOrderId())
                        .build();
            } else if (CustomerEnums.RewardApplyStandard.PACK_DELIVERY.getCode().equals(vo.getRewardApplyStandard())) {
                orderRequestDto = OrderInfoFromMypageRewardRequestDto.builder()
                        .odOrderId(vo.getOdOrderId())
                        .deliveryDate(vo.getDeliveryDate())
                        .goodsDeliveryType(vo.getGoodsDeliveryType())
                        .odShippingPriceId(vo.getOdShippingPriceId())
                        .build();
            }

            List<OrderInfoFromMypageRewardVo> orderInfo = orderFrontBiz.getOrderInfoFromMyPageReward(orderRequestDto);
            if (orderInfo != null && !orderInfo.isEmpty()) {
                vo.setOrderInfo(orderInfo.get(0));
                vo.setOrderGoodsCount(orderInfo.size() - 1);
            }
        }

        return result;
    }

    @Override
    public ApiResult<?> getRewardApply(Long csRewardApplyId, Long urUserId) {
        // validation
        RewardApplyVo rewardApplyInfo = customerRewardMallService.getRewardApply(csRewardApplyId);
        rewardApplyInfo.setRequestUrUserId(urUserId);
        MessageCommEnum validationResult = customerRewardMallService.rewardApplyValidation(rewardApplyInfo, CustomerEnums.RewardValidationType.NORMAL);
        if (!BaseEnums.Default.SUCCESS.equals(validationResult)) {
            return ApiResult.result(validationResult);
        }

        OrderInfoFromMypageRewardRequestDto orderRequestDto = null;
        if (CustomerEnums.RewardApplyStandard.ORDER_GOODS.getCode().equals(rewardApplyInfo.getRewardApplyStandard())) {
            List<Long> goodsIdList = null;
            if (CustomerEnums.RewardGoodsType.TARGET_GOODS.getCode().equals(rewardApplyInfo.getRewardGoodsType())) {
                goodsIdList = customerRewardMallService.getRewardTargetGoods(rewardApplyInfo.getCsRewardId());
            }
            orderRequestDto = OrderInfoFromMypageRewardRequestDto.builder()
                    .odOrderId(rewardApplyInfo.getOdOrderId())
                    .odOrderDetlId(rewardApplyInfo.getOdOrderDetlId())
                    .goodsIdList(goodsIdList)
                    .build();
        } else if (CustomerEnums.RewardApplyStandard.ORDER_NUMBER.getCode().equals(rewardApplyInfo.getRewardApplyStandard())) {
            orderRequestDto = OrderInfoFromMypageRewardRequestDto.builder()
                    .odOrderId(rewardApplyInfo.getOdOrderId())
                    .build();
        } else if (CustomerEnums.RewardApplyStandard.PACK_DELIVERY.getCode().equals(rewardApplyInfo.getRewardApplyStandard())) {
            orderRequestDto = OrderInfoFromMypageRewardRequestDto.builder()
                    .odOrderId(rewardApplyInfo.getOdOrderId())
                    .deliveryDate(rewardApplyInfo.getDeliveryDate())
                    .goodsDeliveryType(rewardApplyInfo.getGoodsDeliveryType())
                    .odShippingPriceId(rewardApplyInfo.getOdShippingPriceId())
                    .build();
        }

        List<OrderInfoFromMypageRewardVo> orderInfo = orderFrontBiz.getOrderInfoFromMyPageReward(orderRequestDto);
        if (orderInfo != null && !orderInfo.isEmpty()) {
            rewardApplyInfo.setOrderInfo(orderInfo.get(0));
            rewardApplyInfo.setOrderGoodsCount(orderInfo.size() - 1);
        }

        // 결과값
        return ApiResult.success(new GetRewardApplyResponseDto(rewardApplyInfo));
    }

    @Override
    public ApiResult<?> delRewardApply(Long csRewardApplyId, Long urUserId) {
        // validation
        RewardApplyVo rewardApplyInfo = customerRewardMallService.getRewardApply(csRewardApplyId);
        rewardApplyInfo.setRequestUrUserId(urUserId);
        MessageCommEnum validationResult = customerRewardMallService.rewardApplyValidation(rewardApplyInfo, CustomerEnums.RewardValidationType.NORMAL);
        if (!BaseEnums.Default.SUCCESS.equals(validationResult)) {
            return ApiResult.result(validationResult);
        }

        // 삭제처리
        customerRewardMallService.delRewardApply(csRewardApplyId);
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> putRewardApplyDelYn(Long csRewardApplyId, Long urUserId) {
        // validation
        RewardApplyVo rewardApplyInfo = customerRewardMallService.getRewardApply(csRewardApplyId);
        rewardApplyInfo.setRequestUrUserId(urUserId);
        MessageCommEnum validationResult = customerRewardMallService.rewardApplyValidation(rewardApplyInfo, CustomerEnums.RewardValidationType.PUT_DEL_YN);
        if (!BaseEnums.Default.SUCCESS.equals(validationResult)) {
            return ApiResult.result(validationResult);
        }

        // 삭제처리
        customerRewardMallService.putRewardApplyDelYn(csRewardApplyId);
        return ApiResult.success();
    }

    @Override
    public RewardOrderResponseDto getRewardOrderInfo(Long csRewardId, Long urUserId) {
        return customerRewardMallService.getRewardOrderInfo(csRewardId, urUserId);
    }

    @Override
    public void putRewardUserCheckYn(Long csRewardApplyId) {
        customerRewardMallService.putRewardUserCheckYn(csRewardApplyId);
    }

}
