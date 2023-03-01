package kr.co.pulmuone.v1.promotion.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@ApiModel(description = "쿠폰 적용 대상 조회 ResponseDto")
public class CouponCoverageResponseDto {

    @ApiModelProperty(value = "쿠폰 적용 대상")
    private List<CouponCoverageDto> coverage;

    @ApiModelProperty(value = "쿠폰 제외 대상")
    private List<CouponCoverageDto> notCoverage;
}
