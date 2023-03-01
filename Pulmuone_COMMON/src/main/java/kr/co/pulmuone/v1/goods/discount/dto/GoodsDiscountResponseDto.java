package kr.co.pulmuone.v1.goods.discount.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.discount.dto.vo.GoodsDiscountVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemInfoVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품할인 Response")
public class GoodsDiscountResponseDto extends BaseResponseDto{

    @ApiModelProperty(value = "품목상세")
    private ItemInfoVo itemInfo;

    @ApiModelProperty(value = "상품할인목록")
	private	List<GoodsDiscountVo> goodsDiscountList;


}
