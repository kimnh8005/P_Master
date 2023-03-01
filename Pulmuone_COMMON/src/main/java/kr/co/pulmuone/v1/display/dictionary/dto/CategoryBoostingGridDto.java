package kr.co.pulmuone.v1.display.dictionary.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;

@Getter
public class CategoryBoostingGridDto extends BaseResponseDto {

    private int no;

    private String dpCtgryBoostingId;

    private String ilCtgryId;

    private String ctgryName;

    private String searchWord;

    private String boostingScore;

    private String useYn;
}
