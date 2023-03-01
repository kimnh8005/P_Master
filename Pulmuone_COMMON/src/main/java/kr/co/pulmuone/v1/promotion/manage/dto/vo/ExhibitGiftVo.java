package kr.co.pulmuone.v1.promotion.manage.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExhibitGiftVo {

  public ExhibitGiftVo () {}

  public ExhibitGiftVo (Object userInfo) {
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


  @ApiModelProperty(value = "기획전 증정행사 상세 PK")
  private String evExhibitGiftId;


  @ApiModelProperty(value = "기획전 증정행사 상품 PK")
  private String evExhibitGiftGoodsId;


  @ApiModelProperty(value = "기획전 증정행사 적용대상상품 PK")
  private String evExhibitGiftTargetGoodsId;


  @ApiModelProperty(value = "기획전 증정행사 적용대상브랜드 PK")
  private String evExhibitGiftTargetBrandId;


  @ApiModelProperty(value = "기획전 PK")
  private String evExhibitId;


  @ApiModelProperty(value = "기획전결제상태")
  private String exhibitStatus;


  @ApiModelProperty(value = "기획전전시여부")
  private String exhibitDispYn;


  @ApiModelProperty(value = "증정수량")
  private int giftCnt;


  @ApiModelProperty(value = "대표상품여부")
  private String repGoodsYn;


  @ApiModelProperty(value = "증정조건 유형")
  private String giftTp;


  @ApiModelProperty(value = "장바구니별 제한금액")
  private String overPrice;


  @ApiModelProperty(value = "증정범위 유형")
  private String giftRangeTp;


  @ApiModelProperty(value = "지급방식(증정방식) 유형")
  private String giftGiveTp;


  @ApiModelProperty(value = "증정배송 유형")
  private String giftShippingTp;


  @ApiModelProperty(value = "출고처 PK")
  private String urWarehouseId;


  @ApiModelProperty(value = "적용대상유형")
  private String giftTargetTp;


  @ApiModelProperty(value = "적용대상상품유형")
  private String giftTargetGoodsTp;


  @ApiModelProperty(value = "적용대상브랜드유형")
  private String giftTargetBrandTp;


  @ApiModelProperty(value = "상품 PK")
  private String ilGoodsId;


  @ApiModelProperty(value = "상품순서")
  private int goodsSort;


  @ApiModelProperty(value = "상품이미지")
  private String goodsImagePath;


  @ApiModelProperty(value = "표준브랜드 PK")
  private String urBrandId;


  @ApiModelProperty(value = "전시브랜드 PK")
  private String dpBrandId;


  @ApiModelProperty(value = "카테고리 PK")
  private String ilCtgryId;


  @ApiModelProperty(value = "원가")
  private int standardPrice;


  @ApiModelProperty(value = "정상가")
  private int recommendedPrice;


  @ApiModelProperty(value = "판매가")
  private int salePrice;


  @ApiModelProperty(value = "출고처매장여부")
  private String storeYn;


  @ApiModelProperty(value = "배송불가지역유형")
  private String undeliverableAreaTp;


  @ApiModelProperty(value = "상품배송정책 PK")
  private String ilGoodsShippingTemplateId;


  @ApiModelProperty(value = "배송정책 PK")
  private String ilShippingTmplId;


  @ApiModelProperty(value = "원본 IL_SHIPPING_TEMPLATE PK")
  private String origIlShippingTmplId;


  @ApiModelProperty(value = "선착불구분")
  private String paymentMethodTp;


  @ApiModelProperty(value = "브랜드 PK")
  private String brandId;


  @ApiModelProperty(value = "브랜드명")
  private String brandNm;


  @ApiModelProperty(value = "배송정책명")
  private String shippingTmplNm;


  @ApiModelProperty(value = "출고처명")
  private String warehouseNm;


  @ApiModelProperty(value = "승안상태명")
  private String exhibitStatusNm;


  @ApiModelProperty(value = "증정조건 유형명")
  private String giftTpNm;


  @ApiModelProperty(value = "증정범위 유형명")
  private String giftRangeTpNm;


  @ApiModelProperty(value = "지급방식(증정방식) 유형명")
  private String giftGiveTpNm;


  @ApiModelProperty(value = "증정배송 유형명")
  private String giftShippingTpNm;


  @ApiModelProperty(value = "증정대상유형명")
  private String giftTargetTpNm;


  @ApiModelProperty(value = "증정대상상품유형명")
  private String giftTargetGoodsTpNm;


  @ApiModelProperty(value = "증정대상브랜드유형명")
  private String giftTargetBrandTpNm;


  @ApiModelProperty(value = "표준브랜드명")
  private String urBrandNm;


  @ApiModelProperty(value = "전시브랜드명")
  private String dpBrandNm;


  @ApiModelProperty(value = "카테고리전체명")
  private String ctgryFullNm;


  @ApiModelProperty(value = "상품명")
  private String goodsNm;


  @ApiModelProperty(value = "배송불가지역유형명")
  private String undeliverableAreaTpNm;


  @ApiModelProperty(value = "등록자")
  private String createId                     ;


  @ApiModelProperty(value = "등록일")
  private String createDt                     ;


  @ApiModelProperty(value = "수정자")
  private String modifyId                     ;


  @ApiModelProperty(value = "수정일")
  private String modifyDt                     ;

  // --------------------------------------------------------------------------
  // List
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "증정상품리스트")
  private List<ExhibitGiftGoodsVo> giftGoodsList;


  @ApiModelProperty(value = "증정대상상품리스트")
  private List<ExhibitGiftGoodsVo> giftTargetGoodsList;


  @ApiModelProperty(value = "증정대상브랜드리스트")
  private List<ExhibitGiftGoodsVo> giftTargetBrandList;



}
