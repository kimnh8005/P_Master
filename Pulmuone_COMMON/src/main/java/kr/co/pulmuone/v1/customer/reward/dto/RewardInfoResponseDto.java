package kr.co.pulmuone.v1.customer.reward.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class RewardInfoResponseDto {

    @ApiModelProperty(value = "보상제명")
    private String rewardName;

    @ApiModelProperty(value = "유의사항")
    private String rewardNotice;

    @ApiModelProperty(value = "보상대상 상품정보")
    private String rewardApplyStandard;

}
