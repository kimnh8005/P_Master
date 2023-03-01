package kr.co.pulmuone.v1.goods.stock.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GoodsStockDisposalResultVo {

    //품목그룹의 랭크
    private int ranking;
    //공급업체
    private String supplierName;
    //품목코드
    private String ilItemCd;
    //품목바코드
    private String itemBarcode;
    //상품유형
    private String goodsTp;
    //상품코드
    private String ilGoodsId;
    //상품 판매상태
    private String saleStatus;
    //폐기임박상품코드
    private String disposalGoodsId;
    //폐기임박상품 판매상태
    private String disposalSaleStatus;
    //품목명
    private String itemNm;
    //임박/출고 기준
    private String stockTp;
    //유통기한
    private String expirationDt;
    //유통기한 잔여일
    private String leftDays;
    //폐기예정 전환 잔여일
    private String disposalLeftDays;
    //수량
    private String stockQty;

}
