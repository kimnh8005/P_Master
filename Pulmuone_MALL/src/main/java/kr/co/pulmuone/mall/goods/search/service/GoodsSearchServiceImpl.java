package kr.co.pulmuone.mall.goods.search.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.category.dto.GetCategoryResultDto;
import kr.co.pulmuone.v1.goods.category.service.GoodsCategoryBiz;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchRequestDto;
import kr.co.pulmuone.v1.search.searcher.dto.SearchResultDto;
import kr.co.pulmuone.v1.search.searcher.dto.SuggestionSearchRequestDto;
import kr.co.pulmuone.v1.search.searcher.dto.SuggestionSearchResultDto;
import kr.co.pulmuone.v1.search.searcher.service.SearchBizImpl;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.goods.category.dto.GetCategoryGoodsListRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200923   	 천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class GoodsSearchServiceImpl implements GoodsSearchService
{

    @Autowired
    private GoodsCategoryBiz goodsCategoryBiz;

	@Autowired
	private SearchBizImpl searchBiz;

	@Autowired
	private UserBuyerBiz userBuyerBiz;

	/**
	 * 카테고리별 상품 리스트
	 *
	 * @param goodsSearchRequestDto
	 * @return GetSearchgoodsListResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getCategoryGoodsList(GetCategoryGoodsListRequestDto getCategorygoodsListRequestDto)throws Exception
	{
		String ilCategoryId = StringUtil.nvl(getCategorygoodsListRequestDto.getIlCategoryId());
		if (StringUtil.isEmpty(ilCategoryId))
		{
			return ApiResult.fail();
		}

		// 파라미터로 넘어온 ilCategoryId의 depth 정보 확인
		GetCategoryResultDto getCategoryResultDto = goodsCategoryBiz.getCategory(new Long(ilCategoryId));
		if (getCategoryResultDto == null)
		{
			return ApiResult.fail();
		}

		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        boolean isEmployee = (buyerVo != null) && StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());

		// 검색엔진연동 요청파라미터 세팅
		GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder().isEmployee(isEmployee).lev1CategoryId(getCategoryResultDto.getLev1CategoryId()).lev2CategoryId(getCategoryResultDto.getLev2CategoryId()).lev3CategoryId(getCategoryResultDto.getLev3CategoryId()).isFirstSearch(getCategorygoodsListRequestDto.isFirstSearch()).page(getCategorygoodsListRequestDto.getPage()).limit(getCategorygoodsListRequestDto.getLimit()).excludeSoldOutGoods(getCategorygoodsListRequestDto.isExcludeSoldOutGoods()).benefitTypeIdList(getCategorygoodsListRequestDto.getBenefitTypeIdList()).brandIdList(getCategorygoodsListRequestDto.getBrandIdList()).deliveryTypeIdList(getCategorygoodsListRequestDto.getDeliveryTypeIdList()).certificationTypeIdList(getCategorygoodsListRequestDto.getCertificationTypeIdList()).storageMethodIdList(getCategorygoodsListRequestDto.getStorageMethodIdList()).minimumPrice(getCategorygoodsListRequestDto.getMinimumPrice()).maximumPrice(getCategorygoodsListRequestDto.getMaximumPrice()).sortCode(getCategorygoodsListRequestDto.getSortCode()).build();

		// '상품 통합검색' 검색엔진 연동
		SearchResultDto searchResultDto = searchBiz.searchGoods(goodsSearchRequestDto);

		return ApiResult.success(searchResultDto);
	}


	/**
	 * 자동완성
	 *
	 * @param	keyword
	 * @param	limit
	 * @return	SuggestionSearchResultDto
	 * @throws	Exception
	 */
	@Override
    public ApiResult<?> getAutoCompleteList(@RequestParam(value = "keyword", required = true) String keyword, @RequestParam(value = "limit", required = false) Integer limit) throws Exception {

    	// '검색어 추천(자동완성)' 검색엔진 연동
    	SuggestionSearchRequestDto suggestionSearchRequestDto = SuggestionSearchRequestDto.builder().keyword(keyword).limit(limit).build();

    	SuggestionSearchResultDto searchResultDto =  searchBiz.searchSuggestion(suggestionSearchRequestDto);

		return ApiResult.success(searchResultDto);
    }


	/**
	 * 상품검색
	 *
	 * @param goodsSearchRequestDto
	 * @return ResponseEntity<GetSearchGoodsListResponseDto>
	 * @throws Exception
	 */
	@Override
    public ApiResult<?> getSearchGoodsList(GoodsSearchRequestDto goodsSearchRequestDto) throws Exception {

		if (!goodsSearchRequestDto.hasKeyword()) {
			return ApiResult.fail();
		}

		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        boolean isEmployee = (buyerVo != null) && StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());

		// '상품 통합검색' 검색엔진 연동
        goodsSearchRequestDto.setIsEmployee(isEmployee);
		goodsSearchRequestDto.decodeKeyword();
		SearchResultDto searchResultDto = searchBiz.searchGoods(goodsSearchRequestDto);

		// 로그인 되어 있으면 회원 최근검색어테이블에 추가
		if(goodsSearchRequestDto.getIsSaveKeyword().equals("true")){
			if (StringUtil.isNotEmpty(buyerVo.getUrUserId())) {
				String urUserId = buyerVo.getUrUserId();
				userBuyerBiz.addUserSearchWordLog(goodsSearchRequestDto.getKeyword(), urUserId);
			}
		}

		return ApiResult.success(searchResultDto);


    }
}
