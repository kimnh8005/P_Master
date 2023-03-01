package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 외부몰 결제 정보 조회")
public class OrderClaimOutmallPaymentInfoDto {

    @ApiModelProperty(value = "주문유형")
    private String agentTypeCd;

    @ApiModelProperty(value = "결제방법")
    private String payTp;
}
