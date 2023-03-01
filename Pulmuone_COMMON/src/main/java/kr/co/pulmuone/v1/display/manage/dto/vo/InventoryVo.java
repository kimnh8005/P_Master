package kr.co.pulmuone.v1.display.manage.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
//import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InventoryVo {

  public InventoryVo () {}

  public InventoryVo (Object userInfo) {
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


  @ApiModelProperty(value = "전시인벤토리PK")
  private String dpInventoryId                ;


  @ApiModelProperty(value = "페이지유형")
  private String pageTp                       ;


  @ApiModelProperty(value = "인벤토리코드")
  private String inventoryCd                  ;


  @ApiModelProperty(value = "인벤토리명")
  private String inventoryNm                  ;


  @ApiModelProperty(value = "전시페이지PK")
  private String dpPageId                     ;


  @ApiModelProperty(value = "몰구분")
  private String mallDiv                      ;


  @ApiModelProperty(value = "카테고리깊이")
  private int ctgryDepth                      ;


  @ApiModelProperty(value = "전시범위")
  private String dpRangeTp                    ;


  @ApiModelProperty(value = "컨텐츠레벨1유형")
  private String contsLevel1Tp                ;


  @ApiModelProperty(value = "컨텐츠레벨1상세정보")
  private String contsLevel1Desc              ;


  @ApiModelProperty(value = "컨텐츠레벨2유형")
  private String contsLevel2Tp                ;


  @ApiModelProperty(value = "컨텐츠레벨2상세정보")
  private String contsLevel2Desc              ;


  @ApiModelProperty(value = "컨텐츠레벨3유형")
  private String contsLevel3Tp                ;


  @ApiModelProperty(value = "컨텐츠레벨3상세정보")
  private String contsLevel3Desc              ;


  @ApiModelProperty(value = "노출순서")
  private int sort                            ;


  @ApiModelProperty(value = "사용여부")
  private String useYn                        ;


  @ApiModelProperty(value = "삭제유무")
  private String delYn                        ;


  @ApiModelProperty(value = "등록자")
  private String createId                     ;


  @ApiModelProperty(value = "등록일")
  private String createDt                     ;


  @ApiModelProperty(value = "수정자")
  private String modifyId                     ;


  @ApiModelProperty(value = "수정일")
  private String modifyDt                     ;

  // --------------------------------------------------------------------------
  // 추가항목
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "전시범위명")
  private String dpRangeTpNm                  ;


  @ApiModelProperty(value = "컨텐츠레벨1유형명")
  private String contsLevel1TpNm              ;


  @ApiModelProperty(value = "컨텐츠레벨2유형명")
  private String contsLevel2TpNm              ;


  @ApiModelProperty(value = "컨텐츠레벨3유형명")
  private String contsLevel3TpNm              ;


  @ApiModelProperty(value = "사용여부명")
  private String useYnNm                      ;


  @ApiModelProperty(value = "전시인벤토리그룹구성PK")
  private String dpInventoryGrpMappingId      ;


  @ApiModelProperty(value = "전시인벤토리그룹PK")
  private String dpInventoryGrpId             ;


  @ApiModelProperty(value = "인벤토리노출순서")
  private int iSort                           ;


  @ApiModelProperty(value = "카테고리PK")
  private String ilCtgryId                    ;


  @ApiModelProperty(value = "인벤토리코드목록String")
  private String inventoryCdsString           ;


  @ApiModelProperty(value = "인벤토리그룹명")
  private String inventoryGrpNm               ;


  @ApiModelProperty(value = "그룹설명")
  private String groupDesc                    ;


  @ApiModelProperty(value = "페이지명")
  private String pageNm                       ;


  @ApiModelProperty(value = "페이지경로")
  private String pageFullPath                 ;


  @ApiModelProperty(value = "깊이")
  private String depth                        ;

  @ApiModelProperty(value = "엑셀노출용인벤토리코드")
  private String excelDispinventoryCd         ;


}
