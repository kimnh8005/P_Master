package kr.co.pulmuone.v1.customer.reward.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RewardApplyRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "조회 시작일자")
    private String startDate;

    @ApiModelProperty(value = "조회 종료일자")
    private String endDate;

    @ApiModelProperty(value = "유저 PK", hidden = true)
    private Long urUserId;

}
