package kr.co.pulmuone.v1.display.dictionary.dto.vo;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryBoostingVo extends BaseRequestDto {

    private String dpCtgryBoostingId;

    private String ilCtgryId;

    private String ctgryName;

    private String searchWord;

    private int boostingScore;

    private String useYn;
}
