package kr.co.pulmuone.v1.promotion.exhibit.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.SelectAddGoodsVo;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.SelectByUserVo;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SelectByUserResponseDto {

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "설명")
    private String description;

    @ApiModelProperty(value = "상시진행여부")
    private String alwaysYn;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "D-Day")
    private Long dday;

    @ApiModelProperty(value = "상품별 구매가능 수량")
    private int goodsBuyLimitCount;

    @ApiModelProperty(value = "기본 구매 수량")
    private int defaultBuyCount;

    @ApiModelProperty(value = "골라담기 균일가")
    private int selectPrice;

    @ApiModelProperty(value = "상세 HTML")
    private String detailHtml;

    @ApiModelProperty(value = "종료여부")
    private String endYn;

    @ApiModelProperty(value = "골라담기 상품정보")
    private List<SelectGoodsResultDto> goods;

    @ApiModelProperty(value = "골라담기 추가 상품 정보")
    private List<SelectAddGoodsVo> addGoods;

    public SelectByUserResponseDto(SelectByUserVo vo) {
        this.title = vo.getTitle();
        this.description = vo.getDescription();
        this.alwaysYn = vo.getAlwaysYn();
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.goodsBuyLimitCount = vo.getGoodsBuyLimitCount();
        this.defaultBuyCount = vo.getDefaultBuyCount();
        this.selectPrice = vo.getSelectPrice();
        this.detailHtml = vo.getDetailHtml();
        this.endYn = vo.getEndYn();
    }
}
