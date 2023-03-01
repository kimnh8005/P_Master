package kr.co.pulmuone.v1.search.indexer.service;

import kr.co.pulmuone.v1.comm.enums.Indices;
import kr.co.pulmuone.v1.comm.mapper.search.index.SearchIndexMapper;
import kr.co.pulmuone.v1.search.indexer.dto.GoodsSuggestionDocumentDto;
import kr.co.pulmuone.v1.search.indexer.analyzer.WordAnalyzer;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GoodsSuggestionIndexService extends IndexServiceTemplate {

    @Autowired
    private SearchIndexMapper goodsIndexMapper;

    protected GoodsSuggestionIndexService() {
        super(Indices.GOODS_SUGGESTION);
    }

    @Override
    protected List<GoodsSuggestionDocumentDto> getIndexTargetList() {
        Set<String> keywordSet = new HashSet<>();

        goodsIndexMapper.getIndexTargetGoodsSuggestionList().stream().forEach(
                item -> {
                    try {
                        List<AnalyzeResponse.AnalyzeToken> tokensByNori =  WordAnalyzer.analyze(item.getGoodsFullName());
                        tokensByNori.stream().forEach(t -> keywordSet.add(t.getTerm()));

                        List<AnalyzeResponse.AnalyzeToken> tokensByStandard = WordAnalyzer.analyze(WordAnalyzer.ANALYZER_STANDARD, item.getGoodsFullName());
                        tokensByStandard.stream().forEach(t -> keywordSet.add(t.getTerm()));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        List<GoodsSuggestionDocumentDto> itemSuggestionKeywordList = new ArrayList<>();
        keywordSet.stream().forEach(
                keyword -> {
                    GoodsSuggestionDocumentDto doc = GoodsSuggestionDocumentDto.builder()
                            .word(keyword)
                            .build();
                    itemSuggestionKeywordList.add(doc);
                }
        );

        return itemSuggestionKeywordList;
    }
}
