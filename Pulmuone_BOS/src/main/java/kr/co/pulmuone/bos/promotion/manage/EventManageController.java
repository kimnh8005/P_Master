package kr.co.pulmuone.bos.promotion.manage;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.EventEnums;
import kr.co.pulmuone.v1.comm.enums.EventEnums.EventMessage;

import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.promotion.manage.dto.EventManageRequestDto;
import kr.co.pulmuone.v1.promotion.manage.dto.EventManageResponseDto;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EventJoinVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EventVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EventGroupDetlVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EventGroupVo;
import kr.co.pulmuone.v1.promotion.manage.service.EventManageBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//import kr.co.pulmuone.v1.comm.enums.ExhibitEnums.GiftShippingTp;
//import kr.co.pulmuone.v1.comm.enums.ExhibitEnums.UndeliverableAreaTp;

/**
* <PRE>
* Forbiz Korea
* 프로모션-이벤트관리 BOS Controller
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0       2021.12.12.              dgyoun         최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@RestController
public class EventManageController {

  final String JOB_ADD = "ADD";
  final String JOB_PUT = "PUT";

  //@SuppressWarnings("unused")
  //@Autowired(required = true)
  //private HttpServletRequest request;

  @Autowired(required=true)
  private HttpServletRequest request;

  @Autowired
  private EventManageBiz eventManageBiz;

  @Autowired
  private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

  //@Autowired
  //private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 조회 - 이벤트
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트 리스트조회
   * @param eventManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/event/selectEventList")
  @ApiOperation(value = "이벤트리스트조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = EventManageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "EVENT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectEventList(EventManageRequestDto eventManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.selectEventList Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto     :: " + eventManageRequestDto.toString());
      log.debug("# Pg.page     :: " + eventManageRequestDto.getPage());
      log.debug("# Pg.sPage    :: " + eventManageRequestDto.getsPage());
      log.debug("# Pg.ePage    :: " + eventManageRequestDto.getePage());
      log.debug("# Pg.pageSize :: " + eventManageRequestDto.getPageSize());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (eventManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_INPUT);
    }

    // ------------------------------------------------------------------------
    // 조회조건 Set - filter
    // ------------------------------------------------------------------------
    // 조회조건 filter
    EventManageRequestDto reqDto  = (EventManageRequestDto) BindUtil.convertRequestToObject(request, EventManageRequestDto.class);
    log.debug("# EventManageController.reqDto[1] :: " + reqDto.toString());

    // 키워드
    if(StringUtil.isEquals(reqDto.getSearchSe(), "NAME")) {
      // 이벤트명
      reqDto.setTitle(StringUtil.nvl(reqDto.getKeyWord()));
    }
    else if(StringUtil.isEquals(reqDto.getSearchSe(), "ID")) {
      // 이벤트ID
      reqDto.setEvEventId(StringUtil.nvl(reqDto.getKeyWord()));
    }

    // 코드값에 ALL 문자열이 포함되어 있어서  indexOf("ALL") != 0 으로 둠, 이 경우 ALL은 항상 맨 먼저 나타나게 해야 함
    // 참고
    //  1) indexOf("ALL") < 0 => ALL 이 아닌 경우
    //  2) indexOf("ALL") > 0 => ALL 이 아니지만, MALL_DIV에서 ALL을 인식한 경우
    // 몰구분(ListString -> List<String>)
    if(StringUtil.isNotEmpty(reqDto.getMallDiv()) && reqDto.getMallDiv().indexOf("ALL") != 0 ) {

      reqDto.setMallDivList(Stream.of(reqDto.getMallDiv().split(Constants.ARRAY_SEPARATORS))
                                  .map(String::trim)
                                  .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                  .collect(Collectors.toList()));
    }

    // 진행상태
    if(StringUtil.isNotEmpty(reqDto.getStatusSe()) && reqDto.getStatusSe().indexOf("ALL") < 0 ) {

      reqDto.setStatusSeList(Stream.of(reqDto.getStatusSe().split(Constants.ARRAY_SEPARATORS))
                                     .map(String::trim)
                                     .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                     .collect(Collectors.toList()));
    }
    // 진행상태 : List와 개별 변수에 다 Set 함
    if(StringUtil.isNotEmpty(reqDto.getStatusSe()) && reqDto.getStatusSe().indexOf("ALL") < 0 ) {
      // 진행상태-진행예정
      if (reqDto.getStatusSe().indexOf("WAIT") >= 0) {
        reqDto.setStatusWait("Y");
      }
      // 진행상태-진행중
      if (reqDto.getStatusSe().indexOf("ING") >= 0) {
        reqDto.setStatusIng("Y");
      }
      // 진행상태-진행완료
      if (reqDto.getStatusSe().indexOf("END") >= 0) {
        reqDto.setStatusEnd("Y");
      }
    }

    // 접근권한설정유형
    if(StringUtil.isNotEmpty(reqDto.getUserGroupFilter())) {
      reqDto.setUserGroupIdList(Stream.of(reqDto.getUserGroupFilter().split(Constants.ARRAY_SEPARATORS))
                                    .map(String::trim)
                                    .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                    .collect(Collectors.toList()));
    }
    //if(StringUtil.isNotEmpty(reqDto.getEvGroupTp()) && reqDto.getEvGroupTp().indexOf("ALL") < 0 ) {
    //
    //  reqDto.setEvGroupTpList(Stream.of(reqDto.getEvGroupTp().split(Constants.ARRAY_SEPARATORS))
    //      .map(String::trim)
    //      .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
    //      .collect(Collectors.toList()));
    //}

    // 노출범위(디바이스)
    if(StringUtil.isNotEmpty(reqDto.getGoodsDisplayType()) && reqDto.getGoodsDisplayType().indexOf("ALL") < 0 ) {

      reqDto.setGoodsDisplayTypeList(Stream.of(reqDto.getGoodsDisplayType().split(Constants.ARRAY_SEPARATORS))
                                           .map(String::trim)
                                           .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                           .collect(Collectors.toList()));
    }
    // 노출범위(디바이스) : List와 개별 변수에 다 Set 함
    if(StringUtil.isNotEmpty(reqDto.getGoodsDisplayType()) && reqDto.getGoodsDisplayType().indexOf("ALL") < 0 ) {
      // 노출범위(디바이스)-PC Web
      if (reqDto.getGoodsDisplayType().indexOf("GOODS_DISPLAY_TYPE.WEB_PC") >= 0) {
        reqDto.setDispWebPcYn("Y");
      }
      // 노출범위(디바이스)-M Web
      if (reqDto.getGoodsDisplayType().indexOf("GOODS_DISPLAY_TYPE.WEB_MOBILE") >= 0) {
        reqDto.setDispWebMobileYn("Y");
      }
      // 노출범위(디바이스)-APP
      if (reqDto.getGoodsDisplayType().indexOf("GOODS_DISPLAY_TYPE.APP") >= 0) {
        reqDto.setDispAppYn("Y");
      }
    }

    // 임직원 전용 여부
    if(StringUtil.isNotEmpty(reqDto.getEvEmployeeTp()) && reqDto.getEvEmployeeTp().indexOf("ALL") < 0 ) {

      reqDto.setEvEmployeeTpList(Stream.of(reqDto.getEvEmployeeTp().split(Constants.ARRAY_SEPARATORS))
                                       .map(String::trim)
                                       .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                                       .collect(Collectors.toList()));
    }

    // 이벤트유형
    if(StringUtil.isNotEmpty(reqDto.getEventTp()) && reqDto.getEventTp().indexOf("ALL") < 0 ) {

      reqDto.setEventTpList(Stream.of(reqDto.getEventTp().split(Constants.ARRAY_SEPARATORS))
          .map(String::trim)
          .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
          .collect(Collectors.toList()));
    }

    // 진행기간-시작시작일자
    if (StringUtil.isNotEmpty(reqDto.getStartBeginDt())) {
      reqDto.setStartBeginDt(reqDto.getStartBeginDt() + "000000");
    }
    // 진행기간-시작종료일자
    if (StringUtil.isNotEmpty(reqDto.getStartFinishDt())) {
      reqDto.setStartFinishDt(reqDto.getStartFinishDt() + "235959");
    }
    // 진행기간-종료시작일자
    if (StringUtil.isNotEmpty(reqDto.getEndBeginDt())) {
      reqDto.setEndBeginDt(reqDto.getEndBeginDt() + "000000");
    }
    // 진행기간-종료종료일자
    if (StringUtil.isNotEmpty(reqDto.getEndFinishDt())) {
      reqDto.setEndFinishDt(reqDto.getEndFinishDt() + "235959");
    }
    log.debug("# EventManageController.reqDto[2] :: " + reqDto.toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return eventManageBiz.selectEventList(reqDto);

  }

  /**
   * 이벤트 상세조회 - 일반(그룹) - 그룹리스트
   * @param eventManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/event/selectfEventGroupList")
  @ApiOperation(value = "이벤트상세조회-일반-그룹리스트")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = EventManageResponseDto.class),
          @ApiResponse(code = 901, message = ""
                  + "EVENT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                  + "EVENT_MANAGE_PARAM_NO_EVENT_ID - 이벤트ID를 입력하세요."
          )
  })
  @ResponseBody
  public ApiResult<?> selectfEventGroupList(EventManageRequestDto eventManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.selectfEventGroupList Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto     :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (eventManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getEvEventId())) {
      // 이벤트ID를 입력하세요.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_EVENT_ID);
    }
    log.debug("# In.evEventId :: " + eventManageRequestDto.getEvEventId());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return eventManageBiz.selectfEventGroupList(eventManageRequestDto);

  }

  /**
   * 이벤트 상세조회 - 일반(그룹) - 그룹상품리스트
   * @param eventManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/event/selectfEventGroupGoodsList")
  @ApiOperation(value = "이벤트상세조회-일반-그룹상품리스트")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = EventManageResponseDto.class),
          @ApiResponse(code = 901, message = ""
                  + "EVENT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                  + "EVENT_MANAGE_PARAM_NO_EVENT_ID - 그룹ID를 입력하세요."
          )
  })
  @ResponseBody
  public ApiResult<?> selectfEventGroupGoodsList(EventManageRequestDto eventManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.selectfEventGroupGoodsList Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto     :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (eventManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getEvEventGroupId())) {
      // 그룹ID를 입력하세요.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_EVENT_GROUPT_ID);
    }
    log.debug("# In.evEventGroupId :: " + eventManageRequestDto.getEvEventGroupId());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return eventManageBiz.selectfEventGroupGoodsList(eventManageRequestDto);

  }

  /**
   * 상품정보리스트조회(엑셀용)
   * @param eventManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/event/selectGoodsInfoList")
  @ApiOperation(value = "이벤트상세조회-증정행사-적용브랜드리스트")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = EventManageResponseDto.class),
          @ApiResponse(code = 901, message = ""
                  + "EVENT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                  + "EVENT_MANAGE_PARAM_NO_GROUP_GOODS_ID_LIST - 상품코드정보를 확인하세요."
          )
  })
  @ResponseBody
  public ApiResult<?> selectGoodsInfoList(EventManageRequestDto eventManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.selectGoodsInfoList Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto     :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (eventManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_INPUT);
    }

    // ilGoodsId JsonString -> List<String> 으로 변환하여  eventManageRequestDto.setIlGoodsIdList 에 넣음
    try {
      eventManageRequestDto.setIlGoodsIdList(BindUtil.convertJsonArrayToDtoList(eventManageRequestDto.getIlGoodsIdListString(), String.class));
    }
    catch (Exception e) {
      //e.printStackTrace();
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_GROUP_GOODS_ID_LIST);
    }
    log.debug("# In.ilGoodsIdList :: " + eventManageRequestDto.getIlGoodsIdList().toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return eventManageBiz.selectGoodsInfoList(eventManageRequestDto);

  }

  /**
   * 이벤트 상세조회
   * @param eventManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/event/selectEventInfo")
  @ApiOperation(value = "이벤트상세조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = EventManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EVENT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          + "EVENT_MANAGE_PARAM_NO_EVENT_ID - 이벤트ID를 입력하세요."
          )
  })
  @ResponseBody
  public ApiResult<?> selectEventInfo(EventManageRequestDto eventManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.selectEventInfo Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto     :: " + eventManageRequestDto.toString());
      log.debug("# Pg.evEventId :: " + eventManageRequestDto.getEvEventId());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (eventManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getEvEventId())) {
      // 이벤트ID를 입력하세요.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_EVENT_ID);
    }
    log.debug("# In.evEventId :: " + eventManageRequestDto.getEvEventId());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return eventManageBiz.selectEventInfo(eventManageRequestDto);

  }





  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 조회 - 이벤트참여
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트참여 리스트조회
   * @param eventManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/event/selectEventJoinList")
  @ApiOperation(value = "이벤트참여리스트조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = EventManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EVENT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          )
  })
  @ResponseBody
  public ApiResult<?> selectEventJoinList(EventManageRequestDto eventManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.selectEventJoinList Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto     :: " + eventManageRequestDto.toString());
      log.debug("# Pg.page     :: " + eventManageRequestDto.getPage());
      log.debug("# Pg.sPage    :: " + eventManageRequestDto.getsPage());
      log.debug("# Pg.ePage    :: " + eventManageRequestDto.getePage());
      log.debug("# Pg.pageSize :: " + eventManageRequestDto.getPageSize());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (eventManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_INPUT);
    }


    // ------------------------------------------------------------------------
    // 조회조건 Set - filter
    // ------------------------------------------------------------------------
    //EventManageRequestDto reqDto = (EventManageRequestDto) BindUtil.convertRequestToObject(request, EventManageRequestDto.class);
    //log.debug("# EventManageController.reqDto[1] :: " + reqDto.toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return eventManageBiz.selectEventJoinList(eventManageRequestDto);

  }

  /**
   * 이벤트참여 설문항목 리스트조회
   * @param eventManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/event/selectEventJoinSurveyList")
  @ApiOperation(value = "이벤트참여설문항목리스트조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = EventManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EVENT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          + "EVENT_MANAGE_PARAM_NO_EVENT_ID - 이벤트ID를 입력하세요."
          )
  })
  @ResponseBody
  public ApiResult<?> selectEventJoinSurveyList(EventManageRequestDto eventManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.selectEventJoinSurveyList Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto     :: " + eventManageRequestDto.toString());
      //log.debug("# Pg.page     :: " + eventManageRequestDto.getPage());
      //log.debug("# Pg.sPage    :: " + eventManageRequestDto.getsPage());
      //log.debug("# Pg.ePage    :: " + eventManageRequestDto.getePage());
      //log.debug("# Pg.pageSize :: " + eventManageRequestDto.getPageSize());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (eventManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getEvEventId())) {
      // 이벤트ID를 입력하세요.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_EVENT_ID);
    }

    // ------------------------------------------------------------------------
    // 조회조건 Set - filter
    // ------------------------------------------------------------------------
    //EventManageRequestDto reqDto = (EventManageRequestDto) BindUtil.convertRequestToObject(request, EventManageRequestDto.class);
    //log.debug("# EventManageController.reqDto[1] :: " + reqDto.toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return eventManageBiz.selectEventJoinSurveyList(eventManageRequestDto);

  }

  /**
   * 이벤트참여 설문항목참여 리스트조회
   * @param eventManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/event/selectEventJoinSurveyItemJoinList")
  @ApiOperation(value = "이벤트참여설문항목참여리스트조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = EventManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EVENT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          + "EVENT_MANAGE_PARAM_NO_EVENT_ID - 이벤트ID를 입력하세요."
          )
  })
  @ResponseBody
  public ApiResult<?> selectEventJoinSurveyItemJoinList(EventManageRequestDto eventManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.selectEventJoinSurveyItemJoinList Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto     :: " + eventManageRequestDto.toString());
      //log.debug("# Pg.page     :: " + eventManageRequestDto.getPage());
      //log.debug("# Pg.sPage    :: " + eventManageRequestDto.getsPage());
      //log.debug("# Pg.ePage    :: " + eventManageRequestDto.getePage());
      //log.debug("# Pg.pageSize :: " + eventManageRequestDto.getPageSize());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (eventManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getEvEventId())) {
      // 이벤트ID를 입력하세요.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_EVENT_ID);
    }

    // ------------------------------------------------------------------------
    // 조회조건 Set - filter
    // ------------------------------------------------------------------------
    //EventManageRequestDto reqDto = (EventManageRequestDto) BindUtil.convertRequestToObject(request, EventManageRequestDto.class);
    //log.debug("# EventManageController.reqDto[1] :: " + reqDto.toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return eventManageBiz.selectEventJoinSurveyItemJoinList(eventManageRequestDto);


  }

  /**
   * 엑셀다운로드-이벤트참여-참여자목록/당첨자목록
   * @param dto
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/admin/pm/event/getExportExcelEventJoinList")
  @ApiOperation(value = "참여자/당첨자엑셀다운로드")
  @SuppressWarnings({ "unchecked", "unused" })
  public ModelAndView getExportExcelEventJoinList(@RequestBody EventManageRequestDto eventManageRequestDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.getExportExcelEventJoinList Start");
    log.debug("# ######################################");
    log.debug("# eventManageRequestDto :: " + eventManageRequestDto.toString());
    log.debug("# request.evEventId  :: " + request.getParameter("evEventId"));
    log.debug("# request.winnerYn   :: " + request.getParameter("winnerYn"));

    //String evEventId  = StringUtil.nvl(request.getParameter("evEventId"));
    //String winnerYn   = StringUtil.nvl(request.getParameter("winnerYn"));
    String evEventId   = eventManageRequestDto.getEvEventId();
    String winnerYn    = eventManageRequestDto.getWinnerYn();
    String eventNm     = "";
    String listNm      = "";

    // ------------------------------------------------------------------------
    // 1. 이벤트상세조회
    // ------------------------------------------------------------------------
    eventManageRequestDto.setEvEventId(evEventId);
    ApiResult<?> resultApi = eventManageBiz.selectEventInfo(eventManageRequestDto);
    //log.debug("# resultApi :: " + resultApi.toString());

    if (resultApi != null) {

      EventManageResponseDto eventManageResponseDto = (EventManageResponseDto)resultApi.getData();
      //log.debug("# getData :: " + eventManageResponseDto.toString());

      if (eventManageResponseDto != null) {
        EventVo resultDetailInfo = (EventVo)eventManageResponseDto.getEventInfo();

        if (resultDetailInfo != null) {
          eventNm = resultDetailInfo.getTitle();
          //log.debug("# 이벤트명 :: " + eventNm);
        }

      } // End of if (eventManageResponseDto != null)

    } // End of if (resultApi != null)

    // 리스트종류명
    if (StringUtil.isEquals(winnerYn, "Y")) {
      // 당첨자
      listNm = "당첨자목록";
    }
    else {
      // 참여자
      listNm = "참여자목록";
    }

    // ------------------------------------------------------------------------
    // 2. 엑셀 레이아웃 설정
    // ------------------------------------------------------------------------
    // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
    String excelFileName   = eventNm + "_" + listNm + "_" + DateUtil.getCurrentDate();
    // 엑셀 파일 내 워크시트 이름
    String excelSheetName  = "sheet";

    //log.debug("# excelFileName :: " + excelFileName);

    /*
     * 컬럼별 width 목록 : 단위 pixel
     * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
     */
    // NO, 이벤트ID, 이벤트명, 이벤트유형, ID, 이름, 회원등급, 임직원여부, 참여제한, 가입일, 휴대폰, 우편번호, 기본 주소, 상세주소, 신청일, 신청내용-분류명, 신청내용-댓글내용, 당첨여부, 당첨방법, 당첨내용, 메모
    Integer[] widthListOfFirstWorksheet = {50, 150, 150, 200, 150, 150, 150, 150, 150, 200, 200, 100, 400, 400, 200, 300, 300, 100, 150, 300, 300};

    /*
     * 본문 데이터 컬럼별 정렬 목록
     * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
     * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
     */
    String[] alignListOfFirstWorksheet = {"center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center"};


    /*
     * 본문 데이터 컬럼별 데이터 property 목록
     * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
     */
    String[] propertyListOfFirstWorksheet = {"no", "evEventId", "title", "eventTpNm", "loginId", "userNm", "groupNm", "employeeYnNm", "eventJoinYn", "regDt", "mobile", "receiverZipCd", "receiverAddr1", "receiverAddr2", "reqDe", "reqConts1", "reqConts2", "winnerYn", "handwrittenLotteryName", "winnerContsOnly", "winnerContsMemo"};

    // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
    // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
    String[] firstHeaderListOfFirstWorksheet = {"No", "이벤트ID", "이벤트명", "이벤트유형", "ID", "이름", "회원등급", "임직원여부", "참여제한", "가입일", "휴대폰", "우편번호", "기본 주소", "상세 주소", "신청일", "신청내용1", "신청내용2", "당첨여부", "당첨방법", "당첨종류", "당첨내용"};

    // 워크시트 DTO 생성 후 정보 세팅
    ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()   //
        .workSheetName(excelSheetName)                                  // 엑셀 파일내 워크시트 명
        .propertyList(propertyListOfFirstWorksheet)                     // 컬럼별 데이터 property 목록
        .widthList(widthListOfFirstWorksheet)                           // 컬럼별 너비 목록
        .alignList(alignListOfFirstWorksheet)                           // 컬럼별 정렬 목록
        .build();

    // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
    // 첫 번째 헤더 컬럼
    firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

    /*
     * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능
     * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
     */
    //ApiResult<?> resultApi = null;
    List<EventJoinVo> resultList = new ArrayList<EventJoinVo>();;

    try {

//      String gbUrWarehouseId        = "";
//      String gbStoreYn              = "";
//      String gbIlShippingTmplId     = "";
//      String gbUndeliverableAreaTp  = "";
//      String targetEnableYn         = "";


      // ----------------------------------------------------------------------
      // 3. 참여자/당첨자 리스트조회
      // ----------------------------------------------------------------------
      eventManageRequestDto.setEvEventId(evEventId);
      eventManageRequestDto.setWinnerYn(winnerYn);      // Y: 당첨자, 그외 : 참여자
      eventManageRequestDto.setExcelYn("Y");            // 엑셀출력

      resultApi = eventManageBiz.selectExcelEventJoinList(eventManageRequestDto);

      if (resultApi != null) {

        EventManageResponseDto eventManageResponseDto = (EventManageResponseDto)resultApi.getData();

        if (eventManageResponseDto != null) {

          List<EventJoinVo>  resultJoinList = (List<EventJoinVo>)eventManageResponseDto.getRows();

          if (resultJoinList != null && resultJoinList.size() > 0) {

            int i = 0;

            for (EventJoinVo vo : resultJoinList) {

              i++;
              vo.setNo(i);
              resultList.add(vo);
            }

          } // End of if (resultJoinList != null && resultJoinList.size() > 0)

        } // End of if (eventManageResponseDto != null)

      } // End of if (resultApi != null)

    } catch (Exception e) {
        log.error(e.getMessage());
        throw e; // 추후 CustomException 으로 변환 예정
    }
    firstWorkSheetDto.setExcelDataList(resultList);

    // xcelDownloadDto 생성 후 workSheetDto 추가
    ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder()  //
        //.excelFileName(URLEncoder.encode(excelFileName, "UTF-8"))   //
        .excelFileName(excelFileName)   //
        .build();

    excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

    ModelAndView modelAndView = new ModelAndView(excelDownloadView);
    modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

    return modelAndView;
  }

  /**
   * 엑셀다운로드-이벤트참여-직접입력 리스트조회
   * @param dto
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/admin/pm/event/getExportExcelEventJoinDirectJoinList")
  @ApiOperation(value = "직접입력리스트엑셀다운로드")
  @SuppressWarnings({ "unchecked", "unused" })
  public ModelAndView getExportExcelEventJoinDirectJoinList(@RequestBody EventManageRequestDto eventManageRequestDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.getExportExcelEventJoinDirectJoinList Start");
    log.debug("# ######################################");
    log.debug("# eventManageRequestDto :: " + eventManageRequestDto.toString());
    log.debug("# request.evEventId  :: " + request.getParameter("evEventSurveyQuestionId"));

    String evEventSurveyQuestionId  = eventManageRequestDto.getEvEventSurveyQuestionId();
    String eventNm     = "";

    // ------------------------------------------------------------------------
    // 2. 엑셀 레이아웃 설정
    // ------------------------------------------------------------------------
    // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
    String excelFileName   = eventNm + "_설문" + evEventSurveyQuestionId + "_" + DateUtil.getCurrentDate();
    // 엑셀 파일 내 워크시트 이름
    String excelSheetName  = "sheet";

    /*
     * 컬럼별 width 목록 : 단위 pixel
     * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
     */
    // NO, 이벤트ID, 이벤트명, 이벤트유형, ID, 이름, 회원등급, 임직원여부, 참여제한, 가입일, 이메일, 휴대폰, 신청일, 신청내용1-설문명, 신청내용2-직접입력, 당첨여부, 당첨내용, 메모
    Integer[] widthListOfFirstWorksheet = {50, 150, 150, 200, 150, 150, 150, 200, 200, 200, 200, 300, 300, 100, 300, 300};

    /*
     * 본문 데이터 컬럼별 정렬 목록
     * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
     * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
     */
    String[] alignListOfFirstWorksheet = {"center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center"};


    /*
     * 본문 데이터 컬럼별 데이터 property 목록
     * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
     */
    String[] propertyListOfFirstWorksheet = {"no", "evEventId", "title", "eventTpNm", "loginId", "userNm", "eventJoinYn", "regDt", "mail", "mobile", "reqDt", "title", "otherCmnt", "winnerYn", "winnerContsOnly", "winnerContsMemo"};

    // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
    // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
    String[] firstHeaderListOfFirstWorksheet = {"No", "이벤트ID", "이벤트명", "이벤트유형", "ID", "이름", "회원등급", "가입일", "이메일", "휴대폰", "신청일", "신청내용1", "신청내용2", "당첨여부", "당첨종류", "당첨내용"};

    // 워크시트 DTO 생성 후 정보 세팅
    ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()   //
        .workSheetName(excelSheetName)                                  // 엑셀 파일내 워크시트 명
        .propertyList(propertyListOfFirstWorksheet)                     // 컬럼별 데이터 property 목록
        .widthList(widthListOfFirstWorksheet)                           // 컬럼별 너비 목록
        .alignList(alignListOfFirstWorksheet)                           // 컬럼별 정렬 목록
        .build();

    // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
    // 첫 번째 헤더 컬럼
    firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

    /*
     * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능
     * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
     */
    ApiResult<?> resultApi = null;
    List<EventJoinVo> resultList = new ArrayList<EventJoinVo>();;

    try {


      // ----------------------------------------------------------------------
      // 3. 참여자/당첨자 리스트조회
      // ----------------------------------------------------------------------
      resultApi = eventManageBiz.selectEventJoinDirectJoinList(eventManageRequestDto);

      if (resultApi != null) {

        EventManageResponseDto eventManageResponseDto = (EventManageResponseDto)resultApi.getData();

        if (eventManageResponseDto != null) {

          List<EventJoinVo>  resultJoinList = (List<EventJoinVo>)eventManageResponseDto.getRows();

          if (resultJoinList != null && resultJoinList.size() > 0) {

            int i = 0;

            for (EventJoinVo vo : resultJoinList) {

              if (i == 0) {
                eventNm = vo.getEventTitle();
              }

              i++;
              vo.setNo(i);
              resultList.add(vo);
            }

          } // End of if (resultJoinList != null && resultJoinList.size() > 0)

        } // End of if (eventManageResponseDto != null)

      } // End of if (resultApi != null)

    } catch (Exception e) {
      log.error(e.getMessage());
      throw e; // 추후 CustomException 으로 변환 예정
    }
    firstWorkSheetDto.setExcelDataList(resultList);

    // xcelDownloadDto 생성 후 workSheetDto 추가
    ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder()  //
        //.excelFileName(URLEncoder.encode(excelFileName, "UTF-8"))   //
        .excelFileName(eventNm + excelFileName)   //
        .build();

    excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

    ModelAndView modelAndView = new ModelAndView(excelDownloadView);
    modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

    return modelAndView;
  }



  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 삭제
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트 삭제
   * @param eventManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/event/delEvent")
  @ApiOperation(value = "이벤트삭제")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = EventManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EVENT_MANAGE_EVENT_DEL_FAIL_INPUT_TARGET - 이벤트 기본 정보삭제 입력정보 오류입니다."
          + "EVENT_MANAGE_EVENT_DEL_FAIL - 이벤트 기본 정보삭제 오류입니다."
          + "EVENT_MANAGE_EVENT_DEL_FAIL_PROC - 이벤트 기본 정보삭제 처리 오류입니다."
          )
  })
  @ResponseBody
  public ApiResult<?> delEvent(EventManageRequestDto eventManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.delEvent Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto     :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (eventManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getEvEventIdListString())) {

      // 이벤트 기본 정보삭제 입력정보 오류입니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_EVENT_DEL_FAIL_INPUT_TARGET);
    }

    // evEventId JsonString -> List<String> 으로 변환하여  eventManageRequestDto.setEvEventIdList 에 넣음
    try {
      eventManageRequestDto.setEvEventIdList(BindUtil.convertJsonArrayToDtoList(eventManageRequestDto.getEvEventIdListString(), String.class));
    }
    catch (Exception e) {
      //e.printStackTrace();
      return ApiResult.result(EventMessage.EVENT_MANAGE_EVENT_DEL_FAIL_INPUT_TARGET_CONVERT);
    }
    log.debug("# In.evEventIdList :: " + eventManageRequestDto.getEvEventIdList().toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return eventManageBiz.delEvent(eventManageRequestDto);

  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 등록
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트 등록
   * @param eventManageRequestDto
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unused")
  @RequestMapping(value="/admin/pm/event/addEvent")
  @ApiOperation(value = "이벤트등록")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = EventManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EVENT_MANAGE_PARAM_NO_INPUT                    - 이벤트 등록처리 오류입니다."
          + "EVENT_MANAGE_PARAM_NO_EVENT_TP                 - 이벤트유형을 확인하세요."
          + "EVENT_MANAGE_EVENT_ADD_FAIL_INPUT_TARGET       - 이벤트 기본정보등록 입력정보 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_PROC_FAIL               - 이벤트 등록처리 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_BASIC_FAIL              - 이벤트 기본정보등록 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_USER_GROUP_FAIL         - 이벤트 접근권한설정 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_NORMAL_PROC_FAIL        - 일반이벤트 등록처리 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_NORMAL_FAIL             - 일반이벤트 상세정보등록 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_NORMAL_COMMENT_FAIL     - 일반이벤트 댓글구분등록 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_NORMAL_COUPON_FAIL      - 일반이벤트 쿠폰정보등록 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_SURVEY_PROC_FAIL        - 설문이벤트 등록처리 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_SURVEY_FAIL             - 설문이벤트 상세정보등록 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_SURVEY_COUPON_FAIL      - 설문이벤트 쿠폰정보등록 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_SURVEY_QUESTION_FAIL    - 설문이벤트 설문항목등록 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_SURVEY_ITEM_FAIL        - 설문이벤트 설문항목아이템등록 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_SURVEY_ITEM_ATTC_FAIL   - 설문이벤트 설문항목아이템첨부파일등록 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_STAMP_PROC_FAIL         - 스탬프이벤트 등록처리 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_STAMP_FAIL              - 스탬프이벤트 상세정보등록 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_STAMP_STARMP_FAIL       - 스탬프이벤트 스탬프정보등록 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_ROULETTE_PROC_FAIL      - 룰렛이벤트 등록처리 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_ROULETTE_FAIL           - 룰렛이벤트 상세정보등록 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_ROULETTE_ITEM_FAIL      - 룰렛이벤트 이벤트아이템정보등록 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_EXPERIENCE_PROC_FAIL    - 체험단이벤트 등록처리 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_EXPERIENCE_FAIL         - 체험단이벤트 상세정보등록 오류입니다."
          + "EVENT_MANAGE_EVENT_ADD_EXPERIENCE_COMMENT_FAIL - 체험단이벤트 댓글구분등록 오류입니다."
          )
  })
  @ResponseBody
  public ApiResult<?> addEvent(@RequestBody EventManageRequestDto eventManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.addEvent Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto     :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }
    // eventInfo
    if (eventManageRequestDto.getEventInfo() != null) {
      log.debug("# In.eventManageRequestDto.eventInfo     :: " + eventManageRequestDto.getEventInfo().toString());
    }
    else {
      log.debug("# In.eventManageRequestDto.eventInfo is Null");
    }
    // eventNormalInfo
    if (eventManageRequestDto.getEventNormalInfo() != null) {
      log.debug("# In.eventManageRequestDto.eventNormalInfo     :: " + eventManageRequestDto.getEventNormalInfo().toString());
    }
    else {
      log.debug("# In.eventManageRequestDto.eventNormalInfo is Null");
    }
    // eventCommentCodeList
    if (eventManageRequestDto.getEventCommentCodeList() != null) {
      log.debug("# In.eventManageRequestDto.eventCommentCodeList     :: " + eventManageRequestDto.getEventCommentCodeList().toString());
    }
    else {
      log.debug("# In.eventManageRequestDto.eventCommentCodeList is Null");
    }
    // eventCouponList
    if (eventManageRequestDto.getEventCouponList() != null) {
      log.debug("# In.eventManageRequestDto.eventCouponList     :: " + eventManageRequestDto.getEventCouponList().toString());
    }
    else {
      log.debug("# In.eventManageRequestDto.eventCouponList is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    if (eventManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getEventInfo())) {
      // 이벤트 기본 정보등록 입력정보 오류입니다
      return ApiResult.result(EventMessage.EVENT_MANAGE_EVENT_ADD_FAIL_INPUT_TARGET);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getEventInfo().getEventTp())) {
      // 이벤트유형을 확인하세요
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_EVENT_TP);
    }

    // ----------------------------------------------------------------------
    // @ 2.1.2. 이벤트 그룹리스트 내 그룹상품리스트 변환
    // ----------------------------------------------------------------------
    if (eventManageRequestDto.getGroupList() != null && eventManageRequestDto.getGroupList().size() > 0) {
      for (EventGroupVo eventGroupVo : eventManageRequestDto.getGroupList()) {
        // ----------------------------------------------------------------------
        // @ 이벤트 그룹리스트정보 변환 : JsonString -> List<eventGroupGoodsVo>
        // ----------------------------------------------------------------------
        try {
          if (StringUtil.isNotEmpty(eventGroupVo.getGroupGoodsListJsonString())) {
            eventGroupVo.setGroupGoodsList(BindUtil.convertJsonArrayToDtoList(eventGroupVo.getGroupGoodsListJsonString(), EventGroupDetlVo.class));
          }
        } catch (Exception e) {
          log.error("# In.eventManageRequestDto(3) :: " + eventManageRequestDto.toString());
          return ApiResult.result(EventMessage.EVENT_MANAGE_EVENT_GROUP_DETL_ADD_FAIL_INPUT_TARGET);
        }
      }
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # 입력데이터Set + BOS 서비스 호출
    eventManageRequestDto.setMode(JOB_ADD);   // TODO 제거 검토
    return eventManageBiz.addEvent(eventManageRequestDto);
  }

  /**
   * 이벤트 수정
   * @param eventManageRequestDto
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unused")
  @RequestMapping(value="/admin/pm/event/putEvent")
  @ApiOperation(value = "이벤트수정")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = EventManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EVENT_MANAGE_PARAM_NO_INPUT                    - 이벤트 수정처리 오류입니다."
          + "EVENT_MANAGE_PARAM_NO_EVENT_TP                 - 이벤트유형을 확인하세요."
          + "EVENT_MANAGE_EVENT_PUT_FAIL_INPUT_TARGET       - 이벤트 기본정보수정 입력정보 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_PROC_FAIL               - 이벤트 수정처리 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_BASIC_FAIL              - 이벤트 기본정보수정 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_USER_GROUP_FAIL         - 이벤트 접근권한설정 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_NORMAL_PROC_FAIL        - 일반이벤트 수정처리 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_NORMAL_FAIL             - 일반이벤트 상세정보수정 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_NORMAL_COMMENT_FAIL     - 일반이벤트 댓글구분수정 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_NORMAL_COUPON_FAIL      - 일반이벤트 쿠폰정보수정 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_SURVEY_PROC_FAIL        - 설문이벤트 수정처리 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_SURVEY_FAIL             - 설문이벤트 상세정보수정 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_SURVEY_COUPON_FAIL      - 설문이벤트 쿠폰정보수정 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_SURVEY_QUESTION_FAIL    - 설문이벤트 설문항목수정 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_SURVEY_ITEM_FAIL        - 설문이벤트 설문항목아이템수정 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_SURVEY_ITEM_ATTC_FAIL   - 설문이벤트 설문항목아이템첨부파일수정 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_STAMP_PROC_FAIL         - 스탬프이벤트 수정처리 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_STAMP_FAIL              - 스탬프이벤트 상세정보수정 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_STAMP_STARMP_FAIL       - 스탬프이벤트 스탬프정보수정 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_ROULETTE_PROC_FAIL      - 룰렛이벤트 수정처리 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_ROULETTE_FAIL           - 룰렛이벤트 상세정보수정 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_ROULETTE_ITEM_FAIL      - 룰렛이벤트 이벤트아이템정보수정 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_EXPERIENCE_PROC_FAIL    - 체험단이벤트 수정처리 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_EXPERIENCE_FAIL         - 체험단이벤트 상세정보수정 오류입니다."
          + "EVENT_MANAGE_EVENT_PUT_EXPERIENCE_COMMENT_FAIL - 체험단이벤트 댓글구분수정 오류입니다."
          )
  })
  @ResponseBody
  public ApiResult<?> putEvent(@RequestBody EventManageRequestDto eventManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.putEvent Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto     :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }
    // eventInfo
    if (eventManageRequestDto.getEventInfo() != null) {
      log.debug("# In.eventManageRequestDto.eventInfo     :: " + eventManageRequestDto.getEventInfo().toString());
    }
    else {
      log.debug("# In.eventManageRequestDto.eventInfo is Null");
    }
    // eventNormalInfo
    if (eventManageRequestDto.getEventNormalInfo() != null) {
      log.debug("# In.eventManageRequestDto.eventNormalInfo     :: " + eventManageRequestDto.getEventNormalInfo().toString());
    }
    else {
      log.debug("# In.eventManageRequestDto.eventNormalInfo is Null");
    }
    // eventCommentCodeList
    if (eventManageRequestDto.getEventCommentCodeList() != null) {
      log.debug("# In.eventManageRequestDto.eventCommentCodeList     :: " + eventManageRequestDto.getEventCommentCodeList().toString());
    }
    else {
      log.debug("# In.eventManageRequestDto.eventCommentCodeList is Null");
    }
    // eventCouponList
    if (eventManageRequestDto.getEventCouponList() != null) {
      log.debug("# In.eventManageRequestDto.eventCouponList     :: " + eventManageRequestDto.getEventCouponList().toString());
    }
    else {
      log.debug("# In.eventManageRequestDto.eventCouponList is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    if (eventManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getEventInfo())) {
      // 이벤트 기본 정보등록 입력정보 오류입니다
      return ApiResult.result(EventMessage.EVENT_MANAGE_EVENT_ADD_FAIL_INPUT_TARGET);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getEventInfo().getEventTp())) {
      // 이벤트유형을 확인하세요
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_EVENT_TP);
    }

    // ----------------------------------------------------------------------
    // @ 2.1.2. 이벤트 그룹리스트 내 그룹상품리스트 변환
    // ----------------------------------------------------------------------
    if (eventManageRequestDto.getGroupList() != null && eventManageRequestDto.getGroupList().size() > 0) {
      for (EventGroupVo eventGroupVo : eventManageRequestDto.getGroupList()) {
        // ----------------------------------------------------------------------
        // @ 이벤트 그룹리스트정보 변환 : JsonString -> List<eventGroupGoodsVo>
        // ----------------------------------------------------------------------
        try {
          if (StringUtil.isNotEmpty(eventGroupVo.getGroupGoodsListJsonString())) {
            eventGroupVo.setGroupGoodsList(BindUtil.convertJsonArrayToDtoList(eventGroupVo.getGroupGoodsListJsonString(), EventGroupDetlVo.class));
          }
        } catch (Exception e) {
          log.error("# In.eventManageRequestDto(3) :: " + eventManageRequestDto.toString());
          return ApiResult.result(EventMessage.EVENT_MANAGE_EVENT_GROUP_DETL_PUT_FAIL_INPUT_TARGET);
        }
      }
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # 입력데이터Set + BOS 서비스 호출
    eventManageRequestDto.setMode(JOB_PUT);   // TODO 제거 검토
    return eventManageBiz.putEvent(eventManageRequestDto);
  }

  /**
   * 이벤트 당첨자공지사항 수정
   * @param eventManageRequestDto
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unused")
  @RequestMapping(value="/admin/pm/event/putEventWinnerNotice")
  @ApiOperation(value = "이벤트당첨자공지사항수정")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = EventManageResponseDto.class),
          @ApiResponse(code = 901, message = ""
                  + "EVENT_MANAGE_PARAM_NO_INPUT                    - 이벤트 수정처리 오류입니다."
                  + "EVENT_MANAGE_EVENT_ADD_FAIL_INPUT_TARGET       - 이벤트 기본정보등록 입력정보 오류입니다."
                  + "EVENT_MANAGE_EVENT_PUT_WINNER_NOTICE_FAIL      - 이벤트 당첨자공지사항 수정 오류입니다."
                  + "EVENT_MANAGE_DETAIL_NO_DATA                    - 이벤트 기본정보가 없습니다."
          )
  })
  @ResponseBody
  public ApiResult<?> putEventWinnerNotice(@RequestBody EventManageRequestDto eventManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.putEventWinnerNotice Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto     :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }
    // eventInfo
    if (eventManageRequestDto.getEventInfo() != null) {
      log.debug("# In.eventManageRequestDto.eventInfo     :: " + eventManageRequestDto.getEventInfo().toString());
    }
    else {
      log.debug("# In.eventManageRequestDto.eventInfo is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    if (eventManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getEventInfo())) {
      // 이벤트 기본 정보등록 입력정보 오류입니다
      return ApiResult.result(EventMessage.EVENT_MANAGE_EVENT_ADD_FAIL_INPUT_TARGET);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # 입력데이터Set + BOS 서비스 호출
    return eventManageBiz.putEventWinnerNotice(eventManageRequestDto);
  }

  /**
   * 이벤트 등록/수정 처리
   * @param eventManageRequestDto
   * @param jobSe
   * @return
   * @throws Exception
   */
  private ApiResult<?> procEvent(EventManageRequestDto eventManageRequestDto, String jobSe) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.procEvent Start");
    log.debug("# ######################################");

    // ========================================================================
    // # 초기화
    // ========================================================================

    // ========================================================================
    // # 호출/반환
    // ========================================================================
    // # BOS 서비스 호출
    if (StringUtil.isEquals(jobSe, JOB_ADD)) {
      // 등록
      return eventManageBiz.addEvent(eventManageRequestDto);
    }
    else if (StringUtil.isEquals(jobSe, JOB_PUT)) {
      // 수정
      //return eventManageBiz.putEvent(eventManageRequestDto);
      return null;
    }
    else {
      return ApiResult.result(EventMessage.EVENT_MANAGE_NO_JOB);
    }
  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 수정
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 수정 - 이벤트참여
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트참여 당첨자처리
   * @param eventManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/event/putWinnerLottery")
  @ApiOperation(value = "당첨자처리")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = EventManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EVENT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          + "EVENT_MANAGE_PARAM_NO_EVENT_ID - 이벤트ID를 입력하세요."
          + "EVENT_MANAGE_PARAM_NO_WINNER_SELECT_TP - 당첨자선택유형을 확인하세요."
          + "EVENT_MANAGE_WINNER_PUT_INPUT_TARGET - 당첨처리 대상 입력정보 오류입니다."
          + "EVENT_MANAGE_PARAM_NO_WINNER_CNT - 당첨자수를 확인하세요."
          )
  })
  @ResponseBody
  public ApiResult<?> putWinnerLottery(EventManageRequestDto eventManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.putWinnerLottery Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto     :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (eventManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getEvEventId())) {

      // 이벤트ID를 입력하세요.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_EVENT_ID);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getWinnerSelectTp())) {

      // 당첨자선택유형을 확인하세요.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_WINNER_SELECT_TP);
    }
    // ----------------------------------------------------------------------
    // 당첨자리스트 변환 : JsonString -> List<EventJoinVo>
    // ----------------------------------------------------------------------
    try {
      if (StringUtil.isNotEmpty(eventManageRequestDto.getWinnerEventJoinListJsonString())) {
        if(EventEnums.WinnerSelectTp.DIRECT.getCode().equals(eventManageRequestDto.getWinnerSelectTp()) || EventEnums.HandwrittenLotteryType.RANDOM_BENEFIT_DIFFERENTIAL.getCode().equals(eventManageRequestDto.getHandwrittenLotteryTp())) {
          eventManageRequestDto.setWinnerEventJoinList(BindUtil.convertJsonArrayToDtoList(eventManageRequestDto.getWinnerEventJoinListJsonString(), EventJoinVo.class));
        }
      }
    } catch (Exception e) {
      log.error("# In.eventManageRequestDto(2) :: " + eventManageRequestDto.toString());
      log.error("# e :: " + e.toString());
      e.printStackTrace();
      // 당첨처리 대상 입력정보 오류입니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_WINNER_PUT_INPUT_TARGET);

    }

    if (EventEnums.RandomBenefitType.RANDOM_BENEFIT_SINGLE == eventManageRequestDto.getRandomBenefitTp() && StringUtil.isEmpty(eventManageRequestDto.getWinnerCnt())) {
      // 당첨자수를 확인하세요.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_WINNER_CNT);
    }
//    if (EventEnums.RandomBenefitType.RANDOM_BENEFIT_DIFFERENTIAL == eventManageRequestDto.getRandomBenefitTp()) {


//    }


    log.error("# In.eventManageRequestDto(3) :: " + eventManageRequestDto.toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # 입력데이터Set + BOS 서비스 호출
    return eventManageBiz.putWinnerLottery(eventManageRequestDto);

  }

  /**
   * 이벤트참여 댓글 차단/차단해제
   * @param eventManageRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/pm/event/putAdminSecretYn")
  @ApiOperation(value = "이벤트참여댓글차단/차단해제")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = EventManageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "EVENT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
          + "EVENT_MANAGE_PARAM_NO_EVENT_ID - 이벤트ID를 입력하세요."
          + "EVENT_MANAGE_PARAM_NO_EVENT_JOIN_ID - 이벤트참여ID를 입력하세요."
          + "EVENT_MANAGE_PARAM_NO_ADMIN_SECRET_YN - 댓글차단여부를 확인하세요."
          )
  })
  @ResponseBody
  public ApiResult<?> putAdminSecretYn(EventManageRequestDto eventManageRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageController.putAdminSecretYn Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto     :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (eventManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getEvEventId())) {

      // 이벤트ID를 입력하세요.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_EVENT_ID);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getEvEventJoinId())) {

      // 이벤트참여ID를 확인하세요.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_EVENT_JOIN_ID);
    }
    if (StringUtil.isEmpty(eventManageRequestDto.getAdminSecretYn())) {

      // 댓글차단여부를 확인하세요.
      return ApiResult.result(EventMessage.EVENT_MANAGE_PARAM_NO_ADMIN_SECRET_YN);
    }

    log.error("# In.eventManageRequestDto(3) :: " + eventManageRequestDto.toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # 입력데이터Set + BOS 서비스 호출
    return eventManageBiz.putAdminSecretYn(eventManageRequestDto);

  }


  // ##########################################################################
  // private
  // ##########################################################################
// 삭제예정
//  public static <T> List<T> convertJsonArrayToDtoListTmp(String jsonString, Class<T> clazz) throws Exception {
//
//    log.debug("# ######################################");
//    log.debug("# EventManageController.convertJsonArrayToDtoListTmp Start");
//    log.debug("# ######################################");
//    log.debug("# jsonString :: " + jsonString);
//
//    Gson gson = new Gson();
//
//    // String => JsonArray
//    JsonParser parser = new JsonParser();
//    JsonArray array = parser.parse(jsonString).getAsJsonArray();
//
//    // JsonArray => Dto List
//    List<T> returnList = new ArrayList<T>();
//
//    try {
//
//      for (final JsonElement json : array) {
//        T entity = gson.fromJson(json, clazz);
//        log.debug("# entity :: " + entity.toString());
//        returnList.add(entity);
//      }
//    }
//    catch (Exception e) {
//      log.debug("# convertJsonArrayToDtoListTmp.error :: " + e.toString());
//      e.printStackTrace();
//
//    }
//
//    return returnList;
//  }




}