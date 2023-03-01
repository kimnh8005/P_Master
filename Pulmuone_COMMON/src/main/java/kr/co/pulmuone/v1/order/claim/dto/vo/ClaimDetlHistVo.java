package kr.co.pulmuone.v1.order.claim.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;



/**
 * <PRE>
 * Forbiz Korea
 * 주문클레임 상세이력 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 12.     김명진         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문클레임 상세이력 OD_CLAIM_DETL_HIST VO")
public class ClaimDetlHistVo {

    @ApiModelProperty(value = "주문상세 PK")
    private long odOrderDetlId;

    @ApiModelProperty(value = "주문클레임 PK")
    private long odClaimId;

    @ApiModelProperty(value = "주문클레임 상세 PK")
    private long odClaimDetlId;

    @ApiModelProperty(value = "클레임처리수량")
    private int claimCnt;

    @ApiModelProperty(value = "변경상태값")
    private String statusCd;

    @ApiModelProperty(value = "처리이력내용")
    private String histMsg;

    @ApiModelProperty(value = "등록자")
    private long createId;

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

    @ApiModelProperty(value = "주문상세 순번 주문번호에 대한 순번")
    private int odOrderDetlSeq;

    @ApiModelProperty(value = "이전상태값")
    private String prevStatusCd;

    @ApiModelProperty(value = "주문클레임처리이력PK")
    private long odClaimDetlHistId;
}
