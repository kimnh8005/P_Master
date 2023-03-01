package kr.co.pulmuone.v1.promotion.manage.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class EventVo {

  public EventVo () {}

  public EventVo (Object userInfo) {
    // 등록자/수정자 Set
    if (userInfo!= null) {
      if (((UserVo)userInfo).getUserId() != null) {

        this.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        this.setCreateId((SessionUtil.getBosUserVO()).getUserId());
      }
      else {
        this.setModifyId("0");
        this.setCreateId("0");
      }
    }
    else {
      this.setModifyId("0");
      this.setCreateId("0");
    }
  }


  @ApiModelProperty(value = "이벤트 PK")
  private String evEventId;


  @ApiModelProperty(value = "이벤트 유형")
  private String eventTp;


  @ApiModelProperty(value = "몰인몰 유형")
  private String mallDiv;


  @ApiModelProperty(value = "사용여부")
  private String useYn;


  @ApiModelProperty(value = "삭제여부")
  private String delYn;

  @ApiModelProperty(value = "전시여부")
  private String dispYn;


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


  @ApiModelProperty(value = "임직원전용유형")
  private String evEmployeeTp;


  @ApiModelProperty(value = "접근권한설정유형")
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

  @ApiModelProperty(value = "이벤트 상세 2 PC")
  private String detlHtmlPc2;

  @ApiModelProperty(value = "이벤트 상세 2 Mobile")
  private String detlHtmlMo2;


  @ApiModelProperty(value = "당첨자 공지사항")
  private String winnerNotice;


  @ApiModelProperty(value = "당첨자 안내")
  private String winnerInfor;


  @ApiModelProperty(value = "당첨자 안내일자")
  private String winnerInforDt;


  @ApiModelProperty(value = "임직원참여여부")
  private String employeeJoinYn;


  @ApiModelProperty(value = "참여횟수 설정유형")
  private String eventJoinTp;


  @ApiModelProperty(value = "당첨자 설정유형")
  private String eventDrawTp;


  @ApiModelProperty(value = "등록자")
  private String createId;


  @ApiModelProperty(value = "등록일")
  private String createDt;


  @ApiModelProperty(value = "수정자")
  private String modifyId;


  @ApiModelProperty(value = "수정일")
  private String modifyDt;

  // --------------------------------------------------------------------------
  // 추가항목
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "접근권한설정유형 - 조회")
  private List<EvUserGroupVo> userGroupList;

  @ApiModelProperty(value = "진행상태명")
  private String statusNm;


  @ApiModelProperty(value = "진행상태")
  private String statusSe;

  @ApiModelProperty(value = "담당자")
  @UserMaskingUserName
  private String userNm;


  @ApiModelProperty(value = "등록자로그인ID")
  @UserMaskingLoginId
  private String createLoginId;

  @ApiModelProperty(value = "수정자이름")
  @UserMaskingUserName
  private String modifyNm;


  @ApiModelProperty(value = "수정자로그인ID")
  @UserMaskingLoginId
  private String modifyLoginId;


  @ApiModelProperty(value = "이벤트유형명")
  private String eventTpNm;


  @ApiModelProperty(value = "몰구분명")
  private String mallDivNm;


  @ApiModelProperty(value = "WEB PC 전시여부명")
  private String dispWebPcYnNm;


  @ApiModelProperty(value = "WEB MOBILE 전시여부명")
  private String dispWebMobileYnNm;


  @ApiModelProperty(value = "APP 전시여부명")
  private String dispAppYnNm;


  @ApiModelProperty(value = "임직원전용여부명")
  private String evEmployeeTpNm;


  @ApiModelProperty(value = "접금권한설정유형명")
  private String evGroupTpNm;


  @ApiModelProperty(value = "참여횟수설정유형명")
  private String eventJoinTpNm;


  @ApiModelProperty(value = "당첨자설졍유형명")
  private String eventDrawTpNm;

  @ApiModelProperty(value = "신규회원 확인 유무")
  private String checkNewUserYn;


  // ==========================================================================
  // 추가
  // ==========================================================================
  @ApiModelProperty(value = "댓글허용여부")
  private String commentYn;

  // --------------------------------------------------------------------------
  // --------------------------------------------------------------------------
  // EV_EVENT_COMMENT_CODE
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "이벤트 댓글코드 PK")
  private String evEventCommentCodeId;

  @ApiModelProperty(value = "값")
  private String commentValue;

  // --------------------------------------------------------------------------
  // EV_EVENT_COUPON
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "이벤트 쿠폰 PK")
  private String evEventCouponId;

  @ApiModelProperty(value = "이벤트상세 PK")
  private String evEventDetlId;

  @ApiModelProperty(value = "쿠폰 PK")
  private String pmCouponId;

  @ApiModelProperty(value = "쿠폰지급수량")
  private int couponCnt;

  @ApiModelProperty(value = "총 당첨 수량")
  private int couponTotalCnt;

  @ApiModelProperty(value = "BOS쿠폰명")
  private String bosCouponNm;

  @ApiModelProperty(value = "전시쿠폰명")
  private String displayCouponNm;

  @ApiModelProperty(value = "쿠폰할인방식")
  private String discountTp;

  @ApiModelProperty(value = "쿠폰할인방식명")
  private String discountTpNm;

  @ApiModelProperty(value = "할인상세")
  private String discountValue;

  @ApiModelProperty(value = "유효기간")
  private String validityConts;

  @ApiModelProperty(value = "분담조직코드")
  private String urErpOrganizationCd;

  @ApiModelProperty(value = "분담조직명")
  private String urErpOrganizationNm;

  @ApiModelProperty(value = "Style ID")
  private String styleId;

  @ApiModelProperty(value = "적용범위")
  private String coverageType;

  @ApiModelProperty(value = "발급기간")
  private String issueDt;

  @ApiModelProperty(value = "유효기간 설정타입(기간설정, 유효일설정)")
  private String validityTp;

}
