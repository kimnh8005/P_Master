package kr.co.pulmuone.v1.promotion.exhibit.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SelectListByUserVo {

    @ApiModelProperty(value = "기획전 PK")
    private Long evExhibitId;

    @ApiModelProperty(value = "배너 이미지 경로")
    private String bannerImagePath;

    @ApiModelProperty(value = "배너 이미지 원본 명")
    private String bannerImageOriginalName;

    @ApiModelProperty(value = "기획명")
    private String title;

    @ApiModelProperty(value = "설명")
    private String description;

    @ApiModelProperty(value = "상시 진행 여부")
    private String alwaysYn;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "기본 구매수량")
    private String defaultBuyCount;

    @ApiModelProperty(value = "골라담기 균일가")
    private String selectPrice;

    @ApiModelProperty(value = "최대할인율")
    private String maxDiscountRate;

    @ApiModelProperty(value = "상품별 구매 가능 수량")
    private String goodsBuyLimitCount;
    
    @ApiModelProperty(value = "대표상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "종료여부")
    private String endYn;
    
    @ApiModelProperty(value = "대표상품 정보")
    private GoodsSearchResultDto mainGoods;

    @ApiModelProperty(value = "골라담기 상품정보")
    private List<GoodsSearchResultDto> goods;

    @ApiModelProperty(value = "추가 상품정보")
    private List<SelectAddGoodsVo> addGoods;

}
