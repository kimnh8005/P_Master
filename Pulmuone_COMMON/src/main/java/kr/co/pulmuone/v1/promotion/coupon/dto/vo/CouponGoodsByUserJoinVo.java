package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원가입 대상 쿠폰 상품 Result")
public class CouponGoodsByUserJoinVo {

    @ApiModelProperty(value = "할인값")
    private String discountValue;

    @ApiModelProperty(value = "상품 PK")
    private Long coverageId;

}
