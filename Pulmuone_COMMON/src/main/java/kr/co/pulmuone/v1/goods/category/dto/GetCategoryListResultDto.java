package kr.co.pulmuone.v1.goods.category.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.category.dto.vo.GetCategoryListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetCategoryListResultDto")
public class GetCategoryListResultDto
{

	private List<GetCategoryListResultVo> category;

	private int total;

}
