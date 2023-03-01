package kr.co.pulmuone.v1.user.store.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ShopListRequestDto")
public class StoreListRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "검색유형")
	private String searchType;

	@ApiModelProperty(value = "검색어")
	private String searchValue;

	@ApiModelProperty(value = "노출여부")
	private String searchUseYn;

	@ApiModelProperty(value = "매장유형")
	private String searchStoreType;

	@ApiModelProperty(value = "O2O매장여부")
	private String searchOnlineDivYn;
}
