package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품 쿠폰 사용 정보 DTO")
public class UseGoodsCouponDto {
	@ApiModelProperty(value = "장바구니 PK")
	private Long spCartId;

	@ApiModelProperty(value = "쿠폰 발급 PK")
	private Long pmCouponIssueId;
}
