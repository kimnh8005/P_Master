package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.promotion.point.dto.vo.PointVo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@ApiModel(description = "관리자 반영 적립금 Dto")
public class ManagerReflectionPointDto {

    @ApiModelProperty(value = "적립금 고유값")
    private Long pmPointId;

    @ApiModelProperty(value = "적립금 지불 유형")
    private PointEnums.PointPayment paymentTp;

    @ApiModelProperty(value = "적립금 처리 유형")
    private PointEnums.PointProcessType pointProcessTp;

    @ApiModelProperty(value = "적립금 정산 유형")
    private PointEnums.PointSettlementType pointSettlementTp;

    @ApiModelProperty(value = "적립금 정산 법인 코드")
    private String issueDeptCd;

    @ApiModelProperty(value = "유효일")
    private int validityDay;

    @ApiModelProperty(value = "적립/차감액")
    private Long amount;

    @ApiModelProperty(value = "포인트 만료일")
    private String expirationDt;

    @Builder
    public ManagerReflectionPointDto(PointVo pointVo){
        this.pmPointId = pointVo.getPmPointId();
        this.paymentTp = pointVo.getPaymentTp();
        this.pointProcessTp = pointVo.getPointProcessTp();
        this.pointSettlementTp = pointVo.getPointSettlementTp();
        this.issueDeptCd = pointVo.getIssueDeptCd();
        this.validityDay = pointVo.getValidityDay();
        this.amount = pointVo.getIssueVal();
        if(PointEnums.PointPayment.DEDUCTION == this.paymentTp){
            this.amount = this.amount*-1;
        }

        this.expirationDt = LocalDate.now().plusDays(this.validityDay).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
