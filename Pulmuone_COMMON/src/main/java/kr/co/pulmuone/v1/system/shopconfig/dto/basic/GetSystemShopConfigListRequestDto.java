package kr.co.pulmuone.v1.system.shopconfig.dto.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetSystemShopConfigListRequestDto")
public class GetSystemShopConfigListRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "", required = false)
    String psGroupType;

    @ApiModelProperty(value = "", required = false)
    String searchPsKeyName;

}
