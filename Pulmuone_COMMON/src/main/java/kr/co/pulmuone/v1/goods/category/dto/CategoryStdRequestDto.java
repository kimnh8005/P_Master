package kr.co.pulmuone.v1.goods.category.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "CategoryStdRequestDto")
public class CategoryStdRequestDto extends BaseRequestPageDto {

  @ApiModelProperty(value = "표준카테고리PK", required = true)
  String standardCategoryId   ;
  @ApiModelProperty(value = "카테고리명", required = false)
  String standardCategoryName ;
  @ApiModelProperty(value = "깊이", required = false)
  String depth                ;
  @ApiModelProperty(value = "상위표준카테고리PK", required = false)
  String parentsCategoryId    ;
  @ApiModelProperty(value = "노출순서", required = false)
  String sort                 ;
  @ApiModelProperty(value = "사용여부", required = false)
  String useYn                ;
  @ApiModelProperty(value = "반품가능여부", required = false)
  String returnableYn         ;
  @ApiModelProperty(value = "삭제유무", required = false)
  String deleteYn             ;
  @ApiModelProperty(value = "등록자", required = false)
  String createId             ;
  @ApiModelProperty(value = "등록일시", required = false)
  String createDt             ;
  @ApiModelProperty(value = "수정자", required = false)
  String modifyId             ;
  @ApiModelProperty(value = "수정일시", required = false)
  String modifyDt             ;
  @ApiModelProperty(value = "변경데이터", required = false)
  String updateData           ;
  @ApiModelProperty(value = "미사용포함여부", required = false)
  String useAllYn             ;

}
