package kr.co.pulmuone.v1.goods.stock.dto.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "품목별 재고리스트 Result Vo")
public class StockListResultVo {

	/*
     * 출고처별 품목 재고 Vo
     */
	@ApiModelProperty(value = "출고처별 품목 재고 seq")
    private long ilItemStockId;

	@ApiModelProperty(value = "품목 출고처 PK")
    private long ilItemWarehouseId;

	@ApiModelProperty(value = "재고 기준일 ( yyyyMMdd 형식 )")
    private String baseDt;

	@ApiModelProperty(value = "ERP 연동여부 ( DB 저장시 => Y:연동, N:미연동 )")
    private boolean erpLinkIfYn;

    /*
     * 재고 수량
     */
	@ApiModelProperty(value = "D0 재고 수량")
    private long d0stockQuantity;

	@ApiModelProperty(value = "D1 재고 수량")
    private long d1stockQuantity;

	@ApiModelProperty(value = "D2 재고 수량")
    private long d2stockQuantity;

	@ApiModelProperty(value = "D3 재고 수량")
    private long d3stockQuantity;

	@ApiModelProperty(value = "D4 재고 수량")
    private long d4stockQuantity;

	@ApiModelProperty(value = "D5 재고 수량")
    private long d5stockQuantity;

	@ApiModelProperty(value = "D6 재고 수량")
    private long d6stockQuantity;

	@ApiModelProperty(value = "D7 재고 수량")
    private long d7stockQuantity;

	@ApiModelProperty(value = "D8 재고 수량")
    private long d8stockQuantity;

	@ApiModelProperty(value = "D9 재고 수량")
    private long d9stockQuantity;

	@ApiModelProperty(value = "D10 재고 수량")
    private long d10stockQuantity;

	@ApiModelProperty(value = "D11 재고 수량")
    private long d11stockQuantity;

	@ApiModelProperty(value = "D12 재고 수량")
	private long d12stockQuantity;

	@ApiModelProperty(value = "D13 재고 수량")
    private long d13stockQuantity;

	@ApiModelProperty(value = "D14 재고 수량")
    private long d14stockQuantity;

	@ApiModelProperty(value = "D15 재고 수량")
	private long d15stockQuantity;

	@ApiModelProperty(value = "D0 폐기 임박 재고 수량")
	private long d0stockDiscardQuantity;

    /*
     * 입고예정 재고 수량
     */
	@ApiModelProperty(value = "D0 입고예정 재고 수량")
    private long d0StockScheduledQuantity;

	@ApiModelProperty(value = "D1 입고예정 재고 수량")
    private long d1StockScheduledQuantity;

	@ApiModelProperty(value = "D2 입고예정 재고 수량")
    private long d2StockScheduledQuantity;

	@ApiModelProperty(value = "D3 입고예정 재고 수량")
    private long d3StockScheduledQuantity;

	@ApiModelProperty(value = "D4 입고예정 재고 수량")
    private long d4StockScheduledQuantity;

	@ApiModelProperty(value = "D5 입고예정 재고 수량")
    private long d5StockScheduledQuantity;

	@ApiModelProperty(value = "D6 입고예정 재고 수량")
    private long d6StockScheduledQuantity;

	@ApiModelProperty(value = "D7 입고예정 재고 수량")
    private long d7StockScheduledQuantity;

	@ApiModelProperty(value = "D8 입고예정 재고 수량")
    private long d8StockScheduledQuantity;

	@ApiModelProperty(value = "D9 입고예정 재고 수량")
    private long d9StockScheduledQuantity;

	@ApiModelProperty(value = "D10 입고예정 재고 수량")
    private long d10StockScheduledQuantity;

	@ApiModelProperty(value = "D11 입고예정 재고 수량")
    private long d11StockScheduledQuantity;

	@ApiModelProperty(value = "D12 입고예정 재고 수량")
	private long d12StockScheduledQuantity;

	@ApiModelProperty(value = "D13 입고예정 재고 수량")
    private long d13StockScheduledQuantity;

	@ApiModelProperty(value = "D14 입고예정 재고 수량")
    private long d14StockScheduledQuantity;

	@ApiModelProperty(value = "D15 입고예정 재고 수량")
	private long d15StockScheduledQuantity;

	/*
     * 주문가능 수량
     */
	@ApiModelProperty(value = "D0 주문가능 수량")
    private long d0orderQuantity;

	@ApiModelProperty(value = "D1 주문가능 수량")
    private long d1orderQuantity;

	@ApiModelProperty(value = "D2 주문가능 수량")
    private long d2orderQuantity;

	@ApiModelProperty(value = "D3 주문가능 수량")
    private long d3orderQuantity;

	@ApiModelProperty(value = "D4 주문가능 수량")
    private long d4orderQuantity;

	@ApiModelProperty(value = "D5 주문가능 수량")
    private long d5orderQuantity;

	@ApiModelProperty(value = "D6 주문가능 수량")
    private long d6orderQuantity;

	@ApiModelProperty(value = "D7 주문가능 수량")
    private long d7orderQuantity;

	@ApiModelProperty(value = "D8 주문가능 수량")
    private long d8orderQuantity;

	@ApiModelProperty(value = "D9 주문가능 수량")
	private long d9orderQuantity;

	@ApiModelProperty(value = "D10 주문가능 수량")
    private long d10orderQuantity;

	@ApiModelProperty(value = "D11 주문가능 수량")
    private long d11orderQuantity;

	@ApiModelProperty(value = "D12 주문가능 수량")
	private long d12orderQuantity;

	@ApiModelProperty(value = "D13 주문가능 수량")
    private long d13orderQuantity;

	@ApiModelProperty(value = "D14 주문가능 수량")
    private long d14orderQuantity;

	@ApiModelProperty(value = "D15 주문가능 수량")
    private long d15orderQuantity;

	@ApiModelProperty(value = "D0 폐기 임박 주문 가능 수량")
	private long d0orderDiscardQuantity;

	@ApiModelProperty(value = "등록자 ID")
    private String createId;

	@ApiModelProperty(value = "등록일시")
    private String createDate;

	@ApiModelProperty(value = "수정자 ID")
    private String modifyId;

	@ApiModelProperty(value = "수정일시")
    private String modifyDate;

	@ApiModelProperty(value = "주문수량 리스트")
    private List<StockListResultVo> orderQtyList;


	/*
     * 주문가능 수량
     */
	@ApiModelProperty(value = "D0 주문 수량")
    private long d0Quantity;

	@ApiModelProperty(value = "D1 주문 수량")
    private long d1Quantity;

	@ApiModelProperty(value = "D2 주문 수량")
    private long d2Quantity;

	@ApiModelProperty(value = "D3 주문 수량")
    private long d3Quantity;

	@ApiModelProperty(value = "D4 주문 수량")
    private long d4Quantity;

	@ApiModelProperty(value = "D5 주문 수량")
    private long d5Quantity;

	@ApiModelProperty(value = "D6 주문 수량")
    private long d6Quantity;

	@ApiModelProperty(value = "D7 주문 수량")
    private long d7Quantity;

	@ApiModelProperty(value = "D8 주문 수량")
    private long d8Quantity;

	@ApiModelProperty(value = "D9 주문 수량")
	private long d9Quantity;

	@ApiModelProperty(value = "D10 주문 수량")
    private long d10Quantity;

	@ApiModelProperty(value = "D11 주문 수량")
    private long d11Quantity;

	@ApiModelProperty(value = "D12 주문 수량")
	private long d12Quantity;

	@ApiModelProperty(value = "D13 주문 수량")
    private long d13Quantity;

	@ApiModelProperty(value = "D14 주문 수량")
    private long d14Quantity;

	@ApiModelProperty(value = "D15 주문 수량")
    private long d15Quantity;

	@ApiModelProperty(value = "D0 폐기임박 주문 수량")
    private long d0DiscardQuantity;

	/*
     * IL_ITEM_WAREHOUSE 테이블의 정보 : ilItemWarehouseId ( 품목 출고처 PK ) 로 매핑됨
     */
	@ApiModelProperty(value = "품목 코드")
    private String ilItemCode;

	@ApiModelProperty(value = "공급처 출고처 PK")
    private long urSupplierWarehouseId;

	@ApiModelProperty(value = "재고무제한 ( true : 재고 무제한 )")
    private Boolean unlimitStockYn;

	@ApiModelProperty(value = "재고발주 ( true : 재고발주 사용 )")
	private Boolean stockPoYn;

	@ApiModelProperty(value = "선주문 여부 ( true : 선주문 사용 )")
    private String preOrderYn;

	@ApiModelProperty(value = "기본 선주문 유형 공통코드 ( PRE_ORDER_TP : NOT_ALLOWED, LIMITED_ALLOWED, UNLIMITED_ALLOWED )")
    private String defaultPreOrderType;

	@ApiModelProperty(value = "현재 선주문 유형 공통코드 ( PRE_ORDER_TP : NOT_ALLOWED, LIMITED_ALLOWED, UNLIMITED_ALLOWED )")
    private String preOrderType;

	@ApiModelProperty(value = "재고운영형태")
    private String preOrderNm;

	@ApiModelProperty(value = "발주 참조 메모")
    private String memo;

    /*
     * 재고상세정보
     */
	@ApiModelProperty(value = "유통기간")
    private String distributionPeriod;

	@ApiModelProperty(value = "품목코드")
    private String ilItemCd;

	@ApiModelProperty(value = "품목바코드")
    private String barcode;

	@ApiModelProperty(value = "품목명")
    private String itemNm;

	@ApiModelProperty(value = "상품코드")
    private String ilGoodsId;

	@ApiModelProperty(value = "상품유형명")
    private String goodsTpNm;

	@ApiModelProperty(value = "상품유형")
    private String goodsTp;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "판매상태명")
    private String saleStatusNm;

	@ApiModelProperty(value = "출고처명")
    private String warehouseNm;

	@ApiModelProperty(value = "공급처명")
    private String supplierNm;

	@ApiModelProperty(value = "보관방법명")
    private String storageMethodTpNm;

	@ApiModelProperty(value = "전일마감재고")
    private String stockClosingCount;

	@ApiModelProperty(value = "입고확정량")
    private String stockConfirmedCount;

	@ApiModelProperty(value = "입고예정량")
    private String stockScheduledCount;

	@ApiModelProperty(value = "오프라인물류재고량")
    private String wmsCount;

	@ApiModelProperty(value = "표준카테고리(대분류)")
    private String ctgryStdNm;

	@ApiModelProperty(value = "재고유형")
    private String stockTp;

	@ApiModelProperty(value = "재고유형명")
    private String stockNm;

	@ApiModelProperty(value = "재고수량")
    private String stockQty;

	@ApiModelProperty(value = "유통기한")
    private String expirationDt;

	@ApiModelProperty(value = "잔여 유통기간")
    private String restDistributionPeriod;

	@ApiModelProperty(value = "잔여 유통기간(출고기준 Dday")
    private String restDistributionDday;

	@ApiModelProperty(value = "공급처ID")
    private String urSupplierId;

	@ApiModelProperty(value = "출고처ID")
    private String urWarehouseId;

	@ApiModelProperty(value = "카테고리 레벨1")
    private String erpCtgryLv1Id;

	@ApiModelProperty(value = "마스터 품목유형")
    private String itemTp;

	@ApiModelProperty(value = "전일마감재고 합계리스트")
    private List<StockListResultVo> stockQtyList;

	@ApiModelProperty(value = "전일마감재고 합계")
    private long totalSum;


	/**
	 * 출고기한 관리
	 */
	@ApiModelProperty(value = "ERP 연동여부")
    private String name;

	@ApiModelProperty(value = "재고기한관리 ID")
    private long ilStockDeadlineId;

	@ApiModelProperty(value = "공급업체CODE")
	private String supplierCd;


	/**
	 * 상품정보
	 */
	@ApiModelProperty(value = "폐기임박상품 여부")
	private int goodsDisposalCnt;



	@ApiModelProperty(value = "D0 전일마감재고 수량")
	private int d0StockClosedQty;

	@ApiModelProperty(value = "D0 입고확정 수량")
	private int d0StockConfirmedQty;

	@ApiModelProperty(value = "D0 전일마감재고 조정수량")
	private int d0StockClosedAdjQty;

	@ApiModelProperty(value = "D0 입고예정 조정수량")
	private int d0StockScheduledAdjQty;

	@ApiModelProperty(value = "D0 입고확정 조정수량")
	private int d0StockConfirmedAdjQty;
	
	@ApiModelProperty(value = "전일마감재고 조정수량 계산")
	private int accStockClosingQty;
	
	@ApiModelProperty(value = "입고확정량 조정수량 계산")
	private int accStockConfirmedQty;
	
	@ApiModelProperty(value = "입고예정수량 조정수량 계산")
	private int accStockScheduleQty;
	
	
	
	public int getAccStockClosingQty() {
		int closingQty = StringUtil.isNumeric(this.stockClosingCount) ? Integer.parseInt(this.stockClosingCount) : 0;		
		return closingQty + this.d0StockClosedAdjQty;
	}
	
	public int getAccStockConfirmedQty() {
		int confirmedQty = StringUtil.isNumeric(this.stockConfirmedCount) ? Integer.parseInt(this.stockConfirmedCount) : 0;
		return confirmedQty + this.d0StockConfirmedAdjQty;
	}
	
	public int getAccStockScheduleQty() {
		int scheduleQty = StringUtil.isNumeric(this.stockScheduledCount) ? Integer.parseInt(this.stockScheduledCount) : 0;
		return scheduleQty + this.d0StockScheduledAdjQty;
	}
}
