package kr.co.pulmuone.v1.goods.stock.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "StockUploadListRequestDto")
public class StockUploadListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "시작일자")
	private String startCreateDate;

	@ApiModelProperty(value = "종료일자")
	private String endCreateDate;

	@ApiModelProperty(value = "재고 엑셀 업로드 로그PK")
	private String ilStockExcelUploadLogId;

}
