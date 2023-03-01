package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "품목 매장 가격 목록 요청 dto")
public class ItemStorePriceLogRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "품목코드")
	private String ilItemCd;

	@ApiModelProperty(value = "매장 PK")
	private String urStoreId;
}
