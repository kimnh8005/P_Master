package kr.co.pulmuone.mall.goods.store.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStoreGoodsRequestDto;
import kr.co.pulmuone.v1.goods.store.service.GoodsStoreBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoodsStoreMallServiceImpl implements GoodsStoreMallService {

    @Autowired
    private GoodsStoreBiz goodsStoreBiz;

    @Override
    public ApiResult<?> getOrgaStorePageInfo() throws Exception {
        return ApiResult.success(goodsStoreBiz.getOrgaStorePageInfo());
    }

    @Override
    public ApiResult<?> getOrgaStoreGoods(OrgaStoreGoodsRequestDto dto) throws Exception {
        return ApiResult.success(goodsStoreBiz.getOrgaStoreGoods(dto));
    }

    @Override
    public ApiResult<?> getOrgaFlyerGoods(String discountType) throws Exception {
        return ApiResult.success(goodsStoreBiz.getOrgaFlyerGoods(discountType));
    }
}
