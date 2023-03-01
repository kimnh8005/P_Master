package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "식단 패턴 패턴정보 MealPatternDetailListVo")
public class MealPatternDetailListVo {

    @ApiModelProperty(value = "식단 패턴 상세ID")
    private long patternDetlId;

    @ApiModelProperty(value = "패턴순번")
    private int patternNo;

    @ApiModelProperty(value = "세트순번")
    private int setNo;

    @ApiModelProperty(value = "세트코드")
    private String setCd;

    @ApiModelProperty(value = "세트명")
    private String setNm;

    @ApiModelProperty(value = "식단품목코드")
    private String mealContsCd;

    @ApiModelProperty(value = "식단품목명")
    private String mealNm;
    
    @ApiModelProperty(value = "알러지 여부")
    private String allergyYn;

    @ApiModelProperty(value = "수정일")
    private String modifyDt;

    @ApiModelProperty(value = "식단패턴기본정보 코드")
    private String patternCd;

    @ApiModelProperty(value = "등록자 ID")
    private long createId;
    
    @ApiModelProperty(value = "수정자 ID")
    private long modifyId;
  
}
