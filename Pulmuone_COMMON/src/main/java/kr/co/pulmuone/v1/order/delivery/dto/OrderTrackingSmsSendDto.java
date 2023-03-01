package kr.co.pulmuone.v1.order.delivery.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 송장번호 등록 대상 SMS 발송 관련 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 03. 10.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문 송장번호 등록 대상 SMS 발송 관련 Dto")
public class OrderTrackingSmsSendDto {
    @ApiModelProperty(value = "주문자 핸드폰")
    private String buyerHp;

    @ApiModelProperty(value = "수집몰 주문번호")
    private String collectionMallId;

}
