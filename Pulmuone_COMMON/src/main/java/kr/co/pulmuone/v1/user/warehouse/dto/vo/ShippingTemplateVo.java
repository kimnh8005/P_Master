package kr.co.pulmuone.v1.user.warehouse.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "WarehouseResultVo")
public class ShippingTemplateVo {

    @ApiModelProperty(value = "배송정책명")
    private String shippingTemplateName;

    @ApiModelProperty(value = "기본여부")
    private String basicYn;

    @ApiModelProperty(value = "기본여부 체크박스")
    private boolean basicYnCheck;

}
