package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "유저 쿠폰 수량 Result")
public class CouponCountByUserVo {

    @ApiModelProperty(value = "상품쿠폰수량 - 중복제거")
    private int goodsCount = 0;

    @ApiModelProperty(value = "장바구니쿠폰수량 - 중복제거")
    private int cartCount = 0;

    @ApiModelProperty(value = "배송비쿠폰수량 - 중복제거")
    private int shippingPriceCount = 0;

    @ApiModelProperty(value = "총쿠폰수량")
    private int couponTotalCount = 0;

}
