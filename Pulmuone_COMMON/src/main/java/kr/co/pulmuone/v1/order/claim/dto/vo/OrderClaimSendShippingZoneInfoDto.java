package kr.co.pulmuone.v1.order.claim.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문클레임 보내는 배송지 배송지 관련 DTO
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
@Getter
@ToString
@ApiModel(description = "주문클레임 보내는 배송지 배송지 관련 DTO")
public class OrderClaimSendShippingZoneInfoDto {
	@ApiModelProperty(value = "주문클레임 보내는 배송지 PK")
	private long odClaimSendShippingZoneId;

    @ApiModelProperty(value = "송화인명")
    private String sendRecvNm;

    @ApiModelProperty(value = "송화인핸드폰")
    private String sendRecvHp;

    @ApiModelProperty(value = "송화인핸드폰 첫번째 자릿수")
    private String sendRecvHp1;

    @ApiModelProperty(value = "송화인핸드폰 두번째 자릿수")
    private String sendRecvHp2;

    @ApiModelProperty(value = "송화인핸드폰 세번째 자릿수")
    private String sendRecvHp3;

    @ApiModelProperty(value = "송화인연락처")
    private String sendRecvTel;

    @ApiModelProperty(value = "송화인우편번호")
    private String sendRecvZipCd;

    @ApiModelProperty(value = "송화인주소1")
    private String sendRecvAddr1;

    @ApiModelProperty(value = "송화인주소2")
    private String sendRecvAddr2;

    @ApiModelProperty(value = "송화인 건물번호")
    private String sendRecvBldNo;

    @ApiModelProperty(value = "송화인 배송요청사항")
    private String sendDeliveryMsg;

    @ApiModelProperty(value = "송화인 출입정보타입 공통코드(DOOR_MSG_CD)")
    private String sendDoorMsgCd;

    @ApiModelProperty(value = "송화인 배송출입 현관 비밀번호")
    private String sendDoorMsg;
}
