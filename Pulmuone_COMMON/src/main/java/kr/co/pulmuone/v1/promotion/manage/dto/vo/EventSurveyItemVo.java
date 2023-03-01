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
public class EventSurveyItemVo {

  public EventSurveyItemVo () {}

  public EventSurveyItemVo (Object userInfo) {
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

  // --------------------------------------------------------------------------
  // EV_EVENT_SURVEY_ITEM
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "설문항목 아이템 정보 PK")
  private String evEventSurveyItemId;


  @ApiModelProperty(value = "설문항목 정보 PK")
  private String evEventSurveyQuestionId;


  @ApiModelProperty(value = "순서")
  private int sort;


  @ApiModelProperty(value = "설문 보기")
  private String item;


  @ApiModelProperty(value = "직접입력여부")
  private String directInputYn;


  @ApiModelProperty(value = "설문아이템첨부파일리스트")
  List<EventSurveyItemAttcVo> eventSurveyItemAttcList;


  // --------------------------------------------------------------------------
  // 추가항목
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "아이템선택수")
  private int itemSum;

  @ApiModelProperty(value = "아이템전체수")
  private int itemTotalSum;

  @ApiModelProperty(value = "아이템비율")
  private String itemRate;


  @ApiModelProperty(value = "등록자")
  private int createId;


  @ApiModelProperty(value = "등록일")
  private String createDt;


  @ApiModelProperty(value = "수정자")
  private int modifyId;


  @ApiModelProperty(value = "수정일")
  private String modifyDt;

  // --------------------------------------------------------------------------
  // 추가테이블 - EV_EVENT_JOIN_SURVEY
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "이벤트참여 PK")
  private String evEventJoinId;

  @ApiModelProperty(value = "이벤트 PK")
  private String evEventId;

  @ApiModelProperty(value = "이벤트참여설문 PK")
  private String evEventJoinServeyId;

  @ApiModelProperty(value = "설문항목아이템정보 PK")
  private String evEventServeyItemId;

  @ApiModelProperty(value = "직접입력내용")
  private String otherCmnt;

  @ApiModelProperty(value = "설문명")
  private String title;

  @ApiModelProperty(value = "설문명")
  private String qSort;


}
