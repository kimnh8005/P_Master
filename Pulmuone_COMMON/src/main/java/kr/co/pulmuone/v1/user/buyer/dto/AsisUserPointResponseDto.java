package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AsisUserPointResponseDto {

    @ApiModelProperty(value = "ASIS 풀무원샵 적립금")
    private int pulmuonePoint;

    @ApiModelProperty(value = "ASIS 올가 적립금")
    private int orgaPoint;

    @ApiModelProperty(value = "부분 적립금")
    private int pointPartialDeposit;

}
