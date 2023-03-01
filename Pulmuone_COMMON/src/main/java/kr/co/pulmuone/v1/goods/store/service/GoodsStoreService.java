package kr.co.pulmuone.v1.goods.store.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.display.contents.service.DisplayContentsBiz;
import kr.co.pulmuone.v1.goods.category.dto.OrgaStoreCategoryRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryDepth1Vo;
import kr.co.pulmuone.v1.goods.category.service.GoodsCategoryBiz;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.goods.search.dto.GoodsSearchStoreGoodsRequestDto;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchHmrVo;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStoreCategoryDto;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStoreGoodsRequestDto;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStoreGoodsResponseDto;
import kr.co.pulmuone.v1.goods.store.dto.OrgaStorePageInfoResponseDto;
import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import kr.co.pulmuone.v1.search.searcher.constants.SortCode;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import kr.co.pulmuone.v1.store.shop.service.StoreShopBiz;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GoodsStoreService {

    @Autowired
    private UserCertificationBiz userCertificationBiz;

    @Autowired
    private StoreDeliveryBiz storeDeliveryBiz;

    @Autowired
    private StoreShopBiz storeShopBiz;

    @Autowired
    private GoodsSearchBiz goodsSearchBiz;

    @Autowired
    private GoodsCategoryBiz goodsCategoryBiz;

    @Autowired
    private DisplayContentsBiz displayContentsBiz;

    @Autowired
    private PolicyConfigBiz policyConfigBiz;

    /**
     * 올가 매장전용관 페이지 정보 조회
     *
     * @return OrgaStorePageInfoResponseDto
     * @throws Exception Exception
     */
    protected OrgaStorePageInfoResponseDto getOrgaStorePageInfo() throws Exception {
        OrgaStorePageInfoResponseDto result = new OrgaStorePageInfoResponseDto();
        String urStoreId = null;

        GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();
        if (sessionShippingDto != null && sessionShippingDto.getReceiverZipCode() != null
                && sessionShippingDto.getBuildingCode() != null) {
            result.setShippingAddress(sessionShippingDto);
            ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryBiz.getStoreShippingPossibilityStoreDeliveryAreaInfo(sessionShippingDto.getReceiverZipCode(), sessionShippingDto.getBuildingCode());
            if (shippingPossibilityStoreDeliveryAreaInfo != null) {
                result.setStore(storeShopBiz.getShop(shippingPossibilityStoreDeliveryAreaInfo.getUrStoreId()));
                urStoreId = shippingPossibilityStoreDeliveryAreaInfo.getUrStoreId();
            }
        }
        result.setBanner(displayContentsBiz.getOrgaStoreBanner(DeviceUtil.getDirInfo()));
        result.setCategory(getOrgaStoreCategoryInfo(urStoreId));

        return result;
    }

    private List<OrgaStoreCategoryDto> getOrgaStoreCategoryInfo(String urStoreId) throws Exception {
        List<String> orgaDpBrandIdList = Arrays.stream(policyConfigBiz.getConfigValue(Constants.ORGA_DP_BRAND_KEY).split(",")).collect(Collectors.toList());

        // 대상 카테고리 정보 조회
        List<CategoryDepth1Vo> categoryList = goodsCategoryBiz.getOrgaStoreCategoryDepth1(
                OrgaStoreCategoryRequestDto.builder()
                        .mallDiv(GoodsEnums.MallDiv.ORGA.getCode())
                        .urStoreId(urStoreId)
                        .dpBrandIdList(orgaDpBrandIdList)
                        .build()).stream()
                .filter(vo -> vo.getExistGoodsYn().equals("Y"))
                .collect(Collectors.toList());

        if (categoryList.isEmpty()) return new ArrayList<>();

        // 카테고리 안 HMR / 손질상품 조회
        List<Long> categoryIdList = categoryList.stream()
                .map(CategoryDepth1Vo::getIlCtgryId)
                .collect(Collectors.toList());


        List<GoodsSearchHmrVo> hmrGoodsList = goodsSearchBiz.getGoodsSearchHmr(
                GoodsSearchStoreGoodsRequestDto.builder()
                        .mallDiv(GoodsEnums.MallDiv.ORGA.getCode())
                        .dpCtgryIdList(categoryIdList)
                        .urStoreId(urStoreId)
                        .dpBrandIdList(orgaDpBrandIdList)
                        .build()
        );
        Map<Long, List<Long>> hmrGoodsMap = new HashMap<>();
        for (GoodsSearchHmrVo hmrGoods : hmrGoodsList) {
            List<Long> list = hmrGoodsMap.getOrDefault(hmrGoods.getIlCtgryId(), new ArrayList<>());
            list.add(hmrGoods.getIlGoodsId());
            hmrGoodsMap.put(hmrGoods.getIlCtgryId(), list);
        }

        // 카테고리 별 상품정보 설정
        List<OrgaStoreCategoryDto> resultList = new ArrayList<>();
        for (CategoryDepth1Vo categoryVo : categoryList) {
            OrgaStoreCategoryDto result = new OrgaStoreCategoryDto();
            result.setIlCtgryId(categoryVo.getIlCtgryId());
            result.setCategoryName(categoryVo.getCategoryName());

            GoodsSearchByGoodsIdRequestDto requestDto = GoodsSearchByGoodsIdRequestDto.builder()
                    .mallDiv(GoodsEnums.MallDiv.ORGA.getCode())
                    .goodsIdList(hmrGoodsMap.get(categoryVo.getIlCtgryId()))
                    .goodsSortCode(SortCode.POPULARITY.name())
                    .build();
            result.setHmrGoods(goodsSearchBiz.searchGoodsByGoodsIdList(requestDto));
            resultList.add(result);
        }
        return resultList;
    }

    /**
     * 올가 매장전용관 상품 조회
     *
     * @param dto OrgaStoreGoodsRequestDto
     * @return OrgaStoreGoodsResponseDto
     * @throws Exception Exception
     */
    protected OrgaStoreGoodsResponseDto getOrgaStoreGoods(OrgaStoreGoodsRequestDto dto) throws Exception {
        //매장 전용상품 조회
        List<String> orgaDpBrandIdList = Arrays.stream(policyConfigBiz.getConfigValue(Constants.ORGA_DP_BRAND_KEY).split(",")).collect(Collectors.toList());
        Page<Long> targetGoodsList = goodsSearchBiz.getGoodsSearchShopOnly(
                GoodsSearchStoreGoodsRequestDto.builder()
                        .mallDiv(GoodsEnums.MallDiv.ORGA.getCode())
                        .dpCtgryIdList(Collections.singletonList(dto.getIlCtgryId()))
                        .urStoreId(dto.getUrStoreId())
                        .dpBrandIdList(orgaDpBrandIdList)
                        .build()
        );

        // 상품정보 조회
        GoodsSearchByGoodsIdRequestDto searchRequestDto = GoodsSearchByGoodsIdRequestDto.builder()
                .mallDiv(GoodsEnums.MallDiv.ORGA.getCode())
                .goodsIdList(targetGoodsList.getResult())
                .urStoreId(dto.getUrStoreId())
                .goodsSortCode(SortCode.POPULARITY.name())
                .page(dto.getPage())
                .limit(dto.getLimit())
                .build();

        List<GoodsSearchResultDto> goodsInfo = goodsSearchBiz.searchGoodsByGoodsIdList(searchRequestDto);

        return OrgaStoreGoodsResponseDto.builder()
                .total((int) targetGoodsList.getTotal())
                .goods(goodsInfo)
                .build();
    }

    /**
     * 올가 전단행사 대체 상품 조회
     *
     * @param displayType
     * @return OrgaStoreGoodsResponseDto
     * @throws Exception Exception
     */
    protected OrgaStoreGoodsResponseDto getOrgaFlyerGoods(String displayType) throws Exception {
        // 전단행사 대체 상품 조회
        Page<Long> targetGoodsList = goodsSearchBiz.getOrgaFlyerGoods(displayType);
        System.out.println("targetList >>>>> " + targetGoodsList.getResult());
        // 상품정보 조회
        GoodsSearchByGoodsIdRequestDto searchRequestDto = GoodsSearchByGoodsIdRequestDto.builder()
                .mallDiv(GoodsEnums.MallDiv.ORGA.getCode())
                .goodsIdList(targetGoodsList.getResult())
                .build();

        List<GoodsSearchResultDto> goodsInfo = goodsSearchBiz.searchGoodsByGoodsIdList(searchRequestDto);

        return OrgaStoreGoodsResponseDto.builder()
                .total((int) targetGoodsList.getTotal())
                .goods(goodsInfo)
                .build();
    }
}
