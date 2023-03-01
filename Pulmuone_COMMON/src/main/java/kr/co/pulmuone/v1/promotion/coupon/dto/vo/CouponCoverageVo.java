package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "쿠폰 적용대상 List Result")
public class CouponCoverageVo {

    @ApiModelProperty(value = "적용대상 구분")
    private String coverageType;

    @ApiModelProperty(value = "적용대상 명")
    private String coverageName;

    @ApiModelProperty(value = "적용대상 PK")
    private Long coverageId;

}