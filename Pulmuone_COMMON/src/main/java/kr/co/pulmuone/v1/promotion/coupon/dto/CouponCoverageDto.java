package kr.co.pulmuone.v1.promotion.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@ApiModel(description = "쿠폰 적용대상 List Result")
public class CouponCoverageDto {

    @ApiModelProperty(value = "적용대상 구분")
    private String coverageType;

    @ApiModelProperty(value = "적용대상 명")
    private List<String> coverageName;
}