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
public class RewardPageInfoVo {

    @ApiModelProperty(value = "보상제코드 PK")
    private Long csRewardId;

    @ApiModelProperty(value = "보상제명")
    private String rewardName;

    @ApiModelProperty(value = "시작일자")
    private String startDate;

    @ApiModelProperty(value = "종료일자")
    private String endDate;

    @ApiModelProperty(value = "상시진행여부")
    private String alwaysYn;

    @ApiModelProperty(value = "보상제내용")
    private String detailHtml;

	@ApiModelProperty(value = "유의사항")
	private String rewardNotice;

    @ApiModelProperty(value = "보상대상 상품정보")
    private String rewardApplyStandard;

}
