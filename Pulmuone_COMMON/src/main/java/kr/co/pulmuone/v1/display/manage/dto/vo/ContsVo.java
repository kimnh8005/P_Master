package kr.co.pulmuone.v1.display.manage.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContsVo {

  public ContsVo () {}

  public ContsVo (Object userInfo) {
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


  @ApiModelProperty(value = "전시컨텐츠PK")
  private String dpContsId                    ;


  @ApiModelProperty(value = "전시인벤토리PK")
  private String dpInventoryId                ;


  @ApiModelProperty(value = "카테고리PK")
  private String ilCtgryId                    ;


  @ApiModelProperty(value = "컨텐츠유형")
  private String contsTp                      ;


  @ApiModelProperty(value = "컨텐츠레벨")
  private int contsLevel                   ;


  @ApiModelProperty(value = "상위전시컨텐츠PK")
  private String prntsContsId                 ;


  @ApiModelProperty(value = "레벨1전시컨텐츠PK")
  private String level1ContsId                ;


  @ApiModelProperty(value = "레벨2전시컨텐츠PK")
  private String level2ContsId                ;


  @ApiModelProperty(value = "레벨3전시컨텐츠PK")
  private String level3ContsId                ;


  @ApiModelProperty(value = "전시시작일")
  private String dpStartDt                    ;


  @ApiModelProperty(value = "전시종료일")
  private String dpEndDt                    ;


  @ApiModelProperty(value = "전시범위")
  private String dpRangeTp                    ;


  @ApiModelProperty(value = "타이틀명")
  private String titleNm                      ;


  @ApiModelProperty(value = "노출카테고리ID")
  private String dpCtgryId                    ;


  @ApiModelProperty(value = "텍스트1")
  private String text1                           ;


  @ApiModelProperty(value = "텍스트1색상")
  private String text1Color                   ;


  @ApiModelProperty(value = "텍스트2")
  private String text2                           ;


  @ApiModelProperty(value = "텍스트2색상")
  private String text2Color                   ;


  @ApiModelProperty(value = "텍스트3")
  private String text3                           ;


  @ApiModelProperty(value = "텍스트3색상")
  private String text3Color                   ;


  @ApiModelProperty(value = "PC링크URL")
  private String linkUrlPc                    ;


  @ApiModelProperty(value = "모바일링크URL")
  private String linkUrlMobile                ;


  @ApiModelProperty(value = "PC HTML")
  private String htmlPc                       ;


  @ApiModelProperty(value = "모바일 HTML")
  private String htmlMobile                   ;


  @ApiModelProperty(value = "PC이미지경로")
  private String imgPathPc                    ;


  @ApiModelProperty(value = "PC이미지원본파일명")
  private String imgOriginNmPc                ;


  @ApiModelProperty(value = "모바일이미지경로")
  private String imgPathMobile                ;


  @ApiModelProperty(value = "모바일이미지원본파일명")
  private String imgOriginNmMobile            ;


  @ApiModelProperty(value = "PC GIF이미지경로")
  private String gifImgPathPc                 ;


  @ApiModelProperty(value = "PC GIF이미지원본파일명")
  private String gifImgOriginNmPc             ;


  @ApiModelProperty(value = "모바일GIF이미지경로")
  private String gifImgPathMobile             ;


  @ApiModelProperty(value = "모바일GIF이미지원본파일명")
  private String gifImgOriginNmMobile         ;


  @ApiModelProperty(value = "컨텐츠ID")
  private String contsId                      ;


  @ApiModelProperty(value = "전시노출조건유형")
  private String dpCondTp                     ;


  @ApiModelProperty(value = "전시노출순서유형")
  private String dpSortTp                     ;


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

  @ApiModelProperty(value = "컨텐츠유형명")
  private String contsTpNm                    ;


  @ApiModelProperty(value = "컨텐츠레벨명")
  private String contsLevelNm                 ;


  @ApiModelProperty(value = "전시범위명")
  private String dpRangeTpNm                  ;


  @ApiModelProperty(value = "전시노출조건유형명")
  private String dpCondTpNm                  ;


  @ApiModelProperty(value = "전시노출순서유형명")
  private String dpSortTpNm                  ;


  @ApiModelProperty(value = "사용여부명")
  private String useYnNm                      ;


  @ApiModelProperty(value = "진행상태")
  private String status                       ;


  @ApiModelProperty(value = "진행상태명")
  private String statusNm                     ;


  @ApiModelProperty(value = "카테고리전체명")
  private String ctgryFullNm                  ;


  @ApiModelProperty(value = "몰구분")
  private String mallDiv                      ;


  @ApiModelProperty(value = "몰구분명")
  private String mallDivNm                    ;


  @ApiModelProperty(value = "컨텐츠명")
  private String contsNm                      ;


  @ApiModelProperty(value = "컨텐츠레벨1상세내용")
  private String contsLevel1Desc              ;


  @ApiModelProperty(value = "컨텐츠레벨2상세내용")
  private String contsLevel2Desc              ;


  @ApiModelProperty(value = "컨텐츠레벨3상세내용")
  private String contsLevel3Desc              ;


  @ApiModelProperty(value = "카테고리Depth")
  private int ctgryDepth                      ;


  @ApiModelProperty(value = "카테고리Depth0ID")
  private String ctgryIdDepth0                ;


  @ApiModelProperty(value = "카테고리Depth1ID")
  private String ctgryIdDepth1                ;


  @ApiModelProperty(value = "카테고리Depth2ID")
  private String ctgryIdDepth2                ;


  @ApiModelProperty(value = "카테고리Depth3ID")
  private String ctgryIdDepth3                ;


  @ApiModelProperty(value = "카테고리Depth4ID")
  private String ctgryIdDepth4                ;


  @ApiModelProperty(value = "카테고리Depth5ID")
  private String ctgryIdDepth5                ;


  @ApiModelProperty(value = "상품기본카테고리ID")
  private String goodsBasicCtgryId            ;


  @ApiModelProperty(value = "상품기본카테고리명")
  private String goodsBasicCtgryFullNm        ;


  @ApiModelProperty(value = "상품브랜드ID")
  private String goodsBrandId                 ;


  @ApiModelProperty(value = "상품브랜드명")
  private String goodsBrandNm                 ;


  @ApiModelProperty(value = "호스트URL")
  private String hostUrl                      ;


  @ApiModelProperty(value = "상품상세화면URL")
  private String goodsDetailUrl               ;


  @ApiModelProperty(value = "노출텍스트1내용")
  private String text1String               ;


  @ApiModelProperty(value = "노출텍스트2내용")
  private String text2String               ;


  @ApiModelProperty(value = "노출텍스트3내용")
  private String text3String               ;

  // --------------------------------------------------------------------------
  // Goods
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "상품 ID")
  private Long goodsId                        ;


  @ApiModelProperty(value = "품목코드")
  private String itemCode;


  @ApiModelProperty(value = "품목바코드")
  private String itemBarcode;


  @ApiModelProperty(value = "프로모션명")
  private String promotionName;


  @ApiModelProperty(value = "상품명")
  private String goodsNm;


  @ApiModelProperty(value = "상품등록일")
  private String goodsCreateDate;


  @ApiModelProperty(value = "상품수정일")
  private String goodsModifyDate;


  @ApiModelProperty(value = "프로모션 시작일")
  private String promotionStartDate;


  @ApiModelProperty(value = "프로모션 종료일")
  private String promotionEndDate;


  @ApiModelProperty(value = "상품유형코드")
  private String goodsTypeCode;


  @ApiModelProperty(value = "상품유형명")
  private String goodsTypeName;


  @ApiModelProperty(value = "회원 구매여부")
  private String purchaseMemberYn;


  @ApiModelProperty(value = "임직원 구매여부")
  private String purchaseEmployeeYn;


  @ApiModelProperty(value = "비회원 구매여부")
  private String purchaseNonmemberYn;


  @ApiModelProperty(value = "WEB PC 전시여부")
  private String displayWebPcYn;


  @ApiModelProperty(value = "WEB MOBILE 전시여부")
  private String displayWebMobileYn;


  @ApiModelProperty(value = "APP 전시여부")
  private String displayAppYn;


  @ApiModelProperty(value = "판매유형코드")
  private String saleTypeCode;


  @ApiModelProperty(value = "판매유형명")
  private String saleTypeName;


  @ApiModelProperty(value = "승인상태코드")
  private String approvalStatusCode;


  @ApiModelProperty(value = "승인상태명")
  private String approvalStatusName;


  @ApiModelProperty(value = "판매상태코드")
  private String saleStatus;


  @ApiModelProperty(value = "판매상태명")
  private String saleStatusName;


  @ApiModelProperty(value = "전시유무")
  private String displayYn;


  @ApiModelProperty(value = "판매시작일")
  private String saleStartDate;


  @ApiModelProperty(value = "판매종료일")
  private String saleEndDate;


  @ApiModelProperty(value = "표준카테고리 ID")
  private Long categoryStandardId;


  @ApiModelProperty(value = "표준카테고리명")
  private String categoryStandardDepthName;


  @ApiModelProperty(value = "전시카테고리 ID")
  private Long categoryId;


  @ApiModelProperty(value = "전시카테고리명")
  private String categoryDepthName;


  @ApiModelProperty(value = "출고처 ID")
  private Long warehouseId;


  @ApiModelProperty(value = "출고처명")
  private String warehouseName;


  @ApiModelProperty(value = "공급처 ID")
  private Long supplierId;


  @ApiModelProperty(value = "공급처 회사 ID")
  private Long supplierCompanyId;


  @ApiModelProperty(value = "공급처명")
  private String supplierName;


  @ApiModelProperty(value = "브랜드 ID")
  private Long brandId;


  @ApiModelProperty(value = "브랜드명")
  private String brandName;


  @ApiModelProperty(value = "보관방법코드")
  private String storageMethodTypeCode;


  @ApiModelProperty(value = "보관방법명")
  private String storageMethodTypeName;


  @ApiModelProperty(value = "재고연동유무")
  private String erpStockIfYn;


  @ApiModelProperty(value = "추가상품 유무")
  private String additionalGoodsYn;


  @ApiModelProperty(value = "쿠폰사용유무")
  private String couponUseYn;


  @ApiModelProperty(value = "MD추천유무")
  private String mdRecommendYn;


  @ApiModelProperty(value = "상품가격 ID")
  private Long goodsPriceId;


  @ApiModelProperty(value = "품목가격 ID")
  private Long itemPriceId;


  @ApiModelProperty(value = "상품할인 ID")
  private Long goodsDiscountId;


  @ApiModelProperty(value = "원가")
  private int standardPrice;


  @ApiModelProperty(value = "정상가")
  private int recommendedPrice;


  @ApiModelProperty(value = "판매가")
  private int salePrice;


  @ApiModelProperty(value = "할인유형코드")
  private String discountTypeCode;


  @ApiModelProperty(value = "할인유형명")
  private String discountTypeName;


  @ApiModelProperty(value = "가격 시작일")
  private String priceStartDate;


  @ApiModelProperty(value = "가격 종료일")
  private String priceEndDate;


  @ApiModelProperty(value = "정상재고갯수")
  private int normalStockCount;


  @ApiModelProperty(value = "임박재고갯수")
  private int imminentStockCount;


  @ApiModelProperty(value = "폐기임박갯수")
  private int disposalStockCount;


  @ApiModelProperty(value = "상품이미지경로")
  private String goodsImagePath;



  @ApiModelProperty(value = "상품PK")
  private String ilGoodsId            ;


  @ApiModelProperty(value = "품목PK")
  private String ilItemCd             ;


  @ApiModelProperty(value = "상품유형")
  private String goodsTp              ;


  @ApiModelProperty(value = "상품유형명")
  private String goodsTpNm            ;


  @ApiModelProperty(value = "공급사명")
  private String supplierNm           ;


  @ApiModelProperty(value = "PC웹노출여부")
  private String dispWebPcYn          ;


  @ApiModelProperty(value = "모바일웹노출여부")
  private String dispWebMobileYn      ;


  @ApiModelProperty(value = "APP노출여부")
  private String dispAppYn            ;


  @ApiModelProperty(value = "PC웹노출여부명")
  private String dispWebPcYnNm        ;


  @ApiModelProperty(value = "모바일웹노출여부명")
  private String dispWebMobileYnNm    ;


  @ApiModelProperty(value = "APP노출여부명")
  private String dispAppYnNm          ;


  @ApiModelProperty(value = "회원구매여부명")
  private String purchaseMemberYnNm   ;


  @ApiModelProperty(value = "임직원구매여부명")
  private String purchaseEmployeeYnNm ;


  @ApiModelProperty(value = "비회원구매여부명")
  private String purchaseNonmemberYnNm;


  @ApiModelProperty(value = "판매허용범위")
  private String dispYnRange;


  @ApiModelProperty(value = "구매허용범위")
  private String purchaseYnRange;


  @ApiModelProperty(value = "전시여부")
  private String dispYn;


  @ApiModelProperty(value = "전시여부명")
  private String dispYnNm;


  @ApiModelProperty(value = "판매허용범위명")
  private String dispRangeNm;


  @ApiModelProperty(value = "구매허용범위명")
  private String purchaseRangeNm;


  // --------------------------------------------------------------------------
  // Brand
  // --------------------------------------------------------------------------

  @ApiModelProperty(value = "브랜드PK")
  private String dpBrandId                    ;


  @ApiModelProperty(value = "공급처PK")
  private String urSupplierId                 ;


  @ApiModelProperty(value = "브랜드명")
  private String dpBrandNm                    ;


  @ApiModelProperty(value = "코드")
  private String code                         ;


  @ApiModelProperty(value = "코드명")
  private String name                         ;


}
