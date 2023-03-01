package kr.co.pulmuone.v1.promotion.exhibit.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.ExhibitGroupByUserVo;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.GiftByUserVo;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.GiftGoodsVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GiftByUserResponseDto {

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

    @ApiModelProperty(value = "상세 HTML")
    private String detailHtml;

    @ApiModelProperty(value = "증정유형")
    private String giftTypeName;

    @ApiModelProperty(value = "장바구니 금액")
    private int overPrice = 0;

    @ApiModelProperty(value = "장바구니 상품상태 유형")
    private String giftRangeTypeName;

    @ApiModelProperty(value = "증정상품 증정 유형")
    private String giftGiveTypeName;

    @ApiModelProperty(value = "증정 수량")
    private int giftCount;

    @ApiModelProperty(value = "종료여부")
    private String endYn;

    @ApiModelProperty(value = "증정 상품 정보")
    private List<GiftGoodsVo> goods;

    @ApiModelProperty(value = "기획전 그룹")
    private List<ExhibitGroupByUserVo> group;

    public GiftByUserResponseDto(GiftByUserVo vo) {
        this.title = vo.getTitle();
        this.description = vo.getDescription();
        this.alwaysYn = vo.getAlwaysYn();
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.detailHtml = vo.getDetailHtml();
        this.giftTypeName = vo.getGiftTypeName();
        this.overPrice = vo.getOverPrice();
        this.giftRangeTypeName = vo.getGiftRangeTypeName();
        this.giftGiveTypeName = vo.getGiftGiveTypeName();
        this.giftCount = vo.getGiftCount();
        this.goods = vo.getGoods();
        this.endYn = vo.getEndYn();
    }

}
