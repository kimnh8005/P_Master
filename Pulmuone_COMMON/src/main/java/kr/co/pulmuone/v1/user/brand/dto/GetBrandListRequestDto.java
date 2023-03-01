package kr.co.pulmuone.v1.user.brand.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "브랜드 검색 Request")
public class GetBrandListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "브랜드 검색유형")
	private String brandSearchType;

	@ApiModelProperty(value = "브랜드 검색어")
	private String brandSearchValue;

	@ApiModelProperty(value = "사용여부")
	private String searchUseYn;

	@ApiModelProperty(value = "공급업체 ID")
	private String searchUrSupplierId;

}
