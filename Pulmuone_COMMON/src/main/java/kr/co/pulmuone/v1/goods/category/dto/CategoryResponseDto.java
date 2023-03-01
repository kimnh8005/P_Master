package kr.co.pulmuone.v1.goods.category.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CategoryResponseDto")
public class CategoryResponseDto extends BaseResponseDto{

	private List<CategoryVo> rows = new ArrayList<CategoryVo>();

	private CategoryVo detail = new CategoryVo();

}
