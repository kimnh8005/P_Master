package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문 사용 가능 쿠폰 조회 응답 DTO")
public class GetCouponPageInfoResponseDto{


	@ApiModelProperty(value = "상품쿠폰 리스트")
	private List<GoodsCouponDto> goodsList;

	@ApiModelProperty(value = "장바구니 쿠폰 리스트")
	private List<CouponDto> cartCouponList;

	@ApiModelProperty(value = "배송비 쿠폰 리스트")
	private List<ShippingCouponDto> shippingCoupon;

	@ApiModelProperty(value = "사용 가능 쿠폰 개수")
	private int availableCouponCnt;

	@ApiModelProperty(value = "사용 가능 쿠폰 발급 PK 리스트")
	private List<Long> pmCouponIssueIds;
}
