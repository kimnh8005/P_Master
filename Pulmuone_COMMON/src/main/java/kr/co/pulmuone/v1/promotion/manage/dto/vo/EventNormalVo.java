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
public class EventNormalVo {

  public EventNormalVo () {}

  public EventNormalVo (Object userInfo) {
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



  @ApiModelProperty(value = "일반이벤트 PK")
  private String evEventNormalId;


  @ApiModelProperty(value = "이벤트 PK")
  private String evEventId;


  @ApiModelProperty(value = "댓글 허용여부")
  private String commentYn;


  @ApiModelProperty(value = "댓글 분류 설정여부")
  private String commentCodeYn;


  @ApiModelProperty(value = "당첨자 설정 유형")
  private String eventDrawTp;


  @ApiModelProperty(value = "당첨자 혜택")
  private String eventBenefitTp;


  @ApiModelProperty(value = "적립금 PK")
  private String pmPointId;


  @ApiModelProperty(value = "당첨자 혜택")
  private String benefitNm;


  // --------------------------------------------------------------------------
  // 추가항목
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "당첨자 혜택명")
  private String eventBenefitTpNm;

  @ApiModelProperty(value = "적립금명")
  private String pointNm;

  @ApiModelProperty(value = "적립금")
  private int issueVal;

  @ApiModelProperty(value = "발급기간")
  private String issueDate;

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

  @ApiModelProperty(value = "당첨확률")
  private String awardRt;

  @ApiModelProperty(value = "예상 참여자 수")
  private String expectJoinUserCnt;

  @ApiModelProperty(value = "일반이벤트 참여방법")
  private String normalEventTp;

  @ApiModelProperty(value = "참여조건")
  private String joinCondition;

  @ApiModelProperty(value = "주문고객-주문배송유형")
  private String goodsDeliveryTp;

  @ApiModelProperty(value = "주문고객-주문유형")
  private String eventStampOrderTp;

  @ApiModelProperty(value = "주문건수")
  private int orderCnt;

  @ApiModelProperty(value = "구매금액")
  private int orderPrice;

  @ApiModelProperty(value = "총 당첨 수량")
  private int totalWinCnt;

  @ApiModelProperty(value = "유효기간 설정타입(기간설정, 유효일설정)")
  private String validityTp;

}
