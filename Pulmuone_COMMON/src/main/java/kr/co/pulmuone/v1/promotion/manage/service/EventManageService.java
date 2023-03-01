package kr.co.pulmuone.v1.promotion.manage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.base.dto.vo.GoodsSearchVo;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.CouponEnums;
import kr.co.pulmuone.v1.comm.enums.EventEnums;
import kr.co.pulmuone.v1.comm.enums.EventEnums.*;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.promotion.manage.EventManageMapper;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.event.dto.vo.CoverageVo;
import kr.co.pulmuone.v1.promotion.event.dto.vo.EventCouponVo;
import kr.co.pulmuone.v1.promotion.manage.dto.EventBenefitCountRequestDto;
import kr.co.pulmuone.v1.promotion.manage.dto.EventManageRequestDto;
import kr.co.pulmuone.v1.promotion.manage.dto.EventManageResponseDto;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.*;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
* <PRE>
* Forbiz Korea
* 프로모션 이벤트관리 COMMON Service
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
public class EventManageService {

  private final PromotionCouponBiz promotionCouponBiz;
  private final PointBiz pointBiz;

  final String YES  = "Y";
  final String NO   = "N";

  final String MODE_ADD = "ADD";
  final String MODE_PUT = "PUT";

  private final EventManageMapper eventManageMapper;

  // ##########################################################################
  // protected
  // ##########################################################################

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 조회 - 이벤트
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트 리스트조회
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @UserMaskingRun(system="BOS")
  protected Page<EventVo> selectEventList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.selectEventList Start");
    log.debug("# ######################################");
    //log.debug("# In.dpPageId :: " + dpPageId);
    //log.debug("# In.useAllYn :: " + useAllYn);

    // ========================================================================
    // # 초기화
    // ========================================================================
    Page<EventVo> resultList = null;

    // ========================================================================
    // # 처리
    // ========================================================================

    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------
    PageMethod.startPage(eventManageRequestDto.getPage(), eventManageRequestDto.getPageSize());
    resultList = eventManageMapper.selectEventList(eventManageRequestDto);

    // ------------------------------------------------------------------------
    // 회원등급조회 및 Set (건별)
    // ------------------------------------------------------------------------
    //if (resultList != null && resultList.size() > 0) {
    //  for (EventVo vo : resultList) {
    //    vo.setUserGroupList(eventManageMapper.selectEventUserGroupListForList(vo.getEvEventId()));
    //  }
    //}

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultList;
  }

  /**
   * 이벤트 상세조회
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  @UserMaskingRun(system = "BOS")
  protected EventManageResponseDto selectEventInfo (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.selectEventInfo Start");
    log.debug("# ######################################");
    log.debug("# In.evEventId :: " + eventManageRequestDto.getEvEventId());

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    // 이벤트-기본정보
    EventVo resultDetailInfo = null;

    // ========================================================================
    // # 처리
    // ========================================================================

    // ************************************************************************
    // 1.기본정보조회
    // ************************************************************************
    // ------------------------------------------------------------------------
    // 1.1. 이벤트정보
    // ------------------------------------------------------------------------
    resultDetailInfo = eventManageMapper.selectEventInfo(eventManageRequestDto.getEvEventId());
    resultResDto.setEventInfo(resultDetailInfo);
    if (resultDetailInfo == null || StringUtil.isEmpty(resultDetailInfo.getEvEventId()) || StringUtil.isEquals(resultDetailInfo.getEvEventId(), "0")) {
      log.debug("# 이벤트 기본정보가 없습니다.");
      resultResDto.setResultCode(EventMessage.EVENT_MANAGE_DETAIL_NO_DATA.getCode());
      resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_DETAIL_NO_DATA.getMessage());
      return resultResDto;
    }
    resultResDto.setTotal(1);

    if (StringUtil.isEmpty(resultDetailInfo) || StringUtil.isEmpty(resultDetailInfo.getEvEventId())) {
      // 이벤트 기본정보가 없습니다.
      resultResDto.setResultCode(EventMessage.EVENT_MANAGE_DETAIL_NO_DATA.getCode());
      resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_DETAIL_NO_DATA.getMessage());
      resultResDto.setTotal(0);
      return resultResDto;
    }
    if (StringUtil.isEmpty(resultDetailInfo.getEventTp())) {
      // 이벤트 유형정보가 없습니다.
      resultResDto.setResultCode(EventMessage.EVENT_MANAGE_DETAIL_NO_EVENT_TP.getCode());
      resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_DETAIL_NO_EVENT_TP.getMessage());
      resultResDto.setTotal(0);
      return resultResDto;
    }

    // ------------------------------------------------------------------------
    // 1.2. 이벤트접근권한
    // ------------------------------------------------------------------------
    List<EvUserGroupVo> userGroupList = eventManageMapper.selectEventUserGroupList(eventManageRequestDto.getEvEventId());
    resultResDto.setUserGroupList(userGroupList);

    // ------------------------------------------------------------------------
    // 1.3. 이벤트 적용범위
    // ------------------------------------------------------------------------
    List<CoverageVo> coverageList = eventManageMapper.getCoverageList(eventManageRequestDto.getEvEventId());
    if (!coverageList.isEmpty()) {
      resultResDto.setCoverageList(coverageList);
    }

    // ************************************************************************
    // 2. 이벤트유형별정보조회
    // ************************************************************************
    if (StringUtil.isEquals(resultDetailInfo.getEventTp(), EventTp.NORMAL.getCode())) {
      // ----------------------------------------------------------------------
      // 2.1. 일반이벤트
      // ----------------------------------------------------------------------

      // ----------------------------------------------------------------------
      // 2.1.1. 일반이벤트상세
      // ----------------------------------------------------------------------
      EventNormalVo eventNormalInfo = eventManageMapper.selectEventNormal(eventManageRequestDto.getEvEventId());
      resultResDto.setEventNormalInfo(eventNormalInfo);


      if (eventNormalInfo != null) {

        // --------------------------------------------------------------------
        // 댓글허용이 Y인 경우만 처리
        // --------------------------------------------------------------------
//        if (StringUtil.isEquals(eventNormalInfo.getCommentYn(), YES)) {
        if (!StringUtil.isEquals(eventNormalInfo.getNormalEventTp(), EventEnums.NormalEventType.NONE.getCode())) {

          // ------------------------------------------------------------------
          // 2.1.2. 이벤트댓글
          // ------------------------------------------------------------------
          if (StringUtil.isEquals(eventNormalInfo.getCommentCodeYn(), YES)) {
            List<EventVo> eventCommentCodeList = eventManageMapper.selectEventCommentCodeList(eventManageRequestDto.getEvEventId());
            resultResDto.setEventCommentCodeList(eventCommentCodeList);
          }

          // ------------------------------------------------------------------
          // 2.1.3. 이벤트쿠폰
          // ------------------------------------------------------------------
          if (StringUtil.isEquals(eventNormalInfo.getEventBenefitTp(), EventBenefitType.COUPON.getCode())) {
            // 이벤트혜택 == 쿠폰
            List<EventVo> eventCouponList = eventManageMapper.selectEventCouponList(eventManageRequestDto.getEvEventId(), null);
            resultResDto.setEventCouponList(eventCouponList);
          }
        }
      }

    }
    else if (StringUtil.isEquals(resultDetailInfo.getEventTp(), EventTp.SURVEY.getCode())) {
      // ----------------------------------------------------------------------
      // 2.2. 설문이벤트
      // ----------------------------------------------------------------------

      // ----------------------------------------------------------------------
      //  2.2.1. 설문이벤트상세
      // ----------------------------------------------------------------------
      EventSurveyVo eventSurveyInfo = eventManageMapper.selectEventSurvey(eventManageRequestDto.getEvEventId());
      resultResDto.setEventSurveyInfo(eventSurveyInfo);

      // ----------------------------------------------------------------------
      //  2.2.2. 이벤트쿠폰리스트
      // ----------------------------------------------------------------------
      if (StringUtil.isEquals(eventSurveyInfo.getEventBenefitTp(), EventBenefitType.COUPON.getCode())) {
        // 이벤트혜택 == 쿠폰
        List<EventVo> eventCouponList = eventManageMapper.selectEventCouponList(eventManageRequestDto.getEvEventId(), null);
        resultResDto.setEventCouponList(eventCouponList);
      }

      // ----------------------------------------------------------------------
      //  2.2.3. 설문항목정보리스트
      // ----------------------------------------------------------------------
      List<EventSurveyVo> eventSurveyQuestionList = eventManageMapper.selectEventSurveyQuestionList(eventManageRequestDto.getEvEventId());
      resultResDto.setEventSurveyQuestionList(eventSurveyQuestionList);

      // ----------------------------------------------------------------------
      //  2.2.4. 설문항목아이템정보
      // ----------------------------------------------------------------------
      if (eventSurveyQuestionList != null && eventSurveyQuestionList.size() > 0) {
        for (EventSurveyVo eventSurveyQuestion : eventSurveyQuestionList) {
          List<EventSurveyItemVo> eventSurveyItemList = eventManageMapper.selectEventSurveyItemList(eventSurveyQuestion.getEvEventSurveyQuestionId());
          eventSurveyQuestion.setEventSurveyItemList(eventSurveyItemList);
          // ------------------------------------------------------------------
          //  2.2.5. 설문항목아이템첨부파일
          // ------------------------------------------------------------------
          if (eventSurveyItemList != null && eventSurveyItemList.size() > 0) {
            for (EventSurveyItemVo eventSurveyItemVo : eventSurveyItemList) {
              List<EventSurveyItemAttcVo> eventSurveyItemAttcList = eventManageMapper.selectEventSurveyItemAttcList(eventSurveyItemVo.getEvEventSurveyItemId());
              eventSurveyItemVo.setEventSurveyItemAttcList(eventSurveyItemAttcList);
            } // End of for (EventSurveyItemVo eventSurveyItemVo : eventSurveyItemList)
          } // End of if (eventSurveyItemList != null && eventSurveyItemList.size() > 0)
        } // End of for (EventSurveyVo eventSurveyQuestion : eventSurveyQuestionList)
      } // End of if (eventSurveyQuestionList != null && eventSurveyQuestionList.size() > 0)
    }
    else if (StringUtil.isEquals(resultDetailInfo.getEventTp(), EventTp.ATTEND.getCode())  ||
             StringUtil.isEquals(resultDetailInfo.getEventTp(), EventTp.MISSION.getCode()) ||
             StringUtil.isEquals(resultDetailInfo.getEventTp(), EventTp.PURCHASE.getCode())) {
      // ----------------------------------------------------------------------
      // 2.3. 스탬프(출석)
      // ----------------------------------------------------------------------

      // ----------------------------------------------------------------------
      // 2.3.1. 스탬프(출석)상세
      // ----------------------------------------------------------------------
      EventStampVo eventStampInfo = eventManageMapper.selectEventStamp(eventManageRequestDto.getEvEventId());
      resultResDto.setEventStampInfo(eventStampInfo);

      // ----------------------------------------------------------------------
      // 2.3.2. 스탬프(출석)이벤트상세리스트
      // ----------------------------------------------------------------------
      if (StringUtil.isNotEmpty(eventStampInfo.getEvEventStampId())) {
        List<EventStampVo> eventStampDetlList = eventManageMapper.selectEventStampDetlList(eventStampInfo.getEvEventStampId());
        if (eventStampDetlList != null && eventStampDetlList.size() > 0) {
          for (EventStampVo eventStampVo : eventStampDetlList) {
            // ----------------------------------------------------------------
            // 2.3.3. 스탬프쿠폰리스트
            // ----------------------------------------------------------------
            if (StringUtil.isEquals(eventStampVo.getEventBenefitTp(), EventBenefitType.COUPON.getCode())) {
              // 이벤트혜택유형 : 쿠폰
              List<EventVo> eventCouponList = eventManageMapper.selectEventCouponList(eventManageRequestDto.getEvEventId(), eventStampVo.getEvEventStampDetlId());
              if (eventCouponList != null && eventCouponList.size() > 0) {
                log.debug("# 쿠폰 Set :: " + eventCouponList.toString());
                eventStampVo.setEventCouponList(eventCouponList);
              }
            }
          }
        }

        resultResDto.setEventStampDetlList(eventStampDetlList);
      }

    }
    else if (StringUtil.isEquals(resultDetailInfo.getEventTp(), EventTp.ROULETTE.getCode())) {
      // ----------------------------------------------------------------------
      // 2.6. 룰렛이벤트
      // ----------------------------------------------------------------------

      // ----------------------------------------------------------------------
      // 2.6.1. 룰렛상세
      // ----------------------------------------------------------------------
      EventRouletteVo eventRouletteInfo = eventManageMapper.selectEventRoulette(eventManageRequestDto.getEvEventId());
      resultResDto.setEventRouletteInfo(eventRouletteInfo);

      // ----------------------------------------------------------------------
      // 2.6.2. 룰렛이벤트아이템리스트
      // ----------------------------------------------------------------------
      if (StringUtil.isNotEmpty(eventRouletteInfo.getEvEventRouletteId())) {
        List<EventRouletteVo> eventRouletteItemList = eventManageMapper.selectEventRouletteItemList(eventRouletteInfo.getEvEventRouletteId());
        if (eventRouletteItemList != null && eventRouletteItemList.size() > 0) {
          for (EventRouletteVo eventRouletteVo : eventRouletteItemList) {
            // ----------------------------------------------------------------
            // 2.3.3. 스탬프쿠폰리스트
            // ----------------------------------------------------------------
            if (StringUtil.isEquals(eventRouletteVo.getEventBenefitTp(), EventBenefitType.COUPON.getCode())) {
              // 이벤트혜택유형 : 쿠폰
              List<EventVo> eventCouponList = eventManageMapper.selectEventCouponList(eventManageRequestDto.getEvEventId(), eventRouletteVo.getEvEventRouletteItemId());
              if (eventCouponList != null && eventCouponList.size() > 0) {
                log.debug("# 쿠폰 Set :: " + eventCouponList.toString());
                eventRouletteVo.setEventCouponList(eventCouponList);
              }
            }
          }
        }
        resultResDto.setEventRouletteItemList(eventRouletteItemList);
      }
    }
    else if (StringUtil.isEquals(resultDetailInfo.getEventTp(), EventTp.EXPERIENCE.getCode())) {
      // ----------------------------------------------------------------------
      // 2.7. 체험단이벤트
      // ----------------------------------------------------------------------

      // ----------------------------------------------------------------------
      // 2.7.1. 체험단이벤트상세
      // ----------------------------------------------------------------------
      EventExperienceVo eventExperienceInfo = eventManageMapper.selectEventExperience(eventManageRequestDto.getEvEventId());
      resultResDto.setEventExperienceInfo(eventExperienceInfo);

      // ----------------------------------------------------------------------
      // 2.7.2. 이벤트댓글
      // ----------------------------------------------------------------------
      if (eventExperienceInfo != null) {

        if (StringUtil.isEquals(eventExperienceInfo.getCommentCodeYn(), YES)) {
          List<EventVo> eventCommentCodeList = eventManageMapper.selectEventCommentCodeList(eventManageRequestDto.getEvEventId());
          resultResDto.setEventCommentCodeList(eventCommentCodeList);
        }
      }
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 이벤트 상세조회 - 일반(그룹) - 그룹리스트
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  protected EventManageResponseDto selectfEventGroupList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.selectfEventGroupList Start");
    log.debug("# ######################################");
    log.debug("# In.evEventId :: " + eventManageRequestDto.getEvEventId());

    // ======================================================================
    // # 초기화
    // ======================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    List<EventGroupVo>        resultGroupList             = null;

    // ======================================================================
    // # 처리
    // ======================================================================

    // ----------------------------------------------------------------------
    // 그룹 리스트조회
    // ----------------------------------------------------------------------
    resultGroupList = eventManageMapper.selectfEventGroupList(eventManageRequestDto.getEvEventId());
    resultResDto.setRows(resultGroupList);

    if (resultGroupList != null && resultGroupList.size() > 0) {
      resultResDto.setTotal(resultGroupList.size());
    }
    else {
      resultResDto.setTotal(0);
    }

    // ======================================================================
    // # 반환
    // ======================================================================
    return resultResDto;
  }

  /**
   * 이벤트 상세조회 - 일반(그룹) - 그룹상품리스트
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  protected EventManageResponseDto selectfEventGroupGoodsList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.selectfEventGroupGoodsList Start");
    log.debug("# ######################################");
    log.debug("# In.evEventId :: " + eventManageRequestDto.getEvEventId());

    // ======================================================================
    // # 초기화
    // ======================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    List<EventGroupDetlVo>    resultGroupGoodsList        = null;

    // ======================================================================
    // # 처리
    // ======================================================================

    // ----------------------------------------------------------------------
    // 그룹상품 리스트조회
    // ----------------------------------------------------------------------
    resultGroupGoodsList = eventManageMapper.selectfEventGroupGoodsList(eventManageRequestDto.getEvEventGroupId());
    resultResDto.setRows(resultGroupGoodsList);

    if (resultGroupGoodsList != null && resultGroupGoodsList.size() > 0) {
      resultResDto.setTotal(resultGroupGoodsList.size());
    }
    else {
      resultResDto.setTotal(0);
    }

    // ======================================================================
    // # 반환
    // ======================================================================
    return resultResDto;
  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 조회 - 이벤트참여
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트참여 리스트조회
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  protected Page<EventJoinVo> selectEventJoinList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.selectEventJoinList Start");
    log.debug("# ######################################");
    //log.debug("# In.dpPageId :: " + dpPageId);
    //log.debug("# In.useAllYn :: " + useAllYn);

    // ========================================================================
    // # 초기화
    // ========================================================================
    Page<EventJoinVo> resultList = null;

    // ========================================================================
    // # 처리
    // ========================================================================

    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------
    PageMethod.startPage(eventManageRequestDto.getPage(), eventManageRequestDto.getPageSize());
    resultList = eventManageMapper.selectEventJoinList(eventManageRequestDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultList;
  }

  /**
   * 이벤트참여 직접입력 리스트조회
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  protected Page<EventJoinVo> selectEventJoinDirectJoinList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.selectEventJoinDirectJoinList Start");
    log.debug("# ######################################");
    //log.debug("# In.dpPageId :: " + dpPageId);
    //log.debug("# In.useAllYn :: " + useAllYn);

    // ========================================================================
    // # 초기화
    // ========================================================================
    Page<EventJoinVo> resultList = null;

    // ========================================================================
    // # 처리
    // ========================================================================

    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------
    PageMethod.startPage(eventManageRequestDto.getPage(), eventManageRequestDto.getPageSize());
    resultList = eventManageMapper.selectEventJoinDirectJoinList(eventManageRequestDto.getEvEventSurveyQuestionId());

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultList;
  }

  /**
   * 이벤트참여 설문항목 리스트조회
   * - No Page
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  protected List<EventSurveyVo> selectEventJoinSurveyList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.selectEventJoinSurveyList Start");
    log.debug("# ######################################");
    //log.debug("# In.dpPageId :: " + dpPageId);
    //log.debug("# In.useAllYn :: " + useAllYn);

    // ========================================================================
    // # 초기화
    // ========================================================================
    List<EventSurveyVo> resultList                      = null;    // 설문항목
    List<EventSurveyItemVo> resultSurveyItemList        = null;    // 설문아이템
    List<EventSurveyItemAttcVo> resultSurveyItemAttcist = null;    // 설문아이템첨부파일

    // ========================================================================
    // # 처리
    // ========================================================================

    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // 1. 설문항목 리스트조회
    // ------------------------------------------------------------------------
    resultList = eventManageMapper.selectEventJoinSurveyQuestionList(eventManageRequestDto.getEvEventId());

    if (resultList != null && resultList.size() > 0) {

      ObjectMapper objectMapper = new ObjectMapper();

      //for (EventSurveyVo unitEventSurveyVo : resultList) {
      for (int i = 0; i < resultList.size(); i++) {
        // 설문항목 리스트 Loop

        // --------------------------------------------------------------------
        // 2. 설문아이템 리스트조회
        // --------------------------------------------------------------------
        resultSurveyItemList = eventManageMapper.selectEventJoinSurveyItemList(resultList.get(i).getEvEventSurveyQuestionId());

        if (resultSurveyItemList != null && resultSurveyItemList.size() > 0) {

          resultList.get(i).setEventSurveyItemList(resultSurveyItemList);

          // ------------------------------------------------------------------
          // depth 2
          // ------------------------------------------------------------------
          //for (EventSurveyVo unitEventSurveyItemVo : resultSurveyItemList) {
          for (int j = 0; j < resultSurveyItemList.size(); j++) {
            // 설문아이템 리스트 Loop

            // ----------------------------------------------------------------
            // 3. 설문아이템첨부파일 리스트조회
            // ----------------------------------------------------------------
            resultSurveyItemAttcist = eventManageMapper.selectEventJoinSurveyItemAttcList(resultSurveyItemList.get(j).getEvEventSurveyItemId());

            if (resultSurveyItemAttcist != null && resultSurveyItemAttcist.size() > 0) {

              resultSurveyItemList.get(j).setEventSurveyItemAttcList(resultSurveyItemAttcist);

            } // End of if (resultSurveyItemAttcist != null && resultSurveyItemAttcist.size() > 0)

          } // End of for (int j = 0; j < resultSurveyItemList.size(); j++)

        } // End of if (resultSurveyItemList != null && resultSurveyItemList.size() > 0)

      } // End of for (int i = 0; i < resultList.size(); i++)

    } // End of if (resultList != null && resultList.size() > 0)

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultList;
  }

  /**
   * 이벤트참여 설문항목참여 리스트조회
   * - No Page
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  protected List<EventSurveyVo> selectEventJoinSurveyItemJoinList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.selectEventJoinSurveyItemJoinList Start");
    log.debug("# ######################################");
    //log.debug("# In.dpPageId :: " + dpPageId);
    //log.debug("# In.useAllYn :: " + useAllYn);

    // ========================================================================
    // # 초기화
    // ========================================================================
    List<EventSurveyVo>     resultList                      = null;    // 설문항목
    List<EventSurveyItemVo> resultSurveyItemJoinList        = null;    // 설문아이템참여

    // ========================================================================
    // # 처리
    // ========================================================================

    // ------------------------------------------------------------------------
    // # 조회
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // 1. 설문항목 리스트조회
    // ------------------------------------------------------------------------
    resultList = eventManageMapper.selectEventJoinSurveyQuestionList(eventManageRequestDto.getEvEventId());

    if (resultList != null && resultList.size() > 0) {

      ObjectMapper objectMapper = new ObjectMapper();

      //for (EventSurveyVo unitEventSurveyVo : resultList) {
      for (int i = 0; i < resultList.size(); i++) {
        // 설문항목 리스트 Loop

        // --------------------------------------------------------------------
        // 2. 설문아이템 리스트조회
        // --------------------------------------------------------------------
        resultSurveyItemJoinList = eventManageMapper.selectEventJoinSurveyItemJoinList(eventManageRequestDto.getEvEventJoinId(), resultList.get(i).getEvEventSurveyQuestionId());

        if (resultSurveyItemJoinList != null && resultSurveyItemJoinList.size() > 0) {

          resultList.get(i).setEventSurveyItemList(resultSurveyItemJoinList);

        } // End of if (resultSurveyItemList != null && resultSurveyItemList.size() > 0)

      } // End of for (int i = 0; i < resultList.size(); i++)

    } // End of if (resultList != null && resultList.size() > 0)

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultList;
  }

  /**
   * 상품정보리스트(엑셀용)
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  protected EventManageResponseDto selectGoodsInfoList (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.selectGoodsInfoList Start");
    log.debug("# ######################################");
    log.debug("# In.ilGoodsList :: " + eventManageRequestDto.getIlGoodsIdList().toString());

    // ======================================================================
    // # 초기화
    // ======================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    List<GoodsSearchVo> resultGoodsList        = null;

    // ======================================================================
    // # 처리
    // ======================================================================

    // ----------------------------------------------------------------------
    // 상품 리스트조회
    // ----------------------------------------------------------------------
    resultGoodsList = eventManageMapper.selectGoodsInfoList(eventManageRequestDto);
    resultResDto.setRows(resultGoodsList);

    if (resultGoodsList != null && resultGoodsList.size() > 0) {
      resultResDto.setTotal(resultGoodsList.size());
    }
    else {
      resultResDto.setTotal(0);
    }

    // ======================================================================
    // # 반환
    // ======================================================================
    return resultResDto;
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 삭제
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트 삭제
   * @param eventList
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  protected EventManageResponseDto delEvent (List<String> eventList) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.delEvent Start");
    log.debug("# ######################################");
    if (eventList != null) {
      log.debug("# In.eventList.size :: " + eventList.size());
      log.debug("# In.eventList      :: " + eventList.toString());
    }
    else {
      log.debug("# In.eventList is Null or size 0");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    int resultInt = 0;
    int resultTotalInt = 0;

    EventVo unitEventVo = null;
    List<EventVo> resultEventList = new ArrayList<EventVo>();

    // ========================================================================
    // # 처리
    // ========================================================================

    try {

      if (eventList == null || eventList.size() <= 0) {
        // 이벤트 기본 정보삭제 입력정보 오류입니다.
        resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_DEL_FAIL_INPUT_TARGET.getCode());
        resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_DEL_FAIL_INPUT_TARGET.getMessage());
        resultResDto.setTotal(0);
        return resultResDto;
      }

      for (String evEventId : eventList) {

        unitEventVo = new EventVo();

        // --------------------------------------------------------------------
        // # 세션정보 Set
        // --------------------------------------------------------------------
        if (SessionUtil.getBosUserVO() != null) {
          unitEventVo.setCreateId((SessionUtil.getBosUserVO()).getUserId());
          unitEventVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        }
        else {
          unitEventVo.setCreateId("0");
          unitEventVo.setModifyId("0");
        }

        // --------------------------------------------------------------------
        // 삭제
        // --------------------------------------------------------------------
        unitEventVo.setEvEventId(evEventId);
        unitEventVo.setDelYn("Y");
        resultInt = eventManageMapper.delEvent(unitEventVo);

        if (resultInt <= 0) {
          // # 한건이라도 실패할 경우 모두 롤백
          log.debug("# 삭제건 없음");
          resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_DEL_FAIL_PROC.getCode());
          resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_DEL_FAIL_PROC.getMessage());
          throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_DEL_FAIL_PROC);
        }
        resultTotalInt++;

        // 다건 반환
        resultEventList.add(unitEventVo);
        // 단건 반환
        //resultResDto.setDetail(unitEventVo);

      } // End of for (String evEventId : eventList)

      // 반환결과 Set
      resultResDto.setRows(resultEventList);
      resultResDto.setTotal(resultTotalInt);

    }
    catch (BaseException be) {
      log.info("# delEvent BaseException e :: " + be.toString());
      throw be;
    }
    catch (Exception e) {
      log.info("# delEvent Exception e :: " + e.toString());
      throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_DEL_FAIL);
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 그룹상세 삭제(그룹상품)
   * @param eventGroupDetlList
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  protected EventManageResponseDto delEventGroupDetl (List<String> eventGroupDetlList) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.delEventGroupDetl Start");
    log.debug("# ######################################");
    if (eventGroupDetlList != null) {
      log.debug("# In.eventGroupDetlList.size :: " + eventGroupDetlList.size());
      log.debug("# In.eventGroupDetlList      :: " + eventGroupDetlList.toString());
    }
    else {
      log.debug("# In.eventGroupDetlList is Null or size 0");
    }

    // ======================================================================
    // # 초기화
    // ======================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    int resultInt = 0;
    int resultTotalInt = 0;

    // ======================================================================
    // # 처리
    // ======================================================================

    try {

      if (eventGroupDetlList == null || eventGroupDetlList.size() <= 0) {
        // 삭제대상그룹상세 없음
        resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_GROUP_DETL_DEL_FAIL_INPUT_TARGET.getCode());
        resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_GROUP_DETL_DEL_FAIL_INPUT_TARGET.getMessage());
        resultResDto.setTotal(0);
        return resultResDto;
      }

      for (String evEventGroupDetlId : eventGroupDetlList) {

        // ------------------------------------------------------------------
        // 삭제 - 개별삭제
        // ------------------------------------------------------------------
        resultInt = eventManageMapper.delEventGroupDetl(evEventGroupDetlId);

        if (resultInt <= 0) {
          // # 한건이라도 실패할 경우 모두 롤백
          log.debug("# 삭제건 없음");
          resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_GROUP_DETL_DEL_FAIL_PROC.getCode());
          resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_GROUP_DETL_DEL_FAIL_PROC.getMessage());
          throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_GROUP_DETL_DEL_FAIL_PROC);
        }
        resultTotalInt++;
        // 현재 단건임, 다건인 경우 List로 반환 필요
        //resultResDto.setDetail(unitEventVo);

      } // End of for (EventVo unitEventVo : eventList)

    }
    catch (BaseException be) {
      log.info("# delEventGroupDetl BaseException e :: " + be.toString());
      throw be;
    }
    catch (Exception e) {
      log.info("# delEventGroupDetl Exception e :: " + e.toString());
      throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_GROUP_DETL_DEL_FAIL);
    }

    //  ======================================================================
    // # 반환
    // ======================================================================
    return resultResDto;
  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 등록
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트 등록
   * @param eventList
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  protected EventManageResponseDto addEvent (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.addEvent Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    int resultInt = 0;

    // ========================================================================
    // # 처리
    // ========================================================================

    try {

      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // @ 1. 이벤트 기본정보 등록
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // ----------------------------------------------------------------------
      // 1.1. 세션정보 Set
      // ----------------------------------------------------------------------
      if (SessionUtil.getBosUserVO() != null) {
        eventManageRequestDto.getEventInfo().setCreateId((SessionUtil.getBosUserVO()).getUserId());
        eventManageRequestDto.getEventInfo().setModifyId((SessionUtil.getBosUserVO()).getUserId());
      }
      else {
        eventManageRequestDto.getEventInfo().setCreateId("0");
        eventManageRequestDto.getEventInfo().setModifyId("0");
      }

      // 항목값 추가 Set
      eventManageRequestDto.getEventInfo().setDelYn("N");                             // 삭제여부

      resultInt = eventManageMapper.addEvent(eventManageRequestDto.getEventInfo());

      if (resultInt <= 0) {
        log.debug("# 이벤트 기본정보등록 오류입니다.");
        resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_BASIC_FAIL.getCode());
        resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_BASIC_FAIL.getMessage());
        throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_BASIC_FAIL);
      }
      log.debug("# ################################################");
      log.debug("# New evEventId :: " + eventManageRequestDto.getEventInfo().getEvEventId());
      log.debug("# ################################################");

      // ----------------------------------------------------------------------
      // 1.2. 이벤트 기본정보ID Set
      // ----------------------------------------------------------------------
      eventManageRequestDto.setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());

      // ----------------------------------------------------------------------
      // 등록처리
      // ----------------------------------------------------------------------
      resultResDto = this.procEventCommon(eventManageRequestDto, MODE_ADD);

      // --------------------------------------------------------------------
      // 2. 이벤트별 그룹 상품등록
      // --------------------------------------------------------------------
      if (
              StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventEnums.EventTp.NORMAL.getCode())
                      || StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventEnums.EventTp.SURVEY.getCode())
                      || StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventEnums.EventTp.ATTEND.getCode())
                      || StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventEnums.EventTp.MISSION.getCode())
                      || StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventEnums.EventTp.PURCHASE.getCode())
                      || StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventEnums.EventTp.ROULETTE.getCode())
      ) {
        int resultDelInt = 0;
        // ------------------------------------------------------------------
        // 2.1.2. 그룹 삭제
        // ------------------------------------------------------------------
        resultDelInt = eventManageMapper.delEventGroupByEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
        log.debug("# 그룹 삭제 건수 :: " + resultDelInt);

        // ------------------------------------------------------------------
        // 2.1.1. 그룹상세 삭제
        // ------------------------------------------------------------------
        resultDelInt = eventManageMapper.delEventGroupDetlByEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
        log.debug("# 그룹상세 삭제 건수 :: " + resultDelInt);

        // ------------------------------------------------------------------
        // 2.1.0. 그룹/그룹상세 등록
        // ------------------------------------------------------------------
        resultResDto = this.addEventGroup(eventManageRequestDto);
      }

      // ----------------------------------------------------------------------
      // 등록결과
      // ----------------------------------------------------------------------
      // 이벤트PK
      resultResDto.setEvEventId( eventManageRequestDto.getEventInfo().getEvEventId());
      // 이벤트유형
      resultResDto.setEventTp( eventManageRequestDto.getEventInfo().getEventTp());


    }
    catch (BaseException be) {
      log.info("# addEvent BaseException e :: " + be.toString());
      throw be;
    }
    catch (Exception e) {
      log.info("# addEvent Exception e :: " + e.toString());
      throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_PROC_FAIL);
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }




  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // @ 수정
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트 수정
   * @param eventList
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  protected EventManageResponseDto putEvent (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.putEvent Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    int resultInt = 0;
    int resultDelInt = 0;

    // ========================================================================
    // # 처리
    // ========================================================================

    try {

      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // @ 1. 이벤트 기본정보 수정
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // ----------------------------------------------------------------------
      // 1.1. 세션정보 Set
      // ----------------------------------------------------------------------
      if (SessionUtil.getBosUserVO() != null) {
        eventManageRequestDto.getEventInfo().setCreateId((SessionUtil.getBosUserVO()).getUserId());
        eventManageRequestDto.getEventInfo().setModifyId((SessionUtil.getBosUserVO()).getUserId());
      }
      else {
        eventManageRequestDto.getEventInfo().setCreateId("0");
        eventManageRequestDto.getEventInfo().setModifyId("0");
      }

      resultInt = eventManageMapper.putEvent(eventManageRequestDto.getEventInfo());
      log.debug("# ################################################");
      log.debug("# 이벤트정보 수정 결과 :: " + resultInt);
      log.debug("# ################################################");

      if (resultInt <= 0) {
        log.debug("# 이벤트 기본정보수정 오류입니다.");
        resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_PUT_BASIC_FAIL.getCode());
        resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_PUT_BASIC_FAIL.getMessage());
        throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_PUT_BASIC_FAIL);
      }
      //log.debug("# ################################################");
      //log.debug("# New evEventId :: " + eventManageRequestDto.getEventInfo().getEvEventId());
      //log.debug("# ################################################");

      // ----------------------------------------------------------------------
      // 2. 이벤트접근권한 삭제
      // ----------------------------------------------------------------------
      resultInt = eventManageMapper.delEventUserGroup(eventManageRequestDto.getEventInfo().getEvEventId());
      log.debug("# ################################################");
      log.debug("# 이벤트접근권한 삭제 결과 :: " + resultInt);
      log.debug("# ################################################");

      // ----------------------------------------------------------------------
      // 수정처리
      // ----------------------------------------------------------------------
      resultResDto = this.procEventCommon(eventManageRequestDto, MODE_PUT);

      // --------------------------------------------------------------------
      // 2. 이벤트별 상세 수정
      //    - 삭제 후 재등록
      // --------------------------------------------------------------------
      if (
              StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventEnums.EventTp.NORMAL.getCode())
              || StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventEnums.EventTp.SURVEY.getCode())
              || StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventEnums.EventTp.ATTEND.getCode())
              || StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventEnums.EventTp.MISSION.getCode())
              || StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventEnums.EventTp.PURCHASE.getCode())
              || StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventEnums.EventTp.ROULETTE.getCode())
      ) {
        // ------------------------------------------------------------------
        // 2.1. 일반이벤트
        // ------------------------------------------------------------------

        // ------------------------------------------------------------------
        // 2.1.2. 그룹 삭제
        // ------------------------------------------------------------------
        resultDelInt = eventManageMapper.delEventGroupByEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
        log.debug("# 그룹 삭제 건수 :: " + resultDelInt);

        // ------------------------------------------------------------------
        // 2.1.1. 그룹상세 삭제
        // ------------------------------------------------------------------
        resultDelInt = eventManageMapper.delEventGroupDetlByEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
        log.debug("# 그룹상세 삭제 건수 :: " + resultDelInt);

        // ------------------------------------------------------------------
        // 2.1.0. 그룹/그룹상세 등록
        // ------------------------------------------------------------------
        resultResDto = this.addEventGroup(eventManageRequestDto);
      }

      // --------------------------------------------------------------------
      // 3. 등록 이벤트정보 Set
      // --------------------------------------------------------------------
      if (StringUtil.isEquals(resultResDto.getResultCode(), EventMessage.EVENT_MANAGE_SUCCESS.getCode())) {
        resultResDto.setDetail(eventManageRequestDto.getEventInfo());
        resultResDto.setTotal(1);
      }

    }
    catch (BaseException be) {
      log.info("# addEvent BaseException e :: " + be.toString());
      throw be;
    }
    catch (Exception e) {
      log.info("# addEvent Exception e :: " + e.toString());
      throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_PUT_PROC_FAIL);
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 이벤트 당첨자공지사항 수정
   * @param eventList
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  protected EventManageResponseDto putEventWinnerNotice (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.putEventWinnerNotice Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    int resultInt = 0;

    // ========================================================================
    // # 처리
    // ========================================================================

    try {

      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // @ 1. 이벤트 당첨자공지사항 수정
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // ----------------------------------------------------------------------
      // 1.1. 세션정보 Set
      // ----------------------------------------------------------------------
      if (SessionUtil.getBosUserVO() != null) {
        eventManageRequestDto.getEventInfo().setCreateId((SessionUtil.getBosUserVO()).getUserId());
        eventManageRequestDto.getEventInfo().setModifyId((SessionUtil.getBosUserVO()).getUserId());
      }
      else {
        eventManageRequestDto.getEventInfo().setCreateId("0");
        eventManageRequestDto.getEventInfo().setModifyId("0");
      }

      // ------------------------------------------------------------------------
      // 1. 당첨자공지사항 수정
      // ------------------------------------------------------------------------
      resultInt = eventManageMapper.putEventWinnerNotice(eventManageRequestDto.getEventInfo());
      log.debug("# ################################################");
      log.debug("# 이벤트 당첨자공지사항 수정 결과 :: " + resultInt);
      log.debug("# ################################################");

      if (resultInt <= 0) {
        log.debug("# 이벤트 당첨자공지사항 수정 오류입니다.");
        resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_PUT_WINNER_NORIXW_FAIL.getCode());
        resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_PUT_WINNER_NORIXW_FAIL.getMessage());
        throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_PUT_WINNER_NORIXW_FAIL);
      }

      // ----------------------------------------------------------------------
      // 2. 이벤트정보
      // ----------------------------------------------------------------------
      EventVo resultDetailInfo = eventManageMapper.selectEventInfo(eventManageRequestDto.getEventInfo().getEvEventId());
      resultResDto.setEventInfo(resultDetailInfo);
      if (resultDetailInfo == null || StringUtil.isEmpty(resultDetailInfo.getEvEventId()) || StringUtil.isEquals(resultDetailInfo.getEvEventId(), "0")) {
        log.debug("# 이벤트 기본정보가 없습니다.");
        resultResDto.setResultCode(EventMessage.EVENT_MANAGE_DETAIL_NO_DATA.getCode());
        resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_DETAIL_NO_DATA.getMessage());
        return resultResDto;
      }
      resultResDto.setTotal(1);

    }
    catch (BaseException be) {
      log.info("# addEvent BaseException e :: " + be.toString());
      throw be;
    }
    catch (Exception e) {
      log.info("# addEvent Exception e :: " + e.toString());
      throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_PUT_PROC_FAIL);
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
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
  @SuppressWarnings("unused")
  protected EventManageResponseDto putWinnerLottery (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.putWinnerLottery Start");
    log.debug("# ######################################");
    log.debug("# In.eventManageRequestDto      :: " + eventManageRequestDto.toString());

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    int resultInt = 0;
    int resultDelInt = 0;

    // ========================================================================
    // # 처리
    // ========================================================================

    try {

      if (StringUtil.isEquals(eventManageRequestDto.getWinnerSelectTp(), WinnerSelectTp.DIRECT.getCode())) {
        // --------------------------------------------------------------------
        // 직접선택
        // --------------------------------------------------------------------
        resultResDto = this.putWinnerLotteryDirect(eventManageRequestDto);
      }
      else {
        // --------------------------------------------------------------------
        // 랜덤선택
        // --------------------------------------------------------------------
        resultResDto = this.putWinnerLotteryRandom(eventManageRequestDto);
      }

    }
    catch (BaseException be) {
      log.info("# putWinnerLottery BaseException e :: " + be.toString());
      be.printStackTrace();
      throw be;
    }
    catch (Exception e) {
      log.info("# putWinnerLottery Exception e :: " + e.toString());
      e.printStackTrace();
      // 당첨자 처리가 실패했습니다.
      throw new BaseException(EventMessage.EVENT_MANAGE_WINNER_PUT_FAIL);
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 이벤트참여 댓글 차단/차단해제
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  protected EventManageResponseDto putAdminSecretYn (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.putAdminSecretYn Start");
    log.debug("# ######################################");
    log.debug("# In.eventManageRequestDto      :: " + eventManageRequestDto.toString());

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    int resultInt = 0;
    int resultDelInt = 0;

    // ========================================================================
    // # 처리
    // ========================================================================

    try {

      EventJoinVo eventJoinVo = new EventJoinVo();

      eventJoinVo.setEvEventJoinId(eventManageRequestDto.getEvEventJoinId());
      eventJoinVo.setEvEventId(eventManageRequestDto.getEvEventId());
      eventJoinVo.setAdminSecretYn(eventManageRequestDto.getAdminSecretYn());   // Y:차단, N:차단해제

      resultInt = eventManageMapper.putAdminSecretYn(eventJoinVo);

      if (resultInt <= 0) {
        // # 한건이라도 실패할 경우 모두 롤백

        if (StringUtil.isEquals(eventManageRequestDto.getAdminSecretYn(), "Y")) {
          // 차단처리
          resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SECRET_PUT_HIDDEN_FAIL_PROC.getCode());
          resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SECRET_PUT_HIDDEN_FAIL_PROC.getMessage());
          throw new BaseException(EventMessage.EVENT_MANAGE_SECRET_PUT_HIDDEN_FAIL_PROC);
        }
        else {
          // 차단해제 처리
          resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SECRET_PUT_DISPLAY_FAIL_PROC.getCode());
          resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SECRET_PUT_DISPLAY_FAIL_PROC.getMessage());
          throw new BaseException(EventMessage.EVENT_MANAGE_SECRET_PUT_DISPLAY_FAIL_PROC);
        }
      }

      resultResDto.setTotal(resultInt);
    }
    catch (BaseException be) {
      log.info("# putWinnerLottery BaseException e :: " + be.toString());
      be.printStackTrace();
      throw be;
    }
    catch (Exception e) {
      log.info("# putWinnerLottery Exception e :: " + e.toString());
      e.printStackTrace();
      // 댓글 차단 처리가 실패했습니다. / 댓글 차단해제 처리가 실패했습니다.
      if (StringUtil.isEquals(eventManageRequestDto.getAdminSecretYn(), "Y")) {
        // 차단처리
        throw new BaseException(EventMessage.EVENT_MANAGE_SECRET_PUT_HIDDEN_FAIL);
      }
      else {
        // 차단해제 처리
        throw new BaseException(EventMessage.EVENT_MANAGE_SECRET_PUT_DISPLAY_FAIL);
      }
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }



  // ##########################################################################
  // private
  // ##########################################################################

  /**
   * 당첨자처리-직접선택
   *  - 관리자추첨 이벤트 : 일반이벤트, 설문이벤트, 스탬프(구매), 체험단이벤트
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  private EventManageResponseDto putWinnerLotteryDirect (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.putWinnerLotteryDirect Start");
    log.debug("# ######################################");
    log.debug("# In.eventManageRequestDto      :: " + eventManageRequestDto.toString());

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    resultResDto.setTotal(0);
    int resultInt = 0;
    int resultTotInt = 0;
    List<EventJoinVo> couponPointList   = null;  // 당첨대상쿠폰/적립금리스트
    List<EventJoinVo> couponResultList = null;  // 당첨성공쿠폰리스트


    List<EventJoinVo> resultFailList = new ArrayList<EventJoinVo>();

    // ========================================================================
    // # 처리
    // ========================================================================
    try {

      // ----------------------------------------------------------------------
      // 세션정보 Set
      // ----------------------------------------------------------------------
      String sessionUserId = "";

      if (SessionUtil.getBosUserVO() != null) {
        sessionUserId = (SessionUtil.getBosUserVO()).getUserId();
      }
      else {
        sessionUserId = "0";
      }


      if (eventManageRequestDto.getWinnerEventJoinList() != null && eventManageRequestDto.getWinnerEventJoinList().size() > 0) {
        log.debug("#  getWinnerEventJoinList size :: " + eventManageRequestDto.getWinnerEventJoinList().size());
        // --------------------------------------------------------------------
        // 개별 Loop 등록 방식
        // --------------------------------------------------------------------
        for (EventJoinVo eventJoinVo : eventManageRequestDto.getWinnerEventJoinList()) {

          if (StringUtil.isEquals(eventJoinVo.getEventBenefitTp(), EventBenefitType.COUPON.getCode())) {
            // ----------------------------------------------------------------
            // 당첨혜택유형 : 쿠폰
            // ----------------------------------------------------------------

            if (StringUtil.isEquals(eventJoinVo.getEventTp(), EventType.NORMAL.getCode()) ||
                StringUtil.isEquals(eventJoinVo.getEventTp(), EventType.SURVEY.getCode()) ||
                StringUtil.isEquals(eventJoinVo.getEventTp(), EventType.EXPERIENCE.getCode()) ) {
              // --------------------------------------------------------------
              // 이벤트유형 : 일반/설문 인 경우 : 쿠폰정보 한번만 조회
              // --------------------------------------------------------------
              if (couponPointList == null || couponPointList.size() <= 0) {
                // 한번만 조회

                if (StringUtil.isEquals(eventJoinVo.getEventTp(), EventType.EXPERIENCE.getCode()) ) {
                  // 체험단인 경우 : EV_EVENT_EXPERIENCE  에 쿠폰정보 있음
                  couponPointList = eventManageMapper.selectEventJoinBenefitCouponByExperienceList(eventJoinVo.getEvEventJoinId(), eventJoinVo.getEvEventId(), eventJoinVo.getUrUserId(), null);
                }
                else {
                  // 일반/설문 인 경우 EV_EVENT_COUPON에 쿠폰정보 있음
                  couponPointList = eventManageMapper.selectEventJoinBenefitCouponList(eventJoinVo.getEvEventJoinId(), eventJoinVo.getEvEventId(), eventJoinVo.getUrUserId(), null);
                }

                if (couponPointList == null || couponPointList.size() <= 0) {
                  log.debug("# 혜택 등록 쿠폰 오류입니다.");
                  resultResDto.setResultCode(EventMessage.EVENT_MANAGE_WINNER_PUT_BENEFIT_POINT.getCode());
                  resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_WINNER_PUT_BENEFIT_POINT.getMessage());
                  return resultResDto;
                }

                if (StringUtil.isEquals(eventJoinVo.getEventTp(), EventType.EXPERIENCE.getCode())) {
                  // 체험단인 경우
                  if (couponPointList != null && couponPointList.size() > 1) {
                    // 체험단 이벤트에 등록된 쿠폰이 1개 초과 존재
                    log.debug("# 체험단 이벤트에 등록된 쿠폰이 1개 초과 존재");
                    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_WINNER_PUT_BENEFIT_COUPON_EXPERIENCE.getCode());
                    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_WINNER_PUT_BENEFIT_COUPON_EXPERIENCE.getMessage());
                    return resultResDto;
                  }
                }

                if (couponPointList != null) {
                  log.debug("# couponList :: " + couponPointList.toString());
                }
                else {
                  log.debug("# couponList is Null");
                }
              }
            }
            else if (StringUtil.isEquals(eventJoinVo.getEventTp(), EventType.PURCHASE.getCode()))  {
              // --------------------------------------------------------------
              // 이벤트유형 : 스탬프(구매) : 쿠폰정보 매번 조회
              // --------------------------------------------------------------
              couponPointList = eventManageMapper.selectEventJoinBenefitCouponList(eventJoinVo.getEvEventJoinId(), eventJoinVo.getEvEventId(), eventJoinVo.getUrUserId(), eventJoinVo.getStampCnt()+"");
            }

            // ----------------------------------------------------------------
            // 쿠폰발급처리
            // ----------------------------------------------------------------
            couponResultList = new ArrayList<EventJoinVo>();
            this.procEventBenefitInfoCoupon(eventJoinVo, couponPointList, couponResultList);
          }
          else if (StringUtil.isEquals(eventJoinVo.getEventBenefitTp(), EventBenefitType.POINT.getCode())) {
            // ----------------------------------------------------------------
            // 당첨혜택유형 : 적립금
            // ----------------------------------------------------------------
            if (couponPointList == null || couponPointList.size() <= 0) {
              //  적립금조회
              couponPointList = eventManageMapper.selectEventJoinBenefitPointList(eventJoinVo.getEvEventJoinId(), eventJoinVo.getEvEventId(), eventJoinVo.getUrUserId());

              if (couponPointList == null || couponPointList.size() <= 0) {
                log.debug("# 혜택 등록 적립금 오류입니다.");
                resultResDto.setResultCode(EventMessage.EVENT_MANAGE_WINNER_PUT_BENEFIT_POINT.getCode());
                resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_WINNER_PUT_BENEFIT_POINT.getMessage());
                return resultResDto;
              }
              if (couponPointList.size() > 1) {
                log.debug("# 혜택 등록 적립금 여러개 존재합니다.");
                resultResDto.setResultCode(EventMessage.EVENT_MANAGE_WINNER_PUT_BENEFIT_POINT_TOOMANY.getCode());
                resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_WINNER_PUT_BENEFIT_POINT_TOOMANY.getMessage());
                return resultResDto;
              }
            }
              // 적립금ID SET
              eventJoinVo.setPmPointId(couponPointList.get(0).getPmPointId());
              // ----------------------------------------------------------------
              // 적립금발급처리
              // ----------------------------------------------------------------
              this.procEventBenefitInfoPoint (eventJoinVo, couponPointList);

          }
          else if (StringUtil.isEquals(eventJoinVo.getEventBenefitTp(), EventBenefitType.GIFT.getCode()) ||
                   StringUtil.isEquals(eventJoinVo.getEventBenefitTp(), EventBenefitType.AUTO.getCode())) {
            // ----------------------------------------------------------------
            // 당첨혜택유형 : 경품/응모
            // ----------------------------------------------------------------
            EventJoinVo giftInfo = eventManageMapper.selectEventJoinBenefitGiftEnterInfo(eventJoinVo.getEvEventJoinId(), eventJoinVo.getEvEventId(), eventJoinVo.getUrUserId(), eventJoinVo.getStampCnt()+"");

            if (giftInfo != null && StringUtil.isNotEmpty(giftInfo.getBenefitNm())) {
              eventJoinVo.setWinnerYn("Y");                         // 당첨여부(당첨)
              eventJoinVo.setBenefitNm(giftInfo.getBenefitNm());    // 당첨혜택명응모내용)
            }
            else {
              eventJoinVo.setWinnerYn("N");                         // 당첨여부(미당첨)
            }
          }

          // ------------------------------------------------------------------
          // @ 당첨 결과 처리
          // ------------------------------------------------------------------
          // 추가 값 Set
          eventJoinVo.setLotteryId(sessionUserId);    // 추첨자ID

          if (StringUtil.isEquals(eventJoinVo.getWinnerYn(), "Y")) {
            // 당첨여부 : Y

            // ----------------------------------------------------------------
            // 당첨정보 갱신 :
            // 변경항목
            //  - WINNER_YN : Y
            //  - LOTTERY_DT : NOW()
            //  - LOTTERY_ID :
            //  - BENEFIT_NM : eventJoinVo.setBenefitNm
            //  - EVENT_BENEFIT_TP
            //  - PM_POINT_ID
            // ----------------------------------------------------------------

            eventJoinVo.setBenefitNm(eventJoinVo.getCombineBenefitName()); // 혜택명 재정의
            eventJoinVo.setHandwrittenLotteryTp(eventManageRequestDto.getHandwrittenLotteryTp()); // 관리자 추첨 당첨방법
            resultInt = eventManageMapper.putWinnerLotteryInfo(eventJoinVo);
            log.debug("# 당첨여부 업데이트 결과 :: " + resultInt);

            if (resultInt <= 0) {
              resultFailList.add(eventJoinVo);
              break;
            }

            // ----------------------------------------------------------------
            // 쿠폰인 경우 쿠폰 등록
            // ----------------------------------------------------------------
            log.debug("# EventBenefitTp :: " + eventJoinVo.getEventBenefitTp());
            if (StringUtil.isEquals(eventJoinVo.getEventBenefitTp(), EventBenefitType.COUPON.getCode())) {
              // 이벤트당첨혜택 : 쿠폰
              if (couponResultList == null) {
                log.debug("# couponResultList is Null");
              }
              else {
                log.debug("# couponResultList.size :: " + couponResultList.size());
              }
              if (couponResultList != null && couponResultList.size() > 0) {
                // 쿠폰발급결과 존재 시
                int i = 0;
                for (EventJoinVo couoonVo : couponResultList) {
                  couoonVo.setSort((i+1)+"");
                  eventManageMapper.addEventJoinCoupon(couoonVo);
                  i++;
                }
              }
            }

            // ----------------------------------------------------------------
            // @ 당첨존재여부 업데이트 : 동일이벤트의 동일사용자의 WINNER_EXIST_YN 업데이트
            // ----------------------------------------------------------------
            if (StringUtil.isEquals(eventJoinVo.getWinnerYn(), "Y")) {
              // 당첨여부가 'Y'인 경우만 실행
              resultInt = eventManageMapper.putWinnerExistYn(eventJoinVo.getEvEventId(), eventJoinVo.getUrUserId(), "Y");
              log.debug("# 당첨여부존재 업데이트 결과 :: " + resultInt);
            }

            resultTotInt++;
          }
          else {
            // 당첨여부 : N
            log.debug("# 당첨여부 N");
          }

          // 실패건 Set
          resultResDto.setRows(resultFailList);


        } // End of for (EventJoinVo eventJoinVo : eventManageRequestDto.getWinnerEventJoinList())
        log.debug("# 처리건수 resultTotInt :: " + resultTotInt);

        // 처리건수 Set
        resultResDto.setTotal(resultTotInt);

        // --------------------------------------------------------------------
        // 처리건 오류 Set
        // --------------------------------------------------------------------
        if (resultTotInt <= 0) {
          // 당첨 처리건이 없습니다.
          resultResDto.setResultCode(EventMessage.EVENT_MANAGE_WINNER_PUT_NO_PROC.getCode());
          resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_WINNER_PUT_NO_PROC.getMessage());
        }
      } // End of if (eventManageRequestDto.getWinnerEventJoinList() != null && eventManageRequestDto.getWinnerEventJoinList().size() > 0)
      else {
        // 직접선택한 당첨자를 확인하세요.
        throw new BaseException(EventMessage.EVENT_MANAGE_WINNER_PUT_DIRECT_NO_DATA);
      }

    }
    catch (BaseException be) {
      log.info("# putWinnerLotteryDirect BaseException e :: " + be.toString());
      be.printStackTrace();
      throw be;
    }
    catch (Exception e) {
      log.info("# putWinnerLotteryDirect Exception e :: " + e.toString());
      e.printStackTrace();
      // 당첨자 처리가 실패했습니다.
      throw new BaseException(EventMessage.EVENT_MANAGE_WINNER_PUT_FAIL);
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 당첨처리-랜덤
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  private EventManageResponseDto putWinnerLotteryRandom (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.putWinnerLotteryRandom Start");
    log.debug("# ######################################");
    log.debug("# In.eventManageRequestDto      :: " + eventManageRequestDto.toString());

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    int resultInt = 0;

    List<EventJoinVo> resultFailList = new ArrayList<EventJoinVo>();

    // ========================================================================
    // # 처리
    // ========================================================================
    // ------------------------------------------------------------------------
    // 세션정보 Set
    // ------------------------------------------------------------------------
    String sessionUserId = "";

    if (SessionUtil.getBosUserVO() != null) {
      sessionUserId = (SessionUtil.getBosUserVO()).getUserId();
    }
    else {
      sessionUserId = "0";
    }

    // 랜덤 차등 해택 리스트
    List<EventJoinVo> randomBenefitList = eventManageRequestDto.getWinnerEventJoinList();

    if (randomBenefitList == null) {
      EventJoinVo eventJoin = new EventJoinVo();
      List<EventJoinVo> newRandomBenefitList =  new ArrayList<EventJoinVo>();
      eventJoin.setWinnerCnt(eventManageRequestDto.getWinnerCnt());
      newRandomBenefitList.add(eventJoin);
      randomBenefitList = newRandomBenefitList;
    }

    try {

      for (EventJoinVo randomBenefit : randomBenefitList) {
        // ----------------------------------------------------------------------
        // 1. 랜덤당첨자 조회
        // ----------------------------------------------------------------------
        EventJoinVo eventJoinVo = new EventJoinVo();

        eventJoinVo.setEmployeeExceptYn(eventManageRequestDto.getEmployeeExceptYn());
        eventJoinVo.setEventJoinYn(eventManageRequestDto.getEventJoinYn());
        eventJoinVo.setEvEventId(eventManageRequestDto.getEvEventId());
        eventJoinVo.setWinnerCnt(randomBenefit.getWinnerCnt());
        eventJoinVo.setRandomBenefitName(randomBenefit.getRandomBenefitName());

        List<EventJoinVo> eventJoinList = eventManageMapper.selectJoinWinnerRandomTarget(eventJoinVo);

        if (eventJoinList == null || eventJoinList.size() <= 0) {

          // 당첨대상자가 없습니다.
          resultResDto.setResultCode(EventMessage.EVENT_MANAGE_WINNER_PUT_RANDOM_NO_DATA.getCode());
          resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_WINNER_PUT_RANDOM_NO_DATA.getMessage());
          return resultResDto;
        }

        log.debug("# 랜덤대상자 :: " + eventJoinList.size());
        // ----------------------------------------------------------------------
        // 랜덤대상자 Set
        // ----------------------------------------------------------------------
        eventManageRequestDto.setWinnerEventJoinList(eventJoinList);

        // ----------------------------------------------------------------------
        // 당첨처리
        // ----------------------------------------------------------------------
        resultResDto = this.putWinnerLotteryDirect(eventManageRequestDto);
      }



      //// ----------------------------------------------------------------------
      //// 2. 랜덤당첨 처리
      //// ----------------------------------------------------------------------
      //// EV_EVENT_JOIN_ID List
      //List<String> evEventJoinIdList = new ArrayList<String>();
      //
      //for (EventJoinVo unitEventJoinVo : eventJoinList) {
      //  evEventJoinIdList.add(unitEventJoinVo.getEvEventJoinId());
      //} // End of for (EventJoinVo eventJoinVo : eventManageRequestDto.getWinnerEventJoinList())
      //
      //
      //eventJoinVo.setWinnerYn("Y");
      //eventJoinVo.setLoginId(sessionUserId);
      //eventJoinVo.setEvEventJoinIdList(evEventJoinIdList);
      //resultInt = eventManageMapper.putWinnerLottery(eventJoinVo);
      //
      //// ----------------------------------------------------------------------
      //// 처리건 오류 Set
      //// ----------------------------------------------------------------------
      //if (resultInt <= 0) {
      //  // 당첨 처리건이 없습니다.
      //  resultResDto.setResultCode(EventMessage.EVENT_MANAGE_WINNER_PUT_NO_PROC.getCode());
      //  resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_WINNER_PUT_NO_PROC.getMessage());
      //
      //  return resultResDto;
      //}
      //
      //// 처리건수 Set
      //resultResDto.setTotal(eventJoinList.size());

    }
    catch (BaseException be) {
      log.info("# putWinnerLotteryRandom BaseException e :: " + be.toString());
      be.printStackTrace();
      throw be;
    }
    catch (Exception e) {
      log.info("# putWinnerLotteryRandom Exception e :: " + e.toString());
      e.printStackTrace();
      // 당첨자 처리가 실패했습니다.
      throw new BaseException(EventMessage.EVENT_MANAGE_WINNER_PUT_FAIL);
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 쿠폰발급처리
   * @param eventJoinVo
   * @param couponList
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private void procEventBenefitInfoCoupon (EventJoinVo eventJoinVo, List<EventJoinVo> couponList, List<EventJoinVo> couponResultList) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageService.procEventBenefitInfoCoupon Start");
    log.debug("# ######################################");
    log.debug("# In.eventJoinVo :: " + eventJoinVo.toString());

    List<EventCouponVo> resultCouponList = new ArrayList<>();
    int couponIdx = 0;
    int issuedCntTotal = 0;
    String benefitNm = "";

    if (couponList != null && couponList.size() > 0) {

      for (EventJoinVo coupon : couponList) {
        // 쿠폰별 loop
        String urUserId         = eventJoinVo.getUrUserId();
        String pmCouponId       = coupon.getPmCouponId();
        String couponIssueCnt   = coupon.getCouponCnt();
        int    iCouponIssueCnt  = Integer.parseInt(couponIssueCnt);
        int issuedCnt = 0;

        for (int i = 0; i < iCouponIssueCnt; i++) {
          // 발급수량만큼 loop
          CouponEnums.AddCouponValidation couponResult = promotionCouponBiz.addCouponByOne(Long.parseLong(pmCouponId), Long.parseLong(urUserId));
          log.debug("# couponResult :: " + couponResult.toString());
          if (couponResult.equals(CouponEnums.AddCouponValidation.PASS_VALIDATION)) {
            issuedCnt++;
          }
        } // End of for (int i = 0; i < iCouponIssueCnt; i++)

        if (issuedCnt > 0) {
          if (couponIdx > 0) {
            benefitNm += ",";
          }
          //benefitNm += coupon.getDisplayCouponNm();
          // 당첨시 관리자쿠폰명 Set
          benefitNm += coupon.getBosCouponNm();
        }

        log.debug("# issuedCnt :: " + issuedCnt);
        issuedCntTotal += issuedCnt;

        // --------------------------------------------------------------------
        // 쿠폰발급결과 Set
        // --------------------------------------------------------------------
        coupon.setCouponCnt(issuedCnt+"");
        couponResultList.add(coupon);

        couponIdx++;
      } // End of for (EventJoinVo coupon : couponList)
      log.debug("# issuedCntTotal :: " + issuedCntTotal);
    }

    if (couponResultList == null) {
      log.debug("# couponResultList is Null");
    }
    else {
      log.debug("# couponResultList.size() :: " + couponResultList.size());
      log.debug("# couponResultList :: " + couponResultList.toString());
    }

    if (issuedCntTotal > 0) {
      eventJoinVo.setWinnerYn("Y");         // 당첨여부(당첨)
      eventJoinVo.setBenefitNm(benefitNm);  // 당첨혜택명
    }
    else {
      eventJoinVo.setWinnerYn("N");         // 당첨여부(미당첨)
    }
  }

  /**
   * 적립금발급처리
   * @param eventJoinVo
   * @param couponList
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private void procEventBenefitInfoPoint (EventJoinVo eventJoinVo, List<EventJoinVo> pointList) throws Exception {
    log.debug("# ######################################");
    log.debug("# EventManageService.procEventBenefitInfoPoint Start");
    log.debug("# ######################################");
    log.debug("# In.eventJoinVo :: " + eventJoinVo.toString());

    List<EventCouponVo> resultCouponList = new ArrayList<>();

    if (pointList != null && pointList.size() > 0) {
      log.debug("# pointList :: " + pointList.toString());
    }
    else {
      log.debug("# pointList is Null");
    }

    if (pointList != null && pointList.size() > 0) {

      String urUserId   = pointList.get(0).getUrUserId();
      String pmPointId  = pointList.get(0).getPmPointId();
      String evEventId  = pointList.get(0).getEvEventId();
      String pointNm    = pointList.get(0).getPointNm();

      log.debug("# urUserId  :: " + urUserId);
      log.debug("# pmPointId :: " + pmPointId);
      log.debug("# evEventId :: " + evEventId);

      ApiResult<?> pointResult = pointBiz.depositEventPoint(Long.parseLong(urUserId), Long.parseLong(pmPointId), Long.parseLong(evEventId), "");

      if (pointResult != null) {
        log.debug("# pointResult :: " + pointResult.getMessageEnum());
      }

      if (BaseEnums.Default.SUCCESS.equals(pointResult.getMessageEnum()) || PointEnums.PointUseMessage.PARTIAL_DEPOSIT_OVER_LIMIT.equals(pointResult.getMessageEnum())) {
        eventJoinVo.setWinnerYn("Y");         // 당첨여부(당첨)
        eventJoinVo.setBenefitNm(pointNm);    // 당첨혜택명(포인트명)
      }
      else {
        // 적립금 발급 실패 처리
        eventJoinVo.setWinnerYn("N");         // 당첨여부(미당첨)
      }
    }
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 이벤트유형별 이벤트등록
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 이벤트 등록/수정 공통처리
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  private EventManageResponseDto procEventCommon (EventManageRequestDto eventManageRequestDto, String mode) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.procEventCommon Start");
    log.debug("# ######################################");
    if (eventManageRequestDto != null) {
      log.debug("# In.eventManageRequestDto :: " + eventManageRequestDto.toString());
    }
    else {
      log.debug("# In.eventManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    int resultInt = 0;

    // ========================================================================
    // # 처리
    // ========================================================================

    try {

      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++**
      // @ 0. 이벤트 적용범위 등록
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++**
      if(MODE_PUT.equals(mode) && StringUtil.isNotEmpty(eventManageRequestDto.getEventInfo().getEvEventId())) {
        // 적용범위 삭제
        eventManageMapper.removeEventCoverage(eventManageRequestDto.getEventInfo().getEvEventId());
      }

      if (eventManageRequestDto.getInsertData() != null && !eventManageRequestDto.getInsertData().isEmpty()) {
          eventManageRequestDto.setInsertRequestDtoList((List<CoverageVo>) BindUtil
                  .convertJsonArrayToDtoList(eventManageRequestDto.getInsertData(), CoverageVo.class));
      }
      List<CoverageVo> insertRequestDtoList = eventManageRequestDto.getInsertRequestDtoList();

      if (insertRequestDtoList != null && !insertRequestDtoList.isEmpty()) {
        for (int i = 0; i < insertRequestDtoList.size(); i++) {
          insertRequestDtoList.get(i).setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
          insertRequestDtoList.get(i).setUserId(eventManageRequestDto.getEventInfo().getCreateId());
        }

        // 적용범위 등록
        eventManageMapper.addEventCoverage(insertRequestDtoList);
      }

      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++**
      // @ 1. 이벤트접근권한 등록 - N건
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++**
      log.debug("# eventManageRequestDto.getUserGroupList().size() :: " + eventManageRequestDto.getUserGroupList().size());
      if (eventManageRequestDto.getUserGroupList() != null && eventManageRequestDto.getUserGroupList().size() > 0) {
        for (EvUserGroupVo userGroupVo : eventManageRequestDto.getUserGroupList()) {
          userGroupVo.setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
          resultInt = eventManageMapper.addEventUserGroup(userGroupVo);
        }
      }

      // 접근권한 필수 제외
      //if (resultInt <= 0) {
      //  log.debug("# 이벤트 접근권한설정 오류입니다.");
      //  resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_USER_GROUP_FAIL.getCode());
      //  resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_USER_GROUP_FAIL.getMessage());
      //  throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_USER_GROUP_FAIL);
      //}

      // ----------------------------------------------------------------------
      // 2. 이벤트유형 별 상세 등록
      // ----------------------------------------------------------------------
      if (StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventTp.NORMAL.getCode())) {
        // --------------------------------------------------------------------
        // 일반이벤트
        // --------------------------------------------------------------------
        resultResDto = this.addEventNormal(eventManageRequestDto, mode);

      }
      else if (StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventTp.SURVEY.getCode())) {
        // --------------------------------------------------------------------
        // 설문이벤트
        // --------------------------------------------------------------------
        resultResDto = this.addEventSurvey(eventManageRequestDto, mode);
      }
      else if (StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventTp.ATTEND.getCode()) ||
               StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventTp.MISSION.getCode()) ||
               StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventTp.PURCHASE.getCode())) {
        // --------------------------------------------------------------------
        // 스탬프(출석), 스탬프(미션), 스탬프(구매)
        // --------------------------------------------------------------------
        resultResDto = this.addEventStamp(eventManageRequestDto, mode);
      }
      else if (StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventTp.ROULETTE.getCode())) {
        // --------------------------------------------------------------------
        // 룰렛이벤트
        // --------------------------------------------------------------------
        resultResDto = this.addEventRoulette(eventManageRequestDto, mode);
      }
      else if (StringUtil.isEquals(eventManageRequestDto.getEventInfo().getEventTp(), EventTp.EXPERIENCE.getCode())) {
        // --------------------------------------------------------------------
        // 체험단이벤트
        // --------------------------------------------------------------------
        resultResDto = this.addEventExperience(eventManageRequestDto, mode);
      }


      // ----------------------------------------------------------------------
      // 등록결과
      // ----------------------------------------------------------------------
      // 이벤트PK
      resultResDto.setEvEventId( eventManageRequestDto.getEventInfo().getEvEventId());
      // 이벤트유형
      resultResDto.setEventTp( eventManageRequestDto.getEventInfo().getEventTp());


    }
    catch (BaseException be) {
      log.info("# addEvent BaseException e :: " + be.toString());
      throw be;
    }
    catch (Exception e) {
      log.info("# addEvent Exception e :: " + e.toString());
      throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_PROC_FAIL);
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 일반이벤트 등록
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  private EventManageResponseDto addEventNormal (EventManageRequestDto eventManageRequestDto, String mode) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.addEventNormal Start");
    log.debug("# ######################################");
    log.debug("# In.evEventId      :: " + eventManageRequestDto.getEvEventId());

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    int resultInt = 0;

    // ========================================================================
    // # 처리
    // ========================================================================

    try {

      // ----------------------------------------------------------------------
      // 0. 수정인 경우 해당 테이블 삭제
      // ----------------------------------------------------------------------
      if (StringUtil.isEquals(mode, MODE_PUT)) {

        // --------------------------------------------------------------------
        // 0.1. 이벤트쿠폰 삭제
        // --------------------------------------------------------------------
//        resultInt = eventManageMapper.delEventCoupon(eventManageRequestDto.getEventInfo().getEvEventId());
//        log.debug("# 이벤트쿠폰 삭제 결과 :: " + resultInt);

        // --------------------------------------------------------------------
        // 0.2. 이벤트댓글코드 삭제
        // --------------------------------------------------------------------
//        resultInt = eventManageMapper.delEventCommentCode(eventManageRequestDto.getEventInfo().getEvEventId());
//        log.debug("# 이벤트댓글코드 삭제 결과 :: " + resultInt);

        // --------------------------------------------------------------------
        // 0.3. 이벤트상세-일반이벤트 삭제
        // --------------------------------------------------------------------
        // resultInt = eventManageMapper.delEventNormal(eventManageRequestDto.getEventInfo().getEvEventId());
        // log.debug("# 일반이벤트 삭제 결과 :: " + resultInt);
      }else{
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // @ 1-1. 일반이벤트 상세정보정보 등록
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 일반이벤트상세.이벤트ID Set
        eventManageRequestDto.getEventNormalInfo().setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
        resultInt = eventManageMapper.addEventNormal(eventManageRequestDto.getEventNormalInfo());

        if (resultInt <= 0) {
          log.debug("# 일반이벤트 상세정보등록 오류입니다.");
          resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_NORMAL_FAIL.getCode());
          resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_NORMAL_FAIL.getMessage());
          throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_NORMAL_FAIL);
        }
      }




      log.debug("# commentYn :: " + eventManageRequestDto.getEventNormalInfo().getCommentYn());
      // ----------------------------------------------------------------------
      // 댓글허용이 Y인 경우만 처리
      // ----------------------------------------------------------------------
//      if (StringUtil.isEquals(eventManageRequestDto.getEventNormalInfo().getCommentYn(), YES)) {
      // ----------------------------------------------------------------------
      // 참여정보 : 댓글모음 경우만 처리
      // ----------------------------------------------------------------------
      if (StringUtil.isEquals(eventManageRequestDto.getEventNormalInfo().getNormalEventTp(), EventEnums.NormalEventType.COMMENT.getCode())) {

        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // @ 1-2. 일반이벤트 이벤트댓글구분코드 등록 - N건
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 항목값 추가 Set
        if (eventManageRequestDto.getEventCommentCodeList() != null && eventManageRequestDto.getEventCommentCodeList().size() > 0) {

          resultInt = eventManageMapper.delEventCommentCode(eventManageRequestDto.getEventInfo().getEvEventId());
          log.debug("# 이벤트댓글코드 삭제 결과 :: " + resultInt);

          for (EventVo eventCommentCode : eventManageRequestDto.getEventCommentCodeList()) {
            //eventCommentCode.setDelYn("N");                             // 삭제여부
            eventCommentCode.setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
            resultInt = eventManageMapper.addEventCommentCode(eventCommentCode);
          }
        }

        if (resultInt <= 0) {
          log.debug("# 일반이벤트 댓글구분등록 오류입니다.");
          resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_NORMAL_COMMENT_FAIL.getCode());
          resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_NORMAL_COMMENT_FAIL.getMessage());
          throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_NORMAL_COMMENT_FAIL);
        }
      }
      // ----------------------------------------------------------------------
      // 참여정보 : 응모버튼, 댓글모음 경우만 처리
      // ----------------------------------------------------------------------
      if (!StringUtil.isEquals(eventManageRequestDto.getEventNormalInfo().getNormalEventTp(), EventEnums.NormalEventType.NONE.getCode())) {
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // @ 1-3. 일반이벤트 쿠폰정보 등록 - N건
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        if (StringUtil.isEquals(eventManageRequestDto.getEventNormalInfo().getEventBenefitTp(), EventBenefitType.COUPON.getCode())) {
          // ------------------------------------------------------------------
          // 당첨자혜택유형 == 쿠폰
          // ------------------------------------------------------------------
          String timestamp = DateUtil.getCurrentDate("yyyyMMddHHmm");
          List<EventVo> couponList = eventManageMapper.getEventCouponList(eventManageRequestDto.getEventInfo().getEvEventId());

          // 항목값 추가 Set
          if (eventManageRequestDto.getEventCouponList() != null && eventManageRequestDto.getEventCouponList().size() > 0
                  && (!StringUtil.isEquals(mode, MODE_PUT) || ( StringUtil.isEquals(mode, MODE_PUT) && (Long.parseLong((eventManageRequestDto.getEventInfo().getStartDt())) > Long.parseLong(timestamp)) ))) {
             List<EventVo> addVoList = eventManageRequestDto.getEventCouponList().stream().filter(f -> couponList.stream().noneMatch(t -> f.getPmCouponId().equals(t.getPmCouponId()))).collect(Collectors.toList());

            List<EventVo> delVoList = couponList.stream().filter(f -> eventManageRequestDto.getEventCouponList().stream().noneMatch(t -> f.getPmCouponId().equals(t.getPmCouponId()))).collect(Collectors.toList());
            if(delVoList != null){
              for(EventVo vo : delVoList){
                resultInt = eventManageMapper.delEventPmCouponId(eventManageRequestDto.getEventInfo().getEvEventId(), vo.getPmCouponId());
              }
            }

            if(addVoList != null){

              for (EventVo eventCoupon : addVoList) {
                eventCoupon.setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
                  resultInt = eventManageMapper.addEventCoupon(eventCoupon);

                if (resultInt <= 0) {
                  log.debug("# 일반이벤트 쿠폰정보등록 오류입니다.");
                  resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_NORMAL_COUPON_FAIL.getCode());
                  resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_NORMAL_COUPON_FAIL.getMessage());
                  throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_NORMAL_COUPON_FAIL);
                }
              }
            }
          }
        }
        else if (StringUtil.isEquals(eventManageRequestDto.getEventNormalInfo().getEventBenefitTp(), EventBenefitType.POINT.getCode())) {
          // ------------------------------------------------------------------
          // 당첨자혜택유형 == 적립금
          // ------------------------------------------------------------------
          // 별도 처리 없음

        }
        else if (StringUtil.isEquals(eventManageRequestDto.getEventNormalInfo().getEventBenefitTp(), EventBenefitType.GIFT.getCode())) {
          // ------------------------------------------------------------------
          // 당첨자혜택유형 == 경품
          // ------------------------------------------------------------------
          // 별도 처리 없음

        }
        else if (StringUtil.isEquals(eventManageRequestDto.getEventNormalInfo().getEventBenefitTp(), EventBenefitType.AUTO.getCode())) {
          // ------------------------------------------------------------------
          // 당첨자혜택유형 == 응모
          // ------------------------------------------------------------------
          // 별도 처리 없음

        }

        // --------------------------------------------------------------------
        // 4. 그룹 등록
        // --------------------------------------------------------------------
        if (StringUtil.isEquals(eventManageRequestDto.getEventInfo().getDispYn(), "Y")) {

          EventManageResponseDto resultGroupDto = this.addEventGroup(eventManageRequestDto);

          if (StringUtil.isEquals(resultGroupDto.getResultCode(), EventMessage.EVENT_MANAGE_SUCCESS.getCode())) {
            // 성공
          }
          else {
            log.debug("# 이벤트 그룹 정보등록 처리 오류입니다.");
            resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC.getCode());
            resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC.getMessage());
            throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC);
          }

        }
      }
    }
    catch (BaseException be) {
      log.info("# addEventNormal BaseException e :: " + be.toString());
      throw be;
    }
    catch (Exception e) {
      log.info("# addEventNormal Exception e :: " + e.toString());
      throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_NORMAL_PROC_FAIL);
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 설문이벤트 등록
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  private EventManageResponseDto addEventSurvey (EventManageRequestDto eventManageRequestDto, String mode) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.addEventSurvey Start");
    log.debug("# ######################################");
    log.debug("# In.evEventId      :: " + eventManageRequestDto.getEvEventId());

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    int resultInt = 0;

    // ========================================================================
    // # 처리
    // ========================================================================

    try {

      // ----------------------------------------------------------------------
      // 0. 수정인 경우 해당 테이블 삭제
      // ----------------------------------------------------------------------
      if (StringUtil.isEquals(mode, MODE_PUT)) {

        // --------------------------------------------------------------------
        // 0.1. 설문항목아이템정보첨부파일 삭제
        // --------------------------------------------------------------------
        resultInt = eventManageMapper.delEventSurveyItemAttc(eventManageRequestDto.getEventInfo().getEvEventId());
        log.debug("# 설문항목아이템정보첨부파일 삭제 결과 :: " + resultInt);

        // --------------------------------------------------------------------
        // 0.2. 설문항목아이템정보 삭제
        // --------------------------------------------------------------------
        resultInt = eventManageMapper.delEventSurveyItem(eventManageRequestDto.getEventInfo().getEvEventId());
        log.debug("# 설문항목아이템정보 삭제 결과 :: " + resultInt);

        // --------------------------------------------------------------------
        // 0.3. 설문항목정보 삭제
        // --------------------------------------------------------------------
        resultInt = eventManageMapper.delEventSurveyQuestion(eventManageRequestDto.getEventInfo().getEvEventId());
        log.debug("# 설문항목정보 삭제 결과 :: " + resultInt);

        // --------------------------------------------------------------------
        // 0.4. 이벤트쿠폰 삭제
        // --------------------------------------------------------------------
        resultInt = eventManageMapper.delEventCoupon(eventManageRequestDto.getEventInfo().getEvEventId());
        log.debug("# 이벤트쿠폰 삭제 결과 :: " + resultInt);

        // --------------------------------------------------------------------
        // 0.5. 이벤트상세-설문이벤트 삭제
        // --------------------------------------------------------------------
        resultInt = eventManageMapper.delEventSurvey(eventManageRequestDto.getEventInfo().getEvEventId());
        log.debug("# 설문이벤트 삭제 결과 :: " + resultInt);
      }

      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // @ 2-1. 설문이벤트 상세정보정보 등록
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 항목값 추가 Set
      eventManageRequestDto.getEventSurveyInfo().setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
      resultInt = eventManageMapper.addEventSurvey(eventManageRequestDto.getEventSurveyInfo());

      if (resultInt <= 0) {
        log.debug("# 설문이벤트 상세정보등록 오류입니다.");
        resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_FAIL.getCode());
        resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_FAIL.getMessage());
        throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_FAIL);
      }

      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // @ 2-2. 설문이벤트 쿠폰정보 등록 - N건
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 항목값 추가 Set
      if (eventManageRequestDto.getEventCouponList() != null && eventManageRequestDto.getEventCouponList().size() > 0) {
        for (EventVo eventCoupon : eventManageRequestDto.getEventCouponList()) {
          //eventManageRequestDto.getEventInfo().setDelYn("N");                             // 삭제여부
          eventCoupon.setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
          resultInt = eventManageMapper.addEventCoupon(eventCoupon);
        }
      }
      if (resultInt <= 0) {
        log.debug("# 설문이벤트 쿠폰정보등록 오류입니다.");
        resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_COUPON_FAIL.getCode());
        resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_COUPON_FAIL.getMessage());
        throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_COUPON_FAIL);
      }

      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // @ 2-3-1. 설문이벤트 설문항목정보 - N건
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      if (eventManageRequestDto.getEventSurveyQuestionList() != null && eventManageRequestDto.getEventSurveyQuestionList().size() > 0) {
        for (EventSurveyVo eventSurveyVo : eventManageRequestDto.getEventSurveyQuestionList()) {
          eventSurveyVo.setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
          resultInt = eventManageMapper.addEventSurveyQuestion(eventSurveyVo);

          if (resultInt <= 0) {
            log.debug("# 설문이벤트 설문항목등록 오류입니다.");
            resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_QUESTION_FAIL.getCode());
            resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_QUESTION_FAIL.getMessage());
            throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_QUESTION_FAIL);
          }

          // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
          // @ 2-3-2. 설문이벤트 설문항목아이템정보 - N건
          // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
          if (eventSurveyVo.getEventSurveyItemList() != null && eventSurveyVo.getEventSurveyItemList().size() > 0) {
            for (EventSurveyItemVo eventSurveyItemVo : eventSurveyVo.getEventSurveyItemList()) {
              eventSurveyItemVo.setEvEventSurveyQuestionId(eventSurveyVo.getEvEventSurveyQuestionId());
              resultInt = eventManageMapper.addEventSurveyItem(eventSurveyItemVo);

              if (resultInt <= 0) {
                log.debug("# 설문이벤트 설문항목아이템등록 오류입니다.");
                resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_ITEM_FAIL.getCode());
                resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_ITEM_FAIL.getMessage());
                throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_ITEM_FAIL);
              }

              // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
              // @ 2-3-3. 설문이벤트 설문항목아이템파일정보 - N건
              // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
              if (eventSurveyItemVo.getEventSurveyItemAttcList() != null && eventSurveyItemVo.getEventSurveyItemAttcList().size() > 0) {
                for (EventSurveyItemAttcVo eventSurveyItemAttcVo : eventSurveyItemVo.getEventSurveyItemAttcList()) {

                  eventSurveyItemAttcVo.setEvEventSurveyItemId(eventSurveyItemVo.getEvEventSurveyItemId());
                  resultInt = eventManageMapper.addEventSurveyItemAttc(eventSurveyItemAttcVo);

                  if (resultInt <= 0) {
                    log.debug("# 설문이벤트 설문항목아이템첨부파일등록 오류입니다.");
                    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_ITEM_ATTC_FAIL.getCode());
                    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_ITEM_ATTC_FAIL.getMessage());
                    throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_ITEM_ATTC_FAIL);
                  }
                }
              } // End of if (eventSurveyItemVo.getEventSurveyItemAttcList() != null && eventSurveyItemVo.getEventSurveyItemAttcList().size() > 0)
            } // End of for (EventSurveyItemVo eventSurveyItemVo : eventSurveyVo.getEventSurveyItemList())
          } // End of if (eventSurveyVo.getEventSurveyItemList() != null && eventSurveyVo.getEventSurveyItemList().size() > 0)
        } // End of for (EventSurveyVo eventSurveyVo : eventManageRequestDto.getEventSurveyQuestionList())
      } // End of if (eventManageRequestDto.getEventSurveyQuestionList() != null && eventManageRequestDto.getEventSurveyQuestionList().size() > 0)

      // --------------------------------------------------------------------
      // 4. 그룹 등록
      // --------------------------------------------------------------------
      if (StringUtil.isEquals(eventManageRequestDto.getEventInfo().getDispYn(), "Y")) {

        EventManageResponseDto resultGroupDto = this.addEventGroup(eventManageRequestDto);

        if (StringUtil.isEquals(resultGroupDto.getResultCode(), EventMessage.EVENT_MANAGE_SUCCESS.getCode())) {
          // 성공
        }
        else {
          log.debug("# 이벤트 그룹 정보등록 처리 오류입니다.");
          resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC.getCode());
          resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC.getMessage());
          throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC);
        }

      }
    }
    catch (BaseException be) {
      log.info("# addEventSurvey BaseException e :: " + be.toString());
      throw be;
    }
    catch (Exception e) {
      log.info("# addEventSurvey Exception e :: " + e.toString());
      throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_SURVEY_PROC_FAIL);
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 스탬프이벤트 등록
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  private EventManageResponseDto addEventStamp (EventManageRequestDto eventManageRequestDto, String mode) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.addEventStamp Start");
    log.debug("# ######################################");
    log.debug("# In.evEventId      :: " + eventManageRequestDto.getEvEventId());

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    int resultInt = 0;

    // ========================================================================
    // # 처리
    // ========================================================================

    try {

      // ----------------------------------------------------------------------
      // 0. 수정인 경우 해당 테이블 삭제
      // ----------------------------------------------------------------------
      if (StringUtil.isEquals(mode, MODE_PUT)) {

        // --------------------------------------------------------------------
        // 0.1. 이벤트쿠폰 삭제
        // --------------------------------------------------------------------
//        resultInt = eventManageMapper.delEventCoupon(eventManageRequestDto.getEventInfo().getEvEventId());
//        log.debug("# 이벤트쿠폰 삭제 결과 :: " + resultInt);

        // --------------------------------------------------------------------
        // 0.2. 스탬프이벤트상세정보 삭제
        // --------------------------------------------------------------------
//        resultInt = eventManageMapper.delEventStampDetl(eventManageRequestDto.getEventInfo().getEvEventId());
//        log.debug("# 스탬프이벤트상세정보 삭제 결과 :: " + resultInt);

        // --------------------------------------------------------------------
        // 0.3. 이벤트상세-스템프이벤트 삭제
        // --------------------------------------------------------------------
        resultInt = eventManageMapper.delEventStamp(eventManageRequestDto.getEventInfo().getEvEventId());
        log.debug("# 스템프이벤트 삭제 결과 :: " + resultInt);
      }

      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // @ 3-1. 스탬프이벤트 상세정보정보 등록
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 항목값 추가 Set
      eventManageRequestDto.getEventStampInfo().setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());  // 이벤트PK
      resultInt = eventManageMapper.addEventStamp(eventManageRequestDto.getEventStampInfo());

      if (resultInt <= 0) {
        log.debug("# 스탬프이벤트 상세정보등록 오류입니다.");
        resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_STAMP_FAIL.getCode());
        resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_STAMP_FAIL.getMessage());
        throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_STAMP_FAIL);
      }

      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // @ 3-2. 스탬프이벤트 스탬프상세 등록 - N건
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 항목값 추가 Set
      if (eventManageRequestDto.getEventStampDetlList() != null && eventManageRequestDto.getEventStampDetlList().size() > 0) {
        for (EventStampVo eventStampVo : eventManageRequestDto.getEventStampDetlList()) {
          eventStampVo.setEvEventStampId(eventManageRequestDto.getEventStampInfo().getEvEventStampId());  // 이벤트스탬프PK
          resultInt = eventManageMapper.addEventStampDetl(eventStampVo);

          if (resultInt <= 0) {
            log.debug("# 스탬프이벤트 스탬프정보등록 오류입니다.");
            resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_STAMP_STARMP_FAIL.getCode());
            resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_STAMP_STARMP_FAIL.getMessage());
            throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_STAMP_STARMP_FAIL);
          }

          // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
          // @ 3-3. 스탬프이벤트 이벤트쿠폰 등록 - N건
          // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
          // 항목값 추가 Set
          if (eventStampVo.getEventCouponList() != null && eventStampVo.getEventCouponList().size() > 0) {
            for (EventVo eventCoupon : eventStampVo.getEventCouponList()) {
              eventCoupon.setEvEventDetlId(eventStampVo.getEvEventStampDetlId());
              eventCoupon.setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
              resultInt = eventManageMapper.addEventCoupon(eventCoupon);

              if (resultInt <= 0) {
                log.debug("# 일반이벤트 쿠폰정보등록 오류입니다.");
                resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_STAMP_COUPON_FAIL.getCode());
                resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_STAMP_COUPON_FAIL.getMessage());
                throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_STAMP_COUPON_FAIL);
              }
            }
          }
        }
      }

      // --------------------------------------------------------------------
      // 4. 그룹 등록
      // --------------------------------------------------------------------
      if (StringUtil.isEquals(eventManageRequestDto.getEventInfo().getDispYn(), "Y")) {

        EventManageResponseDto resultGroupDto = this.addEventGroup(eventManageRequestDto);

        if (StringUtil.isEquals(resultGroupDto.getResultCode(), EventMessage.EVENT_MANAGE_SUCCESS.getCode())) {
          // 성공
        }
        else {
          log.debug("# 이벤트 그룹 정보등록 처리 오류입니다.");
          resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC.getCode());
          resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC.getMessage());
          throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC);
        }

      }

    }
    catch (BaseException be) {
      log.info("# addEventStamp BaseException e :: " + be.toString());
      throw be;
    }
    catch (Exception e) {
      log.info("# addEventStamp Exception e :: " + e.toString());
      throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_STAMP_PROC_FAIL);
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 룰렛이벤트 등록
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  private EventManageResponseDto addEventRoulette (EventManageRequestDto eventManageRequestDto, String mode) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.addEventRoulette Start");
    log.debug("# ######################################");
    log.debug("# In.evEventId      :: " + eventManageRequestDto.getEvEventId());
    log.debug("# In.evEventId      :: " + eventManageRequestDto.getEventInfo().getEvEventId());

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    int resultInt = 0;

    // ========================================================================
    // # 처리
    // ========================================================================

    try {

      // ----------------------------------------------------------------------
      // 0. 수정인 경우 해당 테이블 삭제
      // ----------------------------------------------------------------------
      if (StringUtil.isEquals(mode, MODE_PUT)) {

        // --------------------------------------------------------------------
        // 0.1. 룰렛이벤트아이템정보 삭제
        // --------------------------------------------------------------------
        eventManageMapper.delEventBenefitCount(eventManageRequestDto.getEventInfo().getEvEventId());
        resultInt = eventManageMapper.delEventRouletteItem(eventManageRequestDto.getEventInfo().getEvEventId());
        log.debug("# 룰렛이벤트아이템 삭제 결과 :: " + resultInt);

        // --------------------------------------------------------------------
        // 0.3. 이벤트상세-룰렛이벤트 삭제
        // --------------------------------------------------------------------
        resultInt = eventManageMapper.delEventRoulette(eventManageRequestDto.getEventInfo().getEvEventId());
        log.debug("# 룰렛이벤트 삭제 결과 :: " + resultInt);
      }

      if (StringUtil.isEquals(mode, MODE_ADD)) {
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // @ 6-1. 룰렛이벤트 상세정보정보 등록
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 항목값 추가 Set
        eventManageRequestDto.getEventRouletteInfo().setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());  // 이벤트PK
        resultInt = eventManageMapper.addEventRoulette(eventManageRequestDto.getEventRouletteInfo());

        if (resultInt <= 0) {
          log.debug("# 룰렛이벤트 상세정보등록 오류입니다.");
          resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_ROULETTE_FAIL.getCode());
          resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_ROULETTE_FAIL.getMessage());
          throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_ROULETTE_FAIL);
        }

        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // @ 6-2. 룰렛이벤트 아이템정보 등록 - N건
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 항목값 추가 Set
        if (eventManageRequestDto.getEventRouletteItemList() != null && eventManageRequestDto.getEventRouletteItemList().size() > 0) {
          for (EventRouletteVo eventRouletteVo : eventManageRequestDto.getEventRouletteItemList()) {
            eventRouletteVo.setEvEventRouletteId(eventManageRequestDto.getEventRouletteInfo().getEvEventRouletteId());
            resultInt = eventManageMapper.addEventRouletteItem(eventRouletteVo);

            // 이벤트 혜택 당첨정보 저장
            eventManageMapper.addEventBenefitCount(EventBenefitCountRequestDto.builder()
                    .evEventId(eventManageRequestDto.getEventInfo().getEvEventId())
                    .evEventRouletteItemId(eventRouletteVo.getEvEventRouletteItemId())
                    .awardMaxCount(eventRouletteVo.getAwardMaxCnt())
                    .createId(eventManageRequestDto.getEventInfo().getCreateId())
                    .build());

            if (resultInt <= 0) {
              log.debug("# 룰렛이벤트 룰렛아이템정보등록 오류입니다.");
              resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_ROULETTE_ITEM_FAIL.getCode());
              resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_ROULETTE_ITEM_FAIL.getMessage());
              throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_ROULETTE_ITEM_FAIL);
            }

            // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            // @ 6-3. 룰렛이벤트 이벤트쿠폰 등록 - N건
            // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            // 항목값 추가 Set
            if (eventRouletteVo.getEventCouponList() != null && eventRouletteVo.getEventCouponList().size() > 0) {
              for (EventVo eventCoupon : eventRouletteVo.getEventCouponList()) {
                eventCoupon.setEvEventDetlId(eventRouletteVo.getEvEventRouletteItemId());
                eventCoupon.setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
                resultInt = eventManageMapper.addEventCoupon(eventCoupon);

                if (resultInt <= 0) {
                  log.debug("# 일반이벤트 쿠폰정보등록 오류입니다.");
                  resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_ROULETTE_COUPON_FAIL.getCode());
                  resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_ROULETTE_COUPON_FAIL.getMessage());
                  throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_ROULETTE_COUPON_FAIL);
                }
              }
            }

          }
        }
      }else{
        // 룰렛 이벤트 수정 Case 추가
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // @ 6-1. 룰렛이벤트 상세정보정보 등록
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 항목값 추가 Set
        eventManageRequestDto.getEventRouletteInfo().setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());  // 이벤트PK
        resultInt = eventManageMapper.addEventRoulette(eventManageRequestDto.getEventRouletteInfo());

        // 등록된 룰렛 아이템 리스트 조회
        List<EventRouletteVo> rouletteItemList = eventManageMapper.getRouletteItemList(eventManageRequestDto.getEventRouletteInfo().getEvEventId());

        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // @ 6-2. 룰렛이벤트 아이템정보 등록 - N건
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 항목값 추가 Set
        if (eventManageRequestDto.getEventRouletteItemList() != null && eventManageRequestDto.getEventRouletteItemList().size() > 0 ) {

          List<EventRouletteVo> addVoList = eventManageRequestDto.getEventRouletteItemList().stream().filter(f -> rouletteItemList.stream().noneMatch(t -> f.getEvEventRouletteItemId().equals(t.getEvEventRouletteItemId()))).collect(Collectors.toList());
          List<EventRouletteVo> delVoList = rouletteItemList.stream().filter(f -> eventManageRequestDto.getEventRouletteItemList().stream().noneMatch(t -> f.getEvEventRouletteItemId().equals(t.getEvEventRouletteItemId()))).collect(Collectors.toList());

          if(delVoList != null && rouletteItemList.size() == eventManageRequestDto.getEventRouletteItemList().size()){
            for(EventRouletteVo vo : delVoList){
              resultInt = eventManageMapper.delEventRouletteItemModify(vo.getEvEventRouletteItemId());
            }
          }

          if(addVoList != null){
            for (EventRouletteVo rouletteVo : addVoList) {
                rouletteVo.setEvEventRouletteId(eventManageRequestDto.getEventRouletteInfo().getEvEventRouletteId());
              resultInt = eventManageMapper.addEventRouletteItem(rouletteVo);
            }
          }


          for (EventRouletteVo eventRouletteVo : eventManageRequestDto.getEventRouletteItemList()) {
            eventRouletteVo.setEvEventRouletteId(eventManageRequestDto.getEventRouletteInfo().getEvEventRouletteId());

            // 룰렛 아이템 정보 수정
            resultInt = eventManageMapper.putEventRouletteItem(eventRouletteVo);


            // 이벤트 혜택 당첨정보 저장
            eventManageMapper.addEventBenefitCount(EventBenefitCountRequestDto.builder()
                    .evEventId(eventManageRequestDto.getEventInfo().getEvEventId())
                    .evEventRouletteItemId(eventRouletteVo.getEvEventRouletteItemId())
                    .awardMaxCount(eventRouletteVo.getAwardMaxCnt())
                    .createId(eventManageRequestDto.getEventInfo().getCreateId())
                    .build());

            if (resultInt <= 0) {
              log.debug("# 룰렛이벤트 룰렛아이템정보등록 오류입니다.");
              resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_ROULETTE_ITEM_FAIL.getCode());
              resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_ROULETTE_ITEM_FAIL.getMessage());
              throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_ROULETTE_ITEM_FAIL);
            }

            // 룰렛 이벤트 아이템에 등록된 쿠폰 리스트 조회
            List<EventVo> couponList = eventManageMapper.getRouletteEventCouponList(eventManageRequestDto.getEventInfo().getEvEventId(), eventRouletteVo.getEvEventRouletteItemId());

            // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            // @ 6-3. 룰렛이벤트 이벤트쿠폰 등록 - N건
            // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            // 항목값 추가 Set
            if (eventRouletteVo.getEventCouponList() != null && eventRouletteVo.getEventCouponList().size() > 0) {

              List<EventVo> addCouponVoList = eventRouletteVo.getEventCouponList().stream().filter(f -> couponList.stream().noneMatch(t -> f.getPmCouponId().equals(t.getPmCouponId()))).collect(Collectors.toList());
              List<EventVo> delCouponVoList = couponList.stream().filter(f -> eventRouletteVo.getEventCouponList().stream().noneMatch(t -> f.getPmCouponId().equals(t.getPmCouponId()))).collect(Collectors.toList());
              if(delCouponVoList != null){
                for(EventVo vo : delCouponVoList){
                  resultInt = eventManageMapper.delRouletteEventPmCouponId(eventManageRequestDto.getEventInfo().getEvEventId(), vo.getPmCouponId(), vo.getEvEventDetlId());
                }
              }

              if(addCouponVoList != null){
                for (EventVo eventCoupon : addCouponVoList) {
                  eventCoupon.setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
                  eventCoupon.setEvEventDetlId(eventRouletteVo.getEvEventRouletteItemId());
                  resultInt = eventManageMapper.addEventCoupon(eventCoupon);

                  if (resultInt <= 0) {
                    log.debug("# 일반이벤트 쿠폰정보등록 오류입니다.");
                    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_NORMAL_COUPON_FAIL.getCode());
                    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_NORMAL_COUPON_FAIL.getMessage());
                    throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_NORMAL_COUPON_FAIL);
                  }
                }
              }

              // 쿠폰데이터 수정
              for (EventVo vo : eventRouletteVo.getEventCouponList()) {
                resultInt = eventManageMapper.putRouletteItemEventCoupon(vo);
              }

            }

          }
        }

      }

      String timestamp = DateUtil.getCurrentDate("yyyyMMddHHmm");

      // --------------------------------------------------------------------
      // 4. 그룹 등록  (목록노출 && 진행중)
      // --------------------------------------------------------------------
      if (StringUtil.isEquals(eventManageRequestDto.getEventInfo().getDispYn(), "Y") &&
              (Long.parseLong((eventManageRequestDto.getEventInfo().getStartDt())) <= Long.parseLong(timestamp)) &&
              (Long.parseLong((eventManageRequestDto.getEventInfo().getEndDt())) >= Long.parseLong(timestamp))) {

        EventManageResponseDto resultGroupDto = this.addEventGroup(eventManageRequestDto);

        if (StringUtil.isEquals(resultGroupDto.getResultCode(), EventMessage.EVENT_MANAGE_SUCCESS.getCode())) {
          // 성공
        }
        else {
          log.debug("# 이벤트 그룹 정보등록 처리 오류입니다.");
          resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC.getCode());
          resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC.getMessage());
          throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC);
        }

      }


    }
    catch (BaseException be) {
      log.info("# addEventRoulette BaseException e :: " + be.toString());
      throw be;
    }
    catch (Exception e) {
      log.info("# addEventRoulette Exception e :: " + e.toString());
      throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_ROULETTE_PROC_FAIL);
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  /**
   * 체험단이벤트 등록
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  @SuppressWarnings("unused")
  private EventManageResponseDto addEventExperience (EventManageRequestDto eventManageRequestDto, String mode) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.addEventExperience Start");
    log.debug("# ######################################");
    log.debug("# In.evEventId      :: " + eventManageRequestDto.getEvEventId());

    // ========================================================================
    // # 초기화
    // ========================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    int resultInt = 0;

    // ========================================================================
    // # 처리
    // ========================================================================

    try {

      // ----------------------------------------------------------------------
      // 0. 수정인 경우 해당 테이블 삭제
      // ----------------------------------------------------------------------
      if (StringUtil.isEquals(mode, MODE_PUT)) {

        // --------------------------------------------------------------------
        // 0.1. 이벤트댓글코드 삭제
        // --------------------------------------------------------------------
        resultInt = eventManageMapper.delEventCommentCode(eventManageRequestDto.getEventInfo().getEvEventId());
        log.debug("# 이벤트댓글코드 삭제 결과 :: " + resultInt);

        // --------------------------------------------------------------------
        // 0.3. 이벤트상세-일반이벤트 삭제
        // --------------------------------------------------------------------
        eventManageMapper.delEventBenefitCount(eventManageRequestDto.getEventInfo().getEvEventId());
        resultInt = eventManageMapper.delEventExperience(eventManageRequestDto.getEventInfo().getEvEventId());
        log.debug("# 체험단이벤트 삭제 결과 :: " + resultInt);
      }

      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // @ 7-1. 체험단이벤트 상세정보정보 등록
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 항목값 추가 Set
      eventManageRequestDto.getEventExperienceInfo().setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
      resultInt = eventManageMapper.addEventExperience(eventManageRequestDto.getEventExperienceInfo());

      // 이벤트 혜택 당첨정보 저장 - 선착순 케이스
      if(EventEnums.EventDrawType.FIRST_COME.getCode().equals(eventManageRequestDto.getEventInfo().getEventDrawTp())){
        eventManageMapper.addEventBenefitCount(EventBenefitCountRequestDto.builder()
                .evEventId(eventManageRequestDto.getEventInfo().getEvEventId())
                .awardMaxCount(eventManageRequestDto.getEventExperienceInfo().getFirstComeCnt())
                .createId(eventManageRequestDto.getEventInfo().getCreateId())
                .build());
      }

      if (resultInt <= 0) {
        log.debug("# 체험단이벤트 상세정보등록 오류입니다.");
        resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_EXPERIENCE_PROC_FAIL.getCode());
        resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_EXPERIENCE_PROC_FAIL.getMessage());
        throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_EXPERIENCE_PROC_FAIL);
      }

      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // @ 7-2. 체험단이벤트 이벤트댓글구분코드 등록 - N건
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 항목값 추가 Set
      if (eventManageRequestDto.getEventCommentCodeList() != null && eventManageRequestDto.getEventCommentCodeList().size() > 0) {
        for (EventVo eventCommentCode : eventManageRequestDto.getEventCommentCodeList()) {
          //eventCommentCode.setDelYn("N");                             // 삭제여부
          eventCommentCode.setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
          resultInt = eventManageMapper.addEventCommentCode(eventCommentCode);
        }
      }

      if (resultInt <= 0) {
        log.debug("# 체험단이벤트 댓글구분등록 오류입니다.");
        resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_ADD_EXPERIENCE_COMMENT_FAIL.getCode());
        resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_ADD_EXPERIENCE_COMMENT_FAIL.getMessage());
        throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_EXPERIENCE_COMMENT_FAIL);
      }


    }
    catch (BaseException be) {
      log.info("# addEventExperience BaseException e :: " + be.toString());
      throw be;
    }
    catch (Exception e) {
      log.info("# addEventExperience Exception e :: " + e.toString());
      throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_ADD_EXPERIENCE_FAIL);
    }

    // ========================================================================
    // # 반환
    // ========================================================================
    return resultResDto;
  }

  // ########################################################################
  // private
  // ########################################################################
  /**
   * 일반이벤트 등록
   * @param eventManageRequestDto
   * @return
   * @throws BaseException
   */
  private EventManageResponseDto addEventGroup (EventManageRequestDto eventManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# EventManageService.addEventGroup Start");
    log.debug("# ######################################");
    log.debug("# In.evEventId      :: " + eventManageRequestDto.getEvEventId());

    // ======================================================================
    // # 초기화
    // ======================================================================
    EventManageResponseDto resultResDto = new EventManageResponseDto();
    resultResDto.setResultCode(EventMessage.EVENT_MANAGE_SUCCESS.getCode());
    resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_SUCCESS.getMessage());
    int resultInt = 0;
    int resultDetlInt = 0;

    // ======================================================================
    // # 처리
    // ======================================================================

    try {

      if (StringUtil.isNotEmpty(eventManageRequestDto.getGroupList()) && eventManageRequestDto.getGroupList().size() > 0) {

        for (EventGroupVo eventGroupVo : eventManageRequestDto.getGroupList()) {

          // ----------------------------------------------------------------
          // 0. Param Set
          // ----------------------------------------------------------------
          // 이벤트ID
          eventGroupVo.setEvEventId(eventManageRequestDto.getEventInfo().getEvEventId());
          // 사용여부
          eventGroupVo.setUseYn(eventGroupVo.getGroupUseYn());

          // 배경컬러 사용안할 경우 배경색상 널 처리
          if (StringUtil.isEquals(eventGroupVo.getEventImgTp(), EventImgTp.NOT_USE.getCode())) {
            eventGroupVo.setBgCd("");
          }

          // ----------------------------------------------------------------
          // 1. 그룹정보 등록
          // ----------------------------------------------------------------
          resultInt = eventManageMapper.addEventGroup(eventGroupVo);

          if (resultInt <= 0) {
            log.debug("# 이벤트 그룹 정보등록 처리 오류입니다.");
            resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC.getCode());
            resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC.getMessage());
            throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL_PROC);
          }

          log.debug("# New evEventGroupId :: " + eventGroupVo.getEvEventGroupId());

          // ----------------------------------------------------------------
          // 2. 그룹상세정보 등록 (그룹상품정보)
          // ----------------------------------------------------------------
          if (StringUtil.isNotEmpty(eventGroupVo.getGroupGoodsList()) && eventGroupVo.getGroupGoodsList().size() > 0) {

            for (EventGroupDetlVo eventGroupDetlVo : eventGroupVo.getGroupGoodsList()) {

              // ------------------------------------------------------------
              // 2.1. 이벤트그룹ID Set
              // ------------------------------------------------------------
              eventGroupDetlVo.setEvEventGroupId(eventGroupVo.getEvEventGroupId());

              // ------------------------------------------------------------
              // 2.2. 이벤트 그룹상세 등록
              // ------------------------------------------------------------
              resultDetlInt = eventManageMapper.addEventGroupDetl(eventGroupDetlVo);

              if (resultDetlInt <= 0) {
                log.debug("# 이벤트 그룹상세 정보등록 처리 오류입니다.");
                resultResDto.setResultCode(EventMessage.EVENT_MANAGE_EVENT_GROUP_DETL_ADD_FAIL_PROC.getCode());
                resultResDto.setResultMessage(EventMessage.EVENT_MANAGE_EVENT_GROUP_DETL_ADD_FAIL_PROC.getMessage());
                throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_GROUP_DETL_ADD_FAIL_PROC);
              }
              log.debug("# New EV_EVENT_GROUP_DETL_ID :: [" + eventGroupVo.getEvEventGroupId() + "][" + eventGroupDetlVo.getEvEventGroupDetlId() + "]");

            } // End of for (EventGroupDetlVo eventGroupDetlVo : eventGroupVo.getGroupGoodsList())

          } // End of if (StringUtil.isNotEmpty(eventGroupVo.getGroupGoodsList()) && eventGroupVo.getGroupGoodsList().size() > 0)

        } // End of for (EventGroupVo eventGroupVo : eventManageRequestDto.getGroupList())

      } // End of if (StringUtil.isNotEmpty(eventManageRequestDto.getGroupList()) && eventManageRequestDto.getGroupList().size() > 0)

    }
    catch (BaseException be) {
      log.info("# addEventNormal BaseException e :: " + be.toString());
      throw be;
    }
    catch (Exception e) {
      log.info("# addEventNormal Exception e :: " + e.toString());
      throw new BaseException(EventMessage.EVENT_MANAGE_EVENT_GROUP_ADD_FAIL);
    }

    //  ======================================================================
    // # 반환
    // ======================================================================
    return resultResDto;
  }


}
