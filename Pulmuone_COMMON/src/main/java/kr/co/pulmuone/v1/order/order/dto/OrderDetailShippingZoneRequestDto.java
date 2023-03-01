package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 배송지 관련 VO
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
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "주문 배송지 OD_SHIPPING_ZONE VO")
public class OrderDetailShippingZoneRequestDto {
    @ApiModelProperty(value = "주문 배송지 PK")
    private long odShippingZoneId;

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;

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

    @ApiModelProperty(value = "배송요청사항코드")
    private String deliveryMsgCd;

    @ApiModelProperty(value = "배송요청사항")
    private String deliveryMsg;

    @ApiModelProperty(value = "출입정보타입")
    private String doorMsgCd;

    @ApiModelProperty(value = "배송출입 현관 비밀번호")
    private String doorMsg;

    @ApiModelProperty(value = "배송출입 기타/직접입력 메세지")
    private String doorEtc;

    @ApiModelProperty(value = "등록일")
    private LocalDateTime createDt;

    @ApiModelProperty(value = "적용일자")
    private String deliveryDt;

    @ApiModelProperty(value = "일일상품 타입")
    private String goodsDailyType;

    @ApiModelProperty(value = "기획전 유형")
    private String promotionTp;

    @ApiModelProperty(value = "주문상세 PK")
    private long odOrderDetlId;
}
