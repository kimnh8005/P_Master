package kr.co.pulmuone.v1.user.buyer.service;

import kr.co.pulmuone.v1.user.buyer.dto.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerGroupChangeHistoryListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetPointInfoVo;
import kr.co.pulmuone.v1.user.certification.dto.PutPasswordClearRequestDto;

@Service
public class UserBuyerBosBizImpl implements UserBuyerBosBiz {

  @Autowired
  private UserBuyerBosService userBuyerBosService;

  @Override
  public GetBuyerListResponseDto getBuyerList(GetBuyerListRequestDto dto) throws Exception {
    return userBuyerBosService.getBuyerList(dto);
  }

  @Override
  public GetCouponListResponseDto getCouponList(GetCouponListRequestDto dto) throws Exception {
    return userBuyerBosService.getCouponList(dto);
  }


  @Override
  public GetBuyerResponseDto getBuyer(GetBuyerRequestDto dto) throws Exception {
    GetBuyerResponseDto responseDto = new GetBuyerResponseDto();
    responseDto.setRows(userBuyerBosService.getBuyer(dto));
    return responseDto;
  }

  @Override
  public GetBuyerGroupChangeHistoryListResponseDto getBuyerGroupChangeHistoryList(GetBuyerGroupChangeHistoryListRequestDto dto) throws Exception {
	  GetBuyerGroupChangeHistoryListResponseDto responseDto = new GetBuyerGroupChangeHistoryListResponseDto();
	  responseDto.setRows(userBuyerBosService.getBuyerGroupChangeHistoryList(dto));
    return responseDto;
  }

  @Override
  public GetBuyerRecommendListResponseDto getBuyerRecommendList(GetBuyerRecommendListRequestDto dto) throws Exception {
    GetBuyerRecommendListResponseDto responseDto = new GetBuyerRecommendListResponseDto();
    responseDto.setRows(userBuyerBosService.getBuyerRecommendList(dto));
    return responseDto;
  }

  @Override
  public ApiResult<?> putBuyer(PutBuyerRequestDto dto) throws Exception {
    return ApiResult.success(userBuyerBosService.putBuyer(dto));
  }

  @Override
  public GetBuyerResponseDto getBuyerMove(GetBuyerRequestDto dto) throws Exception {
    GetBuyerResponseDto responseDto = new GetBuyerResponseDto();
    responseDto.setRows(userBuyerBosService.getBuyerMove(dto));
    return responseDto;
  }

  @Override
  public ExcelDownloadDto getBuyerListExcelDownload(GetBuyerListRequestDto dto) throws Exception {
    return userBuyerBosService.getBuyerListExcelDownload(dto);
  }

  @Override
  public ApiResult<?> putPasswordClear(PutPasswordClearRequestDto dto) throws Exception {
    int cnt = userBuyerBosService.putPasswordClear(dto);

    return cnt > 1 ? ApiResult.success() : ApiResult.fail();
  }

  @Override
  public GetUserChangeHistoryListResponseDto getUserChangeHistoryList(GetUserChangeHistoryListRequestDto dto) throws Exception {
    return userBuyerBosService.getUserChangeHistoryList(dto);
  }

  @Override
  public ApiResult<?> getUserMaliciousClaimHistoryList(GetUserMaliciousClaimHistoryListRequestDto getUserMaliciousClaimHistoryListRequestDto) throws Exception {
    return ApiResult.success(userBuyerBosService.getUserMaliciousClaimHistoryList(getUserMaliciousClaimHistoryListRequestDto));
  }

  @Override
  public ApiResult<?> getPointInfo(GetBuyerListRequestDto dto) throws Exception{
  	GetPointListResponseDto result = new GetPointListResponseDto();

      Page<GetPointInfoVo> pointHistoryList = userBuyerBosService.getPointInfo(dto);

      int availablePoint = userBuyerBosService.getUserAvailablePoints(dto);

      result.setTotal(availablePoint);
      result.setRows(pointHistoryList.getResult());

      return ApiResult.success(result);
  }

  @Override
  @UserMaskingRun(system = "BOS")
  public List<GetBuyerListResultVo> getBuyerListExcel(GetBuyerListRequestDto getBuyerListRequestDto)  throws Exception {
    return userBuyerBosService.getBuyerListExcel(getBuyerListRequestDto);
  }
}
