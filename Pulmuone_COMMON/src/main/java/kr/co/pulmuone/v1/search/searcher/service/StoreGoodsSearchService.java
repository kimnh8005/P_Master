package kr.co.pulmuone.v1.search.searcher.service;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.Indices;
import kr.co.pulmuone.v1.search.indexer.dto.CategoryBoostDocumentDto;
import kr.co.pulmuone.v1.search.searcher.constants.Lev1MallCategory;
import kr.co.pulmuone.v1.search.searcher.constants.SortCode;
import kr.co.pulmuone.v1.search.searcher.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.ScriptScoreQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.TermsValuesSourceBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StoreGoodsSearchService extends SearchService
{

	protected SearchResultDto search(StoreGoodsSearchRequestDto searchRequestDto)
	{
		SearchResultDto storeGoodsSearchResultDto = super.search(Indices.STORE_GOODS.getAlias(), makeQuery(searchRequestDto), StoreGoodsSearchResultDto.class);

		if (!searchRequestDto.isFirstSearch()) return storeGoodsSearchResultDto;

		return appendInitialFilterGroupBy(searchRequestDto, storeGoodsSearchResultDto);

	}

	/**
	 * 최초 한번만, 키워드 조건만 가지고 몰인몰 및 필터에 대해 그룹바이 함.
	 *
	 * @param searchRequestDto
	 * @param storeGoodsSearchResultDto
	 * @return
	 * @throws Exception
	 */
	private SearchResultDto appendInitialFilterGroupBy(StoreGoodsSearchRequestDto searchRequestDto, SearchResultDto storeGoodsSearchResultDto) {
		SearchResultDto filterGroupByResultDto = super.search(Indices.STORE_GOODS.getAlias(), makeInitialFilterGroupByQuery(searchRequestDto, true), StoreGoodsSearchResultDto.class);

		filterGroupByResultDto = convertMultiValueFilter(filterGroupByResultDto);
		filterGroupByResultDto = convertMallCategoryFilter(filterGroupByResultDto);

		// mall_id 가 존재 하는 경우 mall tab 상품 수 유지를 위한 mall_id 검색 쿼리 제외
		// 위 검색 결과에 filter > mall 값을 mall_id 제외된 정보로 수정
		if(searchRequestDto.hasMallId()) {
			SearchResultDto filterMallGroupByResultDto = super.search(Indices.STORE_GOODS.getAlias(), makeInitialFilterGroupByQuery(searchRequestDto, false), StoreGoodsSearchResultDto.class);
			filterMallGroupByResultDto = convertMultiValueFilter(filterMallGroupByResultDto);
			filterMallGroupByResultDto = convertMallCategoryFilter(filterMallGroupByResultDto);
			filterGroupByResultDto.getFilter().remove("mall");
			filterGroupByResultDto.getFilter().put("mall", filterMallGroupByResultDto.getFilter().get("mall"));
		}

		storeGoodsSearchResultDto.setFilter(filterGroupByResultDto.getFilter());
		return storeGoodsSearchResultDto;
	}

	/**
	 * 그룹 바이 결과가 없는 mall에 대해서 추가하여 리턴.
	 *
	 * @param filterGroupByResultDto
	 * @return
	 */
	private SearchResultDto convertMallCategoryFilter(SearchResultDto filterGroupByResultDto)
	{
		List<AggregationDocumentDto> searchResultInMallGroupByList = (List<AggregationDocumentDto>) filterGroupByResultDto.getFilter().get("mall");
		List<String> searchResultInMallIdList = searchResultInMallGroupByList.stream().map(AggregationDocumentDto::getCode).collect(Collectors.toList());
		Arrays.stream(Lev1MallCategory.values()).filter(mallCategory -> !searchResultInMallIdList.contains(mallCategory.getCode())).forEach(mallCategory -> searchResultInMallGroupByList.add(AggregationDocumentDto.builder().code(mallCategory.getCode()).name(mallCategory.getName()).build()));
		filterGroupByResultDto.getFilter().put("mall", searchResultInMallGroupByList);

		return filterGroupByResultDto;
	}

	/**
	 * 몰인몰 및 필터 그룹바이 쿼리
	 *
	 * @param searchRequestDto
	 * @return
	 */
	private SearchSourceBuilder makeInitialFilterGroupByQuery(StoreGoodsSearchRequestDto searchRequestDto, boolean isMallCategory)
	{
		BoolQueryBuilder query = QueryBuilders.boolQuery();

		if (searchRequestDto.hasKeyword()) {
			QueryBuilder multiKeywordQuery = QueryBuilders.multiMatchQuery(searchRequestDto.getKeyword(), Indices.STORE_GOODS.getMultiMatchFields()).operator(Operator.AND);
			query.must(multiKeywordQuery);
		} else {
			query = appendCategoryQuery(query, searchRequestDto);
		}

		if (searchRequestDto.hasDisplayChannel()) {
			QueryBuilder displayChannelQuery = QueryBuilders.termsQuery("display_channel_list", searchRequestDto.getDeviceType());
			query.must(displayChannelQuery);
		}

		if (!searchRequestDto.isEmployee()) {
			QueryBuilder purchaseTargetTypeQuery = QueryBuilders.matchQuery("purchase_target_type", "ALL").operator(Operator.AND);
			query.must(purchaseTargetTypeQuery);
		}

		// mall_id 조건으로 카테고리 그룹바이가 필요한 경우
		if (searchRequestDto.hasMallId() && isMallCategory) {
			QueryBuilder mallIdQuery = QueryBuilders.matchQuery("mall_id", searchRequestDto.getMallId()).operator(Operator.AND);
			query.must(mallIdQuery);
		}

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder = appendFilterAggregationQuery(searchSourceBuilder);
		searchSourceBuilder.size(0);
		searchSourceBuilder.query(query);

		log.info("group by query:: {}", searchSourceBuilder);

		return searchSourceBuilder;
	}

	/**
	 * 검색 쿼리 빌드
	 *
	 * @param searchRequestDto
	 * @return
	 * @throws Exception
	 */
	private SearchSourceBuilder makeQuery(StoreGoodsSearchRequestDto searchRequestDto) {
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		query = appendCategoryQuery(query, searchRequestDto);
		query = appendFilterQuery(query, searchRequestDto);

		if (searchRequestDto.hasKeyword()) {
			QueryBuilder multiKeywordQuery = QueryBuilders.multiMatchQuery(searchRequestDto.getKeyword(), Indices.STORE_GOODS.getMultiMatchFields()).operator(Operator.AND);
			query.must(multiKeywordQuery);
		}

		if (!searchRequestDto.isEmployee()) {
			QueryBuilder purchaseTargetTypeQuery = QueryBuilders.matchQuery("purchase_target_type", "ALL").operator(Operator.AND);
			query.must(purchaseTargetTypeQuery);
		}

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.from(searchRequestDto.offset());
		searchSourceBuilder.size(searchRequestDto.getLimit());
		searchSourceBuilder.query(query);


		if ( searchRequestDto.getSortCode() == SortCode.POPULARITY ) {
			query = appendCategoryBoostQuery(query, searchRequestDto);
			ScriptScoreQueryBuilder scriptScoreQueryBuilder = QueryBuilders.scriptScoreQuery(query, new Script("_score < 1000 ? 0 : Math.floor(_score / 1000) * 1000 + doc['popularity_score'].value"));
			searchSourceBuilder.query(scriptScoreQueryBuilder);
		}

		FieldSortBuilder sortQuery = new FieldSortBuilder(searchRequestDto.getSortCode().getField()).order(searchRequestDto.getSortCode().getOrder());
		FieldSortBuilder secondSortQuery = new FieldSortBuilder(searchRequestDto.getSortCode().getSecondField()).order(searchRequestDto.getSortCode().getSecondOrder());
		searchSourceBuilder.sort(sortQuery).sort(secondSortQuery);

		log.info("goods search query: {} ", searchSourceBuilder);

		return searchSourceBuilder;
	}

	/**
	 * 필터 검색 쿼리 append
	 *
	 * @param query
	 * @param searchRequestDto
	 * @return
	 */
	private BoolQueryBuilder appendFilterQuery(BoolQueryBuilder query, StoreGoodsSearchRequestDto searchRequestDto)
	{

		if (searchRequestDto.hasMallId())
		{
			QueryBuilder mallQuery = QueryBuilders.termsQuery("mall_id_list", searchRequestDto.getMallId());
			query.must(mallQuery);

		}

		if (searchRequestDto.isExcludeSoldOutGoods())
		{
			QueryBuilder statusQuery = QueryBuilders.matchQuery("status_code", GoodsEnums.SaleStatus.ON_SALE.getCode()).operator(Operator.AND);
			query.must(statusQuery);
		}

		if (searchRequestDto.hasBrandIdList())
		{
			QueryBuilder brandTermsQuery = QueryBuilders.termsQuery("brand_id", searchRequestDto.getBrandIdList());
			query.must(brandTermsQuery);
		}

		if (searchRequestDto.hasBenefitTypeIdList())
		{
			QueryBuilder benefitTermsQuery = QueryBuilders.termsQuery("benefit_type_id_list", searchRequestDto.getBenefitTypeIdList());
			query.must(benefitTermsQuery);
		}

		if (searchRequestDto.hasCertificationTypeIdList())
		{
			QueryBuilder certificationTermsQuery = QueryBuilders.termsQuery("certification_type_id_list", searchRequestDto.getCertificationTypeIdList());
			query.must(certificationTermsQuery);
		}

		if (searchRequestDto.hasDeliveryTypeIdList())
		{
			QueryBuilder deliveryTypeTermsQuery = QueryBuilders.termsQuery("delivery_type_id_list", searchRequestDto.getDeliveryTypeIdList());
			query.must(deliveryTypeTermsQuery);
		}

		if (searchRequestDto.hasStorageMethodIdList())
		{
			QueryBuilder storageMethodTermsQuery = QueryBuilders.termsQuery("storage_method_id", searchRequestDto.getStorageMethodIdList());
			query.must(storageMethodTermsQuery);
		}

		if (searchRequestDto.hasPriceFilterValue())
		{
			RangeQueryBuilder priceRangeQuery = new RangeQueryBuilder("sale_price").gte(searchRequestDto.getMinimumPrice()).lte(searchRequestDto.getMaximumPrice());
			query.must(priceRangeQuery);
		}

		if (searchRequestDto.hasGoodsIdList())
		{
			QueryBuilder goodsIdTermsQuery = QueryBuilders.termsQuery("goods_id", searchRequestDto.getGoodsIdList());
			query.must(goodsIdTermsQuery);
		}

		if (searchRequestDto.hasDisplayChannel()) {
			QueryBuilder displayChannelQuery = QueryBuilders.termsQuery("display_channel_list", searchRequestDto.getDeviceType());
			query.must(displayChannelQuery);
		}

		if (searchRequestDto.hasPurchaseTargetType()) {
			QueryBuilder purchaseTargetQuery = QueryBuilders.termsQuery("purchase_target_type", searchRequestDto.getPurchaseTargetType());
			query.must(purchaseTargetQuery);
		}

		if (searchRequestDto.hasDiscountRateFilterValue()) {
			RangeQueryBuilder discountRateRangeQuery = searchRequestDto.isEmployee() ?
						new RangeQueryBuilder("employee_discount_rate").gte(searchRequestDto.getMinimumDiscountRate()).lte(searchRequestDto.getMaximumDiscountRate())
							: new RangeQueryBuilder("discount_rate").gte(searchRequestDto.getMinimumDiscountRate()).lte(searchRequestDto.getMaximumDiscountRate());
			query.must(discountRateRangeQuery);
		}

		if(searchRequestDto.isDiscountGoods()) {
			RangeQueryBuilder isDiscountGoods = searchRequestDto.isEmployee() ?
					new RangeQueryBuilder("employee_discount_rate").gt(0)
						: new RangeQueryBuilder("discount_rate").gt(0);
			query.must(isDiscountGoods);
		}

		if (searchRequestDto.hasStoreId())
		{
			QueryBuilder statusQuery = QueryBuilders.matchQuery("store_id", searchRequestDto.getStoreId()).operator(Operator.AND);
			query.must(statusQuery);
		}

		return query;
	}

	/**
	 * 카테고리 필터 쿼리 append
	 *
	 * @param query
	 * @param searchRequestDto
	 * @return
	 */
	private BoolQueryBuilder appendCategoryQuery(BoolQueryBuilder query, StoreGoodsSearchRequestDto searchRequestDto)
	{
		if (searchRequestDto.hasLev1CategoryId())
		{
			QueryBuilder lev1CategoryQuery = QueryBuilders.termsQuery("lev1_category_id_list", searchRequestDto.getLev1CategoryId());
			query.must(lev1CategoryQuery);
		}
		if (searchRequestDto.hasLev2CategoryId())
		{
			QueryBuilder lev2CategoryQuery = QueryBuilders.termsQuery("lev2_category_id_list", searchRequestDto.getLev2CategoryId());
			query.must(lev2CategoryQuery);
		}

		if (searchRequestDto.hasLev3CategoryId())
		{
			QueryBuilder lev3CategoryQuery = QueryBuilders.termsQuery("lev3_category_id_list", searchRequestDto.getLev3CategoryId());
			query.must(lev3CategoryQuery);
		}
		return query;
	}


	/**
	 * 카테고리 부스팅 쿼리 append
	 *
	 * @param query
	 * @param searchRequestDto
	 * @return
	 * @throws Exception
	 */
	private BoolQueryBuilder appendCategoryBoostQuery(BoolQueryBuilder query, StoreGoodsSearchRequestDto searchRequestDto) {

		if (!searchRequestDto.hasKeyword()) return query;

		SearchResultDto boostSearchResult = super.search(Indices.CATEGORY_BOOST.getAlias(), makeQueryForCategoryBoost(searchRequestDto.getKeyword()), CategoryBoostDocumentDto.class);

		Map<String, Integer> map = new HashMap<>();
		List<CategoryBoostDocumentDto> list = boostSearchResult.getDocument();
		list.stream().forEach(b -> map.put(b.getLev1CategoryId(), b.getScore()));

		final int CATEGORY_BOOSTING_MULTIPLY_VALUE = 1000;

		for (String categoryCode : map.keySet())
		{
			QueryBuilder boostQuery = QueryBuilders.termsQuery("lev1_category_id_list", categoryCode);
			QueryBuilder constantScoreQuery = QueryBuilders.constantScoreQuery(boostQuery).boost(map.get(categoryCode) * CATEGORY_BOOSTING_MULTIPLY_VALUE);
			query.should(constantScoreQuery);
		}

		return query;
	}


	private SearchSourceBuilder makeQueryForCategoryBoost(String keyword)
	{
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		QueryBuilder keywordQuery = QueryBuilders.matchQuery("keyword", keyword).operator(Operator.AND);
		query.must(keywordQuery);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(query);
		return searchSourceBuilder;
	}
	
	/**
	 * 필터 그룹바이 Source 쿼리 append
	 *
	 * @param searchSourceBuilder
	 * @return
	 */
	private SearchSourceBuilder appendFilterAggregationQuery(SearchSourceBuilder searchSourceBuilder)
	{
		
		List<CompositeValuesSourceBuilder<?>> category = new ArrayList<>();
		category.add(new TermsValuesSourceBuilder("main_lev1_category_id_name_list").field("main_lev1_category_id_name_list"));
		CompositeAggregationBuilder lev1CategoryCompositeAggregationBuilder = new CompositeAggregationBuilder("category", category);
		lev1CategoryCompositeAggregationBuilder.size(20);
		
		List<CompositeValuesSourceBuilder<?>> brand = new ArrayList<>();
		brand.add(new TermsValuesSourceBuilder("brand_id").field("brand_id"));
		brand.add(new TermsValuesSourceBuilder("brand_name").field("brand_name"));
		CompositeAggregationBuilder brandCompositeAggregationBuilder = new CompositeAggregationBuilder("brand", brand);
		brandCompositeAggregationBuilder.size(20);

		List<CompositeValuesSourceBuilder<?>> delivery = new ArrayList<>();
		delivery.add(new TermsValuesSourceBuilder("delivery_type_id_name_list").field("delivery_type_id_name_list"));
		CompositeAggregationBuilder deliveryCompositeAggregationBuilder = new CompositeAggregationBuilder("delivery", delivery);
		
		List<CompositeValuesSourceBuilder<?>> benefit = new ArrayList<>();
		benefit.add(new TermsValuesSourceBuilder("benefit_type_id_name_list").field("benefit_type_id_name_list"));
		CompositeAggregationBuilder benefitCompositeAggregationBuilder = new CompositeAggregationBuilder("benefit", benefit);
		
		List<CompositeValuesSourceBuilder<?>> certification = new ArrayList<>();
		certification.add(new TermsValuesSourceBuilder("certification_type_id_name_list").field("certification_type_id_name_list"));
		CompositeAggregationBuilder certificationCompositeAggregationBuilder = new CompositeAggregationBuilder("certification", certification);
		
		List<CompositeValuesSourceBuilder<?>> mall = new ArrayList<>();
		mall.add(new TermsValuesSourceBuilder("mall_id_name_list").field("mall_id_name_list"));
		CompositeAggregationBuilder mallCompositeAggregationBuilder = new CompositeAggregationBuilder("mall", mall);

		List<CompositeValuesSourceBuilder<?>> storage = new ArrayList<>();
		storage.add(new TermsValuesSourceBuilder("storage_method_id").field("storage_method_id"));
		storage.add(new TermsValuesSourceBuilder("storage_method_name").field("storage_method_name"));
		CompositeAggregationBuilder storageCompositeAggregationBuilder = new CompositeAggregationBuilder("storage", storage);


		searchSourceBuilder.aggregation(lev1CategoryCompositeAggregationBuilder);
		searchSourceBuilder.aggregation(brandCompositeAggregationBuilder);
		searchSourceBuilder.aggregation(deliveryCompositeAggregationBuilder);
		searchSourceBuilder.aggregation(benefitCompositeAggregationBuilder);
		searchSourceBuilder.aggregation(certificationCompositeAggregationBuilder);
		searchSourceBuilder.aggregation(mallCompositeAggregationBuilder);
		searchSourceBuilder.aggregation(storageCompositeAggregationBuilder);
		
		return searchSourceBuilder;
	}
	
	private SearchResultDto convertMultiValueFilter(SearchResultDto storeGoodsSearchResultDto)
	{
		storeGoodsSearchResultDto.getFilter().keySet().forEach(k -> {
			List<AggregationDocumentDto> list = (List<AggregationDocumentDto>) storeGoodsSearchResultDto.getFilter().get(k);
			list.stream().forEach(l -> {
				String[] aggregations = l.getCode().split(":");
				if (aggregations.length > 1)
				{
					l.setCode(aggregations[0]);
					l.setName(aggregations[1]);
				}
			});
			storeGoodsSearchResultDto.getFilter().put(k, list);
		});
		return storeGoodsSearchResultDto;
	}

}
