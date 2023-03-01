package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
public class PointUsedDetailAddRequestDto {

    @ApiModelProperty(value = "회원 고유번호", required = true)
    private Long urUserId;

    @ApiModelProperty(value = "적립/차감액", required = true)
    private Long amount;

    @ApiModelProperty(value = "연관_포인트이력_고유값")
    private Long refPointUsedDetailId;

    @ApiModelProperty(value = "재적립(환불) 연관 포인트 이력 고유값")
    private Long refDproPointUsedDetlId;

    @ApiModelProperty(value = "적립금 내역 고유값")
    private Long pmPointUsedId;

    @ApiModelProperty(value = "적립금 사용가능일")
    private Date availableDate;

    @ApiModelProperty(value = "적립금 만료일")
    private String expirationDate;

    @ApiModelProperty(value = "적립금 지급 유형")
    private PointEnums.PointPayment pointPaymentType;

    @ApiModelProperty(value = "참조 1 ex 등주문번호, 프로모션 번호")
    private String refNo1;

    @ApiModelProperty(value = "참조 2")
    private String refNo2;

    @ApiModelProperty(value = "조직 코드", required = true)
    private String deptCd;

    @ApiModelProperty(value = "적립금 유형", required = true)
    private String pointType;

    @ApiModelProperty(value = "적립금 처리 유형", required = true)
    private PointEnums.PointProcessType pointProcessType;

    @ApiModelProperty(value = "적립금 정산 유형", required = true)
    private PointEnums.PointSettlementType pointSettlementType;

    @ApiModelProperty(value = "종료 여부")
    private String closeYn;

    @ApiModelProperty(value = "적립금 설정 고유값")
    private Long pmPointId;

    @Setter
    private Long pmPointUsedDetlId;

    @Builder
    public PointUsedDetailAddRequestDto (
        Long urUserId, Long amount, Date availableDate, String expirationDate, PointEnums.PointPayment pointPaymentType, String pointType, PointEnums.PointProcessType pointProcessType, PointEnums.PointSettlementType pointSettlementType
        , String refNo1, String refNo2, String closeYn, String deptCd, Long pmPointUsedId, Long refPointUsedDetailId, Long pmPointId, Long refDproPointUsedDetlId
    ) {
        this.urUserId = urUserId;
        this.amount = amount;
        this.availableDate = availableDate;
        this.expirationDate = expirationDate;
        this.pointType = pointType;
        this.pointProcessType = pointProcessType;
        this.pointSettlementType = pointSettlementType;
        this.pointPaymentType = pointPaymentType;
        this.deptCd = deptCd;
        this.pmPointUsedId = pmPointUsedId;
        this.refPointUsedDetailId = refPointUsedDetailId;
        this.refDproPointUsedDetlId = refDproPointUsedDetlId;
        this.refNo1 = refNo1;
        this.refNo2 = refNo2;
        this.closeYn = closeYn == null ? "N" : "Y";
        this.pmPointId = pmPointId;
    }


}
