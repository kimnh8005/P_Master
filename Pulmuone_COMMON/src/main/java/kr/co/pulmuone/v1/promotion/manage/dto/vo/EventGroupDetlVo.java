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
public class EventGroupDetlVo {

  public EventGroupDetlVo() {}

  public EventGroupDetlVo(Object userInfo) {
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


  @ApiModelProperty(value = "이벤트 전시그룹 상세 PK")
  private String evEventGroupDetlId;


  @ApiModelProperty(value = "이벤트 전시그룹 PK")
  private String evEventGroupId;


  @ApiModelProperty(value = "순서")
  private int goodsSort;


  @ApiModelProperty(value = "상품 PK")
  private String ilGoodsId;

  // --------------------------------------------------------------------------
  // 추가
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "등록자")
  private String createId                     ;


  @ApiModelProperty(value = "등록일")
  private String createDt                     ;


  @ApiModelProperty(value = "수정자")
  private String modifyId                     ;


  @ApiModelProperty(value = "수정일")
  private String modifyDt                     ;




  @ApiModelProperty(value = "품목PK")
  private String itemCd                       ;


  @ApiModelProperty(value = "상품명")
  private String goodsNm                      ;


  @ApiModelProperty(value = "상품유형")
  private String goodsTp                      ;


  @ApiModelProperty(value = "원가")
  private int standardPrice                   ;


  @ApiModelProperty(value = "정상가")
  private int recommendedPrice                ;


  @ApiModelProperty(value = "판매가")
  private int salePrice                       ;


  @ApiModelProperty(value = "할인유형")
  private String discountTp                   ;


  @ApiModelProperty(value = "상품이미지")
  private String goodsImagePath               ;

  // 일괄업로드엑셀용

  @ApiModelProperty(value = "그룹순번")
  private int groupSort                       ;





}
