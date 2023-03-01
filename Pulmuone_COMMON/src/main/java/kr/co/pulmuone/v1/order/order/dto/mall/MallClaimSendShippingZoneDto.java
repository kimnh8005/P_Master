package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 보내는 배송지 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 20.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "주문클레임 OD_CLAIM_SEND_SHIPPING_ZONE VO")
public class MallClaimSendShippingZoneDto {


    @ApiModelProperty(value = "수령인명")
    private String recvNm;

    @ApiModelProperty(value = "수령인핸드폰")
    private String recvHp;

    @ApiModelProperty(value = "수령인연락처")
    private String recvTel;

    @ApiModelProperty(value = "수령인우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "수령인주소1")
    private String recvAddr1;

    @ApiModelProperty(value = "수령인주소2")
    private String recvAddr2;

    @ApiModelProperty(value = "건물번호")
    private String recvBldNo;

    @ApiModelProperty(value = "배송요청사항")
    private String deliveryMsg;

    @ApiModelProperty(value = "출입정보타입 공통코드(DOOR_MSG_CD)")
    private String doorMsgCd;

    @ApiModelProperty(value = "배송출입 현관 비밀번호")
    private String doorMsg;

    @ApiModelProperty(value = "반품회수여부")
    private String returnsYn;
}
