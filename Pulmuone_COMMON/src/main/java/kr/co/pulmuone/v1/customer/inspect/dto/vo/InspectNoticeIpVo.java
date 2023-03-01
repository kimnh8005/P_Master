package kr.co.pulmuone.v1.customer.inspect.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InspectNoticeIpVo {

    @ApiModelProperty(value = "정검IP 시퀀스")
    private int csInspectNoticeIpId;

    @ApiModelProperty(value = "정검 시퀀스")
    private int csInspectNoticeId;

    @ApiModelProperty(value = "IP 주소")
    private String ipAddress;

    @ApiModelProperty(value = "생성 아이디")
    private long createId;

    @ApiModelProperty(value = "생성 일자")
    private String createDt;

}
