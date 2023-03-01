package kr.co.pulmuone.v1.customer.reward.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RewardApplyInfoVo {

    @ApiModelProperty(value = "총 신청 건수")
    private Integer totalCount;

    @ApiModelProperty(value = "신청완료 건수")
    private Integer acceptCount;

    @ApiModelProperty(value = "확인중 건수")
    private Integer confirmCount;

    @ApiModelProperty(value = "처리완료 건수")
    private Integer completeCount;

}
