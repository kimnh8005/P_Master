package kr.co.pulmuone.v1.base.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.base.dto.vo.GoodsSearchVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "상품검색 Response")
public class GoodsSearchResponseDto {

    @ApiModelProperty(value = "상품검색 리스트")
	private	List<GoodsSearchVo> rows;

    @ApiModelProperty(value = "상품검색 카운트")
	private	long	total;
}
