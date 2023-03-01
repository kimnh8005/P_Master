package kr.co.pulmuone.v1.comm.mappers.batch.master.goods.stock;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.batch.goods.stock.dto.BatchItemStockByGoodsSaleStatusReqeustDto;

@Mapper
public interface BatchItemStockByGoodsSaleStatusMapper {

	/**
	 * 묶음상품 제외
	 * 1. 재고 연동, 선주문가능(한정재고), 선주문 불가인 IL_ITEM_STOCK.BASE_DT가 오늘날짜인 IL_ITEM_STOCK.D0_ORDER_QTY ~ IL_ITEM_STOCK.D15_ORDER_QTY의 합계 수량이 0 인 상품 ID 갯수
	 * 2. 재고 연동, 선주문가능(한정재고), 선주문 불가인 IL_ITEM_STOCK.BASE_DT가 오늘날짜인 IL_ITEM_STOCK.D0_ORDER_QTY ~ IL_ITEM_STOCK.D15_ORDER_QTY의 합계 수량이 1 이상인 상품 ID 갯수
	 */
	int itemErpStockByGoodsSaleStatusChangeNum(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto);
	/**
	 * 묶음상품 제외
	 * 1. 재고 연동, 선주문가능(한정재고), 선주문 불가인 IL_ITEM_STOCK.BASE_DT가 오늘날짜인 IL_ITEM_STOCK.D0_ORDER_QTY ~ IL_ITEM_STOCK.D15_ORDER_QTY의 합계 수량이 0 인 상품 판매 상태 변경
	 * 2. 재고 연동, 선주문가능(한정재고), 선주문 불가인 IL_ITEM_STOCK.BASE_DT가 오늘날짜인 IL_ITEM_STOCK.D0_ORDER_QTY ~ IL_ITEM_STOCK.D15_ORDER_QTY의 합계 수량이 1 이상인 상품 판매 상태 변경
	 */
	int itemErpStockByGoodsSaleStatusChangeUpdate(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto);

	/**
	 * 묶음상품 대상
	 * 재고 연동, 선주문가능(한정재고), 선주문 불가인 IL_ITEM_STOCK.BASE_DT가 오늘날짜인 IL_ITEM_STOCK.D0_ORDER_QTY ~ IL_ITEM_STOCK.D15_ORDER_QTY의 합계 수량이 0 인 상품 ID 갯수
	 */
	int itemErpStockByGoodsPackageSaleStatusOutOfStockBySystemNum(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto);
	/**
	 * 묶음상품 대상
	 * 재고 연동, 선주문가능(한정재고), 선주문 불가인 IL_ITEM_STOCK.BASE_DT가 오늘날짜인 IL_ITEM_STOCK.D0_ORDER_QTY ~ IL_ITEM_STOCK.D15_ORDER_QTY의 합계 수량이 0 인 상품 판매 상태 변경
	 */
	int itemErpStockByGoodsPackageSaleStatusOutOfStockBySystemUpdate(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto);
	/**
	 * 묶음상품 대상
	 * 재고 연동, 선주문가능(한정재고), 선주문 불가인 IL_ITEM_STOCK.BASE_DT가 오늘날짜인 IL_ITEM_STOCK.D0_ORDER_QTY ~ IL_ITEM_STOCK.D15_ORDER_QTY의 합계 수량이 1 이상인 상품 갯수
	 */
	Integer itemErpStockByGoodsPackageSaleStatusOnSaleNum(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto);
	/**
	 * 묶음상품 대상
	 * 재고 연동, 선주문가능(한정재고), 선주문 불가인 IL_ITEM_STOCK.BASE_DT가 오늘날짜인 IL_ITEM_STOCK.D0_ORDER_QTY ~ IL_ITEM_STOCK.D15_ORDER_QTY의 합계 수량이 1 이상인 상품 판매 상태 변경
	 * 묶음상품의 모든 구성품(증정품 포함)의 수량 상태를 보고 모두 다 1 이상이어야 판매중으로 변경 처리
	 */
	int itemErpStockByGoodsPackageSaleStatusOnSaleUpdate(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto);



	/**
	 * 묶음상품 제외
	 * 1. 재고미연동(한정재고)인 IL_ITEM_WAREHOUSE.NOT_IF_STOCK_CNT이 0 인 상품 ID 갯수 확인
	 * 2. 재고미연동(한정재고)인 IL_ITEM_WAREHOUSE.NOT_IF_STOCK_CNT이 1 이상인 상품 ID 갯수 확인
	 */
	int itemStockByGoodsSaleStatusChangeNum(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto);
	/**
	 * 묶음상품 제외
	 * 1. 재고미연동(한정재고)인 IL_ITEM_WAREHOUSE.NOT_IF_STOCK_CNT이 0 인 상품 판매상태 변경
	 * 2. 재고미연동(한정재고)인 IL_ITEM_WAREHOUSE.NOT_IF_STOCK_CNT이 1 이상인 상품 판매상태 변경
	 */
	int itemStockByGoodsSaleStatusChangeUpdate(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto);

	/**
	 * 묶음상품 대상
	 * 재고미연동(한정재고)인 IL_ITEM_WAREHOUSE.NOT_IF_STOCK_CNT이 0 인 상품 ID 갯수 확인
	 */
	int itemStockByGoodsPackageSaleStatusOutOfStockBySystemNum(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto);
	/**
	 * 묶음상품 대상
	 * 재고미연동(한정재고)인 IL_ITEM_WAREHOUSE.NOT_IF_STOCK_CNT이 0 인 상품 판매상태 변경
	 */
	int itemStockByGoodsPackageSaleStatusOutOfStockBySystemUpdate(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto);
	/**
	 * 묶음상품 대상
	 * 재고미연동(한정재고)인 IL_ITEM_WAREHOUSE.NOT_IF_STOCK_CNT이 0 인 상품 ID 갯수 확인
	 */
	Integer itemStockByGoodsPackageSaleStatusOnSaleNum(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto);
	/**
	 * 묶음상품 대상
	 * 재고미연동(한정재고)인 IL_ITEM_WAREHOUSE.NOT_IF_STOCK_CNT이 1이상인 상품 판매상태 변경
	 * 묶음상품의 모든 구성품(증정품 포함)의 수량 상태를 보고 모두 다 1 이상이어야 판매중으로 변경 처리
	 */
	int itemStockByGoodsPackageSaleStatusOnSaleUpdate(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto);
	
	/**
	 * 묶음상품 대상
	 * 묶음상품의 구성품중에 하나라도 '판매중' 상태가 아닌 묶음상품 갯수
	 */
	Integer goodsSaleStatusByGoodsPackageSaleStatusNum(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto);
	
	/**
	 * 묶음상품 대상
	 * 묶음상품의 구성품중에 하나라도 '판매중' 상태가 아니라면 묶음상품도 '판매중지(시스템)' 처리
	 */
	int goodsSaleStatusByGoodsPackageSaleStatusUpdate(BatchItemStockByGoodsSaleStatusReqeustDto batchItemStockByGoodsSaleStatusReqeustDto);
}
