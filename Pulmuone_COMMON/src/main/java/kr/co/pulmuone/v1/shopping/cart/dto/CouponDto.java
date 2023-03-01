package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CouponDto {

	/**
	 * 전시 쿠폰명
	 */
	private String displayCouponName;

	/**
	 * 쿠폰 발급 PK
	 */
	private Long pmCouponIssueId;

	/**
	 * 쿠폰 할인대상금액
	 */
	private int salePrice;

	/**
	 * 쿠폰 할인금액
	 */
	private int discountPrice;

	/**
	 * 쿠폰 적용 금액
	 */
	private int paymentPrice;

	/**
	 * 장바구니 쿠폰 사용 가능 여부
	 */
	private String useCartCouponYn;

	/**
	 * 제휴구분 (Y:적용, 결제수단)
	 */
	private String pgPromotionYn;

	/**
	 * 장바구니 쿠폰 제한 결제수단
	 */
	private String psPayCd;

	/**
	 * 장바구니 쿠폰 제한 결제수단 상세(카드)
	 */
	private String cardCode;

	/**
	 * 장바구니 쿠폰 제한 결제수단 최소금액
	 */
	private int minPaymentAmount;

	/**
	 * 쿠폰 사용 가능 여부
	 */
	private boolean isActive;

	/**
	 * 장바구니 추가 안내문구 노출 여부
	 */
	private String additionalNoticeYn;

	/**
	 * 장바구니 쿠폰 적용된 상품 정보 리스트
	 */
	private List<CartCouponApplicationGoodsDto> cartCouponApplicationGoodsList;

	public CartCouponApplicationGoodsDto findUseCartCouponGoodsCoupon(Long spCartId) {
		if (cartCouponApplicationGoodsList != null) {
			return cartCouponApplicationGoodsList.stream().filter(dto -> dto.getSpCartId().equals(spCartId)).findAny()
					.orElse(null);
		}

		return null;
	}

	/**
	 * 쿠폰 조직별 분담금액 정보 리스트
	 */
//	private List<CouponMileageShareOrganizationDto> couponShareOrganizationList;


	/**
	 * 쿠폰 적용된 묶음상품 정보 리스트
	 */
	private List<CartCouponApplicationGoodsDto> cartCouponApplicationGoodsPackageList;

}
