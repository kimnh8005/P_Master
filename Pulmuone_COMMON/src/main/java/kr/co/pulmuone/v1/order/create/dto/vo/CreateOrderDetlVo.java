package kr.co.pulmuone.v1.order.create.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문생성 주문 상세 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 18.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문생성 주문 상세  OD_CREATE_ORDER_DETL VO")
public class CreateOrderDetlVo {
    @ApiModelProperty(value = "주문생성 상세 PK")
    private long odCreateOrderDetlId;

    @ApiModelProperty(value = "주문생성 PK")
    private long odCreateInfoId;

    @ApiModelProperty(value = "주문생성 배송지 PK")
    private long odCreateShippingZoneId;

    @ApiModelProperty(value = "주문생성 배송비 PK")
    private long odCreateShippingPriceId;

    @ApiModelProperty(value = "배송지 정책  PK")
    private long ilShippingTmplId;

    @ApiModelProperty(value = "출고처 PK")
    private long urWarehouseId;

    @ApiModelProperty(value = "공급업체 Pk")
    private long urSupplierId;

    @ApiModelProperty(value = "예약정보 PK")
    private long ilGoodsReserveOptnId;

    @ApiModelProperty(value = "기획전 증정행사 PK")
    private long evExhibitGiftId;

    @ApiModelProperty(value = "판매처 주문번호")
    private String sellerOrderId;

    @ApiModelProperty(value = "출고처그룹")
    private String urWarehouseGrpCd;

    @ApiModelProperty(value = "배송유형")
    private String deliveryTypeCd;

    @ApiModelProperty(value = "상품보관방법")
    private String storageTypeCd;

    @ApiModelProperty(value = "상품유형")
    private String goodsTpCd;

    @ApiModelProperty(value = "배송유형")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "판매유형")
    private String saleTpCd;

    @ApiModelProperty(value = "표준 카테고리")
    private int ilCtgryStdId;

    @ApiModelProperty(value = "전시 카테고리")
    private int ilCtgryDisplayId;

    @ApiModelProperty(value = "몰인몰 카테고리")
    private int ilCtgryMallId;

    @ApiModelProperty(value = "품목바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "상품 PK")
    private long ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "과세구분")
    private String taxYn;

    @ApiModelProperty(value = "할인유형")
    private String goodsDiscountTp;

    @ApiModelProperty(value = "주문수량")
    private int orderCnt;

    @ApiModelProperty(value = "원가")
    private int standardPrice;

    @ApiModelProperty(value = "정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "판매가")
    private int salePrice;

    @ApiModelProperty(value = "주문금액")
    private int paidPrice;

    @ApiModelProperty(value = "쿠폰 할인금액")
    private int couponPrice;

    @ApiModelProperty(value = "장바구니쿠폰 할인금액")
    private int cartCouponPrice;

    @ApiModelProperty(value = "임직원 즉시 할인금액")
    private int directPrice;

    @ApiModelProperty(value = "정기배송 할인금액")
    private int regularPrice;

}
