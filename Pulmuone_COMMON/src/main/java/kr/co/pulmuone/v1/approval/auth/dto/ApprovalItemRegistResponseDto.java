package kr.co.pulmuone.v1.approval.auth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ApprovalItemRegistResponseDto {

    @ApiModelProperty(value = "실패 건수")
    private int failCount;

    @ApiModelProperty(value = "실패 Code")
    private String failMessageCode;

}
