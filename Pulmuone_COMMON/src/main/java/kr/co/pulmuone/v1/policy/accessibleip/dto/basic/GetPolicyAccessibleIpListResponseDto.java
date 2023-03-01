package kr.co.pulmuone.v1.policy.accessibleip.dto.basic;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;

import kr.co.pulmuone.v1.policy.accessibleip.dto.vo.GetPolicyAccessibleIpListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = " GetPolicyAccessibleIpListResponseDto")
public class GetPolicyAccessibleIpListResponseDto extends BaseResponseDto {

    private List<GetPolicyAccessibleIpListResultVo> rows = new ArrayList<GetPolicyAccessibleIpListResultVo>();
    private long total;

}
