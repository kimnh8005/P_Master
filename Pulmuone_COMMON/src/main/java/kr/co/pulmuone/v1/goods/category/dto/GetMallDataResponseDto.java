package kr.co.pulmuone.v1.goods.category.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.category.dto.vo.GetCategoryListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "레이아웃 몰별 데이타 응답 Dto")
public class GetMallDataResponseDto
{

	@ApiModelProperty(value = "카테고리 리스트 정보")
	List<GetCategoryListResultVo> category;


}
