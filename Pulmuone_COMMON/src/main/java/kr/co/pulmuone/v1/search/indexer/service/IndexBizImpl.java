package kr.co.pulmuone.v1.search.indexer.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.SearchEnums.IndexMessage;
import kr.co.pulmuone.v1.search.indexer.dto.IndexResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexBizImpl implements IndexBiz {

    @Autowired
    private GoodsIndexService goodsIndexService;

    @Autowired
    private GoodsSuggestionIndexService goodsSuggestionIndexService;

    @Autowired
    private PromotionSuggestionIndexService promotionSuggestionIndexService;

    @Autowired
    private CategoryBoostIndexService categoryBoostIndexService;

    @Autowired
    private StoreGoodsIndexService storeGoodsIndexService;

    @Override
    public ApiResult<?> indexGoods() throws Exception {
        IndexResultDto indexResultDto = goodsIndexService.bulk();

        if (indexResultDto.isSuccess())
            return ApiResult.success(IndexMessage.GOODS_INDEX_SUCCESS);

        return ApiResult.result(IndexMessage.GOODS_INDEX_FAIL);

    }

    @Override
    public ApiResult<?> indexGoodsSuggestion() throws Exception {
        IndexResultDto indexResultDto = goodsSuggestionIndexService.bulk();
        if (indexResultDto.isSuccess())
            return ApiResult.success(IndexMessage.SUGGESTION_INDEX_SUCCESS);

        return ApiResult.result(IndexMessage.SUGGESTION_INDEX_FAIL);
    }

    @Override
    public ApiResult<?> indexPromotionSuggestion() throws Exception {
        IndexResultDto indexResultDto = promotionSuggestionIndexService.bulk();
        if (indexResultDto.isSuccess())
            return ApiResult.success(IndexMessage.PROMOTION_SUGGESTION_INDEX_SUCCESS);

        return ApiResult.result(IndexMessage.PROMOTION_SUGGESTION_INDEX_FAIL);
    }


    @Override
    public ApiResult<?> indexCategoryBoost() throws Exception {
        IndexResultDto indexResultDto = categoryBoostIndexService.bulk();
        if (indexResultDto.isSuccess())
            return ApiResult.success(IndexMessage.SUGGESTION_INDEX_SUCCESS);

        return ApiResult.result(IndexMessage.SUGGESTION_INDEX_FAIL);

    }

    @Override
    public ApiResult<?> indexStoreGoods() throws Exception {
        IndexResultDto indexResultDto = storeGoodsIndexService.bulk();

        if (indexResultDto.isSuccess())
            return ApiResult.success(IndexMessage.STORE_GOODS_INDEX_SUCCESS);

        return ApiResult.result(IndexMessage.STORE_GOODS_INDEX_FAIL);
    }
}
