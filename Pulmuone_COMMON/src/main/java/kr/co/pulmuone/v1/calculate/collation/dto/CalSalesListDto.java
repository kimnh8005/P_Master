package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel(description = "통합몰 매출 대사 조회 Request Dto")
public class CalSalesListDto {

    @ApiModelProperty(value = "ERP 구분")
    private String erpSettleTypeNm;

    @ApiModelProperty(value = "ERP 주문번호")
    private String erpOdid;

    @ApiModelProperty(value = "ERP 주문 상세번호")
    private String erpOdOrderDetlSeq;

    @ApiModelProperty(value = "ERP 정산처리일자")
    private String erpSettleDt;

    @ApiModelProperty(value = "ERP 수량")
    private int settleItemCnt;

    @ApiModelProperty(value = "ERP 매출금액 (VAT 포함)")
    private Long settlePrice;

    @ApiModelProperty(value = "매출금액 (VAT 제외)")
    private Long vatRemoveSettlePrice;

    @ApiModelProperty(value = "VAT")
    private Long vat;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문 상세번호")
    private String odOrderDetlSeq;

    @ApiModelProperty(value = "판매처명")
    private String sellersNm;

    @ApiModelProperty(value = "출고처명")
    private String warehouseNm;

    @ApiModelProperty(value = "정산처리일자")
    private String settleDt;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "품목바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "수량")
    private int cnt;

    @ApiModelProperty(value = "주문상태")
    private String orderStatusCdNm;

    @ApiModelProperty(value = "정상가")
    private Long recommendedPrice;

    @ApiModelProperty(value = "즉시 할인금액")
    private Long directPrice;

    @ApiModelProperty(value = "즉시할인유형")
    private String directDiscountInfo;

    @ApiModelProperty(value = "판매가")
    private Long salePrice;

    @ApiModelProperty(value = "매출금액")
    private Long paidPrice;

    @ApiModelProperty(value = "통합몰 매출 대사 총 Count")
    private Long totalCnt;
}
