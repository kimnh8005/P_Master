package kr.co.pulmuone.v1.system.menu.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetSystemAuthorityResultVo")
public class GetSystemAuthorityResultVo {

    @ApiModelProperty(value = "")
    String code;

    @ApiModelProperty(value = "")
    String comment;
}
