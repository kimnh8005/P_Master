package kr.co.pulmuone.v1.order.claim.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


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
public class OrderClaimAddPaymentInfoDto {

    @ApiModelProperty(value = "주문PK")
    private long odOrderId;

    @ApiModelProperty(value = "주문클레임PK")
    private long odClaimId;

    @ApiModelProperty(value = "주문결제마스터PK")
    private long odPaymentMasterId;

    @ApiModelProperty(value = "결제금액")
    private int paymentPrice;

    @ApiModelProperty(value = "클레임상태")
    private String claimStatusTp;

    @ApiModelProperty(value = "반품회수여부")
    private String returnsYn;

    @ApiModelProperty(value = "결제상태")
    private String status;

    @ApiModelProperty(value = "PG 정보 치환 DTO")
    OrderClaimAddPaymentPgInfoDto pgIntegratedInfo;
}
