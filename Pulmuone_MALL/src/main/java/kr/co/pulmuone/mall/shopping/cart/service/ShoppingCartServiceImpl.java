package kr.co.pulmuone.mall.shopping.cart.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import kr.co.pulmuone.v1.comm.util.*;
import kr.co.pulmuone.v1.promotion.linkprice.service.LinkPriceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.mall.user.buyer.service.UserBuyerMallService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.constants.PromotionConstants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.DeviceType;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.enums.PgEnums;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.ApplyPayment;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.CartType;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.DeliveryType;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.GiftState;
import kr.co.pulmuone.v1.comm.enums.StoreEnums;
import kr.co.pulmuone.v1.comm.enums.StoreEnums.StoreApiDeliveryIntervalCode;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import kr.co.pulmuone.v1.comm.enums.SystemEnums.AgentType;
import kr.co.pulmuone.v1.comm.enums.UserEnums.BuyerType;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.PackageGoodsListDto;
import kr.co.pulmuone.v1.goods.goods.dto.RecalculationPackageDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsReserveOptionVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderDataDto;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderPaymentDataDto;
import kr.co.pulmuone.v1.order.order.dto.PutOrderPaymentCompleteResDto;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.order.service.OrderProcessBiz;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderRegistrationResponseDto;
import kr.co.pulmuone.v1.order.registration.service.OrderBindCartBizImpl;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationBiz;
import kr.co.pulmuone.v1.order.regular.dto.ApplyRegularResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.vo.RegularPaymentKeyVo;
import kr.co.pulmuone.v1.order.regular.service.OrderRegularBiz;
import kr.co.pulmuone.v1.pg.dto.BasicDataRequestDto;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.pg.dto.EtcDataCartDto;
import kr.co.pulmuone.v1.pg.service.PgAbstractService;
import kr.co.pulmuone.v1.pg.service.PgBiz;
import kr.co.pulmuone.v1.pg.service.inicis.service.InicisPgService;
import kr.co.pulmuone.v1.pg.service.kcp.service.KcpPgService;
import kr.co.pulmuone.v1.policy.clause.dto.GetClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetLatestJoinClauseListResultVo;
import kr.co.pulmuone.v1.policy.clause.service.PolicyClauseBiz;
import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import kr.co.pulmuone.v1.policy.payment.dto.PayUseListDto;
import kr.co.pulmuone.v1.policy.payment.service.PolicyPaymentBiz;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.exhibit.dto.GiftListResponseDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.vo.GiftGoodsVo;
import kr.co.pulmuone.v1.promotion.point.service.PromotionPointBiz;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartInfoAdditionalGoodsRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartInfoRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartInfoResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartListGoodsRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartListRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddSimpleCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddSimpleCartResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.ApplyPaymentRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartBuyerDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartDeliveryDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartGiftDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartGoodsDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartNotiDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartPickGoodsDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartRegularDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartRegularShippingDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartShippingDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingFastestScheduleDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartSummaryDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartSummaryResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartSummaryShippingDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CheckBuyPossibleCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CheckCartGoodsDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CheckCartResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CouponDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CreateOrderCartDto;
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
import kr.co.pulmuone.v1.shopping.cart.dto.GiftPageInfoResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GoodsCouponDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GoodsLackStockNotiDto;
import kr.co.pulmuone.v1.shopping.cart.dto.PutCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.PutCartShippingAddressRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.PutCartShippingAddressResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.ShippingCouponDto;
import kr.co.pulmuone.v1.shopping.cart.dto.UseGoodsCouponDto;
import kr.co.pulmuone.v1.shopping.cart.dto.UseShippingCouponDto;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartVo;
import kr.co.pulmuone.v1.shopping.cart.service.ShoppingCartBiz;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingAddressPossibleDeliveryInfoDto;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import kr.co.pulmuone.v1.store.shop.dto.vo.ShopVo;
import kr.co.pulmuone.v1.store.shop.service.StoreShopBiz;
import kr.co.pulmuone.v1.user.buyer.dto.CommonGetCodeListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.CommonGetRefundBankRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.CommonSaveRefundBankRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.CommonSaveShippingAddressRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetRefundBankResultVo;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.user.certification.dto.AddSessionShippingRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionUserCertificationResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetCertificationResultVo;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.join.service.UserJoinBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200831   	 홍진영            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private UserJoinBiz userJoinBiz;

	@Autowired
	public UserBuyerBiz userBuyerBiz;

	@Autowired
	public PolicyConfigBiz policyConfigBiz;

	@Autowired
	public ShoppingCartBiz shoppingCartBiz;

	@Autowired
	public GoodsGoodsBiz goodsGoodsBiz;

	@Autowired
	public UserCertificationBiz userCertificationBiz;

	@Autowired
	public StoreDeliveryBiz storeDeliveryBiz;

	@Autowired
	public OrderRegularBiz orderRegularBiz;

	@Autowired
	public PgBiz pgBiz;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	KcpPgService kcpPgService;

	@Autowired
	InicisPgService inicisPgService;

	@Autowired
	private PolicyPaymentBiz policyPaymentbiz;

	@Autowired
	private PromotionPointBiz promotionPointBiz;

	@Autowired
	private PolicyClauseBiz policyClauseBiz;

	@Autowired
	private PromotionCouponBiz promotionCouponBiz;

	@Autowired
	private UserBuyerMallService userBuyerMallService;

	@Autowired
	private OrderRegistrationBiz orderRegistrationBiz;

	@Autowired
	OrderOrderBiz orderOrderBiz;

	@Autowired
	StoreShopBiz storeShopBiz;

	@Autowired
	OrderBindCartBizImpl orderBindBiz;

	@Autowired
	OrderProcessBiz orderProcessBiz;

	@Autowired
	private LinkPriceService linkPriceService;

	/**
	 * 주문 사용 가능 장바구니 쿠폰 조회
	 *
	 * @param GetCartCouponListRequestDto
	 * @return CouponDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getCartCouponList(GetCartCouponListRequestDto getCartCouponListRequestDto) throws Exception {

		// 임직원가 적용시 쿠폰 이용 불가
		if ("Y".equals(getCartCouponListRequestDto.getEmployeeYn())) {
			return ApiResult.result(ShoppingEnums.GetCouponPageInfo.EMPLOYEE_NOT_ALLOW);
		}

		// 장바구니 정보 조회
		GetCartDataRequestDto cartDataRequestDto = new GetCartDataRequestDto();
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		cartDataRequestDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
		if (isMember) {
			cartDataRequestDto.setUrUserId(new Long(urUserId));
		}
		cartDataRequestDto.setDeviceInfo(DeviceUtil.getDirInfo());
		cartDataRequestDto.setApp(DeviceUtil.isApp());
		cartDataRequestDto.setMember(isMember);
		cartDataRequestDto.setEmployee(isEmployee);
		cartDataRequestDto.setReceiverZipCode(buyerVo.getReceiverZipCode());
		cartDataRequestDto.setBuildingCode(buyerVo.getBuildingCode());
		cartDataRequestDto.setUrErpEmployeeCode(buyerVo.getUrErpEmployeeCode());

		cartDataRequestDto.setCartType(getCartCouponListRequestDto.getCartType());
		cartDataRequestDto.setSpCartId(getCartCouponListRequestDto.getSpCartId());
		cartDataRequestDto.setEmployeeYn(getCartCouponListRequestDto.getEmployeeYn());
		cartDataRequestDto.setUseGoodsCoupon(getCartCouponListRequestDto.getUseGoodsCoupon());
		cartDataRequestDto.setArrivalScheduled(getCartCouponListRequestDto.getArrivalScheduled());
		cartDataRequestDto.setPresentYn(getCartCouponListRequestDto.getPresentYn());

		// 매장 관련 데이터
		if (ShoppingEnums.CartType.SHOP.getCode().equals(getCartCouponListRequestDto.getCartType())) {
			cartDataRequestDto.setStoreDeliveryType(getCartCouponListRequestDto.getStoreDeliveryType());
			cartDataRequestDto.setNextArrivalScheduledDate(getCartCouponListRequestDto.getStoreArrivalScheduledDate());
		}

		// 장바구니 정보 조회
		List<CartDeliveryDto> cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);

		// cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
		List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);

		// List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
		List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);

		// 접근 디바이스 타입 정보
		DeviceType deviceType = GoodsEnums.DeviceType.PC;
		if (DeviceUtil.isApp()) {
			deviceType = GoodsEnums.DeviceType.APP;
		} else if (DeviceUtil.getDirInfo().equalsIgnoreCase("mobile")) {
			deviceType = GoodsEnums.DeviceType.MOBILE;
		}

		// 장바구니 쿠폰 리스트 조회
		List<CouponDto> cartCouponList = promotionCouponBiz.getCartCouponApplicationListByUser(new Long(urUserId),
				cartGoodsList, deviceType);

		return ApiResult.success(cartCouponList);
	}

	/**
	 * 주문 사용 가능 쿠폰 조회
	 *
	 * @param GetCouponPageInfoRequestDto
	 * @return GetCouponPageInfoResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getCouponPageInfo(GetCouponPageInfoRequestDto getCouponPageInfoRequestDto) throws Exception {

		// 임직원가 적용시 쿠폰 이용 불가
		if ("Y".equals(getCouponPageInfoRequestDto.getEmployeeYn())) {
			return ApiResult.result(ShoppingEnums.GetCouponPageInfo.EMPLOYEE_NOT_ALLOW);
		}

		// 장바구니 정보 조회
		GetCartDataRequestDto cartDataRequestDto = new GetCartDataRequestDto();
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		cartDataRequestDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
		if (isMember) {
			cartDataRequestDto.setUrUserId(new Long(urUserId));
		}
		cartDataRequestDto.setDeviceInfo(DeviceUtil.getDirInfo());
		cartDataRequestDto.setApp(DeviceUtil.isApp());
		cartDataRequestDto.setMember(isMember);
		cartDataRequestDto.setEmployee(isEmployee);
		cartDataRequestDto.setReceiverZipCode(buyerVo.getReceiverZipCode());
		cartDataRequestDto.setBuildingCode(buyerVo.getBuildingCode());
		cartDataRequestDto.setUrErpEmployeeCode(buyerVo.getUrErpEmployeeCode());

		cartDataRequestDto.setCartType(getCouponPageInfoRequestDto.getCartType());
		cartDataRequestDto.setSpCartId(getCouponPageInfoRequestDto.getSpCartId());
		cartDataRequestDto.setEmployeeYn(getCouponPageInfoRequestDto.getEmployeeYn());
		cartDataRequestDto.setArrivalScheduled(getCouponPageInfoRequestDto.getArrivalScheduled());
		cartDataRequestDto.setPresentYn(getCouponPageInfoRequestDto.getPresentYn());

		// 매장 관련 데이터
		if (ShoppingEnums.CartType.SHOP.getCode().equals(getCouponPageInfoRequestDto.getCartType())) {
			cartDataRequestDto.setStoreDeliveryType(getCouponPageInfoRequestDto.getStoreDeliveryType());
			cartDataRequestDto.setNextArrivalScheduledDate(getCouponPageInfoRequestDto.getStoreArrivalScheduledDate());
		}

		// 장바구니 정보 조회
		List<CartDeliveryDto> cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);

		// cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
		List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);

		// List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
		List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);

		// 접근 디바이스 타입 정보
		DeviceType deviceType = GoodsEnums.DeviceType.PC;
		if (DeviceUtil.isApp()) {
			deviceType = GoodsEnums.DeviceType.APP;
		} else if (DeviceUtil.getDirInfo().equalsIgnoreCase("mobile")) {
			deviceType = GoodsEnums.DeviceType.MOBILE;
		}

		// 상품 쿠폰 리스트 조회
		List<GoodsCouponDto> goodsList = promotionCouponBiz.getGoodsCouponApplicationListByUser(new Long(urUserId),
				cartGoodsList, deviceType);

		// 장바구니 쿠폰 리스트 조회
		List<CouponDto> cartCouponList = promotionCouponBiz.getCartCouponApplicationListByUser(new Long(urUserId),
				cartGoodsList, deviceType);

		// 배송비 쿠폰 리스트 조회
		List<ShippingCouponDto> shippingCouponList = promotionCouponBiz
				.getShippingCouponApplicationListByUser(new Long(urUserId), cartDataDto, deviceType);

		// 사용 가능 쿠폰 개수
		int availableCouponCnt = 0;
		List<Long> pmCouponIssueIds = new ArrayList<Long>();

		Stream<CouponDto> useCouponlistStream = null;
		if (goodsList != null) {
			useCouponlistStream = goodsList.stream().map(GoodsCouponDto::getCouponList).flatMap(Collection::stream);
		}
		if (cartCouponList != null) {
			Stream<CouponDto> useCartCouponlistStream = cartCouponList.stream();
			if(useCouponlistStream == null) {
				useCouponlistStream = useCartCouponlistStream;
			} else {
				useCouponlistStream = Stream.concat(useCouponlistStream, useCartCouponlistStream);
			}
		}
		if (shippingCouponList != null) {
			Stream<CouponDto> useShippingCouponlistStream = shippingCouponList.stream().map(ShippingCouponDto::getCouponList).flatMap(Collection::stream);
			if(useCouponlistStream == null) {
				useCouponlistStream = useShippingCouponlistStream;
			} else {
				useCouponlistStream = Stream.concat(useCouponlistStream, useShippingCouponlistStream);
			}
		}

		if(useCouponlistStream != null) {
			pmCouponIssueIds = useCouponlistStream.filter(dto -> dto.isActive() == true).map(CouponDto::getPmCouponIssueId).distinct().collect(Collectors.toList());
			availableCouponCnt = pmCouponIssueIds.size();
		}

		GetCouponPageInfoResponseDto resDto = new GetCouponPageInfoResponseDto();
		resDto.setGoodsList(goodsList);
		resDto.setShippingCoupon(shippingCouponList);
		resDto.setCartCouponList(cartCouponList);
		resDto.setAvailableCouponCnt(availableCouponCnt);
		resDto.setPmCouponIssueIds(pmCouponIssueIds);

		return ApiResult.success(resDto);
	}

	/**
	 * 주문서 정보 조회
	 *
	 * @param GetOrderPageInfoRequestDto
	 * @return GetOrderPageInfoResponseDto
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ApiResult<?> getOrderPageInfo(GetOrderPageInfoRequestDto getOrderPageInfoRequestDto) throws Exception {

		// 장바구니 파라미터 요청정보 하나도 없을때 예외 처리
		if (getOrderPageInfoRequestDto.getSpCartId() == null || getOrderPageInfoRequestDto.getSpCartId().isEmpty()) {
			return ApiResult.result(ShoppingEnums.OrderPageInfo.NO_CART_DATA);
		}

		// 장바구니 정보 조회
		GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();
		GetCartDataRequestDto cartDataRequestDto = new GetCartDataRequestDto();
		ShopVo store = null;
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		boolean isAdditionalOrder = false;	// 정기배송 추가주문 여부
		cartDataRequestDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
		Long longUrUserId = 0L;
		if (isMember) {
			longUrUserId = new Long(urUserId);
			cartDataRequestDto.setUrUserId(longUrUserId);
		}
		cartDataRequestDto.setDeviceInfo(DeviceUtil.getDirInfo());
		cartDataRequestDto.setApp(DeviceUtil.isApp());
		cartDataRequestDto.setMember(isMember);
		cartDataRequestDto.setEmployee(isEmployee);
		// 정기 배송일때 기존 주문건이 있으면 주소지를 기존 주문건 유지해야함
		if (ShoppingEnums.CartType.REGULAR.getCode().equals(getOrderPageInfoRequestDto.getCartType())) {
			CartRegularShippingDto cartRegularShippingDto = orderRegularBiz.getRegularInfoByCart(cartDataRequestDto.getUrUserId());
			if (cartRegularShippingDto.isAdditionalOrder()) {
				sessionShippingDto = orderRegularBiz.getRegularShippingZone(cartRegularShippingDto.getOdRegularReqId());
				isAdditionalOrder = true;
			}
		}
		cartDataRequestDto.setReceiverZipCode(sessionShippingDto.getReceiverZipCode());
		cartDataRequestDto.setBuildingCode(sessionShippingDto.getBuildingCode());
		cartDataRequestDto.setUrErpEmployeeCode(buyerVo.getUrErpEmployeeCode());

		cartDataRequestDto.setCartType(getOrderPageInfoRequestDto.getCartType());
		cartDataRequestDto.setSpCartId(getOrderPageInfoRequestDto.getSpCartId());
		cartDataRequestDto.setEmployeeYn(getOrderPageInfoRequestDto.getEmployeeYn());
		cartDataRequestDto.setArrivalScheduled(getOrderPageInfoRequestDto.getArrivalScheduled());
		cartDataRequestDto.setArrivalGoods(getOrderPageInfoRequestDto.getArrivalGoods());
		cartDataRequestDto.setPresentYn(getOrderPageInfoRequestDto.getPresentYn());

		// 매장 관련 데이터
		if (ShoppingEnums.CartType.SHOP.getCode().equals(getOrderPageInfoRequestDto.getCartType())) {
			cartDataRequestDto.setStoreDeliveryType(getOrderPageInfoRequestDto.getStoreDeliveryType());
			cartDataRequestDto.setNextArrivalScheduledDate(getOrderPageInfoRequestDto.getStoreArrivalScheduledDate());
		}

		ShippingAddressPossibleDeliveryInfoDto deliveryInfo = null;
		CartNotiDto notiDto = null;
		List<CartDeliveryDto> cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);

		if(cartDataDto.isEmpty()) {
			return ApiResult.result(ShoppingEnums.OrderPageInfo.NO_CART_DATA);
		}

		// 매장 정보
		CartStoreDto cartStoreDto = new CartStoreDto();
		if (ShoppingEnums.CartType.SHOP.getCode().equals(getOrderPageInfoRequestDto.getCartType())) {
			cartStoreDto.setStoreDeliveryType(getOrderPageInfoRequestDto.getStoreDeliveryType());
			cartStoreDto.setStoreArrivalScheduledDate(getOrderPageInfoRequestDto.getStoreArrivalScheduledDate());
			cartStoreDto.setUrStoreScheduleId(getOrderPageInfoRequestDto.getUrStoreScheduleId());
		}

		CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataRequestDto, cartDataDto, cartStoreDto);
		// 구매불가 상품 포함시 에러 리턴
		if (!ApplyPayment.SUCCESS.getCode().equals(checkCartResponseDto.getResult().getCode())) {
			if (ApplyPayment.IMPOSSIBLE_STORE_SCHEDULE.getCode().equals(checkCartResponseDto.getResult().getCode())) {
				return ApiResult.result(ShoppingEnums.OrderPageInfo.IMPOSSIBLE_STORE_SCHEDULE);
			} else {
				return ApiResult.result(ShoppingEnums.OrderPageInfo.NOT_BUY_GOODS_INCLUDE);
			}
		}

		CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto);

		if (sessionShippingDto != null && sessionShippingDto.getReceiverZipCode() != null
				&& sessionShippingDto.getBuildingCode() != null) {
			deliveryInfo = storeDeliveryBiz.getShippingAddressPossibleDeliveryInfo(
					sessionShippingDto.getReceiverZipCode(), sessionShippingDto.getBuildingCode());

			// 매장 배송일때
			if (CartType.SHOP.getCode().equals(getOrderPageInfoRequestDto.getCartType())) {
				ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryBiz.getStoreShippingPossibilityStoreDeliveryAreaInfo(sessionShippingDto.getReceiverZipCode(), sessionShippingDto.getBuildingCode());
				if(shippingPossibilityStoreDeliveryAreaInfo != null) {
					store = storeShopBiz.getShop(shippingPossibilityStoreDeliveryAreaInfo.getUrStoreId());
				}
			}
		}

		// 결제 및 카드 정보 조회
		PayUseListDto payUseListDto = policyPaymentbiz.getPayUseList();

		CommonGetRefundBankResultVo refundBankInfo = new CommonGetRefundBankResultVo();
		HashMap<String, Object> discountInfo = new HashMap<>();
		List<GetLatestJoinClauseListResultVo> clauseList = new ArrayList<>();
		List<String> psClauseGrpCdList = new ArrayList<>();
		GetClauseRequestDto clauseReqDto = new GetClauseRequestDto();
		HashMap regularPayment = new HashMap<>();
		HashMap<String, String> userPayment = new HashMap<>();

		if (isMember) {
			// 환불계좌 정보 조회
			CommonGetRefundBankRequestDto refundBankReqDto = new CommonGetRefundBankRequestDto();
			refundBankReqDto.setUrUserId(urUserId);
			refundBankInfo = userBuyerBiz.getRefundBank(refundBankReqDto);

			// 사용가능 쿠폰 개수 조회
			GetCouponPageInfoRequestDto getCouponPageInfoRequestDto = new GetCouponPageInfoRequestDto();
			getCouponPageInfoRequestDto.setCartType(getOrderPageInfoRequestDto.getCartType());
			getCouponPageInfoRequestDto.setSpCartId(getOrderPageInfoRequestDto.getSpCartId());
			getCouponPageInfoRequestDto.setEmployeeYn(getOrderPageInfoRequestDto.getEmployeeYn());

			ApiResult<?> couponPageInfo = getCouponPageInfo(getCouponPageInfoRequestDto);
			if(BaseEnums.Default.SUCCESS.getCode().equals(couponPageInfo.getCode())) {
				GetCouponPageInfoResponseDto couponPageInfoResDto = (GetCouponPageInfoResponseDto) couponPageInfo.getData();
				int availableCouponCnt = couponPageInfoResDto.getAvailableCouponCnt();
				discountInfo.put("availableCouponCnt", availableCouponCnt);
			} else {
				discountInfo.put("availableCouponCnt", 0);
			}

			// 사용가능 적립금 조회
			int availablePoint = promotionPointBiz.getPointUsable(longUrUserId);
			discountInfo.put("availablePoint", availablePoint);

			// 정기결제 카드 정보 조회
			RegularPaymentKeyVo regularPaymentVo = orderRegularBiz.getRegularPaymentKey(longUrUserId);
			if (regularPaymentVo != null) {
				regularPayment.put("isExistPaymentKey", true);
				regularPayment.put("cardName", regularPaymentVo.getCardName());
				regularPayment.put("cardMaskNumber", regularPaymentVo.getCardMaskingNumber());
			} else {
				regularPayment.put("isExistPaymentKey", false);
			}

			// 회원 사용 카드 정보 조회
			userPayment = userBuyerBiz.getUserPaymentInfo(urUserId);

			if (ShoppingEnums.CartType.REGULAR.getCode().equals(getOrderPageInfoRequestDto.getCartType())) {
				// 정기배송 이용약관
				psClauseGrpCdList.add(ShoppingEnums.ClauseGroupCode.REGULAR_DELIVERY_TERMS.getCode());
			} else {
				// 구매약관 정보 조회
				psClauseGrpCdList.add(ShoppingEnums.ClauseGroupCode.PURCHASE_TERMS.getCode());
			}
		} else {
			// 비회원구매일 경우 이용약관 추가
			psClauseGrpCdList.add(ShoppingEnums.ClauseGroupCode.NON_MEMBER_PRIVACY_POLICY.getCode());
		}

		// 환불계좌입력 위한 은행정보 리스트 추가
		CommonGetCodeListResponseDto commonCodeList = (CommonGetCodeListResponseDto) userBuyerBiz.getRefundBankInfo()
				.getData();
		List<CodeInfoVo> refundBankList = commonCodeList.getRows();

		clauseReqDto.setPsClauseGrpCdList(psClauseGrpCdList);
		ApiResult<?> clauseResult = policyClauseBiz.getPurchaseTermsClauseList(clauseReqDto);
		clauseList = (List<GetLatestJoinClauseListResultVo>) clauseResult.getData();

		// 무통장 입금 결제 가능 여부
		String virtualAccountYn = shoppingCartBiz.getVirtualAccountYn(cartDataDto, getOrderPageInfoRequestDto.getPresentYn());

		GetOrderPageInfoResponseDto resDto = new GetOrderPageInfoResponseDto();
		// 장바구니 정보
		resDto.setShippingAddress(sessionShippingDto);
		resDto.setDeliveryInfo(deliveryInfo);
		resDto.setCart(cartDataDto);
		resDto.setCartSummary(cartSummaryDto);
		// 결제 및 카드 정보
		resDto.setPaymentType(payUseListDto.getPaymentType());
		resDto.setCardList(payUseListDto.getCardList());
		resDto.setInstallmentPeriod(payUseListDto.getInstallmentPeriod());
		resDto.setCartBenefit(payUseListDto.getCartBenefit());
		// 환불계좌 정보
		resDto.setRefundBank(refundBankInfo);
		// 사용가능 쿠폰 개수 & 적립금 정보
		resDto.setDiscountInfo(discountInfo);
		// 약관리스트
		resDto.setClause(clauseList);
		// 정기결제 카드정보
		resDto.setRegularPayment(regularPayment);
		// 회원 사용 카드정보
		resDto.setUserPayment(userPayment);
		// 무통장 입금 결제 가능 여부
		resDto.setVirtualAccountYn(virtualAccountYn);
		// 환불계좌 은행정보 리스트
		resDto.setRefundBankList(refundBankList);
		// 매장정보
		resDto.setStore(store);
		// 정기배송 추가 주문 여부
		resDto.setAdditionalOrder(isAdditionalOrder);

		return ApiResult.success(resDto);
	}

	@Override
	public ApiResult<?> getOrderUserCertification() throws Exception {
		GetCertificationResultVo getCertificationResultVo = new GetCertificationResultVo();
		GetSessionUserCertificationResponseDto getSessionUserCertificationResponseDto = userCertificationBiz.getSessionUserCertification();

		if (getSessionUserCertificationResponseDto.getCi() == null) {
			return ApiResult.result(ApplyPayment.REQUIRED_NON_MEMBER_CERTIFICATION);
		}

		if (!userJoinBiz.isCheckUnderAge14(getSessionUserCertificationResponseDto.getBirthday())) {
			return ApiResult.result(ApplyPayment.NON_MEMBER_UNDER_14_AGE_NOT_ALLOW);
		}

		getCertificationResultVo.setUserName(getSessionUserCertificationResponseDto.getUserName());
		getCertificationResultVo.setMobile(getSessionUserCertificationResponseDto.getMobile());
		getCertificationResultVo.setBirth(getSessionUserCertificationResponseDto.getBirthday());
		getCertificationResultVo.setBeforeUserDropYn(getSessionUserCertificationResponseDto.getBeforeUserDropYn());

		return ApiResult.success(getCertificationResultVo);
	}

	/**
	 * 장바구니 배송지 저장
	 *
	 * @param PutCartShippingAddressRequestDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putCartShippingAddress(PutCartShippingAddressRequestDto putCartShippingAddressRequestDto)
			throws Exception {
		PutCartShippingAddressResponseDto putCartShippingAddressResponseDto = new PutCartShippingAddressResponseDto();

		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());

		// 세션에 배송지 정보 저장
		AddSessionShippingRequestDto addSessionShippingRequestDto = new AddSessionShippingRequestDto();
		addSessionShippingRequestDto.setReceiverName(putCartShippingAddressRequestDto.getReceiverName());
		addSessionShippingRequestDto.setReceiverZipCode(putCartShippingAddressRequestDto.getReceiverZipCode());
		addSessionShippingRequestDto.setReceiverAddress1(putCartShippingAddressRequestDto.getReceiverAddress1());
		addSessionShippingRequestDto.setReceiverAddress2(putCartShippingAddressRequestDto.getReceiverAddress2());
		addSessionShippingRequestDto.setBuildingCode(putCartShippingAddressRequestDto.getBuildingCode());
		addSessionShippingRequestDto.setReceiverMobile(putCartShippingAddressRequestDto.getReceiverMobile());
		addSessionShippingRequestDto
				.setAccessInformationType(putCartShippingAddressRequestDto.getAccessInformationType());
		addSessionShippingRequestDto
				.setAccessInformationPassword(putCartShippingAddressRequestDto.getAccessInformationPassword());
		addSessionShippingRequestDto.setShippingComment(putCartShippingAddressRequestDto.getShippingComment());
		addSessionShippingRequestDto.setBasicYn(putCartShippingAddressRequestDto.getBasicYn());
		addSessionShippingRequestDto.setAddShippingAddress(putCartShippingAddressRequestDto.getAddShippingAddress());
		addSessionShippingRequestDto.setSelectBasicYn(putCartShippingAddressRequestDto.getSelectBasicYn());
		addSessionShippingRequestDto.setShippingAddressId(putCartShippingAddressRequestDto.getShippingAddressId());
		userCertificationBiz.addSessionShipping(addSessionShippingRequestDto);

		// 회원배송지목록에 배송지 정보 저장
		if (isMember) {

			// 장바구니 배송지 저장
			if (putCartShippingAddressRequestDto.getAddShippingAddress()
					.equals(ShoppingEnums.CartShippingAddress.ADD.getCode())) {
				CommonSaveShippingAddressRequestDto commonSaveShippingAddressRequestDto = new CommonSaveShippingAddressRequestDto();
				commonSaveShippingAddressRequestDto.setDefaultYn(putCartShippingAddressRequestDto.getBasicYn());
				commonSaveShippingAddressRequestDto.setReceiverName(putCartShippingAddressRequestDto.getReceiverName());
				commonSaveShippingAddressRequestDto
						.setReceiverMobile(putCartShippingAddressRequestDto.getReceiverMobile());
				commonSaveShippingAddressRequestDto
						.setReceiverAddress1(putCartShippingAddressRequestDto.getReceiverAddress1());
				commonSaveShippingAddressRequestDto
						.setReceiverAddress2(putCartShippingAddressRequestDto.getReceiverAddress2());
				commonSaveShippingAddressRequestDto
						.setReceiverZipCode(putCartShippingAddressRequestDto.getReceiverZipCode());
				commonSaveShippingAddressRequestDto.setBuildingCode(putCartShippingAddressRequestDto.getBuildingCode());
				commonSaveShippingAddressRequestDto
						.setShippingComment(putCartShippingAddressRequestDto.getShippingComment());
				commonSaveShippingAddressRequestDto
						.setAccessInformationType(putCartShippingAddressRequestDto.getAccessInformationType());
				commonSaveShippingAddressRequestDto
						.setAccessInformationPassword(putCartShippingAddressRequestDto.getAccessInformationPassword());
				commonSaveShippingAddressRequestDto.setUrUserId(Long.parseLong(urUserId));

				userBuyerBiz.addShippingAddress(commonSaveShippingAddressRequestDto);

				// 장바구니 배송지 수정
			} else if (putCartShippingAddressRequestDto.getAddShippingAddress()
					.equals(ShoppingEnums.CartShippingAddress.PUT.getCode())) {
				CommonSaveShippingAddressRequestDto commonSaveShippingAddressRequestDto = new CommonSaveShippingAddressRequestDto();
				commonSaveShippingAddressRequestDto.setDefaultYn(putCartShippingAddressRequestDto.getBasicYn());
				commonSaveShippingAddressRequestDto.setReceiverName(putCartShippingAddressRequestDto.getReceiverName());
				commonSaveShippingAddressRequestDto
						.setReceiverMobile(putCartShippingAddressRequestDto.getReceiverMobile());
				commonSaveShippingAddressRequestDto
						.setReceiverAddress1(putCartShippingAddressRequestDto.getReceiverAddress1());
				commonSaveShippingAddressRequestDto
						.setReceiverAddress2(putCartShippingAddressRequestDto.getReceiverAddress2());
				commonSaveShippingAddressRequestDto
						.setReceiverZipCode(putCartShippingAddressRequestDto.getReceiverZipCode());
				commonSaveShippingAddressRequestDto.setBuildingCode(putCartShippingAddressRequestDto.getBuildingCode());
				commonSaveShippingAddressRequestDto
						.setShippingComment(putCartShippingAddressRequestDto.getShippingComment());
				commonSaveShippingAddressRequestDto
						.setAccessInformationType(putCartShippingAddressRequestDto.getAccessInformationType());
				commonSaveShippingAddressRequestDto
						.setAccessInformationPassword(putCartShippingAddressRequestDto.getAccessInformationPassword());
				commonSaveShippingAddressRequestDto
						.setSelectBasicYn(putCartShippingAddressRequestDto.getSelectBasicYn());
				commonSaveShippingAddressRequestDto.setUrUserId(new Long(urUserId));

				userBuyerBiz.putShippingAddress(commonSaveShippingAddressRequestDto);
			}

		}

		// 배송 불가 상품 리턴
		GetCartDataRequestDto reqDto = new GetCartDataRequestDto();
		reqDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
		if (isMember) {
			reqDto.setUrUserId(new Long(urUserId));
		}
		reqDto.setDeviceInfo(DeviceUtil.getDirInfo());
		reqDto.setApp(DeviceUtil.isApp());
		reqDto.setMember(isMember);
		reqDto.setEmployee(isEmployee);
		reqDto.setReceiverZipCode(buyerVo.getReceiverZipCode());
		reqDto.setBuildingCode(buyerVo.getBuildingCode());
		reqDto.setUrErpEmployeeCode(buyerVo.getUrErpEmployeeCode());
		reqDto.setCartType(putCartShippingAddressRequestDto.getCartType());
		reqDto.setSpCartId(putCartShippingAddressRequestDto.getSpCartId());

		List<CartDeliveryDto> cartDataDto = shoppingCartBiz.getCartData(reqDto);

		List<CartGoodsDto> cartGoodsSaleStatusList = cartDataDto.stream().map(m -> m.getShipping())
				.flatMap(Collection::stream).map(m -> m.getGoods()).flatMap(Collection::stream)
				.collect(Collectors.toList());

		// 주소기반으로 배송 불가능한 상품 있을 경우
		List<String> notShippingGoodsList = new ArrayList<>();
		for (CartGoodsDto cartGoods : cartGoodsSaleStatusList) {
			if (!cartGoods.isShippingPossibility()) {
				putCartShippingAddressResponseDto.setNotShippingGoods(true);
				notShippingGoodsList.add(cartGoods.getGoodsName());
			}
		}
		putCartShippingAddressResponseDto.setNotShippingGoodsList(notShippingGoodsList);

		if (!notShippingGoodsList.isEmpty() && cartGoodsSaleStatusList.size() == notShippingGoodsList.size()) {
			putCartShippingAddressResponseDto.setNotShippingAllGoods(true);
		}

		return ApiResult.success(putCartShippingAddressResponseDto);
	}

	/**
	 * 배송비 절약 상품 목록
	 *
	 * @param GetSaveShippingCostGoodsListRequestDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getSaveShippingCostGoodsList(
			GetSaveShippingCostGoodsListRequestDto getSaveShippingCostGoodsListRequestDto) throws Exception {
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		String dirInfo = DeviceUtil.getDirInfo();
		boolean isApp = DeviceUtil.isApp();
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = null;

		// 요청DTO의 배송타입으로 판매타입 가져오기
		String saleType = StringUtil.nvl(ShoppingEnums.DeliveryType
				.findByCode(getSaveShippingCostGoodsListRequestDto.getDeliveryType()).getSaleType());


		// 매장배송일경우 배장정보 같이 넘기기
		if (DeliveryType.SHOP_DELIVERY.getCode().equals(getSaveShippingCostGoodsListRequestDto.getDeliveryType())) {
			GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();
			shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryBiz.getStoreShippingPossibilityStoreDeliveryAreaInfo(sessionShippingDto.getReceiverZipCode(), sessionShippingDto.getBuildingCode());
		}

		// 상품조회위한 파라미터 세팅
		getSaveShippingCostGoodsListRequestDto.setSaleType(saleType);
		getSaveShippingCostGoodsListRequestDto.setDeviceInfo(dirInfo);
		getSaveShippingCostGoodsListRequestDto.setApp(isApp);
		getSaveShippingCostGoodsListRequestDto.setMember(isMember);
		getSaveShippingCostGoodsListRequestDto.setEmployee(isEmployee);
		getSaveShippingCostGoodsListRequestDto.setStoreDeliveryInfo(shippingPossibilityStoreDeliveryAreaInfo);

		List<GoodsSearchResultDto> goodsList = shoppingCartBiz
				.getSaveShippingCostGoodsList(getSaveShippingCostGoodsListRequestDto);

		return ApiResult.success(goodsList);
	}

	/**
	 * 장바구니 수정
	 *
	 * @param PutCartRequestDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putCart(PutCartRequestDto putCartRequestDto) throws Exception {
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		String dirInfo = DeviceUtil.getDirInfo();
		boolean isApp = DeviceUtil.isApp();
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();

		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();

		SpCartVo cartVo = shoppingCartBiz.getCart(putCartRequestDto.getSpCartId());

		// 배송타입
		String deliveryType = cartVo.getDeliveryType();

		GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder().ilGoodsId(cartVo.getIlGoodsId()).deviceInfo(dirInfo)
				.isApp(isApp).isMember(isMember).isEmployee(isEmployee).build();

		// 매장 배송일경우 매장 재고를 가지고 오기 위해 상품요청 정보에 매장 정보 추가
		if (DeliveryType.SHOP_DELIVERY.getCode().equals(cartVo.getDeliveryType())) {
			addCartInfoRequestDto.setStoreDeliveryYn("Y");
			goodsRequestDto.setStoreDelivery(true);
			goodsRequestDto.setStoreDeliveryInfo(storeDeliveryBiz.getStoreShippingPossibilityStoreDeliveryAreaInfo(sessionShippingDto.getReceiverZipCode(), sessionShippingDto.getBuildingCode()));
		}

		// 상품 기본정보
		BasicSelectGoodsVo goods = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);

		CartGoodsDto goodsDto = new CartGoodsDto(cartVo);

		// 상품이 없을 경우
		if (goods == null) {
			return ApiResult.result(ShoppingEnums.AddCartInfo.NO_GOODS);
		} else {

			// 상품 재고 정보
			int stock = 0;
			// 묶음상품일 경우
			if (goods.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {

				List<PackageGoodsListDto> goodsPackageList = goodsGoodsBiz.getPackagGoodsInfoList(goods.getIlGoodsId(),
						isMember, isEmployee, false, null, putCartRequestDto.getQty());

				RecalculationPackageDto recalculationPackage = goodsGoodsBiz.getRecalculationPackage(goods.getSaleStatus(), goodsPackageList);

				goods.setSaleStatus(recalculationPackage.getSaleStatus());
				stock = recalculationPackage.getStockQty();

				// 골라담기상품일 경우
			} else if (ShoppingEnums.CartPromotionType.EXHIBIT_SELECT.getCode().equals(goodsDto.getCartPromotionType())
					|| ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode()
					.equals(goodsDto.getCartPromotionType())) {

				List<CartPickGoodsDto> pickGoodsDtoList = shoppingCartBiz.getCartPickGoodsList(goodsDto.getSpCartId(),
						isMember, isEmployee, false, null, putCartRequestDto.getQty(), putCartRequestDto.getGoodsDailyCycleType(), null);
				RecalculationPackageDto recalculatonPackage = shoppingCartBiz.getRecalculationCartPickGoodsList(goods.getSaleStatus(), pickGoodsDtoList);
				goods.setSaleStatus(recalculatonPackage.getSaleStatus());
				stock = recalculatonPackage.getStockQty();
			} else {
				stock = goods.getStockQty();
			}

			if (DeliveryType.RESERVATION.getCode().equals(cartVo.getDeliveryType())) {
				// 예약배송일 경우 예약상품 정보 조회 후 스케줄 처리
				GoodsReserveOptionVo goodsReserveOptionVo = goodsGoodsBiz.getGoodsReserveOption(putCartRequestDto.getIlGoodsReserveOptionId());
				if (goodsReserveOptionVo == null) {
					return ApiResult.result(ShoppingEnums.AddCartInfo.NO_GOODS_STATUS_ON_SALE);
				} else {
					LocalDateTime currentDateTime = LocalDateTime.now();
					if (!(goodsReserveOptionVo.getReserveStartDate().compareTo(currentDateTime) <= 0
							&& currentDateTime
							.compareTo(goodsReserveOptionVo.getReserveEndDate()) <= 0)) {
						// 회차가 기간이 지나면 판매 중지 상태 변경
						return ApiResult.result(ShoppingEnums.AddCartInfo.NO_GOODS_STATUS_ON_SALE);
					} else if (!(goodsReserveOptionVo.getStockQty() > 0)) {
						// 회차의 수량이 없을때 일시 품절
						return ApiResult.result(ShoppingEnums.AddCartInfo.GOODS_LACK_STOCK);
					} else if (goodsReserveOptionVo.getStockQty() < putCartRequestDto.getQty()) {
						// 회차 수량보다 더 많이 담았을 경우
						return ApiResult.result(ShoppingEnums.AddCartInfo.GOODS_LACK_STOCK);
					}
				}
			} else {
				// 상품 재고가 구매 수량보다 작을경우
				if (stock < putCartRequestDto.getQty()) {
					return ApiResult.result(ShoppingEnums.AddCartInfo.GOODS_LACK_STOCK);
				}
			}

			if ("Y".equals(goods.getGoodsDailyBulkYn())
					&& goods.getSaleType().equals(GoodsEnums.SaleType.DAILY.getCode())) {
				ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(goods.getIlGoodsId(),
						sessionShippingDto.getReceiverZipCode(), sessionShippingDto.getBuildingCode());
				// 판매타입이 일일배송상품이고 배송유형이 일괄배송이면서 택배배송 가능 권역일경우 일반 배송으로 처리
				if (shippingPossibilityStoreDeliveryAreaInfo == null ||
						StoreEnums.StoreApiDeliveryIntervalCode.PARCEL.getCommonCode().equals(shippingPossibilityStoreDeliveryAreaInfo.getStoreDeliveryIntervalType())) {
					deliveryType = ShoppingEnums.DeliveryType.NORMAL.getCode();
				}
			}




			List<AddCartInfoAdditionalGoodsRequestDto> additionalGoodsList = putCartRequestDto.getAdditionalGoodsList();
			if (additionalGoodsList != null && additionalGoodsList.size() > 0) {
				for (AddCartInfoAdditionalGoodsRequestDto additionalGoodsRequestDto : additionalGoodsList) {

					GoodsRequestDto addGoodsRequestDto = GoodsRequestDto.builder()
							.ilGoodsId(additionalGoodsRequestDto.getIlGoodsId()).deviceInfo(dirInfo).isApp(isApp)
							.isMember(isMember).isEmployee(isEmployee).build();

					BasicSelectGoodsVo addGoods = goodsGoodsBiz.getGoodsBasicInfo(addGoodsRequestDto);

					// 추가 구성 상품이 없을 경우
					if (addGoods == null) {
						return ApiResult.result(ShoppingEnums.AddCartInfo.NO_GOODS);
					} else {
						// 추가 상품이 판매중이 아닐때
						if (!GoodsEnums.SaleStatus.ON_SALE.getCode().equals(addGoods.getSaleStatus())) {
							return ApiResult.result(ShoppingEnums.AddCartInfo.NO_ADD_GOODS_STATUS_ON_SALE);
						}

						// 추가 상품 재고가 구매 수량보다 작을경우
						if (addGoods.getStockQty() < additionalGoodsRequestDto.getQty()) {
							return ApiResult.result(ShoppingEnums.AddCartInfo.ADD_GOODS_LACK_STOCK);
						}
					}
				}

				addCartInfoRequestDto.setAdditionalGoodsList(additionalGoodsList);
			}

			addCartInfoRequestDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
			if (isMember) {
				addCartInfoRequestDto.setUrUserId(new Long(urUserId));
			}
			addCartInfoRequestDto.setSaleType(goods.getSaleType());
			addCartInfoRequestDto.setGoodsDailyType(goods.getGoodsDailyType());
			addCartInfoRequestDto.setDeliveryType(deliveryType);
			addCartInfoRequestDto.setIlGoodsId(cartVo.getIlGoodsId());
			addCartInfoRequestDto.setQty(putCartRequestDto.getQty());
			addCartInfoRequestDto.setIlGoodsReserveOptionId(putCartRequestDto.getIlGoodsReserveOptionId());
			addCartInfoRequestDto.setBuyNowYn("N");
			addCartInfoRequestDto.setGoodsDailyCycleType(putCartRequestDto.getGoodsDailyCycleType());
			addCartInfoRequestDto.setGoodsDailyCycleTermType(putCartRequestDto.getGoodsDailyCycleTermType());
			addCartInfoRequestDto
					.setGoodsDailyCycleGreenJuiceWeekType(putCartRequestDto.getGoodsDailyCycleGreenJuiceWeekType());
			addCartInfoRequestDto.setGoodsDailyAllergyYn(putCartRequestDto.getGoodsDailyAllergyYn());
			addCartInfoRequestDto.setGoodsDailyBulkYn(putCartRequestDto.getGoodsDailyBulkYn());
			addCartInfoRequestDto.setGoodsBulkType(putCartRequestDto.getGoodsBulkType());

			// 장바구니 필수 옵션 체크
			if (!shoppingCartBiz.isValidationAddCartInfo(addCartInfoRequestDto)) {
				return ApiResult.result(ShoppingEnums.AddCartInfo.FAIL_VALIDATION);
			}

			putCartRequestDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
			if (isMember) {
				putCartRequestDto.setUrUserId(new Long(urUserId));
			}
			putCartRequestDto.setIlGoodsId(cartVo.getIlGoodsId());
			putCartRequestDto.setDeliveryType(deliveryType);
			putCartRequestDto.setSaleType(goods.getSaleType());

			// 장바구니 수정
			shoppingCartBiz.putCart(putCartRequestDto);
		}

		return ApiResult.success();
	}

	/**
	 * 장바구니 일괄 추가
	 *
	 * @param AddCartListRequestDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> addCartList(AddCartListRequestDto addCartListRequestDto) throws Exception {

		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		String dirInfo = DeviceUtil.getDirInfo();
		boolean isApp = DeviceUtil.isApp();
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		List<AddCartInfoRequestDto> addGoodsList = new ArrayList<AddCartInfoRequestDto>();
		MessageCommEnum resultCode = BaseEnums.Default.SUCCESS;
		boolean isDawnDelivery = false;// 새벽배송 여부
		LocalDate arrivalScheduledDate = null; //고객 선택 도착예정일
		int buyQty = 1; // 구매 수량

		List<CheckCartGoodsDto> noGoodsStatusOnSalelist  = new ArrayList<CheckCartGoodsDto>();
		List<CheckCartGoodsDto> goodsLackStockList  = new ArrayList<CheckCartGoodsDto>();
		List<CheckCartGoodsDto> lackLimitMinCntList  = new ArrayList<CheckCartGoodsDto>();
		List<CheckCartGoodsDto> overLimitMaxCntList  = new ArrayList<CheckCartGoodsDto>();

		ShoppingEnums.DeliveryType deliveryType = ShoppingEnums.DeliveryType.findByCode(addCartListRequestDto.getDeliveryType());
		if (deliveryType == null) {
			resultCode = ShoppingEnums.AddCartInfo.FAIL_VALIDATION;
		} else {
			for (AddCartListGoodsRequestDto addCartList : addCartListRequestDto.getAddGoodsList()) {

				GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder().ilGoodsId(addCartList.getIlGoodsId())
						.deviceInfo(dirInfo).isApp(isApp).isMember(isMember).isEmployee(isEmployee).build();

				BasicSelectGoodsVo goods = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);

				// 상품이 없을 경우
				if (goods == null) {
					return ApiResult.result(ShoppingEnums.AddCartInfo.NO_GOODS);
				} else {

					// 상품 재고 정보
					int stock = 0;
					// 묶음상품일 경우 분기처리
					if (goods.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {

						List<PackageGoodsListDto> goodsPackageList = goodsGoodsBiz.getPackagGoodsInfoList(
								goods.getIlGoodsId(), isMember, isEmployee, isDawnDelivery, arrivalScheduledDate, buyQty);

						RecalculationPackageDto recalculationPackage = goodsGoodsBiz.getRecalculationPackage(goods.getSaleStatus(), goodsPackageList);

						goods.setSaleStatus(recalculationPackage.getSaleStatus());
						stock = recalculationPackage.getStockQty();
					} else {
						stock = goods.getStockQty();
					}

					// 상품이 판매중이 아닐때
					if (!GoodsEnums.SaleStatus.ON_SALE.getCode().equals(goods.getSaleStatus())) {
						noGoodsStatusOnSalelist.add(new CheckCartGoodsDto(goods.getIlGoodsId(), goods.getGoodsName(), 0));
						resultCode = ShoppingEnums.AddCartInfo.NO_GOODS_STATUS_ON_SALE;
					}

					// 최소 구매수량 체크
					if(addCartList.getQty() < goods.getLimitMinimumCount()) {
						lackLimitMinCntList.add(new CheckCartGoodsDto(goods.getIlGoodsId(), goods.getGoodsName(), goods.getLimitMinimumCount()));
						resultCode = ShoppingEnums.AddCartInfo.LACK_LIMIT_MIN_CNT;
					}

					// 최대 구매수량 체크
					if ("Y".equals(goods.getLimitMaximumCountYn())) {
						int systemLimitMaxCnt = goods.getLimitMaximumCount();
						int orderGoodsBuyQty = orderOrderBiz.getOrderGoodsBuyQty(goods.getIlGoodsId(), urUserId);
						int limitMaxCnt = systemLimitMaxCnt - orderGoodsBuyQty;
						if (addCartList.getQty() > limitMaxCnt) {
							overLimitMaxCntList.add(new CheckCartGoodsDto(goods.getIlGoodsId(), goods.getGoodsName(), limitMaxCnt));
							resultCode = ShoppingEnums.AddCartInfo.OVER_LIMIT_MAX_CNT;
						}
					}

					// 상품 재고가 구매 수량보다 작을경우
					if (stock < addCartList.getQty()) {
						goodsLackStockList.add(new CheckCartGoodsDto(goods.getIlGoodsId(), goods.getGoodsName(), stock));
						resultCode = ShoppingEnums.AddCartInfo.GOODS_LACK_STOCK;
					}

					if (resultCode.equals(BaseEnums.Default.SUCCESS)) {
						// 장바구니 등록 요청 DTO 세팅
						AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
						addCartInfoRequestDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
						addCartInfoRequestDto.setPmAdExternalCd(CookieUtil.getCookie(request, PromotionConstants.COOKIE_AD_EXTERNAL_CODE_KEY));
						addCartInfoRequestDto.setPmAdInternalPageCd(addCartListRequestDto.getPmAdInternalPageCd());
						addCartInfoRequestDto.setPmAdInternalContentCd(addCartListRequestDto.getPmAdInternalContentCd());
						addCartInfoRequestDto.setIlGoodsId(addCartList.getIlGoodsId());
						addCartInfoRequestDto.setQty(addCartList.getQty());
						addCartInfoRequestDto.setBuyNowYn("N");
						if (isMember) {
							addCartInfoRequestDto.setUrUserId(new Long(urUserId));
						}
						addCartInfoRequestDto.setSaleType(goods.getSaleType());
						addCartInfoRequestDto.setGoodsDailyType(goods.getGoodsDailyType());
						addCartInfoRequestDto.setDeliveryType(deliveryType.getCode());

						// 장바구니 필수 옵션 체크
						if (!shoppingCartBiz.isValidationAddCartInfo(addCartInfoRequestDto)) {
							return ApiResult.result(ShoppingEnums.AddCartInfo.FAIL_VALIDATION);
						}

						// 장바구니 등록
						addGoodsList.add(addCartInfoRequestDto);
					}
				}
			}
		}

		if (resultCode.equals(BaseEnums.Default.SUCCESS)) {
			for (AddCartInfoRequestDto addCartInfoRequestDto : addGoodsList) {
				shoppingCartBiz.addCartInfo(addCartInfoRequestDto);
			}
			return ApiResult.success();
		} else {
			if(ShoppingEnums.AddCartInfo.NO_GOODS_STATUS_ON_SALE.getCode().equals(resultCode)) {
				return ApiResult.result(noGoodsStatusOnSalelist, resultCode);
			} else if (ShoppingEnums.AddCartInfo.GOODS_LACK_STOCK.getCode().equals(resultCode)) {
				return ApiResult.result(goodsLackStockList, resultCode);
			} else if (ShoppingEnums.AddCartInfo.OVER_LIMIT_MAX_CNT.getCode().equals(resultCode)) {
				return ApiResult.result(overLimitMaxCntList, resultCode);
			} else {
				return ApiResult.result(lackLimitMinCntList, resultCode);
			}
		}
	}

	/**
	 * 장바구니 삭제
	 *
	 * @param DelCartRequestDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> delCartAndAddGoods(DelCartRequestDto delCartRequestDto) throws Exception {
		shoppingCartBiz.delCartAndAddGoods(delCartRequestDto);
		return ApiResult.success();
	}

	/**
	 * 장바구니 추가 구성상품 삭제
	 *
	 * @param DelCartAddGoodsRequestDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> delCartAddGoods(DelCartAddGoodsRequestDto delCartAddGoodsRequestDto) throws Exception {
		shoppingCartBiz.delCartAddGoods(delCartAddGoodsRequestDto);
		return ApiResult.success();
	}

	/**
	 * 품절/판매중지 삭제
	 *
	 * @param
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> delCartSoldOutGoods() throws Exception {

		// 장바구니 정보 요청 DTO 생성
		GetCartDataRequestDto getCartDataRequestDto = new GetCartDataRequestDto();

		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		getCartDataRequestDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
		if (isMember) {
			getCartDataRequestDto.setUrUserId(new Long(urUserId));
		}
		getCartDataRequestDto.setDeviceInfo(DeviceUtil.getDirInfo());
		getCartDataRequestDto.setApp(DeviceUtil.isApp());
		getCartDataRequestDto.setMember(isMember);
		getCartDataRequestDto.setEmployee(isEmployee);
		getCartDataRequestDto.setReceiverZipCode(buyerVo.getReceiverZipCode());
		getCartDataRequestDto.setBuildingCode(buyerVo.getBuildingCode());
		getCartDataRequestDto.setUrErpEmployeeCode(buyerVo.getUrErpEmployeeCode());

		List<CartDeliveryDto> cartDataDto = shoppingCartBiz.getCartData(getCartDataRequestDto);

		List<CartGoodsDto> cartGoodsSaleStatusList = cartDataDto.stream().map(m -> m.getShipping())
				.flatMap(Collection::stream).map(m -> m.getGoods()).flatMap(Collection::stream)
				.collect(Collectors.toList());

		// 장바구니 상품 삭제 요청 DTO 생성
		DelCartRequestDto delCartReqeustDto = new DelCartRequestDto();
		List<Long> spCartIdList = new ArrayList<>();
		for (CartGoodsDto cartGoods : cartGoodsSaleStatusList) {
			if (!cartGoods.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode())) {
				spCartIdList.add(cartGoods.getSpCartId());
			}
		}

		if (!spCartIdList.isEmpty()) {
			delCartReqeustDto.setSpCartId(spCartIdList);
			shoppingCartBiz.delCartAndAddGoods(delCartReqeustDto);
		}
		return ApiResult.success();
	}

	/**
	 * 장바구니 페이지 정보
	 *
	 * @return GetCartPageInfoResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getCartPageInfo() throws Exception {

		// cartData 요청 파라미터 세팅
		GetCartDataRequestDto getCartDataRequestDto = new GetCartDataRequestDto();
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		getCartDataRequestDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
		if (isMember) {
			getCartDataRequestDto.setUrUserId(new Long(urUserId));
		}
		getCartDataRequestDto.setDeviceInfo(DeviceUtil.getDirInfo());
		getCartDataRequestDto.setApp(DeviceUtil.isApp());
		getCartDataRequestDto.setMember(isMember);
		getCartDataRequestDto.setEmployee(isEmployee);

		GetCartPageInfoResponseDto getCartPageInfoResponseDto = new GetCartPageInfoResponseDto();
		getCartPageInfoResponseDto.setAccessInformationType(userBuyerBiz.getCommonCode("ACCESS_INFORMATION"));
		getCartPageInfoResponseDto.setCartTypeSummary(shoppingCartBiz.getCartTypeSummary(getCartDataRequestDto));
		getCartPageInfoResponseDto.setRegularShipping(policyConfigBiz.getRegularShippingConfig());
		getCartPageInfoResponseDto.setCartMaintenancePeriod(policyConfigBiz.getConfigValue("OD_CART_MAINTENANCE_PERIOD"));
		getCartPageInfoResponseDto.setStoreDeliveryType(getStoreDelvieryTypeList());

		return ApiResult.success(getCartPageInfoResponseDto);
	}

	/**
	 * 장바구니 담기
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> addCartInfo(AddCartInfoRequestDto reqDto) throws Exception {

		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		String dirInfo = DeviceUtil.getDirInfo();
		boolean isApp = DeviceUtil.isApp();
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());

		GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder().ilGoodsId(reqDto.getIlGoodsId()).deviceInfo(dirInfo)
				.isApp(isApp).isMember(isMember).isEmployee(isEmployee).build();

		// 매장 배송일경우 매장 재고를 가지고 오기 위해 상품요청 정보에 매장 정보 추가
		if ("Y".equals(reqDto.getStoreDeliveryYn())) {
			goodsRequestDto.setStoreDelivery(true);
			GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();
			goodsRequestDto.setStoreDeliveryInfo(storeDeliveryBiz.getStoreShippingPossibilityStoreDeliveryAreaInfo(sessionShippingDto.getReceiverZipCode(), sessionShippingDto.getBuildingCode()));
		}

		BasicSelectGoodsVo goods = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);

		// 상품이 없을 경우
		if (goods == null) {
			return ApiResult.result(ShoppingEnums.AddCartInfo.NO_GOODS);
		} else {

			// 상품 재고 정보
			int stock = 0;
			// 묶음상품일 경우 분기처리
			if (goods.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {

				List<PackageGoodsListDto> goodsPackageList = goodsGoodsBiz.getPackagGoodsInfoList(goods.getIlGoodsId(),
						isMember, isEmployee, false, null, reqDto.getQty());

				RecalculationPackageDto recalculationPackage = goodsGoodsBiz.getRecalculationPackage(goods.getSaleStatus(), goodsPackageList);

				goods.setSaleStatus(recalculationPackage.getSaleStatus());
				stock = recalculationPackage.getStockQty();
			} else {
				stock = goods.getStockQty();
			}

			// 상품이 판매중이 아닐때
			if (!GoodsEnums.SaleStatus.ON_SALE.getCode().equals(goods.getSaleStatus())) {
				return ApiResult.result(ShoppingEnums.AddCartInfo.NO_GOODS_STATUS_ON_SALE);
			}

			// 최소 구매수량 체크
			if(reqDto.getQty() < goods.getLimitMinimumCount()) {
				return ApiResult.result(goods.getLimitMinimumCount(), ShoppingEnums.AddCartInfo.LACK_LIMIT_MIN_CNT);
			}

			// 최대 구매수량 체크
			if ("Y".equals(goods.getLimitMaximumCountYn())) {
				int systemLimitMaxCnt = goods.getLimitMaximumCount();
				int orderGoodsBuyQty = orderOrderBiz.getOrderGoodsBuyQty(goods.getIlGoodsId(), urUserId);
				int limitMaxCnt = systemLimitMaxCnt - orderGoodsBuyQty;
				if (reqDto.getQty() > limitMaxCnt) {
					return ApiResult.result(limitMaxCnt, ShoppingEnums.AddCartInfo.OVER_LIMIT_MAX_CNT);
				}
			}

			if (!"Y".equals(reqDto.getStoreDeliveryYn())
					&& GoodsEnums.SaleType.RESERVATION.getCode().equals(goods.getSaleType())) {
				// 매장 배송이 아닌경우 예약상품 정보 조회 후 스케줄 처리
				GoodsReserveOptionVo goodsReserveOptionVo = goodsGoodsBiz.getGoodsReserveOption(reqDto.getIlGoodsReserveOptionId());
				if (goodsReserveOptionVo == null) {
					return ApiResult.result(ShoppingEnums.AddCartInfo.NO_GOODS_STATUS_ON_SALE);
				} else {
					LocalDateTime currentDateTime = LocalDateTime.now();
					if (!(goodsReserveOptionVo.getReserveStartDate().compareTo(currentDateTime) <= 0
							&& currentDateTime
							.compareTo(goodsReserveOptionVo.getReserveEndDate()) <= 0)) {
						// 회차가 기간이 지나면 판매 중지 상태 변경
						return ApiResult.result(ShoppingEnums.AddCartInfo.NO_GOODS_STATUS_ON_SALE);
					} else if (!(goodsReserveOptionVo.getStockQty() > 0)) {
						// 회차의 수량이 없을때 일시 품절
						return ApiResult.result(ShoppingEnums.AddCartInfo.GOODS_LACK_STOCK);
					} else if (goodsReserveOptionVo.getStockQty() < reqDto.getQty()) {
						// 회차 수량보다 더 많이 담았을 경우
						return ApiResult.result(ShoppingEnums.AddCartInfo.GOODS_LACK_STOCK);
					}
				}
			} else {
				// 상품 재고가 구매 수량보다 작을경우
				if (stock < reqDto.getQty()) {
					return ApiResult.result(ShoppingEnums.AddCartInfo.GOODS_LACK_STOCK);
				}
			}

			List<AddCartInfoAdditionalGoodsRequestDto> additionalGoodsList = reqDto.getAdditionalGoodsList();
			if (additionalGoodsList != null && additionalGoodsList.size() > 0) {
				for (AddCartInfoAdditionalGoodsRequestDto additionalGoodsRequestDto : additionalGoodsList) {

					GoodsRequestDto addGoodsRequestDto = GoodsRequestDto.builder()
							.ilGoodsId(additionalGoodsRequestDto.getIlGoodsId()).deviceInfo(dirInfo).isApp(isApp)
							.isMember(isMember).isEmployee(isEmployee).build();

					BasicSelectGoodsVo addGoods = goodsGoodsBiz.getGoodsBasicInfo(addGoodsRequestDto);
					// 추가 구성 상품이 없을 경우
					if (addGoods == null) {
						return ApiResult.result(ShoppingEnums.AddCartInfo.NO_GOODS);
					} else {
						// 추가 상품 재고 정보
						ArrivalScheduledDateDto addGoodsArrivalScheduledDateDto = addGoods.getArrivalScheduledDateDto();

						// 추가 상품이 판매중이 아닐때
						if (!GoodsEnums.SaleStatus.ON_SALE.getCode().equals(addGoods.getSaleStatus())) {
							return ApiResult.result(ShoppingEnums.AddCartInfo.NO_ADD_GOODS_STATUS_ON_SALE);
						}

						// 추가 상품 재고가 구매 수량보다 작을경우
						if (addGoodsArrivalScheduledDateDto == null || addGoodsArrivalScheduledDateDto.getStock() < additionalGoodsRequestDto.getQty()) {
							return ApiResult.result(ShoppingEnums.AddCartInfo.ADD_GOODS_LACK_STOCK);
						}
					}
				}
			}

			reqDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
			reqDto.setPmAdExternalCd(CookieUtil.getCookie(request, PromotionConstants.COOKIE_AD_EXTERNAL_CODE_KEY));
			if (isMember) {
				reqDto.setUrUserId(new Long(urUserId));
			}
			reqDto.setSaleType(goods.getSaleType());
			reqDto.setGoodsDailyType(goods.getGoodsDailyType());

			DeliveryType deliveryTypeEnum = shoppingCartBiz.getDeliveryTypeBySaleType(goods.getSaleType());

			// 판매타입이 정기배송상품이고 일반구매로 장바구니 담은 경우 -> 배송타입을 일반배송으로 수정
			if ("Y".equals(reqDto.getStoreDeliveryYn()) && "Y".equals(goods.getSaleShopYn())) {
				// 매장 배송
				deliveryTypeEnum = ShoppingEnums.DeliveryType.SHOP_DELIVERY;
			} else if ("Y".equals(reqDto.getGoodsDailyBulkYn())
					&& goods.getSaleType().equals(GoodsEnums.SaleType.DAILY.getCode())) {
				ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(goods.getIlGoodsId(),
						buyerVo.getReceiverZipCode(), buyerVo.getBuildingCode());
				// 판매타입이 일일배송상품이고 배송유형이 일괄배송이면서 택배배송 가능 권역일경우 일반 배송으로 처리
				if (shippingPossibilityStoreDeliveryAreaInfo == null ||
						StoreApiDeliveryIntervalCode.PARCEL.getCommonCode().equals(shippingPossibilityStoreDeliveryAreaInfo.getStoreDeliveryIntervalType())) {
					deliveryTypeEnum = ShoppingEnums.DeliveryType.NORMAL;
				}
			} else if ("N".equals(reqDto.getRegularDeliveryYn())
					&& goods.getSaleType().equals(GoodsEnums.SaleType.REGULAR.getCode())) {
				deliveryTypeEnum = ShoppingEnums.DeliveryType.NORMAL;
			}  else if(GoodsEnums.GoodsType.RENTAL.getCode().equals(goods.getGoodsType())) {
				// 렌탈 상품은 랜탈 배송 타입으로 처리
				deliveryTypeEnum = ShoppingEnums.DeliveryType.RENTAL;
			}  else if(GoodsEnums.GoodsType.INCORPOREITY.getCode().equals(goods.getGoodsType())) {
				// 무형 상품은 무형 배송 타입으로 처리
				deliveryTypeEnum = ShoppingEnums.DeliveryType.INCORPOREITY;
			}
			reqDto.setDeliveryType(deliveryTypeEnum.getCode());

			// 장바구니 필수 옵션 체크
			if (!shoppingCartBiz.isValidationAddCartInfo(reqDto)) {
				return ApiResult.result(ShoppingEnums.AddCartInfo.FAIL_VALIDATION);
			}

			// 장바구니 일일배송상품 동일상품 담기 제외(HGRM-8625)
			if(GoodsEnums.SaleType.DAILY.getCode().equals(goods.getSaleType()) && shoppingCartBiz.isExistDailyGoodsCart(reqDto)){
				return ApiResult.result(ShoppingEnums.AddCartInfo.EXIST_DAILY_GOODS);
			}

			// 장바구니 등록
			Long spCartId = shoppingCartBiz.addCartInfo(reqDto);

			AddCartInfoResponseDto resDto = new AddCartInfoResponseDto();
			resDto.setSpCartId(spCartId);
			resDto.setCartType(ShoppingEnums.CartType.findByDeliveryCode(deliveryTypeEnum.getCode()).getCode());
			// 장바구니 등록된 정보 조회
			SpCartVo cartVo = shoppingCartBiz.getCart(spCartId);
			resDto.setFirstAdd(cartVo.getQty() == reqDto.getQty());
			GetCartDataRequestDto getCartDataRequestDto = new GetCartDataRequestDto();
			getCartDataRequestDto.setUrPcidCd(reqDto.getUrPcidCd());
			getCartDataRequestDto.setUrUserId(reqDto.getUrUserId());
			getCartDataRequestDto.setDeviceInfo(DeviceUtil.getDirInfo());
			getCartDataRequestDto.setApp(DeviceUtil.isApp());
			getCartDataRequestDto.setMember(isMember);
			getCartDataRequestDto.setEmployee(isEmployee);
			resDto.setCartCount(shoppingCartBiz.getCartCount(getCartDataRequestDto));
			return ApiResult.success(resDto);
		}
	}

	/**
	 * 간편 장바구니 추가
	 *
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> addSimpleCart(AddSimpleCartRequestDto simpleReqDto) throws Exception {
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		String dirInfo = DeviceUtil.getDirInfo();
		boolean isApp = DeviceUtil.isApp();
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());

		GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder().ilGoodsId(simpleReqDto.getIlGoodsId()).deviceInfo(dirInfo)
				.isApp(isApp).isMember(isMember).isEmployee(isEmployee).isDawnDelivery(false).build();

		BasicSelectGoodsVo goods = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);

		// 상품이 없을 경우
		if (goods == null) {
			return ApiResult.result(ShoppingEnums.AddSimpleCart.NO_GOODS);
		} else {
			// 일일상품 아닐때 or 렌탈 상품 or 회원이 일반+매장상품일때 or 녹즙 택배 배송일경우 or 무형상품
			if (GoodsEnums.GoodsType.DAILY.getCode().equals(goods.getGoodsType())
					|| GoodsEnums.GoodsType.RENTAL.getCode().equals(goods.getGoodsType())
					|| goods.isGreenjuiceParcel()
					|| (GoodsEnums.SaleType.RESERVATION.getCode().equals(goods.getSaleType()) && !DeliveryType.SHOP_DELIVERY.getCode().equals(simpleReqDto.getDeliveryType()))
					|| GoodsEnums.GoodsType.INCORPOREITY.getCode().equals(goods.getGoodsType())) {
				return ApiResult.result(ShoppingEnums.AddSimpleCart.EXIST_OPTION);
			} else {
				// 간편 장바구니 담기 기본 수량
				int qty = 1;

				// 최소 구매수량 설정시 최소 구매 수량으로 담기
				if (goods.getLimitMinimumCount() > 0) {
					qty = goods.getLimitMinimumCount();
				}

				// 최대 구매수량 체크는 상품 상세로 이동 - 삭제
//				if ("Y".equals(goods.getLimitMaximumCountYn())) {
//					return ApiResult.result(ShoppingEnums.AddSimpleCart.EXIST_OPTION);
//				}

				// 묶음상품일 경우 분기처리
				if (goods.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {

					List<PackageGoodsListDto> goodsPackageList = goodsGoodsBiz.getPackagGoodsInfoList(goods.getIlGoodsId(),
							isMember, isEmployee, false, null, qty);

					RecalculationPackageDto recalculationPackage = goodsGoodsBiz.getRecalculationPackage(goods.getSaleStatus(), goodsPackageList);

					goods.setSaleStatus(recalculationPackage.getSaleStatus());
				}

				// 상품이 판매중, 일시품절이 아닐때 (간편 장바구니 담기에서는 일시품절 장바구니 담을수 있음)
				if (!GoodsEnums.SaleStatus.ON_SALE.getCode().equals(goods.getSaleStatus())
						&& !GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode().equals(goods.getSaleStatus())
						&& !GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_MANAGER.getCode().equals(goods.getSaleStatus())) {
					return ApiResult.result(ShoppingEnums.AddCartInfo.NO_GOODS_STATUS_ON_SALE);
				}

				// 장바구니 추가 하기위한 set
				AddCartInfoRequestDto reqDto = new AddCartInfoRequestDto();
				reqDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
				reqDto.setPmAdExternalCd(CookieUtil.getCookie(request, PromotionConstants.COOKIE_AD_EXTERNAL_CODE_KEY));
				reqDto.setPmAdInternalPageCd(simpleReqDto.getPmAdInternalPageCd());
				reqDto.setPmAdInternalContentCd(simpleReqDto.getPmAdInternalContentCd());
				if (isMember) {
					reqDto.setUrUserId(new Long(urUserId));
				}
				DeliveryType deliveryTypeEnum = null;
				if (DeliveryType.SHOP_DELIVERY.getCode().equals(simpleReqDto.getDeliveryType())) {
					deliveryTypeEnum = ShoppingEnums.DeliveryType.SHOP_DELIVERY;
				} else {
					deliveryTypeEnum = shoppingCartBiz.getDeliveryTypeBySaleType(goods.getSaleType());
					// 판매타입이 정기배송인 상품인 경우, 장바구니 간편추가에서는 배송타입이 정기배송이 아닌 일반배송으로 추가
					if (deliveryTypeEnum.getCode().equals(ShoppingEnums.DeliveryType.REGULAR.getCode())) {
						deliveryTypeEnum = ShoppingEnums.DeliveryType.NORMAL;
					}
				}
				reqDto.setSaleType(goods.getSaleType());
				reqDto.setGoodsDailyType(goods.getGoodsDailyType());
				reqDto.setDeliveryType(deliveryTypeEnum.getCode());
				reqDto.setIlGoodsId(simpleReqDto.getIlGoodsId());
				reqDto.setBuyNowYn("N");
				reqDto.setQty(qty);

				// 장바구니 등록
				Long spCartId = shoppingCartBiz.addCartInfo(reqDto);

				// 장바구니 등록된 정보 조회
				AddSimpleCartResponseDto resDto = new AddSimpleCartResponseDto();
				SpCartVo cartVo = shoppingCartBiz.getCart(spCartId);
				resDto.setFirstAdd(cartVo.getQty() == qty);

				// 장바구니 정보 요청 파라미터 세팅
				GetCartDataRequestDto getCartDataRequestDto = new GetCartDataRequestDto();
				getCartDataRequestDto.setUrPcidCd(reqDto.getUrPcidCd());
				getCartDataRequestDto.setUrUserId(reqDto.getUrUserId());
				getCartDataRequestDto.setDeviceInfo(DeviceUtil.getDirInfo());
				getCartDataRequestDto.setApp(DeviceUtil.isApp());
				getCartDataRequestDto.setMember(isMember);
				getCartDataRequestDto.setEmployee(isEmployee);
				resDto.setCartCount(shoppingCartBiz.getCartCount(getCartDataRequestDto));
				resDto.setDeliveryType(deliveryTypeEnum.getCode());
				return ApiResult.success(resDto);
			}
		}
	}

	/**
	 * 장바구니 조회
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getCartInfo(GetCartDataRequestDto reqDto) throws Exception {
		GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();
		ShippingAddressPossibleDeliveryInfoDto deliveryInfo = null;
		CartNotiDto notiDto = null;
		List<CartDeliveryDto> cartDataDto = null;
		CartRegularShippingDto cartRegularShippingDto = null;
		ShopVo store = null;
		List<CartStoreShippingDto> storeShippingDtoList = null;
		CartStoreShippingFastestScheduleDto fastestSchedule = null;
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		reqDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
		if (isMember) {
			reqDto.setUrUserId(new Long(urUserId));
		}
		reqDto.setDeviceInfo(DeviceUtil.getDirInfo());
		reqDto.setApp(DeviceUtil.isApp());
		reqDto.setMember(isMember);
		reqDto.setEmployee(isEmployee);
		reqDto.setUrErpEmployeeCode(buyerVo.getUrErpEmployeeCode());
		// 정기 배송일때 기존 주문건이 있으면 주소지를 기존 주문건 유지해야함
		if (ShoppingEnums.CartType.REGULAR.getCode().equals(reqDto.getCartType())) {
			cartRegularShippingDto = orderRegularBiz.getRegularInfoByCart(reqDto.getUrUserId());
			if (cartRegularShippingDto.isAdditionalOrder()) {
				// 정기결제 추가 주문이면 다음 배송일 기준으로 재고 세팅처리 필요
				reqDto.setNextArrivalScheduledDate(cartRegularShippingDto.getNextArrivalScheduledDate());
				if ("Y".equals(reqDto.getBridgeYn())) {
					sessionShippingDto = orderRegularBiz.getRegularShippingZone(cartRegularShippingDto.getOdRegularReqId());
				}
			}
		}
		reqDto.setReceiverZipCode(sessionShippingDto.getReceiverZipCode());
		reqDto.setBuildingCode(sessionShippingDto.getBuildingCode());

		// 장바구니 조회
		if ("Y".equals(reqDto.getBridgeYn()) && reqDto.getSpCartId() == null) {
			cartDataDto = new ArrayList<>();
		} else {
			cartDataDto = shoppingCartBiz.getCartData(reqDto);
		}
		CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto);

		// 브릿지 페이지일때
		if ("Y".equals(reqDto.getBridgeYn())) {
			// 알림 관련 추직 추가
			notiDto = new CartNotiDto();
			if (ShoppingEnums.CartType.REGULAR.getCode().equals(reqDto.getCartType())) {
				if(cartRegularShippingDto.isAdditionalOrder()) {
					notiDto.setRegularShippingAdditionalOrder(true);
				} else {
					notiDto.setRegularShippingAdditionalOrder(false);
					// 기존 주문건이 없으면 최초 배송예정일을 구해야함
					List<LocalDate> choiceArrivalScheduledDateList = shoppingCartBiz
							.getRegularShippingArrivalScheduledDateList(cartDataDto);
					cartRegularShippingDto.setChoiceArrivalScheduledDateList(choiceArrivalScheduledDateList);
					if (choiceArrivalScheduledDateList != null && choiceArrivalScheduledDateList.size() > 0) {
						cartRegularShippingDto.setFirstArrivalScheduledDate(choiceArrivalScheduledDateList.get(0));
					}
				}
			}
		}

		// 배송지 주소 아이콘 처리
		if (sessionShippingDto != null && sessionShippingDto.getReceiverZipCode() != null
				&& sessionShippingDto.getBuildingCode() != null) {
			deliveryInfo = storeDeliveryBiz.getShippingAddressPossibleDeliveryInfo(
					sessionShippingDto.getReceiverZipCode(), sessionShippingDto.getBuildingCode());

			// 매장 배송일때
			if (CartType.SHOP.getCode().equals(reqDto.getCartType())) {
				ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryBiz.getStoreShippingPossibilityStoreDeliveryAreaInfo(sessionShippingDto.getReceiverZipCode(), sessionShippingDto.getBuildingCode());
				if(shippingPossibilityStoreDeliveryAreaInfo != null) {
					store = storeShopBiz.getShop(shippingPossibilityStoreDeliveryAreaInfo.getUrStoreId());
					storeShippingDtoList = shoppingCartBiz.getStoreShippingScheduleList(cartDataDto, shippingPossibilityStoreDeliveryAreaInfo);
					fastestSchedule = shoppingCartBiz.getStoreFastestSchedule(storeShippingDtoList);
				}
			}
		}

		// 결과 처리
		GetCartInfoResponseDto resDto = new GetCartInfoResponseDto();
		resDto.setDeliveryInfo(deliveryInfo);
		resDto.setShippingAddress(sessionShippingDto);
		resDto.setCart(cartDataDto);
		resDto.setCartSummary(cartSummaryDto);
		resDto.setRegularShipping(cartRegularShippingDto);
		resDto.setNoti(notiDto);
		resDto.setStore(store);
		resDto.setStoreShipping(storeShippingDtoList);
		resDto.setFastestSchedule(fastestSchedule);

		return ApiResult.success(resDto);
	}

	/**
	 * 장바구니 예정금액 조회
	 */
	@Override
	public ApiResult<?> getCartSummary(GetCartDataRequestDto reqDto) throws Exception {
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		reqDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
		if (isMember) {
			reqDto.setUrUserId(new Long(urUserId));
		}
		reqDto.setDeviceInfo(DeviceUtil.getDirInfo());
		reqDto.setApp(DeviceUtil.isApp());
		reqDto.setMember(isMember);
		reqDto.setEmployee(isEmployee);
		reqDto.setReceiverZipCode(buyerVo.getReceiverZipCode());
		reqDto.setBuildingCode(buyerVo.getBuildingCode());
		reqDto.setUrErpEmployeeCode(buyerVo.getUrErpEmployeeCode());

		List<CartDeliveryDto> cartDataDto = shoppingCartBiz.getCartData(reqDto);
		CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto);

		CartSummaryResponseDto resDto = new CartSummaryResponseDto();
		List<CartSummaryShippingDto> shipping = shoppingCartBiz.getCartShippingSummary(cartDataDto);
		resDto.setCartSummary(cartSummaryDto);
		resDto.setShipping(shipping);

		return ApiResult.success(resDto);
	}

	@Override
	public ApiResult<?> checkBuyPossibleCart(CheckBuyPossibleCartRequestDto reqDto) throws Exception {
		GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();
		List<CartDeliveryDto> cartDataDto = null;
		CartRegularShippingDto cartRegularShippingDto = null;
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		reqDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
		if (isMember) {
			reqDto.setUrUserId(new Long(urUserId));
		}
		reqDto.setDeviceInfo(DeviceUtil.getDirInfo());
		reqDto.setApp(DeviceUtil.isApp());
		reqDto.setMember(isMember);
		reqDto.setEmployee(isEmployee);
		reqDto.setUrErpEmployeeCode(buyerVo.getUrErpEmployeeCode());
		// 정기 배송일때 기존 주문건이 있으면 주소지를 기존 주문건 유지해야함
		if (ShoppingEnums.CartType.REGULAR.getCode().equals(reqDto.getCartType())) {
			cartRegularShippingDto = orderRegularBiz.getRegularInfoByCart(reqDto.getUrUserId());
			if (cartRegularShippingDto.isAdditionalOrder()) {
				// 정기결제 추가 주문이면 다음 배송일 기준으로 재고 세팅처리 필요
				reqDto.setNextArrivalScheduledDate(cartRegularShippingDto.getNextArrivalScheduledDate());
				if ("Y".equals(reqDto.getBridgeYn())) {
					sessionShippingDto = orderRegularBiz.getRegularShippingZone(cartRegularShippingDto.getOdRegularReqId());
				}
			}
		}
		reqDto.setReceiverZipCode(sessionShippingDto.getReceiverZipCode());
		reqDto.setBuildingCode(sessionShippingDto.getBuildingCode());

		// 장바구니 조회
		if ("Y".equals(reqDto.getBridgeYn()) && reqDto.getSpCartId() == null) {
			cartDataDto = new ArrayList<>();
		} else {
			cartDataDto = shoppingCartBiz.getCartData(reqDto);
		}

		CartStoreDto cartStoreDto = new CartStoreDto();
		if (ShoppingEnums.CartType.SHOP.getCode().equals(reqDto.getCartType())) {
			cartStoreDto.setStoreDeliveryType(reqDto.getStoreDeliveryType());
			cartStoreDto.setStoreArrivalScheduledDate(reqDto.getStoreArrivalScheduledDate());
			cartStoreDto.setUrStoreScheduleId(reqDto.getUrStoreScheduleId());
		}

		CheckCartResponseDto resDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, reqDto, cartDataDto, cartStoreDto);
		if(ApplyPayment.SUCCESS.getCode().equals(resDto.getResult().getCode())) {
			// 임직원이고 임직원 구매일때 임직원 초과 안내
			CartNotiDto notiDto = new CartNotiDto();
			if (reqDto.isEmployee() && "Y".equals(reqDto.getEmployeeYn())) {
				notiDto.setEmployeeDiscountExceedingLimit(shoppingCartBiz.isEmployeeDiscountExceedingLimit(cartDataDto));
			} else {
				notiDto.setEmployeeDiscountExceedingLimit(false);
			}
			resDto.setNoti(notiDto);
			return ApiResult.success(resDto);
		} else {
			return ApiResult.result(resDto, resDto.getResult());
		}
	}

	/**
	 * 결제 요청
	 */
	@Override
	public ApiResult<?> applyPayment(ApplyPaymentRequestDto reqDto) throws Exception {

		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		Long longUrUserId = null;
		if (!"".equals(urUserId)) {
			longUrUserId = new Long(urUserId);
		}
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		GetSessionUserCertificationResponseDto nonMemberCertificationDto = null;
		RegularPaymentKeyVo regularPaymentVo = null;
		List<CartGiftDto> gift = new ArrayList<>();

		// 접근 디바이스 타입 정보
		DeviceType deviceType = GoodsEnums.DeviceType.PC;
		AgentType agentType = SystemEnums.AgentType.PC;
		if (DeviceUtil.isApp()) {
			deviceType = GoodsEnums.DeviceType.APP;
			agentType = SystemEnums.AgentType.APP;
		} else if (DeviceUtil.getDirInfo().equalsIgnoreCase("mobile")) {
			deviceType = GoodsEnums.DeviceType.MOBILE;
			agentType = SystemEnums.AgentType.MOBILE;
		}

		GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();
		// 정기 배송일때 기존 주문건이 있으면 주소지를 기존 주문건 유지해야함
		if (ShoppingEnums.CartType.REGULAR.getCode().equals(reqDto.getCartType())) {
			CartRegularShippingDto cartRegularShippingDto = orderRegularBiz.getRegularInfoByCart(longUrUserId);
			if (cartRegularShippingDto.isAdditionalOrder()) {
				sessionShippingDto = orderRegularBiz.getRegularShippingZone(cartRegularShippingDto.getOdRegularReqId());
			}
		}

		if(StringUtil.isEmpty(sessionShippingDto.getReceiverZipCode()) && StringUtil.isEmpty(sessionShippingDto.getBuildingCode())) {
			// 세션 없음
			return ApiResult.result(ApplyPayment.EMPTY_SESSION);
		} else if (StringUtil.isEmpty(sessionShippingDto.getReceiverZipCode()) || StringUtil.isEmpty(sessionShippingDto.getBuildingCode())) {
			// 배송지 정보 없음
			return ApiResult.result(ApplyPayment.REQUIRED_SHIPPING_ZONE);
		}

		// 사용 적립금 검증
		if (isMember) {
			// 사용가능 적립금 조회
			int availablePoint = promotionPointBiz.getPointUsable(longUrUserId);
			if (availablePoint < reqDto.getUsePoint()) {
				return ApiResult.result(ApplyPayment.ORVER_AVAILABLE_POINT);
			}

			// 정기 결제시
			if (CartType.REGULAR.getCode().equals(reqDto.getCartType())) {
				// 정기배송 결제 정보 등록 체크
				regularPaymentVo = orderRegularBiz.getRegularPaymentKey(longUrUserId);
				if ("".equals(StringUtil.nvl(regularPaymentVo.getBatchKey()))) {
					return ApiResult.result(ApplyPayment.REQUIRED_REGULAR_PAYMENT);
				}
				// 정기배송 정보 체크
				if ("".equals(StringUtil.nvl(reqDto.getRegularShippingCycleTermType()))
						|| "".equals(StringUtil.nvl(reqDto.getRegularShippingCycleType()))
						|| "".equals(StringUtil.nvl(reqDto.getRegularShippingArrivalScheduledDate()))) {
					return ApiResult.result(ApplyPayment.REQUIRED_REGULAR_INFO);
				}
			}

			// 쿠폰 검증
			List<Long> couponPmCouponIssueIds = new ArrayList<Long>();
			if (reqDto.getUseGoodsCoupon() != null && reqDto.getUseGoodsCoupon().size() > 0) {
				for (UseGoodsCouponDto useGoodsCouponDto : reqDto.getUseGoodsCoupon()) {
					couponPmCouponIssueIds.add(useGoodsCouponDto.getPmCouponIssueId());
				}
			}
			if (reqDto.getUseShippingCoupon() != null && reqDto.getUseShippingCoupon().size() > 0) {
				for (UseShippingCouponDto useShippingCouponDto : reqDto.getUseShippingCoupon()) {
					couponPmCouponIssueIds.add(useShippingCouponDto.getPmCouponIssueId());
				}
			}
			if (reqDto.getUseCartPmCouponIssueId() != null) {
				couponPmCouponIssueIds.add(reqDto.getUseCartPmCouponIssueId());
			}
			if (couponPmCouponIssueIds.size() > 0) {
				Set<Long> uniqueCouponPmCouponIssueIds = new HashSet<Long>(couponPmCouponIssueIds);
				if (uniqueCouponPmCouponIssueIds.size() != couponPmCouponIssueIds.size()) {
					return ApiResult.result(ApplyPayment.USE_DUPLICATE_COUPON);
				}

				GetCouponPageInfoRequestDto useableCouponReqDto = new GetCouponPageInfoRequestDto();
				useableCouponReqDto.setCartType(reqDto.getCartType());
				useableCouponReqDto.setSpCartId(reqDto.getSpCartId());
				useableCouponReqDto.setEmployeeYn(reqDto.getEmployeeYn());
				useableCouponReqDto.setArrivalScheduled(reqDto.getArrivalScheduled());
				ApiResult<GetCouponPageInfoResponseDto> useableCouponResDto = (ApiResult<GetCouponPageInfoResponseDto>) getCouponPageInfo(useableCouponReqDto);
				if(BaseEnums.Default.SUCCESS.getCode().equals(useableCouponResDto.getCode())) {
					List<Long> useablePmCouponIssueIds = useableCouponResDto.getData().getPmCouponIssueIds();
					for (Long couponPmCouponIssueId : couponPmCouponIssueIds) {
						if (!useablePmCouponIssueIds.contains(couponPmCouponIssueId)) {
							return ApiResult.result(ApplyPayment.FAIL_VALIDATION_COUPON);
						}
					}
				} else {
					return ApiResult.result(ApplyPayment.FAIL_VALIDATION_COUPON);
				}
			}
			// 회원이고 렌탈일때 이름 , 핸드폰 번호 필수 체크
			if (ShoppingEnums.CartType.RENTAL.getCode().equals(reqDto.getCartType())) {
				// 구매자명 체크
				if ("".equals(StringUtil.nvl(reqDto.getBuyerName()))) {
					return ApiResult.result(ApplyPayment.REQUIRED_RENTAL_MEMBER_NAME);
				}
				// 핸드폰 번호 체크
				if ("".equals(StringUtil.nvl(reqDto.getBuyerMobile()))) {
					return ApiResult.result(ApplyPayment.REQUIRED_RENTAL_MEMBER_MOBILE);
				}
				// 이메일 체크
				if ("".equals(StringUtil.nvl(reqDto.getBuyerMail()))) {
					return ApiResult.result(ApplyPayment.REQUIRED_RENTAL_MEMBER_EMAIL);
				}
			}
			// 무형일 경우
			else if (ShoppingEnums.CartType.INCORPOREITY.getCode().equals(reqDto.getCartType())) {
				// 받는분 체크
				if ("".equals(StringUtil.nvl(reqDto.getReceiveName()))) {
					return ApiResult.result(ApplyPayment.REQUIRED_INCORPOREITY_RECEVE_NAME);
				}
				// 받는사람 휴대전화 체크
				if ("".equals(StringUtil.nvl(reqDto.getReceiveMobile()))) {
					return ApiResult.result(ApplyPayment.REQUIRED_INCORPOREITY_RECEVE_MOBILE);
				}
			}
		} else {
			// 적립금 사용 체크
			if (reqDto.getUsePoint() > 0) {
				return ApiResult.result(ApplyPayment.NON_MEMBER_NOT_POINT);
			}

			// 본인인증 체크
			nonMemberCertificationDto = userCertificationBiz.getSessionUserCertification();
			if ("".equals(StringUtil.nvl(nonMemberCertificationDto.getCi()))) {
				return ApiResult.result(ApplyPayment.REQUIRED_NON_MEMBER_CERTIFICATION);
			}

			// 만 14세 체크
			if (!userJoinBiz.isCheckUnderAge14(nonMemberCertificationDto.getBirthday())) {
				return ApiResult.result(ApplyPayment.NON_MEMBER_UNDER_14_AGE_NOT_ALLOW);
			}

			// 이메일 정보 체크
			if ("".equals(StringUtil.nvl(reqDto.getBuyerMail()))) {
				return ApiResult.result(ApplyPayment.REQUIRED_NON_MEMBER_EMAIL);
			}

			// 정기 결제 체크
			if (CartType.REGULAR.getCode().equals(reqDto.getCartType())) {
				return ApiResult.result(ApplyPayment.NON_MEMBER_NOT_REGULAR);
			}
		}

		// 가상계좌 일때
		if (PaymentType.VIRTUAL_BANK.getCode().equals(reqDto.getPsPayCd())) {
			if ("".equals(StringUtil.nvl(reqDto.getBankCode())) || "".equals(StringUtil.nvl(reqDto.getAccountNumber()))
					|| "".equals(StringUtil.nvl(reqDto.getHolderName()))) {
				return ApiResult.result(ApplyPayment.REQUIRED_REFUND_ACCOUNT);
			}
		}

		// 매장픽업/배송 정보 체크
		if (ShoppingEnums.CartType.SHOP.getCode().equals(reqDto.getCartType())) {
			// 매장배송 배송유형 정보 체크
			if ("".equals(StringUtil.nvl(reqDto.getStoreDeliveryType()))) {
				return ApiResult.result(ApplyPayment.REQUIRED_STORE_DELIVERY_TYPE);
			}
			// 매장배송 도착일 정보 없음
			if ("".equals(StringUtil.nvl(reqDto.getStoreArrivalScheduledDate()))) {
				return ApiResult.result(ApplyPayment.REQUIRED_STORE_SCHEDULE_DATE);
			}
			// 매장배송 회차 정보 없음
			if ("".equals(StringUtil.nvl(reqDto.getUrStoreScheduleId()))) {
				return ApiResult.result(ApplyPayment.REQUIRED_STORE_SCHEDULE_ID);
			}
		}

		// 장바구니 데이터 조회
		GetCartDataRequestDto cartDataRequestDto = new GetCartDataRequestDto();
		cartDataRequestDto.setCartType(reqDto.getCartType());
		cartDataRequestDto.setSpCartId(reqDto.getSpCartId());
		cartDataRequestDto.setEmployeeYn(reqDto.getEmployeeYn());
		cartDataRequestDto.setUseGoodsCoupon(reqDto.getUseGoodsCoupon());
		cartDataRequestDto.setUseShippingCoupon(reqDto.getUseShippingCoupon());

		cartDataRequestDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
		if (isMember) {
			cartDataRequestDto.setUrUserId(new Long(urUserId));
		}
		cartDataRequestDto.setDeviceInfo(DeviceUtil.getDirInfo());
		cartDataRequestDto.setApp(DeviceUtil.isApp());
		cartDataRequestDto.setMember(isMember);
		cartDataRequestDto.setEmployee(isEmployee);
		cartDataRequestDto.setReceiverZipCode(sessionShippingDto.getReceiverZipCode());
		cartDataRequestDto.setBuildingCode(sessionShippingDto.getBuildingCode());
		cartDataRequestDto.setUrErpEmployeeCode(buyerVo.getUrErpEmployeeCode());
		cartDataRequestDto.setArrivalScheduled(reqDto.getArrivalScheduled());
		cartDataRequestDto.setArrivalGoods(reqDto.getArrivalGoods());
		cartDataRequestDto.setPresentYn(reqDto.getPresentYn());

		// 매장 관련 데이터
		if (ShoppingEnums.CartType.SHOP.getCode().equals(reqDto.getCartType())) {
			cartDataRequestDto.setStoreDeliveryType(reqDto.getStoreDeliveryType());
			cartDataRequestDto.setNextArrivalScheduledDate(reqDto.getStoreArrivalScheduledDate());
		}

		// 현 시점에 증정품 포함안된 기본 장바구니 정보 조회 후 증정품 상품 변동 및 제외 로직을 타고 증정품을 추가 로직을 탄 후 다시 증정품 포함된 장바구니 정보를 조회한다. (`증정품 Gift shoppingCartBiz.getCartData 재조회` 검색)
		List<CartDeliveryDto> cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);

		// 증정품
		List<CartGiftDto> cartGift = new ArrayList<CartGiftDto>();
		// 증정품 유효성 검사
		if (reqDto.getGift() != null && !reqDto.getGift().isEmpty()) {
			String userStatus = userBuyerMallService.getUserStatus(buyerVo).getCode();
			// 증정 기획전 조회
			List<GiftListResponseDto> giftResultList = shoppingCartBiz.giftGetValidation(cartDataDto, userStatus,
					buyerVo.getUrGroupId(), isMember, isEmployee, reqDto.getPresentYn());

			// 카트에서 넘어온 증정품정보와 증정품기획전 정보 일치하는지 확인
			for (CartGiftDto cartGiftDto : reqDto.getGift()) {
				// responseDto에 set하기 위한 cartGiftDto
				CartGiftDto setCartGiftDto = new CartGiftDto();

				// 1. 기획전 PK 비교
				if (giftResultList.stream().map(m -> m.getEvExhibitId()).collect(Collectors.toList()).stream()
						.noneMatch(a -> a.equals(cartGiftDto.getEvExhibitId()))) {
					return ApiResult.result(ApplyPayment.FAIL_VALIDATION_GIFT);
				}

				// 2. 증정품 PK 비교
				for (GiftListResponseDto giftResDto : giftResultList) {
					if (cartGiftDto.getEvExhibitId().equals(giftResDto.getEvExhibitId())) {
						if (giftResDto.getGoods().stream().map(m -> m.getIlGoodsId())
								.noneMatch(a -> a.equals(cartGiftDto.getIlGoodsId()))) {
							return ApiResult.result(ApplyPayment.FAIL_VALIDATION_GIFT);
						} else {

							// 증정품
							for (GiftGoodsVo goodsVo : giftResDto.getGoods()) {
								if (cartGiftDto.getIlGoodsId().equals(goodsVo.getIlGoodsId())) {
									if(goodsVo.getStock() == 0) {
										setCartGiftDto.setEvExhibitId(cartGiftDto.getEvExhibitId());
										setCartGiftDto.setTitle(giftResDto.getTitle());
										setCartGiftDto.setIlGoodsId(cartGiftDto.getIlGoodsId());

										// 증정품 재고 부족 + 모든 증정품 재고 없음
										if (giftResDto.getGoods().stream().allMatch(a -> a.getStock() == 0)) {
											setCartGiftDto.setGiftState(GiftState.X.getCode());
											gift.add(setCartGiftDto);
											break;
										}

										// 증정품 재고 부족 + 다른 증정품 재고 있음
										GiftGoodsVo otherGoodsVo = giftResDto.getGoods().stream().filter(a -> a.getStock() != 0
												&& !a.getIlGoodsId().equals(cartGiftDto.getIlGoodsId())).findAny().orElse(null);
										if (otherGoodsVo != null) {
											setCartGiftDto.setGiftState(GiftState.S.getCode());
											gift.add(setCartGiftDto);
											cartGiftDto.setIlGoodsId(otherGoodsVo.getIlGoodsId());
											cartGift.add(cartGiftDto);
											break;
										}
									} else {
										cartGift.add(cartGiftDto);
										break;
									}
								}
							}
						}
					}
				}
			}
		}

		// 증정품 Gift shoppingCartBiz.getCartData 재조회
		if (!cartGift.isEmpty()) {
			cartDataRequestDto.setGift(cartGift);
			cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);
		}

		// 장바구니 할인 정보 반영된 카트정보 get
		if (reqDto.getUseCartPmCouponIssueId() != null && reqDto.getUseCartPmCouponIssueId() > 0) {

			// cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
			List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);

			// List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
			List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);

			CouponDto cartCouponDto = promotionCouponBiz.getCartCouponApplicationListByUser(longUrUserId, cartGoodsList,
					deviceType, reqDto.getUseCartPmCouponIssueId());
			cartDataRequestDto.setUseCartCoupon(cartCouponDto);

			cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);
		}

		// 매장 정보
		CartStoreDto cartStoreDto = new CartStoreDto();
		if (ShoppingEnums.CartType.SHOP.getCode().equals(reqDto.getCartType())) {
			cartStoreDto.setStoreDeliveryType(reqDto.getStoreDeliveryType());
			cartStoreDto.setStoreArrivalScheduledDate(reqDto.getStoreArrivalScheduledDate());
			cartStoreDto.setUrStoreScheduleId(reqDto.getUrStoreScheduleId());
		}

		CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(sessionShippingDto, cartDataRequestDto, cartDataDto, cartStoreDto);
		// 재고 부족은 아래 배송일자 변경 프로세스 에서 다시 한번 체크 함
		if (!ApplyPayment.SUCCESS.getCode().equals(checkCartResponseDto.getResult().getCode())
				&& !ApplyPayment.GOODS_LACK_STOCK.getCode().equals(checkCartResponseDto.getResult().getCode())) {
			return ApiResult.result(checkCartResponseDto.getResult());
		}

		if (!ApplyPayment.SUCCESS.getCode().equals(checkCartResponseDto.getResult().getCode())) {
			// 매장 배송/픽업은 재고 부족시 중지
			if(ShoppingEnums.CartType.SHOP.getCode().equals(reqDto.getCartType())) {
				return ApiResult.result(checkCartResponseDto.getResult());
			} else {
				// 매장 배송/픽업 외 주문은 재고 부족은 아래 배송일자 변경 프로세스 에서 다시 한번 체크 함
				if(!ApplyPayment.GOODS_LACK_STOCK.getCode().equals(checkCartResponseDto.getResult().getCode())) {
					return ApiResult.result(checkCartResponseDto.getResult());
				}
			}
		}

		// 상품 판매중 또는 품절 관련 체크 하여 일자 변경 가능 및 불가 확인 후 응답 하기위한 로직
		List<GoodsLackStockNotiDto> goodsLackStockNotiDtoList = new ArrayList<GoodsLackStockNotiDto>();
		// 장바구니 데이터에서 배송정책별 데이터 List 추출
		List<CartShippingDto> cartShippingDtoList = shoppingCartBiz.getCartShippingList(cartDataDto);
		for (int i = 0; i < cartShippingDtoList.size(); i++) {
			boolean check = false;
			for (CartGoodsDto cartGoodsDto : cartShippingDtoList.get(i).getGoods()) {
				// 증정품 제외하고 체크
				if (!GoodsEnums.GoodsType.GIFT.getCode().equals(cartGoodsDto.getGoodsType()) && !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(cartGoodsDto.getGoodsType())) {
					if (GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode().equals(cartGoodsDto.getSaleStatus())
							&& GoodsEnums.SaleStatus.ON_SALE.getCode().equals(cartGoodsDto.getSystemSaleStatus())) {
						// 프론트에서 보여지는 상태가 시스템 일시 품절(재고가 0이면 시스템품절로 처리함) 이지만 실제 상태는 판매중일때 재고체크 다시 해야함
						check = true;
					} else {
						// 재고가 있지만 구매 수량보다 적을경우
						if (cartGoodsDto.getStockQty() - cartGoodsDto.getQty() < 0) {
							check = true;
						}
					}
				}
			}

			// 재고 체크 다시 해야 하면 goodsLackStockNotiDtoList 에 데이터 추가 (이후 goodsLackStockNotiDtoList 있는 배송정책 그룹만 로직을 타기위해 add)
			if(check == true) {
				GoodsLackStockNotiDto goodsLackStockNotiDto = new GoodsLackStockNotiDto();
				goodsLackStockNotiDto.setCartShippingListIndex(i);
				goodsLackStockNotiDto.setDawnDeliveryYn(reqDto.getArrivalScheduled().get(i).getDawnDeliveryYn());
				goodsLackStockNotiDto.setPrevArrivalScheduledDate(cartShippingDtoList.get(i).getArrivalScheduledDate());
				goodsLackStockNotiDto.setGoodsNameList(cartShippingDtoList.get(i).getGoods().stream().map(CartGoodsDto::getGoodsName).collect(Collectors.toList()));
				goodsLackStockNotiDtoList.add(goodsLackStockNotiDto);
			}
		}

		// 도착 예정 일자를 다시 체크해야 하는 경우
		if (!goodsLackStockNotiDtoList.isEmpty()) {
			// 재고 선택 일자를 변경하기 위해 브릿지 페이지에서 재고 선택 일자정보 조회기 위한 장바구니 데이터 조회
			cartDataRequestDto.setArrivalScheduled(null);
			cartDataRequestDto.setBridgeYn("Y");
			cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);
			// 장바구니 데이터에서 배송정책별 데이터 List 추출
			cartShippingDtoList = shoppingCartBiz.getCartShippingList(cartDataDto);
			// 체크해야하는 배송정책그룹 goodsLackStockNotiDtoList 별로 반복문 실행
			for (GoodsLackStockNotiDto goodsLackStockNotiDto : goodsLackStockNotiDtoList) {
				// 체크해야하는 배송정책 그룹 정보를 이용해 장바구니 배송정책dto 조회
				CartShippingDto cartShippingDto = cartShippingDtoList.get(goodsLackStockNotiDto.getCartShippingListIndex());
				List<LocalDate> choiceArrivalScheduledDateList = null;
				// 새벽배송 여부에 따른 배송 가능한 일자 조회
				if ("Y".equals(goodsLackStockNotiDto.getDawnDeliveryYn())) {
					choiceArrivalScheduledDateList = cartShippingDto.getDawnChoiceArrivalScheduledDateList();
				} else {
					choiceArrivalScheduledDateList = cartShippingDto.getChoiceArrivalScheduledDateList();
				}
				if (choiceArrivalScheduledDateList != null && !choiceArrivalScheduledDateList.isEmpty()) {
					// 배송가능한 일자 데이터가 있을경우 고객이 선택한 일자보다 가장빠른 일자로 조회 (고객이 선택한 일자가 19일 일 경우 배송 가능한 일자가 17,18,19,22,23,24,25 일경우 22일자 데이터 조회)
					LocalDate afterArrivalScheduledDate = goodsGoodsBiz.getNextArrivalScheduledDate(choiceArrivalScheduledDateList, goodsLackStockNotiDto.getPrevArrivalScheduledDate());
					if(afterArrivalScheduledDate == null) {
						return ApiResult.result(ApplyPayment.GOODS_LACK_STOCK);
					} else {
						goodsLackStockNotiDto.setAfterArrivalScheduledDate(afterArrivalScheduledDate);
						//고객이 요청한 도착 예정일자를 변경
						reqDto.getArrivalScheduled().get(goodsLackStockNotiDto.getCartShippingListIndex())
								.setArrivalScheduledDate(afterArrivalScheduledDate);
					}
				} else {
					return ApiResult.result(ApplyPayment.GOODS_LACK_STOCK);
				}
			}
			// 다시 변경된 기준으로 조회
			cartDataRequestDto.setArrivalScheduled(reqDto.getArrivalScheduled());
			cartDataRequestDto.setBridgeYn("N");
			cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);
		}

		CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, reqDto.getUsePoint());

		// 무료 결제 체크
		if (OrderEnums.PaymentType.FREE.getCode().equals(reqDto.getPsPayCd())
				&& cartSummaryDto.getPaymentPrice() != 0) {
			return ApiResult.result(ApplyPayment.FAIL_FREE);
		}

		PaymentType paymentType = PaymentType.findByCode(reqDto.getPsPayCd());

		if (isMember) {
			if ("Y".equals(reqDto.getSavePaymentMethodYn())) {
				// 선택한 결제수단을 다음에도 사용
				userBuyerBiz.putUserPaymentInfo(longUrUserId, paymentType.getCode(), reqDto.getCardCode());
			}
			// 가상계좌일때 환불계좌 정보 처리
			if (PaymentType.VIRTUAL_BANK.getCode().equals(reqDto.getPsPayCd())) {
				CommonSaveRefundBankRequestDto saveRefundBankRequestDto = new CommonSaveRefundBankRequestDto();
				saveRefundBankRequestDto.setBankCode(reqDto.getBankCode());
				saveRefundBankRequestDto.setAccountNumber(reqDto.getAccountNumber());
				saveRefundBankRequestDto.setHolderName(reqDto.getHolderName());
				saveRefundBankRequestDto.setUrUserId(urUserId);
				CommonGetRefundBankRequestDto getRefundBankDto = new CommonGetRefundBankRequestDto();
				getRefundBankDto.setUrUserId(urUserId);
				CommonGetRefundBankResultVo refundBankVo = userBuyerBiz.getRefundBank(getRefundBankDto);
				if (refundBankVo != null) {
//					saveRefundBankRequestDto.setUrRefundBankId(refundBankVo.getUrRefundBankId());
//					userBuyerBiz.putRefundBank(saveRefundBankRequestDto);
				} else {
					userBuyerBiz.addRefundBank(saveRefundBankRequestDto);
				}
			}
		}

		// 임시 주문 생성 등록
		CartBuyerDto cartBuyerDto = new CartBuyerDto();
		if (isMember) {
			cartBuyerDto.setUrUserId(longUrUserId);
			cartBuyerDto.setUrGroupId(buyerVo.getUrGroupId());
			cartBuyerDto.setUrEmployeeCd(buyerVo.getUrErpEmployeeCode());
			cartBuyerDto.setGuestCi("");
			// 회원이 렌탈 신청시에는 세션정보가 아닌 요청정보로 처리
			if (ShoppingEnums.CartType.RENTAL.getCode().equals(reqDto.getCartType())) {
				cartBuyerDto.setBuyerName(reqDto.getBuyerName());
				cartBuyerDto.setBuyerMobile(reqDto.getBuyerMobile());
				cartBuyerDto.setBuyerEmail(reqDto.getBuyerMail());
			} else {
				cartBuyerDto.setBuyerName(buyerVo.getUserName());
				cartBuyerDto.setBuyerMobile(buyerVo.getUserMobile());
				cartBuyerDto.setBuyerEmail(buyerVo.getUserEmail());
			}
			if (isEmployee) {
				if("Y".equals(reqDto.getEmployeeYn())) {
					cartBuyerDto.setBuyerType(BuyerType.EMPLOYEE);
				} else {
					cartBuyerDto.setBuyerType(BuyerType.EMPLOYEE_BASIC);
				}
			} else {
				cartBuyerDto.setBuyerType(BuyerType.USER);
			}
		} else {
			cartBuyerDto.setUrUserId(0L);
			cartBuyerDto.setUrGroupId(0L);
			cartBuyerDto.setUrEmployeeCd("");
			cartBuyerDto.setGuestCi(nonMemberCertificationDto.getCi());
			cartBuyerDto.setBuyerName(nonMemberCertificationDto.getUserName());
			cartBuyerDto.setBuyerMobile(nonMemberCertificationDto.getMobile());
			cartBuyerDto.setBuyerEmail(reqDto.getBuyerMail());
			cartBuyerDto.setBuyerType(BuyerType.GUEST);
		}
		cartBuyerDto.setPaymentType(paymentType);
		cartBuyerDto.setAgentType(agentType);
		cartBuyerDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
		//환불 계좌 정보
		cartBuyerDto.setBankCode(reqDto.getBankCode());
		cartBuyerDto.setAccountNumber(reqDto.getAccountNumber());
		cartBuyerDto.setHolderName(reqDto.getHolderName());
		// 선물하기 정보
		cartBuyerDto.setPresentYn(reqDto.getPresentYn());
		cartBuyerDto.setReceiveName(reqDto.getReceiveName());
		cartBuyerDto.setReceiveMobile(reqDto.getReceiveMobile());
		cartBuyerDto.setPresentCardType(reqDto.getPresentCardType());
		cartBuyerDto.setPresentCardMessage(reqDto.getPresentCardMessage());

		cartBuyerDto.setReceiveName(reqDto.getReceiveName());
		cartBuyerDto.setReceiveMobile(reqDto.getReceiveMobile());

		CartRegularDto cartRegularDto = new CartRegularDto();
		cartRegularDto.setCycleType(reqDto.getRegularShippingCycleType());
		cartRegularDto.setCycleTermType(reqDto.getRegularShippingCycleTermType());
		cartRegularDto.setArrivalScheduledDate(reqDto.getRegularShippingArrivalScheduledDate());

		CreateOrderCartDto createOrderCartDto = new CreateOrderCartDto();
		createOrderCartDto.setBuyer(cartBuyerDto);
		createOrderCartDto.setShippingZone(sessionShippingDto);
		createOrderCartDto.setCartList(cartDataDto);
		createOrderCartDto.setCartSummary(cartSummaryDto);
		createOrderCartDto.setRegular(cartRegularDto);
		// 매장 정보
		if (ShoppingEnums.CartType.SHOP.getCode().equals(reqDto.getCartType())) {
			createOrderCartDto.setStore(cartStoreDto);
		}
		BasicDataResponseDto resDto = null;

		if(ShoppingEnums.CartType.REGULAR.getCode().equals(reqDto.getCartType())) {
			// 정기배송일때는 별도 로직 처리
			ApplyRegularResponseDto applyRegularResponseDto = orderRegularBiz.applyRegular(createOrderCartDto);
			if(applyRegularResponseDto.isResult()) {

				// 장바구니 삭제
				DelCartRequestDto delCartRequestDto = new DelCartRequestDto();
				delCartRequestDto.setSpCartId(reqDto.getSpCartId());
				shoppingCartBiz.delCartAndAddGoods(delCartRequestDto);

				resDto = new BasicDataResponseDto();
				resDto.setOdid(applyRegularResponseDto.getReqId());
				resDto.setPaymentPrice(cartSummaryDto.getPaymentPrice());
			} else {
				return ApiResult.result(ApplyPayment.FAIL_CREATE_ORDER);
			}
		} else {
			List<OrderBindDto> orderBindList = orderBindBiz.orderDataBind(createOrderCartDto);

			OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrder(orderBindList, "N");

			if (!OrderEnums.OrderRegistrationResult.SUCCESS.getCode()
					.equals(orderRegistrationResponseDto.getOrderRegistrationResult().getCode())) {
				return ApiResult.result(ApplyPayment.FAIL_CREATE_ORDER);
			}

			String odid = orderRegistrationResponseDto.getOdids().split(",")[0];

			if(OrderEnums.PaymentType.FREE.getCode().equals(reqDto.getPsPayCd())) {
				// 주문번호로 주문데이터 조회
				PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

				// 승인처리
				PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
				paymentData.setApprovalDate(LocalDateTime.now());
				PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

				if (!PgEnums.PgErrorType.SUCCESS.getCode().equals(putResDto.getResult().getCode())) {
					return ApiResult.result(ApplyPayment.FAIL_FREE_UPDATE);
				}

				// 장바구니 삭제
				DelCartRequestDto delCartRequestDto = new DelCartRequestDto();
				delCartRequestDto.setSpCartId(reqDto.getSpCartId());
				shoppingCartBiz.delCartAndAddGoods(delCartRequestDto);

				// 응답
				resDto = new BasicDataResponseDto();
				resDto.setOdid(odid);
				resDto.setPaymentPrice(cartSummaryDto.getPaymentPrice());
			} else {
				// PG
				PgAbstractService<?, ?> pgService = pgBiz.getService(paymentType, reqDto.getCardCode());
				String pgBankCode = pgBiz.getPgBankCode(pgService.getServiceType().getCode(), paymentType.getCode(), reqDto.getCardCode());

				// 기본 결제
				BasicDataRequestDto paymentRequestFormDataDto = new BasicDataRequestDto();
				paymentRequestFormDataDto.setOdid(odid);
				paymentRequestFormDataDto.setPaymentType(paymentType);
				paymentRequestFormDataDto.setPgBankCode(pgBankCode);
				paymentRequestFormDataDto.setQuota(reqDto.getInstallmentPeriod());
				paymentRequestFormDataDto.setGoodsName(cartSummaryDto.getGoodsSummaryName());
				paymentRequestFormDataDto.setPaymentPrice(cartSummaryDto.getPaymentPrice());
				paymentRequestFormDataDto.setTaxPaymentPrice(cartSummaryDto.getTaxPaymentPrice());
				paymentRequestFormDataDto.setTaxFreePaymentPrice(cartSummaryDto.getTaxFreePaymentPrice());
				paymentRequestFormDataDto.setBuyerName(cartBuyerDto.getBuyerName());
				paymentRequestFormDataDto.setBuyerEmail(cartBuyerDto.getBuyerEmail());
				paymentRequestFormDataDto.setBuyerMobile(cartBuyerDto.getBuyerMobile());
				paymentRequestFormDataDto
						.setLoginId(StringUtil.isNotEmpty(buyerVo.getLoginId()) ? buyerVo.getLoginId() : "비회원");
				if (PaymentType.VIRTUAL_BANK.getCode().equals(reqDto.getPsPayCd())) {
					paymentRequestFormDataDto.setVirtualAccountDateTime(pgBiz.getVirtualAccountDateTime());
				}
				EtcDataCartDto etcDataCartDto = new EtcDataCartDto();
				etcDataCartDto.setPaymentType(paymentType.getCode());
				etcDataCartDto.setOrderInputUrl(reqDto.getOrderInputUrl());
				etcDataCartDto.setCartType(reqDto.getCartType());
				etcDataCartDto.setOdid(odid);
				etcDataCartDto.setSpCartId(reqDto.getSpCartId());

				//PG 사 연동후 결제 성공했을때 링크프라이스 전송 정보를 받기 위한 전송값 세팅
				try {
					ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
					HttpServletRequest request = servletRequestAttributes.getRequest();
					String lpinfoCookie = CookieUtil.getCookie(request, PromotionConstants.COOKIE_AD_EXTERNAL_LP_CODE_KEY);
					etcDataCartDto.setLpinfo("");
					etcDataCartDto.setUserAgent("");
					etcDataCartDto.setIp("");
					etcDataCartDto.setDeviceType("");
					if (StringUtils.isNotEmpty(lpinfoCookie)) {
						etcDataCartDto.setLpinfo(lpinfoCookie);
						String userAgent = request.getHeader("user-agent");
						String strDeviceType = linkPriceService.getByUserAgent(userAgent);
						String customerIp = SystemUtil.getIpAddress(request);
						if (StringUtils.isNotEmpty(userAgent)) {
							etcDataCartDto.setUserAgent(userAgent);
						}
						if (StringUtils.isNotEmpty(customerIp)) {
							etcDataCartDto.setIp(customerIp);
						}
						if (StringUtils.isNotEmpty(strDeviceType)) {
							etcDataCartDto.setDeviceType(strDeviceType);
						}
					}
				} catch (Exception e) {
					log.warn("", e);
				}

				paymentRequestFormDataDto.setEtcData(pgService.toStringEtcData(etcDataCartDto));
//				paymentRequestFormDataDto.setCashReceipt(false);
//				paymentRequestFormDataDto.setCashReceiptEnum(OrderEnums.cashReceipt.DEDUCTION);
//				paymentRequestFormDataDto.setCashReceiptNumber("01038874023");

				resDto = pgService.getBasicData(paymentRequestFormDataDto);
			}
		}

		// 증정품 지급상태
		resDto.setGift(gift);
		resDto.setGoodsLackStockNotiYn(goodsLackStockNotiDtoList.isEmpty() ? "N" : "Y");
		resDto.setGoodsLackStockNoti(goodsLackStockNotiDtoList);

		return ApiResult.success(resDto);
	}

	/**
	 * 주문서 혜택 반영 조회
	 */
	@Override
	public ApiResult<?> getOrderSummary(GetOrderSummaryRequestDto reqDto) throws Exception {
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = StringUtil.nvl(buyerVo.getUrUserId());
		Long longUrUserId = null;
		if (!"".equals(urUserId)) {
			longUrUserId = new Long(urUserId);
		}
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		// 접근 디바이스 타입 정보
		DeviceType deviceType = GoodsEnums.DeviceType.PC;
		if (DeviceUtil.isApp()) {
			deviceType = GoodsEnums.DeviceType.APP;
		} else if (DeviceUtil.getDirInfo().equalsIgnoreCase("mobile")) {
			deviceType = GoodsEnums.DeviceType.MOBILE;
		}

		GetCartDataRequestDto cartDataRequestDto = new GetCartDataRequestDto();
		cartDataRequestDto.setCartType(reqDto.getCartType());
		cartDataRequestDto.setSpCartId(reqDto.getSpCartId());
		cartDataRequestDto.setEmployeeYn(reqDto.getEmployeeYn());
		cartDataRequestDto.setUseGoodsCoupon(reqDto.getUseGoodsCoupon());
		cartDataRequestDto.setUseShippingCoupon(reqDto.getUseShippingCoupon());

		cartDataRequestDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
		if (isMember) {
			cartDataRequestDto.setUrUserId(longUrUserId);
		}
		cartDataRequestDto.setDeviceInfo(DeviceUtil.getDirInfo());
		cartDataRequestDto.setApp(DeviceUtil.isApp());
		cartDataRequestDto.setMember(isMember);
		cartDataRequestDto.setEmployee(isEmployee);
		cartDataRequestDto.setReceiverZipCode(buyerVo.getReceiverZipCode());
		cartDataRequestDto.setBuildingCode(buyerVo.getBuildingCode());
		cartDataRequestDto.setUrErpEmployeeCode(buyerVo.getUrErpEmployeeCode());
		cartDataRequestDto.setPresentYn(reqDto.getPresentYn());

		// 매장 관련 데이터
		if (ShoppingEnums.CartType.SHOP.getCode().equals(reqDto.getCartType())) {
			cartDataRequestDto.setStoreDeliveryType(reqDto.getStoreDeliveryType());
			cartDataRequestDto.setNextArrivalScheduledDate(reqDto.getStoreArrivalScheduledDate());
		}

		// 장바구니 할인 정보 반영된 카트정보 get
		List<CartDeliveryDto> cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);
		if (reqDto.getUseCartPmCouponIssueId() != null && reqDto.getUseCartPmCouponIssueId() > 0) {

			// cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
			List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);

			// List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
			List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);

			CouponDto cartCouponDto = promotionCouponBiz.getCartCouponApplicationListByUser(longUrUserId, cartGoodsList,
					deviceType, reqDto.getUseCartPmCouponIssueId());
			cartDataRequestDto.setUseCartCoupon(cartCouponDto);

			cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);
		}

		CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, reqDto.getUsePoint());

		CartSummaryResponseDto resDto = new CartSummaryResponseDto();

		resDto.setCartSummary(cartSummaryDto);

		return ApiResult.success(resDto);
	}

	@Override
	public ApiResult<?> getGiftPageInfo(GetOrderPageInfoRequestDto getOrderPageInfoRequestDto) throws Exception {

		// 장바구니 정보 조회
		GetCartDataRequestDto cartDataRequestDto = new GetCartDataRequestDto();
		BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
		String urUserId = (buyerVo != null) ? StringUtil.nvl(buyerVo.getUrUserId()) : "";
		String dirInfo = DeviceUtil.getDirInfo();
		boolean isApp = DeviceUtil.isApp();
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = buyerVo != null && StringUtil.isNotEmpty(buyerVo.getUrErpEmployeeCode());
		cartDataRequestDto.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
		if (isMember) {
			cartDataRequestDto.setUrUserId(new Long(urUserId));
		}
		cartDataRequestDto.setDeviceInfo(dirInfo);
		cartDataRequestDto.setApp(isApp);
		cartDataRequestDto.setMember(isMember);
		cartDataRequestDto.setEmployee(isEmployee);
		cartDataRequestDto.setReceiverZipCode((buyerVo != null) ? buyerVo.getReceiverZipCode() : "");
		cartDataRequestDto.setBuildingCode((buyerVo != null) ? buyerVo.getBuildingCode() : "");
		cartDataRequestDto.setUrErpEmployeeCode((buyerVo != null) ? buyerVo.getUrErpEmployeeCode() : "");

		cartDataRequestDto.setCartType(getOrderPageInfoRequestDto.getCartType());
		cartDataRequestDto.setSpCartId(getOrderPageInfoRequestDto.getSpCartId());
		cartDataRequestDto.setEmployeeYn(getOrderPageInfoRequestDto.getEmployeeYn());
		cartDataRequestDto.setArrivalScheduled(getOrderPageInfoRequestDto.getArrivalScheduled());
		cartDataRequestDto.setPresentYn(getOrderPageInfoRequestDto.getPresentYn());

		// 매장 관련 데이터
		if (ShoppingEnums.CartType.SHOP.getCode().equals(getOrderPageInfoRequestDto.getCartType())) {
			cartDataRequestDto.setStoreDeliveryType(getOrderPageInfoRequestDto.getStoreDeliveryType());
			cartDataRequestDto.setNextArrivalScheduledDate(getOrderPageInfoRequestDto.getStoreArrivalScheduledDate());
		}

		List<CartDeliveryDto> cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);

		String userStatus = userBuyerMallService.getUserStatus(buyerVo).getCode();

		// 증정 기획전 조회 및 validation
		List<GiftListResponseDto> giftResultList = shoppingCartBiz.giftGetValidation(cartDataDto, userStatus, buyerVo.getUrGroupId(),
				isMember, isEmployee, getOrderPageInfoRequestDto.getPresentYn());

		return ApiResult.success(
				GiftPageInfoResponseDto.builder().isGift(giftResultList.size() > 0).gift(giftResultList).build());
	}

	public List<CodeInfoVo> getStoreDelvieryTypeList(){

		// 배송장소 코드 세팅
		List<CodeInfoVo> storeDeliveryType = new ArrayList<>();
		CodeInfoVo homeCodeInfo = new CodeInfoVo();
		homeCodeInfo.setCode("STORE_DELIVERY_TYPE.HOME");
		homeCodeInfo.setName(GoodsEnums.StoreDeliveryType.HOME.getDeliveryName());
		CodeInfoVo officeCodeInfo = new CodeInfoVo();
		officeCodeInfo.setCode("STORE_DELIVERY_TYPE.OFFICE");
		officeCodeInfo.setName(GoodsEnums.StoreDeliveryType.OFFICE.getDeliveryName());
		storeDeliveryType.add(homeCodeInfo);
		storeDeliveryType.add(officeCodeInfo);

		return storeDeliveryType;
	}

}
