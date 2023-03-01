package kr.co.pulmuone.v1.promotion.exhibit.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GiftListVo {

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

    @ApiModelProperty(value = "증정대상 유형")
    private String giftTargetType;

    @ApiModelProperty(value = "배너 이미지 경로")
    private String bannerImagePath;

    @ApiModelProperty(value = "증정 상품 정보")
    private List<GiftGoodsVo> goods;

    @ApiModelProperty(value = "증정 대상 상품")
    private List<Long> targetGoods;

    @ApiModelProperty(value = "증정 대상 브랜드")
    private List<Long> targetBrand;

}