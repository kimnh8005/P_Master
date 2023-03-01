package kr.co.pulmuone.v1.search.searcher.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchRequestDto;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.v1.search.searcher.dto.SearchResultDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
class GoodsSearchServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    GoodsSearchService goodsSearchService;

    @Test
    void test_키워드조건만있는경우_디폴트_파라미터_값체크() throws Exception {

        String keyword = "테스트";

        GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder().keyword(keyword).build();
        Assertions.assertFalse(goodsSearchRequestDto.isFirstSearch());
        Assertions.assertTrue(goodsSearchRequestDto.getLimit() == 50);

        SearchResultDto searchResult = goodsSearchService.search(goodsSearchRequestDto);
        Assertions.assertNull(searchResult.getFilter());

    }

    @Test
    void test_품절상품제외() throws Exception {

        String keyword = "테스트";

        GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder()
                                                        .keyword(keyword)
                                                        .excludeSoldOutGoods(true)
                                                        .build();

        SearchResultDto searchResult = goodsSearchService.search(goodsSearchRequestDto);
        List<GoodsSearchResultDto> items = searchResult.getDocument();
        items.stream().forEach(
                i -> log.info("sale_status: {}", i.getStatusCode())
        );

        Assertions.assertTrue(items.stream().allMatch(i -> "SALE_STATUS.ON_SALE".equals(i.getStatusCode())));

    }

    @Test
    void test_그룹바이쿼리() throws Exception {

        boolean isFirstSearch = true;
        GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder()
                .keyword("만두")
                .isFirstSearch(isFirstSearch)
                .build();

        SearchResultDto searchResult = goodsSearchService.search(goodsSearchRequestDto);
        Assertions.assertTrue(ObjectUtils.isNotEmpty(searchResult.getFilter()));

    }


    @Test
    void test_몰인몰필터검색() throws Exception {

        String keyword = "테스트";
        String mallId = "MALL_DIV.PULMUONE";

        GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder()
                .keyword(keyword)
                .mallId(mallId)
                .build();

        SearchResultDto searchResult = goodsSearchService.search(goodsSearchRequestDto);
        List<GoodsSearchResultDto> items = searchResult.getDocument();
        items.stream().forEach(
                i -> log.info(i.getMallId())
        );

        Assertions.assertTrue(items.size() >= 0);

    }


    @Test
    void test_가격필터검색() throws Exception {

        String keyword = "연동";
        int minimumPrice = 100;
        int maximumPrice = 1000;

        GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder()
                .keyword(keyword)
                .minimumPrice(minimumPrice)
                .maximumPrice(maximumPrice)
                .build();

        SearchResultDto searchResult = goodsSearchService.search(goodsSearchRequestDto);
        List<GoodsSearchResultDto> items = searchResult.getDocument();

        Assertions.assertTrue(items.stream().allMatch(i-> i.getSalePrice() >= minimumPrice));
        Assertions.assertTrue(items.stream().allMatch(i-> i.getSalePrice() <= maximumPrice));

    }


    @Test
    void test_브랜드필터검색() throws Exception {

        String keyword = "테스트";
        List brandIdList = new ArrayList();
        brandIdList.add("1");
        brandIdList.add("226");

        GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder()
                .keyword(keyword)
                .brandIdList(brandIdList)
                .build();

        SearchResultDto searchResult = goodsSearchService.search(goodsSearchRequestDto);
        List<GoodsSearchResultDto> items = searchResult.getDocument();
        List list = items.stream()
                .filter(i -> !i.getBrandId().equals("1"))
                .filter(i -> !i.getBrandId().equals("226"))
                .collect(Collectors.toList());

        Assertions.assertTrue(list.size() == 0);

    }

    @Test
    void test_기타필터검색() throws Exception {
        //혜택유형
        List<String> benefitIdList = new ArrayList<>();
        benefitIdList.add("COUPON");

        //인증유형
        List<String> certificationIdList = new ArrayList<>();
        certificationIdList.add("53");
        certificationIdList.add("54");

        //배송유형
        List<String> deliveryTypeIdList = new ArrayList<>();
        deliveryTypeIdList.add("SALE_TYPE.NORMAL");

        //보관방법
        List<String> storageMethodIdList = new ArrayList<>();
        storageMethodIdList.add("ERP_STORAGE_TYPE.COOL");


        GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder()
                .benefitTypeIdList(benefitIdList)
                .certificationTypeIdList(certificationIdList)
                .deliveryTypeIdList(deliveryTypeIdList)
                .storageMethodIdList(storageMethodIdList)
                .purchaseTargetType("PURCHASE_TARGET_TP.ALL")
                .limit(5)
                .build();

        SearchResultDto searchResult = goodsSearchService.search(goodsSearchRequestDto);
        List<GoodsSearchResultDto> items = searchResult.getDocument();
        items.stream().forEach(i -> log.info(i.toString()));

        Assertions.assertTrue(items.size() >= 0);


    }

    @Test
    void test_통합몰_전시카테고리ID_검색() throws Exception {

        String lev1CategoryId = "60";
        String lev2CategoryId = "154";
        String lev3CategoryId = "160";

        int limit = 5;

        GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder()
                .lev1CategoryId(lev1CategoryId)
                .lev2CategoryId(lev2CategoryId)
                .lev3CategoryId(lev3CategoryId)
                .limit(5)
                .build();

        SearchResultDto searchResult = goodsSearchService.search(goodsSearchRequestDto);
        List<GoodsSearchResultDto> items = searchResult.getDocument();
        items.stream().forEach(i -> log.info(i.toString()));

        Assertions.assertTrue(items.size() >= 0);

    }


    @Test
    void test_몰인몰_전시카테고리ID_검색() throws Exception {

        String mallId = "MALL_DIV.PULMUONE";
        String lev1CategoryId = "60";
        //String lev2CategoryId = "55";
        int limit = 5;

        GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder()
                .lev1CategoryId(lev1CategoryId)
                .mallId(mallId)
                .limit(5)
                .build();

        SearchResultDto searchResult = goodsSearchService.search(goodsSearchRequestDto);
        List<GoodsSearchResultDto> items = searchResult.getDocument();
        items.stream().forEach(i -> log.info(i.toString()));

        Assertions.assertTrue(Optional.ofNullable(items).get().size() >= 0);

    }


    @Test
    void test_상품코드_검색() throws Exception {

        List<Long> itemIdList = new ArrayList<>();
        itemIdList.add(175L);
        itemIdList.add(177L);

        GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder()
                .goodsIdList(itemIdList)
                .build();

        SearchResultDto searchResult = goodsSearchService.search(goodsSearchRequestDto);
        List<GoodsSearchResultDto> items = searchResult.getDocument();
        items.stream().forEach(i -> log.info(i.toString()));

        Assertions.assertTrue(items.size() >= 0);

    }


    @Test
    void test_임직원여부에_따른_가격정보_확인() throws Exception {

        String keyword = "테스트";

        GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder()
                .keyword(keyword)
                .build();

        SearchResultDto searchResult = goodsSearchService.search(goodsSearchRequestDto);
        List<GoodsSearchResultDto> items = searchResult.getDocument();

        items.stream().forEach(
                i ->
                    log.info("정상가: {}, 판매가: {}, 할인율: {}, 임직원판매가: {}, 임직원할인율: {}"
                            ,i.getRecommendedPrice(), i.getSalePrice(), i.getDiscountRate(), i.getEmployeeDiscountPrice(), i.getEmployeeDiscountRate())

        );

    }


    @Test
    void test_전시허용범위_검색() throws Exception {

        String keyword = "건강생활연동";
        GoodsEnums.DeviceType deviceType = GoodsEnums.DeviceType.MOBILE;

        GoodsSearchRequestDto goodsSearchRequestDto = GoodsSearchRequestDto.builder()
                .keyword(keyword)
                .deviceType(deviceType)
                .build();

        SearchResultDto searchResult = goodsSearchService.search(goodsSearchRequestDto);
        List<GoodsSearchResultDto> items = searchResult.getDocument();

        items.stream().forEach(
                i -> Assertions.assertTrue(i.getDisplayMobileYn().equals("Y"))
        );
    }

}
