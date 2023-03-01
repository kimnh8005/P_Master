package kr.co.pulmuone.v1.order.create.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


/**
 * <PRE>
 * Forbiz Korea
 * 주문생성 주문 배송비 관련 VO
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
@ApiModel(description = "주문생성 주문 배송비 OD_CREATE_SHIPPING_PRICE VO")
public class CreateShippingPriceVo {
    @ApiModelProperty(value = "주문생성 주문 배송비 PK")
    private long odCreateShippingPriceId;

    @ApiModelProperty(value = "배송정책 PK")
    private String ilShippingTmplId;

    @ApiModelProperty(value = "선불착불여부")
    private String paymentMethod;

    @ApiModelProperty(value = "택배")
    private String method;

    @ApiModelProperty(value = "배송비")
    private String shippingPrice;

    @ApiModelProperty(value = "쿠폰 PK")
    private String pmCouponId;

    @ApiModelProperty(value = "쿠폰명")
    private String pmCouponNm;

    @ApiModelProperty(value = "배송비 할인금액")
    private String shippingDiscountPrice;

    @ApiModelProperty(value = "정산 배송비금액")
    private String orgShippingPrice;

    @ApiModelProperty(value = "정산여")
    private String setlYn;

    @ApiModelProperty(value = "등록일")
    private LocalDateTime createDt;
}
