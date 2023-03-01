package kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송비 출고처 정보 Vo")
public class ShippingWarehouseVo{

    @ApiModelProperty(value = "배송정책 ID")
    private Long shippingTemplateId;

    @ApiModelProperty(value = "배송비 출고처 정보 ID")
    private Long shippingWarehouseId;

    @ApiModelProperty(value = "출고처코드")
    private Long warehouseId;

    @ApiModelProperty(value = "출고처명")
    private String warehouseName;

    @ApiModelProperty(value = "기본여부")
    private String basicYn;

    @ApiModelProperty(value = "기본여부 체크박스")
    private boolean basicYnCheck;

    @ApiModelProperty(value = "등록자ID")
    private String createId;

}
