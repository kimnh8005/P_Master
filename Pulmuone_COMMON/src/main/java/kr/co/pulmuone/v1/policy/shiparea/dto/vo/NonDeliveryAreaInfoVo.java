package kr.co.pulmuone.v1.policy.shiparea.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "배송불가권역 정보 VO")
public class NonDeliveryAreaInfoVo {

    @ApiModelProperty(value = "우편번호")
    private String zipCd;

    @ApiModelProperty(value = "배송불가권역 유형")
    private String undeliverableTp;

    @ApiModelProperty(value = "배송불가권역 메시지")
    private String nonDeliveryAreaMessage;

    @ApiModelProperty(value = "대체배송 여부")
    private BaseEnums.AlternateDeliveryType alternateDeliveryType;
}