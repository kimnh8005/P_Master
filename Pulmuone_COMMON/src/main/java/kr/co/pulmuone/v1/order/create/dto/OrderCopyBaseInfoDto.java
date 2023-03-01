package kr.co.pulmuone.v1.order.create.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * <PRE>
 * Forbiz Korea
 * 주문복사 기본정보 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 02.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문복사 기본정보 Dto")
public class OrderCopyBaseInfoDto {

	@ApiModelProperty(value = "주문PK")
    private Long odOrderId;

	@ApiModelProperty(value = "주문번호")
	private String odid;

	@ApiModelProperty(value = "주문상세 부모 ID")
	private long odOrderDetlParentId;

    @ApiModelProperty(value = "주문자 유형 : 공통코드(BUYER_TYPE)")
    private String buyerTypeCd;

    @ApiModelProperty(value = "주문상세 PK ")
    private long odOrderDetlId;

    @ApiModelProperty(value = "배송지 PK ")
    private long odShippingZoneId;

    @ApiModelProperty(value = "배송비 정책 PK")
    private long ilGoodsShippingTemplateId;

    @ApiModelProperty(value = "배송비  PK")
    private long odShippingPriceId;

    @ApiModelProperty(value = "배송비 정책 PK")
    private long ilShippingTmplId;

    @ApiModelProperty(value = "출고처 PK")
    private long urWarehouseId;

    @ApiModelProperty(value = "상품유형")
    private String goodsTpCd;

    @ApiModelProperty(value = "상품PK")
    private long ilGoodsId;

    @ApiModelProperty(value = "주문수량")
    private int orderCnt;

    @ApiModelProperty(value = "장바구니쿠폰할인금액")
    private int cartCouponPrice;

    @ApiModelProperty(value = "상품쿠폰할인금액")
    private int goodsCouponPrice;

    @ApiModelProperty(value = "상품,장바구니쿠폰 할인 제외한 할인금액")
    private int directPrice;

    @ApiModelProperty(value = "결제금액 (쿠폰까지 할인된 금액)")
    private int paidPrice;

    @ApiModelProperty(value = "배송비 정책 PK + 출고처 PK")
    private String shippingZone;

    @ApiModelProperty(value = "배송비쿠폰할인금액")
    private int shippingCouponPrice;

    @ApiModelProperty(value = "배송비구하려는 상품금액")
    private int goodsPrice;

    @ApiModelProperty(value = "배송비구하려는 우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "과세금액")
    private int taxablePrice;

    @ApiModelProperty(value = "비과세금액")
    private int nonTaxablePrice;

    @ApiModelProperty(value = "일일배송주기코드")
    private String goodsCycleTp;

	@ApiModelProperty(value = "새벽배송여부")
	private Boolean isDawnDelivery;

	@ApiModelProperty(value = "판매가")
	private int salePrice;

    @ApiModelProperty(value = "주문상세 뎁스")
    private int odOrderDetlDepthId;

    @ApiModelProperty(value = "주문 배송비")
    private int orgShippingPrice;

    @ApiModelProperty(value = "주문 I/F 일자")
    private LocalDate orderIfDt;

    @ApiModelProperty(value = "출고예정일일자")
    private LocalDate shippingDt;

    @ApiModelProperty(value = "도착예정일일자")
    private LocalDate deliveryDt;

    @ApiModelProperty(value = "매장 PK")
    private String urStoreId;

    @ApiModelProperty(value = "배송유형")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "프로모션 타입")
    private String promotionTp;

}