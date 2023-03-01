package kr.co.pulmuone.v1.display.dictionary.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchWordLogConditionDto extends BaseRequestPageDto{

    private String startCreateDate;
    private String endCreateDate;
}
