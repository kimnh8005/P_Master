package kr.co.pulmuone.v1.display.contents.service;

import kr.co.pulmuone.v1.display.contents.dto.*;
import kr.co.pulmuone.v1.display.contents.dto.vo.ContentsDetailVo;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;

import java.util.List;

public interface DisplayContentsBiz {

    List<InventoryContentsInfoResponseDto> getInventoryContentsInfoList(String pageCd, String deviceType, String userType) throws Exception;

    List<InventoryCategoryResponseDto> getCategoryInfo(String inventoryCd, String deviceType, String userType) throws Exception;

    ContentsInfoResponseDto getContentsInfo(Long dpContsId, String deviceType, String userType, String goodsSortCode) throws Exception;

    LohasBannerResponseDto getLohasBanner(LohasBannerRequestDto dto) throws Exception;

    List<ContentsDetailVo> getContentsLevel1ByInventoryCd(String inventoryCd, String deviceType) throws Exception;

    List<?> getContentsInfoByInventoryCd(String inventoryCd, String deviceType) throws Exception;

    boolean isDealGoods(Long ilGoodsId) throws Exception;

    List<GoodsSearchResultDto> getBestGoods(BestGoodsRequestDto dto) throws Exception;

    List<ContentsDetailBannerResponseDto> getOrgaStoreBanner(String deviceType) throws Exception;

}