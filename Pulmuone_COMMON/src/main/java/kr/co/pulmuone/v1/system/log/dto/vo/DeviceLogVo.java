package kr.co.pulmuone.v1.system.log.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DeviceLogVo {

    @ApiModelProperty(value = "사용자 환경 정보")
    private String urPcidCd;

    @ApiModelProperty(value = "탐지 내용")
    private String detectContent;

    @ApiModelProperty(value = "회원 PK List")
    private List<Long> userIdList;

}
