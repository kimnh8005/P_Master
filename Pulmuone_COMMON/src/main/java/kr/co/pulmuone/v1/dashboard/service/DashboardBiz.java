package kr.co.pulmuone.v1.dashboard.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.dashboard.dto.DashboardRequestDto;

/**
* <PRE>
* Forbiz Korea
* 대시보드 COMMON Interface
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

public interface DashboardBiz {

  /**
   * 대시보드 리스트 조회
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectDashboardList (DashboardRequestDto dashboardRequestDto) throws BaseException;

  /**
   * 대시보드 수정
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> putDashboardList (DashboardRequestDto dashboardRequestDto) throws BaseException;

  /**
   * 대시보드-주문/매출현황
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> getDashboardOrderSaleStatics (DashboardRequestDto dashboardRequestDto) throws BaseException;

  /**
   * 대시보드-클레임현황
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> getDashboardClaimStatics (DashboardRequestDto dashboardRequestDto) throws BaseException;

  /**
   * 대시보드-회원가입 현황
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> getSignUpStatics(DashboardRequestDto dashboardRequestDto) throws BaseException;

  /**
   * 대시보드-고객 문의 현황
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> getCustomerQnaStatics(DashboardRequestDto dashboardRequestDto) throws BaseException;

  /**
   * 대시보드-보상제 처리 현황
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> getRewardApplyStatics(DashboardRequestDto dashboardRequestDto) throws BaseException;

  /**
   * 대시보드-부정거래 탐지 현황
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> getIllegalDetectStatics(DashboardRequestDto dashboardRequestDto) throws BaseException;
  
    /**
   * 대시보드-내 승인요청 현황
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> getMyApprovalRequestStatics(DashboardRequestDto dashboardRequestDto) throws BaseException;
  
   /**
   * 대시보드-내 승인처리 목록
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> getMyApprovalAcceptStatics(DashboardRequestDto dashboardRequestDto) throws BaseException;

  /**
   * 대시보드-담당자별 승인처리 현황
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> getTotalApprovalAcceptStatics(DashboardRequestDto dashboardRequestDto) throws BaseException;
  
   /**
   * 대시보드 모듈 전체 초기화
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> initDashboardList(DashboardRequestDto dashboardRequestDto) throws BaseException;

   /**
   * 대시보드 모듈 숨기기 (삭제)
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> delDashboardCard (DashboardRequestDto dashboardRequestDto) throws BaseException;


}
