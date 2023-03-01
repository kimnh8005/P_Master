package kr.co.pulmuone.v1.dashboard.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.dashboard.dto.DashboardRequestDto;
import kr.co.pulmuone.v1.dashboard.dto.DashboardResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
* <PRE>
* Forbiz Korea
* 대시보드 COMMON Impl
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0       2021.04.15.              dgyoun         최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardBizImpl implements DashboardBiz {

  @Autowired
  private DashboardService dashboardService;

  /**
   * 대시보드 리스트 조회
   */
  @Override
  public ApiResult<?> selectDashboardList (DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DashboardBizImpl.selectDashboardList Start");
    log.info("# ######################################");
    if (dashboardRequestDto != null) {
      log.info("# In.dashboardRequestDto :: " + dashboardRequestDto.toString());
    }
    else {
      log.info("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = dashboardService.selectDashboardList(dashboardRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 대시보드 수정
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putDashboardList (DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DashboardBizImpl.putDashboardList Start");
    log.info("# ######################################");
    if (dashboardRequestDto != null) {
      log.info("# In.dashboardRequestDto :: " + dashboardRequestDto.toString());
    }
    else {
      log.info("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = dashboardService.putDashboardList(dashboardRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 대시보드-주문/매출현황
   */
  @Override
  public ApiResult<?> getDashboardOrderSaleStatics (DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DashboardBizImpl.getDashboardOrderSaleStatics Start");
    log.info("# ######################################");
    if (dashboardRequestDto != null) {
      log.info("# In.dashboardRequestDto :: " + dashboardRequestDto.toString());
    }
    else {
      log.info("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = dashboardService.getDashboardOrderSaleStatics(dashboardRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 대시보드-클레임현황
   */
  @Override
  public ApiResult<?> getDashboardClaimStatics (DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DashboardBizImpl.getDashboardClaimStatics Start");
    log.info("# ######################################");
    if (dashboardRequestDto != null) {
      log.info("# In.dashboardRequestDto :: " + dashboardRequestDto.toString());
    }
    else {
      log.info("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = dashboardService.getDashboardClaimStatics(dashboardRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  @Override
  public ApiResult<?> getSignUpStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DashboardBizImpl.getSignUpStatics Start");
    log.info("# ######################################");
    if (dashboardRequestDto != null) {
      log.info("# In.dashboardRequestDto :: " + dashboardRequestDto.toString());
    }
    else {
      log.info("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = dashboardService.getSignUpStatics(dashboardRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  @Override
  public ApiResult<?> getCustomerQnaStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DashboardBizImpl.getCustomerQnaStatics Start");
    log.info("# ######################################");
    if (dashboardRequestDto != null) {
      log.info("# In.dashboardRequestDto :: " + dashboardRequestDto.toString());
    }
    else {
      log.info("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = dashboardService.getCustomerQnaStatics(dashboardRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  @Override
  public ApiResult<?> getRewardApplyStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DashboardBizImpl.getRewardApplyStatics Start");
    log.info("# ######################################");
    if (dashboardRequestDto != null) {
      log.info("# In.dashboardRequestDto :: " + dashboardRequestDto.toString());
    }
    else {
      log.info("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = dashboardService.getRewardApplyStatics(dashboardRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  @Override
  public ApiResult<?> getIllegalDetectStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DashboardBizImpl.getIllegalDetectStatics Start");
    log.info("# ######################################");
    if (dashboardRequestDto != null) {
      log.info("# In.dashboardRequestDto :: " + dashboardRequestDto.toString());
    }
    else {
      log.info("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = dashboardService.getIllegalDetectStatics(dashboardRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  @Override
  public ApiResult<?> getMyApprovalRequestStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DashboardBizImpl.getMyApprovalRequestStatics Start");
    log.info("# ######################################");
    if (dashboardRequestDto != null) {
      log.info("# In.dashboardRequestDto :: " + dashboardRequestDto.toString());
    }
    else {
      log.info("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = dashboardService.getMyApprovalRequestStatics(dashboardRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  @Override
  public ApiResult<?> getMyApprovalAcceptStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DashboardBizImpl.getMyApprovalAcceptStatics Start");
    log.info("# ######################################");
    if (dashboardRequestDto != null) {
      log.info("# In.dashboardRequestDto :: " + dashboardRequestDto.toString());
    }
    else {
      log.info("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = dashboardService.getMyApprovalAcceptStatics(dashboardRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  @Override
  public ApiResult<?> getTotalApprovalAcceptStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DashboardBizImpl.getTotalApprovalAcceptStatics Start");
    log.info("# ######################################");
    if (dashboardRequestDto != null) {
      log.info("# In.dashboardRequestDto :: " + dashboardRequestDto.toString());
    }
    else {
      log.info("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = dashboardService.getTotalApprovalAcceptStatics(dashboardRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 대시보드 모듈 전체 초기화
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> initDashboardList (DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DashboardBizImpl.initDashboardList Start");
    log.info("# ######################################");
    if (dashboardRequestDto != null) {
      log.info("# In.dashboardRequestDto :: " + dashboardRequestDto.toString());
    }
    else {
      log.info("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = dashboardService.initDashboardList(dashboardRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 대시보드 모듈 숨기기 (삭제)
   * @param urUserDashboardId
   * @return
   * @throws BaseException
   */
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delDashboardCard (DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DashboardBizImpl.delDashboardCard Start");
    log.info("# ######################################");
    if (dashboardRequestDto != null) {
      log.info("# In.dashboardRequestDto :: " + dashboardRequestDto.toString());
    }
    else {
      log.info("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = dashboardService.delDashboardCard(dashboardRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }
}
