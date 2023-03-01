package kr.co.pulmuone.bos.user.member.service;

import kr.co.pulmuone.v1.user.buyer.dto.*;

public interface BuyerStatusBosService {
    GetUserStopListResponseDto getBuyerStopList(GetUserStopListRequestDto convertRequestToObject) throws Exception;

    GetUserStopHistoryListResponseDto getBuyerStopHistoryList(GetUserStopHistoryListRequestDto convertRequestToObject) throws Exception;

    GetUserStopHistoryResponseDto getBuyerStopLog(GetUserStopHistoryRequestDto dto) throws Exception;
}
