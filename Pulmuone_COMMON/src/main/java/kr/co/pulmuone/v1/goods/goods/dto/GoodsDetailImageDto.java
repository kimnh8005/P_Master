package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@ApiModel(description = "상품 상세 이미지 리스트")
public class GoodsDetailImageDto {
    @ApiModelProperty(value = "업데이트 일자")
    private String modifyDate;

    @ApiModelProperty(value = "상품코드")
    private String goodsId;

    @ApiModelProperty(value = "마스터 품목코드")
    private String itemCode;

    @ApiModelProperty(value = "품목바코드")
    private String itemBarcode;

    @ApiModelProperty(value = "상품유형 코드")
    private String goodsType;

    @ApiModelProperty(value = "상품유형")
    private String goodsTypeName;

    @ApiModelProperty(value = "상품명")
    private String goodsName;

    @ApiModelProperty(value = "수정 담당자")
    private String modifyId;

    @ApiModelProperty(value = "PC 이미지 파일명")
    private String pcImgNm;

    @ApiModelProperty(value = "관리자명")
    private String chargeName;

    @ApiModelProperty(value = "관리자 ID")
    private String chargeId;

    @ApiModelProperty(value = "상품상세정보 수정여부")
    private String chgGoodsDetlYn;

    @ApiModelProperty(value = "상품고시정보 수정여부")
    private String chgSpceYn;

    @ApiModelProperty(value = "업데이트 항목 엑셀")
    private String goodsUpdateStatusExcel;

    @ApiModelProperty(value = "업데이트 항목 엑셀")
    private String chargeNameExcel;

    @ApiModelProperty(value = "업데이트 항목 엑셀")
    private String itemCodeExcel;


}
