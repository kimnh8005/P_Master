package kr.co.pulmuone.bos.dashboard;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.DashboardEnums;
import kr.co.pulmuone.v1.dashboard.dto.DashboardRequestDto;
import kr.co.pulmuone.v1.dashboard.dto.DashboardResponseDto;
import kr.co.pulmuone.v1.dashboard.service.DashboardBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
* <PRE>
* Forbiz Korea
* 대시보드 BOS Controller
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
@RestController
public class DashboardController {

  //@Autowired(required=true)
  //private HttpServletRequest request;

  @Autowired
  private DashboardBiz dashboardBiz;

  /**
   * 대시보드 리스트 조회
   * @param dashboardRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/dashboard/selectDashboardList")
  @ApiOperation(value = "대시보드리스트조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DashboardResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DASHBOARD_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectDashboardList(DashboardRequestDto dashboardRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# DashboardController.selectDashboardList Start");
    log.debug("# ######################################");
    if (dashboardRequestDto != null) {
      log.debug("# In.dashboardRequestDto     :: " + dashboardRequestDto.toString());
    }
    else {
      log.debug("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (dashboardRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_INPUT);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return dashboardBiz.selectDashboardList(dashboardRequestDto);
  }


  /**
   * 대시보드 수정
   * @param dashboardRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/dashboard/putDashboardList")
  @ApiOperation(value = "대시보드수정")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DashboardResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DASHBOARD_PARAM_NO_PUT_DATA - 대시보드 수정 대상이 없습니다."
    )
  })
  @ResponseBody
  public ApiResult<?> putDashboardList(@RequestBody DashboardRequestDto dashboardRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# DashboardController.putDashboardList Start");
    log.debug("# ######################################");
    if (dashboardRequestDto != null) {
      log.debug("# In.dashboardRequestDto     :: " + dashboardRequestDto.toString());
    }
    else {
      log.debug("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (dashboardRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_PUT_DATA);
    }

    // ------------------------------------------------------------------------
    // 조회조건 Set - filter
    // ------------------------------------------------------------------------

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return dashboardBiz.putDashboardList(dashboardRequestDto);
  }

  /**
   * 대시보드-주문/매출현황
   * @param dashboardRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/dashboard/getDashboardOrderSaleStatics")
  @ApiOperation(value = "대시보드-주문/매출현황")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DashboardResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                                                           + "DASHBOARD_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
    )
  })
  @ResponseBody
  public ApiResult<?> getDashboardOrderSaleStatics(@RequestBody DashboardRequestDto dashboardRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# DashboardController.getDashboardOrderSaleStatics Start");
    log.debug("# ######################################");
    if (dashboardRequestDto != null) {
      log.debug("# In.dashboardRequestDto     :: " + dashboardRequestDto.toString());
    }
    else {
      log.debug("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (dashboardRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_INPUT);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return dashboardBiz.getDashboardOrderSaleStatics(dashboardRequestDto);
  }

  /**
   * 대시보드-주문/매출현황
   * @param dashboardRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/dashboard/getDashboardClaimStatics")
  @ApiOperation(value = "대시보드-주문/매출현황")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DashboardResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                                                           + "DASHBOARD_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
    )
  })
  @ResponseBody
  public ApiResult<?> getDashboardClaimStatics(@RequestBody DashboardRequestDto dashboardRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# DashboardController.getDashboardClaimStatics Start");
    log.debug("# ######################################");
    if (dashboardRequestDto != null) {
      log.debug("# In.dashboardRequestDto     :: " + dashboardRequestDto.toString());
    }
    else {
      log.debug("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (dashboardRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_INPUT);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return dashboardBiz.getDashboardClaimStatics(dashboardRequestDto);
  }

  /**
   * 대시보드-회원가입 현황
   * @param dashboardRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/dashboard/getDashboardSignUpStatics")
  @ApiOperation(value = "대시보드-회원가입 현황")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DashboardResponseDto.class),
          @ApiResponse(code = 901, message = ""
                  + "DASHBOARD_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          )
  })
  @ResponseBody
  public ApiResult<?> getSignUpStatics(@RequestBody DashboardRequestDto dashboardRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# DashboardController.getSignUpStatics Start");
    log.debug("# ######################################");
    if (dashboardRequestDto != null) {
      log.debug("# In.dashboardRequestDto     :: " + dashboardRequestDto.toString());
    }
    else {
      log.debug("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (dashboardRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_INPUT);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return dashboardBiz.getSignUpStatics(dashboardRequestDto);
  }

  /**
   * 대시보드-고객 문의 현황
   * @param dashboardRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/dashboard/getDashboardCustomerQnaStatics")
  @ApiOperation(value = "대시보드-고객 문의 현황")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DashboardResponseDto.class),
          @ApiResponse(code = 901, message = ""
                  + "DASHBOARD_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          )
  })
  @ResponseBody
  public ApiResult<?> getCustomerQnaStatics(@RequestBody DashboardRequestDto dashboardRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# DashboardController.getCustomerQnaStatics Start");
    log.debug("# ######################################");
    if (dashboardRequestDto != null) {
      log.debug("# In.dashboardRequestDto     :: " + dashboardRequestDto.toString());
    }
    else {
      log.debug("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (dashboardRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_INPUT);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return dashboardBiz.getCustomerQnaStatics(dashboardRequestDto);
  }

  /**
   * 대시보드-보상제 처리 현황
   * @param dashboardRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/dashboard/getDashboardRewardApplyStatics")
  @ApiOperation(value = "대시보드-보상제 처리 현황")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DashboardResponseDto.class),
          @ApiResponse(code = 901, message = ""
                  + "DASHBOARD_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          )
  })
  @ResponseBody
  public ApiResult<?> getRewardApplyStatics(@RequestBody DashboardRequestDto dashboardRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# DashboardController.getRewardApplyStatics Start");
    log.debug("# ######################################");
    if (dashboardRequestDto != null) {
      log.debug("# In.dashboardRequestDto     :: " + dashboardRequestDto.toString());
    }
    else {
      log.debug("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (dashboardRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_INPUT);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return dashboardBiz.getRewardApplyStatics(dashboardRequestDto);
  }

  /**
   * 대시보드-부정거래 탐지
   * @param dashboardRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/dashboard/getDashboardIllegalDetectStatics")
  @ApiOperation(value = "대시보드-부정거래 탐지")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DashboardResponseDto.class),
          @ApiResponse(code = 901, message = ""
                  + "DASHBOARD_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          )
  })
  @ResponseBody
  public ApiResult<?> getDashboardIllegalDetectStatics(@RequestBody DashboardRequestDto dashboardRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# DashboardController.getDashboardIllegalDetectStatics Start");
    log.debug("# ######################################");
    if (dashboardRequestDto != null) {
      log.debug("# In.dashboardRequestDto     :: " + dashboardRequestDto.toString());
    }
    else {
      log.debug("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (dashboardRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_INPUT);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return dashboardBiz.getIllegalDetectStatics(dashboardRequestDto);
  }
  
  /**
   * 대시보드-내 승인요청 현황
   * @param dashboardRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/dashboard/getDashboardMyApprovalRequestStatics")
  @ApiOperation(value = "대시보드-내 승인요청 현황")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DashboardResponseDto.class),
          @ApiResponse(code = 901, message = ""
                  + "DASHBOARD_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          )
  })
  @ResponseBody
  public ApiResult<?> getDashboardApprovalRequestStatics(@RequestBody DashboardRequestDto dashboardRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# DashboardController.getDashboardApprovalRequestStatics Start");
    log.debug("# ######################################");
    if (dashboardRequestDto != null) {
      log.debug("# In.dashboardRequestDto     :: " + dashboardRequestDto.toString());
    }
    else {
      log.debug("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (dashboardRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_INPUT);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return dashboardBiz.getMyApprovalRequestStatics(dashboardRequestDto);
  }


  /**
   * 대시보드-내 승인처리 목록
   * @param dashboardRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/dashboard/getDashboardMyApprovalAcceptStatics")
  @ApiOperation(value = "대시보드-내 승인처리 목록")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DashboardResponseDto.class),
          @ApiResponse(code = 901, message = ""
                  + "DASHBOARD_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          )
  })
  @ResponseBody
  public ApiResult<?> getDashboardMyApprovalAcceptStatics(@RequestBody DashboardRequestDto dashboardRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# DashboardController.getDashboardMyApprovalAcceptStatics Start");
    log.debug("# ######################################");
    if (dashboardRequestDto != null) {
      log.debug("# In.dashboardRequestDto     :: " + dashboardRequestDto.toString());
    }
    else {
      log.debug("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (dashboardRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_INPUT);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return dashboardBiz.getMyApprovalAcceptStatics(dashboardRequestDto);
  }

  /**
   * 대시보드-담당자별 승인처리 현황
   * @param dashboardRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/dashboard/getDashboardTotalApprovalAcceptStatics")
  @ApiOperation(value = "대시보드-담당자별 승인처리 현황")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DashboardResponseDto.class),
          @ApiResponse(code = 901, message = ""
                  + "DASHBOARD_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          )
  })
  @ResponseBody
  public ApiResult<?> getDashboardTotalApprovalAcceptStatics(@RequestBody DashboardRequestDto dashboardRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# DashboardController.getDashboardTotalApprovalAcceptStatics Start");
    log.debug("# ######################################");
    if (dashboardRequestDto != null) {
      log.debug("# In.dashboardRequestDto     :: " + dashboardRequestDto.toString());
    }
    else {
      log.debug("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (dashboardRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_INPUT);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return dashboardBiz.getTotalApprovalAcceptStatics(dashboardRequestDto);
  }
  
   /**
   * 대시보드 모듈 전체 초기화
   * @param dashboardRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/dashboard/initDashboardList")
  @ApiOperation(value = "대시보드 모듈 전체 초기화")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DashboardResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DASHBOARD_PARAM_NO_PUT_DATA - 대시보드 수정 대상이 없습니다."
    )
  })
  @ResponseBody
  public ApiResult<?> initDashboardList(@RequestBody DashboardRequestDto dashboardRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# DashboardController.initDashboardList Start");
    log.debug("# ######################################");
    if (dashboardRequestDto != null) {
      log.debug("# In.dashboardRequestDto     :: " + dashboardRequestDto.toString());
    }
    else {
      log.debug("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (dashboardRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_PUT_DATA);
    }

    // ------------------------------------------------------------------------
    // 조회조건 Set - filter
    // ------------------------------------------------------------------------

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return dashboardBiz.initDashboardList(dashboardRequestDto);
  }
  
     /**
   * 대시보드 모듈 숨기기 (삭제)
   * @param dashboardRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/dashboard/delDashboardCard")
  @ApiOperation(value = "대시보드 모듈 숨기기 (삭제)")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DashboardResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DASHBOARD_PARAM_NO_PUT_DATA - 대시보드 수정 대상이 없습니다."
    )
  })
  @ResponseBody
  public ApiResult<?> delDashboardCard(@RequestBody DashboardRequestDto dashboardRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# DashboardController.delDashboardCard Start");
    log.debug("# ######################################");
     if (dashboardRequestDto != null) {
      log.debug("# In.dashboardRequestDto     :: " + dashboardRequestDto.toString());
    }
    else {
      log.debug("# In.dashboardRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (dashboardRequestDto.getUrUserDashboardId() == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(DashboardEnums.DashboardMessage.DASHBOARD_PARAM_NO_PUT_DATA);
    }

    // ------------------------------------------------------------------------
    // 조회조건 Set - filter
    // ------------------------------------------------------------------------

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return dashboardBiz.delDashboardCard(dashboardRequestDto);
  }

}
