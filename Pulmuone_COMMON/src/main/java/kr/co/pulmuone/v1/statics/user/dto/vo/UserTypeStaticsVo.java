package kr.co.pulmuone.v1.statics.user.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserTypeStaticsVo {

    @ApiModelProperty(value = "구분")
    private String gubun;

    @ApiModelProperty(value = "구매고객 수")
    private Integer standardUserCount;

    @ApiModelProperty(value = "매출금액")
    private Long standardPaidPrice;

    @ApiModelProperty(value = "주문건수")
    private Integer standardOrderCount;

    @ApiModelProperty(value = "주문 상품수량")
    private Integer standardGoodsCount;

    @ApiModelProperty(value = "구매고객 수")
    private Integer contrastUserCount;

    @ApiModelProperty(value = "매출금액")
    private Long contrastPaiPrice;

    @ApiModelProperty(value = "주문건수")
    private Integer contrastOrderCount;

    @ApiModelProperty(value = "주문 상품수량")
    private Integer contrastGoodsCount;

}
