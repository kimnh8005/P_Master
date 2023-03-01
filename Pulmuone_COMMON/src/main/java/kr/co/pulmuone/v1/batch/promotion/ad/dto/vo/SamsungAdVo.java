package kr.co.pulmuone.v1.batch.promotion.ad.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@ApiModel(description = "SamsungAdVo")
public class SamsungAdVo {

    @ApiModelProperty(value = "상품번호")
    @JsonProperty("PRODUCTID")
    private Long productId;

    @ApiModelProperty(value = "상품명")
    @JsonProperty("NAME")
    private String name;

    @ApiModelProperty(value = "회사명")
    @JsonProperty("COMPANY")
    private String company;

    @ApiModelProperty(value = "이미지주소")
    @JsonProperty("IMAGE")
    private String image;

    @ApiModelProperty(value = "상품주소")
    @JsonProperty("URL")
    private String url;

    @ApiModelProperty(value = "바코드")
    @JsonProperty("BARCODE")
    private String barCode;

    @ApiModelProperty(value = "원가")
    @JsonProperty("ORIGINALPRICE")
    private double originalPrice;

    @ApiModelProperty(value = "판매가격")
    @JsonProperty("SALEPRICE")
    private double salePrice;

    @ApiModelProperty(value = "???")
    @JsonProperty("INGREDIENTS")
    private String ingredients;

    @ApiModelProperty(value = "카테고리")
    @JsonProperty("CATEGORY")
    private String category;

    @ApiModelProperty(value = "판매중여부")
    @JsonProperty("INVENTORY")
    private String inventory;

    @ApiModelProperty(value = "유효기간")
    @JsonProperty("EXPIRATIONDATE")
    private String expirationDate;

    @ApiModelProperty(value = "브랜드")
    @JsonProperty("BRAND")
    private String brand;

    @ApiModelProperty(value = "서브카테고리")
    @JsonProperty("SUBCATEGORY")
    private String subCategory;

    @ApiModelProperty(value = "상품 리뷰 평점")
    @JsonProperty("RATING")
    private float rating;

    @ApiModelProperty(value = "리뷰수")
    @JsonProperty("REVIEWS_COUNT")
    private int reviewsCount;

    @ApiModelProperty(value = "리뷰상세URL")
    @JsonProperty("REVIEWS_URL")
    private String reviewsUrl;

}
