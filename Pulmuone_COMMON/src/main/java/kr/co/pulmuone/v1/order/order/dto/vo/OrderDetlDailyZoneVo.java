package kr.co.pulmuone.v1.order.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 일일배송 배송지 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 04. 26.            홍진영         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 일일배송 배송지 OD_ORDER_DETL_DAILY_ZONE VO")
public class OrderDetlDailyZoneVo {

    @ApiModelProperty(value = "주문 상세 일일배송 배송지 PK")
    private long odOrderDetlDailyZoneId;

    @ApiModelProperty(value = "주문 상세 PK")
    private long odOrderDetlId;

    @ApiModelProperty(value = "주문배송지 PK")
    private long odShippingZoneId;

    @ApiModelProperty(value = "시작일")
    private LocalDate startDt;

    @ApiModelProperty(value = "종료일")
    private LocalDate endDt;

    @ApiModelProperty(value = "사용여부")
    private String useYn;
}
