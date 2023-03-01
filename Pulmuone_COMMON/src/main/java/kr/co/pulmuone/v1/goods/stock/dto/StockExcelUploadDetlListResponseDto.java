package kr.co.pulmuone.v1.goods.stock.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockExcelUploadDetlListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "StockExcelUploadDetlListResponseDto")
public class StockExcelUploadDetlListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "결과값 row")
	private	List<StockExcelUploadDetlListResultVo> rows;

	@ApiModelProperty(value = "총 count")
	private long total;

	@ApiModelProperty(value = "ERP 재고 엑셀 업로드 상세내역 리스트 Vo")
	private StockExcelUploadDetlListResultVo stockExcelUploadDetlListResultVo;

}
