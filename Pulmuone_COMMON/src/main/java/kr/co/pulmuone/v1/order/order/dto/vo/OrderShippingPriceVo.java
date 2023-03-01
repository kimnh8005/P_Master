package kr.co.pulmuone.v1.order.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 배송비 관련 VO
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
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(description = "주문 배송비 OD_SHIPPING_PIRCE VO")
public class OrderShippingPriceVo {
    @ApiModelProperty(value = "주문 배송비 PK")
    private long odShippingPriceId;

    @ApiModelProperty(value = "배송정책 PK")
    private long ilShippingTmplId;

    @ApiModelProperty(value = "선불착불여부")
    private int paymentMethod;

    @ApiModelProperty(value = "택배")
    private int method;

    @ApiModelProperty(value = "주문시 상품 배송비")
    private int orderShippingPrice;

    @ApiModelProperty(value = "도서산간지역배송비")
    private int jejuIslandShippingPrice;

    @ApiModelProperty(value = "배송비")
    private int shippingPrice;

    @ApiModelProperty(value = "쿠폰 PK")
    private long pmCouponIssueId;

    @ApiModelProperty(value = "쿠폰명")
    private String pmCouponNm;

    @ApiModelProperty(value = "배송비 할인금액")
    private int shippingDiscountPrice;

    @ApiModelProperty(value = "정산 배송비금액")
    private int orgShippingPrice;

    @ApiModelProperty(value = "정산여부")
    private String setlYn;

    @ApiModelProperty(value = "등록일")
    private LocalDateTime createDt;

    public OrderShippingPriceVo copy(OrderShippingPriceVo vo){
        this.odShippingPriceId = vo.getOdShippingPriceId();
        this.ilShippingTmplId = vo.getIlShippingTmplId();
        this.paymentMethod = vo.getPaymentMethod();
        this.method = vo.getMethod();
        this.orderShippingPrice = vo.getOrderShippingPrice();
        this.jejuIslandShippingPrice = vo.getJejuIslandShippingPrice();
        this.shippingPrice = vo.getShippingPrice();
        this.pmCouponIssueId = vo.getPmCouponIssueId();
        this.pmCouponNm = vo.getPmCouponNm();
        this.shippingDiscountPrice = vo.getShippingDiscountPrice();
        this.orgShippingPrice = vo.getOrgShippingPrice();
        this.setlYn = vo.getSetlYn();
        this.createDt = vo.getCreateDt();

        return this;
    }
}
