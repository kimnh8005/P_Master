package kr.co.pulmuone.v1.goods.brand.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DpBrandListResultVo {

    @ApiModelProperty(value = "브랜드 PK")
    private int dpBrandId;

    @ApiModelProperty(value = "브랜드명")
    private String brandName;

    @ApiModelProperty(value = "모바일 메인 이미지")
    private String mobileMainImage;

    @ApiModelProperty(value = "PC 메인 이미지")
    private String pcMainImage;

    @ApiModelProperty(value = "모바일 메인 오버 이미지")
    private String mobileMainOverImage;

    @ApiModelProperty(value = "PC 메인 오버 이미지")
    private String pcMainOverImage;

    @ApiModelProperty(value = "모바일 타이블 배너 이미지")
    private String mobileTitleBannerImage;

    @ApiModelProperty(value = "PC 타이블 배너 이미지")
    private String pcTitleBannerImage;

    @ApiModelProperty(value = "상품 존재 여부")
    private String existGoodsYn;

}
