package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MealContsVo {

    @ApiModelProperty(value = "식단컨텐츠 코드")
    private String ilGoodsDailyMealContsCd;

    @ApiModelProperty(value = "식단명")
    private String ilGoodsDailyMealNm;

    @ApiModelProperty(value = "식단분류")
    private String mallDiv;

    @ApiModelProperty(value = "식단분류명")
    private String mallDivNm;

    @ApiModelProperty(value = "알러지식단 여부")
    private String allergyYn;

    @ApiModelProperty(value = "등록수정 시간정보")
    private String createInfo;

    @ApiModelProperty(value = "등록정보")
    private String createDt;

    @ApiModelProperty(value = "수정정보")
    private String modifyDt;

    @ApiModelProperty(value = "삭제가능여부 (Y:삭제가능)")
    private String deletePossibleYn;
}
