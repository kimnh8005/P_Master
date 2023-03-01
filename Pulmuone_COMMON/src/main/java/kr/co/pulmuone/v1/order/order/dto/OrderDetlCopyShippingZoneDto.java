package kr.co.pulmuone.v1.order.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingAddress;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingAddressDetail;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingMobile;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingUserName;
import lombok.Builder;
import lombok.Getter;
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
@Builder
@Getter
@ToString
@ApiModel(description = "주문 상세 배송정보 OD_SHIPPING_ZONE DTO")
public class OrderDetlCopyShippingZoneDto {

	@ApiModelProperty(value = "배송번호")
    private long odShippingZoneId;

	@ApiModelProperty(value = "주문배송비 PK")
	private long odShippingPriceId;

	@ApiModelProperty(value = "주문상세번호 PK")
	private long odOrderDetlId;

	@ApiModelProperty(value = "배송방법명")
	private String deliveryTypeNm;

	@ApiModelProperty(value = "출고지명")
	private String warehouseNm;

	@ApiModelProperty(value = "출고지PK")
	private long urWarehouseId;

	@ApiModelProperty(value = "배송비 정책 PK")
	private long ilShippingTmplId;

	@ApiModelProperty(value = "송장번호")
	private String trackingNo;

	@ApiModelProperty(value = "택배사명")
	private String shippingCompNm;

	@ApiModelProperty(value = "배송비")
	private int shippingPrice;

	@ApiModelProperty(value = "배송비할인금액")
	private int shippingDiscountPrice;

	@ApiModelProperty(value = "쿠폰명")
	private String pmCouponNm;

	@ApiModelProperty(value = "상품 PK")
	private long ilGoodsId;

	@ApiModelProperty(value = "상품명")
	private String goodsNm;

	@ApiModelProperty(value = "받는분")
    @UserMaskingUserName
    private String recvNm;

	@ApiModelProperty(value = "받는분")
    @UserMaskingUserName
    private String orgRecvNm;

	@ApiModelProperty(value = "휴대폰번호(전체)")
    @UserMaskingMobile
    private String recvHp;

	@ApiModelProperty(value = "휴대폰번호(전체)")
    @UserMaskingMobile
    private String orgRecvHp;

	@ApiModelProperty(value = "휴대폰번호 첫번째 자릿수")
    @UserMaskingMobile
    private String phonePrefix;

	@ApiModelProperty(value = "휴대폰번호 두번째 자릿수")
    @UserMaskingMobile
    private String recvHp1;

	@ApiModelProperty(value = "휴대폰번호 마지막 자릿수")
    @UserMaskingMobile
    private String recvHp2;

	@ApiModelProperty(value = "전화번호(전체)")
    @UserMaskingMobile
	private String recvTel;

	@ApiModelProperty(value = "이메일")
    @UserMaskingMobile
	private String recvMail;

	@ApiModelProperty(value = "우편번호")
    private String recvZipCd;

	@ApiModelProperty(value = "주소 1")
    @UserMaskingAddress
    private String recvAddr1;

	@ApiModelProperty(value = "주소 1")
    @UserMaskingAddress
    private String orgRecvAddr1;

    @ApiModelProperty(value = "주소 2")
    @UserMaskingAddressDetail
    private String recvAddr2;

	@ApiModelProperty(value = "주소 2")
    @UserMaskingAddress
    private String orgRecvAddr2;

	@ApiModelProperty(value = "건물번호")
    private String recvBldNo;

	@ApiModelProperty(value = "배송요청사항")
    private String deliveryMsg;

	@ApiModelProperty(value = "배송출입정보코드")
    private String doorMsgCd;

	@ApiModelProperty(value = "배송출입정보코드명")
	private String doorMsgCdName;

    @ApiModelProperty(value = "배송출입 현관 비밀번호")
    private String doorMsg;

    @ApiModelProperty(value = "정상주문상태")
    private String orderStatusCd;

    @ApiModelProperty(value = "회원ID")
    private long urUserId;

    @ApiModelProperty(value = "비회원 CI")
    private String guestCi;

    @ApiModelProperty(value = "주문 PK")
    private long odOrderId;
}
