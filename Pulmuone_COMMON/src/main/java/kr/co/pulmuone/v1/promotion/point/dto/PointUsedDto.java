package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.promotion.point.dto.vo.PointVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@ApiModel(description = "적립금 사용 내역 Dto")
public class PointUsedDto {

    @ApiModelProperty(value = "적립금 내역 고유값")
    @Setter private Long pmPointUsedId;

    @ApiModelProperty(value = "적립금 처리 유형", required = true)
    private PointEnums.PointProcessType pointProcessTp;

    @ApiModelProperty(value = "적립금 정산 유형", required = true)
    private PointEnums.PointSettlementType pointSettlementTp;

    @ApiModelProperty(value = "적립금 지급 유형", required = true)
    private PointEnums.PointPayment paymentTp;

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
    public PointUsedDto(DepositPointDto depositPointDto, PointVo pointVo){
        this.pmPointId = depositPointDto.getPmPointId() == null ? pointVo.getPmPointId(): depositPointDto.getPmPointId();
        this.pointProcessTp = depositPointDto.getPointProcessType() == null ? pointVo.getPointProcessTp() : depositPointDto.getPointProcessType();
        this.pointSettlementTp = depositPointDto.getPointSettlementType() == null ? pointVo.getPointSettlementTp() : depositPointDto.getPointSettlementType();
        this.paymentTp = depositPointDto.getPointPaymentType() == null ? null : depositPointDto.getPointPaymentType();
        this.urUserId = depositPointDto.getUrUserId();
        this.amount = depositPointDto.getAmount();
        this.refNo1 = depositPointDto.getRefNo1();
        this.refNo2 = depositPointDto.getRefNo2();
        this.cmnt = depositPointDto.getCmnt();

    }
}
