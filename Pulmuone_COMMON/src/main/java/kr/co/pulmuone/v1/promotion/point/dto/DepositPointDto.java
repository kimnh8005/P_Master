package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel(description = "적립금 적립 Dto")
public class DepositPointDto {

    @ApiModelProperty(value = "적립금 번호")
    private Long pmPointUsedId;

    @ApiModelProperty(value = "적립금 처리 유형 enum", required = true)
    private PointEnums.PointProcessType pointProcessType;

    @ApiModelProperty(value = "적립금 정산 유형 enum", required = true)
    private PointEnums.PointSettlementType pointSettlementType;

    @ApiModelProperty(value = "적립금 지급 유형 적립/차감 enum", required = true)
    private PointEnums.PointPayment pointPaymentType;

    @ApiModelProperty(value = "회원 고유번호", required = true)
    private Long urUserId;

    @ApiModelProperty(value = "적립/차감액", required = true)
    private Long amount;

    @ApiModelProperty(value = "참조 1 ex 등주문번호, 프로모션 번호")
    private String refNo1;

    @ApiModelProperty(value = "참조 2")
    private String refNo2;

    @ApiModelProperty(value = "메시지")
    private String cmnt;

    @ApiModelProperty(value = "적립금 설정 고유값", required = true)
    private Long pmPointId;

    @Builder
    public DepositPointDto(Long pmPointUsedId, PointEnums.PointProcessType pointProcessType, PointEnums.PointSettlementType pointSettlementType
                        , PointEnums.PointPayment pointPaymentType, Long urUserId, Long amount, String refNo1, String refNo2, String cmnt, Long pmPointId){

        this.pmPointUsedId = pmPointUsedId;
        this.pointProcessType = pointProcessType;
        this.pointSettlementType = pointSettlementType;
        this.pointPaymentType = pointPaymentType;
        this.urUserId = urUserId == null ? 0 : urUserId;
        this.amount = amount == null ? 0 : amount;
        this.refNo1 = refNo1;
        this.refNo2 = refNo2;
        this.cmnt = cmnt;
        this.pmPointId = pmPointId == null ? 0 : pmPointId;

    }
}
