package kr.co.pulmuone.bos.user.member.service;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBosBiz;

import kr.co.pulmuone.v1.user.buyer.service.UserBuyerDropBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyerBosServiceImpl implements BuyerBosService {
  @Autowired
  private UserBuyerBosBiz userBuyerBosBiz;

  @Autowired
  private UserBuyerDropBiz userBuyerDropBiz;

  @Override
  @UserMaskingRun(system = "MUST_MASKING")
  public GetBuyerListResponseDto getBuyerList(GetBuyerListRequestDto dto) throws Exception {
    return userBuyerBosBiz.getBuyerList(dto);
  }

  @Override
  @UserMaskingRun(system = "BOS")
  public GetCouponListResponseDto getCouponList(GetCouponListRequestDto dto) throws Exception {
    return userBuyerBosBiz.getCouponList(dto);
  }

  @Override
  @UserMaskingRun(system = "BOS")
  public GetBuyerResponseDto getBuyer(GetBuyerRequestDto dto) throws Exception {
    return userBuyerBosBiz.getBuyer(dto);
  }

  @Override
  @UserMaskingRun(system = "BOS")
  public GetBuyerResponseDto getBuyerMove(GetBuyerRequestDto dto) throws Exception {
    return userBuyerBosBiz.getBuyerMove(dto);
  }

  @Override
  @UserMaskingRun(system = "BOS")
  public GetBuyerRecommendListResponseDto getBuyerRecommendList(GetBuyerRecommendListRequestDto dto) throws Exception {
    return userBuyerBosBiz.getBuyerRecommendList(dto);
  }

  @Override
  @UserMaskingRun(system = "MUST_MASKING")
  public GetUserChangeHistoryListResponseDto getUserChangeHistoryList(GetUserChangeHistoryListRequestDto dto) throws Exception {
    return userBuyerBosBiz.getUserChangeHistoryList(dto);
  }

  @Override
  public ExcelDownloadDto getBuyerListExcelDownload(GetBuyerListRequestDto dto) throws Exception {
    return userBuyerBosBiz.getBuyerListExcelDownload(dto);
  }

  @Override
  public ApiResult<?> addBuyerDrop(UserDropRequestDto dto) throws Exception {
    UserVo userVo = SessionUtil.getBosUserVO();
    if (userVo == null || StringUtil.isEmpty(userVo.getUserId())) {
      return ApiResult.result(UserEnums.Join.NEED_LOGIN);
    }
    dto.setCode(BuyerConstants.UR_MOVE_REASON_ID);
    dto.setComment("관리자 탈퇴");
    dto.setCreateId(Long.valueOf(userVo.getUserId()));

    return userBuyerDropBiz.progressUserDrop(dto);
  }

}
