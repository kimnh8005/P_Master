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
@ApiModel(description = "OmBasicFeeHistListRequestDto")
public class OmBasicFeeHistListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "판매처 PK")
	private Long omSellersId;

	@ApiModelProperty(value = "공급처 PK")
	private Long urSupplierId;

}