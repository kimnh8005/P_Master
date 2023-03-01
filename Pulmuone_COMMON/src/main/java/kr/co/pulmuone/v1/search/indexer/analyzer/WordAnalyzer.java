package kr.co.pulmuone.v1.search.indexer.analyzer;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.client.indices.AnalyzeResponse.AnalyzeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class WordAnalyzer {

    @Autowired
    WordAnalyzer(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    private static RestHighLevelClient restHighLevelClient;

    public static final String DEFAULT_ANALYZER_NORI = "nori";
    public static final String ANALYZER_STANDARD = "standard";

    /**
     * 복합명사 여부 체크
     * @param word
     * @return
     * @throws Exception
     */
    public static boolean isCompoundNoun(String word) throws Exception {
        return analyze(word).size() > 1 ;
    }


    /**
     * nori analyzer를 사용한 형태소 분석
     * @param word
     * @return
     */
    public static List<AnalyzeToken> analyze(String word) {
        try {
            return analyze(DEFAULT_ANALYZER_NORI, word);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 형태소 분석
     * @param analyzerName
     * @param word
     * @return
     * @throws Exception
     */
    public static List<AnalyzeToken> analyze(String analyzerName, String word) throws Exception {
        AnalyzeRequest analyzeRequest = AnalyzeRequest.withGlobalAnalyzer(analyzerName, word);

        AnalyzeResponse analyzeResponse = null;
        try {
            analyzeResponse = restHighLevelClient.indices().analyze(analyzeRequest, RequestOptions.DEFAULT);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new Exception("Word Analyzer Exception Caused By [" + word  + "]");
        }

        return analyzeResponse.getTokens();
    }


    /**
     * 파라미터로 넘어온 리스트를 체크하여,
     * 일반(단일)명사를 제외한 복합명사들만 리턴한다.
     * @param wordList
     * @return
     */
    public static List<String> reduceCompoundNounList(List<String> wordList)  {
        List<String> compoundNounList = new ArrayList<>();

        wordList.stream()
                .filter(w -> analyze(w).size() > 1)
                .forEach(w -> compoundNounList.add(w));

        return compoundNounList;
    }

}
