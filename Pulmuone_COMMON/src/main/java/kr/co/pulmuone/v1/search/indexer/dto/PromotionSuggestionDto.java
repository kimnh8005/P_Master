package kr.co.pulmuone.v1.search.indexer.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromotionSuggestionDto {

    private Long id;
    private String typeCode;
    private String typeDetailCode;
    private String title;
    private String createDate;
    private String displayWebPcYn;
    private String displayWebMobileYn;
    private String displayAppYn;

}
