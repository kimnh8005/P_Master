package kr.co.pulmuone.v1.batch.promotion.point.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PointExpiredListVo {

    @ApiModelProperty(value = "유효기간")
    private Long urUserId;

    @ApiModelProperty(value = "유효기간")
    private String expirationDate;

    @ApiModelProperty(value = "금액")
    private String amount;

}
