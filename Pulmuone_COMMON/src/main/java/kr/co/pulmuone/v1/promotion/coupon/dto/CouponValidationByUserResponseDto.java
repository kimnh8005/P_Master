package kr.co.pulmuone.v1.promotion.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.CouponEnums;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponValidationInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "유저 쿠폰 등록 검증 ResponseDto")
public class CouponValidationByUserResponseDto {

    @ApiModelProperty(value = "유저 쿠폰 등록 검증 resultVo")
    private CouponValidationInfoVo data;

    @ApiModelProperty(value = "유저 쿠폰 등록 검증 결과 Enum")
    private CouponEnums.AddCouponValidation validationEnum;
}
