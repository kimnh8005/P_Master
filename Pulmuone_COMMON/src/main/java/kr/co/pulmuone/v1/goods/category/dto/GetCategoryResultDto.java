package kr.co.pulmuone.v1.goods.category.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "카테고리 정보 ResultDto")
public class GetCategoryResultDto
{

	@ApiModelProperty(value = "카테고리 PK")
	private Long ilCategoryId;

	@ApiModelProperty(value = "카테고리명")
	private String categoryName;

	@ApiModelProperty(value = "몰구분 ID")
	private String mallId;

	@ApiModelProperty(value = "대 카테고리 PK")
	private String lev1CategoryId;

	@ApiModelProperty(value = "중 카테고리 PK")
	private String lev2CategoryId;

	@ApiModelProperty(value = "소 카테고리 PK")
	private String lev3CategoryId;

	@ApiModelProperty(value = "세분류 카테고리 PK")
	private String lev4CategoryId;


}
