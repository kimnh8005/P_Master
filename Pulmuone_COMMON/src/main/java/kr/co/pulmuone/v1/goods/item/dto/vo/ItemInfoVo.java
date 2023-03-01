package kr.co.pulmuone.v1.goods.item.dto.vo;


import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@ApiModel(description = "품목상세 Vo")
public class ItemInfoVo {

    @ApiModelProperty(value = "상품 ID")
    private Long goodsId;

    @ApiModelProperty(value = "품목코드")
    private String itemCode;

    @ApiModelProperty(value = "품목유형")
    private String itemType;

    @ApiModelProperty(value = "품목명")
    private String itemName;

    @ApiModelProperty(value = "품목바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "ERP 연동여부")
    private String erpIfYn;

    @ApiModelProperty(value = "ERP 재고연동여부")
    private String erpStockIfYn;

    @ApiModelProperty(value = "표준카테고리 ID")
    private Long categoryStandardId;

    @ApiModelProperty(value = "공급처 ID")
    private Long supplierId;

    @ApiModelProperty(value = "브랜드 ID")
    private Long brandId;

    @ApiModelProperty(value = "보관방법")
    private String storageMethodType;

    @ApiModelProperty(value = "과세구분")
    private String taxYn;

    @ApiModelProperty(value = "품목가격 ID")
    private Long itemPriceId;

    @ApiModelProperty(value = "품목가격 적용시작일")
    private LocalDateTime itemPriceStartDate;

    @ApiModelProperty(value = "품목가격 적용종료일")
    private LocalDateTime itemPriceEndDate;

    @ApiModelProperty(value = "품목 원가")
    private Long itemStandardPrice;

    @ApiModelProperty(value = "품목 정상가")
    private Long itemRecommendedPrice;
}
