package kr.co.pulmuone.batch.erp.domain.service.send.template.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchSmsTargetVo {
    
    @ApiModelProperty(value = "유저 Pk")
    private Long urUserId;

    @ApiModelProperty(value = "핸드폰 번호")
    private String mobile;

}
