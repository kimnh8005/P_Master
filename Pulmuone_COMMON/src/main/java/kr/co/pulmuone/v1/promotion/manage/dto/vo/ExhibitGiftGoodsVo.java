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
public class ExhibitGiftGoodsVo {

  public ExhibitGiftGoodsVo () {}

  public ExhibitGiftGoodsVo (Object userInfo) {
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


  @ApiModelProperty(value = "기획전 증정행사 상품 PK")
  private String evExhibitGiftGoodsId;


  @ApiModelProperty(value = "기획전 증정대상 상품 PK")
  private String evExhibitGiftTargetGoodsId;


  @ApiModelProperty(value = "기획전 증정대상 브랜드 PK")
  private String evExhibitGiftTargetBrandId;


  @ApiModelProperty(value = "기획전 PK")
  private String evExhibitId;


  @ApiModelProperty(value = "상품 PK")
  private String ilGoodsId;


  @ApiModelProperty(value = "순번")
  private int goodsSort;


  @ApiModelProperty(value = "지급수량")
  private int giftCnt;


  @ApiModelProperty(value = "대표상품여부")
  private String repGoodsYn;


  @ApiModelProperty(value = "구대표상품여부")
  private String oldRepGoodsYn;

//
//  @ApiModelProperty(value = "증정재고")
//  private String stockCnt;


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
  private String urWarehouseNm;


  @ApiModelProperty(value = "출고처명")
  private String warehouseNm;

//  IL_GOODS_SHIPPING_TEMPLATE, IL_SHIPPING_TEMPLATE 테이블 참조
//
//  @ApiModelProperty(value = "배송정책")
//  private String urWarehouseNm;
//
//
//  @ApiModelProperty(value = "배송정책명")
//  private String urWarehouseNm;


  @ApiModelProperty(value = "원가")
  private int standardPrice;


  @ApiModelProperty(value = "정상가")
  private int recommendedPrice;


  @ApiModelProperty(value = "판매가")
  private int salePrice;


  @ApiModelProperty(value = "브랜드PK")
  private String brandId;


  @ApiModelProperty(value = "브랜드명")
  private String brandNm;


  @ApiModelProperty(value = "카테고리PK")
  private String ilCtgryId;


  @ApiModelProperty(value = "카테고리전체명")
  private String ctgryFullNm;

  //
  //@ApiModelProperty(value = "표준브랜드PK")
  //private int urBrandId;
  //
  //
  //@ApiModelProperty(value = "표준브랜드명")
  //private int urBrandNm;
  //
  //
  //@ApiModelProperty(value = "전시브랜드PK")
  //private int dpBrandId;
  //
  //
  //@ApiModelProperty(value = "전시브랜드명")
  //private int dpBrandNm;


  @ApiModelProperty(value = "적용대상브랜드유형")
  private String giftTargetBrandTp;


  @ApiModelProperty(value = "적용대상브랜드유형")
  private String giftTargetBrandTpNm;


  @ApiModelProperty(value = "출고처매장여부")
  private String storeYn;


  @ApiModelProperty(value = "상품배송정책 PK")
  private String ilGoodsShippingTemplateId;


  @ApiModelProperty(value = "배송정책 PK")
  private String ilShippingTmplId;


  @ApiModelProperty(value = "원본 IL_SHIPPING_TEMPLATE PK")
  private String origIlShippingTmplId;


  @ApiModelProperty(value = "선착불구분")
  private String paymentMethodTp;


  @ApiModelProperty(value = "배송정책명")
  private String shippingTmplNm;


  @ApiModelProperty(value = "배송불가지역유형")
  private String undeliverableAreaTp;


  @ApiModelProperty(value = "배송불가지역유형명")
  private String undeliverableAreaTpNm;


  @ApiModelProperty(value = "증정배송 유형")
  private String giftShippingTp;


  @ApiModelProperty(value = "대상가능여부명")
  private String targetEnableYnNm;


  @ApiModelProperty(value = "대상가능여부명")
  private int no;


  @ApiModelProperty(value = "표준브랜드명")
  private String urBrandNm;


  @ApiModelProperty(value = "전시브랜드명")
  private String dpBrandNm;

}

