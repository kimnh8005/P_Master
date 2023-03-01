package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문복사에 필요한 주문상세 정보 Dto")
public class OrderCopyDetlInfoDto {

	@ApiModelProperty(value = "주문 상세 PK")
    private long odOrderDetlId;

    @ApiModelProperty(value = "주문 배송지 PK")
    private long odShippingZoneId;

    @ApiModelProperty(value = "주문 배송비 PK")
    private long odShippingPriceId;

    @ApiModelProperty(value = "배송비 정책 PK")
    private long ilGoodsShippingTemplateId;

    @ApiModelProperty(value = "출고처 PK")
    private long urWarehouseId;

    @ApiModelProperty(value = "상품 코드 PK")
    private long ilGoodsId;

    @ApiModelProperty(value = "상품유형")
    private String goodsTpCd;

	@ApiModelProperty(value = "새벽배송여부")
	private Boolean isDawnDelivery;

	@ApiModelProperty(value = "일일 배송주기코드")
	private String goodsCycleTp;

    @ApiModelProperty(value = "엑셀 업로드 성공 정보 PK")
    private String ifOutmallExcelSuccId;

    @ApiModelProperty(value = "일일 상품 배송유형")
    private String goodsDailyTp;

    @ApiModelProperty(value = "주문수량")
    private int orderCnt;

    @ApiModelProperty(value = "판매가")
    private int salePrice;

    @ApiModelProperty(value = "장바구니 쿠폰 할인금액 합")
    private int cartCouponPrice;

    @ApiModelProperty(value = "상품 쿠폰 할인금액")
    private int goodsCouponPrice;

    @ApiModelProperty(value = "결제금액금액 합")
    private int paidPrice;

    @ApiModelProperty(value = "상품,장바구니쿠폰 할인 제외한 할인금액")
    private int directPrice;

    @ApiModelProperty(value = "과세금액")
    private int taxablePrice;

    @ApiModelProperty(value = "비과세금액")
    private int nonTaxablePrice;
}
