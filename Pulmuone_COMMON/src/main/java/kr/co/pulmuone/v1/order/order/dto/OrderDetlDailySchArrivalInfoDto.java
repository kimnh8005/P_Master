package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 03. 02.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 일일배송 스케쥴 OD_ORDER_DETL_DAILY_SCH VO")
public class OrderDetlDailySchArrivalInfoDto {

    @ApiModelProperty(value = "주문 상세 일일배송 스케쥴 PK")
    private long odOrderDetlDailySchId;

    @ApiModelProperty(value = "주문 상세 일일배송 스케쥴 라인번호")
    private long odOrderDetlDailySchSeq;

    @ApiModelProperty(value = "주문 상세 일일배송 패턴 PK")
    private long odOrderDetlDailyId;

    @ApiModelProperty(value = "도착예정일일자")
    private String deliveryDt;

    @ApiModelProperty(value = "택배사 설정 PK")
    private long psShippingCompId;

    @ApiModelProperty(value = "개별송장번호")
    private String trackingNo;

    @ApiModelProperty(value = "주문수량")
    private int orderCnt;

    @ApiModelProperty(value = "취소수량")
    private int cancelCnt;

    @ApiModelProperty(value = "배송완료 여부")
    private String deliveryYn;

    @ApiModelProperty(value = "스캐줄 주문 상태(1:주문, 2:취소)")
    private String orderSchStatus;

    @ApiModelProperty(value = "스캐줄 사용 여부")
    private String useYn;

    @ApiModelProperty(value = "주문배송지 PK")
    private long odShippingZoneId;

    @ApiModelProperty(value = "주문 상세 일일배송 패턴 부모 ID")
    private long odOrderDetlDailySchParentId;

    @ApiModelProperty(value = "재배송연동여부")
    private String redeliveryExecFl;

    @ApiModelProperty(value = "재배송연동일자")
    private LocalDateTime redeliveryExecDt;
}
