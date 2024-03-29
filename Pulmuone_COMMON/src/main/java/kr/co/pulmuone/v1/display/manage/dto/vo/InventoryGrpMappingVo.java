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
public class InventoryGrpMappingVo {

  public InventoryGrpMappingVo () {}

  public InventoryGrpMappingVo (Object userInfo) {
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


  @ApiModelProperty(value = "전시인벤토리그룹PK")
  private String dpInventoryGrpMappingId      ;


  @ApiModelProperty(value = "전시인벤토리그룹PK")
  private String dpInventoryGrpId             ;


  @ApiModelProperty(value = "전시인벤토리PK")
  private String dpInventoryId                ;


  @ApiModelProperty(value = "카테고리PK")
  private String ilCtgryId                    ;


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


  @ApiModelProperty(value = "사용여부명")
  private String useYnNm                      ;


  @ApiModelProperty(value = "인벤토리코드")
  private String inventoryCd                  ;


}
