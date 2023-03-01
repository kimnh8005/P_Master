package kr.co.pulmuone.mall.shopping.favorites.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.v1.shopping.favorites.dto.CommonGetFavoritesGoodsListByUserRequestDto;
import kr.co.pulmuone.v1.shopping.favorites.dto.CommonGetFavoritesGoodsListByUserResponseDto;
import kr.co.pulmuone.v1.shopping.favorites.dto.ShoppingFavoritesDto;
import kr.co.pulmuone.v1.shopping.favorites.service.ShoppingFavoritesBiz;
import kr.co.pulmuone.mall.shopping.favorites.dto.GetFavoritesGoodsListByUserResponseDto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200826   	 천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class ShoppingFavoritesServiceImpl implements ShoppingFavoritesService {

    @Autowired
    public ShoppingFavoritesBiz shoppingFavoritesBiz;

	@Autowired
	GoodsSearchBiz goodsSearchBiz;

    /**
     * 찜 추가
     *
     * @param ilGoodsId
     * @throws Exception
     */
    @Override
    public ApiResult<?> addGoodsFavorites(Long ilGoodsId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(ShoppingEnums.ShoppingBasic.NEED_LOGIN);
        }

        ShoppingFavoritesDto shoppingFavoritesDto = shoppingFavoritesBiz.addGoodsFavorites(ilGoodsId, buyerVo.getUrUserId());

        return ApiResult.success(shoppingFavoritesDto);
    }


    /**
     * 찜 삭제
     *
     * @param    spFavoritesGoodsId
     * @throws Exception
     */
    @Override
    public ApiResult<?> delGoodsFavorites(Long spFavoritesGoodsId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(ShoppingEnums.ShoppingBasic.NEED_LOGIN);
        }

        shoppingFavoritesBiz.delGoodsFavorites(spFavoritesGoodsId, buyerVo.getUrUserId());

        return ApiResult.success();
    }

    /**
     * 찜 상품조회
     *
     * @return GetFavoritesGoodsListByUserResponseDto
     * @param    dto CommonGetFavoritesGoodsListByUserRequestDto
     * @throws Exception exception
     */
    @Override
    public ApiResult<?> getFavoritesGoodsListByUser(CommonGetFavoritesGoodsListByUserRequestDto dto) throws Exception {
        GetFavoritesGoodsListByUserResponseDto result = new GetFavoritesGoodsListByUserResponseDto();
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(ShoppingEnums.ShoppingBasic.NEED_LOGIN);
        }
        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        dto.setDeviceInfo(DeviceUtil.getDirInfo());
        dto.setApp(DeviceUtil.isApp());
        dto.setMember(StringUtil.isNotEmpty(buyerVo.getUrUserId()));
        dto.setEmployee(StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode()));
        CommonGetFavoritesGoodsListByUserResponseDto goodsIdList = shoppingFavoritesBiz.getFavoritesGoodsListByUser(dto);

        if (goodsIdList.getTotal() > 0) {

        	// '상품 통합검색' 상품ID로 조회
        	GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto  = GoodsSearchByGoodsIdRequestDto.builder()
        			.goodsIdList(goodsIdList.getGoodsIdList())
        			.build();

        	List<GoodsSearchResultDto> searchGoodsList = goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto);
            result.setDocument(searchGoodsList);
        }
        result.setTotal(goodsIdList.getTotal());

        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> delFavoritesGoodsByGoodsId(Long ilGoodsId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(ShoppingEnums.ShoppingBasic.NEED_LOGIN);
        }

        shoppingFavoritesBiz.delFavoritesGoodsByGoodsId(ilGoodsId, Long.valueOf(buyerVo.getUrUserId()));
        return ApiResult.success();
    }
}
