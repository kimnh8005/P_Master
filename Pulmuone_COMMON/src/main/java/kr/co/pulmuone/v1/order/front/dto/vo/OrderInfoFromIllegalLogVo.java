package kr.co.pulmuone.v1.order.front.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "주문 정보 From 부정거래 탐지 Result")
public class OrderInfoFromIllegalLogVo {

    @ApiModelProperty(value = "고객 환경정보")
    private String urPcidCd;

    @ApiModelProperty(value = "주문 건수")
    private String orderCount;

    @ApiModelProperty(value = "주문 금액")
    private String orderPrice;

    @ApiModelProperty(value = "회원 PK List")
    private List<Long> userIdList;

    @ApiModelProperty(value = "주문 PK List")
    private List<Long> orderIdList;

}
