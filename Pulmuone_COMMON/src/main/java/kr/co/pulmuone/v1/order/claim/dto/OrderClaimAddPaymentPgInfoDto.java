package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 추가 결제 정보 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 20.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 클레임 추가 결제 정보 조회 결과 Dto")
public class OrderClaimAddPaymentPgInfoDto {

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문마스터PK")
    private String odPaymentMasterId;

    @ApiModelProperty(value = "거래번호")
    private String tid;

    @ApiModelProperty(value = "승인번호")
    private String authCode;

    @ApiModelProperty(value = "승인 일자")
    private LocalDateTime approvalDate;

    @ApiModelProperty(value = "현금 영수증 발행 여부")
    private String cashReceiptYn;

    @ApiModelProperty(value = "현금 영수증 신청번호")
    private String cashReceiptNo;

    @ApiModelProperty(value = "현금 영수증 승인번호")
    private String cashReceiptAuthNo;

    @ApiModelProperty(value = "현금 영수증 발급 구분")
    private String cashReceiptType;

    @ApiModelProperty(value = "PG 응답 데이터")
    private String responseData;

    @ApiModelProperty(value = "금액")
    private String amt;
}
