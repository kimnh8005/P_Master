package kr.co.pulmuone.v1.promotion.point.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "적립금 사용내역 등록 RequestDto")
public class CommonAddPointUsedDetailRequestDto {

    @ApiModelProperty(value = "회원 ID")
    private Long urUserId;

    @ApiModelProperty(value = "적립/차감액")
    private String amount;

    @ApiModelProperty(value = "포인트 PK")
    private Long pmPointUsedId;

    @ApiModelProperty(value = "포인트 만료일")
    private String expirationDate;

    @ApiModelProperty(value = "지급유형")
    private String paymentType;

    @ApiModelProperty(value = "참조1")
    private String refNo1;

    @ApiModelProperty(value = "참조2")
    private String refNo2;

    @ApiModelProperty(value = "부서코드")
    private String deptCd;

    @ApiModelProperty(value = "포인트 상세 유형")
    private String pointType;

    @ApiModelProperty(value = "종료여부")
    private String closeYn;

    @ApiModelProperty(value = "연관 포인트이력 고유값")
    private Long refPointUsedDetlId;

    @ApiModelProperty(value = "연관 포인트이력 고유값 DB 용도")
    private Long refPointUsedDetlIdSelect;

    @ApiModelProperty(value = "포인트 처리 유형")
    private String pointProcessType;

    @ApiModelProperty(value = "포인트 정산 유형")
    private String pointSettlementType;
}
