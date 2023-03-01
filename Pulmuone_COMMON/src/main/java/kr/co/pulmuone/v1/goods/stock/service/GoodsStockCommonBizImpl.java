package kr.co.pulmuone.v1.goods.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.stock.dto.vo.ItemErpStockCommonVo;


@Service
public class GoodsStockCommonBizImpl implements GoodsStockCommonBiz {

	@Autowired
	GoodsStockCommonService goodsStockCommonService;

	@Override
	public ApiResult<?> addErpStock(ItemErpStockCommonVo itemErpStockCommonVo) throws Exception {
		goodsStockCommonService.addErpStock(itemErpStockCommonVo);
		return ApiResult.success();
	}

	@Override
	public ApiResult<?> addErpStockHistory(long ilItemErpStockId) throws Exception {
		goodsStockCommonService.addErpStockHistory(ilItemErpStockId);
		return ApiResult.success();
	}

	@Override
	public ApiResult<?> callSpItemStockCaculated(long ilItemWarehouseId) throws Exception {
		goodsStockCommonService.callSpItemStockCaculated(ilItemWarehouseId);
		return ApiResult.success();
	}

	@Override
	public ApiResult<?> getItemErpStockId(ItemErpStockCommonVo itemErpStockCommonVo) throws Exception {
		ItemErpStockCommonVo result = goodsStockCommonService.getItemErpStockId(itemErpStockCommonVo);
		return ApiResult.success(result);
	}

	@Override
	public ApiResult<?> putItemErpStockQty(ItemErpStockCommonVo itemErpStockCommonVo) throws Exception {
		goodsStockCommonService.putItemErpStockQty(itemErpStockCommonVo);
		return ApiResult.success();
	}

	@Override
	public ApiResult<?> putItemErpStockHistoryQty(long ilItemErpStockId) throws Exception {
		goodsStockCommonService.putItemErpStockHistoryQty(ilItemErpStockId);
		return ApiResult.success();
	}

	@Override
	public ApiResult<?> processMergeErpStock(ItemErpStockCommonVo itemErpStockCommonVo, boolean runProcedure) throws Exception {
		ApiResult<?> origData = this.getItemErpStockId(itemErpStockCommonVo);
		ItemErpStockCommonVo resultVo = (ItemErpStockCommonVo)origData.getData();
		if (resultVo != null && resultVo.getIlItemErpStockId() > 0) { // 존재하면 update
			itemErpStockCommonVo.setIlItemErpStockId(resultVo.getIlItemErpStockId());
			this.putItemErpStockQty(itemErpStockCommonVo);
			this.putItemErpStockHistoryQty(itemErpStockCommonVo.getIlItemErpStockId());// 주문생성 수량 이력저장
		}
		else { // 그렇지 않으면 insert
			this.addErpStock(itemErpStockCommonVo);
			this.addErpStockHistory(itemErpStockCommonVo.getIlItemErpStockId());// 주문생성 수량 이력저장
		}

		if (runProcedure) {
			this.callSpItemStockCaculated(itemErpStockCommonVo.getIlItemWarehouseId());// 재고수량 재계산 프로시저
		}

		return ApiResult.success();
	}

}
