package kr.co.pulmuone.v1.promotion.event.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class EventJoinCouponVo {

    @ApiModelProperty(value = "이벤트 참여 PK")
    private Long evEventJoinId;

    @ApiModelProperty(value = "이벤트 PK")
    private Long pmCouponId;

    @ApiModelProperty(value = "유저 PK")
    private int couponCount;

    @ApiModelProperty(value = "당첨여부")
    private int sort;

}
