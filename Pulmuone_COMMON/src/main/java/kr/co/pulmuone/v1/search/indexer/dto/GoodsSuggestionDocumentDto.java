package kr.co.pulmuone.v1.search.indexer.dto;

import kr.co.pulmuone.v1.search.searcher.parser.KoreanParser;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoodsSuggestionDocumentDto {

    private String word;
    private String separatedWord;
    private String trimmedWord;
    private String highlight;

    @Builder
    public GoodsSuggestionDocumentDto(String word){
        this.word = word;
        this.separatedWord = new KoreanParser().parse(word);
        this.trimmedWord = new KoreanParser().parse(word.replaceAll(" ",""));

    }
}