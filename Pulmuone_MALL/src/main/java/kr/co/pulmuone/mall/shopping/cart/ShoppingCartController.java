package kr.co.pulmuone.mall.shopping.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.mall.shopping.cart.service.ShoppingCartService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.GiftListVo;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartInfoRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartInfoResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartListRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddSimpleCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddSimpleCartResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.ApplyPaymentRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartSummaryDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CheckBuyPossibleCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CouponDto;
import kr.co.pulmuone.v1.shopping.cart.dto.DelCartAddGoodsRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.DelCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartCouponListRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartDataRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartInfoResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartPageInfoResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCouponPageInfoRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCouponPageInfoResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetOrderPageInfoRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetOrderPageInfoResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetOrderSummaryRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetSaveShippingCostGoodsListRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.PutCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.PutCartShippingAddressRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.PutCartShippingAddressResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetCertificationResultVo;

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
 *  1.0    20200831   	 	홍진영            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class ShoppingCartController {

	@Autowired
	public ShoppingCartService shoppingCartService;

	/**
	 * 주문 사용 가능 장바구니 쿠폰 조회
	 *
	 * @param	GetCartCouponListRequestDto
	 * @throws	Exception
	 */
	@PostMapping(value = "/shopping/cart/getCartCouponList")
	@ApiOperation(value = "주문 사용 가능 장바구니 쿠폰 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = CouponDto.class)})
	public ApiResult<?> getCartCouponList(GetCartCouponListRequestDto getCartCouponListRequestDto) throws Exception {
		return shoppingCartService.getCartCouponList(getCartCouponListRequestDto);
	}


	/**
	 * 주문 사용 가능 쿠폰 조회
	 *
	 * @param GetCouponPageInfoRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/shopping/cart/getCouponPageInfo")
	@ApiOperation(value = "주문 사용 가능 쿠폰 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetCouponPageInfoResponseDto.class) })
	public ApiResult<?> getCouponPageInfo(GetCouponPageInfoRequestDto getCouponPageInfoRequestDto) throws Exception {
		return shoppingCartService.getCouponPageInfo(getCouponPageInfoRequestDto);
	}

	/**
	 * 주문서 정보 조회
	 *
	 * @param GetOrderPageInfoRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/shopping/cart/getOrderPageInfo")
	@ApiOperation(value = "주문서 정보 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetOrderPageInfoResponseDto.class) })
	public ApiResult<?> getOrderPageInfo(GetOrderPageInfoRequestDto getOrderPageInfoRequestDto) throws Exception {
		return shoppingCartService.getOrderPageInfo(getOrderPageInfoRequestDto);
	}

	/**
	 * 본인인증 정보 조회
	 *
	 * @param
	 * @return GetCertificationResponseDto
	 * @throws Exception
	 */
	@ApiOperation(value = "주문서 비회원 본인인증 정보 조회")
	@PostMapping(value = "/shopping/cart/getOrderUserCertification")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetCertificationResultVo.class),
			@ApiResponse(code = 901, message = "" + "[NOT_ANY_CERTI] 1203 - 본인 인증정보 없음 \n"),
			@ApiResponse(code = 901, message = "" + "[NOT_ANY_CERTI] 1202 - 14세 미만은 회원가입불가 \n")})
	public ApiResult<?> getOrderUserCertification() throws Exception {
		return shoppingCartService.getOrderUserCertification();
	}

	/**
	 * 장바구니 배송지 저장
	 *
	 * @param PutCartShippingAddressRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/shopping/cart/putCartShippingAddress")
	@ApiOperation(value = "장바구니 배송지 저장", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = PutCartShippingAddressResponseDto.class) })
	public ApiResult<?> putCartShippingAddress(PutCartShippingAddressRequestDto putCartShippingAddressRequestDto)
			throws Exception {
		return shoppingCartService.putCartShippingAddress(putCartShippingAddressRequestDto);
	}

	/**
	 * 배송비 절약 상품 목록
	 *
	 * @param GetSaveShippingCostGoodsListRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/shopping/cart/getSaveShippingCostGoodsList")
	@ApiOperation(value = "배송비 절약 상품 목록", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GoodsSearchResultDto.class) })
	public ApiResult<?> getSaveShippingCostGoodsList(
			GetSaveShippingCostGoodsListRequestDto getSaveShippingCostGoodsListRequestDto) throws Exception {
		return shoppingCartService.getSaveShippingCostGoodsList(getSaveShippingCostGoodsListRequestDto);
	}

	/**
	 * 장바구니 수정
	 *
	 * @param PutCartRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/shopping/cart/putCart")
	@ApiOperation(value = "장바구니 수정", httpMethod = "POST")
	public ApiResult<?> putCart(PutCartRequestDto putCartRequestDto) throws Exception {
		return shoppingCartService.putCart(putCartRequestDto);
	}

	/**
	 * 장바구니 일괄 추가
	 *
	 * @param AddCartListRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/shopping/cart/addCartList")
	@ApiOperation(value = "장바구니 일괄 추가", httpMethod = "POST")
	public ApiResult<?> addCartList(AddCartListRequestDto addCartListRequestDto) throws Exception {
		return shoppingCartService.addCartList(addCartListRequestDto);
	}

	/**
	 * 장바구니 삭제
	 *
	 * @param DelCartRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/shopping/cart/delCartAndAddGoods")
	@ApiOperation(value = "장바구니 삭제", httpMethod = "POST")
	public ApiResult<?> delCartAndAddGoods(DelCartRequestDto delCartRequestDto) throws Exception {
		return shoppingCartService.delCartAndAddGoods(delCartRequestDto);
	}

	/**
	 * 장바구니 추가 구성상품 삭제
	 *
	 * @param DelCartAddGoodsRequestDto
	 * @throws Exception
	 */
	@PostMapping(value = "/shopping/cart/delCartAddGoods")
	@ApiOperation(value = "장바구니 추가 구성상품 삭제", httpMethod = "POST")
	public ApiResult<?> delCartAddGoods(DelCartAddGoodsRequestDto delCartAddGoodsRequestDto) throws Exception {
		return shoppingCartService.delCartAddGoods(delCartAddGoodsRequestDto);
	}

	/**
	 * 품절/판매중지 삭제
	 *
	 * @param
	 * @throws Exception
	 */
	@GetMapping(value = "/shopping/cart/delCartSoldOutGoods")
	@ApiOperation(value = "품절/판매중지 삭제", httpMethod = "GET")
	public ApiResult<?> delCartSoldOutGoods() throws Exception {
		return shoppingCartService.delCartSoldOutGoods();
	}

	/**
	 * 장바구니 페이지 정보
	 *
	 * @param
	 * @throws Exception
	 */
	@GetMapping(value = "/shopping/cart/getCartPageInfo")
	@ApiOperation(value = "장바구니 페이지 정보", httpMethod = "GET")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetCartPageInfoResponseDto.class) })
	public ApiResult<?> getCartPageInfo() throws Exception {
		return shoppingCartService.getCartPageInfo();
	}

	/**
	 * 장바구니 담기
	 *
	 * @param AddCartInfoRequestDto reqDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/shopping/cart/addCartInfo")
	@ApiOperation(value = "장바구니 담기", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = AddCartInfoResponseDto.class),
			@ApiResponse(code = 901, message = "" + "NO_GOODS - 상품정보 없음 \n"
					+ "NO_GOODS_STATUS_ON_SALE - 상품 판매중 상태 아님 \n" + "GOODS_LACK_STOCK - 상품 재고 수량 보다 장바구니 수량이 더 큰 경우 \n"
					+ "NO_ADD_GOODS_STATUS_ON_SALE - 추가 구성 상품 판매중 상태 아님 \n"
					+ "ADD_GOODS_LACK_STOCK - 추가 구성 상품 재고 수량 보다 장바구니 수량이 더 큰 경우 \n"
					+ "FAIL_VALIDATION - 필수 옵션 및 선택적 필수 옵션 정보 없음 \n") })
	public ApiResult<?> addCartInfo(AddCartInfoRequestDto reqDto) throws Exception {
		return shoppingCartService.addCartInfo(reqDto);
	}

	/**
	 * /shopping/cart/addSimpleCart
	 *
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/shopping/cart/addSimpleCart")
	@ApiOperation(value = "간편 장바구니 추가", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = AddSimpleCartResponseDto.class),
			@ApiResponse(code = 901, message = "" + "NO_GOODS - 상품정보 없음 \n" + "EXIST_OPTION - 옵션 선택 상품") })
	public ApiResult<?> addSimpleCart(AddSimpleCartRequestDto reqDto) throws Exception {
		return shoppingCartService.addSimpleCart(reqDto);
	}

	/**
	 * 장바구니 조회
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/shopping/cart/getCartInfo")
	@ApiOperation(value = "장바구니 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetCartInfoResponseDto.class) })
	public ApiResult<?> getCartInfo(GetCartDataRequestDto reqDto) throws Exception {
		return shoppingCartService.getCartInfo(reqDto);
	}

	/**
	 * 장바구니 예정금액 조회
	 */
	@PostMapping(value = "/shopping/cart/getCartSummary")
	@ApiOperation(value = "장바구니 예정금액 조회", httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = CartSummaryDto.class) })
	public ApiResult<?> getCartSummary(GetCartDataRequestDto reqDto) throws Exception {
		return shoppingCartService.getCartSummary(reqDto);
	}

	/**
	 * 장바구니 결제요청
	 */
	@PostMapping(value = "/shopping/cart/applyPayment")
	@ApiOperation(value = "결제요청", httpMethod = "POST")
	@ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = BasicDataResponseDto.class),
			@ApiResponse(code = 901, message = ""
					+ "[GOODS_LACK_STOCK] GOODS_LACK_STOCK - 상품 재고 수량 보다 장바구니 수량이 더 큰 경우 \n"
					+ "[NON_MEMBER_NOT_POINT] NON_MEMBER_NOT_POINT - 비회원 포인트 사용 불가 \n"
					+ "[NON_MEMBER_NOT_REGULAR] NON_MEMBER_NOT_REGULAR - 비회원 정기결제 정보 이용 안됨 \n"
					+ "[NOT_BUY_GOODS] NOT_BUY_GOODS - 구매 불가 상품 \n"
					+ "[ORVER_AVAILABLE_POINT] ORVER_AVAILABLE_POINT - 가용 포인트 보다 많은 포인트 사용 \n"
					+ "[REQUIRED_NON_MEMBER_CERTIFICATION] REQUIRED_NON_MEMBER_CERTIFICATION - 비회원 본인인증 정보 없음 \n"
					+ "[REQUIRED_NON_MEMBER_EMAIL] REQUIRED_NON_MEMBER_EMAIL - 비회원 이메일 정보 없음 \n"
					+ "[REQUIRED_REFUND_ACCOUNT] REQUIRED_REFUND_ACCOUNT - 환불계좌 정보 없음 \n"
					+ "[REQUIRED_REGULAR_PAYMENT] REQUIRED_REGULAR_PAYMENT - 정기배송 결제 정보 없음 \n"
					+ "[USE_DUPLICATE_COUPON] USE_DUPLICATE_COUPON - 중복 쿠폰 사용 \n"
					+ "[FAIL_VALIDATION_GIFT] FAIL_VALIDATION_GIFT - 증정품 검증 실패") })
	public ApiResult<?> applyPayment(ApplyPaymentRequestDto reqDto) throws Exception {
		return shoppingCartService.applyPayment(reqDto);
	}

	/**
	 * 주문서 혜택 반영 조회
	 */
	@PostMapping(value = "/shopping/cart/getOrderSummary")
	@ApiOperation(value = "주문서 혜택 반영 조회", httpMethod = "POST")
	public ApiResult<?> getOrderSummary(GetOrderSummaryRequestDto reqDto) throws Exception {
		return shoppingCartService.getOrderSummary(reqDto);
	}

	@PostMapping(value = "/shopping/cart/getGiftPageInfo")
	@ApiOperation(value = "증정품 선택 리스트", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GiftListVo.class) })
	public ApiResult<?> getGiftPageInfo(GetOrderPageInfoRequestDto getOrderPageInfoRequestDto) throws Exception {
		return shoppingCartService.getGiftPageInfo(getOrderPageInfoRequestDto);
	}

	/**
	 * 장바구니 체크
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/shopping/cart/checkBuyPossibleCart")
	@ApiOperation(value = "장바구니 조회", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetCartInfoResponseDto.class) })
	public ApiResult<?> checkBuyPossibleCart(CheckBuyPossibleCartRequestDto reqDto) throws Exception {
		return shoppingCartService.checkBuyPossibleCart(reqDto);
	}
}