package kr.co.pulmuone.v1.order.order.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingAddress;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingAddressDetail;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingMobile;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
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
 *  1.0    2021.01.26.             최윤지         최초작성
 * =======================================================================
 * </PRE>
 */
@Setter
@Getter
@ToString
@ApiModel(description = "주문 상세 배송정보 OD_SHIPPING_ZONE VO")
public class OrderDetlShippingZoneVo {

	@ApiModelProperty(value = "배송번호")
    private Long odShippingZoneId;

	@ApiModelProperty(value = "주문상세번호 PK")
	private Long odOrderDetlId;

	@ApiModelProperty(value = "주문 PK")
	private Long odOrderId;

	@ApiModelProperty(value = "상품 PK")
	private Long ilGoodsId;

	@ApiModelProperty(value = "주문상태코드")
	private String orderStatusCode;

	@ApiModelProperty(value = "배송방법")
    private String deliveryType;

	@ApiModelProperty(value = "배송방법코드")
	private String deliveryTypeCode;

	@ApiModelProperty(value = "받는분")
    @UserMaskingUserName
    private String recvNm;

    @ApiModelProperty(value = "받는분 (마스킹X)")
    private String orgRecvNm;

	@ApiModelProperty(value = "휴대폰번호(전체)")
    @UserMaskingMobile
    private String recvHp;

    @ApiModelProperty(value = "휴대폰번호(전체) (마스킹X)")
    private String orgRecvHp;

	@ApiModelProperty(value = "휴대폰번호 prefix")
    private String phonePrefix;

	@ApiModelProperty(value = "휴대폰번호 1")
    private String recvHp1;

	@ApiModelProperty(value = "휴대폰번호 2")
    private String recvHp2;

	@ApiModelProperty(value = "우편번호")
    private String recvZipCd;

	@ApiModelProperty(value = "주소 1")
    @UserMaskingAddress
    private String recvAddr1;

    @ApiModelProperty(value = "주소 1 (마스킹X)")
    private String orgRecvAddr1;

    @ApiModelProperty(value = "주소 2")
    @UserMaskingAddressDetail
    private String recvAddr2;

	@ApiModelProperty(value = "주소 2 (마스킹X)")
    private String orgRecvAddr2;

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

    @ApiModelProperty(value = "적용일")
    private String deliveryDt;

    @ApiModelProperty(value = "등록일")
    private String createDt;

    @ApiModelProperty(value = "회원 pk")
    private String urUserId;

    @ApiModelProperty(value = "비회원 CI")
    private String guestCi;

    @ApiModelProperty(value = "일일상품유형")
    private String goodsDailyType;

    @ApiModelProperty(value = "배송권역")
    private String deliveryAreaNm;

    @ApiModelProperty(value = "배송방식")
    private String storeDeliveryIntervalTp;

    @ApiModelProperty(value = "배송변경이력횟수")
    private String histCnt;

    @ApiModelProperty(value = "일일상품타입-녹즙")
    private String goodsDailyTpGreenjuiceYn;

    @ApiModelProperty(value = "일일배송 배송예정(Y:배송예정 배송지)")
    private String deliveryScheduledYn;

    @ApiModelProperty(value = "기획전유형(골라담기,내맘대로)")
    private String promotionTp;

    @ApiModelProperty(value = "주문상세 부모PK")
    private Long odOrderDetlParentId;

    @ApiModelProperty(value = "출고처명")
    private String warehouseNm;

    @ApiModelProperty(value = "출고처ID(출고처PK)")
    private Long urWarehouseId;

    @ApiModelProperty(value = "배송비")
    private int shippingPrice;

    @ApiModelProperty(value = "배송비 할인")
    private int shippingDiscountPrice;

    @ApiModelProperty(value = "배송정보(송장번호,택배사 등)")
    private String shippingInfo;

    @ApiModelProperty(value = "상품명정보 (n개 이상 시 : n건)")
    private String goodsNmInfo;

    @ApiModelProperty(value = "주문배송비 PK")
    private long odShippingPriceId;

    @ApiModelProperty(value = "배송비 정책 PK")
    private long ilShippingTmplId;

    @ApiModelProperty(value = "배송정보 내 각 주문상태정보")
    private String orderStatusInfo;

    @ApiModelProperty(value = "가맹점 대표번호")
    private String deliveryAreaTel;

    @ApiModelProperty(value = "배송유형 : ORDER_STATUS_DELI_TP")
    private String orderStatusDeliTp;
    
    @ApiModelProperty(value = "매장명")
    private String urStoreNm;

    @ApiModelProperty(value = "일괄배송여부")
    private String dailyBulkYn;

}
