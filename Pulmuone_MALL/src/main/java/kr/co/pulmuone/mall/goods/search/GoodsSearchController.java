package kr.co.pulmuone.mall.goods.search;

import io.swagger.annotations.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchRequestDto;
import kr.co.pulmuone.v1.search.searcher.dto.SearchResultDto;
import kr.co.pulmuone.v1.search.searcher.dto.SuggestionSearchResultDto;
import kr.co.pulmuone.mall.goods.search.service.GoodsSearchService;
import kr.co.pulmuone.v1.goods.category.dto.GetCategoryGoodsListRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.GetSearchGoodsListResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200923   	 	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class GoodsSearchController {

    @Autowired
    public GoodsSearchService goodsSearchService;


	/**
	 * 카테고리별 상품 리스트
	 *
	 * @param getCategoryGoodsListRequestDto
	 * @return ResponseEntity<GetSearchGoodsListResponseDto>
	 * @throws Exception
	 */
    @PostMapping(value = "/goods/search/getCategoryGoodsList")
    @ApiOperation(value = "카테고리별 상품 리스트")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetSearchGoodsListResponseDto.class)
    })
    public ApiResult<?> getCategoryGoodsList(GetCategoryGoodsListRequestDto getCategoryGoodsListRequestDto) throws Exception {

        return goodsSearchService.getCategoryGoodsList(getCategoryGoodsListRequestDto);
    }


	/**
	 * 자동완성
	 *
	 * @param	keyword
	 * @param	limit
	 * @return	SuggestionSearchResultDto
	 * @throws	Exception
	 */
    @PostMapping(value = "/goods/search/getAutoCompleteList")
    @ApiOperation(value = "자동완성")
	@ApiImplicitParams({ @ApiImplicitParam(name = "keyword", value = "검색 키워드", required = true, dataType = "String"), @ApiImplicitParam(name = "limit", value = "limit", required = false, dataType = "Integer") })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = SuggestionSearchResultDto.class)
    })
    public ApiResult<?> getAutoCompleteList(@RequestParam(value = "keyword", required = true) String keyword, @RequestParam(value = "limit", required = false) Integer limit) throws Exception {

		return goodsSearchService.getAutoCompleteList(keyword, limit);
    }


	/**
	 * 상품검색
	 *
	 * @param 	goodsSearchRequestDto
	 * @return	SearchResultDto
	 * @throws	Exception
	 */
    @PostMapping(value = "/goods/search/getSearchGoodsList")
    @ApiOperation(value = "상품검색")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = SearchResultDto.class)
    })
    public ApiResult<?> getSearchGoodsList(GoodsSearchRequestDto goodsSearchRequestDto) throws Exception {
    	return goodsSearchService.getSearchGoodsList(goodsSearchRequestDto);
    }



}