package kr.co.pulmuone.v1.promotion.exhibit.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.GiftGoodsVo;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.GiftListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GiftListResponseDto {

    @ApiModelProperty(value = "기획전 PK")
    private Long evExhibitId;

    @ApiModelProperty(value = "이벤트 제목")
    private String title;

    @ApiModelProperty(value = "상시 유무")
    private String alwaysYn;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "증정 수량")
    private int giftCount;

    @ApiModelProperty(value = "증정유형")
    private String giftType;

    @ApiModelProperty(value = "장바구니 금액")
    private int overPrice = 0;

    @ApiModelProperty(value = "장바구니 상품상태 유형")
    private String giftRangeType;

    @ApiModelProperty(value = "증정상품 증정 유형")
    private String giftGiveType;

    @ApiModelProperty(value = "배너 이미지 경로")
    private String bannerImagePath;

    @ApiModelProperty(value = "증정 상품 정보")
    private List<GiftGoodsVo> goods;

    public GiftListResponseDto(GiftListVo vo) {
        this.evExhibitId = vo.getEvExhibitId();
        this.title = vo.getTitle();
        this.alwaysYn = vo.getAlwaysYn();
        this.startDate = vo.getStartDate();
        this.endDate = vo.getEndDate();
        this.giftType = vo.getGiftType();
        this.overPrice = vo.getOverPrice();
        this.giftRangeType = vo.getGiftRangeType();
        this.giftGiveType = vo.getGiftGiveType();
        this.bannerImagePath = vo.getBannerImagePath();
        this.goods = vo.getGoods();
        this.giftCount = vo.getGiftCount();
    }
}