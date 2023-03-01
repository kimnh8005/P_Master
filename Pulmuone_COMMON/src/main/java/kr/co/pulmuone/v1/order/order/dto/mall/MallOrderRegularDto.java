package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 정기배송 정보
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 03.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 주문상세 리스트 Dto")
public class MallOrderRegularDto {

    @ApiModelProperty(value = "현재 회차")
    private int reqRound;

	@ApiModelProperty(value = "전체 회차")
    private long totCnt;

    @ApiModelProperty(value = "결제 일자")
    private String paymentDt;
}