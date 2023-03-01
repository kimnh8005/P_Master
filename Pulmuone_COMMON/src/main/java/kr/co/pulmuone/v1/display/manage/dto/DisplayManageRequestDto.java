package kr.co.pulmuone.v1.display.manage.dto;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.pagehelper.util.StringUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.display.manage.dto.vo.ContsVo;
import kr.co.pulmuone.v1.display.manage.dto.vo.InventoryVo;
import kr.co.pulmuone.v1.display.manage.dto.vo.PageVo;
import kr.co.pulmuone.v1.display.manage.service.DisplayManageService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
@ApiModel(description = "DisplayManageRequestDto")
public class DisplayManageRequestDto extends BaseRequestPageDto {

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전시페이지관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  @ApiModelProperty(value = "전시페이지ID", required = false)
  private String dpPageId;

  @ApiModelProperty(value = "깊이", required = false)
  private String depth;

  @ApiModelProperty(value = "미사용포함여부", required = false)
  private String useAllYn;

  @ApiModelProperty(value = "몰구분", required = false)
  private String mallDiv;



  @ApiModelProperty(value = "순번", required = false)
  private String sort;

  @ApiModelProperty(value = "페이지코드", required = false)
  private String pageCd;

  @ApiModelProperty(value = "페이지명", required = false)
  private String pageNm;

  @ApiModelProperty(value = "상위페이지ID", required = false)
  private String prntsPageId;

  @ApiModelProperty(value = "페이지정보", required = false)
  private PageVo pageInfo;

  @ApiModelProperty(value = "페이지정보JsonString", required = false)
  private String pageInfoJsonString;

  @ApiModelProperty(value = "페이지리스트", required = false)
  private List<PageVo> pageList;


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전시인벤토리관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  @ApiModelProperty(value = "페이지검색유형", required = false)
  private String pageSchTp;

  @ApiModelProperty(value = "페이지유형", required = false)
  private String pageTp;

  @ApiModelProperty(value = "사용여부", required = false)
  private String useYn;

  @ApiModelProperty(value = "전시인벤토리ID", required = false)
  private String dpInventoryId;

  @ApiModelProperty(value = "인벤토리정보", required = false)
  private InventoryVo inventoryInfo;

  @ApiModelProperty(value = "인벤토리정보JsonString", required = false)
  private String inventoryInfoJsonString;

  @ApiModelProperty(value = "인벤토리리스트", required = false)
  private List<InventoryVo> inventoryList;

  @ApiModelProperty(value = "인벤토리리스트JsonString", required = false)
  private String inventoryListJsonString;

  @ApiModelProperty(value = "전시인벤토리그룹PK", required = false)
  private String dpInventoryGrpId;


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전시컨텐츠관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  @ApiModelProperty(value = "상위컨텐츠ID", required = false)
  private String prntsContsId;

  @ApiModelProperty(value = "전시컨텐츠ID", required = false)
  private String dpContsId;

  @ApiModelProperty(value = "컨텐츠유형", required = false)
  private String contsTp;

  @ApiModelProperty(value = "컨텐츠레벨", required = false)
  private int contsLevel;

  @ApiModelProperty(value = "전시범위", required = false)
  private String dpRangeTp;

  @ApiModelProperty(value = "진행상태", required = false)
  private String status;

  @ApiModelProperty(value = "전시시작일", required = false)
  private String dpStartDt;

  @ApiModelProperty(value = "전시종료일", required = false)
  private String dpEndDt;

  @ApiModelProperty(value = "전시노출조건유형", required = false)
  private String dpCondTp;

  @ApiModelProperty(value = "전시노출순서유형", required = false)
  private String dpSortTp;

  @ApiModelProperty(value = "컨텐츠리스트여부", required = false)
  private String contsListYn;

  @ApiModelProperty(value = "카테고리PK", required = false)
  private String ilCtgryId;

  @ApiModelProperty(value = "컨텐츠정보", required = false)
  private ContsVo contsInfo;

  @ApiModelProperty(value = "컨텐츠정보JsonString", required = false)
  private String contsInfoJsonString;

  @ApiModelProperty(value = "컨텐츠리스트", required = false)
  private List<ContsVo> contsList;

  @ApiModelProperty(value = "컨텐츠리스트JsonString", required = false)
  private String contsListJsonString;

  @ApiModelProperty(value = "검색키워드리스트", required = false)
  private List<String> findKeywordList;

  @ApiModelProperty(value = "검색키워드리스트String", required = false)
  private String findKeywordListString;

  @ApiModelProperty(value = "검색코드유형", required = false)
  private String searchCondition;

  @ApiModelProperty(value = "상품상세URL", required = false)
  private String goodsDetailUrl;

  @ApiModelProperty(value = "호스트URL", required = false)
  private String hostUrl;

  @ApiModelProperty(value = "페이지경로", required = false)
  private String pageFullPath;

  @ApiModelProperty(value = "엑셀다운여부", required = false)
  private String excelDownYn;

  @ApiModelProperty(value = "인벤토리명", required = false)
  private String inventoryNm;

  @ApiModelProperty(value = "인벤토코드", required = false)
  private String inventoryCd;


  // ==========================================================================
  // convert Data
  // ==========================================================================
  // 리스트정보 -> List 변환
  public void convertDataList() throws BaseException {

    // 인벤토리
    try {
      if (StringUtil.isNotEmpty(this.inventoryListJsonString)) {
        this.inventoryList  = BindUtil.convertJsonArrayToDtoList(this.inventoryListJsonString, InventoryVo.class);
      }
    }
    catch (Exception e) {
      throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_JSON);
    }

    // 컨텐츠
    try {
      if (StringUtil.isNotEmpty(this.contsListJsonString)) {
        this.contsList      = BindUtil.convertJsonArrayToDtoList(this.contsListJsonString, ContsVo.class);
      }
    }
    catch (Exception e) {
      throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_JSON);
    }
  }

  // 페이지정보 -> PageVo 변환
  public void convertPageDataObject() throws BaseException {

    // 페이지정보
    try {
      ObjectMapper objMqpper = new ObjectMapper();
      this.pageInfo = objMqpper.readValue(pageInfoJsonString, PageVo.class);
    }
    catch (Exception e) {
      throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_JSON);
    }

  }

  // 인벤토리정보 -> InventoryVo 변환
  public void convertInventoryDataObject() throws BaseException {

    // 인벤토리정보
    try {
      ObjectMapper objMqpper = new ObjectMapper();
      this.inventoryInfo = objMqpper.readValue(inventoryInfoJsonString, InventoryVo.class);
    }
    catch (Exception e) {
      throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_JSON);
    }

  }

  // 컨텐츠정보 -> ContsVo 변환
  public void convertContsDataObject() throws BaseException {

    // 컨텐츠정보
    try {
      ObjectMapper objMqpper = new ObjectMapper();
      this.contsInfo = objMqpper.readValue(contsInfoJsonString, ContsVo.class);
    }
    catch (Exception e) {
      log.debug("# contsInfo :: " + contsInfo.toString());
      throw new BaseException(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_JSON);
    }

  }




}
