package kr.co.pulmuone.v1.user.group.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GroupBenefitVo {

    @ApiModelProperty(value = "혜택 쿠폰 / 적립금 명")
    private String benefitRelationName;

    @ApiModelProperty(value = "혜택")
    private String benefit;

    @ApiModelProperty(value = "혜택 쿠폰 수량")
    private String benefitCount;

    @ApiModelProperty(value = "혜택 유형")
    private String benefitType;
}
