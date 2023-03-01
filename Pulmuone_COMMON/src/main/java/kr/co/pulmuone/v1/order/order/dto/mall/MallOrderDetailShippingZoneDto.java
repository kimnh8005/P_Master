package kr.co.pulmuone.v1.order.order.dto.mall;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 주문상세 배송지정보 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 01. 23.	김명진		최초작성
 * =======================================================================
 * </PRE>
 */
@Builder
@Getter
@ToString
@ApiModel(description = "Mall 주문상세 배송지정보 Dto")
public class MallOrderDetailShippingZoneDto {

    @ApiModelProperty(value = "주문 배송지 PK")
    private Long odShippingZoneId;

	@ApiModelProperty(value = "수령인명")
    private String recvNm;

	@ApiModelProperty(value = "수령인우편번호")
    private String recvZipCd;

	@ApiModelProperty(value = "수령인주소1")
    private String recvAddr1;

    @ApiModelProperty(value = "수령인주소2")
    private String recvAddr2;

    @ApiModelProperty(value = "건물번호")
    private String recvBldNo;

    @ApiModelProperty(value = "수령인핸드폰")
    private String recvHp;

    @ApiModelProperty(value = "출입정보타입 공통코드(DOOR_MSG_CD)")
    private String doorMsgCd;

    @ApiModelProperty(value = "배송출입현관비밀번호")
    private String doorMsg;

    @ApiModelProperty(value = "배송요청사항")
    private String deliveryMsg;
}