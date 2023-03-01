package kr.co.pulmuone.v1.policy.fee.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "OmLogisticsFeeListRequestDto")
public class OmLogisticsFeeListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "출고처 PK")
	private Long searchUrWarehouseId;

	@ApiModelProperty(value = "공급처 PK")
	private Long searchUrSupplierId;

	@ApiModelProperty(value = "정산방식")
	private String searchCalcType;

	@ApiModelProperty(value = "정산방식 목록")
	private List<String> calcTypeList;

}
