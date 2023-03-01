package kr.co.pulmuone.v1.shopping.restock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingRestockBizImpl implements ShoppingRestockBiz {

	@Autowired
	private ShoppingRestockService shoppingRestockService;

	@Override
	public int putRetockInfo(Long ilGoodsId, String urUserId) throws Exception {
		return shoppingRestockService.putRetockInfo(ilGoodsId, urUserId);
	}
}
