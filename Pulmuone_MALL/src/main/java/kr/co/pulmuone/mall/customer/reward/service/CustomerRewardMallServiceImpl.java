package kr.co.pulmuone.mall.customer.reward.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.CustomerEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.customer.reward.dto.RewardApplyRequestDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardGoodsRequestDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardInfoResponseDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardRequestDto;
import kr.co.pulmuone.v1.customer.reward.service.CustomerRewardMallBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerRewardMallServiceImpl implements CustomerRewardMallService {

    @Autowired
    private CustomerRewardMallBiz customerRewardMallBiz;

    @Override
    public ApiResult<?> getRewardPageInfo() throws Exception {
        String deviceType = DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode();
        return ApiResult.success(customerRewardMallBiz.getRewardPageInfo(deviceType));
    }

    @Override
    public ApiResult<?> getRewardGoods(RewardGoodsRequestDto dto) throws Exception {
        // TODO : 검색엔진 사용여부 추가 - 협의예정
//        if("Y".equals(dto.getSearchYn())){
//            //return 검색엔진
//        }

        return ApiResult.success(customerRewardMallBiz.getRewardGoods(dto));
    }

    @Override
    public ApiResult<?> getRewardInfo(Long csRewardId) throws Exception {
        String deviceType = DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode();
        RewardInfoResponseDto response = customerRewardMallBiz.getRewardInfo(deviceType, csRewardId);

        if (response == null) return ApiResult.result(CustomerEnums.RewardApply.NO_CS_REWARD);

        return ApiResult.success(response);
    }

    @Override
    public ApiResult<?> addRewardApply(RewardRequestDto dto) {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }
        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        dto.setRewardApplyStatus(CustomerEnums.RewardApplyStatus.ACCEPT.getCode()); // 접수상태

        return customerRewardMallBiz.addRewardApply(dto);
    }

    @Override
    public ApiResult<?> getRewardApplyInfo(RewardApplyRequestDto dto) {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }
        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));

        return ApiResult.success(customerRewardMallBiz.getRewardApplyInfo(dto));
    }

    @Override
    public ApiResult<?> getRewardApplyList(RewardApplyRequestDto dto) {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }
        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));

        return ApiResult.success(customerRewardMallBiz.getRewardApplyList(dto));
    }

    @Override
    public ApiResult<?> getRewardApply(Long csRewardApplyId) {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        return customerRewardMallBiz.getRewardApply(csRewardApplyId, Long.valueOf(buyerVo.getUrUserId()));
    }

    @Override
    public ApiResult<?> delRewardApply(Long csRewardApplyId) {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        return customerRewardMallBiz.delRewardApply(csRewardApplyId, Long.valueOf(buyerVo.getUrUserId()));
    }

    @Override
    public ApiResult<?> putRewardApplyDelYn(Long csRewardApplyId) {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        return customerRewardMallBiz.putRewardApplyDelYn(csRewardApplyId, Long.valueOf(buyerVo.getUrUserId()));
    }

    @Override
    public ApiResult<?> getRewardOrderInfo(Long csRewardId) {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        return ApiResult.success(customerRewardMallBiz.getRewardOrderInfo(csRewardId, Long.valueOf(buyerVo.getUrUserId())));
    }

    @Override
    public ApiResult<?> putRewardUserCheckYn(Long csRewardApplyId) {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(UserEnums.Join.NEED_LOGIN);
        }

        customerRewardMallBiz.putRewardUserCheckYn(csRewardApplyId);
        return ApiResult.success();
    }

}