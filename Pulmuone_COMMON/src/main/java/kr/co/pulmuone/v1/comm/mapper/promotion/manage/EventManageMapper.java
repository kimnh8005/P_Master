package kr.co.pulmuone.v1.comm.mapper.promotion.manage;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.base.dto.vo.GoodsSearchVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.promotion.event.dto.vo.CoverageVo;
import kr.co.pulmuone.v1.promotion.manage.dto.EventBenefitCountRequestDto;
import kr.co.pulmuone.v1.promotion.manage.dto.EventManageRequestDto;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventManageMapper {

  // ==========================================================================
  // @ 조회 - 이벤트
  // ==========================================================================
  // --------------------------------------------------------------------------
  // 이벤트 리스트조회
  // --------------------------------------------------------------------------
  Page<EventVo> selectEventList (EventManageRequestDto eventManageRequestDto) throws BaseException;

  // 일반이벤트-그룹정보 리스트조회
  List<EventGroupVo> selectfEventGroupList (@Param("evEventId") String evEventId) throws BaseException;

  // 일반이벤트-그룹정보-그룹상품 리스트조회
  List<EventGroupDetlVo> selectfEventGroupGoodsList (@Param("evEventGroupId") String evEventGroupId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트 접근권한정보 리스트조회 (리스트용)
  // --------------------------------------------------------------------------
  List<EvUserGroupVo> selectEventUserGroupListForList (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트 상세조회
  // --------------------------------------------------------------------------
  EventVo selectEventInfo (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트 접근권한리스트
  // --------------------------------------------------------------------------
  List<EvUserGroupVo> selectEventUserGroupList (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트 적용범위 리스트
  // --------------------------------------------------------------------------
  List<CoverageVo> getCoverageList (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트상세 - 일반이벤트
  // --------------------------------------------------------------------------
  EventNormalVo selectEventNormal (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트댓글구분리스트
  // --------------------------------------------------------------------------
  List<EventVo> selectEventCommentCodeList (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트쿠폰리스트
  // --------------------------------------------------------------------------
  List<EventVo> selectEventCouponList (@Param("evEventId") String evEventId, @Param("evEventStampDetlId") String evEventStampDetlId) throws BaseException;

  List<EventVo>  getEventCouponList (@Param("evEventId") String evEventId) throws BaseException;

  List<EventVo>  getRouletteEventCouponList (@Param("evEventId") String evEventId, @Param("evEventRouletteItemId") String evEventRouletteItemId) throws BaseException;


  // --------------------------------------------------------------------------
  // 이벤트상세 - 설문이벤트
  // --------------------------------------------------------------------------
  EventSurveyVo selectEventSurvey (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 설문항목리스트조회 - 설문이벤트
  // --------------------------------------------------------------------------
  List<EventSurveyVo> selectEventSurveyQuestionList (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 설문항목아이템리스트조회 - 설문이벤트
  // --------------------------------------------------------------------------
  List<EventSurveyItemVo> selectEventSurveyItemList (@Param("evEventSurveyQuestionId") String evEventSurveyQuestionId) throws BaseException;

  // --------------------------------------------------------------------------
  // 설문항목아이템첨부파일리스트조회 - 설문이벤트
  // --------------------------------------------------------------------------
  List<EventSurveyItemAttcVo> selectEventSurveyItemAttcList (@Param("evEventSurveyItemId") String evEventSurveyItemId) throws BaseException;


  List<EventRouletteVo>  getRouletteItemList (@Param("evEventId") String evEventId) throws BaseException;


  // --------------------------------------------------------------------------
  // 이벤트상세 - 스탬프(출석)/스탬프(미션)/스탬프(구매)
  // --------------------------------------------------------------------------
  EventStampVo selectEventStamp (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트상세 리스트조회 - 스탬프(출석)/스탬프(미션)/스탬프(구매)
  // --------------------------------------------------------------------------
  List<EventStampVo> selectEventStampDetlList (@Param("evEventStampId") String evEventStampId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트상세 - 룰렛이벤트
  // --------------------------------------------------------------------------
  EventRouletteVo selectEventRoulette (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 룰렛이벤트아이템리스트 - 룰렛이벤트
  // --------------------------------------------------------------------------
  List<EventRouletteVo> selectEventRouletteItemList (@Param("evEventRouletteId") String evEventRouletteId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트상세 - 체험단이벤트
  // --------------------------------------------------------------------------
  EventExperienceVo selectEventExperience (@Param("evEventId") String evEventId) throws BaseException;



  // ==========================================================================
  // @ 조회 - 이벤트참여
  // ==========================================================================
  // --------------------------------------------------------------------------
  // 이벤트참여 리스트조회
  // --------------------------------------------------------------------------
  Page<EventJoinVo> selectEventJoinList (EventManageRequestDto eventManageRequestDto) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트참여 직접입력 리스트조회
  // --------------------------------------------------------------------------
  Page<EventJoinVo> selectEventJoinDirectJoinList (@Param("evEventSurveyQuestionId") String evEventSurveyQuestionId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트참여 설문항목 리스트조회
  // --------------------------------------------------------------------------
  List<EventSurveyVo> selectEventJoinSurveyQuestionList (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트참여 설문아이템 리스트조회
  // --------------------------------------------------------------------------
  List<EventSurveyItemVo> selectEventJoinSurveyItemList (@Param("evEventSurveyQuestionId") String evEventSurveyQuestionId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트참여 설문아이템참여 리스트조회
  // --------------------------------------------------------------------------
  List<EventSurveyItemVo> selectEventJoinSurveyItemJoinList (@Param("evEventJoinId") String evEventJoinId, @Param("evEventSurveyQuestionId") String evEventSurveyQuestionId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트참여 설문아이템첨부파일 리스트조회
  // --------------------------------------------------------------------------
  List<EventSurveyItemAttcVo> selectEventJoinSurveyItemAttcList (@Param("evEventSurveyItemId") String evEventSurveyItemId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트참여 직접선택 대상자 조회
  // --------------------------------------------------------------------------
  List<EventJoinVo> selectJoinWinnerDirectTarget (EventJoinVo eventJoinVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트참여 랜덤 당첨 대상자 조회
  // --------------------------------------------------------------------------
  List<EventJoinVo> selectJoinWinnerRandomTarget (EventJoinVo eventJoinVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트참여 당첨혜택 쿠폰리스트 조회
  // --------------------------------------------------------------------------
  List<EventJoinVo> selectEventJoinBenefitCouponList (@Param("evEventJoinId") String evEventJoinId, @Param("evEventId") String evEventId, @Param("urUserId") String urUserId, @Param("stampCnt") String stampCnt) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트참여 당첨혜택 쿠폰리스트 조회 - 체험단용
  // --------------------------------------------------------------------------
  List<EventJoinVo> selectEventJoinBenefitCouponByExperienceList (@Param("evEventJoinId") String evEventJoinId, @Param("evEventId") String evEventId, @Param("urUserId") String urUserId, @Param("stampCnt") String stampCnt) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트참여 당첨혜택 포인트리스트 조회
  // --------------------------------------------------------------------------
  List<EventJoinVo> selectEventJoinBenefitPointList (@Param("evEventJoinId") String evEventJoinId, @Param("evEventId") String evEventId, @Param("urUserId") String urUserId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트참여 당첨혜택 경품응모정보 조회
  // --------------------------------------------------------------------------
  EventJoinVo selectEventJoinBenefitGiftEnterInfo (@Param("evEventJoinId") String evEventJoinId, @Param("evEventId") String evEventId, @Param("urUserId") String urUserId, @Param("stampCnt") String stampCnt) throws BaseException;

  // 상품정보리스트(엑셀용)
  List<GoodsSearchVo> selectGoodsInfoList (EventManageRequestDto eventManageRequestDto) throws BaseException;


  // ==========================================================================
  // @ 등록
  // ==========================================================================

  // --------------------------------------------------------------------------
  // 이벤트등록
  // --------------------------------------------------------------------------
  int addEvent (EventVo eventVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 일반이벤트
  // --------------------------------------------------------------------------
  // 이벤트 등록 - 전시그룹
  int addEventGroup (EventGroupVo eventGroupVo) throws BaseException;

  // 이벤트 등록 - 전시그룹상세
  int addEventGroupDetl (EventGroupDetlVo eventGroupDetlVo) throws BaseException;

  // --------------------------------------------------------------------------
  // *이벤트상세 - 일반이벤트 등록
  // --------------------------------------------------------------------------
  int addEventNormal (EventNormalVo inVo) throws BaseException;

  // --------------------------------------------------------------------------
  // *이벤트상세 - 설문이벤트 등록
  // --------------------------------------------------------------------------
  int addEventSurvey (EventSurveyVo inVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트상세 - 설문항목정보 등록
  // --------------------------------------------------------------------------
  int addEventSurveyQuestion (EventSurveyVo inVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트상세 - 설문아이템정보 등록
  // --------------------------------------------------------------------------
  int addEventSurveyItem (EventSurveyItemVo inVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트상세 - 설문아이템첨부파일 등록
  // --------------------------------------------------------------------------
  int addEventSurveyItemAttc (EventSurveyItemAttcVo inVo) throws BaseException;

  // --------------------------------------------------------------------------
  // *이벤트상세 - 스탬프이벤트 등록
  // --------------------------------------------------------------------------
  int addEventStamp (EventStampVo inVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트상세 - 스탬프이벤트상세 등록
  // --------------------------------------------------------------------------
  int addEventStampDetl (EventStampVo inVo) throws BaseException;

  // --------------------------------------------------------------------------
  // *이벤트상세 - 룰렛이벤트 등록
  // --------------------------------------------------------------------------
  int addEventRoulette (EventRouletteVo inVo) throws BaseException;

  // --------------------------------------------------------------------------
  // *이벤트상세 - 룰렛이벤트아이템 등록
  // --------------------------------------------------------------------------
  int addEventRouletteItem (EventRouletteVo inVo) throws BaseException;

  int putEventRouletteItem (EventRouletteVo inVo) throws BaseException;

  int putRouletteItemEventCoupon (EventVo vo ) throws BaseException;

  // --------------------------------------------------------------------------
  // *이벤트상세 - 체험단이벤트 등록
  // --------------------------------------------------------------------------
  int addEventExperience (EventExperienceVo inVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트쿠폰 등록
  // --------------------------------------------------------------------------
  int addEventCoupon (EventVo inVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트댓글구분 등록
  // --------------------------------------------------------------------------
  int addEventCommentCode (EventVo inVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트접근권한 등록
  // --------------------------------------------------------------------------
  int addEventUserGroup (EvUserGroupVo inVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트 적용범위 등록
  // --------------------------------------------------------------------------
  int addEventCoverage(List<CoverageVo> coverageVoList) throws Exception;

  // --------------------------------------------------------------------------
  // 이벤트 적용범위 삭제
  // --------------------------------------------------------------------------
  int removeEventCoverage(@Param("evEventId") String evEventId) throws Exception;

  // ==========================================================================
  // @ 수정
  // ==========================================================================

  // --------------------------------------------------------------------------
  // 이벤트수정
  // --------------------------------------------------------------------------
  int putEvent (EventVo eventVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트 당첨자곤지사항 수정
  // --------------------------------------------------------------------------
  int putEventWinnerNotice (EventVo eventVo) throws BaseException;


  // ==========================================================================
  // @ 수정 - 이벤트참여
  // ==========================================================================

  // --------------------------------------------------------------------------
  // 이벤트참여 당첨 등록/해제 - 개별
  // --------------------------------------------------------------------------
  int putWinnerLotteryInfo (EventJoinVo eventJoinVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트참여 당첨 쿠폰 등록 - 개별
  // --------------------------------------------------------------------------
  int addEventJoinCoupon (EventJoinVo eventJoinVo) throws BaseException;


  // --------------------------------------------------------------------------
  // 이벤트참여 당첨 등록/해제 - 개별
  // --------------------------------------------------------------------------
  int putWinnerExistYn (@Param("evEventId") String evEventId, @Param("urUserId") String urUserId, @Param("winnerExistYn") String winnerExistYn) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트참여 당첨 등록/해제
  // --------------------------------------------------------------------------
  int putWinnerLottery (EventJoinVo eventJoinVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트참여 댓글 차단/차단해제
  // --------------------------------------------------------------------------
  int putAdminSecretYn (EventJoinVo eventJoinVo) throws BaseException;


  // ==========================================================================
  // @ 삭제
  // ==========================================================================
  // --------------------------------------------------------------------------
  // 이벤트삭제 (DEL_YN = 'Y' 업데이트 처리)
  // --------------------------------------------------------------------------
  int delEvent (EventVo eventVo) throws BaseException;

  // --------------------------------------------------------------------------
  // 일반이벤트
  // --------------------------------------------------------------------------
  // 이벤트 삭제 - 전시그룹 - 이벤트ID 기준
  int delEventGroupByEvEventId (@Param("evEventId") String evEventId) throws BaseException;

  // 이벤트 삭제 - 전시그룹상세 - 이벤트ID 기준
  int delEventGroupDetlByEvEventId (@Param("evEventId") String evEventId) throws BaseException;

  // 개별 삭제 - 전시그룹
  int delEventGroup (@Param("evEventGroupId") String evEventGroupId) throws BaseException;

  // 개별 삭제 - 전시그룹상세
  int delEventGroupDetl (@Param("evEventGroupDetlId") String evEventGroupDetlId) throws BaseException;


  // --------------------------------------------------------------------------
  // 이벤트상세 삭제 - 일반이벤트
  // --------------------------------------------------------------------------
  int delEventNormal (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트상세 삭제 - 설문이벤트
  // --------------------------------------------------------------------------
  int delEventSurvey (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 설문항목정보 삭제 - 설문이벤트
  // --------------------------------------------------------------------------
  int delEventSurveyQuestion (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 설문항목아이템정보 삭제 - 설문이벤트
  // --------------------------------------------------------------------------
  int delEventSurveyItem (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 설문항목아이템정보점푸파일 삭제 - 설문이벤트
  // --------------------------------------------------------------------------
  int delEventSurveyItemAttc (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트상세 삭제 - 스탬프(출석)/스탬프(미션)/스탬프(구매)
  // --------------------------------------------------------------------------
  int delEventStamp (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트스탬프상세 삭제 - 스탬프(출석)/스탬프(미션)/스탬프(구매)
  // --------------------------------------------------------------------------
  int delEventStampDetl (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트상세 삭제 - 룰렛이벤트
  // --------------------------------------------------------------------------
  int delEventRoulette (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 룰렛이벤트아이템 삭제 - 룰렛이벤트
  // --------------------------------------------------------------------------
  int delEventRouletteItem (@Param("evEventId") String evEventId) throws BaseException;

  int delEventRouletteItemModify (@Param("evEventRouletteItemId") String evEventRouletteItemId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트상세 삭제 - 체험단이벤트
  // --------------------------------------------------------------------------
  int delEventExperience (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트접근권한 삭제
  // --------------------------------------------------------------------------
  int delEventUserGroup (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이벤트댓글코드 삭제
  // --------------------------------------------------------------------------
  int delEventCommentCode (@Param("evEventId") String evEventId) throws BaseException;

  // --------------------------------------------------------------------------
  // 이베트쿠폰 삭제
  // --------------------------------------------------------------------------
  int delEventCoupon (@Param("evEventId") String evEventId) throws BaseException;

  int delEventPmCouponId (@Param("evEventId") String evEventId, @Param("pmCouponId") String pmCouponId) throws BaseException;

  int delRouletteEventPmCouponId (@Param("evEventId") String evEventId, @Param("pmCouponId") String pmCouponId, @Param("evEventDetlId") String evEventDetlId) throws BaseException;


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전체건수
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  int selectTotalCount () throws BaseException;

  int addEventBenefitCount(EventBenefitCountRequestDto dto) throws BaseException;

  int delEventBenefitCount(String evEventId) throws BaseException;

}
