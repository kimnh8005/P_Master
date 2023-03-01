package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsPackageListVo {

	@ApiModelProperty(value = "상품 ID")
    private String goodsId;

    @ApiModelProperty(value = "품목코드")
    private String itemCd;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "상품유형 공통코드(GOODS_TYPE)")
    private String goodsTp;

    @ApiModelProperty(value = "상품유형 공통코드(GOODS_TYPE)명")
    private String goodsTpNm;

    @ApiModelProperty(value = "프로모션명")
    private String promotionName;

    @ApiModelProperty(value = "판매유형 공통코드(SALE_TYPE)")
    private String saleTp;

    @ApiModelProperty(value = "판매유형 공통코드(SALE_TYPE)명")
    private String saleTpNm;

    @ApiModelProperty(value = "상품가격 ID")
    private String goodsPriceId;

    @ApiModelProperty(value = "판매 시작일")
    private String saleStartDate;

    @ApiModelProperty(value = "판매 종료일")
    private String saleEndDate;

    @ApiModelProperty(value = "상품 등록일")
    private String goodsCreateDate;

    @ApiModelProperty(value = "상품 종료일")
    private String goodsModifyDate;

    @ApiModelProperty(value = "전시 카테고리 ID")
    private String categoryId;

    @ApiModelProperty(value = "판매상태코드")
    private String saleStatus;

    @ApiModelProperty(value = "판매상태코드명")
    private String saleStatusName;

    @ApiModelProperty(value = "전시여부")
    private String dispYn;

    @ApiModelProperty(value = "전시 카테고리 명")
    private String categoryDepthName;

    @ApiModelProperty(value = "품목가격 ID")
    private String itemPriceId;

    @ApiModelProperty(value = "상품할인 ID")
    private String goodsDiscountId;

    @ApiModelProperty(value = "원가")
    private int standardPrice;

    @ApiModelProperty(value = "정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "판매가")
    private int salePrice;

    @ApiModelProperty(value = "원가")
    private String standardPriceStr;

    @ApiModelProperty(value = "정상가")
    private String recommendedPriceStr;

    @ApiModelProperty(value = "판매가")
    private String salePriceStr;

    @ApiModelProperty(value = "할인유형코드")
    private String discountTypeCode;

    @ApiModelProperty(value = "할인유형명")
    private String discountTypeName;

    @ApiModelProperty(value = "묶음 상품 부모")
    private String parentGoodsId;

    @ApiModelProperty(value = "묶음 상품 상품코드")
    private String targetGoodsId;

    @ApiModelProperty(value = "묶음 상품 수량")
    private String goodsQty;

    @ApiModelProperty(value = "썸네일 주소")
    private String goodsImagePath;

}
