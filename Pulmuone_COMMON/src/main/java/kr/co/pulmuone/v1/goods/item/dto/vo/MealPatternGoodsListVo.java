package kr.co.pulmuone.v1.goods.item.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "식단 패턴 연결상품 MealPatternGoodsListVo")
public class MealPatternGoodsListVo {

    @ApiModelProperty(value = "품목 코드")
    private String ilItemCd;

	@ApiModelProperty(value = "상품 코드")
    private long ilGoodsId;
	
	@ApiModelProperty(value = "상품명")
    private String goodsNm;

	@ApiModelProperty(value = "표준브랜드 명(몰 구분 : MALL_DIV.BABYMEAL, MALL_DIV.EATSLIM)")
    private String mallDivNm;
	
	@ApiModelProperty(value = "판매상태")
    private String saleStatusNm;
	
	@ApiModelProperty(value = "전시상태")
    private String displayYnNm;
	
	@ApiModelProperty(value = "등록자 ID")
    private long createId;
	
	@ApiModelProperty(value = "수정자 ID")
    private long modifyId;

	@ApiModelProperty(value = "표준브랜드 코드(몰 구분 : MALL_DIV.BABYMEAL, MALL_DIV.EATSLIM)")
    private String mallDiv;
	
  
}
