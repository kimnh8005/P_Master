package kr.co.pulmuone.mall.shopping.cart.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartInfoRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartListRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddSimpleCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.ApplyPaymentRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CheckBuyPossibleCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.DelCartAddGoodsRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.DelCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartCouponListRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartDataRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCouponPageInfoRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetOrderPageInfoRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetOrderSummaryRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetSaveShippingCostGoodsListRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.PutCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.PutCartShippingAddressRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200831		 	홍진영            최초작성
 * =======================================================================
 * </PRE>
 */

public interface ShoppingCartService {

	/**
	 * 주문 사용 가능 장바구니 쿠폰 조회
	 *
	 * @param	GetCartCouponListRequestDto
	 * @return	CouponDto
	 * @throws	Exception
	 */
	ApiResult<?> getCartCouponList(GetCartCouponListRequestDto getCartCouponListRequestDto) throws Exception;

	/**
	 * 주문 사용 가능 쿠폰 조회
	 *
	 * @param GetCouponPageInfoRequestDto
	 * @return GetCouponPageInfoResponseDto
	 * @throws Exception
	 */
	ApiResult<?> getCouponPageInfo(GetCouponPageInfoRequestDto getCouponPageInfoRequestDto) throws Exception;

	/**
	 * 주문서 정보 조회
	 *
	 * @param GetOrderPageInfoRequestDto
	 * @return GetOrderPageInfoResponseDto
	 * @throws Exception
	 */
	ApiResult<?> getOrderPageInfo(GetOrderPageInfoRequestDto getOrderPageInfoRequestDto) throws Exception;

	/**
	 * 주문서 비회원 본인인증 정보 조회
	 *
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderUserCertification() throws Exception;

	/**
	 * 장바구니 배송지 저장
	 *
	 * @param PutCartShippingAddressRequestDto
	 * @throws Exception
	 */
	ApiResult<?> putCartShippingAddress(PutCartShippingAddressRequestDto putCartShippingAddressRequestDto)
			throws Exception;

	/**
	 * 배송비 절약 상품 목록
	 *
	 * @param GetSaveShippingCostGoodsListRequestDto
	 * @throws Exception
	 */
	ApiResult<?> getSaveShippingCostGoodsList(
			GetSaveShippingCostGoodsListRequestDto getSaveShippingCostGoodsListRequestDto) throws Exception;

	/**
	 * 장바구니 수정
	 *
	 * @param AddCartListRequestDto
	 * @throws Exception
	 */
	ApiResult<?> putCart(PutCartRequestDto putCartRequestDto) throws Exception;

	/**
	 * 장바구니 일괄 추가
	 *
	 * @param AddCartListRequestDto
	 * @throws Exception
	 */
	ApiResult<?> addCartList(AddCartListRequestDto addCartListRequestDto) throws Exception;

	/**
	 * 장바구니 삭제
	 *
	 * @param DelCartRequestDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> delCartAndAddGoods(DelCartRequestDto delCartRequestDto) throws Exception;

	/**
	 * 장바구니 추가 구성상품 삭제
	 *
	 * @param DelCartAddGoodsRequestDto
	 * @throws Exception
	 */
	ApiResult<?> delCartAddGoods(DelCartAddGoodsRequestDto delCartAddGoodsRequestDto) throws Exception;

	/**
	 * 품절/판매중지 삭제
	 *
	 * @param
	 * @throws Exception
	 */
	ApiResult<?> delCartSoldOutGoods() throws Exception;

	/**
	 * 장바구니 페이지 정보
	 *
	 * @param
	 * @throws Exception
	 */
	ApiResult<?> getCartPageInfo() throws Exception;

	/**
	 * 장바구니 담기
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> addCartInfo(AddCartInfoRequestDto reqDto) throws Exception;

	/**
	 * 간편 장바구니 추가
	 *
	 * @param ilGoodsId
	 * @param pmAdInternalPageCd
	 * @param pmAdInternalContentCd
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> addSimpleCart(AddSimpleCartRequestDto reqDto) throws Exception;

	/**
	 * 장바구니 조회
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getCartInfo(GetCartDataRequestDto reqDto) throws Exception;

	/**
	 * 장바구니 예정 금액 조회
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getCartSummary(GetCartDataRequestDto reqDto) throws Exception;

	/**
	 * 결제 요청
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> applyPayment(ApplyPaymentRequestDto reqDto) throws Exception;

	/**
	 * 주문서 혜택 반영 조회
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getOrderSummary(GetOrderSummaryRequestDto reqDto) throws Exception;

	ApiResult<?> getGiftPageInfo(GetOrderPageInfoRequestDto getOrderPageInfoRequestDto) throws Exception;

	ApiResult<?> checkBuyPossibleCart(CheckBuyPossibleCartRequestDto reqDto) throws Exception;
}
