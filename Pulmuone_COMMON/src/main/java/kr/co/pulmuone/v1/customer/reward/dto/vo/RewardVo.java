package kr.co.pulmuone.v1.customer.reward.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RewardVo {

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "보상대상 상품정보")
    private String rewardApplyStandard;

    @ApiModelProperty(value = "주문인정기간")
    private Integer orderApprPeriod;

    @ApiModelProperty(value = "보상제 상품유형")
    private String rewardGoodsType;

}
