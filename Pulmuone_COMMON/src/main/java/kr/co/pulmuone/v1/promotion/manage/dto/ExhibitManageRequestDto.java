package kr.co.pulmuone.v1.promotion.manage.dto;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitGiftGoodsVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitGiftVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitGroupVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitSelectGoodsVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitSelectVo;
import kr.co.pulmuone.v1.promotion.manage.dto.vo.ExhibitVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@Slf4j
@Getter
@Setter
@ToString
@ApiModel(description = "ExhibitManageRequestDto")
public class ExhibitManageRequestDto extends BaseRequestPageDto {


  @ApiModelProperty(value = "기획전 PK", required = false)
  private String evExhibitId;

  @ApiModelProperty(value = "기획전그룹 PK", required = false)
  private String evExhibitGroupId;

  @ApiModelProperty(value = "기획전골라담기 PK", required = false)
  private String evExhibitSelectId;

  @ApiModelProperty(value = "기획전증정행사 PK", required = false)
  private String evExhibitGiftId;

  @ApiModelProperty(value = "기획전증정행사상품 PK", required = false)
  private String evExhibitGiftGoodsId;

  @ApiModelProperty(value = "제목", required = false)
  private String title;

  @ApiModelProperty(value = "몰구분", required = false)
  private String mallDiv;

  @ApiModelProperty(value = "몰구분String", required = false)
  private String mallDivJsonString;

  @ApiModelProperty(value = "진행상태String", required = false)
  private String stausTpJsonString;                                           // 공통코드아님(조회조건, 화면에서만 사용)

  @ApiModelProperty(value = "사용여부", required = false)
  private String useYn;

  @ApiModelProperty(value = "전시여부", required = false)
  private String dispYn;

  @ApiModelProperty(value = "PC전시여부", required = false)
  private String dispWebPcYn;                                             // 노출여부(디바이스)-PC

  @ApiModelProperty(value = "Mobile전시여부", required = false)           // 노출여부(디바이스)-M Web
  private String dispWebMobileYn;

  @ApiModelProperty(value = "APP전시여부", required = false)              // 노출여부(디바이스)-App
  private String dispAppYn;

  @ApiModelProperty(value = "비회원노출여부", required = false)
  private String dispNonmemberYn;

  @ApiModelProperty(value = "임직원전용유형", required = false)
  private String evEmployeeTp;

  @ApiModelProperty(value = "접근권한설정유형", required = false)
  private String userGroupFilter;

  @ApiModelProperty(value = "시작일자", required = false)
  private String startDe;                                                 // 시작일자(YYYYMMDD)

  @ApiModelProperty(value = "시작일자", required = false)
  private String endDe;                                                   // 종료일자(YYYYMMDD)

  @ApiModelProperty(value = "상시진행여부", required = false)
  private String alwaysYn;

  @ApiModelProperty(value = "기획전리스트String", required = false)
  private String evExhibitIdListString;

  @ApiModelProperty(value = "그룹상세리스트String", required = false)
  private String evExhibitGroupDetlIdListString;

  @ApiModelProperty(value = "골라담기상품리스트String", required = false)
  private String evExhibitSelectGoodsIdListString;

  @ApiModelProperty(value = "골라담기추가상품리스트String", required = false)
  private String evExhibitSelectAddGoodsIdListString;

  @ApiModelProperty(value = "증정행사상품리스트String", required = false)
  private String evExhibitGiftGoodsIdListString;

  @ApiModelProperty(value = "증정행사대상상품리스트String", required = false)
  private String evExhibitGiftTargetGoodsIdListString;

  @ApiModelProperty(value = "증정행사대상브랜드리스트String", required = false)
  private String evExhibitGiftTargetBrandIdListString;

  @ApiModelProperty(value = "상품코드리스트String", required = false)
  private String ilGoodsIdListString;


  // --------------------------------------------------------------------------
  // 추가
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "기획전유형", required = false)
  private String exhibitTp;

  @ApiModelProperty(value = "몰구분리스트", required = false)
  private List<String> mallDivList;

  //@ApiModelProperty(value = "승인상태리스트", required = false)
  //private List<String> exhibitStatusList;

  @ApiModelProperty(value = "승인상태리스트", required = false)
  private List<String> approvalStatusList;

  @ApiModelProperty(value = "진행상태리스트", required = false)
  private List<String> stausTpList;

  @ApiModelProperty(value = "노출범위(디바이스)리스트", required = false)
  private List<String> goodsDisplayTypeList;

  @ApiModelProperty(value = "임직원전용유형리스트", required = false)
  private List<String> evEmployeeTpList;

  @ApiModelProperty(value = "접근권한설정유형리스트", required = false)
  private List<String> userGroupIdList;

  @ApiModelProperty(value = "지급방식(증정방식)리스트", required = false)
  private List<String> giftGiveTpList;

  @ApiModelProperty(value = "지급방식(증정방식)리스트", required = false)
  private List<String> evExhibitIdList;

  @ApiModelProperty(value = "그룹상세ID리스트", required = false)
  private List<String> evExhibitGroupDetlIdList;

  @ApiModelProperty(value = "골라담기상품ID리스트", required = false)
  private List<String> evExhibitSelectGoodsIdList;

  @ApiModelProperty(value = "골라담기추가상품ID리스트", required = false)
  private List<String> evExhibitSelectAddGoodsIdList;

  @ApiModelProperty(value = "증정행사상품ID리스트", required = false)
  private List<String> evExhibitGiftGoodsIdList;

  @ApiModelProperty(value = "증정행사대상상품ID리스트", required = false)
  private List<String> evExhibitGiftTargetGoodsIdList;

  @ApiModelProperty(value = "증정행사대상브랜드ID리스트", required = false)
  private List<String> evExhibitGiftTargetBrandIdList;

  @ApiModelProperty(value = "상품코드리스트String", required = false)
  private List<String> ilGoodsIdList;


  // 기획전기본
  @ApiModelProperty(value = "기획전기본정보", required = false)
  private ExhibitVo exhibitInfo;

  @ApiModelProperty(value = "기획전기본정보JsonString", required = false)
  private String exhibitDataJsonString;

  // 전시그룹
  @ApiModelProperty(value = "기획전그룹정보", required = false)
  private ExhibitGroupVo exhibitGroupInfo;

  @ApiModelProperty(value = "기획전그룹정보JsonString", required = false)
  private String exhibitGroupInfoJsonString;

  // 골라담기
  @ApiModelProperty(value = "골라담기정보", required = false)
  private ExhibitSelectVo exhibitSelectInfo;

  @ApiModelProperty(value = "골라담기정보JsonString", required = false)
  private String exhibitSelectInfoJsonString;

  @ApiModelProperty(value = "골라담기상품리스트", required = false)
  private List<ExhibitSelectGoodsVo> exhibitSelectGoodsList;

  @ApiModelProperty(value = "골라담기상품리스트JsonString", required = false)
  private String exhibitSelectGoodsListJsonString;

  @ApiModelProperty(value = "골라담기추가상품리스트", required = false)
  private List<ExhibitSelectGoodsVo> exhibitSelectAddGoodsList;

  @ApiModelProperty(value = "골라담기추가상품리스트JsonString", required = false)
  private String exhibitSelectAddGoodsListJsonString;

  // 증정행사
  @ApiModelProperty(value = "증정행사정보", required = false)
  private ExhibitGiftVo exhibitGiftInfo;

  @ApiModelProperty(value = "증정행사정보JsonString", required = false)
  private String exhibitGiftInfoJsonString;

  @ApiModelProperty(value = "증정행사상품리스트", required = false)
  private List<ExhibitGiftGoodsVo> exhibitGiftGoodsList;

  @ApiModelProperty(value = "증정행사상품리스트JsonString", required = false)
  private String exhibitGiftGoodsListJsonString;

  @ApiModelProperty(value = "증정대상상품리스트", required = false)
  private List<ExhibitGiftGoodsVo> exhibitGiftTargetGoodsList;

  @ApiModelProperty(value = "증정대상상품리스트JsonString", required = false)
  private String exhibitGiftTargetGoodsListJsonString;

  @ApiModelProperty(value = "증정대상브랜드리스트", required = false)
  private List<ExhibitGiftGoodsVo> exhibitGiftTargetBrandList;

  @ApiModelProperty(value = "증정대상브랜드리스트JsonString", required = false)
  private String exhibitGiftTargetBrandListJsonString;

  @ApiModelProperty(value = "진행예정여부", required = false)
  private String statusWaitYn;

  @ApiModelProperty(value = "진행중여부", required = false)
  private String statusIngYn;

  @ApiModelProperty(value = "진행종료여부", required = false)
  private String statusEndYn;

  @ApiModelProperty(value = "진행기간시작일시", required = false)
  private String startDt;

  @ApiModelProperty(value = "진행기간종료일시", required = false)
  private String endDt;

  @ApiModelProperty(value = "담당자구분", required = false)
  private String manageSe;

  @ApiModelProperty(value = "담당자ID", required = false)
  private String managerId;

  //@ApiModelProperty(value = "승인상태", required = false)
  //private String exhibitStatus;

  @ApiModelProperty(value = "승인상태", required = false)
  private String approvalStatus;

  @ApiModelProperty(value = "지급방식(증정방식)", required = false)
  private String giftGiveTp;

  @ApiModelProperty(value = "증정품배송조건", required = false)
  private String giftShippingTp;

  @ApiModelProperty(value = "증정조건", required = false)
  private String giftTp;

  @ApiModelProperty(value = "증정범위", required = false)
  private String giftRangeTp;


  // --------------------------------------------------------------------------
  // 검색조건
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "검색구분", required = false)
  private String searchSe;

  @ApiModelProperty(value = "키워드", required = false)
  private String keyWord;

  @ApiModelProperty(value = "진행상태구분", required = false)
  private String statusYnSe;

  @ApiModelProperty(value = "진행상태구분리스트", required = false)
  private List<String> statusYnSeList;

  @ApiModelProperty(value = "노출범위(디바이스)유형", required = false)
  private String goodsDisplayType;

  @ApiModelProperty(value = "회원등급레벨", required = false)
  private String groupLevelSe;

  @ApiModelProperty(value = "등록자", required = false)
  private String createId;

  @ApiModelProperty(value = "수정자", required = false)
  private String modifyId;


  // --------------------------------------------------------------------------
  // 등록 관련
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "그룹리스트String", required = false)
  private String groupListJsonString;

  @ApiModelProperty(value = "그룹리스트", required = false)
  private List<ExhibitGroupVo> groupList;


  // ==========================================================================
  // convert Data
  // ==========================================================================
  // 리스트정보 -> List 변환
  //public void convertDataList() throws BaseException {
  //
  //  // 몰구분
  //  try {
  //    if (StringUtil.isNotEmpty(this.mallDivJsonString)) {
  //      this.mallDivList  = BindUtil.convertJsonArrayToDtoList(this.mallDivJsonString, String.class);
  //    }
  //  }
  //  catch (Exception e) {
  //    throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_JSON); // TODO UserEnums에 추가
  //  }
  //
  //  // 진행상태
  //  try {
  //    if (StringUtil.isNotEmpty(this.stausTpJsonString)) {
  //      this.mallDivList  = BindUtil.convertJsonArrayToDtoList(this.stausTpJsonString, String.class);
  //    }
  //  }
  //  catch (Exception e) {
  //    throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_JSON); // TODO UserEnums에 추가
  //  }
  //}


  // --------------------------------------------------------------------------
  // VO Set
  // --------------------------------------------------------------------------
  // 기획전기본정보 -> ExhibitVo 변환
  public void convertExhibitDataObject() throws BaseException {

    try {
      ObjectMapper objMqpper = new ObjectMapper();
      this.exhibitInfo = objMqpper.readValue(exhibitDataJsonString, ExhibitVo.class);
    }
    catch (Exception e) {
      throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_JSON);      // TODO UserEnums에 추가
    }
  }

  // 전시그룹정보 -> ExhibitGroupVo 변환
  public void convertExhibitGroupDataObject() throws BaseException {

    try {
      ObjectMapper objMqpper = new ObjectMapper();
      this.exhibitGroupInfo = objMqpper.readValue(exhibitGroupInfoJsonString, ExhibitGroupVo.class);
    }
    catch (Exception e) {
      throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_JSON);      // TODO UserEnums에 추가
    }
  }

  // 골라담기정보 -> ExhibitSelectVo 변환
  public void convertExhibitSelectDataObject() throws BaseException {

    try {
      ObjectMapper objMqpper = new ObjectMapper();
      this.exhibitSelectInfo = objMqpper.readValue(exhibitSelectInfoJsonString, ExhibitSelectVo.class);
    }
    catch (Exception e) {
      throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_JSON);      // TODO UserEnums에 추가
    }
  }

  // 증정행사정보 -> ExhibitGiftVo 변환
  public void convertExhibitGiftDataObject() throws BaseException {

    try {
      ObjectMapper objMqpper = new ObjectMapper();
      this.exhibitGiftInfo = objMqpper.readValue(exhibitGiftInfoJsonString, ExhibitGiftVo.class);
    }
    catch (Exception e) {
      throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_JSON);      // TODO UserEnums에 추가
    }
  }



}
