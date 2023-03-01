package kr.co.pulmuone.v1.policy.fee.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "OmLogisticsFeeHistListRequestDto")
public class OmLogisticsFeeHistListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "출고처 pk")
	private Long urWarehouseId;

	@ApiModelProperty(value = "공급처 PK")
	private Long urSupplierId;

}