package kr.co.pulmuone.v1.statics.claim.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClaimReasonStaticsVo {

    @ApiModelProperty(value = "취소사유(대)")
    private String bosClaimLargeName;

    @ApiModelProperty(value = "취소사유(중)")
    private String bosClaimMiddleName;

    @ApiModelProperty(value = "귀책처")
    private String bosClaimSmallName;

    @ApiModelProperty(value = "귀책구분")
    private String targetName;

    @ApiModelProperty(value = "취소금액")
    private Long cancelCompletePrice;

    @ApiModelProperty(value = "취소건수")
    private Integer cancelCompleteCount;

    @ApiModelProperty(value = "취소수량")
    private Integer cancelClaimCount;

    @ApiModelProperty(value = "반품금액")
    private Long returnCompletePrice;

    @ApiModelProperty(value = "반품건수")
    private Integer returnCompleteCount;

    @ApiModelProperty(value = "반품수량")
    private Integer returnClaimCount;

    @ApiModelProperty(value = "재배송금액")
    private Long exchangeCompletePrice;

    @ApiModelProperty(value = "재배송건수")
    private Integer exchangeCompleteCount;

    @ApiModelProperty(value = "재배송수량")
    private Integer exchangeClaimCount;

}
