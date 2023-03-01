package kr.co.pulmuone.v1.order.claim.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 상세 일일배송 스케쥴 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 13.            김명진         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문클레임 OD_CLAIM_DETL_DAILY_SCH VO")
public class ClaimDetlDailySchVo {

    @ApiModelProperty(value = "주문 클레임 상세 일일배송 스케쥴 PK")
    private long odClaimDetlDailySch;

    @ApiModelProperty(value = "주문 클레임 상세 일일배송 스케쥴 라인번호")
    private int odClaimDetlDailySchSeq;

    @ApiModelProperty(value = "주문 클레임 PK")
    private long odClaimId;

    @ApiModelProperty(value = "주문 클레임 상세 PK")
    private long odClaimDetlId;

    @ApiModelProperty(value = "주문 상세 일일배송 패턴 PK")
    private long odOrderDetlDailyId;

    @ApiModelProperty(value = "주문 상세 일일배송 스케쥴 PK")
    private long odOrderDetlDailySchId;

    @ApiModelProperty(value = "클레임 수량")
    private int claimCnt;
}
