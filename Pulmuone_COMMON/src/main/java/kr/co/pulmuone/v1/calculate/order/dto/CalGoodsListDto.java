package kr.co.pulmuone.v1.calculate.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 상품 정산 조회 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Setter
@Getter
@ToString
@ApiModel(description = "상품 정산 조회 Request Dto")
public class CalGoodsListDto {

    @ApiModelProperty(value = "상품정산구분")
    private String orderType;

    @ApiModelProperty(value = "주문경로(유형)")
    private String agentTypeCd;

    @ApiModelProperty(value = "정산처리일자")
    private String goodsSettleDt;

    @ApiModelProperty(value = "주문일자")
    private String goodsOrderDt;

    @ApiModelProperty(value = "결제일자/환불일자")
    private String paymentDt;

    @ApiModelProperty(value = "결제수단/환불수단")
    private String orderPaymentType;

    @ApiModelProperty(value = "판매처")
    private String sellersNm;

    @ApiModelProperty(value = "BOS 주문번호")
    private String odid;

    @ApiModelProperty(value = "주문 상세번호")
    private String odOrderDetlId;

    @ApiModelProperty(value = "공급업체 ID")
    private String urSupplierId;

    @ApiModelProperty(value = "출고처 명")
    private String warehouseName;

    @ApiModelProperty(value = "매장명")
    private String storeName;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "품목바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "상품코드")
    private String ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "상품유형")
    private String goodsTpCd;

    @ApiModelProperty(value = "수량")
    private Integer orderCnt;

    @ApiModelProperty(value = "정상가")
    private Long recommendedPrice;

    @ApiModelProperty(value = "즉시 할인금액")
    private double discountPrice;

    @ApiModelProperty(value = "판매가")
    private double salePrice;

    @ApiModelProperty(value = "상품쿠폰 할인금액")
    private double goodsCouponPrice;

    @ApiModelProperty(value = "장바구니쿠폰 할인금액")
    private double cartCouponPrice;

    @ApiModelProperty(value = "주문금액")
    private double orderPrice;

    @ApiModelProperty(value = "결제금액(배송비제외)")
    private double paidPriceGoods;

    @ApiModelProperty(value = "매출금액")
    private double taxablePrice;

    @ApiModelProperty(value = "매출금액(VAT제외)")
    private double nonTaxablePrice;

    @ApiModelProperty(value = "VAT금액")
    private double vat;

    @ApiModelProperty(value = "과세구분")
    private String taxYn;

    @ApiModelProperty(value = "통합 ERP I/F 여부")
    private String orderChangeYn;

    @ApiModelProperty(value = "외부몰 주문번호")
    private String outmallId;

    @ApiModelProperty(value = "외부몰 주문상세번호")
    private String outmallDetailId;

    @ApiModelProperty(value = "매출금액 합계")
    private double totalTaxablePrice;

    @ApiModelProperty(value = "예상상품매출(VAT포함)")
    private double totalSalePrice;

    @ApiModelProperty(value = "예상상품매출(VAT별도)")
    private double totalSaleNonTaxPrice;

    @ApiModelProperty(value = "즉시할인 금액")
    private double directPrice;

    @ApiModelProperty(value = "임직원 할인 금액")
    private double discountEmployeePrice;

    @ApiModelProperty(value = "PG")
    private String pgServiceNm;

    @ApiModelProperty(value = "가맹점코드")
    private String branchStoreId;

    @ApiModelProperty(value = "가맹점명")
    private String branchStoreNm;

    @ApiModelProperty(value = "주문자명")
    private String buyerNm;

}
