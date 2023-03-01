package kr.co.pulmuone.v1.order.create.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.shopping.cart.dto.UseGoodsCouponDto;
import kr.co.pulmuone.v1.shopping.cart.dto.UseShippingCouponDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <PRE>
 * Forbiz Korea
 * 주문생성 결제요청 Request Dto
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일                		:	작성자      :	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 04. 16.		이규한	최초작성
 * =======================================================================
 * </PRE>
 */

@Getter
@Setter
@ToString
@ApiModel(description = "주문생성 결제요청 Request Dto")
public class OrderCreatePaymentRequestDto {

	@ApiModelProperty(value = "회원 PK")
	private Long urUserId;

	@ApiModelProperty(value = "회원 등급 PK")
	private Long urGroupId;

	@ApiModelProperty(value = "회원명")
	private String userName;

	@ApiModelProperty(value = "회원 휴대폰")
	private String userMobile;

	@ApiModelProperty(value = "회원 이메일")
	private String userEmail;

	@ApiModelProperty(value = "배송지 수령인명")
	private String receiverName;

	@ApiModelProperty(value = "배송지 모바일")
	private String receiverMobile;

	@ApiModelProperty(value = "배송지 우편번호")
	private String receiverZipCode;

    @ApiModelProperty(value = "배송지 건물 관리 번호")
    private String buildingCode;

    @ApiModelProperty(value = "배송지 주소1")
	private String receiverAddress1;

    @ApiModelProperty(value = "배송지 주소2")
	private String receiverAddress2;

    @ApiModelProperty(value = "배송 요청 사항")
	private String shippingComment;

    @ApiModelProperty(value = "배송 출입정보 타입")
	private String accessInformationType;

    @ApiModelProperty(value = "배송 출입정보 비밀번호")
	private String accessInformationPassword;

	private String spCartIdListData;

	@ApiModelProperty(value = "장바구니PK 리스트", required = true)
	private List<Long> spCartIdList;

	private String arrivalScheduledListData;

	@ApiModelProperty(value = "배송 스케줄 변경 정보 리스트")
	private List<OrderCreateScheduledDto> arrivalScheduledList;

	private String useGoodsCouponListData;

	@ApiModelProperty(value = "상품 쿠폰 사용 정보")
	private List<UseGoodsCouponDto> useGoodsCouponList;

	private String useShippingCouponListData;

	@ApiModelProperty(value = "배송 쿠폰 사용 정보")
	private List<UseShippingCouponDto> useShippingCouponList;

	@ApiModelProperty(value = "장바구니 쿠폰 사용 발급 PK")
	private Long useCartPmCouponIssueId;

	@ApiModelProperty(value = "사용 적립금")
	private int usePoint;

	@ApiModelProperty(value = "결제 정보 PK", required = true)
	private String psPayCd;

	@ApiModelProperty(value = "카드 정보 PK")
	private String cardCode;

	@ApiModelProperty(value = "할부 기간")
	private String installmentPeriod;

	@ApiModelProperty(value = "환불 계좌 은행코드")
	private String bankCode;

	@ApiModelProperty(value = "환불 계좌 계좌번호")
	private String accountNumber;

	@ApiModelProperty(value = "환불 계좌 예금주")
	private String holderName;

	@ApiModelProperty(value = "BOS 구분 (BOS : 'BOS')")
	private String bosTp;

	@ApiModelProperty(value = "배송비무료 여부")
	private String freeShippingPriceYn = "N";

	public void convertDataList() throws Exception {
		if (!StringUtil.isEmpty(this.spCartIdListData)) this.spCartIdList = BindUtil.convertJsonArrayToDtoList(this.spCartIdListData, Long.class);
		if (!StringUtil.isEmpty(this.arrivalScheduledListData)) this.arrivalScheduledList = BindUtil.convertJsonArrayToDtoList(this.arrivalScheduledListData, OrderCreateScheduledDto.class);
		if (!StringUtil.isEmpty(this.useGoodsCouponListData)) this.useGoodsCouponList = BindUtil.convertJsonArrayToDtoList(this.useGoodsCouponListData, UseGoodsCouponDto.class);
		if (!StringUtil.isEmpty(this.useShippingCouponListData)) this.useShippingCouponList = BindUtil.convertJsonArrayToDtoList(this.useShippingCouponListData, UseShippingCouponDto.class);
    }
}