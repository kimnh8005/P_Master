package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "식단 정보 엑셀 MealPatternExcelRequestDto")
public class MealPatternExcelRequestDto extends BaseRequestPageDto{

    @ApiModelProperty(value = "패턴순번")
    private String patternNo;

    @ApiModelProperty(value = "세트순번")
    private String setNo;

    @ApiModelProperty(value = "세트코드")
    private String setCd;

    @ApiModelProperty(value = "세트명")
    private String setNm;

    @ApiModelProperty(value = "식단품목코드")
    private String mealContsCd;

}
