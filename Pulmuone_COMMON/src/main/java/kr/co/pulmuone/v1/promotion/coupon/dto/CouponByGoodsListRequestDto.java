package kr.co.pulmuone.v1.promotion.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "쿠폰 다운로드 - 상품상세 RequestDto")
public class CouponByGoodsListRequestDto {

    @ApiModelProperty(value = "쿠폰 ID")
    private List<CouponByGoodsRequestDto> coupon;

}
