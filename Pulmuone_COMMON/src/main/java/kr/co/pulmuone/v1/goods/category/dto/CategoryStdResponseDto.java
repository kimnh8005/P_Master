package kr.co.pulmuone.v1.goods.category.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryStdVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CategoryStdResponseDto")
public class CategoryStdResponseDto extends BaseResponseDto{

	private List<CategoryStdVo> rows = new ArrayList<CategoryStdVo>();

	private CategoryStdVo detail = new CategoryStdVo();

}
