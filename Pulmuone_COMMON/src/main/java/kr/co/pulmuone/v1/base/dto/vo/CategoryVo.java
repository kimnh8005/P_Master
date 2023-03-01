package kr.co.pulmuone.v1.base.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "카테고리 Vo")
public class CategoryVo {

    @ApiModelProperty(value = "카테고리 ID")
    private Long categoryId;

    @ApiModelProperty(value = "몰구분")
    private String mallDiv;

    @ApiModelProperty(value = "카테고리명")
    private String categoryName;

    @ApiModelProperty(value = "카테고리레벨")
    private int depth;

    @ApiModelProperty(value = "상위 카테고리 ID")
    private Long parentsCategoryId;

    @ApiModelProperty(value = "전시여부")
    private String displayYn;

    @ApiModelProperty(value = "성인여부")
    private String adultYn;

    @ApiModelProperty(value = "후기작성여부")
    private String feedbackYn;
}
