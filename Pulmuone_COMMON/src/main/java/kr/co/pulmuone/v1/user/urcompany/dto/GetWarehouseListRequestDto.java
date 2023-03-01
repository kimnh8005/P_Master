package kr.co.pulmuone.v1.user.urcompany.dto;

import java.util.ArrayList;
import java.util.Arrays;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.comm.constants.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetWarehouseListRequestDto")
public class GetWarehouseListRequestDto extends BaseRequestPageDto{

    @ApiModelProperty(value = "타입", required = false)
    String type;

    @ApiModelProperty(value = "공급처명", required = false)
    String supplierCompanyName;

    @ApiModelProperty(value = "출고처그룹", required = false)
    String warehouseGroup;

    @ApiModelProperty(value = "공급처코드", required = false)
    String supplierCode;

    @ApiModelProperty(value = "재고발주여부", required = false)
    String stockOrderYn;

	public ArrayList<String> getWarehouseGroupList() {
		if(!StringUtil.isNvl(StringUtil.nvl(this.warehouseGroup)) && !Constants.ARRAY_SEPARATORS.equals(StringUtil.nvl(this.warehouseGroup))) {
			return new ArrayList<String>(Arrays.asList(warehouseGroup.split(Constants.ARRAY_SEPARATORS)));
		}
		return new ArrayList<String>();

	}
}
