package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@ApiModel(description = "적립금 적립 예약 정보 Dto")
public class PointDepositReservationDto {

    @ApiModelProperty(value = "적립금 설정 고유값", required = true)
    private Long pmPointId;

    @ApiModelProperty(value = "후기 적립금 유형 설정 고유값", required = true)
    private Long pmPointUserGradeId;

    @ApiModelProperty(value = "회원 고유번호", required = true)
    private Long urUserId;

    @ApiModelProperty(value = "적립/차감액", required = true)
    private Long amount;

    @ApiModelProperty(value = "포인트 적립일", required = true)
    private String depositDt;

    @ApiModelProperty(value = "포인트 만료일", required = true)
    private String expirationDt;

    @Builder
    public PointDepositReservationDto(Long pmPointId, Long pmPointUserGradeId, Long urUserId, Long amount, String depositDt, String expirationDt){

        this.pmPointId = pmPointId;
        this.pmPointUserGradeId = pmPointUserGradeId;
        this.urUserId = urUserId;
        this.amount = amount;
        this.depositDt = depositDt;
        this.expirationDt = expirationDt;
    }
}
