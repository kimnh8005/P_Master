package kr.co.pulmuone.v1.promotion.exhibit.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchAdditionalVo;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SelectAddGoodsVo {

    @ApiModelProperty(value = "대표상품 PK")
    private Long ilGoodsId;

	@ApiModelProperty(value = "표준브랜드 PK")
	private Long urBrandId;

    @ApiModelProperty(value = "골라담기 추가상품 정가")
	private int recommendedPrice;

    @ApiModelProperty(value = "골라담기 추가상품 판매가")
    private int salePrice;

    @ApiModelProperty(value = "추가 상품정보")
    private GoodsSearchAdditionalVo goods;

}
