package kr.co.pulmuone.v1.order.schedule.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * <PRE>
 * Forbiz Korea
 * 주문리스트 관련 dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 4. 27.       석세동         최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문 상세 Shipping Info Dto")
public class OrderDetailScheduleShippingInfoDto {

	@ApiModelProperty(value = "주문 PK")
    private String odOrderId;

    @ApiModelProperty(value = "주문번호")
    private String odid;

    @ApiModelProperty(value = "주문경로 ERP I/F 주문경로 필요")
    private String orderHpnCd;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "주문자명")
    private String buyerNm;

    @ApiModelProperty(value = "주문자 전화번호")
    private String buyerTel;

    @ApiModelProperty(value = "주문자 휴대폰번호")
    private String buyerHp;

    @ApiModelProperty(value = "주문자 이메일")
    private String buyerMail;

    @ApiModelProperty(value = "수령자명")
    private String recvNm;

    @ApiModelProperty(value = "수령자 전화번호")
    private String recvTel;

    @ApiModelProperty(value = "수령자 휴대폰번호")
    private String recvHp;

    @ApiModelProperty(value = "수령자 주소 전체")
    private String recvAddr;

    @ApiModelProperty(value = "수령자 주소 앞")
    private String recvAddr1;

    @ApiModelProperty(value = "수령자 주소 뒤")
    private String recvAddr2;

    @ApiModelProperty(value = "수령자 주소 건물번호")
    private String recvBldNo;

    @ApiModelProperty(value = "수령자 주소 우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "고객 요청사항")
    private String deliveryMsg;

    @ApiModelProperty(value = "출입정보")
    private String doorMsgNm;

    @ApiModelProperty(value = "배송출입 현관 비밀번호")
    private String doorMsg;

    @ApiModelProperty(value = "시퀀스번호")
    private String seqNo;

}