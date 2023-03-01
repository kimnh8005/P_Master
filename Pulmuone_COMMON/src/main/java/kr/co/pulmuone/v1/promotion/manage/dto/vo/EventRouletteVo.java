package kr.co.pulmuone.v1.promotion.manage.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class EventRouletteVo {

  public EventRouletteVo () {}

  public EventRouletteVo (Object userInfo) {
    // 등록자/수정자 Set
    if (userInfo!= null) {
      if (((UserVo)userInfo).getUserId() != null) {

        this.setModifyId(Integer.parseInt((SessionUtil.getBosUserVO()).getUserId()));
        this.setCreateId(Integer.parseInt((SessionUtil.getBosUserVO()).getUserId()));
      }
      else {
        this.setModifyId(0);
        this.setCreateId(0);
      }
    }
    else {
      this.setModifyId(0);
      this.setCreateId(0);
    }
  }



  @ApiModelProperty(value = "룰렛이벤트 PK")
  private String evEventRouletteId;


  @ApiModelProperty(value = "이벤트 PK")
  private String evEventId;


  @ApiModelProperty(value = "당첨자 설정 유형")
  private String eventDrawTp;


  @ApiModelProperty(value = "시작버튼 이미지 경로")
  private String startBtnPath;


  @ApiModelProperty(value = "시작버튼 이미지 원본 파일명")
  private String startBtnOriginNm;


  @ApiModelProperty(value = "화살표 이미지 경로")
  private String arrowPath;


  @ApiModelProperty(value = "화살표 이미지 원본 파일명")
  private String arrowOriginNm;


  @ApiModelProperty(value = "배경 이미지 경로")
  private String bgPath;


  @ApiModelProperty(value = "배경 이미지 원본 파일명")
  private String bgOriginNm;


  @ApiModelProperty(value = "룰렛 이미지 경로")
  private String roulettePath;


  @ApiModelProperty(value = "룰렛 이미지 원본 파일명")
  private String rouletteOriginNm;


  @ApiModelProperty(value = "룰렛 개수")
  private int rouletteCnt;


  @ApiModelProperty(value = "이벤트제한고객룰렛설정수")
  private int exceptionUserRouletteCnt;

  //@ApiModelProperty(value = "당첨자공지사항")
  //private String winnerNotice;

  // --------------------------------------------------------------------------
  // 추가항목
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "쿠폰리스트")
  List<EventVo> eventCouponList;

  @ApiModelProperty(value = "당첨자 혜택명")
  private String eventBenefitTpNm;

  @ApiModelProperty(value = "적립금명")
  private String pointNm;

  @ApiModelProperty(value = "적립금")
  private int issueVal;

  @ApiModelProperty(value = "적립구분")
  private String pointTp;

  @ApiModelProperty(value = "적립구분명")
  private String pointTpNm;

  @ApiModelProperty(value = "분담조직코드")
  private String urErpOrganizationCd;

  @ApiModelProperty(value = "분담조직명")
  private String urErpOrganizationNm;

  @ApiModelProperty(value = "등록자")
  private int createId;

  @ApiModelProperty(value = "등록일")
  private String createDt;

  @ApiModelProperty(value = "수정자")
  private int modifyId;

  @ApiModelProperty(value = "수정일")
  private String modifyDt;

  // --------------------------------------------------------------------------
  // EV_EVENT_ROULETTE_ITEM
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "룰렛이벤트 아이템 PK")
  private String evEventRouletteItemId;


  @ApiModelProperty(value = "룰렛이미지 경로")
  private String itemPath;


  @ApiModelProperty(value = "룰렛이미지 원본 파일명")
  private String itemOriginNm;


  @ApiModelProperty(value = "당첨자 혜택")
  private String eventBenefitTp;


  @ApiModelProperty(value = "혜택 PK ")
  private String benefitId;


  @ApiModelProperty(value = "당첨자 혜택 경품 명")
  private String benefitNm;


  @ApiModelProperty(value = "당첨확률")
  private String awardRt;


  @ApiModelProperty(value = "최대당첨자수")
  private int awardMaxCnt;

  @ApiModelProperty(value = "예상참여자수")
  private int expectJoinUserCnt;

  @ApiModelProperty(value = "롤렛 이벤트 예상참여자수")
  private int expectJoinUserCntRoulette;



  // --------------------------------------------------------------------------
  //
  // --------------------------------------------------------------------------


  // --------------------------------------------------------------------------
  //
  // --------------------------------------------------------------------------


  // --------------------------------------------------------------------------
  //
  // --------------------------------------------------------------------------




}
