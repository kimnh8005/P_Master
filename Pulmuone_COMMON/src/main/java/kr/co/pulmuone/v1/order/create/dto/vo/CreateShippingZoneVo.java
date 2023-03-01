package kr.co.pulmuone.v1.order.create.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문생성 배송지 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 18.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Getter
@Setter
@ToString
@ApiModel(description = "주문생성 배송지 OD_CREATE_SHIPPING_ZONE VO")
public class CreateShippingZoneVo {
    @ApiModelProperty(value = "주문 배송지 PK")
    private long odCreateShippingZoneId;

    @ApiModelProperty(value = "주문생성정보 PK")
    private long odCreateInfoId;

    @ApiModelProperty(value = "배송정책 PK")
    private long ilShippingTmplId;

    @ApiModelProperty(value = "배송타입")
    private String deliveryType;

    @ApiModelProperty(value = "주문타입")
    private int shippingType;

    @ApiModelProperty(value = "수령인 명")
    private String recvNm;

    @ApiModelProperty(value = "수령인 핸드폰")
    private String recvHp;

    @ApiModelProperty(value = "수령인 연락처")
    private String recvTel;

    @ApiModelProperty(value = "수령인 이메일")
    private String recvMail;

    @ApiModelProperty(value = "수령인 우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "수령인 주소1")
    private String recvAddr1;

    @ApiModelProperty(value = "수령인 주소2")
    private String recvAddr2;

    @ApiModelProperty(value = "건물번호")
    private String recvBldNo;

    @ApiModelProperty(value = "배송요청사항")
    private String deliveryMsg;

    @ApiModelProperty(value = "출입정보타입")
    private String doorMsgCd;

    @ApiModelProperty(value = "배송출입 현관 비밀번호")
    private String doorMsg;

}
