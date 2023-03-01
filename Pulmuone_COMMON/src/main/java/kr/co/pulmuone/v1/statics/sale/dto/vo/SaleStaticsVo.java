package kr.co.pulmuone.v1.statics.sale.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SaleStaticsVo {

  @ApiModelProperty(value = "공급업체ID")
  private String urSupplierId;

  @ApiModelProperty(value = "공급업체명")
  private String supplierNm;

  @ApiModelProperty(value = "판매처그룹ID")
  private String sellersGroupCd;

  @ApiModelProperty(value = "판매처그룹명")
  private String sellersGroupCdNm;

  @ApiModelProperty(value = "판매처ID")
  private String omSellersId;

  @ApiModelProperty(value = "판매처명")
  private String sellersNm;

  @ApiModelProperty(value = "회원유형")
  private String buyerTypeNm;

  @ApiModelProperty(value = "기준기간-매출금액(VAT포함)")
  private long standardPaidPrice;

  @ApiModelProperty(value = "기준기간-매출금액(VAT별도)")
  private long standardPaidPriceNonTax;

  @ApiModelProperty(value = "기준기간-판매금액(VAT포함)-엑셀용")
  private String standardPaidPriceFm;

  @ApiModelProperty(value = "기준기간-판매금액(VAT별도)-엑셀용")
  private String standardPaidPriceNonTaxFm;

  @ApiModelProperty(value = "기준기간-주문건수")
  private long standardOrderCnt;

  @ApiModelProperty(value = "기준기간-주문건수-엑셀용")
  private String standardOrderCntFm;

  @ApiModelProperty(value = "기준기간-주문상품수량")
  private long standardGoodsCnt;

  @ApiModelProperty(value = "기준기간-주문상품수량-엑셀용")
  private String standardGoodsCntFm;

  @ApiModelProperty(value = "대비기간-주문건수")
  private long contrastOrderCnt;

  @ApiModelProperty(value = "대비기간-주문건수-엑셀용")
  private String contrastOrderCntFm;

  @ApiModelProperty(value = "대비기간-주문상품수량")
  private long contrastGoodsCnt;

  @ApiModelProperty(value = "대비기간-주문상품수량-엑셀용")
  private String contrastGoodsCntFm;

  @ApiModelProperty(value = "대비기간-매출금액(VAT포함)")
  private long contrastPaidPrice;

  @ApiModelProperty(value = "대비기간-매출금액(VAT별도)")
  private long contrastPaidPriceNonTax;

  @ApiModelProperty(value = "대비기간-판매금액(VAT포함)-엑셀용")
  private String contrastPaidPriceFm;

  @ApiModelProperty(value = "대비기간-판매금액(VAT별도)-엑셀용")
  private String contrastPaidPriceNonTaxFm;

  @ApiModelProperty(value = "판매금액신장률")
  private long stretchRate;

  @ApiModelProperty(value = "판매금액신장률-엑셀용")
  private String stretchRateFm;

  @ApiModelProperty(value = "표준카테고리명")
  private String ctgryStdNm;

  @ApiModelProperty(value = "전시카테고리명")
  private String ctgryNm;

  @ApiModelProperty(value = "카테고리명-엑셀용")
  private String ctgryNmEx;

  @ApiModelProperty(value = "상품코드")
  private String ilGoodsId;

  @ApiModelProperty(value = "상품명")
  private String goodsNm;

  @ApiModelProperty(value = "과세구분")
  private String taxTypeNm;

  @ApiModelProperty(value = "정상가")
  private long recommendedPrice;

  @ApiModelProperty(value = "정상가-엑셀용")
  private String recommendedPriceFm;

  @ApiModelProperty(value = "판매가")
  private long salePrice;

  @ApiModelProperty(value = "판매가-엑셀용")
  private String salePriceFm;

  @ApiModelProperty(value = "주문상품수량")
  private long goodsCnt;

  @ApiModelProperty(value = "주문상품수량-엑셀용")
  private String goodsCntFm;

  @ApiModelProperty(value = "VAT포함총판매금액")
  private long taxPaidPrice;

  @ApiModelProperty(value = "VAT포함총판매금액-엑셀용")
  private String taxPaidPriceFm;

  @ApiModelProperty(value = "VAT제외총판매금액")
  private long nonTaxPaidPrice;

  @ApiModelProperty(value = "VAT제외총판매금액-엑셀용")
  private String nonTaxPaidPriceFm;

  @ApiModelProperty(value = "품목코드")
  private String ilItemCd;

  @ApiModelProperty(value = "품목바코드")
  private String itemBarcode;

  @ApiModelProperty(value = "순번")
  private String no;

  @ApiModelProperty(value = "카테고리1Dept")
  private String ctgryNmEx1;

  @ApiModelProperty(value = "카테고리2Dept")
  private String ctgryNmEx2;

  @ApiModelProperty(value = "카테고리3Dept")
  private String ctgryNmEx3;

  @ApiModelProperty(value = "카테고리4Dept")
  private String ctgryNmEx4;


}
