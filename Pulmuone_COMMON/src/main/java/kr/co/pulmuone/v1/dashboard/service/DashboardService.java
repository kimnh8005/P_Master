package kr.co.pulmuone.v1.dashboard.service;

import kr.co.pulmuone.v1.comm.enums.DashboardEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.dashboard.DashboardMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.dashboard.dto.DashboardRequestDto;
import kr.co.pulmuone.v1.dashboard.dto.DashboardResponseDto;
import kr.co.pulmuone.v1.dashboard.dto.vo.DashboardVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
* <PRE>
* Forbiz Korea
* 대시보드 COMMON Service
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
public class DashboardService {

  private final DashboardMapper dashboardMapper;

  // ##########################################################################
  // protected
  // ##########################################################################

  /**
   * 대시보드 리스트 조회
   */
  protected DashboardResponseDto selectDashboardList(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DashboardService.selectDashboardList Start");
    log.debug("# ######################################");

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto resultResDto = new DashboardResponseDto();
    resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getMessage());
    List<DashboardVo> resultList = null;
    int resultInt = 0;

    // ========================================================================
    // # 처리
    // ========================================================================

    // ----------------------------------------------------------------------
    // 세션정보 Set
    // ----------------------------------------------------------------------
    String sessionUserId = "";

    if (SessionUtil.getBosUserVO() != null) {
      sessionUserId = (SessionUtil.getBosUserVO()).getUserId();
    } else {
      sessionUserId = "0";
    }

    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------
    dashboardRequestDto.setUrUserId(sessionUserId);
    resultList = dashboardMapper.selectDashboardList(dashboardRequestDto);

    if (resultList != null && resultList.size() > 0) {
      resultResDto.setTotal(resultList.size());
    } else {
      // 조회건 없는 경우 기본값 등록 후 재조회
      log.debug("# 대시보드 기본값 등록");
      resultInt = dashboardMapper.addDashboardDefaultInfo(dashboardRequestDto);

      if (resultInt > 0) {
        resultList = dashboardMapper.selectDashboardList(dashboardRequestDto);

        if (resultList != null && resultList.size() > 0) {
          resultResDto.setTotal(resultList.size());
        } else {
          log.debug("# 대시보드 기본값 등록 후 조회 건 없음");
          resultResDto.setTotal(0);
        }
      } else {
        log.debug("# 대시보드 기본값 등록 실패");
        resultResDto.setTotal(0);
      }
    }
    resultResDto.setRows(resultList);

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 대시보드 수정
   */
  protected DashboardResponseDto putDashboardList(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DashboardService.putDashboardList Start");
    log.debug("# ######################################");

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto resultResDto = new DashboardResponseDto();
    resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getMessage());
    List<DashboardVo> resultList = null;
    int resultInt = 0;
    int resultIntTot = 0;

    // ========================================================================
    // # 처리
    // ========================================================================

    // ----------------------------------------------------------------------
    // 세션정보 Set
    // ----------------------------------------------------------------------
    String sessionUserId = "";

    if (SessionUtil.getBosUserVO() != null) {
      sessionUserId = (SessionUtil.getBosUserVO()).getUserId();
    } else {
      sessionUserId = "0";
    }

    if (dashboardRequestDto == null || dashboardRequestDto.getDashboardList() == null && dashboardRequestDto.getDashboardList().size() <= 0) {

      log.debug("# 대시보드 수정 대상이 없습니다.");
      resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_PUT_DATA.getCode());
      resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_PUT_DATA.getMessage());
      return resultResDto;
    }

    for (DashboardVo vo : dashboardRequestDto.getDashboardList()) {

      vo.setUrUserId(sessionUserId);
      resultInt = dashboardMapper.putDashboardDefaultInfo(vo);
      log.debug("# [" + vo.getUrUserDashboardId() + "] :: " + resultInt);
      resultIntTot += resultInt;

    } // End of for (DashboardVo vo : dashboardRequestDto.getDashboardList())

    if (resultIntTot != dashboardRequestDto.getDashboardList().size()) {
      log.debug("# 대시정보 수정 오류입니다. :: [" + resultIntTot + "][" + dashboardRequestDto.getDashboardList().size() + "]");
      resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_ERROR_PUT_PROC.getCode());
      resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_ERROR_PUT_PROC.getMessage());
      return resultResDto;
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 대시보드-주문/매출현황
   *
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  protected DashboardResponseDto getDashboardOrderSaleStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DashboardService.getDashboardOrderSaleStatics Start");
    log.debug("# ######################################");

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto resultResDto = new DashboardResponseDto();
    resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getMessage());
    DashboardVo result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------
    result = dashboardMapper.getDashboardOrderSaleStatics(dashboardRequestDto);
    resultResDto.setOrderSaleStatics(result);

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 대시보드-클레임현황
   *
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  protected DashboardResponseDto getDashboardClaimStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DashboardService.getDashboardClaimStatics Start");
    log.debug("# ######################################");

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto resultResDto = new DashboardResponseDto();
    resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getMessage());
    DashboardVo result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------
    result = dashboardMapper.getDashboardClaimStatics(dashboardRequestDto);
    resultResDto.setClaimStatics(result);

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 대시보드-회원가입 현황
   *
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  protected DashboardResponseDto getSignUpStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DashboardService.getSignUpStatics Start");
    log.debug("# ######################################");

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto resultResDto = new DashboardResponseDto();
    resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getMessage());
    DashboardVo result = null;
    dashboardRequestDto.setSearchPeriodFrom(getSearchPeriodFromCode(dashboardRequestDto.getSearchPeriodSe()));
    // ========================================================================
    // # 처리
    // ========================================================================
    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------
    result = dashboardMapper.getSignUpStatics(dashboardRequestDto);
    resultResDto.setSignUpStatics(result);

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 대시보드-고객 문의 현황
   *
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  protected DashboardResponseDto getCustomerQnaStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DashboardService.getCustomerQnaStatics Start");
    log.debug("# ######################################");

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto resultResDto = new DashboardResponseDto();
    resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getMessage());
    DashboardVo result = null;
    dashboardRequestDto.setSearchPeriodFrom(getSearchPeriodFromCode(dashboardRequestDto.getSearchPeriodSe()));
    // ========================================================================
    // # 처리
    // ========================================================================
    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------
    result = dashboardMapper.getCustomerQnaStatics(dashboardRequestDto);
    DashboardVo outMallQna = dashboardMapper.getOutMallQnaStatics(dashboardRequestDto);
    result.setOutmall(outMallQna.getOutmall());
    result.setOutmallDelay(outMallQna.getOutmallDelay());

    resultResDto.setCustomerQnaStatics(result);

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 대시보드-보상제 처리 현황
   *
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  protected DashboardResponseDto getRewardApplyStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DashboardService.getRewardApplyStatics Start");
    log.debug("# ######################################");

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto resultResDto = new DashboardResponseDto();
    resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getMessage());
    DashboardVo result = null;
    dashboardRequestDto.setSearchPeriodFrom(getSearchPeriodFromCode(dashboardRequestDto.getSearchPeriodSe()));
    // ========================================================================
    // # 처리
    // ========================================================================
    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------
    result = dashboardMapper.getRewardApplyStatics(dashboardRequestDto);
    resultResDto.setRewardApplyStatics(result);

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 대시보드-부정거래 탐지
   *
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  protected DashboardResponseDto getIllegalDetectStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DashboardService.getIllegalDetectStatics Start");
    log.debug("# ######################################");

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto resultResDto = new DashboardResponseDto();
    resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getMessage());
    DashboardVo result = null;
    dashboardRequestDto.setSearchPeriodFrom(getSearchPeriodFromCode(dashboardRequestDto.getSearchPeriodSe()));
    // ========================================================================
    // # 처리
    // ========================================================================
    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------
    result = dashboardMapper.getIllegalDetectStatics(dashboardRequestDto);
    resultResDto.setIllegalDetectStatics(result);

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 대시보드-내 승인요청 현황
   *
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  protected DashboardResponseDto getMyApprovalRequestStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DashboardService.getMyApprovalRequestStatics Start");
    log.debug("# ######################################");

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto resultResDto = new DashboardResponseDto();
    resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getMessage());
    List<DashboardVo> rows = new ArrayList<>();
    // ----------------------------------------------------------------------
    // 세션정보 Set
    // ----------------------------------------------------------------------
    String sessionUserId = "";

    if (SessionUtil.getBosUserVO() != null) {
      sessionUserId = (SessionUtil.getBosUserVO()).getUserId();
    } else {
      sessionUserId = "0";
    }
    dashboardRequestDto.setUrUserId(sessionUserId);
    // ========================================================================
    // # 처리
    // ========================================================================
    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------
    rows = dashboardMapper.getMyApprovalRequestStatics(dashboardRequestDto);

    resultResDto.setMyApprovalRequestStatics(rows);

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 대시보드-내 승인처리 목록
   *
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  protected DashboardResponseDto getMyApprovalAcceptStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DashboardService.getMyApprovalAcceptStatics Start");
    log.debug("# ######################################");

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto resultResDto = new DashboardResponseDto();
    resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getMessage());
    DashboardVo result = new DashboardVo();
    // ----------------------------------------------------------------------
    // 세션정보 Set
    // ----------------------------------------------------------------------
    String sessionUserId = "";

    if (SessionUtil.getBosUserVO() != null) {
      sessionUserId = (SessionUtil.getBosUserVO()).getUserId();
    } else {
      sessionUserId = "0";
    }
    dashboardRequestDto.setUrUserId(sessionUserId);
    // ========================================================================
    // # 처리
    // ========================================================================
    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------
    result = dashboardMapper.getMyApprovalAcceptStatics(dashboardRequestDto);
    //테스트 데이터
    resultResDto.setMyApprovalAcceptStatics(result);

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }
  
  /**
   * 대시보드-담당자별 승인처리 현황
   *
   * @param dashboardRequestDto
   * @return
   * @throws BaseException
   */
  protected DashboardResponseDto getTotalApprovalAcceptStatics(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DashboardService.getTotalApprovalAcceptStatics Start");
    log.debug("# ######################################");

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto resultResDto = new DashboardResponseDto();
    resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getMessage());
    List<DashboardVo> rows = new ArrayList<>();
    // ========================================================================
    // # 처리
    // ========================================================================
    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------
    rows = dashboardMapper.getTotalApprovalAcceptStatics(dashboardRequestDto);
    resultResDto.setTotApprovalAcceptStatics(rows);

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }
  

  /**
   * search period code 날짜로 변환
   *
   * @param periodCode
   * @return
   */
  private LocalDateTime getSearchPeriodFromCode(String periodCode) {
    LocalDateTime now = LocalDateTime.now();
    switch (periodCode) {
      case "1H":
        return now.minusHours(1);
      case "3H":
        return now.minusHours(3);
      case "6H":
        return now.minusHours(6);
      case "12H":
        return now.minusHours(12);
      case "1D":
        return now.minusDays(1);
      default:
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
    }
  }

  /**
   * 대시보드 전체 모듈 초기화
   */
  protected DashboardResponseDto initDashboardList(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DashboardService.initDashboardList Start");
    log.debug("# ######################################");

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto resultResDto = new DashboardResponseDto();
    resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getMessage());
    List<DashboardVo> resultList = null;
    int resultInt = 0;

    // ========================================================================
    // # 처리
    // ========================================================================

    // ----------------------------------------------------------------------
    // 세션정보 Set
    // ----------------------------------------------------------------------
    String sessionUserId = "";

    if (SessionUtil.getBosUserVO() != null) {
      sessionUserId = (SessionUtil.getBosUserVO()).getUserId();
    } else {
      sessionUserId = "0";
    }

    if (dashboardRequestDto == null || dashboardRequestDto.getDashboardList() == null && dashboardRequestDto.getDashboardList().size() <= 0) {

      log.debug("# 대시보드 수정 대상이 없습니다.");
      resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_PUT_DATA.getCode());
      resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_PUT_DATA.getMessage());
      return resultResDto;
    }

    dashboardRequestDto.setUrUserId(sessionUserId);

    // 기존 대시보드 설정 삭제
    resultInt = dashboardMapper.delDashboardDefaultInfo(dashboardRequestDto);
    
    if(resultInt > 0) {
       // 대시보드 기본설정 모드로 처리
       dashboardMapper.addDashboardDefaultInfo(dashboardRequestDto);
    }
      // ========================================================================
      // # 반환
      // ========================================================================
      return resultResDto;
  }

  /**
   * 대시보드 모듈 숨기기 (삭제)
   */
  protected DashboardResponseDto delDashboardCard(DashboardRequestDto dashboardRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DashboardService.delDashboardCard Start");
    log.debug("# ######################################");

    // ========================================================================
    // # 초기화
    // ========================================================================
    DashboardResponseDto resultResDto = new DashboardResponseDto();
    resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_MANAGE_SUCCESS.getMessage());
    List<DashboardVo> resultList = null;
    int resultInt = 0;
    // ========================================================================
    // # 처리
    // ========================================================================

    // ----------------------------------------------------------------------
    // 세션정보 Set
    // ----------------------------------------------------------------------
    String sessionUserId = "";

    if (SessionUtil.getBosUserVO() != null) {
      sessionUserId = (SessionUtil.getBosUserVO()).getUserId();
    } else {
      sessionUserId = "0";
    }

    if (dashboardRequestDto.getUrUserDashboardId() == null) {

      log.debug("# 대시보드 수정 대상이 없습니다.");
      resultResDto.setResultCode(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_PUT_DATA.getCode());
      resultResDto.setResultMessage(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_PUT_DATA.getMessage());
      return resultResDto;
    }
      dashboardRequestDto.setUrUserId(sessionUserId);
      // 기존 대시보드 설정 개별 삭제
      dashboardMapper.delDashboardDefaultInfo(dashboardRequestDto);
      // ========================================================================
      // # 반환
      // ========================================================================
      return resultResDto;
  }
}
