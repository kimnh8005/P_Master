package kr.co.pulmuone.v1.comm.mapper.goods.search;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.search.dto.GoodsSearchNewGoodsRequestDto;
import kr.co.pulmuone.v1.goods.search.dto.GoodsSearchStoreGoodsRequestDto;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchAdditionalVo;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchExperienceVo;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchHmrVo;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchOutMallVo;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsSearchMapper {

    GoodsSearchExperienceVo getGoodsSearchExperience(Long ilGoodsId) throws Exception;

    List<GoodsSearchResultDto> searchGoodsByGoodsIdList(GoodsSearchByGoodsIdRequestDto reqDto) throws Exception;

    Page<Long> getDailyGoods(String getDailyGoods) throws Exception;

    List<GoodsSearchOutMallVo> getGoodsFromOutMall(@Param("goodsIdList") List<String> goodsIdList) throws Exception;

    List<GoodsSearchOutMallVo> getGoodsFromOutMall_PULSHOP(@Param("goodsIdList") List<String> goodsIdList) throws Exception;

    List<Long> getNewGoods(GoodsSearchNewGoodsRequestDto dto);

    List<GoodsSearchAdditionalVo> getGoodsAdditional(@Param("goodsIdList") List<Long> goodsIdList) throws Exception;

    List<String> getGoodsCouponCoverageByUrWareHouseId(@Param("wareHouseIdList") List<Long> wareHouseIdList) throws Exception;

    List<GoodsSearchHmrVo> getGoodsSearchHmr(GoodsSearchStoreGoodsRequestDto dto) throws Exception;

    Page<Long> getGoodsSearchShopOnly(GoodsSearchStoreGoodsRequestDto dto) throws Exception;

    String getPsConfig(@Param("psKey") String psKey);

    Page<Long> getOrgaFlyerGoods(String displayType) throws Exception;

}
