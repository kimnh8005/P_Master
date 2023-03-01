package kr.co.pulmuone.v1.policy.benefit.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeePastInfoByUserVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "임직원 혜택 관리 과거 내역 조회 by User response Dto")
public class PolicyBenefitEmployeePastInfoByUserResponseDto {

    @ApiModelProperty(value = "사용금액 합계")
    private int sumAmount;

    @ApiModelProperty(value = "임직원 혜택 과거내역 정보")
    private List<PolicyBenefitEmployeePastInfoByUserVo> rows;

}
