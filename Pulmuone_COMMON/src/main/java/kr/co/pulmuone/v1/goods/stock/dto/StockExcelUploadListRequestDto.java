package kr.co.pulmuone.v1.goods.stock.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ERP 재고 엑셀 업로드 Request")
public class StockExcelUploadListRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "수량")
    private String stockQty;

	@ApiModelProperty(value = "남은 일수")
    private long restDay;

    @ApiModelProperty(value = "출고처ID")
    private long urWarehouseId;

	@ApiModelProperty(value = "출고처 ID")
	private String ilItemWarehouseId;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "유통기간")
    private String expirationDt;

    @ApiModelProperty(value = "BOS타입")
    private String stockTp;

    @ApiModelProperty(value = "기준일")
    private String baseDt;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "성공여부")
    private String successYn;

    @ApiModelProperty(value = "메시지")
    private String msg;

    @ApiModelProperty(value = "엑셀업로드 로그ID")
    private String ilStockExcelUploadLogId;

    @ApiModelProperty(value = "파일이름")
    private String fileNm;
}
