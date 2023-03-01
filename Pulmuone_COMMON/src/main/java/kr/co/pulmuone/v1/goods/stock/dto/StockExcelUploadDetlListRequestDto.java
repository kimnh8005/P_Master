package kr.co.pulmuone.v1.goods.stock.dto;

import java.util.ArrayList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "StockExcelUploadDetlListRequestDto")
public class StockExcelUploadDetlListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "품목코드 PK")
	private String ilItemCd;

	@ApiModelProperty(value = "코드검색")
	private String itemCodes;

	@ApiModelProperty(value = "품목명")
	private String itemName;

	@ApiModelProperty(value = "품목코드Array")
	private ArrayList<String> ilItemCodeArray;

	@ApiModelProperty(value = "검색조건 종류")
    private String selectConditionType;

	@ApiModelProperty(value = "엑셀업로드ID")
	private String ilStockExcelUploadLogId;

	@ApiModelProperty(value = "검색종류")
	private String searchKind;
}
