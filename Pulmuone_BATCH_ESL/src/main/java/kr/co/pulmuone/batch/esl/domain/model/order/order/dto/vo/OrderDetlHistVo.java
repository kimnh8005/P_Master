package kr.co.pulmuone.batch.esl.domain.model.order.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 이력 VO
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
@ApiModel(description = "주문 상세 이력 VO OD_ORDER_DETL_HIST VO")
public class OrderDetlHistVo {

	@ApiModelProperty(value = "주문 PK")
	private long odOrderId;

    @ApiModelProperty(value = "주문 상세 PK")
    private long odOrderDetlId;

    @ApiModelProperty(value = "주문상세 순번")
    private long odOrderDetlSeq;

    @ApiModelProperty(value = "변경상태값")
    private String statusCd;

    @ApiModelProperty(value = "처리이력내용")
    private String histMsg;

    @ApiModelProperty(value = "등록자ID")
    private long createId;
}
