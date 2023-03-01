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
@ApiModel(description = "CategoryRequestDto")
public class CategoryRequestDto extends BaseRequestPageDto {

  @ApiModelProperty(value = "카테고리PK", required = true)
  String categoryId         ;
  @ApiModelProperty(value = "몰구분", required = true)
  String mallDivision       ;
  @ApiModelProperty(value = "카테고리명", required = false)
  String categoryName       ;
  @ApiModelProperty(value = "깊이", required = false)
  String depth              ;
  @ApiModelProperty(value = "상위카테고리PK", required = false)
  String parentsCategoryId  ;
  @ApiModelProperty(value = "성인여부", required = false)
  String adultYn            ;
  @ApiModelProperty(value = "후기작성여부", required = false)
  String feedbackYn         ;
  @ApiModelProperty(value = "PC이미지", required = false)
  String pcImage            ;
  @ApiModelProperty(value = "Mobile이미지", required = false)
  String mobileImage        ;
  @ApiModelProperty(value = "노출순서", required = false)
  String sort               ;
  @ApiModelProperty(value = "전시상품수", required = false)
  String displayGoodsCount  ;
  @ApiModelProperty(value = "전시여부", required = false)
  String displayYn          ;
  @ApiModelProperty(value = "사영여부", required = false)
  String useYn              ;
  @ApiModelProperty(value = "삭제유무", required = false)
  String deleteYn           ;
  @ApiModelProperty(value = "URL연결사용여부", required = false)
  String linkYn             ;
  @ApiModelProperty(value = "링크할URL정보", required = false)
  String linkUrl            ;
  @ApiModelProperty(value = "등록자", required = false)
  String createId           ;
  @ApiModelProperty(value = "등록일시", required = false)
  String createDt           ;
  @ApiModelProperty(value = "수정자", required = false)
  String modifyId           ;
  @ApiModelProperty(value = "수정일시", required = false)
  String modifyDt           ;
  @ApiModelProperty(value = "변경데이터", required = false)
  String updateData         ;
  @ApiModelProperty(value = "미사용포함여부", required = false)
  String useAllYn           ;
  @ApiModelProperty(value = "이전사용여부", required = false)
  String befUseYn           ;


}
