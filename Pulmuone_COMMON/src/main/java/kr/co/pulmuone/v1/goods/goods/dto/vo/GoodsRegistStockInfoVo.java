package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GoodsStockInfoVo")
public class GoodsRegistStockInfoVo
{
	/**
	 * 판매/전시 > 재고운영형태, 전일마감재고 관련
	 **/
	@ApiModelProperty(value = "품목 코드")
	private String ilItemCode;

	@ApiModelProperty(value = "품목 바코드")
	private String itemBarcode;

	@ApiModelProperty(value = "품목명")
	private String itemName;

	@ApiModelProperty(value = "재고운영형태, 재고상세보기용")
	private String preOrderName;

	@ApiModelProperty(value = "품목재고 ID")
	private String ilItemStockId;

	@ApiModelProperty(value = "입고예정량")
	private String stockScheduledCount;

	@ApiModelProperty(value = "품목별 출고처 관리 ID")
	private String ilItemWarehouseId;

	@ApiModelProperty(value = "품목재고 기준일")
	private String baseDate;

	@ApiModelProperty(value = "출고처 ID")
	private String urWarehouseId;

	@ApiModelProperty(value = "출고처명")
	private String warehouseName;

	@ApiModelProperty(value = "공급처 ID")
	private String urSupplierId;

	@ApiModelProperty(value = "공급처 CODE")
	private String supplierCode;

	@ApiModelProperty(value = "공급처명")
	private String supplierName;

	@ApiModelProperty(value = "선주문 가능/불가 구분")
	private String preOrderYn;

	@ApiModelProperty(value = "재고발주여부(연동재고 or 미연동재고)")
	private String stockOrderYn;

	@ApiModelProperty(value = "재고미연동시 재고무제한 사용여부")
	private String unlimitStockYn;

	@ApiModelProperty(value = "재고운영형태")
	private String stockOperationForm;

	@ApiModelProperty(value = "재고운영형태_선주문노출")
	private String stockOperationFormSub;

	@ApiModelProperty(value = "전일마감 재고 수량 or 미연동 한정재고 수량")
	private String stockQuantity;
}
