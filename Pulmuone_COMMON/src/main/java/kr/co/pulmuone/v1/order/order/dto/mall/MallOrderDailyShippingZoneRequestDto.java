package kr.co.pulmuone.v1.order.order.dto.mall;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * Mall 일일배송 배송지 정보 수정 요청 Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 04.	천혜현		최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "Mall 일일배송 배송지 정보 수정 요청 Dto")
public class MallOrderDailyShippingZoneRequestDto {

	@ApiModelProperty(value = "주문 배송지 PK")
    private long odShippingZoneId;

    @ApiModelProperty(value = "수령인 명")
    private String recvNm;

    @ApiModelProperty(value = "수령인 우편번호")
    private String recvZipCd;

    @ApiModelProperty(value = "수령인 주소1")
    private String recvAddr1;

    @ApiModelProperty(value = "수령인 주소2")
    private String recvAddr2;

    @ApiModelProperty(value = "건물번호")
    private String recvBldNo;

    @ApiModelProperty(value = "수령인 핸드폰")
    private String recvHp;

    @ApiModelProperty(value = "출입정보타입")
    private String doorMsgCd;

    @ApiModelProperty(value = "배송출입 현관 비밀번호")
    private String doorMsg;

    @ApiModelProperty(value = "배송요청사항")
    private String deliveryMsg;

    @ApiModelProperty(value = "주문상세 PK")
    private Long odOrderDetlId;

    @ApiModelProperty(value = "상품 PK")
    private Long ilGoodsId;

    @ApiModelProperty(value = "변경 배송일자")
    private String deliveryDt;

    @ApiModelProperty(value = "스토어(매장가맹점) PK")
    private String urStoreId;
}