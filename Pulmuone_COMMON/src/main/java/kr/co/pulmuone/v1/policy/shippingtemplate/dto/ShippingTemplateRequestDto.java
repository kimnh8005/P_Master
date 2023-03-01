package kr.co.pulmuone.v1.policy.shippingtemplate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송정책설정 목록 Request")
public class ShippingTemplateRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "배송정책명")
    private String shippingTemplateName;

    @ApiModelProperty(value = "출고처 ID")
    private String warehouseId;

    @ApiModelProperty(value = "조건배송비구분")
    private String conditionType;

    @ApiModelProperty(value = "출고처 기준 보기")
    private String warehouseViewYn;

}
