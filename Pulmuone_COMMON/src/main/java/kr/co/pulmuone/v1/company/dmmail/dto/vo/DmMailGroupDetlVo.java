package kr.co.pulmuone.v1.company.dmmail.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DmMailGroupDetlVo {

    @ApiModelProperty(value = "DM메일 전시그룹 상세 PK")
    private String dmMailGroupDetlId;

    @ApiModelProperty(value = "DM메일 전시그룹 PK")
    private String dmMailGroupId;

    @ApiModelProperty(value = "순서")
    private int goodsSort;

    @ApiModelProperty(value = "상품 PK")
    private String ilGoodsId;

    @ApiModelProperty(value = "상품 유형")
    private String goodsTp;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "상품명상세")
    private String goodsName;

    @ApiModelProperty(value = "상품 이미지 경로")
    private String goodsImagePath;

    @ApiModelProperty(value = "원가")
    private String standardPrice;

    @ApiModelProperty(value = "정상가")
    private String recommendedPrice;

    @ApiModelProperty(value = "판매가")
    private String salePrice;

    @ApiModelProperty(value = "할인율")
    private int discountRate;

    @ApiModelProperty(value = "임직원 판매가")
    private String employeeSalePrice;

    @ApiModelProperty(value = "임직원 할인율")
    private int employeeDiscountRate;

    @ApiModelProperty(value = "할인 유형")
    private String discountTp;

    @ApiModelProperty(value = "판매상태")
    private String saleStatus;

    @ApiModelProperty(value = "출고처 PK")
    private String urWarehouseId;

    @ApiModelProperty(value = "출고처명")
    private String warehouseNm;

    @ApiModelProperty(value = "배송정책 PK")
    private String ilShippingTmplId;

    @ApiModelProperty(value = "배송정책명")
    private String shippingTemplateName;

    @ApiModelProperty(value = "증정품 여부")
    private String bundleYn;

    @ApiModelProperty(value = "상품유형명")
    private String goodsTpNm;

    @ApiModelProperty(value = "할인 유형명")
    private String discountTpNm;

    @ApiModelProperty(value = "판매상태명")
    private String saleStatusNm;

    @ApiModelProperty(value = "상품 큰이미지 경로")
    private String goodsBigImagePath;

    @ApiModelProperty(value = "전시 브랜드 명")
    private String dpBrandNm;
}
