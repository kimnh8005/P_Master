package kr.co.pulmuone.bos.display.search.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.search.indexer.service.IndexBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchIndexController {

    @Autowired
    private IndexBiz indexBiz;


    /**
     * 상품 색인
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/dp/searchIndex/callItemIndex")
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" +
                    "[GOODS_INDEX_FAIL] INDEX_FAIL - 상품 색인이 실패했습니다. \n" +
                    "[GOODS_INDEX_SUCCESS] INDEX_SUCCESS - 상품 색인이 정상적으로 완료되었습니다."
            )
    })
    public ApiResult<?> callItemIndex() throws Exception {
        return indexBiz.indexGoods();
    }


    /**
     * 자동완성 색인
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/dp/searchIndex/callItemSuggestionIndex")
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" +
                    "[SUGGESTION_INDEX_FAIL] INDEX_FAIL - 자동완성 색인이 실패했습니다. \n" +
                    "[SUGGESTION_INDEX_SUCCESS] INDEX_SUCCESS - 자동완성 색인이 정상적으로 완료되었습니다."
            )
    })
    public ApiResult<?> callItemSuggestionIndex() throws Exception {
        return indexBiz.indexGoodsSuggestion();
    }

    /**
     * 이벤트/기획전 자동완성 색인
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/dp/searchIndex/callPromotionSuggestionIndex")
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" +
                    "[PROMOTION_SUGGESTION_INDEX_FAIL] INDEX_FAIL - 이벤트/기획전 자동완성 색인이 실패했습니다.  \n" +
                    "[PROMOTION_SUGGESTION_INDEX_SUCCESS] INDEX_SUCCESS - 이벤트/기획전 자동완성 색인이 정상적으로 완료되었습니다."
            )
    })
    public ApiResult<?> callPromotionSuggestionIndex() throws Exception {
        return indexBiz.indexPromotionSuggestion();
    }

    /**
     * 매장 상품 색인
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/admin/dp/searchIndex/callStoreItemIndex")
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" +
                    "[STORE_GOODS_INDEX_FAIL] INDEX_FAIL - 매장 상품 색인이 실패했습니다. \n" +
                    "[STORE_GOODS_INDEX_SUCCESS] INDEX_SUCCESS - 매장 상품 색인이 정상적으로 완료되었습니다."
            )
    })
    public ApiResult<?> callStoreItemIndex() throws Exception {
        return indexBiz.indexStoreGoods();
    }
}
