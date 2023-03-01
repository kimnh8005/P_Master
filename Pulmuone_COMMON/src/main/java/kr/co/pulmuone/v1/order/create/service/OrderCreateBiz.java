package kr.co.pulmuone.v1.order.create.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.create.dto.*;
import kr.co.pulmuone.v1.order.create.dto.vo.CreateInfoVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import kr.co.pulmuone.v1.shopping.cart.dto.CartDeliveryDto;
import kr.co.pulmuone.v1.shopping.cart.dto.DelCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartDataRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 관련 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
public interface OrderCreateBiz {

	/**
	 * 서버정보 조회
	 * @return
	 * @throws Exception
	 */
	ApiResult<?> getServerInfo() throws Exception;

	/** 주문생성 엑셀 업로드 */
	ApiResult<?> addBosCreateExcelUpload(MultipartFile file) throws Exception;

	List<OrderExcelResponseDto> getExcelUploadList(OrderExcelRequestDto orderExcelRequestDto) throws Exception;

    ApiResult<?> addOrderCreate(OrderCreateRequestDto orderCreateRequestDto) throws Exception;

    GoodsInfoDto getGoodsInfo(long ilGoodsId) throws Exception;

    UserGroupInfoDto getUserGroupInfo(long urUserId) throws Exception;

    int addCreateInfo(CreateInfoVo createInfoVo) throws Exception;


    List<String> getOdIdList(String odid) throws Exception;

    //int putOrderInfo(List<OrderRegistrationResponseDto> orderRegistrationResponseDtoList) throws Exception;

    ApiResult<?> getOrderCreateList(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

    ApiResult<?> deleteOrderCreate(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

	ApiResult<?> deleteOrderCreateInfo(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

	ApiResult<?> deleteOrder(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

	ApiResult<?> deleteOrderDt(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

	ApiResult<?> deleteShippingZone(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

	ApiResult<?> deleteShippingZoneHist(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

	ApiResult<?> deleteShippingPrice(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

	ApiResult<?> deleteOrderDetl(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception;

	ApiResult<?> addCardPayOrderCreate(OrderCardPayRequestDto orderCardPayRequestDto) throws Exception;

	OrderInfoDto getOrderInfo(OrderCardPayRequestDto OrderCardPayRequestDto) throws Exception;

	PaymentInfoDto getPaymentInfo(long odPaymentMasterId) throws Exception;

	int putPaymentMasterInfo(OrderPaymentMasterVo OrderPaymentMasterVo) throws Exception;

	ApiResult<?> addBankBookOrderCreate(OrderCardPayRequestDto orderCardPayRequestDto) throws Exception;
	/** 주문생성 주문자 정보(배송지, 적립금) 조회 */
	ApiResult<?> getBuyerBaseInfo(String urUserId) throws Exception;
	/** 주문생성  장바구니 정보 생성 */
	ApiResult<?> addCartInfo(OrderCreateCartRequestDto orderCreateCartRequestDto) throws Exception;
	/** 주문생성 장바구니 정보 조회 */
	ApiResult<?> getCartInfo(GetCartDataRequestDto getCartDataRequestDto) throws Exception;
	/** 주문생성 장바구니 정보(구매수량) 수정 */
	ApiResult<?> putCartInfo(OrderCreateCartRequestDto orderCreateCartRequestDto) throws Exception;
	/** 주문생성 사용가능 쿠폰 조회 */
	ApiResult<?> getCouponPageInfo(OrderCreateCouponRequestDto orderCreateCouponRequestDto) throws Exception;
	/** 주문생성 사용가능 장바구니 쿠폰 조회 */
	ApiResult<?> getCartCouponList(OrderCreateCouponRequestDto orderCreateCouponRequestDto) throws Exception;
	/** 주문생성 결제요청 */
	ApiResult<?> createOrderPayment(OrderCreatePaymentRequestDto orderCreatePaymentRequestDto) throws Exception;
	/** 주문생성 비인증 신용카드 결제 */
	ApiResult<?> cardPayOrderCreate(OrderCardPayRequestDto orderCardPayRequestDto) throws Exception;

	/**
	 * 주문연동 매출만연동시 결제완료시 처리
	 * @param odOrderId
	 */
	void orderCopySalIfExecute(long odOrderId, String orderCopySalIfYn, String orderCopyOdid) throws Exception;

	/**
	 * 주문 실패 처리
	 * @param odOrderId
	 */
	void putOrderFail(Long odOrderId);

	/**
	 * 주문 실패 처리
	 * @param odOrderId
	 */
	void putOrderSuccess(Long odPaymentMasterId);

	/** 주문생성 > 일일상품 옵션변경 팝업 > 일일상품 정보 조회 */
	ApiResult<?> getGoodsDailyCycleList(Long ilGoodsId, String recvBldNo, String zipCode) throws Exception;

	/** 주문생성 > 상품삭제 */
	ApiResult<?> delCartAndAddGoods(DelCartRequestDto delCartRequestDto) throws Exception;

	/** 장바구니 정보 조회 */
	List<CartDeliveryDto> getCartData(GetCartDataRequestDto getCartDataRequestDto) throws Exception;

	ApiResult<?> getAdditionalGoodsInfoList(long ilGoodsId) throws Exception;
}