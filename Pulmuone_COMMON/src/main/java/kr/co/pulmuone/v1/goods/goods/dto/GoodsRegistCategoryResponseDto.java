package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistCategoryVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GoodsRegistCategoryResponseDto")
public class GoodsRegistCategoryResponseDto extends BaseResponseDto{
	@ApiModelProperty(value = "")
	private	List<GoodsRegistCategoryVo> rows = new ArrayList<GoodsRegistCategoryVo>();
}
