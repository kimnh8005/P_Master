package kr.co.pulmuone.v1.user.buyer.service;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerGroupChangeHistoryListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerListResultVo;
import kr.co.pulmuone.v1.user.certification.dto.PutPasswordClearRequestDto;

public interface UserBuyerBosBiz {

  GetBuyerListResponseDto getBuyerList(GetBuyerListRequestDto dto) throws Exception;

  GetCouponListResponseDto getCouponList(GetCouponListRequestDto dto) throws Exception;

  GetBuyerResponseDto getBuyer(GetBuyerRequestDto dto) throws Exception;

  GetBuyerGroupChangeHistoryListResponseDto getBuyerGroupChangeHistoryList(GetBuyerGroupChangeHistoryListRequestDto dto) throws Exception;

  GetBuyerRecommendListResponseDto getBuyerRecommendList(GetBuyerRecommendListRequestDto dto) throws Exception;

  ApiResult<?> putBuyer(PutBuyerRequestDto dto) throws Exception;

  GetBuyerResponseDto getBuyerMove(GetBuyerRequestDto dto) throws Exception;

  ExcelDownloadDto getBuyerListExcelDownload(GetBuyerListRequestDto dto) throws Exception;

  ApiResult<?> putPasswordClear(PutPasswordClearRequestDto dto) throws Exception;

  GetUserChangeHistoryListResponseDto getUserChangeHistoryList(GetUserChangeHistoryListRequestDto dto) throws Exception;

  ApiResult<?> getUserMaliciousClaimHistoryList(GetUserMaliciousClaimHistoryListRequestDto getUserMaliciousClaimHistoryListRequestDto) throws Exception;

  ApiResult<?> getPointInfo(GetBuyerListRequestDto dto) throws Exception;

  List<GetBuyerListResultVo> getBuyerListExcel(GetBuyerListRequestDto getBuyerListRequestDto) throws Exception;
}
