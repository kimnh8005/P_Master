package kr.co.pulmuone.v1.promotion.shoplive.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShopliveGoodsVo {
    @ApiModelProperty(value = "이벤트 전시그룹 PK")
    private String evShopliveGoodsId;

    @ApiModelProperty(value = "이벤트 전시그룹 상세 PK")
    private String evShopliveId;

    @ApiModelProperty(value = "샵라이브 연동코드")
    private String campaignKey;

    @ApiModelProperty(value = "순서")
    private int goodsSort;

    @ApiModelProperty(value = "상품 PK")
    private String ilGoodsId;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "전시브랜드명")
    private String dpBrandNm;

    @ApiModelProperty(value = "상품유형")
    private String goodsTp;

    @ApiModelProperty(value = "상품설명")
    private String description;

    @ApiModelProperty(value = "원가")
    private int standardPrice;

    @ApiModelProperty(value = "정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "판매가")
    private int salePrice;

    @ApiModelProperty(value = "할인유형")
    private String discountTp;

    @ApiModelProperty(value = "상품이미지")
    private String goodsImagePath;

    @ApiModelProperty(value = "그룹순번")
    private int groupSort;

    @ApiModelProperty(value = "상품 상태")
    private String goodsStatus;
}
