package kr.co.pulmuone.v1.promotion.manage.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventSurveyItemAttcVo {

  public EventSurveyItemAttcVo () {}

  public EventSurveyItemAttcVo (Object userInfo) {
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
  // EV_EVENT_SURVEY_ITEM_ATTC
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "설문이벤트첨부파일PK")
  private String evEventSurveyItemAttcId;


  @ApiModelProperty(value = "설문항목 아이템 정보 PK")
  private String evEventSurveyItemId;


  @ApiModelProperty(value = "파일전체경로")
  private String imgPath;


  @ApiModelProperty(value = "원본파일명")
  private String imgOriginNm;

  // --------------------------------------------------------------------------
  // 추가항목
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "등록자")
  private int createId;


  @ApiModelProperty(value = "등록일")
  private String createDt;


  @ApiModelProperty(value = "수정자")
  private int modifyId;


  @ApiModelProperty(value = "수정일")
  private String modifyDt;


}
