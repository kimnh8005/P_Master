package kr.co.pulmuone.v1.promotion.manage.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.event.dto.vo.CoverageVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EvUserGroupVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EventExperienceVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EventNormalVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EventRouletteVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EventStampVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EventSurveyVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.EventVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "EventManageResponseDto")
public class EventManageResponseDto {

  @ApiModelProperty(value = "결과코드")
  private String resultCode;

  @ApiModelProperty(value = "결과메시지")
  private String resultMessage;

  @ApiModelProperty(value = "리스트전체건수")
  private long total;

  @ApiModelProperty(value = "리스트")
  private List<?> rows;

  @ApiModelProperty(value = "상세(기본)")
  private EventVo detail;

  // 등록결과정보
  @ApiModelProperty(value = "이벤트PK")
  private String evEventId;

  @ApiModelProperty(value = "이벤트유형")
  private String eventTp;


  // --------------------------------------------------------------------------
  // [공통] - EV_EVENT
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "이벤트기본정보", required = false)
  private EventVo eventInfo;

  // --------------------------------------------------------------------------
  // [일반] - EV_EVENT_NORMA
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "일반이벤트정보", required = false)
  private EventNormalVo eventNormalInfo;

  // --------------------------------------------------------------------------
  // [설문] - EV_EVENT_SURVEY
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "설문조사이벤트정보", required = false)
  private EventSurveyVo eventSurveyInfo;

  // --------------------------------------------------------------------------
  // [설문] - EV_EVENT_SURVEY_QUESTION > EV_EVENT_SURVEY_ITEM >  EV_EVENT_SURVEY_ITEM_ATTC
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "설문항목리스트", required = false)
  private List<EventSurveyVo> eventSurveyQuestionList;

  // --------------------------------------------------------------------------
  // [스탬프] - EV_EVENT_STAMP
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "스탬프이벤트정보", required = false)
  private EventStampVo eventStampInfo;

  // --------------------------------------------------------------------------
  // [스탬프] - EV_EVENT_STAMP_DETL
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "스탬프이벤트스탬프리스트", required = false)
  private List<EventStampVo> eventStampDetlList;

  // --------------------------------------------------------------------------
  // [룰렛] - EV_EVENT_ROULETTE
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "룰렛이벤트정보", required = false)
  private EventRouletteVo eventRouletteInfo;

  // --------------------------------------------------------------------------
  // [룰렛] - EV_EVENT_ROULETTE_ITEM
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "룰렛이벤트아이템리스트", required = false)
  private List<EventRouletteVo> eventRouletteItemList;

  // --------------------------------------------------------------------------
  // [체험단] - EV_EVENT_EXPERIENCE
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "체험단이벤트정보", required = false)
  private EventExperienceVo eventExperienceInfo;

  // --------------------------------------------------------------------------
  // [공통] - List<EV_EVENT_USER_GROUP>
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "이벤트접근권한리스트", required = false)
  private List<EvUserGroupVo> userGroupList;

  // --------------------------------------------------------------------------
  // [일반][체험단] - List< EV_EVENT_COMMENT_CODE>
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "이벤트댓글구분리스트", required = false)
  private List<EventVo> eventCommentCodeList;

  // --------------------------------------------------------------------------
  // [일반][설문] - List<EV_EVENT_COUPON>
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "이벤트쿠폰리스트", required = false)
  private List<EventVo> eventCouponList;

  // --------------------------------------------------------------------------
  // [공통] - List<EV_EVENT_COVERAGE>
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "이벤트 커버리지 리스트", required = false)
  private List<CoverageVo> coverageList;
}
