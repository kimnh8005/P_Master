package kr.co.pulmuone.v1.goods.category.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryExcelVo {


  @ApiModelProperty(value = "카테고리PK")
	String categoryId     ;

  @ApiModelProperty(value = "깊이")
  String depth         ;

  @ApiModelProperty(value = "0DEPTH 카테고리 PK")
  String categoryId0  ;

	@ApiModelProperty(value = "1DEPTH 카테고리 PK")
	String categoryId1  ;

	@ApiModelProperty(value = "2DEPTH 카테고리 PK")
	String categoryId2  ;

	@ApiModelProperty(value = "3DEPTH 카테고리 PK")
	String categoryId3  ;

	@ApiModelProperty(value = "4DEPTH 카테고리 PK")
	String categoryId4  ;

	@ApiModelProperty(value = "5DEPTH 카테고리 PK")
	String categoryId5  ;

  @ApiModelProperty(value = "0DEPTH 카테고리명")
  String categoryName0  ;

	@ApiModelProperty(value = "1DEPTH 카테고리명")
	String categoryName1  ;

	@ApiModelProperty(value = "2DEPTH 카테고리명")
	String categoryName2  ;

	@ApiModelProperty(value = "3DEPTH 카테고리명")
	String categoryName3  ;

	@ApiModelProperty(value = "4DEPTH 카테고리명")
	String categoryName4  ;

	@ApiModelProperty(value = "5DEPTH 카테고리명")
	String categoryName5  ;

	@ApiModelProperty(value = "0DEPTH 노출순서")
	String sort0     ;

	@ApiModelProperty(value = "1DEPTH 노출순서")
	String sort1     ;

	@ApiModelProperty(value = "2DEPTH 노출순서")
	String sort2     ;

	@ApiModelProperty(value = "3DEPTH 노출순서")
	String sort3     ;

	@ApiModelProperty(value = "4DEPTH 노출순서")
	String sort4     ;

	@ApiModelProperty(value = "5DEPTH 노출순서")
	String sort5     ;

}
