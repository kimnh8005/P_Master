package kr.co.pulmuone.v1.order.schedule.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 *
 * <PRE>
 * Forbiz Korea
 * 주문상세 스캐줄리스트 관련 vo
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 2. 1.       석세동         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 OD_ORDER_DETL_DAILY_SCH VO")
public class OrderDetlScheduleVo {

    @ApiModelProperty(value = "주문 PK")
    private long odid;

    @ApiModelProperty(value = "주문 상세 PK")
    private long odOrderDetlId;

    @ApiModelProperty(value = "주문 상세 일일배송 패턴 라인번호")
    private int odOrderDetlDailySchSeq;

    @ApiModelProperty(value = "주문 상세 일일배송 패턴 PK")
    private long odOrderDetlDailyId;

    @ApiModelProperty(value = "도착 예정 일자")
    private String deliveryDt;

    @ApiModelProperty(value = "주문 수량")
    private int orderCnt;

    @ApiModelProperty(value = "주문상태")
    private String orderSchStatus;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "주문 상세 일일배송 스케줄 PK")
	private long odOrderDetlDailySchId;

    @ApiModelProperty(value = "주문상세 순번(라인번호) 주문번호에 대한 순번")
    private int odOrderDetlSeq;

    @ApiModelProperty(value = "주문배송지 PK")
	private long odShippingZoneId;
}