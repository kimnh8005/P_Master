package kr.co.pulmuone.v1.user.brand.dto.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUrSupplierIdListResultVo")
public class GetUrSupplierIdListResultVo {

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "공급업체 코드")
    private String urSupplierId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "회사 코드")
    private String urCompanyId;

    @JsonSerialize(using =  ToStringSerializer.class)
    @ApiModelProperty(value = "회사 이름")
    private String companyName;


}
