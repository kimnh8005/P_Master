package kr.co.pulmuone.v1.promotion.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "쿠폰 정보 목록 조회 RequestDto")
public class CouponListByUserRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "회원 ID", hidden = true)
    private Long urUserId;

    @ApiModelProperty(value = "조회구분")
    private String status;

    @ApiModelProperty(value = "쿠폰구분")
    private String couponType;

}
