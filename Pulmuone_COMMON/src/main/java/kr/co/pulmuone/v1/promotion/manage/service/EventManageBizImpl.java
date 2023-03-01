package kr.co.pulmuone.v1.promotion.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.promotion.manage.dto.EventManageRequestDto;
import kr.co.pulmuone.v1.promotion.manage.dto.EventManageResponseDto;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EventJoinVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EventSurveyVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EventVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 프로모션 이벤트관리 COMMON Impl
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0       2021.01.12.              dgyoun         최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@Service
@RequiredArgsConstructor
public class EventManageBizImpl implements EventManageBiz {

  @Autowired
  private EventManageService eventManageService;

  //@Autowired
  //private SystemBasicEnvironmentBiz systemBasicEnvironmentBiz;
  //호스트
  //private String hostUrl; // public 저장소 접근 url

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 조회 - 이벤트
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트 리스트조회
   */
  @UserMaskingRun(system = "MUST_MASKING")  // 강제 마스킹
  @Override
  public ApiResult<?> selectEventList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# EventManageBizImpl.selectEventList Start");
    log.info("# ######################################");
    if (eventManageRequestDto != null) {
      log.info("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.info("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto result = new EventManageResponseDto();

    // ========================================================================
    // # 처리
    // ========================================================================
    Page<EventVo> voList = eventManageService.selectEventList(eventManageRequestDto);
    result.setTotal(voList.getTotal());
    result.setRows(voList.getResult());

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 이벤트 상세조회
   */
  @Override
  public ApiResult<?> selectEventInfo (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# EventManageBizImpl.selectEventInfo Start");
    log.info("# ######################################");
    if (eventManageRequestDto != null) {
      log.info("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.info("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto result = new EventManageResponseDto();

    // ========================================================================
    // # 처리
    // ========================================================================
    result = eventManageService.selectEventInfo(eventManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  // **************************************************************************
  // 일반(그룹)
  // **************************************************************************
  /**
   * 이벤트 상세조회 - 일반(그룹) - 그룹리스트
   */
  @Override
  public ApiResult<?> selectfEventGroupList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# EventManageBizImpl.selectfEventGroupList Start");
    log.info("# ######################################");
    if (eventManageRequestDto != null) {
      log.info("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.info("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = eventManageService.selectfEventGroupList(eventManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 이벤트 상세조회 - 일반(그룹) - 그룹상품리스트
   */
  @Override
  public ApiResult<?> selectfEventGroupGoodsList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# EventManageBizImpl.selectfEventGroupGoodsList Start");
    log.info("# ######################################");
    if (eventManageRequestDto != null) {
      log.info("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.info("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = eventManageService.selectfEventGroupGoodsList(eventManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }



  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 조회 - 이벤트참여
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트참여 리스트조회
   */
  @UserMaskingRun(system = "MUST_MASKING")
  @Override
  public ApiResult<?> selectEventJoinList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# EventManageBizImpl.selectEventJoinList Start");
    log.info("# ######################################");
    if (eventManageRequestDto != null) {
      log.info("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.info("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto result = new EventManageResponseDto();

    // ========================================================================
    // # 처리
    // ========================================================================
    Page<EventJoinVo> voList = eventManageService.selectEventJoinList(eventManageRequestDto);
    result.setTotal(voList.getTotal());
    result.setRows(voList.getResult());

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 이벤트참여 리스트조회
   */
  @UserMaskingRun
  @Override
  public ApiResult<?> selectExcelEventJoinList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    Page<EventJoinVo> voList = eventManageService.selectEventJoinList(eventManageRequestDto);

    EventManageResponseDto result = new EventManageResponseDto();
    result.setTotal(voList.getTotal());
    result.setRows(voList.getResult());
    return ApiResult.success(result);
  }

  /**
   * 이벤트참여 직접입력 리스트조회
   */
  @Override
  //@UserMaskingRun(system = "MUST_MASKING")  // 테스트 시 사용하세요.
  @UserMaskingRun(system = "BOS")
  public ApiResult<?> selectEventJoinDirectJoinList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# EventManageBizImpl.selectEventJoinDirectJoinList Start");
    log.info("# ######################################");
    if (eventManageRequestDto != null) {
      log.info("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.info("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto result = new EventManageResponseDto();

    // ========================================================================
    // # 처리
    // ========================================================================
    Page<EventJoinVo> voList = eventManageService.selectEventJoinDirectJoinList(eventManageRequestDto);
    result.setTotal(voList.getTotal());
    result.setRows(voList.getResult());

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 이벤트참여 설문항목 리스트조회
   * - No Page
   */
  @Override
  public ApiResult<?> selectEventJoinSurveyList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# EventManageBizImpl.selectEventJoinSurveyList Start");
    log.info("# ######################################");
    if (eventManageRequestDto != null) {
      log.info("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.info("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto result = new EventManageResponseDto();

    // ========================================================================
    // # 처리
    // ========================================================================
    List<EventSurveyVo> voList = eventManageService.selectEventJoinSurveyList(eventManageRequestDto);
    // No Page
    result.setTotal(voList.size());
    result.setRows(voList);
    // Page
    //result.setTotal(voList.getTotal());
    //result.setRows(voList.getResult());

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 이벤트참여 설문항목참여 리스트조회
   * - No Page
   */
  @Override
  public ApiResult<?> selectEventJoinSurveyItemJoinList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# EventManageBizImpl.selectEventJoinSurveyItemJoinList Start");
    log.info("# ######################################");
    if (eventManageRequestDto != null) {
      log.info("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.info("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto result = new EventManageResponseDto();

    // ========================================================================
    // # 처리
    // ========================================================================
    List<EventSurveyVo> voList = eventManageService.selectEventJoinSurveyItemJoinList(eventManageRequestDto);
    // No Page
    result.setTotal(voList.size());
    result.setRows(voList);
    // Page
    //result.setTotal(voList.getTotal());
    //result.setRows(voList.getResult());

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  /**
   * 상품정보리스트(엑셀용)
   */
  @Override
  public ApiResult<?> selectGoodsInfoList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# EventManageBizImpl.selectGoodsInfoList Start");
    log.info("# ######################################");
    if (eventManageRequestDto != null) {
      log.info("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.info("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto result = null;

    // ========================================================================
    // # 처리
    // ========================================================================
    result = eventManageService.selectGoodsInfoList(eventManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return ApiResult.success(result);
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 삭제
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트 삭제
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delEvent (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageBizImpl.delEvent Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // Controller에서 List<String> 형태로 Set 한다
    EventManageResponseDto result = eventManageService.delEvent(eventManageRequestDto.getEvEventIdList());

    return ApiResult.success(result);
  }

  /**
   * 그룹상세 삭제(그룹상품)
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delEventGroupDetl (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageBizImpl.delEventGroupDetl Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // Controller에서 List<String> 형태로 Set 한다
    EventManageResponseDto result = eventManageService.delEventGroupDetl(eventManageRequestDto.getEvEventGroupDetlIdList());

    return ApiResult.success(result);
  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 등록
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> addEvent (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageBizImpl.addEvent Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }
    EventManageResponseDto result = eventManageService.addEvent(eventManageRequestDto);
    return ApiResult.success(result);
  }



  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 수정
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트정보 수정
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putEvent (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageBizImpl.putEvent Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }
    EventManageResponseDto result = eventManageService.putEvent(eventManageRequestDto);
    return ApiResult.success(result);
  }

  /**
   * 이벤트 당첨자공지사항 수정
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putEventWinnerNotice (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageBizImpl.putEventWinnerNotice Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }
    EventManageResponseDto result = eventManageService.putEventWinnerNotice(eventManageRequestDto);
    return ApiResult.success(result);
  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 수정 - 이벤트참여
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트참여 당첨자 설정
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putWinnerLottery (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageBizImpl.putWinnerLottery Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    EventManageResponseDto result = eventManageService.putWinnerLottery(eventManageRequestDto);

    return ApiResult.success(result);
  }

  /**
   * 이벤트참여 댓글 차단/차단해제
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putAdminSecretYn (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageBizImpl.putAdminSecretYn Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    EventManageResponseDto result = eventManageService.putAdminSecretYn(eventManageRequestDto);

    return ApiResult.success(result);
  }



}
