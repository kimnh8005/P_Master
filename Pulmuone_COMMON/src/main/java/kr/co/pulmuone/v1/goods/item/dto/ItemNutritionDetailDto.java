package kr.co.pulmuone.v1.goods.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "품목별 영양정보 상세항목 등록 dto")
public class ItemNutritionDetailDto {

    /*
     * 품목별 영양정보 상세항목 등록 dto
     */
    @ApiModelProperty(value = "품목코드", required = true)
    private String ilItemCode; // 품목코드 PK

    @ApiModelProperty(value = "영양정보 분류코드", required = true)
    private String nutritionCode; // 영양정보 분류코드 PK

    @ApiModelProperty(value = "영양정보 분류명", required = true)
    private String nutritionCodeName; // 영양정보 분류명

    @ApiModelProperty(value = "영양정보단위", required = true)
    private String nutritionUnit; // 영양정보 단위

    @ApiModelProperty(value = "ERP 영양성분량", required = false)
    private Double erpNutritionQuantity; // ERP 영양성분량

    @ApiModelProperty(value = "ERP 영양성분 기준치대비 함량", required = false)
    private Double erpNutritionPercent; // ERP 영양성분 기준치대비 함량

    @ApiModelProperty(value = "BOS 영양성분량", required = true)
    private Double nutritionQuantity; // BOS 영양성분량

    @ApiModelProperty(value = "BOS 영양성분 기준치대비 함량", required = true)
    private Double nutritionPercent; // BOS 영양성분 기준치대비 함량

    @ApiModelProperty(value = "정렬 순서", required = true)
    private int sort; // 정렬 순서 : 0 부터 시작함

    private Long createId; // 등록자 ID

    private Long modifyId; // 수정자 ID

}
