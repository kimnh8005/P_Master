package kr.co.pulmuone.v1.order.order.dto.vo;

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
 * 주문 날짜 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@Setter
@ToString
@ApiModel(description = "주문 날짜 OD_ORDER_DT VO")
public class OrderDtVo {
    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

    @ApiModelProperty(value = "등록일")
    private LocalDateTime createDt;

    @ApiModelProperty(value = "입금대기중 등록자")
    private long irId;

    @ApiModelProperty(value = "입금대기중 일자")
    private LocalDateTime irDt;

    @ApiModelProperty(value = "입금전취소 등록자")
    private long ibId;

    @ApiModelProperty(value = "입금전취소 일자")
    private LocalDateTime ibDt;

    @ApiModelProperty(value = "결제완료 등록자")
    private long icId;

    @ApiModelProperty(value = "결제완료 일자")
    private LocalDateTime icDt;

}
