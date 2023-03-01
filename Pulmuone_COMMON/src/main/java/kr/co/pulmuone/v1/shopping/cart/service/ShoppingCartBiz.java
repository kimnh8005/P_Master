package kr.co.pulmuone.v1.shopping.cart.service;

import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.DeliveryType;
import kr.co.pulmuone.v1.goods.goods.dto.RecalculationPackageDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.GiftListResponseDto;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartInfoRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.AddCartPickGoodsRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartDeliveryDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartGoodsDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartPickGoodsDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartShippingDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartStoreShippingFastestScheduleDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartSummaryDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartSummaryShippingDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CartTypeSummaryDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CheckCartResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.DelCartAddGoodsRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.DelCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartDataRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetSaveShippingCostGoodsListRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.PutCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartAddGoodsVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartVo;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface ShoppingCartBiz {

	/**
	 * 배송비 절약 상품 목록
	 *
	 * @param	GetSaveShippingCostGoodsListRequestDto
	 * @Return 	List<GoodsSearchResultDto>
	 * @throws	Exception
	 */
	List<GoodsSearchResultDto> getSaveShippingCostGoodsList(GetSaveShippingCostGoodsListRequestDto getSaveShippingCostGoodsListRequestDto) throws Exception;

	/**
	 * 장바구니 수정
	 *
	 * @param	PutCartRequestDto
	 * @throws	Exception
	 */
	int putCart(PutCartRequestDto putCartRequestDto) throws Exception;


	/**
	 * 장바구니 삭제
	 *
	 * @param DelCartRequestDto
	 * @throws Exception
	 */
	int delCartAndAddGoods(DelCartRequestDto delCartRequestDto) throws Exception;

	/**
	 * 장바구니 추가 구성상품 삭제
	 *
	 * @param DelCartAddGoodsRequestDto
	 * @throws Exception
	 */
	int delCartAddGoods(DelCartAddGoodsRequestDto delCartAddGoodsRequestDto) throws Exception;

	/**
	 * 장바구니 타입별 정보
	 *
	 * @param GetCartDataRequestDto
	 * @return List<CartTypeSummaryDto>
	 * @throws Exception
	 */
	List<CartTypeSummaryDto> getCartTypeSummary(GetCartDataRequestDto getCartDataRequestDto) throws Exception;

	/**
	 * 장바구니 수량 정보
	 *
	 * @param GetCartDataRequestDto
	 * @return List<CartTypeSummaryDto>
	 * @throws Exception
	 */
	int getCartCount(GetCartDataRequestDto getCartDataRequestDto) throws Exception;

	/**
	 * 로그인 장바구니 머지
	 *
	 * @param urPcidCd
	 * @param urUserId
	 * @throws Exception
	 */
	void loginCartMerge(String urPcidCd, Long urUserId) throws Exception;

	/**
	 * 장바구니 담기
	 *
	 * @param reqDto
	 * @return Long spCartId
	 * @throws Exception
	 */
	Long addCartInfo(AddCartInfoRequestDto reqDto) throws Exception;

	/**
	 * 기획전 전용 장바구니 담기
	 *
	 * @param AddCartPickGoodsRequestDto
	 * @return Long spCartId
	 * @throws Exception
	 */
	Long addCartPickGoods(AddCartPickGoodsRequestDto reqDto) throws Exception;

	/**
	 * 상품 saleType 으로 DeliveryType 가지고 오기
	 *
	 * @param saleType
	 * @return DeliveryType
	 * @throws Exception
	 */
	DeliveryType getDeliveryTypeBySaleType(String saleType) throws Exception;

	/**
	 * get 장바구니
	 *
	 * @param spCartId
	 * @return SpCartVo
	 * @throws Exception
	 */
	SpCartVo getCart(Long spCartId) throws Exception;

	/**
	 * 장바구니 담기 validation 체크
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	boolean isValidationAddCartInfo(AddCartInfoRequestDto reqDto) throws Exception;

	/**
	 * 장바구니 정보
	 *
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	List<CartDeliveryDto> getCartData(GetCartDataRequestDto reqDto) throws Exception;

	/**
	 * get 장바구니 summary data
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	CartSummaryDto getCartDataSummary(List<CartDeliveryDto> cartDataDto) throws Exception;

	/**
	 * get 장바구니 summary data (포인트 사용)
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	CartSummaryDto getCartDataSummary(List<CartDeliveryDto> cartDataDto, int usePoint) throws Exception;

	/**
	 * 정기배송 선택 도착예정일 리스트
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	List<LocalDate> getRegularShippingArrivalScheduledDateList(List<CartDeliveryDto> cartDataDto) throws Exception;

	/**
	 * 임직원 한도 초가 여부
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	boolean isEmployeeDiscountExceedingLimit(List<CartDeliveryDto> cartDataDto) throws Exception;

	/**
	 * 장바구니에서 판매중인 상품만 추출
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	List<Long> getSaleGoodsIdListByCartData(List<CartDeliveryDto> cartDataDto) throws Exception;

	/**
	 * 무통장 입금 결제 가능 여부
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	String getVirtualAccountYn(List<CartDeliveryDto> cartDataDto) throws Exception;

	/**
	 * 무통장 입금 결제 가능 여부 - 선물하기 포함
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	String getVirtualAccountYn(List<CartDeliveryDto> cartDataDto, String presentYn) throws Exception;

	/**
	 * 배송타입&출고처별 장바구니 예정금액 조회
	 *
	 * @param cartDataDto
	 * @return List<CartSummaryShippingDto>
	 * @throws Exception
	 */
	List<CartSummaryShippingDto> getCartShippingSummary(List<CartDeliveryDto> cartDataDto) throws Exception;

	/**
	 * cartData에서 배송정책별 리스트 추출
	 *
	 * @param	List<CartDeliveryDto>
	 * @return	List<CartShippingDto>
	 * @throws	Exception
	 */
	List<CartShippingDto> getCartShippingList(List<CartDeliveryDto> cartDataDto)throws Exception;

	/**
	 * 배송정책별 리스트에서 상품리스트 추출
	 *
	 * @param	List<CartShippingDto>
	 * @return	List<CartGoodsDto>
	 * @throws	Exception
	 */
	List<CartGoodsDto> getCartGoodsList(List<CartShippingDto> cartShippingDto)throws Exception;

	/**
	 * 골라담기상품 목록 조회
	 *
	 * @param	spCartId, isMember, isEmployee, isDawnDelivery, arrivalScheduledDate
	 * @return	List<CartPickGoodsDto>
	 * @throws	Exception
	 */
	List<CartPickGoodsDto> getCartPickGoodsList(Long spCartId, boolean isMember, boolean isEmployee,
			boolean isDawnDelivery, LocalDate arrivalScheduledDate, int buyQty, String goodsDailyCycleType, HashMap<String, Integer> overlapBuyItem) throws Exception;

	/**
	 * 골라담기상품 재계산
	 *
	 * @param List<PackageGoodsListDto>
	 * @return RecalculationPackageDto
	 * @throws Exception
	 */
	RecalculationPackageDto getRecalculationCartPickGoodsList(String goodsSaleStatus, List<CartPickGoodsDto> cartPickGoodsDtoList) throws Exception;

	/**
	 * 증정품 기획전 조회 및 validation
	 *
	 * @param List<CartDeliveryDto>
	 * @return List<GiftListResponseDto>
	 * @throws Exception
	 */
	List<GiftListResponseDto> giftGetValidation(List<CartDeliveryDto> cartDataDto, String userStatus, Long urGroupId,
												boolean isMember, boolean isEmployee, String presentYn) throws Exception;

	void setGiftStock(List<GiftListResponseDto> giftResponseDto, HashMap<Long, CartShippingDto> shippingMap, String dirInfo, boolean isApp,
					  boolean isMember, boolean isEmployee, String presentYn) throws Exception;

	/**
	 * 장바구니 체크
	 *
	 * @param cartDataDto
	 * @return
	 * @throws Exception
	 */
	CheckCartResponseDto checkBuyPossibleCart(GetSessionShippingResponseDto shippingDto, GetCartDataRequestDto cartDataReqDto, List<CartDeliveryDto> cartDataDto, CartStoreDto cartStoreDto) throws Exception;

	List<CartStoreShippingDto> getStoreShippingScheduleList(List<CartDeliveryDto> cartData, ShippingPossibilityStoreDeliveryAreaDto storeDeliveryAreaInfo) throws Exception;

	CartStoreShippingFastestScheduleDto getStoreFastestSchedule(List<CartStoreShippingDto> list) throws Exception;

	boolean isExistDailyGoodsCart(AddCartInfoRequestDto reqDto) throws Exception;

	List<Long> getCartIdList(GetCartDataRequestDto reqDto) throws Exception;

	List<SpCartAddGoodsVo> getCartAddGoodsIdList(Long spCartId) throws Exception;
}
