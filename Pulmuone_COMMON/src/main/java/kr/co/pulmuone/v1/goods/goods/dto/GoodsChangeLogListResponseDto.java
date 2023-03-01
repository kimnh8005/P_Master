package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsChangeLogListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품 업데이트 내역 Response")
public class GoodsChangeLogListResponseDto {

	@ApiModelProperty(value = "상품 업데이트 내역 목록")
	private	List<GoodsChangeLogListVo> rows;

	@ApiModelProperty(value = "상품 업데이트 내역 카운트")
	private	long total;
}
