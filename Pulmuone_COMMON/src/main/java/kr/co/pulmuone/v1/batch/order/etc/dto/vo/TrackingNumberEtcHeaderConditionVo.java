package kr.co.pulmuone.v1.batch.order.etc.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * I/F 기타송장정보 조회완료 기본 정보 Vo
 * </PRE>
 */

@Builder
@Getter
@ToString
public class TrackingNumberEtcHeaderConditionVo {

    @ApiModelProperty(value = "통합몰 주문번호")
    private String ordNum;

    @ApiModelProperty(value = "주문상품 순번")
    private String ordNoDtl;

    @ApiModelProperty(value = "송장번호")
    private String dlvNo;
}