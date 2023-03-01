package kr.co.pulmuone.v1.order.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 배송정보 관련 VO
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021.02.08	            	최윤지         최초작성
 * =======================================================================
 * </PRE>
 */
@Setter
@Getter
@ToString
@ApiModel(description = "주문 상세 배송정보 OD_SHIPPING_ZONE_HIST VO")
public class OrderDetlShippingZoneHistVo {

	@ApiModelProperty(value = "배송번호")
    private Long odShippingZoneId;

	@ApiModelProperty(value = "배송방법")
    private String deliveryType;

	@ApiModelProperty(value = "받는분")
    private String recvNm;

	@ApiModelProperty(value = "휴대폰번호(전체)")
    private String recvHp;

	@ApiModelProperty(value = "휴대폰번호 prefix")
    private String phonePrefix;

	@ApiModelProperty(value = "휴대폰번호 1")
    private String recvHp1;

	@ApiModelProperty(value = "휴대폰번호 2")
    private String recvHp2;

	@ApiModelProperty(value = "우편번호")
    private String recvZipCd;

	@ApiModelProperty(value = "주소 1")
    private String recvAddr1;

	@ApiModelProperty(value = "주소 2")
    private String recvAddr2;

	@ApiModelProperty(value = "건물번호")
    private String recvBldNo;

	@ApiModelProperty(value = "배송요청사항코드")
    private String deliveryMsgCd;

	@ApiModelProperty(value = "배송요청사항")
    private String deliveryMsg;

	@ApiModelProperty(value = "배송출입정보코드")
    private String doorMsgCd;

    @ApiModelProperty(value = "배송출입정보명")
    private String doorMsgCdName;

    @ApiModelProperty(value = "배송출입 현관 비밀번호")
    private String doorMsg;

    @ApiModelProperty(value = "배송출입 기타/직접입력 메세지")
    private String doorEtc;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "적용일")
    private String deliveryDt;

    @ApiModelProperty(value = "회원 pk")
    private String urUserId;

    @ApiModelProperty(value = "비회원 CI")
    private String guestCi;

    @ApiModelProperty(value = "권역정보")
    private String deliveryAreaNm;

    @ApiModelProperty(value = "배송방식")
    private String storeDeliveryIntervalTp;

    @ApiModelProperty(value = "주문 PK")
    private String odOrderId;
}
