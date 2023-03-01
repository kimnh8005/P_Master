package kr.co.pulmuone.mall.shopping.restock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.shopping.restock.service.ShoppingRestockBiz;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShoppingRestockServiceImpl implements ShoppingRestockService {

	@Autowired
	public ShoppingRestockBiz shoppingRestockBiz;

	/**
	 * 재입고 알림 요청
	 *
	 * @param ilGoodsId
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> applyRestockNotice(Long ilGoodsId) throws Exception {
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(ShoppingEnums.ShoppingBasic.NEED_LOGIN);
        } else {
        	shoppingRestockBiz.putRetockInfo(ilGoodsId, buyerVo.getUrUserId());
    		return ApiResult.success();
        }
	}
}
