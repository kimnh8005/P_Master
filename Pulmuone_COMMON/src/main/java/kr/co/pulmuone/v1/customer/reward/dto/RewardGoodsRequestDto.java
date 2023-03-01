package kr.co.pulmuone.v1.customer.reward.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.MallBaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RewardGoodsRequestDto extends MallBaseRequestPageDto {

    @ApiModelProperty(value = "고객 보상제 PK")
    private Long csRewardId;

    @ApiModelProperty(value = "검색엔진 사용여부")
    private String searchYn;

    @ApiModelProperty(value = "검색어")
    private String keyword;

}
