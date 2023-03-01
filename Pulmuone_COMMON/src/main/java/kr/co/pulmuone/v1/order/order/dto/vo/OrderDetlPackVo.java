package kr.co.pulmuone.v1.order.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 묶음/골라담기 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 21.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 묶음/골라담기 OD_ORDER_DETL_PACK VO")
public class OrderDetlPackVo {

    @ApiModelProperty(value = "주문 상세 PK")
    private long odOrderDetlId;

    @ApiModelProperty(value = "주문상세 정렬 키")
    private long odOrderDetlStepId;

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

    @ApiModelProperty(value = "주문상세 정렬 뎁스")
    private long odOrderDetlDepthId;

    @ApiModelProperty(value = "상품유형")
    private String goodsTpCd;

    @ApiModelProperty(value = "배송유형")
    private String goodsDeliveryType;

    @ApiModelProperty(value = "기획전 유형")
    private String promotionTp;

    @ApiModelProperty(value = "상품 코드 PK")
    private long ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "주문수량")
    private int orderCnt;

    @ApiModelProperty(value = "원가")
    private int standardPrice;

    @ApiModelProperty(value = "정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "판매가")
    private double salePrice;

    @ApiModelProperty(value = "장바구니 쿠폰 할인금액 합")
    private int cartCouponPrice;

    @ApiModelProperty(value = "상품 쿠폰 할인금액")
    private int goodsCouponPrice;

    @ApiModelProperty(value = "즉시 할인금액 합")
    private int directPrice;

    @ApiModelProperty(value = "결제금액금액 합")
    private int paidPrice;


}
