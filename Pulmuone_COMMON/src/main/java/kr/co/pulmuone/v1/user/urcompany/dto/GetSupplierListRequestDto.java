package kr.co.pulmuone.v1.user.urcompany.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetSupplierListRequestDto")
public class GetSupplierListRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "출고처 아이디", required = false)
    private String urWarehouseId;

}
