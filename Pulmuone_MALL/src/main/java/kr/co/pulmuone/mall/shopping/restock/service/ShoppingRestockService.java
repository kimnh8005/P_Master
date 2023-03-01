package kr.co.pulmuone.mall.shopping.restock.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;

public interface ShoppingRestockService {

	/**
	 * 재입고 알림 요청
	 *
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> applyRestockNotice(Long ilGoodsId) throws Exception;
}
