package kr.co.pulmuone.v1.goods.search;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.search.dto.GoodsSearchNewGoodsRequestDto;
import kr.co.pulmuone.v1.goods.search.dto.GoodsSearchStoreGoodsRequestDto;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchAdditionalVo;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchExperienceVo;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchHmrVo;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchOutMallVo;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsSearchBiz {

    GoodsSearchExperienceVo getGoodsSearchExperience(Long ilGoodsId) throws Exception;

    List<GoodsSearchResultDto> searchGoodsByGoodsIdList(GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto) throws Exception;

    List<Long> getDailyGoods(String goodsDailyType) throws Exception;

    List<GoodsSearchOutMallVo> getGoodsFromOutMall(List<String> goodsIdList) throws Exception;

    List<Long> getNewGoods(GoodsSearchNewGoodsRequestDto dto);

    List<GoodsSearchAdditionalVo> getGoodsAdditional(List<Long> goodsIdList) throws Exception;

    List<String> getGoodsCouponCoverageByUrWareHouseId(List<Long> wareHouseIdList) throws Exception;

    List<GoodsSearchHmrVo> getGoodsSearchHmr(GoodsSearchStoreGoodsRequestDto dto) throws Exception;

    Page<Long> getGoodsSearchShopOnly(GoodsSearchStoreGoodsRequestDto dto) throws Exception;

    Page<Long> getOrgaFlyerGoods(String discountType) throws Exception;

}
