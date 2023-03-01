package kr.co.pulmuone.bos.order.create;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.order.create.dto.*;
import kr.co.pulmuone.v1.order.create.service.OrderCreateBiz;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.shopping.cart.dto.CouponDto;
import kr.co.pulmuone.v1.shopping.cart.dto.DelCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCartDataRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.GetCouponPageInfoResponseDto;
import lombok.RequiredArgsConstructor;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

/**
* <PRE>
* Forbiz Korea
* 주문생성 Controller
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0  2020. 12. 18.     강상국          최초작성
* =======================================================================
* </PRE>
*/
@RestController
@RequiredArgsConstructor
public class OrderCreateController {

	@Autowired
    private final OrderCreateBiz orderCreateBiz;

	/**
     * 주문생성 서버정보 조회
     *
     * @return ApiResult<?>
     */
	@ApiOperation(value = "주문생성 엑셀 업로드", httpMethod = "POST")
    @RequestMapping(value = "/admin/order/create/getServerInfo")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = OrderExcelServerInfoResponseDto.class)
	})
	public ApiResult<?> getServerInfo(HttpServletRequest request) throws Exception {
		return orderCreateBiz.getServerInfo();
	}

	/**
     * 주문생성 엑셀 업로드
     *
     * @param orderCreatePaymentRequestDto
     * @return ApiResult<?>
     */
	@ApiOperation(value = "주문생성 엑셀 업로드", httpMethod = "POST")
    @RequestMapping(value = "/admin/order/create/addBosCreateExcelUpload")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = OrderExcelUploadResponseDto.class)
	})
	public ApiResult<?> addBosCreateExcelUpload(MultipartHttpServletRequest request) throws Exception {
		MultipartFile file = null;
		Iterator<String> iterator = request.getFileNames();
		if (iterator.hasNext()) {
			file = request.getFile(iterator.next());
		}
		return orderCreateBiz.addBosCreateExcelUpload(file);
	}

	/**
     * 주문생성 엑셀 업로드 주문생성
     *
     * @param orderCreatePaymentRequestDto
     * @return ApiResult<?>
     */
	@ApiOperation(value = "주문생성 엑셀 업로드 주문생성", httpMethod = "POST")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	@PostMapping(value = "/admin/order/create/addOrderCreate")
	public ApiResult<?> addOrderCreate(OrderCreateRequestDto orderCreateRequestDto) throws Exception {
    	orderCreateRequestDto.setOrderCreateList(BindUtil.convertJsonArrayToDtoList(orderCreateRequestDto.getInsert(), OrderCreateDto.class));
		return orderCreateBiz.addOrderCreate(orderCreateRequestDto);
	}

    /**
     * 엑셀업로드 데이터 상품정보 조회
     *
     * @param GoodsDisExcelUploadRequestDto
     * @return ApiResult<?>
     */
    /*@ApiOperation(value = "엑셀업로드 데이터 상품정보 조회")
    @PostMapping(value = "/admin/order/create/getExcelGoodsList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> getExcelUploadList(OrderExcelRequestDto orderExcelRequestDto) throws Exception {
    	orderExcelRequestDto.setOrderExcelList(BindUtil.convertJsonArrayToDtoList(orderExcelRequestDto.getUpload(), OrderExeclDto.class));
		return orderCreateBiz.getExcelUploadList(orderExcelRequestDto);
	}*/

    @ApiOperation(value = "주문생성 주문조회")
	@PostMapping(value = "/admin/order/create/getOrderCreateList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> getOrderCreateList(HttpServletRequest request, OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
		return orderCreateBiz.getOrderCreateList(BindUtil.bindDto(request, OrderCreateListRequestDto.class));

	}

    @ApiOperation(value = "주문생성 삭제")
    @PostMapping(value = "/admin/order/create/deleteOrderCreate")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> deleteOrderCreate(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
		return orderCreateBiz.deleteOrderCreate(orderCreateListRequestDto);

	}

    @ApiOperation(value = "주문생성 신용카드 결제")
    @PostMapping(value = "/admin/order/create/addCardPayOrderCreate")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> addCardPayOrderCreate(OrderCardPayRequestDto cardPayOrderRequestDto) throws Exception {
		return orderCreateBiz.addCardPayOrderCreate(cardPayOrderRequestDto);

	}

	@ApiOperation(value = "주문생성 가상계좌", httpMethod = "POST")
	@RequestMapping(value = "/admin/order/create/addBankBookOrderCreate")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> addBankBookOrderCreate(OrderCardPayRequestDto cardPayOrderRequestDto) throws Exception {

		try{
			return orderCreateBiz.addBankBookOrderCreate(cardPayOrderRequestDto);
		}catch(BaseException be){
			be.printStackTrace();
			return ApiResult.result(be.getMessageEnum());
		}catch(Exception e){
			e.printStackTrace();
			return ApiResult.result(e.getMessage(),OrderEnums.OrderCreateErrMsg.VIRTUAL_BANK_PG_ERROR);
		}

	}

    /**
     * 주문생성 주문자 정보(배송지, 적립금) 조회
     *
     * @param urUserId
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문생성 주문자 정보(배송지, 적립금) 조회", httpMethod = "GET")
    @RequestMapping(value = "/admin/order/create/getBuyerBaseInfo")
    @ApiImplicitParams({ @ApiImplicitParam(name = "urUserId", value = "회원 PK", required = true, dataType = "String")})
    @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = OrderCreateBuyerInfoResponseDto.class)
	})
	public ApiResult<?> getBuyerBaseInfo(@RequestParam(value = "urUserId", required = true) String urUserId) throws Exception {
		return orderCreateBiz.getBuyerBaseInfo(urUserId);
	}

    /**
     * 주문생성  장바구니 정보 생성
     *
     * @param orderCreateCartRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문생성  장바구니 정보 생성", httpMethod = "POST")
    @RequestMapping(value = "/admin/order/create/addCartInfo")
    @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data<>", response = OrderCreateCartDto.class)
	})
	public ApiResult<?> addCartInfo(OrderCreateCartRequestDto orderCreateCartRequestDto) throws Exception {
    	orderCreateCartRequestDto.convertDataList();
    	return orderCreateBiz.addCartInfo(orderCreateCartRequestDto);
	}

    /**
     * 주문생성 장바구니 정보 조회
     *
     * @param getCartDataRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문생성 장바구니 정보 조회", httpMethod = "POST")
    @RequestMapping(value = "/admin/order/create/getCartInfo")
    @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data<>", response = OrderCreateCartDto.class)
	})
    public ApiResult<?> getCartInfo(@RequestBody GetCartDataRequestDto getCartDataRequestDto) throws Exception {
		return orderCreateBiz.getCartInfo(getCartDataRequestDto);
	}

    /**
     * 주문생성 장바구니 정보(구매수량) 수정
     *
     * @param orderCreateCartRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문생성 장바구니 정보(구매수량) 수정", httpMethod = "POST")
    @RequestMapping(value = "/admin/order/create/putCartInfo")
    @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data<>", response = OrderCreateCartDto.class)
	})
    public ApiResult<?> putCartInfo(@RequestBody OrderCreateCartRequestDto orderCreateCartRequestDto) throws Exception {
    	orderCreateCartRequestDto.convertDataList();
		return orderCreateBiz.putCartInfo(orderCreateCartRequestDto);
	}

    /**
     * 주문생성 사용가능 쿠폰 조회
     *
     * @param orderCreateCouponRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문생성 사용가능 쿠폰 조회", httpMethod = "POST")
    @RequestMapping(value = "/admin/order/create/getCouponPageInfo")
    @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GetCouponPageInfoResponseDto.class)
	})
    public ApiResult<?> getCouponPageInfo(OrderCreateCouponRequestDto orderCreateCouponRequestDto) throws Exception {
    	orderCreateCouponRequestDto.convertDataList();
		return orderCreateBiz.getCouponPageInfo(orderCreateCouponRequestDto);
	}

    /**
     * 주문생성 사용가능 장바구니 쿠폰 조회
     *
     * @param orderCreateCouponRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문생성 사용가능 장바구니 쿠폰 조회", httpMethod = "POST")
    @RequestMapping(value = "/admin/order/create/getCartCouponList")
    @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = CouponDto.class)
	})
    public ApiResult<?> getCartCouponList(OrderCreateCouponRequestDto orderCreateCouponRequestDto) throws Exception {
    	orderCreateCouponRequestDto.convertDataList();
		return orderCreateBiz.getCartCouponList(orderCreateCouponRequestDto);
	}

    /**
     * 주문생성 결제요청
     *
     * @param orderCreatePaymentRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문생성 결제요청", httpMethod = "POST")
    @RequestMapping(value = "/admin/order/create/createOrderPayment")
    @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = BasicDataResponseDto.class)
	})
    public ApiResult<?> createOrderPayment(OrderCreatePaymentRequestDto orderCreatePaymentRequestDto) throws Exception {
    	orderCreatePaymentRequestDto.convertDataList();
		return orderCreateBiz.createOrderPayment(orderCreatePaymentRequestDto);
	}

    /**
     * 주문생성 비인증 신용카드 결제
     *
     * @param orderCreatePaymentRequestDto
     * @return ApiResult<?>
     */
    @ApiOperation(value = "주문생성 비인증 신용카드 결제", httpMethod = "POST")
    @RequestMapping(value = "/admin/order/create/cardPayOrderCreate")
    @ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> cardPayOrderCreate(OrderCardPayRequestDto orderCardPayRequestDto) throws Exception {
		return orderCreateBiz.cardPayOrderCreate(orderCardPayRequestDto);
	}

	/**
	 * 주문생성 > 일일상품 옵션변경 팝업 > 일일상품 정보 조회
	 *
	 * @param orderCreatePaymentRequestDto
	 * @return ApiResult<?>
	 */
	@ApiOperation(value = "주문생성 > 일일상품 옵션변경 팝업 > 일일상품 정보 조회", httpMethod = "POST")
	@RequestMapping(value = "/admin/order/create/getGoodsDailyCycleList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> getGoodsDailyCycleList(@RequestParam(value = "ilGoodsId", required = true) Long ilGoodsId,
											   @RequestParam(value = "recvBldNo", required = true) String recvBldNo,
											   @RequestParam(value = "zipCode", required = true) String zipCode) throws Exception {
		return orderCreateBiz.getGoodsDailyCycleList(ilGoodsId,recvBldNo, zipCode);
	}

	/**
	 * 주문생성 > 상품삭제
	 *
	 * @param delCartRequestDto
	 * @return ApiResult<?>
	 */
	@ApiOperation(value = "주문생성 > 상품삭제", httpMethod = "POST")
	@RequestMapping(value = "/admin/order/create/delCartAndAddGoods")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> delCartAndAddGoods(@RequestBody DelCartRequestDto delCartRequestDto) throws Exception {
		return orderCreateBiz.delCartAndAddGoods(delCartRequestDto);
	}

	/**
	 * 주문생성 > 추가 옵션변경 팝업 > 추가상품 정보 조회
	 *
	 * @param ilGoodsId
	 * @return ApiResult<?>
	 */
	@ApiOperation(value = "주문생성 > 추가상품 옵션변경 팝업 > 추가상품 정보 조회", httpMethod = "POST")
	@RequestMapping(value = "/admin/order/create/getAdditionalGoodsInfoList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data : Integer.class") })
	public ApiResult<?> getAdditionalGoodsInfoList(@RequestParam(value = "ilGoodsId", required = true) Long ilGoodsId) throws Exception {
		return orderCreateBiz.getAdditionalGoodsInfoList(ilGoodsId);
	}
}