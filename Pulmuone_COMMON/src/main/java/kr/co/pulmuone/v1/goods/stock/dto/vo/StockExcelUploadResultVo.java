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
@ApiModel(description = "ERP 재고 엑셀업로드 Result Vo")
public class StockExcelUploadResultVo extends BaseRequestDto {

	@ApiModelProperty(value = "남은 일수")
	private long restDay;

	@ApiModelProperty(value = "고객사코드")
	private String strKey;

	@ApiModelProperty(value = "재고 수량")
	private String stockQty;

	@ApiModelProperty(value = "예정일")
	private String scheduleDt;

	@ApiModelProperty(value = "출고 기준일")
	private long delivery;

	@ApiModelProperty(value = "품목별 출고처 PK")
	private long ilItemWarehouseId;

	@ApiModelProperty(value = "기준일")
	private String baseDt;

	@ApiModelProperty(value = "유통기한")
	private String expirationDt;

	@ApiModelProperty(value = "상품코드")
	private String ilGoodsId;

	@ApiModelProperty(value = "마스터상품명")
	private String itemNm;

	@ApiModelProperty(value = "재고타입")
	private String stockTp;

	@ApiModelProperty(value = "성공여부")
	private String successYn;

	@ApiModelProperty(value = "엑셀업로드 로그ID")
	private String ilStockExcelUploadLogId;

	@ApiModelProperty(value = "품목코드")
	private String ilItemCd;

	@ApiModelProperty(value = "에러내용")
	private String msg;

	@ApiModelProperty(value = "중복건수")
	private int num;

}
