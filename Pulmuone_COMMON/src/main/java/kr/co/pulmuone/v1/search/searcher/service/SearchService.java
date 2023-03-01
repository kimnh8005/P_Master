package kr.co.pulmuone.v1.search.searcher.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import kr.co.pulmuone.v1.search.searcher.dto.AggregationDocumentDto;
import kr.co.pulmuone.v1.search.searcher.dto.SearchResultDto;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.bucket.composite.ParsedComposite;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private static final ObjectMapper MAPPER = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected SearchResultDto search(String index, SearchSourceBuilder query, Class valueType) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        searchRequest.source(query);

        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

        return SearchResultDto.builder()
                .count(searchResponse.getHits().getTotalHits().value)
                .document(convertResultList(searchResponse, valueType))
                .filter(convertAggregations(searchResponse))
                .build();
    }


    private Map convertAggregations(SearchResponse response) {

        if( response.getAggregations() == null ) return null;

        Map<String, List<AggregationDocumentDto>> filterMap = new ConcurrentHashMap<>();

        Iterator it = response.getAggregations().iterator();
        while(it.hasNext()) {

            List list = new ArrayList();
            ParsedComposite agg = (ParsedComposite) it.next();

            agg.getBuckets().stream().forEach(
                    a -> {
                        try {
                            a.getKey().put("count", a.getDocCount());
                            String sourceMapToString = MAPPER.writeValueAsString(a.getKey());
                            list.add(MAPPER.readValue(sourceMapToString, AggregationDocumentDto.class));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
            );
            filterMap.put(agg.getName(), list);
        }
        return filterMap;
    }


    private static List convertResultList(SearchResponse response, Class valueType) {
        int initialSize = (int) (response.getHits().getTotalHits().value);
        List list = new ArrayList<>(initialSize);

        response.getHits().forEach(
                hit -> {
                    try {
                        if( !hit.getHighlightFields().isEmpty() ) {
                            hit.getSourceAsMap().put("highlight", hit.getHighlightFields().get("word").getFragments()[0].string());
                        }
                        String sourceMapToString = MAPPER.writeValueAsString(hit.getSourceAsMap());
                        list.add(MAPPER.readValue(sourceMapToString, valueType));

                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
        );
        return list;
    }


    @PreDestroy
    public void closeClient() throws Exception {
        restHighLevelClient.close();
    }
}
