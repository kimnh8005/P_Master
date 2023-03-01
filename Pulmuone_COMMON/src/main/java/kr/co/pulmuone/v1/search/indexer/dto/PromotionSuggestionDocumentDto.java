package kr.co.pulmuone.v1.search.indexer.dto;

import kr.co.pulmuone.v1.search.searcher.parser.KoreanParser;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromotionSuggestionDocumentDto {

    private Long seq;
    private String typeCode;
    private String typeDetailCode;
    private String word;
    private String separatedWord;
    private String trimmedWord;
    private String createDate;
    private String displayWebPcYn;
    private String displayWebMobileYn;
    private String displayAppYn;

    private String highlight;



    @Builder
    public PromotionSuggestionDocumentDto(Long seq, String name, String typeCode, String typeDetailCode, String displayWebPcYn, String displayWebMobileYn
                                      , String displayAppYn, String createDate){
        this.seq = seq;
        this.typeCode = typeCode;
        this.typeDetailCode = typeDetailCode;
        this.word = name;
        this.separatedWord = new KoreanParser().parse(name);
        this.trimmedWord = new KoreanParser().parse(name.replaceAll(" ",""));
        this.displayWebPcYn = displayWebPcYn;
        this.displayWebMobileYn = displayWebMobileYn;
        this.displayAppYn = displayAppYn;
        this.createDate = createDate;

    }

}
