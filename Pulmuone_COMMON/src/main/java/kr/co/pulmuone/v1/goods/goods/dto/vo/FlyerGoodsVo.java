package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@ApiModel(description = "FlyerGoodsVo")
public class FlyerGoodsVo {

    @ApiModelProperty(value = "상품번호")
    private String ilGoodsId;

    @ApiModelProperty(value = "품목코드")
    private String ilItemCd;

    @ApiModelProperty(value = "상품명")
    private String goodsNm;

    @ApiModelProperty(value = "정상가")
    private int recommendedPrice;

    @ApiModelProperty(value = "판매가")
    private int salePrice;

    @ApiModelProperty(value = "할인율")
    private int discountRate;

    @ApiModelProperty(value = "임직원 판매가")
    private int employeeSalePrice;

    @ApiModelProperty(value = "임직원 할인율")
    private int employeeDiscountRate;

    @ApiModelProperty(value = "할인유형(NORMAL : 일반회원, EMPLOYEE : 임직원)")
    private String discountTp;

    @ApiModelProperty(value = "")
    private List<Map> insertData;
}
