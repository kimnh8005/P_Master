package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ItemStoreStockRequestDto extends BaseRequestPageDto  {

	@ApiModelProperty(value = "단일, 복수 조건")
	private String selectConditionType;

	@ApiModelProperty(value = "마스터품목명")
	private String itemNm;

	@ApiModelProperty(value = "매장")
	private String storeNm;

	@ApiModelProperty(value = "매장상품 유형 리스트")
	private List<String> itemStoreTypeList;

	@ApiModelProperty(value = "매장상품 유형")
	private String itemStoreType;

	@ApiModelProperty(value = "품목 코드")
	private String itemCodes;

	@ApiModelProperty(value = "품목 코드 리스트")
	private List<String> itemCodeList;

}
