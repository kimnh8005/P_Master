package kr.co.pulmuone.v1.goods.category.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.category.dto.vo.GetCategoryListResultVo;
import kr.co.pulmuone.v1.goods.category.dto.vo.GetSubCategoryListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CategoryListResponseDto {

    /**
     * 카테고리 PK
     */
    private int ilCategoryId;

    /**
     * 카테고리 명
     */
    private String categoryName;

    /**
     * 카테고리 PC 이미지
     */

    private String pcImage;

    /**
     * 카테고리 MOBILE 이미지
     */
    private String mobileImage;

    @ApiModelProperty(value = "level2 정보")
    private List<?> level2;

    public CategoryListResponseDto(GetCategoryListResultVo vo) {
        this.ilCategoryId = vo.getIlCategoryId();
        this.categoryName = vo.getCategoryName();
        this.pcImage = vo.getPcImage();
        this.mobileImage = vo.getMobileImage();
    }

}
