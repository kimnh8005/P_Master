package kr.co.pulmuone.v1.goods.stock.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "유통기한별 재고 연동 내역 관리 Result Vo")
public class StockExprResultVo {

	@ApiModelProperty(value = "품목 출고처 PK")
	private long ilItemWarehouseId;

	@ApiModelProperty(value = "재고 기준일 ( yyyyMMdd 형식 )")
    private String baseDt;

	@ApiModelProperty(value = "등록일시")
    private String createDate;

	@ApiModelProperty(value = "품목코드")
    private String ilItemCd;

	@ApiModelProperty(value = "품목바코드")
    private String itemBarcode;

	@ApiModelProperty(value = "바코드")
    private String barcode;

	@ApiModelProperty(value = "품목명")
    private String itemNm;

	@ApiModelProperty(value = "출고처명")
    private String warehouseNm;

	@ApiModelProperty(value = "공급처명")
    private String supplierNm;

	@ApiModelProperty(value = "보관방법명")
    private String storageMethodTpNm;

	@ApiModelProperty(value = "재고수량")
    private String stockQty;

	@ApiModelProperty(value = "유통기한")
    private String expirationDt;

	@ApiModelProperty(value = "잔여 유통기간")
    private String restDistributionPeriod;

	@ApiModelProperty(value = "재고타입명")
    private String stockTpNm;

	@ApiModelProperty(value = "상품유형명")
    private String goodsTpNm;

	@ApiModelProperty(value = "상품코드")
    private String ilGoodsId;

	@ApiModelProperty(value = "상품판매상태명")
    private String saleStatusNm;

	@ApiModelProperty(value = "표준카테고리(대분류)")
    private String ctgryStdNm;

	@ApiModelProperty(value = "재고미연동시 재고무제한 사용여부")
    private String unlimitStockYn;

	@ApiModelProperty(value = "미연동 재고 수량")
    private String notIfStockCnt;

	@ApiModelProperty(value = "재고")
    private String stock;

	@ApiModelProperty(value = "ERP 연동여부 ( DB 저장시 => Y:연동, N:미연동 )")
    private boolean erpLinkIfYn;

	@ApiModelProperty(value = "마스터 품목유형")
    private String itemTp;

	@ApiModelProperty(value = "상품유형명")
    private String goodsTp;

	@ApiModelProperty(value = "출고처 ID")
    private Long warehouseId;

	@ApiModelProperty(value = "출고처명")
    private String warehouseName;
}
