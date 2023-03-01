package kr.co.pulmuone.bos.user.member.service;


import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.user.buyer.dto.*;

public interface BuyerBosService {
  GetBuyerListResponseDto getBuyerList(GetBuyerListRequestDto dto) throws Exception;

  GetCouponListResponseDto getCouponList(GetCouponListRequestDto dto) throws Exception;

  GetBuyerResponseDto getBuyer(GetBuyerRequestDto dto) throws Exception;

  GetBuyerResponseDto getBuyerMove(GetBuyerRequestDto dto) throws Exception;

  GetBuyerRecommendListResponseDto getBuyerRecommendList(GetBuyerRecommendListRequestDto dto) throws Exception;

  GetUserChangeHistoryListResponseDto getUserChangeHistoryList(GetUserChangeHistoryListRequestDto dto) throws Exception;

  ExcelDownloadDto getBuyerListExcelDownload(GetBuyerListRequestDto dto) throws Exception;

  ApiResult<?> addBuyerDrop(UserDropRequestDto dto) throws Exception;

}
