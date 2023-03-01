package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품리스트 Response")
public class GoodsListResponseDto {

    @ApiModelProperty(value = "상품목록")
	private	List<GoodsVo> rows;

    @ApiModelProperty(value = "상품목록 카운트")
	private	long total;
}
