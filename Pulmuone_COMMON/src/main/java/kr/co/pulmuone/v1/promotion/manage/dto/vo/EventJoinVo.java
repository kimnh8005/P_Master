package kr.co.pulmuone.v1.promotion.manage.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingEmail;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingMobile;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class EventJoinVo {

  public EventJoinVo () {}

  public EventJoinVo (Object userInfo) {
    // 등록자/수정자 Set
    //if (userInfo!= null) {
    //  if (((UserVo)userInfo).getUserId() != null) {
    //    this.setModifyId((SessionUtil.getBosUserVO()).getUserId());
    //    this.setCreateId((SessionUtil.getBosUserVO()).getUserId());
    //  }
    //  else {
    //    this.setModifyId("0");
    //    this.setCreateId("0");
    //  }
    //}
    //else {
    //  this.setModifyId("0");
    //  this.setCreateId("0");
    //}
  }

  // --------------------------------------------------------------------------
  // EV_EVENT_JOIN
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "이벤트참여정보 PK")
  private String evEventJoinId;


  @ApiModelProperty(value = "이벤트 PK")
  private String evEventId;


  @ApiModelProperty(value = "참여자 PK")
  private String urUserId;


  @ApiModelProperty(value = "당첨여부")
  private String winnerYn;


  @ApiModelProperty(value = "당첨존재여부")
  private String winnerExistYn;


  @ApiModelProperty(value = "추첨일시")
  private String lotteryDt;


  @ApiModelProperty(value = "추첨자ID")
  private String lotteryId;


  @ApiModelProperty(value = "당첨자혜택유형")
  private String eventBenefitTp;


  @ApiModelProperty(value = "댓글분류")
  private String commentValue;


  @ApiModelProperty(value = "댓글")
  private String comment;


  @ApiModelProperty(value = "관리자 비공개 여부")
  private String adminSecretYn;


  @ApiModelProperty(value = "관리자 비공개 여부 변경일")
  private String adminSecretYnChngDt;


  @ApiModelProperty(value = "혜택PK")
  private String pmPointId;


  @ApiModelProperty(value = "혜택PK")
  private String benefitId;


  @ApiModelProperty(value = "당첨자 혜택 경품명")
  private String benefitNm;


  @ApiModelProperty(value = "룰렛이벤트 아이템 PK")
  private String evEventRouletteItemId;


  @ApiModelProperty(value = "스탬프 개수")
  private int stampCnt;


  @ApiModelProperty(value = "등록일")
  private String createDt;


  // --------------------------------------------------------------------------
  // 추가 항목
  // --------------------------------------------------------------------------

  @UserMaskingUserName
  @ApiModelProperty(value = "사용자명")
  private String userNm;

  @UserMaskingLoginId
  @ApiModelProperty(value = "로그인ID")
  private String loginId;


  @ApiModelProperty(value = "가입일시")
  private String regDt;


  @ApiModelProperty(value = "구매회원PK")
  private String urBuyerId;


  @ApiModelProperty(value = "사번PK")
  private String urErpEmployeeCd;


  @ApiModelProperty(value = "임직원여부")
  private String employeeYn;


  @ApiModelProperty(value = "임직원여부명")
  private String employeeYnNm;


  @ApiModelProperty(value = "임직원제외여부")
  private String  employeeExceptYn;


  @ApiModelProperty(value = "이벤트참여제한여부")
  private String eventJoinYn;

  @UserMaskingEmail
  @ApiModelProperty(value = "이메일 (암호화)")
  private String mail;


  @UserMaskingMobile
  @ApiModelProperty(value = "모바일전화번호 (암호화)")
  private String mobile;


  @ApiModelProperty(value = "회원그룹유형")
  private String urGroupTp;


  @ApiModelProperty(value = "그룹명")
  private String groupNm;


  @ApiModelProperty(value = "신청일시")
  private String reqDt;

  @ApiModelProperty(value = "신청일자")
  private String reqDe;

  @ApiModelProperty(value = "신청내용")
  private String reqConts;


  @ApiModelProperty(value = "신청내용1")
  private String reqConts1;


  @ApiModelProperty(value = "신청내용2")
  private String reqConts2;


  @ApiModelProperty(value = "당첨내용")
  private String winnerConts;

  @ApiModelProperty(value = "당첨내용")
  private String winnerContsOnly;

  @ApiModelProperty(value = "당첨내용메모")
  private String winnerContsMemo;


  @ApiModelProperty(value = "혜택유형명")
  private String eventBenefitTpNm;


  @ApiModelProperty(value = "이벤트유형명")
  private String eventTpNm;


  @ApiModelProperty(value = "몰구분명")
  private String mallDivNm;


  @ApiModelProperty(value = "임직원 전용유형명")
  private String evEmployeeTpNm;


  @ApiModelProperty(value = "그룹유형명")
  private String evGroupTpNm;


  @ApiModelProperty(value = "이벤트참여유형명")
  private String eventJoinTpNm;


  @ApiModelProperty(value = "당첨자설정유형명")
  private String eventDrawTpNm;

  @ApiModelProperty(value = "개수")
  private int cnt;

  @ApiModelProperty(value = "이벤트명TITLE")
  private String eventTitle;

  // --------------------------------------------------------------------------
  // 추가 테이블 - EV_EVENT
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "이벤트 유형 - 일반이벤트(EVENT_TP.NORMAL) 설문이벤트(EVENT_TP.SURVEY) 스탬프(출석)(EVENT_TP.ATTEND) 스탬프(미션)(EVENT_TP.MISSION) 스탬프(구매)(EVENT_TP.PURCHASE) 룰렛이벤트(EVENT_TP.ROULETTE) 체험단이벤트(EVENT_TP.EXPERIENCE)")
  private String eventTp;


  @ApiModelProperty(value = "몰인몰 유형 - 풀무원(MALL_DIV.PULMUONE) 올가(MALL_DIV.ORGA) 베이비밀(MALL_DIV.BABYMEAL) 잇슬림(MALL_DIV.EATSLIM)")
  private String mallDiv;


  @ApiModelProperty(value = "사용여부")
  private String useYn;


  @ApiModelProperty(value = "삭제여부")
  private String delYn;


  @ApiModelProperty(value = "이벤트 제목")
  private String title;


  @ApiModelProperty(value = "이벤트 설명")
  private String description;


  @ApiModelProperty(value = "WEB PC 전시여부(Y:전시)")
  private String dispWebPcYn;


  @ApiModelProperty(value = "WEB MOBILE 전시여부(Y:전시)")
  private String dispWebMobileYn;


  @ApiModelProperty(value = "APP 전시여부(Y:전시)")
  private String dispAppYn;


  @ApiModelProperty(value = "임직원 전용 유형 - 제한없음(EV_EMPLOYEE_TP.NO_LIMIT) 임직원전용(EV_EMPLOYEE_TP.EMPLOYEE_ONLY) 임직원제외(EV_EMPLOYEE_TP.EMPLOYEE_EXCEPT)")
  private String evEmployeeTp;


  @ApiModelProperty(value = "접근권한 설정 유형 - 제한없음(EV_GROUP_TP.NO_LIMIT) 일반(EV_GROUP_TP.NORMAL) 프리미엄(EV_GROUP_TP.PREMIUM)")
  private String evGroupTp;


  @ApiModelProperty(value = "진행기간시작일시")
  private String startDt;


  @ApiModelProperty(value = "진행기간종료일시")
  private String endDt;


  @ApiModelProperty(value = "기간 종료 후 사용 자동종료 여부")
  private String timeOverCloseYn;


  @ApiModelProperty(value = "배너이미지경로PC")
  private String bnrImgPathPc;


  @ApiModelProperty(value = "배너이미지원본명PC")
  private String bnrImgOriginNmPc;


  @ApiModelProperty(value = "배너이미지경로Mobile")
  private String bnrImgPathMo;


  @ApiModelProperty(value = "배너이미지원본명Mobile")
  private String bnrImgOriginNmMo;


  @ApiModelProperty(value = "이벤트 상세 PC")
  private String detlHtmlPc;


  @ApiModelProperty(value = "이벤트 상세 Mobile")
  private String detlHtmlMo;


  @ApiModelProperty(value = "당첨자 안내")
  private String winnerInfor;


  @ApiModelProperty(value = "당첨자 안내 일자")
  private String winnerInforDt;


  @ApiModelProperty(value = "임직원참여여부")
  private String employeeJoinYn;


  @ApiModelProperty(value = "참여횟수 설정유형 - 일1회(EVENT_JOIN_TP.DAY_1) 기간내1회(EVENT_JOIN_TP.RANGE_1), 제한없음(EVENT_JOIN_TP.NO_LIMIT)")
  private String eventJoinTp;


  @ApiModelProperty(value = "당첨자 설정 유형 - 관리자추첨(EVENT_DRAW_TP.ADMIN) 자동추첨(EVENT_DRAW_TP.AUTO) 선착순 당첨(EVENT_DRAW_TP.FIRST_COME)")
  private String eventDrawTp;


  @ApiModelProperty(value = "등록자")
  private int createId;


  @ApiModelProperty(value = "수정자")
  private int modifyId;


  @ApiModelProperty(value = "수정일")
  private String modifyDt;


  // --------------------------------------------------------------------------
  // 추가 항목
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "포인트유형")
  private String pointTp;


  @ApiModelProperty(value = "포인트명")
  private String pointNm;


  @ApiModelProperty(value = "발급값")
  private String issueVal;


  @ApiModelProperty(value = "관리자쿠폰명")
  private String bosCouponNm;


  @ApiModelProperty(value = "전시쿠폰명")
  private String displayCouponNm;


  @ApiModelProperty(value = "당첨자수")
  private int winnerCnt;


  @ApiModelProperty(value = "당첨자수")
  private List<String> evEventJoinIdList;


  @ApiModelProperty(value = "NO")
  private int no;


  // --------------------------------------------------------------------------
  // 추가 테이블 - EV_EVENT_JOIN_SERVEY
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "이벤트참여설멍 PK")
  private String evEventJoinServeyId;


  @ApiModelProperty(value = "설문항목정보 PK")
  private String evEventSurveyQuestionId;


  @ApiModelProperty(value = "설문항목아이템정보 PK")
  private String evEventServeyItemId;


  @ApiModelProperty(value = "직접입력내용")
  private String otherCmnt;

  // --------------------------------------------------------------------------
  // 추가 테이블 - EV_EVENT_JOIN_COUPON
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "이벤트참여쿠폰 PK")
  private String evEventJoinCouponId;


  @ApiModelProperty(value = "쿠폰 PK")
  private String pmCouponId;


  @ApiModelProperty(value = "쿠폰지급수량")
  private String couponCnt;

  @ApiModelProperty(value = "순서")
  private String sort;

  @ApiModelProperty(value = "랜덤 추첨 햬택명")
  private String randomBenefitName;

  @ApiModelProperty(value = "당첨 방법")
  private String handwrittenLotteryTp;

  @ApiModelProperty(value = "당첨 방법명")
  private String handwrittenLotteryName;

  /**
   * 당첨 혜택명 조합.
   * 랜덤 관리자 추첨 혜택명 + 당첨 혜택명
   * @return
   */
  public String getCombineBenefitName(){
    String combineBenefitName = this.getRandomBenefitName();
//    if (StringUtil.isNotEmpty(this.getBenefitNm()) && StringUtil.isNotEmpty(this.getRandomBenefitName())) {
//      combineBenefitName += ",";
//    }
//    combineBenefitName += this.getRandomBenefitName();

    return combineBenefitName;
  }

  // --------------------------------------------------------------------------
  // 추가 테이블 - EV_EVENT_NORMAL
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "참여방법")
  private String normalEventTp;

  @ApiModelProperty(value = "기본배송지 우편번호")
  private String receiverZipCd;

  @ApiModelProperty(value = "기본배송지 기본주소")
  private String receiverAddr1;

  @ApiModelProperty(value = "기본배송지 상세주소")
  private String receiverAddr2;


}
