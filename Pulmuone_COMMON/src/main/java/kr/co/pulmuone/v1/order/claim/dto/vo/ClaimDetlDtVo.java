package kr.co.pulmuone.v1.order.claim.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 상세 일자 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.   김명진         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문클레임 상세 일자 OD_CLAIM_DETL_DT VO")
public class ClaimDetlDtVo {

    @ApiModelProperty(value = "주문클레임 PK")
    private long odClaimId;

    @ApiModelProperty(value = "주문클레임 상세 PK")
    private long odClaimDetlId;

    @ApiModelProperty(value = "취소요청 등록자")
    private long caId;

    @ApiModelProperty(value = "취소요청 일자")
    private LocalDateTime caDt;

    @ApiModelProperty(value = "취소철회 등록자")
    private long cwId;

    @ApiModelProperty(value = "취소철회 일자")
    private LocalDateTime cwDt;

    @ApiModelProperty(value = "취소완료 등록자")
    private long ccId;

    @ApiModelProperty(value = "취소완료 일자")
    private LocalDateTime ccDt;

    @ApiModelProperty(value = "반품요청 등록자")
    private long raId;

    @ApiModelProperty(value = "반품요청 일자")
    private LocalDateTime raDt;

    @ApiModelProperty(value = "반품철회 등록자")
    private long rwId;

    @ApiModelProperty(value = "반품철회 일자")
    private LocalDateTime rwDt;

    @ApiModelProperty(value = "반품승인 등록자")
    private long riId;

    @ApiModelProperty(value = "반품승인 일자")
    private LocalDateTime riDt;

    @ApiModelProperty(value = "반품보류 등록자")
    private long rfId;

    @ApiModelProperty(value = "반품보류 일자")
    private LocalDateTime rfDt;

    @ApiModelProperty(value = "반품완료 등록자")
    private long rcId;

    @ApiModelProperty(value = "반품완료 일자")
    private LocalDateTime rcDt;

    @ApiModelProperty(value = "재배송 등록자")
    private long ecId;

    @ApiModelProperty(value = "재배송 일자")
    private LocalDateTime ecDt;

    @ApiModelProperty(value = "CS환불 등록자")
    private long csId;

    @ApiModelProperty(value = "CS환불 일자")
    private LocalDateTime csDt;

    @ApiModelProperty(value = "환불요청 등록자")
    private long faId;

    @ApiModelProperty(value = "환불요청 일자")
    private LocalDateTime faDt;

    @ApiModelProperty(value = "환불완료 등록자")
    private long fcId;

    @ApiModelProperty(value = "환불완료 일자")
    private LocalDateTime fcDt;

    @ApiModelProperty(value = "클레임요청 등록자")
    private long crId;

    @ApiModelProperty(value = "클레임요청 일자")
    private LocalDateTime crDt;

    @ApiModelProperty(value = "클레임승인 등록자")
    private long ceId;

    @ApiModelProperty(value = "클레임승인 일자")
    private LocalDateTime ceDt;

    @ApiModelProperty(value = "주문 I/F 등록자")
    private long orderIfId;

    @ApiModelProperty(value = "주문 I/F 일자")
    private LocalDateTime orderIfDt;

    @ApiModelProperty(value = "출고예정일 등록자")
    private long shippingId;

    @ApiModelProperty(value = "출고예정일 일자")
    private LocalDateTime shippingDt;

    @ApiModelProperty(value = "도착예정일 등록자")
    private long deliveryId;

    @ApiModelProperty(value = "도착예정일 일자")
    private LocalDateTime deliveryDt;

}
