package kr.co.pulmuone.v1.calculate.employee.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.*;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 임직원정산 마스터 확정 업데이트 Request Dto
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "SettleEmployeeMasterConfirmRequestDto")
public class SettleEmployeeMasterConfirmRequestDto extends BaseRequestPageDto {

    @ApiModelProperty(value = "정산월")
    private String settleMonth;

    @ApiModelProperty(value = "OU코드")
    private String ouId;

    @ApiModelProperty(value = "세션아이디")
    private long sessionId;

    @ApiModelProperty(value = "사용자 Login Id")
    private String userId;

    @Builder
    public SettleEmployeeMasterConfirmRequestDto(String settleMonth, String ouId, long sessionId, String userId) {
        this.settleMonth = settleMonth;
        this.ouId = ouId;
        this.sessionId = sessionId;
        this.userId = userId;
    }

}
