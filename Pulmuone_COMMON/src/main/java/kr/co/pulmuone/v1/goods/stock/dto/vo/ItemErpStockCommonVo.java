package kr.co.pulmuone.v1.goods.stock.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ItemErpStockCommonVo {

	// [S] IL_ITEM_ERP_STOCK 테이블 기본 컬럼
	@ApiModelProperty(value = "품목 출고처 재고 인터페이스 PK")
	private long ilItemErpStockId;

	@ApiModelProperty(value = "품목 출고처 관리 PK")
	private long ilItemWarehouseId;

	@ApiModelProperty(value = "재고 기준일")
	private String baseDt;

	@ApiModelProperty(value = "재고 타입")
	private String stockTp;

	@ApiModelProperty(value = "재고 수량")
	private int stockQty;

	@ApiModelProperty(value = "예정일")
	private String scheduleDt;

	@ApiModelProperty(value = "등록자")
	private long createId;

	@ApiModelProperty(value = "등록일")
	private String createDt;

	@ApiModelProperty(value = "수정자")
	private long modifyId;

	@ApiModelProperty(value = "수정일")
	private String modifyDt;
	// [E] IL_ITEM_ERP_STOCK 테이블 기본 컬럼

}
