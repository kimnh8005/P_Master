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
@ApiModel(description = "OmBasicFeeListRequestDto")
public class OmBasicFeeListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "공급처 PK")
	private Long searchSupplierId;

	@ApiModelProperty(value = "판매처 그룹코드")
	private String searchSellersGroup;

	@ApiModelProperty(value = "판매처 PK")
	private Long searchOmSellersId;

	@ApiModelProperty(value = "정산방식")
	private String searchCalcType;

	@ApiModelProperty(value = "정산방식 목록")
	private List<String> calcTypeList;

}
