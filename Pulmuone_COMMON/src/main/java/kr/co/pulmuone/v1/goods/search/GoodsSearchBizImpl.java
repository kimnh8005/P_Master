package kr.co.pulmuone.v1.goods.search;

import java.util.List;
import java.util.stream.Collectors;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.goods.search.dto.GoodsSearchNewGoodsRequestDto;
import kr.co.pulmuone.v1.goods.search.dto.GoodsSearchStoreGoodsRequestDto;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchHmrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsType;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.SaleStatus;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.comm.util.PriceUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.StorePriceDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ItemStoreInfoVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchAdditionalVo;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchExperienceVo;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchOutMallVo;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;

@Service
public class GoodsSearchBizImpl implements GoodsSearchBiz {

    @Autowired
    private GoodsSearchService goodsSearchService;

    @Autowired
    private GoodsGoodsBiz goodsGoodsBiz;

    @Override
    public GoodsSearchExperienceVo getGoodsSearchExperience(Long ilGoodsId) throws Exception {
        return goodsSearchService.getGoodsSearchExperience(ilGoodsId);
    }

    @Override
    public List<GoodsSearchResultDto> searchGoodsByGoodsIdList(GoodsSearchByGoodsIdRequestDto dto) throws Exception {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        if(dto.getDeviceInfo() == null) {
            dto.setDeviceInfo(DeviceUtil.getGoodsEnumDeviceTypeByUserDevice().getCode());
            dto.setMember(buyerVo != null && StringUtil.isNotEmpty(buyerVo.getUrUserId()));
            dto.setEmployee(buyerVo != null && StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode()));
        }

        if(dto.isNeedNonMemberShowMemberGoods()){
            dto.setMember(true);
        }

        List<GoodsSearchResultDto> list = goodsSearchService.searchGoodsByGoodsIdList(dto);

		// 매장 상품 관련 데이터 수정 처리
		if (list != null && !list.isEmpty() && StringUtil.isNotEmpty(dto.getUrStoreId())) {
			List<String> ilItemCds = list.stream().map(GoodsSearchResultDto::getItemCd).collect(Collectors.toList());
			List<ItemStoreInfoVo> itemStoreInfoList = goodsGoodsBiz.getItemStoreInfoList(dto.getUrStoreId(), ilItemCds);
			for (GoodsSearchResultDto goodsDto : list) {
				ItemStoreInfoVo storeInfo = itemStoreInfoList.stream().filter(info-> info.getIlItemCd().equals(goodsDto.getItemCd())).findAny().orElse(null);
				if (storeInfo != null) {
					StorePriceDto storePriceDto = goodsGoodsBiz.getStoreSalePrice(goodsDto.getDiscountType(), goodsDto.getRecommendedPrice(), goodsDto.getSalePrice(), storeInfo.getStoreSalePrice());
					goodsDto.setSalePrice(storePriceDto.getSalePrice());
					goodsDto.setDiscountRate(PriceUtil.getRate(goodsDto.getRecommendedPrice(), goodsDto.getSalePrice()));
					goodsDto.setStatusCode(goodsGoodsBiz.getSaleStatus(goodsDto.getStatusCode(), storeInfo.getStoreStock()));
				} else {
					if(GoodsType.SHOP_ONLY.getCode().equals(goodsDto.getGoodsType())) {
						goodsDto.setStatusCode(SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode());
					}
				}
			}
		}
        return list;
    }

    @Override
    public List<Long> getDailyGoods(String goodsDailyType) throws Exception {
        return goodsSearchService.getDailyGoods(goodsDailyType);
    }

    @Override
    public List<GoodsSearchOutMallVo> getGoodsFromOutMall(List<String> goodsIdList) throws Exception {
        return goodsSearchService.getGoodsFromOutMall(goodsIdList);
    }

    @Override
    public List<Long> getNewGoods(GoodsSearchNewGoodsRequestDto dto) {
        return goodsSearchService.getNewGoods(dto);
    }

    @Override
    public List<GoodsSearchAdditionalVo> getGoodsAdditional(List<Long> goodsIdList) throws Exception {
        return goodsSearchService.getGoodsAdditional(goodsIdList);
    }

    @Override
    public List<String> getGoodsCouponCoverageByUrWareHouseId(List<Long> wareHouseIdList) throws Exception {
        return goodsSearchService.getGoodsCouponCoverageByUrWareHouseId(wareHouseIdList);
    }

    @Override
    public List<GoodsSearchHmrVo> getGoodsSearchHmr(GoodsSearchStoreGoodsRequestDto dto) throws Exception {
        return goodsSearchService.getGoodsSearchHmr(dto);
    }

    @Override
    public Page<Long> getGoodsSearchShopOnly(GoodsSearchStoreGoodsRequestDto dto) throws Exception {
        return goodsSearchService.getGoodsSearchShopOnly(dto);
    }

    @Override
    public Page<Long> getOrgaFlyerGoods(String discountType) throws Exception {
        return goodsSearchService.getOrgaFlyerGoods(discountType);
    }
}
