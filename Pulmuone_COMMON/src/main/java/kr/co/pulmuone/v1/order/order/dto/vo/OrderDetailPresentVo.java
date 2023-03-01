package kr.co.pulmuone.v1.order.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 선물 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 08.17            최윤지         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문 선물 VO")
public class OrderDetailPresentVo {

    @ApiModelProperty(value = "주문 PK")
	private long odOrderId;

    @ApiModelProperty(value = "선물 받는사람명")
	private String presentReceiveNm;

    @ApiModelProperty(value = "선물 받는 핸드폰명")
    private String presentReceiveHp;

    @ApiModelProperty(value = "선물카드내용")
	private String presentCardMsg;

    @ApiModelProperty(value = "메시지 재발송 횟수")
    private int presentMsgSendCnt;

    @ApiModelProperty(value = "선물하기 상태")
    private String presentOrderStatus;
}