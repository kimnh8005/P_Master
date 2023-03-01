package kr.co.pulmuone.v1.goods.category.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryStdVo {


  @ApiModelProperty(value = "표준카테고리PK")
	String standardCategoryId  ;

  @ApiModelProperty(value = "카테고리명")
  String standardCategoryName    ;

  @ApiModelProperty(value = "깊이")
  String depth         ;

  @ApiModelProperty(value = "상위표준카테고리PK")
  String parentsCategoryId  ;

  @ApiModelProperty(value = "노출순서")
  String sort          ;

  @ApiModelProperty(value = "사용여부")
  String useYn         ;

	@ApiModelProperty(value = "반품가능여부")
	String returnableYn  ;

	@ApiModelProperty(value = "삭제유무")
	String deleteYn         ;

  @ApiModelProperty(value = "등록자")
  String createId      ;

	@ApiModelProperty(value = "등록일시")
	String createDt      ;

  @ApiModelProperty(value = "수정자")
  String modifyId      ;

	@ApiModelProperty(value = "수정일시")
	String modifyDt      ;

  @ApiModelProperty(value = "하위존재여부")
  String isleaf        ;

}
