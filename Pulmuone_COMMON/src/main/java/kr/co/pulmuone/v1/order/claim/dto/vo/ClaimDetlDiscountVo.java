package kr.co.pulmuone.v1.order.claim.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 상세 할인정보 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 11.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문 클레임 상세 할인정보 OD_CLAIM_DETL_DISCOUNT VO")
public class ClaimDetlDiscountVo {

    @ApiModelProperty(value = "주문 클레임 상세 할인정보 PK")
    private long odClaimDetlDiscountId;

    @ApiModelProperty(value = "주문 클레임 상세 PK")
    private long odClaimDetlId;

    @ApiModelProperty(value = "상품할인 유형코드")
    private String discountTp;

    @ApiModelProperty(value = "쿠폰 PK")
    private long pmCouponIssueId;

    @ApiModelProperty(value = "쿠폰명")
    private String pmCouponNm;

    @ApiModelProperty(value = "할인금액")
    private int discountPrice;

    @ApiModelProperty(value = "임직원 혜택 그룹")
    private long psEmplDiscMasterId;

    @ApiModelProperty(value = "임직원 혜택 표준 브랜드")
    private long urBrandId;

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

    @ApiModelProperty(value = "주문 클레임 PK")
    private long odClaimId;

}
