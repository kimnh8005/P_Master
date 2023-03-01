package kr.co.pulmuone.v1.goods.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.goods.stock.ItemErpStockCommonMapper;
import kr.co.pulmuone.v1.goods.stock.dto.vo.ItemErpStockCommonVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoodsStockCommonService {

	@Autowired
	private final ItemErpStockCommonMapper itemErpStockCommonMapper;


	/**
	 * @Desc ERP 품목에 주문수량 저장
	 * @param ItemErpStockCommonVo
	 * @return int
	 */
	protected int addErpStock(ItemErpStockCommonVo resultVo) {
		return itemErpStockCommonMapper.addItemErpStock(resultVo);
	}

	/**
	 * @Desc ERP 품목에 주문수량 이력저장
	 * @param long
	 * @return int
	 */
	protected int addErpStockHistory(long ilItemErpStockId) {
		return itemErpStockCommonMapper.addItemErpStockHistory(ilItemErpStockId);
	}

	/**
	 * @Desc 재고수량 재계산 프로시저
	 * @param long
	 * @return int
	 */
	protected int callSpItemStockCaculated(long ilItemWarehouseId) {
		return itemErpStockCommonMapper.callSpItemStockCaculated(ilItemWarehouseId);
	}

	/**
	 * @Desc 기준일/재고분류에 의한 ERP 재고 ID
	 * @param ItemErpStockCommonVo
	 * @return ItemErpStockCommonVo
	 */
	protected ItemErpStockCommonVo getItemErpStockId(ItemErpStockCommonVo itemErpStockCommonVo) {
		return itemErpStockCommonMapper.getItemErpStockId(itemErpStockCommonVo);
	}

	/**
	 * @Desc ERP 재고 수량 수정
	 * @param ItemErpStockCommonVo
	 * @return int
	 */
	protected int putItemErpStockQty(ItemErpStockCommonVo itemErpStockCommonVo) {
		return itemErpStockCommonMapper.putItemErpStockQty(itemErpStockCommonVo);
	}

	/**
	 * @Desc ERP 재고이력 수정
	 * @param long
	 * @return int
	 */
	protected int putItemErpStockHistoryQty(long ilItemErpStockId) {
		return itemErpStockCommonMapper.putItemErpStockHistoryQty(ilItemErpStockId);
	}

}
