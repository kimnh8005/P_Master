package kr.co.pulmuone.v1.goods.stock.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.goods.stock.dto.StockOrderRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockOrderResultVo;

class GoodsStockOrderServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
    private GoodsStockOrderService goodsStockOrderService;

	@Test
	void 품목별_출고처ID_조회_성공() {

		StockOrderRequestDto dto = new StockOrderRequestDto();

		dto.setIlGoodsId(1);//상품ID

		StockOrderResultVo resultVo = goodsStockOrderService.getIlItemWarehouseIdInfo(dto);

		assertTrue(resultVo != null && resultVo.getIlItemWarehouseId() > 0);
	}

	@Test
	void 주문수량_저장_성공() {
		StockOrderResultVo resultVo = new StockOrderResultVo();

		resultVo.setIlItemWarehouseId(825);//품목별 출고처ID
		resultVo.setStockTp("ERP_STOCK_TP.ORDER");//재고타입
		resultVo.setOrderQty(30);//주문수량
		resultVo.setScheduleDt("2021-02-04");//예정일

		int cnt = goodsStockOrderService.addErpStockOrder(resultVo);

		assertTrue(cnt > 0);
	}

	@Test
	void 재고수량_재계산_프로시저_성공() {
		StockOrderResultVo resultVo = new StockOrderResultVo();

		resultVo.setIlItemWarehouseId(825);//품목별 출고처ID

		int cnt = goodsStockOrderService.spItemStockCaculated(resultVo);

		assertTrue(cnt > 0);
	}

	@Test
	void 미연동_재고수량_조회_성공() {
		StockOrderResultVo resultVo = new StockOrderResultVo();

		resultVo.setIlItemWarehouseId(281);//품목별 출고처ID

		int cnt = goodsStockOrderService.getNotIfStockCnt(resultVo);

		assertTrue(cnt > 0);
	}

	@Test
	void 미연동_재고수량_수정_성공() {
		StockOrderResultVo resultVo = new StockOrderResultVo();

		resultVo.setIlItemWarehouseId(825);//품목별 출고처ID
		resultVo.setOrderQty(30);//주문수량

		int cnt = goodsStockOrderService.putNotIfStockCnt(resultVo);

		assertTrue(cnt > 0);
	}

}
