package kr.co.pulmuone.v1.goods.stock.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "StockListRequestDto")
public class StockListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "품목 출고처 PK")
	private long ilItemWarehouseId;

	@ApiModelProperty(value = "공급처,출고처 PK")
	private long urSupplierWarehouseId;

	@ApiModelProperty(value = "품목코드 PK")
	private String ilItemCd;

	@ApiModelProperty(value = "코드검색")
	private String itemCodes;

	@ApiModelProperty(value = "선주문 여부")
	private String popPreOrderYn;

	@ApiModelProperty(value = "선주문 유형")
	private String popPreOrderTp;

	@ApiModelProperty(value = "기준일자(단일조건)")
	private String baseDt;

	@ApiModelProperty(value = "기준일자(복수조건)")
	private String subBaseDt;

	@ApiModelProperty(value = "품목코드Array")
	private ArrayList<String> ilItemCodeArray;

	@ApiModelProperty(value = "검색조건 종류")
    private String selectConditionType;

	@ApiModelProperty(value = "상품 판매상태")
    private String saleStatus;

	@ApiModelProperty(value = "상품유형")
    private String goodsType;

	@ApiModelProperty(value = "보관방법")
    private String keepMethod;

	@ApiModelProperty(value = "재고운영형태")
    private String stockOperType;

	@ApiModelProperty(value = "표준카테고리(대분류)")
    private String bigCategory;

	@ApiModelProperty(value = "공급업체")
    private String urSupplierId;

	@ApiModelProperty(value = "출고처")
    private String urWarehouseId;

	@ApiModelProperty(value = "마스터품목명")
    private String itemName;

	@ApiModelProperty(value = "판매상태 리스트")
    private List<String> saleStatusList;

	@ApiModelProperty(value = "상품유형 리스트")
    private List<String> goodsTypeList;

	@ApiModelProperty(value = "상품 미생성 품목 여부")
    private String isNoGoodsItem;

	@ApiModelProperty(value = "상품유형 리스트")
    private List<String> storageMethodTypeList;

	@ApiModelProperty(value = "품목재고SEQ")
    private String ilItemStockId;

	@ApiModelProperty(value = "입고예정수량")
    private String stockScheduledCount;

	@ApiModelProperty(value = "재고기한관리ID")
    private String popIlStockDeadlineId;

	@ApiModelProperty(value = "전일마감재고 조정수량")
    private int d0StockClosedAdjQty;

	@ApiModelProperty(value = "입고예정수량 조정수량")
    private int d0StockScheduledAdjQty;

	@ApiModelProperty(value = "입고확정수량 조정수량")
    private int d0StockConfirmedAdjQty;

	@ApiModelProperty(value = "상품ID")
	private int goodsId;

	@ApiModelProperty(value = "접근권한 출고처 ID 리스트")
    private List<String> listAuthWarehouseId;

	@ApiModelProperty(value = "접근권한 공급처 ID 리스트")
    private List<String> listAuthSupplierId;

}
