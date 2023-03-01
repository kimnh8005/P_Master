package kr.co.pulmuone.v1.display.contents.service;

import kr.co.pulmuone.v1.display.contents.dto.*;
import kr.co.pulmuone.v1.display.contents.dto.vo.ContentsDetailVo;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisplayContentsBizImpl implements DisplayContentsBiz {

    @Autowired
    private DisplayContentsService displayContentsService;

    @Override
    public List<InventoryContentsInfoResponseDto> getInventoryContentsInfoList(String pageCd, String deviceType, String userType) throws Exception {
        return displayContentsService.getInventoryContentsInfoList(pageCd, deviceType, userType);
    }

    @Override
    public List<InventoryCategoryResponseDto> getCategoryInfo(String inventoryCd, String deviceType, String userType) throws Exception {
        return displayContentsService.getCategoryInfo(inventoryCd, deviceType, userType);
    }

    @Override
    public ContentsInfoResponseDto getContentsInfo(Long dpContsId, String deviceType, String userType, String goodsSortCode) throws Exception {
        return displayContentsService.getContentsInfo(dpContsId, deviceType, userType, goodsSortCode);
    }

    @Override
    public LohasBannerResponseDto getLohasBanner(LohasBannerRequestDto dto) throws Exception {
        return displayContentsService.getLohasBanner(dto);
    }

    @Override
    public List<ContentsDetailVo> getContentsLevel1ByInventoryCd(String inventoryCd, String deviceType) throws Exception {
        return displayContentsService.getContentsLevel1ByInventoryCd(inventoryCd, deviceType);
    }

    @Override
    public List<?> getContentsInfoByInventoryCd(String inventoryCd, String deviceType) throws Exception {
        List<ContentsDetailVo> contentsList = displayContentsService.getContentsLevel1ByInventoryCd(inventoryCd, deviceType);
        return displayContentsService.contentsDetailVoToResponseDto(contentsList, deviceType);
    }

    @Override
    public boolean isDealGoods(Long ilGoodsId) throws Exception {
        return displayContentsService.isDealGoods(ilGoodsId);
    }

    @Override
    public List<GoodsSearchResultDto> getBestGoods(BestGoodsRequestDto dto) throws Exception {
        return displayContentsService.getBestGoods(dto);
    }

    @Override
    public List<ContentsDetailBannerResponseDto> getOrgaStoreBanner(String deviceType) throws Exception {
        return displayContentsService.getOrgaStoreBanner(deviceType);
    }

}