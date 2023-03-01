package kr.co.pulmuone.v1.statics.user.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserGroupStaticsVo {

    @ApiModelProperty(value = "등급 마스터 명")
    private String groupMasterName;

    @ApiModelProperty(value = "등급 명")
    private String groupName;

    @ApiModelProperty(value = "구매고객 수")
    private Integer userCount;

    @ApiModelProperty(value = "판매금액")
    private Long paidPrice;

    @ApiModelProperty(value = "주문건수")
    private Integer orderCount;

    @ApiModelProperty(value = "주문 상품수량")
    private Integer goodsCount;

}
