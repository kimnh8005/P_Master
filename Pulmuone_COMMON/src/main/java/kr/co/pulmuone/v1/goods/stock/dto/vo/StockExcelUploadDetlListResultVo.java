package kr.co.pulmuone.v1.goods.stock.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ERP 재고 엑셀 업로드 상세내역 Result Vo")
public class StockExcelUploadDetlListResultVo extends BaseRequestDto {

	@ApiModelProperty(value = "재고 수량")
	private String stockQty;

	@ApiModelProperty(value = "품목코드")
	private String ilItemCd;

	@ApiModelProperty(value = "유통기한")
	private String expirationDt;

	@ApiModelProperty(value = "품목명")
	private String itemNm;

	@ApiModelProperty(value = "등록일시")
	private String createDt;

	@ApiModelProperty(value = "성공여부")
	private String successYn;

}
