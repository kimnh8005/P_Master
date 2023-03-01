package kr.co.pulmuone.bos.user.member.service;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerStatusBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyerStatusBosServiceImpl implements BuyerStatusBosService {

    @Autowired
    private UserBuyerStatusBiz userBuyerStatusBiz;

    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public GetUserStopListResponseDto getBuyerStopList(GetUserStopListRequestDto getUserStopListRequestDto) throws Exception{
        return userBuyerStatusBiz.getBuyerStopList(getUserStopListRequestDto);
    }

    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public GetUserStopHistoryListResponseDto getBuyerStopHistoryList(GetUserStopHistoryListRequestDto getUserStopHistoryListRequestDto) throws Exception{
        return userBuyerStatusBiz.getBuyerStopHistoryList(getUserStopHistoryListRequestDto);
    }

    @Override
    @UserMaskingRun(system = "BOS")
    public GetUserStopHistoryResponseDto getBuyerStopLog(GetUserStopHistoryRequestDto dto) throws Exception {
        return userBuyerStatusBiz.getBuyerStopLog(dto);
    }


}
