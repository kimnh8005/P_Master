package kr.co.pulmuone.v1.statics.user.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserCountStaticsVo {

    @ApiModelProperty(value = "회원구분")
    private String type;

    @ApiModelProperty(value = "구분")
    private String subType;

    @ApiModelProperty(value = "기준기간")
    private Integer standardCount;

    @ApiModelProperty(value = "대비기간")
    private Integer contrastTotal;

}
