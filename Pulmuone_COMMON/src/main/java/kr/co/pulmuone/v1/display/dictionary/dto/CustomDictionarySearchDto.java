package kr.co.pulmuone.v1.display.dictionary.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CustomDictionarySearchDto extends BaseRequestPageDto {

    private String customizeWord;
    private String useYn;

    @Builder
    public CustomDictionarySearchDto(String customizeWord, String useYn) {
        this.customizeWord = customizeWord;
        this.useYn = useYn;
    }

}
