package kr.co.pulmuone.v1.policy.config.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "meta data 정보 Vo")
public class MetaConfigVo {
    @ApiModelProperty(value = "정책 코드")
    private String policyKey;

    @ApiModelProperty(value = "정책 명")
    private String policyName;

    @ApiModelProperty(value = "정책 값")
    private String policyValue;
}
