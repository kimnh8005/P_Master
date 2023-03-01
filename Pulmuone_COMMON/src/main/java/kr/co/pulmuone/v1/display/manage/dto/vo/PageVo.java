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
public class PageVo {

  public PageVo () {}

  public PageVo (Object userInfo) {
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


  @ApiModelProperty(value = "전시페이지PK")
  private String dpPageId                   ;


  @ApiModelProperty(value = "페이지코드")
  private String pageCd                     ;


  @ApiModelProperty(value = "페이지명")
  private String pageNm                     ;


  @ApiModelProperty(value = "깊이")
  private int depth                         ;


  @ApiModelProperty(value = "상위전시페이지PK")
  private String prntsPageId                ;


  @ApiModelProperty(value = "깊이1전시페이지PK")
  private String depth1PageId               ;


  @ApiModelProperty(value = "깊이2전시페이지PK")
  private String depth2PageId               ;


  @ApiModelProperty(value = "깊이3전시페이지PK")
  private String depth3PageId               ;


  @ApiModelProperty(value = "깊이4전시페이지PK")
  private String depth4PageId               ;


  @ApiModelProperty(value = "깊이5전시페이지PK")
  private String depth5PageId               ;


  @ApiModelProperty(value = "노출순서")
  private int sort                          ;


  @ApiModelProperty(value = "사용여부")
  private String useYn                      ;


  @ApiModelProperty(value = "삭제유무")
  private String delYn                      ;


  @ApiModelProperty(value = "등록자")
  private String createId                   ;


  @ApiModelProperty(value = "등록일")
  private String createDt                   ;


  @ApiModelProperty(value = "수정자")
  private String modifyId                   ;


  @ApiModelProperty(value = "수정일")
  private String modifyDt                   ;


  @ApiModelProperty(value = "하위존재여부")
  private String isleaf                     ;


  @ApiModelProperty(value = "사용여부명")
  private String useYnNm                    ;


  @ApiModelProperty(value = "페이지경로")
  private String pageFullPath               ;


  @ApiModelProperty(value = "그룹설명")
  private String groupDesc                  ;

}
