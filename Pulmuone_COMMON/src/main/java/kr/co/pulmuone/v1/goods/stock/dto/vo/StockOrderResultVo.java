package kr.co.pulmuone.v1.goods.stock.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "재고 주문 Result Vo")
public class StockOrderResultVo {

	@ApiModelProperty(value = "품목별 출고처ID")
    private long ilItemWarehouseId;

	@ApiModelProperty(value = "품목 연동재고ID")
    private long ilItemErpStockId;

	@ApiModelProperty(value = "주문수량")
	private long orderQty;

	@ApiModelProperty(value = "재고연동여부")
    private String stockOrderYn;

	@ApiModelProperty(value = "미연동 재고 무제한 사용여부")
    private String unlimitStockYn;

	@ApiModelProperty(value = "재고타입")
    private String stockTp;

	@ApiModelProperty(value = "예정일")
	private String scheduleDt;

	@ApiModelProperty(value = "등록자 PK")
	private long createId;

	@ApiModelProperty(value = "상품유형")
	private String goodsTp;

	@ApiModelProperty(value = "판매유형")
	private String saleTp;

	@ApiModelProperty(value = "기타정보(주문번호등)")
	private String memo;

	@ApiModelProperty(value = "주문가능재고수량")
	private int availableOrderCnt;

}
