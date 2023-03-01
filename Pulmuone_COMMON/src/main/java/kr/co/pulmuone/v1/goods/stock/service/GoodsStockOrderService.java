package kr.co.pulmuone.v1.goods.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.goods.stock.GoodsStockOrderMapper;
import kr.co.pulmuone.v1.goods.stock.dto.StockOrderRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockOrderResultVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoodsStockOrderService {

	@Autowired
	private final GoodsStockOrderMapper goodsStockOrderMapper;

	/**
	 * @Desc 상품ID로 품목별 출고처ID 조회
	 * @param StockOrderRequestDto
	 * @return StockOrderResultVo
	 */
	protected StockOrderResultVo getIlItemWarehouseIdInfo(StockOrderRequestDto dto) {
		return goodsStockOrderMapper.getIlItemWarehouseIdInfo(dto);
	}

	/**
	 * @Desc ERP 품목에 주문수량 저장
	 * @param StockOrderResultVo
	 * @return long
	 */
	protected int addErpStockOrder(StockOrderResultVo resultVo) {
		return goodsStockOrderMapper.addErpStockOrder(resultVo);
	}

	/**
	 * @Desc ERP 품목에 주문수량 이력저장
	 * @return int
	 */
	protected int addErpStockOrderHistory(long ilItemErpStockId) {
		return goodsStockOrderMapper.addErpStockOrderHistory(ilItemErpStockId);
	}

	/**
	 * @Desc 재고수량 재계산 프로시저
	 * @param StockOrderResultVo
	 * @return int
	 */
	protected int spItemStockCaculated(StockOrderResultVo resultVo) {
		return goodsStockOrderMapper.spItemStockCaculated(resultVo);
	}

	/**
	 * @Desc 미연동 재고 수량 조회
	 * @param StockOrderResultVo
	 * @return int
	 */
	protected int getNotIfStockCnt(StockOrderResultVo resultVo) {
		return goodsStockOrderMapper.getNotIfStockCnt(resultVo);
	}

	/**
	 * @Desc 미연동 재고 수량 수정
	 * @param StockOrderResultVo
	 * @return int
	 */
	protected int putNotIfStockCnt(StockOrderResultVo resultVo) {
		return goodsStockOrderMapper.putNotIfStockCnt(resultVo);
	}

}
