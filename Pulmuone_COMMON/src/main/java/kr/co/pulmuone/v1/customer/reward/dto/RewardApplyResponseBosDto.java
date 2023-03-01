package kr.co.pulmuone.v1.customer.reward.dto;

import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class RewardApplyResponseBosDto {

    private long total;

    private List<RewardApplyVo> rows;

    private RewardApplyVo row;
}
