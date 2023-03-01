package kr.co.pulmuone.v1.promotion.manage.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingLoginId;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserNameLoginId;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExhibitVo {

  public ExhibitVo () {}

  public ExhibitVo (Object userInfo) {
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


  @ApiModelProperty(value = "기획전 PK")
  private String evExhibitId;


  @ApiModelProperty(value = "기획전 유형")
  private String exhibitTp;                  // EXHIBIT_TP : 일반기획전(EXHIBIT_TP.NORMAL) 골라담기(균일가)(EXHIBIT_TP.SELECT) 증정행사(EXHIBIT_TP.GIFT)


  @ApiModelProperty(value = "몰구분")
  private String mallDiv;                  // MALL_DIV


  @ApiModelProperty(value = "사용여부")
  private String useYn;


  @ApiModelProperty(value = "삭제여부")
  private String delYn;

  @ApiModelProperty(value = "전시여부")
  private String dispYn;

  @ApiModelProperty(value = "제목")
  private String title;


  @ApiModelProperty(value = "설명")
  private String description;


  @ApiModelProperty(value = "PC전시여부")
  private String dispWebPcYn;


  @ApiModelProperty(value = "Mobile전시여부")
  private String dispWebMobileYn;


  @ApiModelProperty(value = "APP전시여부")
  private String dispAppYn;


  @ApiModelProperty(value = "비회원노출여부")
  private String dispNonmemberYn;


  @ApiModelProperty(value = "임직원전용유형")
  private String evEmployeeTp;


  @ApiModelProperty(value = "접근권한설정유형 - 입력, 수정")
  private List<Long> userGroupIdList;


  @ApiModelProperty(value = "접근권한설정유형 - 조회")
  private List<EvUserGroupVo> userGroupList;


  @ApiModelProperty(value = "상시표시여부")
  private String alwaysYn;


  @ApiModelProperty(value = "진행기간시작일시")
  private String startDt;


  @ApiModelProperty(value = "진행기간종료일시")
  private String endDt;


  @ApiModelProperty(value = "기간종료후사용자동종료여부")
  private String timeOverCloseYn;


  @ApiModelProperty(value = "배너이미지경로")
  private String bnrImgPath;


  @ApiModelProperty(value = "배너이미지원본명")
  private String bnrImgOriginNm;


  @ApiModelProperty(value = "기획전상세PC")
  private String detlHtmlPc;


  @ApiModelProperty(value = "기획전상세Mobile")
  private String detlHtmlMo;


  @ApiModelProperty(value = "기획전승인상태")
  private String exhibitStatus;


  @ApiModelProperty(value = "등록자")
  @UserMaskingLoginId
  private String createId;


  @ApiModelProperty(value = "등록일")
  private String createDt;


  @ApiModelProperty(value = "수정자")
  @UserMaskingLoginId
  private String modifyId;


  @ApiModelProperty(value = "수정일")
  private String modifyDt;

  // --------------------------------------------------------------------------
  // 추가항목-상세
  // --------------------------------------------------------------------------
  // 일반기획전-그룹

  @ApiModelProperty(value = "그룹리스트")
  private List<ExhibitGroupVo> groupInfoList;
  // 골라담기기획전

  @ApiModelProperty(value = "골라담기")
  private ExhibitSelectVo selectExhibitInfo;
  // 증정행사

  @ApiModelProperty(value = "증정행사")
  private ExhibitGiftVo giftExhibitInfo;

  // --------------------------------------------------------------------------
  // 추가항목-상세-골라담기
  //  - 기획 상 1개만 존재하지만 리스트로 처리한다.
  // --------------------------------------------------------------------------
//
//  @ApiModelProperty(value = "골라담기상세리스트")
//  private List<ExhibitSelectVo> selectDetailList;
//
//
//  @ApiModelProperty(value = "골라담기상품리스트")
//  private List<ExhibitSelectVo> selectGoodsList;
//
//
//  @ApiModelProperty(value = "골라담기추가상품리스트")
//  private List<ExhibitSelectVo> selectAddGoodsList;



  // --------------------------------------------------------------------------
  // 추가항목 - IN
  // --------------------------------------------------------------------------
//
//  @ApiModelProperty(value = "몰구분String")
//  private String mallDivString;
//
//
//  @ApiModelProperty(value = "몰구분List")
//  private List<String> mallDivList;
//
//
//  @ApiModelProperty(value = "진행예정여부")
//  private String statusWaitYn;                      // 조회조건, 화면에서만 사용)
//
//
//  @ApiModelProperty(value = "진행중여부")
//  private String statusIngYn;                       // 조회조건, 화면에서만 사용)
//
//
//  @ApiModelProperty(value = "진행종료여부")
//  private String statusEndYn;                       // 조회조건, 화면에서만 사용)

  // --------------------------------------------------------------------------
  // 추가항목 - OUT
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "지급방식(증정방식) 유형")
  private String giftGiveTp;
  // 증정품배송조건

  @ApiModelProperty(value = "증정배송 유형")
  private String giftShippingTp;
  // 증정조건

  @ApiModelProperty(value = "증정조건 유형")
  private String giftTp;
  // 증정범위

  @ApiModelProperty(value = "증정범위 유형")
  private String giftRangeTp;
  // 증정범위

  @ApiModelProperty(value = "기획전전시여부")
  private String exhibitDispYn;

  @ApiModelProperty(value = "장바구니별 제한금액")
  private int overPrice;


  @ApiModelProperty(value = "지급방식(증정방식) 유형명")
  private String giftGiveTpNm;
  // 증정품배송조건

  @ApiModelProperty(value = "증정배송 유형명")
  private String giftShippingTpNm;
  // 증정조건

  @ApiModelProperty(value = "증정조건 유형명")
  private String giftTpNm;
  // 증정범위

  @ApiModelProperty(value = "증정범위 유형명")
  private String giftRangeTpNm;

  @ApiModelProperty(value = "진행상태")
  private String statusSe;

  @ApiModelProperty(value = "진행상태명")
  private String statusNm;

  @ApiModelProperty(value = "기획전유형명")
  private String exhibitTpNm;

  @ApiModelProperty(value = "몰구분명")
  private String mallDivNm;

  @ApiModelProperty(value = "담당자이름")
  @UserMaskingUserName
  private String userNm;

  @ApiModelProperty(value = "등록자로그인ID")
  @UserMaskingLoginId
  private String createLoginId;

  @ApiModelProperty(value = "담당자명로그인ID")
  @UserMaskingUserNameLoginId
  private String userNmLoginId;

  @ApiModelProperty(value = "정렬")
  private String sort;

  @ApiModelProperty(value = "수정자이름")
  @UserMaskingUserName
  private String modifyNm;

  @ApiModelProperty(value = "수정자로그인ID")
  @UserMaskingLoginId
  private String modifyLoginId;

  @ApiModelProperty(value = "PC전시여부명")
  private String dispWebPcYnNm;

  @ApiModelProperty(value = "Mobile전시여부명")
  private String dispWebMobileYnNm;

  @ApiModelProperty(value = "APP전시여부명")
  private String dispAppYnNm;

  @ApiModelProperty(value = "임직원전용여부명")
  private String evEmployeeTpNm;

  @ApiModelProperty(value = "승인상태명")
  private String exhibitStatusNm;

  @ApiModelProperty(value = "사용자PK")
  private String urUserId;

  @ApiModelProperty(value = "대표상품여부")
  private String repGoodsYn;

  @ApiModelProperty(value = "대표상품ID")
  private String repGoodsId;

  @ApiModelProperty(value = "대표상품명")
  private String repGoodsNm;

  @ApiModelProperty(value = "승인요청여부")
  private String approvalRequestYn;

  @ApiModelProperty(value = "승인 상태 코드")
  private String approvalStatus;

  @ApiModelProperty(value = "승인 상태 코드 명")
  private String approvalStatusName;

  @ApiModelProperty(value = "승인 요청자")
  private String approvalRequestUserId;

  @ApiModelProperty(value = "승인 1차 담당자")
  private String approvalSubUserId;

  @ApiModelProperty(value = "승인 2차 담당자")
  private String approvalUserId;

  @ApiModelProperty(value = "승인 상태 사유")
  private String approvalComment;

  @ApiModelProperty(value = "승인 1차 담당자 정보")
  private ApprovalUserVo subUser;

  @ApiModelProperty(value = "승인 2차 담당자 정보")
  private ApprovalUserVo user;


  // --------------------------------------------------------------------------
  //
  // --------------------------------------------------------------------------

  // --------------------------------------------------------------------------
  //
  // --------------------------------------------------------------------------



}
