package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class StampDetailValidation {

    @ApiModelProperty(value = "스탬프 상세 PK")
    private Long evEventStampDetlId;

    @ApiModelProperty(value = "스탬프번호")
    private int stampCount;

    @ApiModelProperty(value = "당첨자 혜택 유형")
    private String eventBenefitType;

    @ApiModelProperty(value = "적립금 PK")
    private Long pmPointId;

    @ApiModelProperty(value = "당첨자 혜택")
    private String benefitName;

    @ApiModelProperty(value = "쿠폰 list pk")
    private List<EventCouponVo> coupon;

}
