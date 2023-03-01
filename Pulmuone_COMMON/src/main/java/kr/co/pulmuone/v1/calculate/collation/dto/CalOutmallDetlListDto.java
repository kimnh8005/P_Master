package kr.co.pulmuone.v1.calculate.collation.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel(description = "외부몰 주문 대사 상세내역 조회 Dto")
public class CalOutmallDetlListDto {

	@ApiModelProperty(value = "판매처")
    private String uploadSellersNm;

    @ApiModelProperty(value = "외부몰 주문번호")
    private String uploadOutmallDetailId;

    @ApiModelProperty(value = "매출금액")
    private Long orderAmt;

    @ApiModelProperty(value = "대비금액")
    private Long contrastAmt;

    @ApiModelProperty(value = "최종매출금액")
    private Long settlePrice;

    @ApiModelProperty(value = "공급업체명")
    private String supplierNm;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문상세번호")
    private String odOrderDetlId;

    @ApiModelProperty(value = "판매처명")
    private String sellersNm;

    @ApiModelProperty(value = "외부몰 주문번호")
    private String outmallDetailId;

    @ApiModelProperty(value = "매출금액(vat 제외)")
    private Long vatRemovePaidPrice;

    @ApiModelProperty(value = "vat")
    private Long vat;

    @ApiModelProperty(value = "매출금액(vat 포함)")
    private Long paidPrice;

    @ApiModelProperty(value = "판매수량")
    private Long orderCnt;

    @ApiModelProperty(value = "결제일자")
    private String icDt;

    @ApiModelProperty(value = "주문일자")
    private String orderIfDt;

    @ApiModelProperty(value = "주문확정일자")
    private String settleDt;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "에누리금액")
    private Long discountPrice;

    @ApiModelProperty(value = "쿠폰금액")
    private Long couponPrice;

    @ApiModelProperty(value = "기타공제금액")
    private Long etcDiscountPrice;

    @ApiModelProperty(value = "최종판매수량")
    private Long settleItemCnt;

    @ApiModelProperty(value = "판매처ID")
    private String omSellersId;

    @ApiModelProperty(value = "외부몰 주문번호")
    private Long outmallId;

    @ApiModelProperty(value = "총매출금액")
    private Long totalAmt;

    @ApiModelProperty(value = "외부몰 주문 대사 총 Count")
    private Long totalCnt;
}
