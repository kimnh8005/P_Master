package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@ToString
public class GoodsMealVo {

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "품목바코드")
    private String itemBarcode;
    
    @ApiModelProperty(value = "상품코드")
    private String ilGoodsId;

    @ApiModelProperty(value = "프로모션명")
    private String promotionNm;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "공급업체")
    private String compNm;

    @ApiModelProperty(value = "표준브랜드")
    private String brandNm;

    @ApiModelProperty(value = "판매상태")
    private String saleStatus;

    @ApiModelProperty(value = "전시상태")
    private String dispYn;

    @ApiModelProperty(value = "식단 패턴 리스트")
    private String mealPatternList;
    
    // TODO 사용유무 체크
    //@ApiModelProperty(value = "식단 패턴 리스트")
    //private List<MealPatternListVo> mealPatternList;
}
