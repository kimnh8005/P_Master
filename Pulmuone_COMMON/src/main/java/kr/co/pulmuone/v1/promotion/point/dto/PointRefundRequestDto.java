package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.ClaimEnums;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
@AllArgsConstructor
@ApiModel(description = "적립금 환불 요청 Dto")
public class PointRefundRequestDto {

    @ApiModelProperty(value = "환불 요청 금액", required = true)
    private Long amount;
    @ApiModelProperty(value = "회원 번호", required = true)
    private Long urUserId;
    @ApiModelProperty(value = "주문 번호", required = true)
    private String orderNo;
    @ApiModelProperty(value = "클레임 PK", required = true)
    private String odClaimId;
    @ApiModelProperty(value = "환불 귀책 사유 코드")
    private ClaimEnums.ReasonAttributableType reasonAttributableType;


    private DepositPointDto depositPointDto;
    public void initDepositPointDto(long maximumAccrualAmount) {
        this.depositPointDto = depositPointDto.builder()
                .amount(maximumAccrualAmount)
                .urUserId(this.urUserId)
                .pointPaymentType(PointEnums.PointPayment.PROVISION)
                .refNo1(this.getOrderNo())
                .build();
    }

    public long masterPointUsedId() {
        return this.depositPointDto.getPmPointUsedId();
    }
}
