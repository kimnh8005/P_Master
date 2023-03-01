package kr.co.pulmuone.v1.goods.category.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryVo {


  @ApiModelProperty(value = "카테고리PK")
	String categoryId     ;

  @ApiModelProperty(value = "몰구분")
  String mallDivision       ;

  @ApiModelProperty(value = "카테고리명")
  String categoryName       ;

  @ApiModelProperty(value = "깊이")
  String depth         ;

  @ApiModelProperty(value = "상위카테고리PK")
  String parentsCategoryId  ;

  @ApiModelProperty(value = "성인여부")
  String adultYn       ;

  @ApiModelProperty(value = "후기작성여부")
  String feedbackYn    ;

  @ApiModelProperty(value = "PC이미지")
  String pcImage         ;

  @ApiModelProperty(value = "Mobile이미지")
  String mobileImage         ;

  @ApiModelProperty(value = "노출순서")
  String sort          ;

  @ApiModelProperty(value = "전시상품수")
  String displayGoodsCount   ;

	@ApiModelProperty(value = "전시여부")
	String displayYn        ;

  @ApiModelProperty(value = "사용여부")
  String useYn         ;

  @ApiModelProperty(value = "삭제유무")
  String deleteYn         ;

	@ApiModelProperty(value = "URL연결사용여부")
	String linkYn        ;

	@ApiModelProperty(value = "링크할URL정보")
	String linkUrl       ;

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
