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
public class EventStampVo {

  public EventStampVo () {}

  public EventStampVo (Object userInfo) {
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


  @ApiModelProperty(value = "스탬프 이벤트 PK")
  private String evEventStampId;


  @ApiModelProperty(value = "이벤트 PK")
  private String evEventId;


  @ApiModelProperty(value = "당첨자 설정 유형")
  private String eventDrawTp;


  @ApiModelProperty(value = "참여버튼 BG color code")
  private String btnColorCd;


  @ApiModelProperty(value = "스탬프 기본 이미지 경로")
  private String defaultPath;


  @ApiModelProperty(value = "스탬프 기본 이미지 원본 파일명")
  private String defaultOriginNm;


  @ApiModelProperty(value = "스탬프 체크 이미지 경로")
  private String checkPath;


  @ApiModelProperty(value = "스탬프 체크 이미지 원본 파일명")
  private String checkOriginNm;


  @ApiModelProperty(value = "스탬프 배경 이미지 경로")
  private String bgPath;


  @ApiModelProperty(value = "스탬프 배경 이미지 원본 파일명")
  private String bgOriginNm;


  @ApiModelProperty(value = "스탬프 개수1")
  private int stampCnt1;


  @ApiModelProperty(value = "스탬프 개수2")
  private int stampCnt2;


  @ApiModelProperty(value = "스탬프지급조건 금액")
  private int orderPrice;


  @ApiModelProperty(value = "스탬프지급조건 유형")
  private String eventStampOrderTp;


  @ApiModelProperty(value = "스탬프 업데이트 일시")
  private String stampUpdateDt;


  // --------------------------------------------------------------------------
  // 추가항목
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "쿠폰리스트")
  List<EventVo> eventCouponList;

  @ApiModelProperty(value = "스탬프지급조건 유형명")
  private String eventStampOrderTpNm;

  @ApiModelProperty(value = "적립금명")
  private String pointNm;

  @ApiModelProperty(value = "적립금Pk")
  private String pmPointId;

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
  // EV_EVENT_STAMP_DETL
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "스탬프 이벤트 상세 PK")
  private String evEventStampDetlId;

  @ApiModelProperty(value = "스탬프 개수")
  private int stampCnt;

  @ApiModelProperty(value = "스탬프 아이콘 이미지 경로")
  private String iconPath;

  @ApiModelProperty(value = "스탬프 아이콘 이미지 원본 파일명")
  private String iconOriginNm;

  @ApiModelProperty(value = "당첨자 혜택")
  private String eventBenefitTp;

  @ApiModelProperty(value = "혜택 PK")
  private String benefitId;

  @ApiModelProperty(value = "당첨자 혜택 경품 명")
  private String benefitNm;

  @ApiModelProperty(value = "스탬프 노출 URL")
  private String stampUrl;


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
