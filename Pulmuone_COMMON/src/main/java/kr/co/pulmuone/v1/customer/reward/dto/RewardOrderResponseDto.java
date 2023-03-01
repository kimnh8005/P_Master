package kr.co.pulmuone.v1.customer.reward.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class RewardOrderResponseDto {

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "보상대상 상품정보")
    private String rewardApplyStandard;

    @ApiModelProperty(value = "주문정보")
    private List<RewardOrderDto> order;

}
