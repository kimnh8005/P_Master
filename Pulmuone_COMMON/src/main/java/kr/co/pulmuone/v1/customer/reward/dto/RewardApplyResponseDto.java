package kr.co.pulmuone.v1.customer.reward.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyListVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class RewardApplyResponseDto {

    @ApiModelProperty(value = "총 건수")
    private Long total;

    @ApiModelProperty(value = "신청내역")
    private List<RewardApplyListVo> reward;

}
