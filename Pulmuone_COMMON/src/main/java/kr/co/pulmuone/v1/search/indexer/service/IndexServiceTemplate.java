package kr.co.pulmuone.v1.search.indexer.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import kr.co.pulmuone.v1.comm.enums.Indices;
import kr.co.pulmuone.v1.search.indexer.dto.IndexResultDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.GetAliasesResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class IndexServiceTemplate {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private final Indices indices;

    private static final ObjectMapper MAPPER = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private String indexName;

    /**
     * 색인 대상 리스트 조회
     * @return
     */
    protected abstract List<?> getIndexTargetList();


    /**
     * 전체색인; bulk indexing
     * @throws IOException
     */
    protected final IndexResultDto bulk() throws Exception {

        createIndexName();

        BulkResponse response = index();

        boolean isSwitchedAlias = response.hasFailures() ? false : switchAlias();

        return new IndexResultDto().builder()
                .count(response.getItems().length)
                .indexStatusCode(response.status().getStatus())
                .wasAliasSwitched(isSwitchedAlias)
                .build();
    }


    /**
     * 색인
     * @throws IOException
     */
    private BulkResponse index() {

        BulkResponse response = null;

        try {

            List<?> list = getIndexTargetList();

            if( CollectionUtils.isEmpty(list) ) {
                log.error("Bulk Index Fail; index target is empty. [{}]",  this.indices.name());
                throw new Exception("Bulk Index Fail; index target is empty. [ " + this.indices.name() + " ]");
            }

            BulkRequest request = new BulkRequest();
            list.forEach(
                   data -> {
                       IndexRequest indexRequest;
                       Map document = MAPPER.convertValue(data, Map.class);
                       if ( !ObjectUtils.isEmpty(document.get("id")) ) {
                           indexRequest = new IndexRequest(indexName).id(String.valueOf(document.get("id"))).source(document);
                       } else {
                           indexRequest = new IndexRequest(indexName).source(document);
                       }
                       request.add(indexRequest);
                   }
            );

            response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);

            if (response.hasFailures()) {
                log.error(response.buildFailureMessage());
                throw new Exception("Bulk Index Fail [ " + this.indices.name() + " ]");
            }

        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return response;

    }


    /**
     * 색인 별칭 변경
     * @return
     * @throws IOException
     */
    private boolean switchAlias() throws IOException {

        if( !isExistAlias() ) {
            log.info("[{}] There is no alias.", indices.name());
            return addAlias();
        }

        if( ! removeAlias() ) {
            log.info("[{}] The alias has not been removed.", indices.name());
            return false;
        }

        return addAlias();
    }


    private boolean isExistAlias() throws IOException {
        GetAliasesRequest requestWithAlias = new GetAliasesRequest(indices.getAlias());
        return restHighLevelClient.indices().existsAlias(requestWithAlias, RequestOptions.DEFAULT);
    }

    private boolean addAlias() throws IOException {

        IndicesAliasesRequest request = new IndicesAliasesRequest();
        IndicesAliasesRequest.AliasActions aliasAction = new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
                .index(indexName)
                .alias(indices.getAlias());

        request.addAliasAction(aliasAction);
        AcknowledgedResponse indicesAliasesResponse = restHighLevelClient.indices()
                .updateAliases(request, RequestOptions.DEFAULT);
        return indicesAliasesResponse.isAcknowledged();

    }

    private boolean removeAlias() throws IOException {
        IndicesAliasesRequest request = new IndicesAliasesRequest();
        IndicesAliasesRequest.AliasActions aliasAction = new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.REMOVE)
                .index(getPreviousIndexName())
                .alias(indices.getAlias());
        request.addAliasAction(aliasAction);
        AcknowledgedResponse indicesAliasesResponse = restHighLevelClient.indices()
                .updateAliases(request, RequestOptions.DEFAULT);

        return indicesAliasesResponse.isAcknowledged();
    }


    private String getPreviousIndexName() throws IOException {
        GetAliasesRequest requestWithAlias = new GetAliasesRequest(indices.getAlias());
        GetAliasesResponse response = restHighLevelClient.indices().getAlias(requestWithAlias, RequestOptions.DEFAULT);

        return response.getAliases().keySet().stream().findAny().get();
    }

    private void createIndexName() {
        this.indexName = indices.getAlias() + "_" + new SimpleDateFormat( "yyyyMMddHHmmss").format(new Date());
    }


}
