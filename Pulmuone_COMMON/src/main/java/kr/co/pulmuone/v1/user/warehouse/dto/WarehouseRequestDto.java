package kr.co.pulmuone.v1.user.warehouse.dto;

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
@ApiModel(description = "GetCompanyListRequestDto")
public class WarehouseRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "출고처 명")
	private String warehouseName;

	@ApiModelProperty(value = "공급처")
	private String supplier;

	@ApiModelProperty(value = "출고처 ID")
	private String urWarehouseId;

	@ApiModelProperty(value = "출고처 그룹")
	private String warehouseGroup;

	@ApiModelProperty(value = "배송 타입")
	private String deliveryType;

	public ArrayList<String> getWarehouseGroupList() {

		if(!StringUtil.isEmpty(this.warehouseGroup)) {
			return new ArrayList<String>(Arrays.asList(warehouseGroup.split(Constants.ARRAY_SEPARATORS)));
		}
		return new ArrayList<String>();

	}

}
