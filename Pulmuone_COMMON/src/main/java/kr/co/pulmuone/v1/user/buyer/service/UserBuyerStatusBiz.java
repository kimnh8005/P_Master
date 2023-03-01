package kr.co.pulmuone.v1.user.buyer.service;

import kr.co.pulmuone.v1.user.buyer.dto.GetBuyerStatusHistoryListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetBuyerStatusHistoryListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopHistoryListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopHistoryListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopHistoryRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopHistoryResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserStopListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.MarketingInfoDto;
import kr.co.pulmuone.v1.user.buyer.dto.PutBuyerNormalRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.PutBuyerNormalResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.PutBuyerStopRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.PutBuyerStopResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.BuyerStatusResultVo;

public interface UserBuyerStatusBiz {

	GetUserStopListResponseDto getBuyerStopList(GetUserStopListRequestDto dto) throws Exception;

	GetUserStopHistoryResponseDto getBuyerStopLog(GetUserStopHistoryRequestDto dto) throws Exception;

	GetUserStopHistoryListResponseDto getBuyerStopHistoryList(GetUserStopHistoryListRequestDto dto) throws Exception;

	GetBuyerStatusHistoryListResponseDto getBuyerStatusHistoryList(GetBuyerStatusHistoryListRequestDto dto) throws Exception;

	PutBuyerStopResponseDto putBuyerStop(PutBuyerStopRequestDto dto) throws Exception;

	BuyerStatusResultVo getBuyerStatusConvertInfo(String urUserId);

	void getBuyerStopConvertCompleted(BuyerStatusResultVo buyerStatusResultVo);

	PutBuyerNormalResponseDto putBuyerNormal(PutBuyerNormalRequestDto dto) throws Exception;

	void getBuyerNormalConvertCompleted(BuyerStatusResultVo buyerStatusResultVo);

	MarketingInfoDto getMarketingInfo(Long urUserId) throws Exception;




}
