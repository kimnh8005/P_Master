package kr.co.pulmuone.v1.promotion.manage.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventSurveyVo {

  public EventSurveyVo () {}

  public EventSurveyVo (Object userInfo) {
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



  @ApiModelProperty(value = "설문조사 정보 PK")
  private String evEventSurveyId;


  @ApiModelProperty(value = "이벤트 PK")
  private String evEventId;


  @ApiModelProperty(value = "당첨자 설정 유형")
  private String eventDrawTp;


  @ApiModelProperty(value = "참여버튼 BG color code")
  private String btnColorCd;


  @ApiModelProperty(value = "당첨자 혜택")
  private String eventBenefitTp;


  @ApiModelProperty(value = "적립금 PK")
  private String pmPointId;


  @ApiModelProperty(value = "당첨자 혜택")
  private String benefitNm;


  @ApiModelProperty(value = "설문아이템리스트")
  List<EventSurveyItemVo> eventSurveyItemList;


//  @ApiModelProperty(value = "설문아이템참여리스트")
//  List<EventJoinVo> eventSurveyItemJoinList;

  // --------------------------------------------------------------------------
  // 추가항목
  // --------------------------------------------------------------------------
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
  // EV_EVENT_SURVEY_QUESTION
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "설문항목 정보 PK")
  private String evEventSurveyQuestionId;


  @ApiModelProperty(value = "순서")
  private int sort;


  @ApiModelProperty(value = "설문제목")
  private String title;


  @ApiModelProperty(value = "설문유형")
  private String eventSurveyTp;


  @ApiModelProperty(value = "설문유형")
  private String eventSurveyTpNm;

  // --------------------------------------------------------------------------
  // EV_EVENT_SURVEY_ITEM
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "설문항목 아이템 정보 PK")
  private String evEventSurveyItemId;


  @ApiModelProperty(value = "설문 보기")
  private String item;


  @ApiModelProperty(value = "직접입력여부")
  private String directInputYn;

  // --------------------------------------------------------------------------
  // EV_EVENT_SURVEY_ITEM_ATTC
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "설문이벤트첨부파일PK")
  private String evEventSurveyItemAttcId;


  @ApiModelProperty(value = "파일전체경로")
  private String imgPath;


  @ApiModelProperty(value = "원본파일명")
  private String imgOriginNm;



}
