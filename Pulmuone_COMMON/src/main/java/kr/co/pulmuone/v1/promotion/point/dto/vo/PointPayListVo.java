package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PointPayListVo")
public class PointPayListVo {

    @ApiModelProperty(value = "적립 회원 ID")
    private String loginId;

    @ApiModelProperty(value = "적립 회원명")
    private String userNm;

    @ApiModelProperty(value = "적립금")
    private int issueVal;
}
