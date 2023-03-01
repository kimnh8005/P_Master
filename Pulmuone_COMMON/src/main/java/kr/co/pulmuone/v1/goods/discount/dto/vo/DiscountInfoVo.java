package kr.co.pulmuone.v1.goods.discount.dto.vo;

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
@ApiModel(description = "할인 Vo")
public class DiscountInfoVo {

	@ApiModelProperty(value = "SEQ")
    private Long ilGoodsDiscountId;

    @ApiModelProperty(value = "상품 ID")
    private Long ilGoodsId;

    @ApiModelProperty(value = "품목 코드")
    private String ilItemCode;

    @ApiModelProperty(value = "상품할인코드")
    private String discountType;

    @ApiModelProperty(value = "상품할인 시작일자")
    private String discountStartDate;

    @ApiModelProperty(value = "상품할인 종료일자")
    private String discountEndDate;

    @ApiModelProperty(value = "상품할인 유형코드")
    private String discountMethodType;

    @ApiModelProperty(value = "할인율")
    private int discountRatio;

    @ApiModelProperty(value = "할인 판매가")
    private String discountSalePrice;

    @ApiModelProperty(value = "등록자")
    private long createId;

    @ApiModelProperty(value = "수정자")
    private long modifyId;
}
