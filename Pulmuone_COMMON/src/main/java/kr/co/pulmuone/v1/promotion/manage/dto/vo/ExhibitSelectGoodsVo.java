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
public class ExhibitSelectGoodsVo {

  public ExhibitSelectGoodsVo () {}

  public ExhibitSelectGoodsVo (Object userInfo) {
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


  @ApiModelProperty(value = "기획전 골라담기 상품 PK")
  private String evExhibitSelectGoodsId;


  @ApiModelProperty(value = "기획전 골라담기 추가상품 PK")
  private String evExhibitSelectAddGoodsId;


  @ApiModelProperty(value = "기획전 PK")
  private String evExhibitId;


  @ApiModelProperty(value = "상품 PK")
  private String ilGoodsId;


  @ApiModelProperty(value = "순서")
  private String goodsSort;

  // --------------------------------------------------------------------------
  // 추가항목
  // --------------------------------------------------------------------------
  @ApiModelProperty(value = "등록자")
  private String createId;

  @ApiModelProperty(value = "등록일")
  private String createDt;

  @ApiModelProperty(value = "수정자")
  private String modifyId;

  @ApiModelProperty(value = "수정일")
  private String modifyDt;

  @ApiModelProperty(value = "품목PK")
  private String itemCd;

  @ApiModelProperty(value = "상품명")
  private String goodsNm;

  @ApiModelProperty(value = "상품유형")
  private String goodsTp;

  @ApiModelProperty(value = "상품이미지")
  private String goodsImagePath;

  @ApiModelProperty(value = "출고처PK")
  private String urWarehouseId;

  @ApiModelProperty(value = "출고처명")
  private String warehouseNm;

  @ApiModelProperty(value = "배송비 템플릿 ID")
  private String ilShippingTmplId;

  @ApiModelProperty(value = "배송비 템플릿 명")
  private String shippingTemplateName;

  @ApiModelProperty(value = "대표상품 배송비 템플릿 ID")
  private String targetIlShippingTmplId;

  @ApiModelProperty(value = "대표상품 배송비 템플릿 명")
  private String targetShippingTemplateName;

  @ApiModelProperty(value = "상품유형명")
  private String goodsTpNm;

//  IL_GOODS_SHIPPING_TEMPLATE, IL_SHIPPING_TEMPLATE 테이블 참조
//
//  @ApiModelProperty(value = "배송정책")
//  private String urWarehouseNm               ;
//
//
//  @ApiModelProperty(value = "배송정책명")
//  private String urWarehouseNm               ;

  @ApiModelProperty(value = "원가")
  private int standardPrice;

  @ApiModelProperty(value = "정상가")
  private int recommendedPrice;

  @ApiModelProperty(value = "(원본)판매가")
  private int oriSalePrice;

  @ApiModelProperty(value = "추가상품판매가")
  private int salePrice;

  @ApiModelProperty(value = "배송불가지역")
  private String undeliverableAreaTp;

  @ApiModelProperty(value = "배송불가지역명")
  private String undeliverableAreaTpNm;

  @ApiModelProperty(value = "합배송여부")
  private String bundleYn;

  @ApiModelProperty(value = "배송유형")
  private String giftShippingTp;

}
