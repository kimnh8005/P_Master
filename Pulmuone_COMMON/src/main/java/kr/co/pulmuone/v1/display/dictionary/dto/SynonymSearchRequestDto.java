package kr.co.pulmuone.v1.display.dictionary.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SynonymSearchRequestDto extends BaseRequestPageDto {

    private String representSynonym;
    private String useYn;
}
