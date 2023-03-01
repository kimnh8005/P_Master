package kr.co.pulmuone.v1.system.shopconfig.dto.basic;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = " SaveShopConfigRequestDto")
public class SaveSystemShopConfigRequestDto extends BaseRequestDto {

    @ApiModelProperty(value = "", required = false)
    String insertData;

    @ApiModelProperty(value = "", required = false)
    String updateData;

    @ApiModelProperty(value = "", required = false)
    String deleteData;

    @ApiModelProperty(value = "", hidden = true)
    List<SaveSystemShopConfigRequestSaveDto> insertRequestDtoList = new ArrayList<SaveSystemShopConfigRequestSaveDto>();

    @ApiModelProperty(value = "", hidden = true)
    List<SaveSystemShopConfigRequestSaveDto> updateRequestDtoList = new ArrayList<SaveSystemShopConfigRequestSaveDto>();

    @ApiModelProperty(value = "", hidden = true)
    List<SaveSystemShopConfigRequestSaveDto> deleteRequestDtoList = new ArrayList<SaveSystemShopConfigRequestSaveDto>();

}
