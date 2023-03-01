package kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송비 조건 정보 Vo")
public class ShippingConditionVo{

    @ApiModelProperty(value = "배송정책 ID")
    private Long shippingTemplateId;

    @ApiModelProperty(value = "배송비 조건정보 ID")
    private Long shippingConditionId;

    @ApiModelProperty(value = "배송비 금액")
    private long shippingPrice;

    @ApiModelProperty(value = "조건")
    private long conditionValue;

    @ApiModelProperty(value = "등록자ID")
    private String createId;

}
