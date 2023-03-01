package kr.co.pulmuone.v1.promotion.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.enums.EventEnums;
import kr.co.pulmuone.v1.promotion.event.dto.vo.CoverageVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

//@Slf4j
@Getter
@Setter
@ToString
@ApiModel(description = "EventManageRequestDto")
public class EventManageRequestDto extends BaseRequestPageDto {

  // ==========================================================================
  // PK
  // ==========================================================================
  // 이벤트
  @ApiModelProperty(value = "이벤트 PK", required = false)
  private String evEventId;

  @ApiModelProperty(value = "이벤트그룹 PK", required = false)
  private String evEventGroupId;

  // * 일반이벤트
  @ApiModelProperty(value = "일반이벤트 PK", required = false)
  private String evEventNormalId;

  // * 설문이벤트
  @ApiModelProperty(value = "설문이벤트 PK", required = false)
  private String evEventServeyId;

  @ApiModelProperty(value = "설문항목정보 PK", required = false)
  private String evEventSurveyQuestionId;

  @ApiModelProperty(value = "설문항목아이템 PK", required = false)
  private String evEventServeyItemId;

  @ApiModelProperty(value = "설문항목아이템정보첨부파일 PK", required = false)
  private String evEventServeyItemAttcId;

  // * 스탬프이벤트
  @ApiModelProperty(value = "스탬프이벤트 PK", required = false)
  private String evEventStampId;

  @ApiModelProperty(value = "스탬프이벤트상세 PK", required = false)
  private String evEventStampDetlId;

  // * 룰렛이벤트
  @ApiModelProperty(value = "룰렛이벤트 PK", required = false)
  private String evEventRouletteId;

  @ApiModelProperty(value = "룰렛이벤트아이템 PK", required = false)
  private String evEventRouletteItemId;

  // * 체험단이벤트
  @ApiModelProperty(value = "체험단 PK", required = false)
  private String evEventExperienceId;

  // * 이벤트참여
  @ApiModelProperty(value = "이벤트참여 PK", required = false)
  private String evEventJoinId;

  @ApiModelProperty(value = "이벤트참여설문 PK", required = false)
  private String evEventJoinServeyId;

  @ApiModelProperty(value = "이벤트참여쿠폰 PK", required = false)
  private String evEventJoinCouponId;

  // * 이벤트쿠폰
  @ApiModelProperty(value = "이벤트쿠폰 PK", required = false)
  private String evEventCouponId;

  // * 이벤트댓글구분
  @ApiModelProperty(value = "이벤트댓글구분 PK", required = false)
  private String evEventCommentCodeId;

  // * 이벤트접근권한정보
  @ApiModelProperty(value = "이벤트접근권한정보 PK", required = false)
  private String evEventUserGroupId;



  // ==========================================================================
  // Vo
  // ==========================================================================

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

  @ApiModelProperty(value = "모드", required = false)
  private String mode;

  @ApiModelProperty(value = "상품코드리스트String", required = false)
  private List<String> ilGoodsIdList;

  // --------------------------------------------------------------------------
  // 이벤트참여 관련
  // --------------------------------------------------------------------------
  // EV_EVENT_JOIN, EV_EVENT_JOIN_COUPON, EV_EVENT_JOIN_SERVEY
  @ApiModelProperty(value = "이벤트참여정보", required = false)
  private EventJoinVo eventJoinInfo;

  @ApiModelProperty(value = "이벤트참여정보JsonString", required = false)
  private String eventJoinInfoJsonString;

  // ==========================================================================
  // 항목
  // ==========================================================================
  @ApiModelProperty(value = "검색구분", required = false)
  private String searchSe;

  @ApiModelProperty(value = "키워드", required = false)
  private String keyWord;

  @ApiModelProperty(value = "제목", required = false)
  private String title;

  @ApiModelProperty(value = "이벤트유형", required = false)
  private String eventTp;

  @ApiModelProperty(value = "사용여부", required = false)
  private String useYn;

  @ApiModelProperty(value = "전시여부", required = false)
  private String dispYn;

  @ApiModelProperty(value = "몰구분", required = false)
  private String mallDiv;

  @ApiModelProperty(value = "진행상태", required = false)
  private String statusSe;

  @ApiModelProperty(value = "진행상태.진행예정", required = false)
  private String statusWait;

  @ApiModelProperty(value = "진행상태.진행중", required = false)
  private String statusIng;

  @ApiModelProperty(value = "진행상태.진행완료", required = false)
  private String statusEnd;

  @ApiModelProperty(value = "노출범위(디바이스)", required = false)
  private String goodsDisplayType;

  @ApiModelProperty(value = "노출범위(디바이스).Web.PC", required = false)
  private String dispWebPcYn;

  @ApiModelProperty(value = "노출범위(디바이스).Web.Mobile", required = false)
  private String dispWebMobileYn;

  @ApiModelProperty(value = "노출범위(디바이스).Web.App", required = false)
  private String dispAppYn;

  @ApiModelProperty(value = "입직원전용여부", required = false)
  private String evEmployeeTp;

  @ApiModelProperty(value = "접근권한설정유형", required = false)
  private String userGroupFilter;
  //private String evGroupTp;

  @ApiModelProperty(value = "이벤트유형", required = false)
  private List<String> eventTpList;

  @ApiModelProperty(value = "몰구분리스트", required = false)
  private List<String> mallDivList;

  @ApiModelProperty(value = "진행상태리스트", required = false)
  private List<String> stausTpList;

  @ApiModelProperty(value = "접근권한설정유형리스트", required = false)
  private List<String> userGroupIdList;
  //private List<String> evGroupTpList;

  @ApiModelProperty(value = "노출범위(디바이스)리스트", required = false)
  private List<String> goodsDisplayTypeList;

  @ApiModelProperty(value = "임직원전용유형리스트", required = false)
  private List<String> evEmployeeTpList;

  @ApiModelProperty(value = "진행상태구분리스트", required = false)
  private List<String> statusSeList;

  @ApiModelProperty(value = "시작시작일자", required = false)
  private String startBeginDt;

  @ApiModelProperty(value = "시작종료일자", required = false)
  private String startFinishDt;

  @ApiModelProperty(value = "종료시작일자", required = false)
  private String endBeginDt;

  @ApiModelProperty(value = "종료종료일자", required = false)
  private String endFinishDt;

  @ApiModelProperty(value = "로그인ID", required = false)
  private String loginId;

  @ApiModelProperty(value = "이벤트ID리스트JsonString", required = false)
  private String evEventIdListString;

  @ApiModelProperty(value = "이벤트리스트", required = false)
  private List<String> evEventIdList;

  @ApiModelProperty(value = "그룹상세리스트String", required = false)
  private String evEventGroupDetlIdListString;

  @ApiModelProperty(value = "그룹상세ID리스트", required = false)
  private List<String> evEventGroupDetlIdList;

  @ApiModelProperty(value = "엑셀출력여부", required = false)
  private String excelYn;

  @ApiModelProperty(value = "상품코드리스트String", required = false)
  private String ilGoodsIdListString;



  // ==========================================================================
  // 이벤트참여/당첨자처리
  // ==========================================================================
  @ApiModelProperty(value = "당첨여부", required = false)
  private String winnerYn;

  @ApiModelProperty(value = "당첨자선택유형", required = false)
  private String winnerSelectTp;

  @ApiModelProperty(value = "임직원제외여부", required = false)
  private String employeeExceptYn;

  @ApiModelProperty(value = "이벤트참여제한고객제외", required = false)
  private String eventJoinYn;

  @ApiModelProperty(value = "당첨자수", required = false)
  private int winnerCnt;

  @ApiModelProperty(value = "당첨자이벤트참여ID리스트JsonString", required = false)
  private String winnerEventJoinListJsonString;

  @ApiModelProperty(value = "당첨자이벤트참여ID리스트", required = false)
  private List<EventJoinVo> winnerEventJoinList;

  @ApiModelProperty(value = "관리자 비공개 여부")
  private String adminSecretYn;

//  @ApiModelProperty(value = "임직원제외여부", required = false)
//  private String employeeExceptYn;

//  @ApiModelProperty(value = "이벤트참여제한고객제외여부", required = false)
//  private String eventJoinYn;



  // ==========================================================================
  // 이벤트접근권한정보
  // ==========================================================================
  @ApiModelProperty(value = "그룹PK리스트", required = false)
  private List<String> urGroupIdList;

  @ApiModelProperty(value = "이벤트ID그룹PK리스트JsonString", required = false)
  private String urGroupIdListString;


  @ApiModelProperty(value = "랜덤 당첨 혜택 구분", required = false)
  private EventEnums.RandomBenefitType randomBenefitTp;

  @ApiModelProperty(value = "당첨방법", required = false)
  private String handwrittenLotteryTp;

  // --------------------------------------------------------------------------
  // 등록 관련
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "그룹리스트String", required = false)
  private String groupListJsonString;

  @ApiModelProperty(value = "그룹리스트", required = false)
  private List<EventGroupVo> groupList;

  // --------------------------------------------------------------------------
  // 적용범위
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "적용범위")
  private String coverageType;

  @ApiModelProperty(value = "적용범위 grid Data", required = false)
  String insertData;

  @ApiModelProperty(value = "적용범위 grid Data List", hidden = true)
  List<CoverageVo> insertRequestDtoList = new ArrayList<>();
}
