package kr.co.pulmuone.mall.shopping.recently.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.v1.shopping.recently.dto.CommonGetRecentlyViewListByUserRequestDto;
import kr.co.pulmuone.v1.shopping.recently.dto.CommonGetRecentlyViewListByUserResponseDto;
import kr.co.pulmuone.v1.shopping.recently.service.ShoppingRecentlyBiz;
import kr.co.pulmuone.mall.shopping.recently.dto.GetRecentlyViewListByUserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Class 의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200831   	 	이원호            최초작성
 * =======================================================================
 * </PRE>
 */

@Service("shoppingRecentlyServiceMall")
public class ShoppingRecentlyServiceImpl implements ShoppingRecentlyService {

	@Autowired
	GoodsSearchBiz goodsSearchBiz;

    @Autowired
    ShoppingRecentlyBiz shoppingRecentlyBiz;

    @Override
    public ApiResult<?> getRecentlyViewListByUser(CommonGetRecentlyViewListByUserRequestDto dto) throws Exception {
        GetRecentlyViewListByUserResponseDto result = new GetRecentlyViewListByUserResponseDto();
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (buyerVo == null || StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(ShoppingEnums.ShoppingBasic.NEED_LOGIN);
        }
        dto.setUrUserId(Long.valueOf(buyerVo.getUrUserId()));
        dto.setDeviceInfo(DeviceUtil.getDirInfo());
        dto.setApp(DeviceUtil.isApp());
        dto.setMember(StringUtil.isNotEmpty(buyerVo.getUrUserId()));
        dto.setEmployee(StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode()));
        CommonGetRecentlyViewListByUserResponseDto goodsIdList = shoppingRecentlyBiz.getRecentlyViewListByUser(dto);

        List<GoodsSearchResultDto> sortedList = new ArrayList<>();
        if (goodsIdList.getTotal() > 0) {

        	// '상품 통합검색' 상품 ID로 조회
        	GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
        			.goodsIdList(goodsIdList.getGoodsIdList())
        			.build();

        	sortedList = goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto);
        }

        result.setTotal(goodsIdList.getTotal());
        result.setDocument(sortedList);

        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> delRecentlyViewByGoodsId(Long ilGoodsId) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
            return ApiResult.result(ShoppingEnums.ShoppingBasic.NEED_LOGIN);
        }

        shoppingRecentlyBiz.delRecentlyViewByGoodsId(ilGoodsId, Long.valueOf(buyerVo.getUrUserId()));

        return ApiResult.success();
    }
}
