package kr.co.pulmuone.v1.batch.order.front.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "주문 정보 From 회원등급 Result")
public class OrderInfoFromUserGroupBatchVo {

    @ApiModelProperty(value = "유저 PK")
    private long urUserId;

    @ApiModelProperty(value = "주문 건수")
    private int orderCount = 0;

    @ApiModelProperty(value = "주문 금액")
    private int paidPriceSum = 0;

}
