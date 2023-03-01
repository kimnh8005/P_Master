package kr.co.pulmuone.v1.order.create.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.DeviceType;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderStatus;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PayType;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.enums.PgEnums.InicisCardCode;
import kr.co.pulmuone.v1.comm.enums.PgEnums.PgServiceType;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.ApplyPayment;
import kr.co.pulmuone.v1.comm.enums.SystemEnums.AgentType;
import kr.co.pulmuone.v1.comm.enums.UserEnums.BuyerType;
import kr.co.pulmuone.v1.comm.excel.factory.OrderExcelUploadFactory;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.*;
import kr.co.pulmuone.v1.goods.goods.dto.*;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemWarehouseVo;
import kr.co.pulmuone.v1.goods.stock.dto.StockOrderRequestDto;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockOrderBiz;
import kr.co.pulmuone.v1.order.create.dto.*;
import kr.co.pulmuone.v1.order.create.dto.vo.CreateInfoVo;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderDataDto;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderPaymentDataDto;
import kr.co.pulmuone.v1.order.order.dto.PutOrderPaymentCompleteResDto;
import kr.co.pulmuone.v1.order.order.dto.StockCheckOrderDetailDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderVo;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.order.service.OrderProcessBiz;
import kr.co.pulmuone.v1.order.registration.dto.OrderApprovalDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderRegistrationResponseDto;
import kr.co.pulmuone.v1.order.registration.service.OrderBindBosCreateBizImpl;
import kr.co.pulmuone.v1.order.registration.service.OrderBindCartBizImpl;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationBiz;
import kr.co.pulmuone.v1.pg.dto.BasicDataRequestDto;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.pg.dto.CancelRequestDto;
import kr.co.pulmuone.v1.pg.dto.VirtualAccountDataResponseDto;
import kr.co.pulmuone.v1.pg.service.PgAbstractService;
import kr.co.pulmuone.v1.pg.service.PgBiz;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNonAuthenticationCartPayRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNonAuthenticationCartPayResponseDto;
import kr.co.pulmuone.v1.pg.service.inicis.service.InicisPgService;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.point.dto.DepositPointDto;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import kr.co.pulmuone.v1.promotion.point.service.PromotionPointBiz;
import kr.co.pulmuone.v1.shopping.cart.dto.*;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartAddGoodsVo;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.SpCartVo;
import kr.co.pulmuone.v1.shopping.cart.service.ShoppingCartBiz;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import kr.co.pulmuone.v1.store.delivery.service.StoreDeliveryBiz;
import kr.co.pulmuone.v1.user.buyer.dto.CommonGetRefundBankRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetRefundBankResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetShippingAddressListResultVo;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionShippingResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 생성 관련 Implements
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
@Slf4j
@Service
public class OrderCreateBizImpl implements OrderCreateBiz {

    @Autowired
    private OrderCreateService orderCreateService;

    @Autowired
    private InicisPgService inicisPgService;

    @Autowired
    private OrderRegistrationBiz orderRegistrationBiz;

    @Autowired
    private PgBiz pgBiz;

	@Autowired
	private OrderExcelUploadFactory orderExcelUploadFactory;

	@Autowired
	private UserBuyerBiz userBuyerBiz;

	@Autowired
	private PromotionPointBiz promotionPointBiz;

	@Autowired
	private GoodsGoodsBiz goodsGoodsBiz;

	@Autowired
	private OrderOrderBiz orderOrderBiz;

	@Autowired
	private ShoppingCartBiz shoppingCartBiz;

	@Autowired
	private PromotionCouponBiz promotionCouponBiz;

	@Autowired
	private OrderProcessBiz orderProcessBiz;

	@Autowired
	private OrderEmailBiz orderEmailBiz;

	@Autowired
	private OrderEmailSendBiz orderEmailSendBiz;

	@Autowired
	OrderBindBosCreateBizImpl bosOrderBindBiz;

	@Autowired
	OrderBindCartBizImpl cartOrderBindBiz;

	@Autowired
	private GoodsStockOrderBiz goodsStockOrderBiz;

	@Autowired
	private PointBiz pointBiz;

	@Autowired
	public StoreDeliveryBiz storeDeliveryBiz;




	private static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;

	private static final String ORDER_CREATE_DIR_INFO 			= "PC";		// 디바이스 정보
	private static final boolean ORDER_CREATE_IS_APP 			= false;	// 앱 여부
	private static final boolean ORDER_CREATE_IS_EMPLOYEE 		= false;	// 임직원 여부
	private static final boolean ORDER_CREATE_IS_DAWN_DELIVERY 	= false; 	// 새벽배송 여부
	private static final String ORDER_CREATE_UR_PCID_CD 		= "99999999-9999-9999-9999-999999999999";	// 사용자 환경 정보 PK
	private static final String ORDER_CREATE_CART_TYPE 			= ShoppingEnums.CartType.NORMAL.getCode();

	public static final String SCHEDULE_DATE = "2000-01-01"; // 출고예정일이 null일때 기본값

	/**
	 * 주문생성 서버정보 조회
	 *
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ApiResult<?> getServerInfo() throws Exception {

		OrderExcelServerInfoResponseDto orderExcelServerInfoResponseDto = new OrderExcelServerInfoResponseDto();
		orderExcelServerInfoResponseDto.setIsProdServerYn(OrderEnums.OrderCreateProdServer.PROD_N.getCode());
		if (SystemUtil.PROD_PROFILE.equals(System.getProperty(SystemUtil.SPRING_PROFILES_ACTIVE))) {
			orderExcelServerInfoResponseDto.setIsProdServerYn(OrderEnums.OrderCreateProdServer.PROD_Y.getCode());
		}
		return ApiResult.success(orderExcelServerInfoResponseDto);
	}

	/**
	 * 주문생성 엑셀 업로드
	 *
	 * @param file
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ApiResult<?> addBosCreateExcelUpload(MultipartFile file) throws Exception {

		if (ExcelUploadUtil.isFile(file) != true) return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_UPLOAD_NONE);

		// Excel Import 정보 -> Dto 변환
		Sheet uploadSheet = ExcelUploadUtil.excelParse(file);
		if (uploadSheet == null) return ApiResult.result(null, ExcelUploadValidateEnums.UploadResponseCode.EXCEL_TRANSFORM_FAIL);

		String excelUploadType = ExcelUploadEnums.ExcelUploadType.BOS_CREATE.getCode();

		// Excel 데이터 Mapping
		List<OrderExeclDto> excelList = (List<OrderExeclDto>) orderExcelUploadFactory.setExcelData(excelUploadType, uploadSheet);

		// 검증용 상품정보 조회
		List<OrderExcelResponseDto> excelGoodsList = orderExcelUploadFactory.getBoarCreateIlGoodsIdList(excelUploadType, excelList);

		// 항목별 검증 진행
		excelGoodsList = (List<OrderExcelResponseDto>) orderExcelUploadFactory.getGoodsRowItemValidator(excelUploadType, excelGoodsList, null, null, null);

		// 업로드 현황 설정
		List<OrderExcelResponseDto> successVoList 	= new ArrayList<>();	// 성공리스트
		List<OrderExcelResponseDto> failVoList 		= new ArrayList<>();	// 실패리스트

		for (OrderExcelResponseDto dto : excelGoodsList) {
			if (dto.isSuccess()) {
				successVoList.add(dto);
			} else {
				failVoList.add(dto);
			}
		}

		// Return 값 설정
		OrderExcelUploadResponseDto responseDto = new OrderExcelUploadResponseDto();
		responseDto.setUploadFlag(true);
		responseDto.setSuccessList(successVoList);
		responseDto.setFailList(failVoList);
		responseDto.setTotalCount(excelList.size());
		responseDto.setSuccessCount(successVoList.size());
		responseDto.setFailCount(failVoList.size());

		if (responseDto.getFailCount() > 0) {
			responseDto.setUploadFlag(false);
			responseDto.setFailMessage(failVoList.stream().map(OrderExcelResponseDto::getFailMessage).distinct()
					.collect(Collectors.joining("<BR>")));
		}
		return ApiResult.success(responseDto);
	}


    @Override
    public List<OrderExcelResponseDto> getExcelUploadList(OrderExcelRequestDto orderExcelRequestDto) throws Exception {
        return orderCreateService.getExcelUploadList(orderExcelRequestDto);
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
	public ApiResult<?> addOrderCreate(OrderCreateRequestDto orderCreateRequestDto) throws Exception {
		UserVo userVo = SessionUtil.getBosUserVO();

        List<OrderBindDto> orderBindList = bosOrderBindBiz.orderDataBind(orderCreateRequestDto);

		String failMessage = "";


		int orderFailCnt = (int) orderBindList.stream().filter(o -> "N".equals(o.getOrder().getCreateYn())).count();

		OrderEnums.OrderRegistrationResult result = OrderEnums.OrderRegistrationResult.SUCCESS;

		BasicDataResponseDto resDto = new BasicDataResponseDto();
		if (orderFailCnt > 0){
			List<String> messageList = new ArrayList<>();
			for(OrderBindDto item: orderBindList){
				if ("N".equals(item.getOrder().getCreateYn())){
					 messageList.add(item.getOrder().getFailMessage());
				}
			}

			List<String> failMessageList = messageList.stream()
					.distinct()
					.collect(Collectors.toList());

			for(String item: failMessageList){
					failMessage += Constants.ARRAY_SEPARATORS + item;
			}

			resDto.setResult(OrderEnums.OrderRegistrationResult.FAIL);
			resDto.setFailMessage(failMessage.substring(1));


			return ApiResult.success(resDto);
		} else {



			OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrderNonTransaction(orderBindList, "N");

			CreateInfoVo createInfoVo = CreateInfoVo.builder()
					.createType("PARTY".equals(orderCreateRequestDto.getOrderType()) ? "T" : "S")    //구분
					.buyerNm(orderCreateRequestDto.getBuyerNm())                    //구분
					.orderPrice(orderRegistrationResponseDto.getPaymentPrice())        //주문금액
					.orderPaymentType(orderCreateRequestDto.getPsPayCd())            //결제수단
					.successOrderCnt(orderRegistrationResponseDto.getSuccCnt())        //성공주문번호건수
					.failureOrderCnt(orderRegistrationResponseDto.getFailCnt())        //실패주문번호건수
					.odid(orderRegistrationResponseDto.getOdids())    //주문번호검색용
					.createStatus("W")        //진행상태
					.originNm(null)            //원본파일명
					.uploadNm(null)            //업로드파일명
					.uploadPath(null)        //업로드경로
					.createId(Long.parseLong(userVo.getUserId()))        //등록자
					.build();

			addCreateInfo(createInfoVo);    //주문생성 업데이트 한다.

			resDto.setResult(result);
			resDto.setOdid(orderRegistrationResponseDto.getOdids());
			resDto.setOdOrderId(orderRegistrationResponseDto.getOdOrderIds().split(",")[0]);
			resDto.setOdPaymentMasterId(orderRegistrationResponseDto.getOdPaymentMasterId());
			resDto.setPaymentPrice(orderRegistrationResponseDto.getPaymentPrice());

			return ApiResult.success(resDto);
		}
    }

	@Override
	public UserGroupInfoDto getUserGroupInfo(long urUserId) throws Exception {
		return orderCreateService.getUserGroupInfo(urUserId);
	}

	@Override
	public GoodsInfoDto getGoodsInfo(long ilGoodsId) throws Exception {
		return orderCreateService.getGoodsInfo(ilGoodsId);
	}

	@Override
	public int addCreateInfo(CreateInfoVo createInfoVo) throws Exception {
		return orderCreateService.addCreateInfo(createInfoVo);
	}
/*
	@Override
	public int putOrderInfo(List<OrderRegistrationResponseDto> orderRegistrationResponseDtoList) throws Exception {
		int cnt = 0;
		for (OrderRegistrationResponseDto orderRegistrationResponseDto: orderRegistrationResponseDtoList) {
			List<String> odIdList = new ArrayList<>();
			String[] odIdArray = orderRegistrationResponseDto.getOdid().split(",");
			for (String odId : odIdArray) {
				odIdList.add(odId);
			}
			OrderCreateRequestDto orderCreateRequestDto = new OrderCreateRequestDto();
			orderCreateRequestDto.setFindOdIdList(odIdList);
			cnt += orderCreateService.putOrderInfo(orderCreateRequestDto);
		}
		return cnt;
	}
*/
	@Override
	public ApiResult<?> getOrderCreateList(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
		Page<CreateInfoDto> orderInfoList = orderCreateService.getOrderCreateList(orderCreateListRequestDto);

		OrderCreateListResponseDto orderCreateListResponseDto = OrderCreateListResponseDto.builder()
				.total(orderInfoList.getTotal())
				.rows(orderInfoList.getResult())
				.build();

        return ApiResult.success(orderCreateListResponseDto);
	}

	@Override
	public ApiResult<?> deleteOrderCreate(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {

		List<String> odIdList = getOdIdList(orderCreateListRequestDto.getOdid());
		orderCreateListRequestDto.setFindOdIdList(odIdList);

		deleteShippingPrice(orderCreateListRequestDto);
		deleteOrderDetl(orderCreateListRequestDto);
		deleteShippingZoneHist(orderCreateListRequestDto);
		deleteShippingZone(orderCreateListRequestDto);
		deleteOrderDt(orderCreateListRequestDto);
		deleteOrder(orderCreateListRequestDto);
	    deleteOrderCreateInfo(orderCreateListRequestDto);

        OrderCreateResponseDto orderCreateResponseDto = OrderCreateResponseDto.builder()
        		.orderRegistrationResult(OrderEnums.OrderRegistrationResult.SUCCESS)
                .build();
		return ApiResult.success(orderCreateResponseDto);
	}

	@Override
	public ApiResult<?> addCardPayOrderCreate(OrderCardPayRequestDto orderCardPayRequestDto) throws Exception {
		List<String> odIdList = getOdIdList(orderCardPayRequestDto.getOdid());
		orderCardPayRequestDto.setFindOdIdList(odIdList);
		log.debug("카드정보 orderCardPayRequestDto <{}>", orderCardPayRequestDto);

		OrderInfoDto orderInfoDto = getOrderInfo(orderCardPayRequestDto);
		log.debug("주문정보 orderInfoDto <{}>", orderInfoDto);

		PaymentInfoDto paymentInfoDto = getPaymentInfo(orderCardPayRequestDto.getOdPaymentMasterId());
		log.debug("결제금액정보 PaymentInfoDto1 <{}>", paymentInfoDto);

		// 신용카드 비인증 결제
		InicisNonAuthenticationCartPayRequestDto payReqDto = new InicisNonAuthenticationCartPayRequestDto();
		payReqDto.setOdid(UidUtil.randomUUID().toString());
		payReqDto.setGoodsName(orderInfoDto.getGoodsNm());
		payReqDto.setPaymentPrice(paymentInfoDto.getPaidPrice());
		payReqDto.setTaxPaymentPrice(paymentInfoDto.getTaxablePrice());
		payReqDto.setTaxFreePaymentPrice(paymentInfoDto.getNonTaxablePrice());
		payReqDto.setBuyerName(orderInfoDto.getBuyerNm());
		payReqDto.setBuyerEmail(orderInfoDto.getBuyerMail());
		payReqDto.setBuyerMobile(orderInfoDto.getBuyerHp());
		payReqDto.setQuota(orderCardPayRequestDto.getPlanPeriod());
		payReqDto.setCardNumber(orderCardPayRequestDto.getCardNo());
		payReqDto.setCardExpire(orderCardPayRequestDto.getCardNumYy() + orderCardPayRequestDto.getCardNumMm());
		payReqDto.setRegNo(orderCardPayRequestDto.getAddInfoVal());
		payReqDto.setCardPw(orderCardPayRequestDto.getCardPass());

		log.debug("카드결제 payReqDto <{}>", payReqDto);

		InicisNonAuthenticationCartPayResponseDto payResDto = inicisPgService.nonAuthenticationCartPay(payReqDto);

		log.debug("카드결제 결과 payResDto <{}>", payResDto);

		OrderEnums.OrderRegistrationResult orderRegistrationResult = null;

		if (payResDto.isSuccess()) {
			OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.CARD;

			OrderPaymentMasterVo orderPaymentMasterVo = OrderPaymentMasterVo.builder()
					.odPaymentMasterId(orderCardPayRequestDto.getOdPaymentMasterId())
					.type(PayType.G.getCode())					//결제타입 (G : 결제, F : 환불 , A : 추가)
					.status(OrderStatus.INCOM_COMPLETE.getCode())			//결제상태(IR:입금예정,IC:입금완료)
					.payTp(paymentType.getCode())				//결제방법 공통코드(PAY_TP)
					.pgService(PgServiceType.INICIS.getCode()) 	//PG 종류 공통코드(PG_SERVICE)
					.tid(payResDto.getTid()) 					//거래 ID
					.authCd(payResDto.getPayAuthCode()) 		//승인코드
					.cardNumber(MaskingUtil.cardNumber(orderCardPayRequestDto.getCardNo())) // 카드번호
					.info(InicisCardCode.findByCode(payResDto.getCardCode()).getCodeName())	//결제정보
					.responseData(payResDto.toString())			//응담메세지
					.approvalDt(LocalDateTime.parse(payResDto.getPayDate().substring(0, 4) + "-" + payResDto.getPayDate().substring(4, 6) + "-" + payResDto.getPayDate().substring(6, 8) + " " +
							payResDto.getPayTime().substring(0, 2) + ":" + payResDto.getPayTime().substring(2, 4) + ":" + payResDto.getPayTime().substring(4, 6), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
					.escrowYn("N") 	//에스크로 결제 여부
					.build();

			putPaymentMasterInfo(orderPaymentMasterVo);

			orderRegistrationResult = OrderEnums.OrderRegistrationResult.SUCCESS;

		} else {
			orderRegistrationResult = OrderEnums.OrderRegistrationResult.FAIL;
		}

		OrderCardPayResponseDto orderCreateResponseDto = OrderCardPayResponseDto.builder()
        		.result(orderRegistrationResult)
        		.message(payResDto.getMessage())
                .build();

		return ApiResult.success(orderCreateResponseDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> addBankBookOrderCreate(OrderCardPayRequestDto orderCardPayRequestDto) throws Exception {
		UserVo userVo = SessionUtil.getBosUserVO();
		log.debug("addOrderCreate 로그인 정보 userVo ::: <{}>", userVo);

		List<String> odIdList = getOdIdList(orderCardPayRequestDto.getOdid());
		orderCardPayRequestDto.setFindOdIdList(odIdList);
		log.debug("무통장입금 orderCardPayRequestDto <{}>", orderCardPayRequestDto);

		OrderInfoDto orderInfoDto = getOrderInfo(orderCardPayRequestDto);
		log.debug("무통장 주문정보 orderInfoDto <{}>", orderInfoDto);

		PaymentInfoDto paymentInfoDto = getPaymentInfo(orderCardPayRequestDto.getOdPaymentMasterId());
		log.debug("무통장 금액정보 PaymentInfoDto1 <{}>", paymentInfoDto);

		PaymentType paymentType = PaymentType.VIRTUAL_BANK; //결제정보PK
		log.debug("무통장 paymentType <{}>", paymentType);

		//PgActiveRateVo pgActiveRateVo = pgBiz.getPgActiveRate(paymentType.getCode());	//결제정보PK
		//log.debug("무통장 pgActiveRateVo <{}>", pgActiveRateVo);

		//PgServiceType pgServiceType = PgServiceType.getProbabilityChoiceService(pgActiveRateVo.getKcpRate(), pgActiveRateVo.getInicisRate());
		/* BOS 가상계좌 채번은 이니시스로만 하기로 함 */
		PgServiceType pgServiceType = PgServiceType.INICIS;
		log.debug("무통장 pgServiceType <{}>", pgServiceType);

		PgAbstractService<?, ?> pgService = pgBiz.getService(pgServiceType);

		log.debug("무통장 pgService <{}>", pgService);

		BasicDataRequestDto basicDataRequestDto = new BasicDataRequestDto();
		basicDataRequestDto.setOdid(Constants.ORDER_ODID_DIV_PAYMENT_MASTER + String.valueOf(orderCardPayRequestDto.getOdPaymentMasterId()));
		if(!OrderClaimEnums.OrderCreateTp.EXCEL_UPLOAD.getCode().equals(orderCardPayRequestDto.getOrderCreateTp())) {
			// 주문생성/주문복사 가상계좌 사용시 엑셀업로드 형태를 제외하고는 PG사와 연동 key 값으로 주문번호를 사용함
			basicDataRequestDto.setOdid(orderCardPayRequestDto.getOdid());
		}
		basicDataRequestDto.setPaymentType(paymentType);
		basicDataRequestDto.setPgBankCode(pgBiz.getPgBankCode(pgServiceType.getCode(), paymentType.getCode(), orderCardPayRequestDto.getPgBankCd()));
		basicDataRequestDto.setGoodsName(orderInfoDto.getGoodsNm());
		basicDataRequestDto.setPaymentPrice(paymentInfoDto.getPaymentPrice());
		basicDataRequestDto.setTaxPaymentPrice(paymentInfoDto.getTaxablePrice());
		basicDataRequestDto.setTaxFreePaymentPrice(paymentInfoDto.getNonTaxablePrice());
		basicDataRequestDto.setBuyerName(orderInfoDto.getBuyerNm());
		basicDataRequestDto.setBuyerEmail(StringUtil.isNotEmpty(orderInfoDto.getBuyerMail())?orderInfoDto.getBuyerMail() :Constants.PG_SERVICE_TEMPORARY_EMAIL);
		basicDataRequestDto.setBuyerMobile(orderInfoDto.getBuyerHp());
		basicDataRequestDto.setLoginId(StringUtil.isNotEmpty(userVo.getLoginId()) ? userVo.getLoginId() : "비회원");
		basicDataRequestDto.setVirtualAccountDateTime(pgBiz.getVirtualAccountDateTime());
		basicDataRequestDto.setFlgCash(orderCardPayRequestDto.getFlgCash());
		basicDataRequestDto.setCashReceiptNumber(orderCardPayRequestDto.getCashReceiptNumber());

		log.debug("가상계좌 채번 하기 위한 요청 값  basicDataRequestDto <{}>", basicDataRequestDto);

		VirtualAccountDataResponseDto responseDto = pgService.getVirtualAccountData(basicDataRequestDto);

		log.debug("가상계좌 채번 결과 responseDto ::: <{}>", responseDto);

		OrderEnums.OrderRegistrationResult orderRegistrationResult = null;
		if (responseDto.isSuccess()) {
			for(String odid : odIdList){

				// 주문번호로 주문데이터 조회
				PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

				PutOrderPaymentCompleteResDto putResDto = orderOrderBiz.putOrderPaymentComplete(orderData, responseDto);

				if (!PgEnums.PgErrorType.SUCCESS.getCode().equals(putResDto.getResult().getCode())) {
					throw new BaseException(putResDto.getResult().getCodeName());
				}
			}
			orderRegistrationResult = OrderEnums.OrderRegistrationResult.SUCCESS;

		} else {
			orderRegistrationResult = OrderEnums.OrderRegistrationResult.FAIL;
		}

		OrderCardPayResponseDto orderCreateResponseDto = OrderCardPayResponseDto.builder()
        		.result(orderRegistrationResult)
        		.message(responseDto.getMessage())
				.odid(orderCardPayRequestDto.getOdid())
                .build();
		return ApiResult.success(orderCreateResponseDto);
	}

	@Override
	public List<String> getOdIdList(String odid) throws Exception {
		List<String> odIdList = new ArrayList<>();
		String[] odIdArray = odid.split(",");
		for (String odId : odIdArray) {
			odIdList.add(odId);
		}
		return odIdList;
	}

	@Override
	public OrderInfoDto getOrderInfo(OrderCardPayRequestDto orderCardPayRequestDto) throws Exception {
    	return orderCreateService.getOrderInfo(orderCardPayRequestDto);
    }

	@Override
	public PaymentInfoDto getPaymentInfo(long odPaymentMasterId) throws Exception {
    	return orderCreateService.getPaymentInfo(odPaymentMasterId);
    }

	@Override
	public int putPaymentMasterInfo(OrderPaymentMasterVo orderPaymentMasterVo) throws Exception {
		return orderCreateService.putPaymentMasterInfo(orderPaymentMasterVo);
	}

	@Override
	public ApiResult<?> deleteOrderCreateInfo(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
		orderCreateService.deleteOrderCreateInfo(orderCreateListRequestDto);
    	return ApiResult.success();
    }

	@Override
	public ApiResult<?> deleteOrder(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
		orderCreateService.deleteOrder(orderCreateListRequestDto);
    	return ApiResult.success();
    }

	@Override
	public ApiResult<?> deleteOrderDt(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
		orderCreateService.deleteOrderDt(orderCreateListRequestDto);
    	return ApiResult.success();
    }

	@Override
	public ApiResult<?> deleteShippingZone(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
		orderCreateService.deleteShippingZone(orderCreateListRequestDto);
    	return ApiResult.success();
    }

	@Override
	public ApiResult<?> deleteShippingZoneHist(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
		orderCreateService.deleteShippingZoneHist(orderCreateListRequestDto);
    	return ApiResult.success();
    }

	@Override
	public ApiResult<?> deleteShippingPrice(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
		orderCreateService.deleteShippingPrice(orderCreateListRequestDto);
    	return ApiResult.success();
    }

	@Override
	public ApiResult<?> deleteOrderDetl(OrderCreateListRequestDto orderCreateListRequestDto) throws Exception {
		orderCreateService.deleteOrderDetl(orderCreateListRequestDto);
    	return ApiResult.success();
    }

    /**
     * 주문생성 주문자 정보(배송지, 적립금) 조회
     *
     * @param urUserId
     * @return ApiResult<?>
     * @throws Exception
     */
	@Override
	public ApiResult<?> getBuyerBaseInfo(String urUserId) throws Exception {
		if (urUserId != null) {
			OrderCreateBuyerInfoResponseDto orderCreateBuyerInfoResponseDto = new OrderCreateBuyerInfoResponseDto();
			GetShippingAddressListResultVo getShippingAddressListResultVo = userBuyerBiz.getBasicShippingAddress(null, urUserId);
			if (getShippingAddressListResultVo != null) orderCreateBuyerInfoResponseDto = OBJECT_MAPPER.convertValue(getShippingAddressListResultVo, OrderCreateBuyerInfoResponseDto.class);
			orderCreateBuyerInfoResponseDto.setAvailablePoint(promotionPointBiz.getPointUsable(new Long(urUserId)));
			//주문자 환불계좌 조회
			CommonGetRefundBankRequestDto dto = new CommonGetRefundBankRequestDto();
			dto.setUrUserId(urUserId);
			CommonGetRefundBankResultVo refundInfo = userBuyerBiz.getRefundBank(dto);
			orderCreateBuyerInfoResponseDto.setRefundBankResult(refundInfo);
			return ApiResult.success(orderCreateBuyerInfoResponseDto);
		}
		return ApiResult.success();
	}

	/**
     * 주문생성  장바구니 정보 생성
     *
     * @param orderCreateCartRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
	@Override
	public ApiResult<?> addCartInfo(OrderCreateCartRequestDto orderCreateCartRequestDto) throws Exception {

		boolean isMember = StringUtil.isNotEmpty(orderCreateCartRequestDto.getUrUserId());
		List<AddCartInfoRequestDto> addGoodsList = new ArrayList<AddCartInfoRequestDto>();
		MessageCommEnum resultCode = BaseEnums.Default.SUCCESS;

		LocalDate arrivalScheduledDate = null;	//고객 선택 도착예정일
		int buyQty = 1; 						// 구매 수량

		List<CheckCartGoodsDto> noGoodsStatusOnSalelist	= new ArrayList<CheckCartGoodsDto>();
		List<CheckCartGoodsDto> goodsLackStockList  	= new ArrayList<CheckCartGoodsDto>();
		List<CheckCartGoodsDto> lackLimitMinCntList  	= new ArrayList<CheckCartGoodsDto>();
		List<CheckCartGoodsDto> overLimitMaxCntList  	= new ArrayList<CheckCartGoodsDto>();
		GoodsDailyCycleDto goodsDailyCycle 	= new GoodsDailyCycleDto();

		ShoppingEnums.DeliveryType deliveryType = ShoppingEnums.DeliveryType.findByCode(orderCreateCartRequestDto.getDeliveryType());
		List<String> messageList = new ArrayList<>();


		isMember = true;
		if (deliveryType == null) {
			resultCode = ShoppingEnums.AddCartInfo.ORDER_CREATE_FAIL_VALIDATION;
		} else {
			for (AddCartListGoodsRequestDto addCartList : orderCreateCartRequestDto.getAddGoodsList()) {
				GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder().ilGoodsId(addCartList.getIlGoodsId())
						.deviceInfo(ORDER_CREATE_DIR_INFO).isApp(ORDER_CREATE_IS_APP).isMember(isMember).isEmployee(ORDER_CREATE_IS_EMPLOYEE).build();

				//판매대기, 관리자품절 상품도 가능하게 세팅
				goodsRequestDto.setBosCreateOrder(true);

				BasicSelectGoodsVo goods = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);
				resultCode = BaseEnums.Default.SUCCESS;

				// 상품이 없을 경우
				if (goods == null) {
					//return ApiResult.result(ShoppingEnums.AddCartInfo.NO_GOODS);
					resultCode = ShoppingEnums.AddCartInfo.NO_GOODS;
					messageList.add("상품 정보를 확인해주세요. 상품코드 : ["+addCartList.getIlGoodsId()+"]" );
				} else {

					deliveryType = shoppingCartBiz.getDeliveryTypeBySaleType(goods.getSaleType());

					// 상품 판매타입이 정기 -> 배송타입 일반으로 수정
					if(deliveryType == ShoppingEnums.DeliveryType.REGULAR){
						deliveryType = ShoppingEnums.DeliveryType.NORMAL;
					}

					// 증정품,증정(식품마케팅)일 경우 -> 배송타입 일반
					if(GoodsEnums.GoodsType.GIFT.getCode().equals(goods.getGoodsType()) || GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(goods.getGoodsType())){
						deliveryType = ShoppingEnums.DeliveryType.NORMAL;
					}

					// 최소 구매수량 체크 -> 구매수량보다 상품 최소 구매수량이 더 높을경우 최소 구매수량으로 수량 수정
					if(addCartList.getQty() < goods.getLimitMinimumCount()) {
//						lackLimitMinCntList.add(new CheckCartGoodsDto(goods.getIlGoodsId(), goods.getGoodsName(), goods.getLimitMinimumCount()));
//						if (resultCode.equals(BaseEnums.Default.SUCCESS)) {
//							resultCode = ShoppingEnums.AddCartInfo.LACK_LIMIT_MIN_CNT;
//							messageList.add("[" + addCartList.getIlGoodsId() + "]" + resultCode.getMessage());
//						}
						buyQty = goods.getLimitMinimumCount();
					}

					// 상품 재고 정보
					int stock = 0;
					// 묶음상품일 경우 분기처리
					if (goods.getGoodsType().equals(GoodsEnums.GoodsType.PACKAGE.getCode())) {
						List<PackageGoodsListDto> goodsPackageList = goodsGoodsBiz.getPackagGoodsInfoList(
								goods.getIlGoodsId(), isMember, ORDER_CREATE_IS_EMPLOYEE, ORDER_CREATE_IS_DAWN_DELIVERY, arrivalScheduledDate, buyQty);
						RecalculationPackageDto recalculationPackage = goodsGoodsBiz.getRecalculationPackage(goods.getSaleStatus(), goodsPackageList);
						stock = recalculationPackage.getStockQty();
						goods.setSaleStatus(recalculationPackage.getSaleStatus());
					} else {
						stock = goods.getStockQty();
					}

					// 일일상품일 경우
					if (GoodsEnums.SaleType.DAILY.getCode().equals(goods.getSaleType())) {

						// 일일상품 정보 조회
						List<GoodsDailyCycleDto> goodsDailyCycleList = goodsGoodsBiz.getGoodsDailyCycleList(addCartList.getIlGoodsId(), goods.getGoodsDailyType(), orderCreateCartRequestDto.getReceiverZipCode(), orderCreateCartRequestDto.getBuildingCode());
						goodsDailyCycle = goodsDailyCycleList.get(0);
					}

					// 상품이 판매중이 아닐때
					if (!GoodsEnums.SaleStatus.ON_SALE.getCode().equals(goods.getSaleStatus())) {
						noGoodsStatusOnSalelist.add(new CheckCartGoodsDto(goods.getIlGoodsId(), goods.getGoodsName(), 0));
						if (resultCode.equals(BaseEnums.Default.SUCCESS)) {
							resultCode = ShoppingEnums.AddCartInfo.ORDER_CREATE_NO_ADD;
							messageList.add("[" + addCartList.getIlGoodsId() + "] " + GoodsEnums.SaleStatus.findByCode(goods.getSaleStatus()).getCodeName() + resultCode.getMessage());
						}
					}


					// 최대 구매수량 체크
					if ("Y".equals(goods.getLimitMaximumCountYn())) {
						int systemLimitMaxCnt = goods.getLimitMaximumCount();
						int orderGoodsBuyQty = orderOrderBiz.getOrderGoodsBuyQty(goods.getIlGoodsId(), String.valueOf(orderCreateCartRequestDto.getUrUserId()));
						int limitMaxCnt = systemLimitMaxCnt - orderGoodsBuyQty;
						if (addCartList.getQty() > limitMaxCnt) {
							overLimitMaxCntList.add(new CheckCartGoodsDto(goods.getIlGoodsId(), goods.getGoodsName(), limitMaxCnt));
							if (resultCode.equals(BaseEnums.Default.SUCCESS)) {
								resultCode = ShoppingEnums.AddCartInfo.OVER_LIMIT_MAX_CNT;
								messageList.add("[" + addCartList.getIlGoodsId() + "]" + resultCode.getMessage());
							}
						}
					}

					// 상품 재고가 구매 수량보다 작을경우
					if (stock < addCartList.getQty()) {
						goodsLackStockList.add(new CheckCartGoodsDto(goods.getIlGoodsId(), goods.getGoodsName(), stock));
						if (resultCode.equals(BaseEnums.Default.SUCCESS)) {
							resultCode = ShoppingEnums.AddCartInfo.GOODS_LACK_STOCK;
							messageList.add("[" + addCartList.getIlGoodsId() + "]" + resultCode.getMessage());
						}
					}

					ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(goods.getIlGoodsId(),
							orderCreateCartRequestDto.getReceiverZipCode(), orderCreateCartRequestDto.getBuildingCode());
					if ("Y".equals(goods.getGoodsDailyBulkYn())
							&& goods.getSaleType().equals(GoodsEnums.SaleType.DAILY.getCode())) {
						// 판매타입이 일일배송상품이고 배송유형이 일괄배송이면서 택배배송 가능 권역일경우 일반 배송으로 처리
						if (shippingPossibilityStoreDeliveryAreaInfo == null ||
								StoreEnums.StoreApiDeliveryIntervalCode.PARCEL.getCommonCode().equals(shippingPossibilityStoreDeliveryAreaInfo.getStoreDeliveryIntervalType())) {
							deliveryType = ShoppingEnums.DeliveryType.NORMAL;
						}
					}

					if (resultCode.equals(BaseEnums.Default.SUCCESS)) {
						// 장바구니 등록 요청 DTO 세팅
						AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
						addCartInfoRequestDto.setUrPcidCd(ORDER_CREATE_UR_PCID_CD);
						addCartInfoRequestDto.setIlGoodsId(addCartList.getIlGoodsId());
						addCartInfoRequestDto.setQty(buyQty);
						addCartInfoRequestDto.setBuyNowYn("Y");
						addCartInfoRequestDto.setSaleType(goods.getSaleType());
						addCartInfoRequestDto.setGoodsDailyType(goods.getGoodsDailyType());
						addCartInfoRequestDto.setDeliveryType(deliveryType.getCode());
						addCartInfoRequestDto.setUrUserId(orderCreateCartRequestDto.getUrUserId());
						addCartInfoRequestDto.setGoodsGiftPossible(true);
						// 일일배송 -> 배송패턴 기본값 세팅
						if(ObjectUtils.isNotEmpty(goodsDailyCycle) && GoodsEnums.SaleType.DAILY.getCode().equals(goods.getSaleType())){
							addCartInfoRequestDto.setGoodsDailyCycleType(goodsDailyCycle.getGoodsDailyCycleType());
							addCartInfoRequestDto.setGoodsDailyCycleTermType(goodsDailyCycle.getTerm().get(0).get("goodsDailyCycleTermType"));

							// 녹즙
							if(GoodsEnums.GoodsDailyType.GREENJUICE.getCode().equals(goods.getGoodsDailyType())){
								String goodsDailyCycleGreenJuiceWeekType[] = new String[1];
								goodsDailyCycleGreenJuiceWeekType[0] = goodsDailyCycle.getWeek().get(0).get("goodsDailyCycleGreenJuiceWeekType");
								addCartInfoRequestDto.setGoodsDailyCycleGreenJuiceWeekType(goodsDailyCycleGreenJuiceWeekType);

								// 베이비밀
							}else if (GoodsEnums.GoodsDailyType.BABYMEAL.getCode().equals(goods.getGoodsDailyType())) {
								addCartInfoRequestDto.setGoodsDailyAllergyYn("N");
								if("Y".equals(goods.getGoodsDailyBulkYn())){
									addCartInfoRequestDto.setGoodsDailyBulkYn("Y");
									List<HashMap<String, String>> goodsDailyBulkList = goodsGoodsBiz.getGoodsDailyBulkList(goods.getIlGoodsId());
									addCartInfoRequestDto.setGoodsBulkType(goodsDailyBulkList.get(0).get("goodsBulkType"));
								}else{
									addCartInfoRequestDto.setGoodsDailyBulkYn("N");
								}
							}
						}

						// 장바구니 필수 옵션 체크

						if (!shoppingCartBiz.isValidationAddCartInfo(addCartInfoRequestDto)) {
							//return ApiResult.result(ShoppingEnums.AddCartInfo.ORDER_CREATE_FAIL_VALIDATION);
							if (resultCode.equals(BaseEnums.Default.SUCCESS)) {
								resultCode = ShoppingEnums.AddCartInfo.ORDER_CREATE_FAIL_VALIDATION;
								messageList.add("[" + addCartList.getIlGoodsId() + "]" + resultCode.getMessage());
							}
						} else {
							// 장바구니 등록
							addGoodsList.add(addCartInfoRequestDto);
						}
					}
				}
			}
		}

		//if (resultCode.equals(BaseEnums.Default.SUCCESS)) {
			List<Long> spCartIds = new ArrayList<Long>();
			for (AddCartInfoRequestDto addCartInfoRequestDto : addGoodsList) {
				Long spCartId = shoppingCartBiz.addCartInfo(addCartInfoRequestDto);
				spCartIds.add(spCartId);
			}

			// 주문생성  장바구니 정보 조회
			GetCartDataRequestDto getCartDataRequestDto = new GetCartDataRequestDto();
			getCartDataRequestDto.setUrUserId(orderCreateCartRequestDto.getUrUserId());
			getCartDataRequestDto.setBuildingCode(orderCreateCartRequestDto.getBuildingCode());
			getCartDataRequestDto.setReceiverZipCode(orderCreateCartRequestDto.getReceiverZipCode());
			getCartDataRequestDto.setCartType(orderCreateCartRequestDto.getCartType());


			//기존 상품정보 그리드 내 SP_CART_ID 같이 전달
			if(CollectionUtils.isNotEmpty(orderCreateCartRequestDto.getSpCartIdList())){
				spCartIds.addAll(orderCreateCartRequestDto.getSpCartIdList());
			}

			getCartDataRequestDto.setSpCartId(spCartIds);

			if (orderCreateCartRequestDto.getSpCartIdList() != null && orderCreateCartRequestDto.getSpCartIdList().size() > 0) {
				spCartIds.addAll(orderCreateCartRequestDto.getSpCartIdList());
			}

			// 장바구니 정보 조회
			if(CollectionUtils.isNotEmpty(spCartIds)){
				List<CartDeliveryDto> cartDeliveryDtoList = getCartData(getCartDataRequestDto);
				if(CollectionUtils.isNotEmpty(cartDeliveryDtoList)){
					List<CartShippingDto> cartShippingDtoList = shoppingCartBiz.getCartShippingList(cartDeliveryDtoList);
					List<CartGoodsDto> cartGoodsDtoList = shoppingCartBiz.getCartGoodsList(cartShippingDtoList);
					// 배송불가능 상품 존재여부
					boolean isExistShippingImpossible = cartGoodsDtoList.stream().anyMatch(f->!f.isShippingPossibility());
					// 도착예정일 없는 상품 존재여부
					boolean isNotExistDeliveryDt = cartShippingDtoList.stream().anyMatch(f->f.getArrivalScheduledDate() == null);
					if(isExistShippingImpossible || isNotExistDeliveryDt){

						boolean isWarehouseUndeliverableArea = cartGoodsDtoList.stream().anyMatch(f-> StringUtils.isNotEmpty(f.getShippingImpossibilityMsg()));
						if(isWarehouseUndeliverableArea){
							// 출고처 배송불가지역인 경우
							resultCode = ShoppingEnums.AddCartInfo.WAREHOUSE_UNDELIVERABLE_AREA;
						}else{
							resultCode = ShoppingEnums.AddCartInfo.EXIST_DELIVERY_NOT_POSSIBLE_GOODS;
						}
						messageList.add(resultCode.getMessage());
					}
				}
			}

			// 실패메세지
			List<String> failMessageList = messageList.stream()
				.distinct()
				.collect(Collectors.toList());

			OrderCreateAddGoodsResponseDto orderCreateAddGoodsResponseDto = new OrderCreateAddGoodsResponseDto();
			orderCreateAddGoodsResponseDto.setOrderCreateCartList(getCartInfo(getCartDataRequestDto));
			orderCreateAddGoodsResponseDto.setFailMessageList(failMessageList);

			return ApiResult.success(orderCreateAddGoodsResponseDto);
		/*
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
		 */
	}

	/**
     * 주문생성  장바구니 정보 조회
     *
     * @param getCartDataRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
	@Override
	public ApiResult<?> getCartInfo(GetCartDataRequestDto getCartDataRequestDto) throws Exception {
		List<CartDeliveryDto> cartDeliveryDtoList = null;
		String urUserId = StringUtil.nvl(getCartDataRequestDto.getUrUserId());
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		getCartDataRequestDto.setUrPcidCd(ORDER_CREATE_UR_PCID_CD);
		getCartDataRequestDto.setDeviceInfo(ORDER_CREATE_DIR_INFO);
		getCartDataRequestDto.setApp(ORDER_CREATE_IS_APP);
		getCartDataRequestDto.setMember(isMember);
		getCartDataRequestDto.setEmployee(ORDER_CREATE_IS_EMPLOYEE);
		getCartDataRequestDto.setUrErpEmployeeCode("");
		getCartDataRequestDto.setBridgeYn("Y");
		getCartDataRequestDto.setBosCreateOrder(true); // 판매대기상품, 관리자품절상품도 가능하게 하기위해

		List<OrderCreateCartDto> orderCreateCartList = new ArrayList<OrderCreateCartDto>();

		// 장바구니 조회
		if (CollectionUtils.isEmpty(getCartDataRequestDto.getSpCartId())) return ApiResult.success(orderCreateCartList);
		cartDeliveryDtoList = shoppingCartBiz.getCartData(getCartDataRequestDto);

		cartDeliveryDtoList.forEach(cartDeliveryDto -> {
			cartDeliveryDto.getShipping().stream().filter(f->f.getArrivalScheduledDate()!= null).forEach(cartShippingDto -> {
				cartShippingDto.getGoods().stream().filter(f->f.isShippingPossibility()).forEach(cartGoodsDto -> {
					OrderCreateCartDto orderCreateCartDto = OBJECT_MAPPER.convertValue(cartGoodsDto, OrderCreateCartDto.class);
					orderCreateCartDto.setDeliveryType(cartDeliveryDto.getDeliveryType());
					orderCreateCartDto.setShippingIndex(cartShippingDto.getShippingIndex());
					orderCreateCartDto.setShippingRecommendedPrice(cartShippingDto.getShippingRecommendedPrice());
					orderCreateCartDto.setChoiceArrivalScheduledDateList(cartShippingDto.getChoiceArrivalScheduledDateList());
					orderCreateCartDto.setOrderIfDt(cartShippingDto.getOrderDate()); // 주문일자 (주문 I/F 일자)
					orderCreateCartDto.setShippingDt(cartShippingDto.getForwardingScheduledDate()); // 출고 예정 일자
					orderCreateCartDto.setUrWarehouseId(cartShippingDto.getUrWarehouseId());
					orderCreateCartDto.setWarehouseNm(cartShippingDto.getWarehouseNm());
					orderCreateCartDto.setGoodsType(cartGoodsDto.getGoodsType());
					orderCreateCartDto.setGoodsNm(cartGoodsDto.getGoodsName());
					orderCreateCartDto.setDawnDeliveryYn(cartShippingDto.getDawnDeliveryYn()); // 새벽배송여부
					LocalDate deliveryDt = cartShippingDto.getArrivalScheduledDate();
					if("Y".equals(cartShippingDto.getDawnDeliveryYn())){
						deliveryDt = cartShippingDto.getDawnArrivalScheduledDate();
					}
					orderCreateCartDto.setDeliveryDt(deliveryDt); // 도착예정일자
					orderCreateCartDto.setRecommendedPrice(cartGoodsDto.getRecommendedPrice());
					orderCreateCartDto.setSalePrice(cartGoodsDto.getSalePrice());
					//일일배송
					if(GoodsEnums.GoodsType.DAILY.getCode().equals(cartGoodsDto.getGoodsType())){
						orderCreateCartDto.setGoodsDailyType(cartGoodsDto.getGoodsDailyType());
						orderCreateCartDto.setGoodsDailyCycleType(cartGoodsDto.getGoodsDailyCycleType());
						orderCreateCartDto.setGoodsDailyCycleTypeName(cartGoodsDto.getGoodsDailyCycleTypeName());
						orderCreateCartDto.setGoodsDailyCycleTermType(cartGoodsDto.getGoodsDailyCycleTermType());
						orderCreateCartDto.setGoodsDailyCycleTermTypeName(cartGoodsDto.getGoodsDailyCycleTermTypeName());
						orderCreateCartDto.setGoodsDailyCycleGreenJuiceWeekType(cartGoodsDto.getGoodsDailyCycleGreenJuiceWeekType());
						orderCreateCartDto.setGoodsDailyCycleGreenJuiceWeekTypeName(cartGoodsDto.getGoodsDailyCycleGreenJuiceWeekTypeName());
						orderCreateCartDto.setGoodsDailyWeekText(GoodsEnums.GoodsCycleType.findByCode(cartGoodsDto.getGoodsDailyCycleType()).getWeekText());
						orderCreateCartDto.setGoodsDailyAllergyYn(cartGoodsDto.getGoodsDailyAllergyYn());
						orderCreateCartDto.setGoodsDailyBulkYn(cartGoodsDto.getGoodsDailyBulkYn());
						orderCreateCartDto.setGoodsBulkType(cartGoodsDto.getGoodsBulkType());
						orderCreateCartDto.setGoodsBulkTypeName(cartGoodsDto.getGoodsBulkTypeName());
						ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = null;
						try{
							shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(cartGoodsDto.getIlGoodsId(),
									getCartDataRequestDto.getReceiverZipCode(), getCartDataRequestDto.getBuildingCode());
							if(shippingPossibilityStoreDeliveryAreaInfo != null)
								orderCreateCartDto.setStoreDeliveryIntervalType(shippingPossibilityStoreDeliveryAreaInfo.getStoreDeliveryIntervalType());
						}catch(Exception e){
							log.info(e.getMessage());
						}

					}

					// 재고
					ItemWarehouseVo itemWarehouseVo = orderCreateService.getIlItemWarehouseIdByIlGoodsId(cartGoodsDto.getIlGoodsId());
					if(ObjectUtils.isNotEmpty(itemWarehouseVo)){
						orderCreateCartDto.setIlItemWarehouseId(Long.parseLong(itemWarehouseVo.getIlItemWarehouseId()));
						orderCreateCartDto.setUnlimitStockYn(itemWarehouseVo.isUnlimitStockYn());
						orderCreateCartDto.setNotIfStockCnt(itemWarehouseVo.getNotIfStockCnt());
					}

					orderCreateCartDto.setAdditionalGoodsExistYn("N");
					// 구매가능한 추가구성상품 존재 여부
					try{
						List<AdditionalGoodsDto> additionalGoodsList = goodsGoodsBiz.getAdditionalGoodsInfoList(cartGoodsDto.getIlGoodsId(),true,false,false,null);
						if(CollectionUtils.isNotEmpty(additionalGoodsList)){
							orderCreateCartDto.setAdditionalGoodsExistYn("Y");
						}
					}catch(Exception e){
						e.printStackTrace();
					}

					// 추가상품
					if(CollectionUtils.isNotEmpty(cartGoodsDto.getAdditionalGoods())){
						for(CartAdditionalGoodsDto dto : cartGoodsDto.getAdditionalGoods()){
							ItemWarehouseVo additionalItemWarehouseVo = orderCreateService.getIlItemWarehouseIdByIlGoodsId(dto.getIlGoodsId());
							if(ObjectUtils.isNotEmpty(additionalItemWarehouseVo)){
								dto.setIlItemWarehouseId(Long.parseLong(additionalItemWarehouseVo.getIlItemWarehouseId()));
								dto.setUnlimitStockYn(additionalItemWarehouseVo.isUnlimitStockYn());
								dto.setNotIfStockCnt(additionalItemWarehouseVo.getNotIfStockCnt());
							}
						}
						orderCreateCartDto.setAdditionalGoods(cartGoodsDto.getAdditionalGoods());
					}

					orderCreateCartList.add(orderCreateCartDto);
				});
			});
		});


		return ApiResult.success(orderCreateCartList);
	}

	/**
     * 주문생성 장바구니 정보(구매수량) 수정
     *
     * @param orderCreateCartRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
	@Override
	public ApiResult<?> putCartInfo(OrderCreateCartRequestDto orderCreateCartRequestDto) throws Exception {

		boolean isMember = StringUtil.isNotEmpty(orderCreateCartRequestDto.getUrUserId());
		SpCartVo cartVo = shoppingCartBiz.getCart(orderCreateCartRequestDto.getSpCartId());
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate arrivalScheduledDate = LocalDate.parse(orderCreateCartRequestDto.getDeliveryDt(), dateFormatter);
		String deliveryType = cartVo.getDeliveryType();

		GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder().ilGoodsId(cartVo.getIlGoodsId())
				.deviceInfo(ORDER_CREATE_DIR_INFO).isApp(ORDER_CREATE_IS_APP).isMember(isMember).isEmployee(ORDER_CREATE_IS_EMPLOYEE).isBosCreateOrder(true).build();

		// 상품 기본정보
		BasicSelectGoodsVo goods = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
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
						isMember, ORDER_CREATE_IS_EMPLOYEE, false, arrivalScheduledDate, orderCreateCartRequestDto.getQty());
				RecalculationPackageDto recalculationPackage = goodsGoodsBiz.getRecalculationPackage(goods.getSaleStatus(), goodsPackageList);
				stock = recalculationPackage.getStockQty();

			// 골라담기상품일 경우
			} else if (ShoppingEnums.CartPromotionType.EXHIBIT_SELECT.getCode().equals(goodsDto.getCartPromotionType())
					|| ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(goodsDto.getCartPromotionType())) {

				List<CartPickGoodsDto> pickGoodsDtoList = shoppingCartBiz.getCartPickGoodsList(goodsDto.getSpCartId(),
						isMember, ORDER_CREATE_IS_EMPLOYEE, false, null, orderCreateCartRequestDto.getQty(), null, null);
				RecalculationPackageDto recalculatonPackage = shoppingCartBiz.getRecalculationCartPickGoodsList(goodsDto.getSaleStatus(), pickGoodsDtoList);
				stock = recalculatonPackage.getStockQty();
			} else {
				stock = goods.getStockQty();

				// 도착예정일에 맞는 재고 조회
				List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList = goods.getArrivalScheduledDateDtoList();
				if(CollectionUtils.isNotEmpty(arrivalScheduledDateDtoList)){
					ArrivalScheduledDateDto arrivalScheduledDateDto = arrivalScheduledDateDtoList.stream().filter(f-> f.getArrivalScheduledDate().equals(arrivalScheduledDate)).findAny().orElse(null);
					if(arrivalScheduledDateDto != null){
						stock = arrivalScheduledDateDto.getStock();
					}
				}
			}

			if ("Y".equals(goods.getGoodsDailyBulkYn())
					&& goods.getSaleType().equals(GoodsEnums.SaleType.DAILY.getCode())) {
				ShippingPossibilityStoreDeliveryAreaDto shippingPossibilityStoreDeliveryAreaInfo = storeDeliveryBiz.getShippingPossibilityStoreDeliveryAreaInfo(goods.getIlGoodsId(),
						orderCreateCartRequestDto.getReceiverZipCode(), orderCreateCartRequestDto.getBuildingCode());
				// 판매타입이 일일배송상품이고 배송유형이 일괄배송이면서 택배배송 가능 권역일경우 일반 배송으로 처리
				if (shippingPossibilityStoreDeliveryAreaInfo == null ||
						StoreEnums.StoreApiDeliveryIntervalCode.PARCEL.getCommonCode().equals(shippingPossibilityStoreDeliveryAreaInfo.getStoreDeliveryIntervalType())) {
					deliveryType = ShoppingEnums.DeliveryType.NORMAL.getCode();
				}
			}



			if (GoodsEnums.SaleType.RESERVATION.getCode().equals(goods.getSaleType())) {
				/* 예약상품 케이스 */
//				// 예약상품 정보 조회 후 스케줄 처리
//				GoodsReserveOptionVo goodsReserveOptionVo = goodsGoodsBiz.getGoodsReserveOption(putCartRequestDto.getIlGoodsReserveOptionId());
//				if (goodsReserveOptionVo == null) {
//					return ApiResult.result(ShoppingEnums.AddCartInfo.NO_GOODS_STATUS_ON_SALE);
//				} else {
//					LocalDateTime currentDateTime = LocalDateTime.now();
//					if (!(goodsReserveOptionVo.getReserveStartDate().compareTo(currentDateTime) <= 0
//							&& currentDateTime
//									.compareTo(goodsReserveOptionVo.getReserveEndDate()) <= 0)) {
//						// 회차가 기간이 지나면 판매 중지 상태 변경
//						return ApiResult.result(ShoppingEnums.AddCartInfo.NO_GOODS_STATUS_ON_SALE);
//					} else if (!(goodsReserveOptionVo.getStockQty() > 0)) {
//						// 회차의 수량이 없을때 일시 품절
//						return ApiResult.result(ShoppingEnums.AddCartInfo.GOODS_LACK_STOCK);
//					} else if (goodsReserveOptionVo.getStockQty() < putCartRequestDto.getQty()) {
//						// 회차 수량보다 더 많이 담았을 경우
//						return ApiResult.result(ShoppingEnums.AddCartInfo.GOODS_LACK_STOCK);
//					}
//				}
			} else {
				// 상품 재고가 구매 수량보다 작을경우
				if (stock < orderCreateCartRequestDto.getQty()) {
					return ApiResult.result(ShoppingEnums.AddCartInfo.GOODS_LACK_STOCK);
				}
			}

			// 추가 구성 상품 세팅
			ApiResult<?> additionalGoodsResult = orderCreateService.getPutCartInfoAdditionalGoods(orderCreateCartRequestDto.getSpCartId(), orderCreateCartRequestDto.getAddGoodsList(),isMember);
			if(additionalGoodsResult.getCode().equals(BaseEnums.Default.SUCCESS.getCode())){
				addCartInfoRequestDto.setAdditionalGoodsList((List<AddCartInfoAdditionalGoodsRequestDto>)additionalGoodsResult.getData());
			}else{
				return additionalGoodsResult;
			}

			addCartInfoRequestDto.setUrPcidCd(ORDER_CREATE_UR_PCID_CD);
			if (isMember) {
				addCartInfoRequestDto.setUrUserId(orderCreateCartRequestDto.getUrUserId());
			}
			addCartInfoRequestDto.setSaleType(goods.getSaleType());
			addCartInfoRequestDto.setGoodsDailyType(goods.getGoodsDailyType());
			addCartInfoRequestDto.setDeliveryType(deliveryType);
			addCartInfoRequestDto.setIlGoodsId(cartVo.getIlGoodsId());
			addCartInfoRequestDto.setQty(orderCreateCartRequestDto.getQty());
//			addCartInfoRequestDto.setIlGoodsReserveOptionId(putCartRequestDto.getIlGoodsReserveOptionId());
			addCartInfoRequestDto.setBuyNowYn("Y");
			addCartInfoRequestDto.setGoodsDailyCycleType(orderCreateCartRequestDto.getGoodsDailyCycleType());
			addCartInfoRequestDto.setGoodsDailyCycleTermType(orderCreateCartRequestDto.getGoodsDailyCycleTermType());
			addCartInfoRequestDto.setGoodsDailyCycleGreenJuiceWeekType(orderCreateCartRequestDto.getGoodsDailyCycleGreenJuiceWeekType());
			addCartInfoRequestDto.setGoodsDailyAllergyYn(orderCreateCartRequestDto.getGoodsDailyAllergyYn());
			addCartInfoRequestDto.setGoodsDailyBulkYn(orderCreateCartRequestDto.getGoodsDailyBulkYn());
			addCartInfoRequestDto.setGoodsBulkType(orderCreateCartRequestDto.getGoodsBulkType());

			// 장바구니 필수 옵션 체크
			if (!shoppingCartBiz.isValidationAddCartInfo(addCartInfoRequestDto)) {
				return ApiResult.result(ShoppingEnums.AddCartInfo.FAIL_VALIDATION);
			}

			PutCartRequestDto putCartRequestDto = new PutCartRequestDto();
			putCartRequestDto.setSpCartId(orderCreateCartRequestDto.getSpCartId());
			putCartRequestDto.setQty(orderCreateCartRequestDto.getQty());
			putCartRequestDto.setUrPcidCd(ORDER_CREATE_UR_PCID_CD);

			if (isMember) {
				putCartRequestDto.setUrUserId(orderCreateCartRequestDto.getUrUserId());
			}
			putCartRequestDto.setIlGoodsId(cartVo.getIlGoodsId());
			putCartRequestDto.setDeliveryType(deliveryType);
			putCartRequestDto.setSaleType(goods.getSaleType());
			putCartRequestDto.setGoodsDailyCycleType(orderCreateCartRequestDto.getGoodsDailyCycleType());
			putCartRequestDto.setGoodsDailyCycleTermType(orderCreateCartRequestDto.getGoodsDailyCycleTermType());
			putCartRequestDto.setGoodsDailyCycleGreenJuiceWeekType(orderCreateCartRequestDto.getGoodsDailyCycleGreenJuiceWeekType());
			putCartRequestDto.setGoodsDailyAllergyYn(orderCreateCartRequestDto.getGoodsDailyAllergyYn());
			putCartRequestDto.setGoodsDailyBulkYn(orderCreateCartRequestDto.getGoodsDailyBulkYn());
			putCartRequestDto.setGoodsBulkType(orderCreateCartRequestDto.getGoodsBulkType());
			if(CollectionUtils.isNotEmpty(addCartInfoRequestDto.getAdditionalGoodsList())){
				putCartRequestDto.setAdditionalGoodsList(addCartInfoRequestDto.getAdditionalGoodsList());
			}

			// 장바구니 수정
			shoppingCartBiz.putCart(putCartRequestDto);

			// 주문생성  장바구니 정보 조회
			GetCartDataRequestDto getCartDataRequestDto = new GetCartDataRequestDto();
			getCartDataRequestDto.setUrUserId(orderCreateCartRequestDto.getUrUserId());
			getCartDataRequestDto.setBuildingCode(orderCreateCartRequestDto.getBuildingCode());
			getCartDataRequestDto.setCartType(orderCreateCartRequestDto.getCartType());
			getCartDataRequestDto.setSpCartId(orderCreateCartRequestDto.getSpCartIdList());
			return ApiResult.success(getCartInfo(getCartDataRequestDto));
		}
	}

	/**
     * 주문생성 사용가능 쿠폰 조회
     *
     * @param orderCreateCouponRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
	@SuppressWarnings("resource")
	@Override
	public ApiResult<?> getCouponPageInfo(OrderCreateCouponRequestDto orderCreateCouponRequestDto) throws Exception {
		// 임직원가 적용시 쿠폰 이용 불가
		if ("Y".equals(ORDER_CREATE_IS_EMPLOYEE ? "Y" : "N")) {
			return ApiResult.result(ShoppingEnums.GetCouponPageInfo.EMPLOYEE_NOT_ALLOW);
		}


		// 장바구니 정보 조회
		GetCartDataRequestDto cartDataRequestDto = new GetCartDataRequestDto();

		String urUserId = StringUtil.nvl(orderCreateCouponRequestDto.getUrUserId());
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(ORDER_CREATE_IS_EMPLOYEE);
		cartDataRequestDto.setUrPcidCd(ORDER_CREATE_UR_PCID_CD);
		if (isMember) {
			cartDataRequestDto.setUrUserId(new Long(urUserId));
		}

		cartDataRequestDto.setDeviceInfo(ORDER_CREATE_DIR_INFO);
		cartDataRequestDto.setApp(ORDER_CREATE_IS_APP);
		cartDataRequestDto.setMember(isMember);
		cartDataRequestDto.setEmployee(isEmployee);
		cartDataRequestDto.setReceiverZipCode(orderCreateCouponRequestDto.getReceiverZipCode());
		cartDataRequestDto.setBuildingCode(orderCreateCouponRequestDto.getBuildingCode());


		cartDataRequestDto.setCartType(ORDER_CREATE_CART_TYPE);
		cartDataRequestDto.setSpCartId(orderCreateCouponRequestDto.getSpCartIdList());
		cartDataRequestDto.setEmployeeYn(ORDER_CREATE_IS_EMPLOYEE ? "Y" : "N");
		cartDataRequestDto.setBosCreateOrder(true);

		if (orderCreateCouponRequestDto.getArrivalScheduledList() != null && orderCreateCouponRequestDto.getArrivalScheduledList().size() > 0) {
			List<OrderCreateScheduledDto> list = orderCreateCouponRequestDto.getArrivalScheduledList();

			List<ChangeArrivalScheduledDto> arrivalScheduled = new ArrayList<ChangeArrivalScheduledDto>();
			for (OrderCreateScheduledDto orderCreateScheduledDto: list) {
				ChangeArrivalScheduledDto changeArrivalScheduledDto = new ChangeArrivalScheduledDto();
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate endDate = LocalDate.parse(orderCreateScheduledDto.getArrivalScheduledDate(), dateFormatter);
				changeArrivalScheduledDto.setArrivalScheduledDate(endDate);
				changeArrivalScheduledDto.setDawnDeliveryYn(orderCreateScheduledDto.getDawnDeliveryYn());
				arrivalScheduled.add(changeArrivalScheduledDto);
			}
			cartDataRequestDto.setArrivalScheduled(arrivalScheduled);
		}
		// 장바구니 정보 조회
		List<CartDeliveryDto> cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);
		// cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
		List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);
		// List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
		List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);
		// 접근 디바이스 타입 정보
		DeviceType deviceType = GoodsEnums.DeviceType.PC;

		// 상품 쿠폰 리스트 조회
		List<GoodsCouponDto> goodsList = promotionCouponBiz.getGoodsCouponApplicationListByUser(new Long(urUserId), cartGoodsList, deviceType);
		// 장바구니 쿠폰 리스트 조회
		List<CouponDto> cartCouponList = promotionCouponBiz.getCartCouponApplicationListByUser(new Long(urUserId), cartGoodsList, deviceType);
		// 배송비 쿠폰 리스트 조회
		List<ShippingCouponDto> shippingCouponList = promotionCouponBiz
				.getShippingCouponApplicationListByUser(new Long(urUserId), cartDataDto, deviceType);
		// 사용 가능 쿠폰 개수
		int availableCouponCnt = 0;

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
			availableCouponCnt = Long.valueOf(useCouponlistStream.filter(dto -> dto.isActive() == true).mapToLong(CouponDto::getPmCouponIssueId).distinct().count()).intValue();
		}
		GetCouponPageInfoResponseDto resDto = new GetCouponPageInfoResponseDto();
		resDto.setGoodsList(goodsList);
		resDto.setShippingCoupon(shippingCouponList);
		resDto.setCartCouponList(cartCouponList);
		resDto.setAvailableCouponCnt(availableCouponCnt);
		return ApiResult.success(resDto);
	}

	/**
     * 주문생성 사용가능 장바구니 쿠폰 조회
     *
     * @param orderCreateCouponRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
	@Override
	public ApiResult<?> getCartCouponList(OrderCreateCouponRequestDto orderCreateCouponRequestDto) throws Exception {
		// 임직원가 적용시 쿠폰 이용 불가
		if ("Y".equals(ORDER_CREATE_IS_EMPLOYEE ? "Y" : "N")) {
			return ApiResult.result(ShoppingEnums.GetCouponPageInfo.EMPLOYEE_NOT_ALLOW);
		}

		// 장바구니 정보 조회
		GetCartDataRequestDto cartDataRequestDto = new GetCartDataRequestDto();
		String urUserId = StringUtil.nvl(orderCreateCouponRequestDto.getUrUserId());
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = StringUtil.isNotEmpty(ORDER_CREATE_IS_EMPLOYEE);
		cartDataRequestDto.setUrPcidCd(ORDER_CREATE_UR_PCID_CD);
		if (isMember) {
			cartDataRequestDto.setUrUserId(new Long(urUserId));
		}
		cartDataRequestDto.setDeviceInfo(ORDER_CREATE_DIR_INFO);
		cartDataRequestDto.setApp(ORDER_CREATE_IS_APP);
		cartDataRequestDto.setMember(isMember);
		cartDataRequestDto.setEmployee(isEmployee);
		cartDataRequestDto.setReceiverZipCode(orderCreateCouponRequestDto.getReceiverZipCode());
		cartDataRequestDto.setBuildingCode(orderCreateCouponRequestDto.getBuildingCode());

		cartDataRequestDto.setCartType(ORDER_CREATE_CART_TYPE);
		cartDataRequestDto.setSpCartId(orderCreateCouponRequestDto.getSpCartIdList());
		cartDataRequestDto.setEmployeeYn(ORDER_CREATE_IS_EMPLOYEE ? "Y" : "N");
		cartDataRequestDto.setUseGoodsCoupon(orderCreateCouponRequestDto.getUseGoodsCouponList());
		cartDataRequestDto.setBosCreateOrder(true);

		if (orderCreateCouponRequestDto.getArrivalScheduledList() != null && orderCreateCouponRequestDto.getArrivalScheduledList().size() > 0) {
			List<OrderCreateScheduledDto> list = orderCreateCouponRequestDto.getArrivalScheduledList();

			List<ChangeArrivalScheduledDto> arrivalScheduled = new ArrayList<ChangeArrivalScheduledDto>();

			for (OrderCreateScheduledDto orderCreateScheduledDto: list) {
				ChangeArrivalScheduledDto changeArrivalScheduledDto = new ChangeArrivalScheduledDto();
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate endDate = LocalDate.parse(orderCreateScheduledDto.getArrivalScheduledDate(), dateFormatter);
				changeArrivalScheduledDto.setArrivalScheduledDate(endDate);
				changeArrivalScheduledDto.setDawnDeliveryYn(orderCreateScheduledDto.getDawnDeliveryYn());
				arrivalScheduled.add(changeArrivalScheduledDto);
			}
			cartDataRequestDto.setArrivalScheduled(arrivalScheduled);
		}

		// 장바구니 정보 조회
		List<CartDeliveryDto> cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);

		// cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
		List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);

		// List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
		List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);

		// 접근 디바이스 타입 정보
		DeviceType deviceType = GoodsEnums.DeviceType.PC;

		// 장바구니 쿠폰 리스트 조회
		List<CouponDto> cartCouponList = promotionCouponBiz.getCartCouponApplicationListByUser(new Long(urUserId),
				cartGoodsList, deviceType);

		return ApiResult.success(cartCouponList);
	}

	/**
     * 주문생성 결제요청
     *
     * @param orderCreatePaymentRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	@Override
	public ApiResult<?> createOrderPayment(OrderCreatePaymentRequestDto orderCreatePaymentRequestDto) throws Exception {

		String urUserId = StringUtil.nvl(orderCreatePaymentRequestDto.getUrUserId());
		Long longUrUserId = null;
		if (!"".equals(urUserId)) {
			longUrUserId = new Long(urUserId);
		}
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		boolean isEmployee = ORDER_CREATE_IS_EMPLOYEE;
//		GetSessionUserCertificationResponseDto nonMemberCertificationDto = null;
//		RegularPaymentKeyVo regularPaymentVo = null;
//		List<CartGiftDto> gift = new ArrayList<>();

		// 접근 디바이스 타입 정보
		DeviceType deviceType = GoodsEnums.DeviceType.PC;
		AgentType agentType = SystemEnums.AgentType.PC;

//		GetSessionShippingResponseDto sessionShippingDto = userCertificationBiz.getSessionShipping();
		// 정기 배송일때 기존 주문건이 있으면 주소지를 기존 주문건 유지해야함
//		if (ShoppingEnums.CartType.REGULAR.getCode().equals(ORDER_CREATE_CART_TYPE)) {
//			CartRegularShippingDto cartRegularShippingDto = orderRegularBiz.getRegularInfoByCart(longUrUserId);
//			if (cartRegularShippingDto.isAdditionalOrder()) {
//				sessionShippingDto = orderRegularBiz.getRegularShippingZone(cartRegularShippingDto.getOdRegularReqId());
//			}
//		}

		if (StringUtil.isEmpty(orderCreatePaymentRequestDto.getReceiverZipCode()) && StringUtil.isEmpty(orderCreatePaymentRequestDto.getBuildingCode())) {
			// 세션 없음
			return ApiResult.result(ApplyPayment.EMPTY_SESSION);
		} else if (StringUtil.isEmpty(orderCreatePaymentRequestDto.getReceiverZipCode()) || StringUtil.isEmpty(orderCreatePaymentRequestDto.getBuildingCode())) {
			// 배송지 정보 없음
			return ApiResult.result(ApplyPayment.REQUIRED_SHIPPING_ZONE);
		}

		// 사용 적립금 검증
		if (isMember) {
			// 사용가능 적립금 조회
			int availablePoint = promotionPointBiz.getPointUsable(longUrUserId);
			if (availablePoint < orderCreatePaymentRequestDto.getUsePoint()) {
				return ApiResult.result(ApplyPayment.ORVER_AVAILABLE_POINT);
			}

//			// 정기 결제시
//			if (CartType.REGULAR.getCode().equals(ORDER_CREATE_CART_TYPE)) {
//				// 정기배송 결제 정보 등록 체크
//				regularPaymentVo = orderRegularBiz.getRegularPaymentKey(longUrUserId);
//				if ("".equals(StringUtil.nvl(regularPaymentVo.getBatchKey()))) {
//					return ApiResult.result(ApplyPayment.REQUIRED_REGULAR_PAYMENT);
//				}
//				// 정기배송 정보 체크
//				if ("".equals(StringUtil.nvl(orderCreatePaymentRequestDto.getRegularShippingCycleTermType()))
//						|| "".equals(StringUtil.nvl(orderCreatePaymentRequestDto.getRegularShippingCycleType()))
//						|| "".equals(StringUtil.nvl(orderCreatePaymentRequestDto.getRegularShippingArrivalScheduledDate()))) {
//					return ApiResult.result(ApplyPayment.REQUIRED_REGULAR_INFO);
//				}
//			}

			// 쿠폰 검증
			List<Long> couponPmCouponIssueIds = new ArrayList<Long>();
			if (orderCreatePaymentRequestDto.getUseGoodsCouponList() != null && orderCreatePaymentRequestDto.getUseGoodsCouponList().size() > 0) {
				for (UseGoodsCouponDto useGoodsCouponDto : orderCreatePaymentRequestDto.getUseGoodsCouponList()) {
					couponPmCouponIssueIds.add(useGoodsCouponDto.getPmCouponIssueId());
				}
			}
			if (orderCreatePaymentRequestDto.getUseShippingCouponList() != null && orderCreatePaymentRequestDto.getUseShippingCouponList().size() > 0) {
				for (UseShippingCouponDto useShippingCouponDto : orderCreatePaymentRequestDto.getUseShippingCouponList()) {
					couponPmCouponIssueIds.add(useShippingCouponDto.getPmCouponIssueId());
				}
			}
			if (orderCreatePaymentRequestDto.getUseCartPmCouponIssueId() != null) {
				couponPmCouponIssueIds.add(orderCreatePaymentRequestDto.getUseCartPmCouponIssueId());
			}
			if (couponPmCouponIssueIds.size() > 0) {
				Set<Long> uniqueCouponPmCouponIssueIds = new HashSet<Long>(couponPmCouponIssueIds);
				if (uniqueCouponPmCouponIssueIds.size() != couponPmCouponIssueIds.size()) {
					return ApiResult.result(ApplyPayment.USE_DUPLICATE_COUPON);
				}
			}
		} else {
			// 적립금 사용 체크
			if (orderCreatePaymentRequestDto.getUsePoint() > 0) {
				return ApiResult.result(ApplyPayment.NON_MEMBER_NOT_POINT);
			}

			// 본인인증 체크
//			nonMemberCertificationDto = userCertificationBiz.getSessionUserCertification();
//			if ("".equals(StringUtil.nvl(nonMemberCertificationDto.getCi()))) {
//				return ApiResult.result(ApplyPayment.REQUIRED_NON_MEMBER_CERTIFICATION);
//			}

			// 이메일 정보 체크
			if ("".equals(StringUtil.nvl(orderCreatePaymentRequestDto.getUserEmail()))) {
				return ApiResult.result(ApplyPayment.REQUIRED_NON_MEMBER_EMAIL);
			}

//			// 정기 결제 체크
//			if (CartType.REGULAR.getCode().equals(orderCreatePaymentRequestDto.getCartType())) {
//				return ApiResult.result(ApplyPayment.NON_MEMBER_NOT_REGULAR);
//			}
		}

		// 가상계좌 일때
		if (PaymentType.VIRTUAL_BANK.getCode().equals(orderCreatePaymentRequestDto.getPsPayCd())) {
			if ("".equals(StringUtil.nvl(orderCreatePaymentRequestDto.getBankCode())) || "".equals(StringUtil.nvl(orderCreatePaymentRequestDto.getAccountNumber()))
					|| "".equals(StringUtil.nvl(orderCreatePaymentRequestDto.getHolderName()))) {
				//return ApiResult.result(ApplyPayment.REQUIRED_REFUND_ACCOUNT);
			}
		}

		// 장바구니 데이터 조회
		GetCartDataRequestDto cartDataRequestDto = new GetCartDataRequestDto();
		cartDataRequestDto.setCartType(ORDER_CREATE_CART_TYPE);
		cartDataRequestDto.setSpCartId(orderCreatePaymentRequestDto.getSpCartIdList());
		cartDataRequestDto.setEmployeeYn(ORDER_CREATE_IS_EMPLOYEE ? "Y" : "N");
		cartDataRequestDto.setUseGoodsCoupon(orderCreatePaymentRequestDto.getUseGoodsCouponList());
		cartDataRequestDto.setUseShippingCoupon(orderCreatePaymentRequestDto.getUseShippingCouponList());
		cartDataRequestDto.setUrPcidCd(ORDER_CREATE_UR_PCID_CD);
		if (isMember) {
			cartDataRequestDto.setUrUserId(new Long(urUserId));
		}

		cartDataRequestDto.setDeviceInfo(ORDER_CREATE_DIR_INFO);
		cartDataRequestDto.setApp(ORDER_CREATE_IS_APP);
		cartDataRequestDto.setMember(isMember);
		cartDataRequestDto.setEmployee(isEmployee);
		cartDataRequestDto.setReceiverZipCode(orderCreatePaymentRequestDto.getReceiverZipCode());
		cartDataRequestDto.setBuildingCode(orderCreatePaymentRequestDto.getBuildingCode());

		if (orderCreatePaymentRequestDto.getArrivalScheduledList() != null && orderCreatePaymentRequestDto.getArrivalScheduledList().size() > 0) {
			List<OrderCreateScheduledDto> list = orderCreatePaymentRequestDto.getArrivalScheduledList();
			List<ChangeArrivalScheduledDto> arrivalScheduled = new ArrayList<ChangeArrivalScheduledDto>();

			for (OrderCreateScheduledDto orderCreateScheduledDto: list) {
				ChangeArrivalScheduledDto changeArrivalScheduledDto = new ChangeArrivalScheduledDto();
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate endDate = LocalDate.parse(orderCreateScheduledDto.getArrivalScheduledDate(), dateFormatter);
				changeArrivalScheduledDto.setArrivalScheduledDate(endDate);
				changeArrivalScheduledDto.setDawnDeliveryYn(orderCreateScheduledDto.getDawnDeliveryYn());
				arrivalScheduled.add(changeArrivalScheduledDto);
			}
			cartDataRequestDto.setArrivalScheduled(arrivalScheduled);
		}
//		cartDataRequestDto.setArrivalGoods(orderCreatePaymentRequestDto.getArrivalGoods());

		// 판매대기상품,관리자품절 상품 가능하게
		cartDataRequestDto.setBosCreateOrder(true);
		cartDataRequestDto.setFreeShippingPriceYn(orderCreatePaymentRequestDto.getFreeShippingPriceYn());
		List<CartDeliveryDto> cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);

//		// 증정품
//		List<CartGiftDto> cartGift = new ArrayList<CartGiftDto>();
//		// 증정품 유효성 검사
//		if (orderCreatePaymentRequestDto.getGift() != null && !orderCreatePaymentRequestDto.getGift().isEmpty()) {
//			String userStatus = userBuyerMallService.getUserStatus(buyerVo).getCode();
//			// 증정 기획전 조회
//			List<GiftListResponseDto> giftResultList = shoppingCartBiz.giftGetValidation(cartDataDto, userStatus,
//					buyerVo.getUrGroupId(), isMember, isEmployee);
//
//			// 카트에서 넘어온 증정품정보와 증정품기획전 정보 일치하는지 확인
//			for (CartGiftDto cartGiftDto : reqDto.getGift()) {
//				// responseDto에 set하기 위한 cartGiftDto
//				CartGiftDto setCartGiftDto = new CartGiftDto();
//
//				// 1. 기획전 PK 비교
//				if (giftResultList.stream().map(m -> m.getEvExhibitId()).collect(Collectors.toList()).stream()
//						.noneMatch(a -> a.equals(cartGiftDto.getEvExhibitId()))) {
//					return ApiResult.result(ApplyPayment.FAIL_VALIDATION_GIFT);
//				}
//
//				// 2. 증정품 PK 비교
//				for (GiftListResponseDto giftResDto : giftResultList) {
//					if (cartGiftDto.getEvExhibitId().equals(giftResDto.getEvExhibitId())) {
//						if (giftResDto.getGoods().stream().map(m -> m.getIlGoodsId())
//								.noneMatch(a -> a.equals(cartGiftDto.getIlGoodsId()))) {
//							return ApiResult.result(ApplyPayment.FAIL_VALIDATION_GIFT);
//						} else {
//
//							// 증정품
//							for (GiftGoodsVo goodsVo : giftResDto.getGoods()) {
//								if (cartGiftDto.getIlGoodsId().equals(goodsVo.getIlGoodsId())) {
//									if(goodsVo.getStock() == 0) {
//										setCartGiftDto.setEvExhibitId(cartGiftDto.getEvExhibitId());
//										setCartGiftDto.setTitle(giftResDto.getTitle());
//										setCartGiftDto.setIlGoodsId(cartGiftDto.getIlGoodsId());
//
//										// 증정품 재고 부족 + 모든 증정품 재고 없음
//										if (giftResDto.getGoods().stream().allMatch(a -> a.getStock() == 0)) {
//											setCartGiftDto.setGiftState(GiftState.X.getCode());
//											gift.add(setCartGiftDto);
//											break;
//										}
//
//										// 증정품 재고 부족 + 다른 증정품 재고 있음
//										GiftGoodsVo otherGoodsVo = giftResDto.getGoods().stream().filter(a -> a.getStock() != 0
//												&& !a.getIlGoodsId().equals(cartGiftDto.getIlGoodsId())).findAny().orElse(null);
//										if (otherGoodsVo != null) {
//											setCartGiftDto.setGiftState(GiftState.S.getCode());
//											gift.add(setCartGiftDto);
//											cartGiftDto.setIlGoodsId(otherGoodsVo.getIlGoodsId());
//											cartGift.add(cartGiftDto);
//											break;
//										}
//									} else {
//										cartGift.add(cartGiftDto);
//										break;
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}

//		if (!cartGift.isEmpty()) {
//			cartDataRequestDto.setGift(cartGift);
//			cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);
//		}

		// 장바구니 할인 정보 반영된 카트정보 get
		if (orderCreatePaymentRequestDto.getUseCartPmCouponIssueId() != null && orderCreatePaymentRequestDto.getUseCartPmCouponIssueId() > 0) {

			// cartData에서 List<CartShippingDto> 배송정책별 리스트 추출
			List<CartShippingDto> cartShippingDto = shoppingCartBiz.getCartShippingList(cartDataDto);

			// List<CartShippingDto> 배송정책별 리스트에서 List<CartGoodsDto> 상품 리스트 추출
			List<CartGoodsDto> cartGoodsList = shoppingCartBiz.getCartGoodsList(cartShippingDto);

			CouponDto cartCouponDto = promotionCouponBiz.getCartCouponApplicationListByUser(longUrUserId, cartGoodsList,
					deviceType, orderCreatePaymentRequestDto.getUseCartPmCouponIssueId());
			cartDataRequestDto.setUseCartCoupon(cartCouponDto);

			cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);
		}

		// 배송지 정보
		GetSessionShippingResponseDto shippingDto = new GetSessionShippingResponseDto();
		shippingDto.setReceiverName(orderCreatePaymentRequestDto.getReceiverName());
		shippingDto.setReceiverZipCode(orderCreatePaymentRequestDto.getReceiverZipCode());
		shippingDto.setReceiverAddress1(orderCreatePaymentRequestDto.getReceiverAddress1());
		shippingDto.setReceiverAddress2(orderCreatePaymentRequestDto.getReceiverAddress2());
		shippingDto.setBuildingCode(orderCreatePaymentRequestDto.getBuildingCode());
		shippingDto.setReceiverMobile(orderCreatePaymentRequestDto.getReceiverMobile());
		shippingDto.setAccessInformationType(orderCreatePaymentRequestDto.getAccessInformationType());
		shippingDto.setAccessInformationPassword(orderCreatePaymentRequestDto.getAccessInformationPassword());
		shippingDto.setShippingComment(orderCreatePaymentRequestDto.getShippingComment());
		shippingDto.setBosTp(orderCreatePaymentRequestDto.getBosTp());

		CheckCartResponseDto checkCartResponseDto = shoppingCartBiz.checkBuyPossibleCart(shippingDto, cartDataRequestDto, cartDataDto, null);
		// 재고 부족은 아래 배송일자 변경 프로세스 에서 다시 한번 체크 함
		if (!ApplyPayment.SUCCESS.getCode().equals(checkCartResponseDto.getResult().getCode())
				&& !ApplyPayment.GOODS_LACK_STOCK.getCode().equals(checkCartResponseDto.getResult().getCode())) {
			return ApiResult.result(checkCartResponseDto.getResult());
		}

//		// 상품 판매중 또는 품절 관련 체크
//		List<GoodsLackStockNotiDto> goodsLackStockNotiDtoList = new ArrayList<GoodsLackStockNotiDto>();
//		List<CartShippingDto> cartShippingDtoList = shoppingCartBiz.getCartShippingList(cartDataDto);
//		for (int i = 0; i < cartShippingDtoList.size(); i++) {
//			boolean check = false;
//			for (CartGoodsDto cartGoodsDto : cartShippingDtoList.get(i).getGoods()) {
//				// 증정품 제외하고 체크
//				if (!cartGoodsDto.getGoodsType().equals(GoodsEnums.GoodsType.GIFT.getCode())) {
//					if (GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode().equals(cartGoodsDto.getSaleStatus())
//							&& GoodsEnums.SaleStatus.ON_SALE.getCode().equals(cartGoodsDto.getSystemSaleStatus())) {
//						// 프론트에서 보여지는 상태가 시스템 일시 품절(재고가 0이면 시스템품절로 처리함) 이지만 실제 상태는 판매중일때 재고체크 다시 해야함
//						check = true;
//					} else {
//						// 재고가 있지만 구매 수량보다 적을경우
//						if (cartGoodsDto.getStockQty() - cartGoodsDto.getQty() < 0) {
//							check = true;
//						}
//					}
//				}
//			}
//
//			// 재고 체크 다시 해야 하면 goodsLackStockNotiDtoList 에 데이터 추가
//			if(check == true) {
//				GoodsLackStockNotiDto goodsLackStockNotiDto = new GoodsLackStockNotiDto();
//				goodsLackStockNotiDto.setCartShippingListIndex(i);
//				goodsLackStockNotiDto.setDawnDeliveryYn(orderCreatePaymentRequestDto.getArrivalScheduled().get(i).getDawnDeliveryYn());
//				goodsLackStockNotiDto.setPrevArrivalScheduledDate(cartShippingDtoList.get(i).getArrivalScheduledDate());
//				goodsLackStockNotiDto.setGoodsNameList(cartShippingDtoList.get(i).getGoods().stream().map(CartGoodsDto::getGoodsName).collect(Collectors.toList()));
//				goodsLackStockNotiDtoList.add(goodsLackStockNotiDto);
//			}
//		}

//		// 도착 예정 일자를 다시 체크해야 하는 경우
//		if (!goodsLackStockNotiDtoList.isEmpty()) {
//			// 재고 선택 일자를 초기화 하여 브릿지 페이지에서 재고 선택 일자 불러오기
//			cartDataRequestDto.setArrivalScheduled(null);
//			cartDataRequestDto.setBridgeYn("Y");
//			cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);
//			cartShippingDtoList = shoppingCartBiz.getCartShippingList(cartDataDto);
//			for (GoodsLackStockNotiDto goodsLackStockNotiDto : goodsLackStockNotiDtoList) {
//				CartShippingDto cartShippingDto = cartShippingDtoList.get(goodsLackStockNotiDto.getCartShippingListIndex());
//				List<LocalDate> choiceArrivalScheduledDateList = null;
//				if ("Y".equals(goodsLackStockNotiDto.getDawnDeliveryYn())) {
//					choiceArrivalScheduledDateList = cartShippingDto.getDawnChoiceArrivalScheduledDateList();
//				} else {
//					choiceArrivalScheduledDateList = cartShippingDto.getChoiceArrivalScheduledDateList();
//				}
//				if (choiceArrivalScheduledDateList != null && !choiceArrivalScheduledDateList.isEmpty()) {
//					LocalDate afterArrivalScheduledDate = goodsGoodsBiz.getNextArrivalScheduledDate(choiceArrivalScheduledDateList, goodsLackStockNotiDto.getPrevArrivalScheduledDate());
//					if(afterArrivalScheduledDate == null) {
//						return ApiResult.result(ApplyPayment.GOODS_LACK_STOCK);
//					} else {
//						goodsLackStockNotiDto.setAfterArrivalScheduledDate(afterArrivalScheduledDate);
//						//고객이 요청한 도착 예정일자를 변경
//						orderCreatePaymentRequestDto.getArrivalScheduled().get(goodsLackStockNotiDto.getCartShippingListIndex())
//								.setArrivalScheduledDate(afterArrivalScheduledDate);
//					}
//				} else {
//					return ApiResult.result(ApplyPayment.GOODS_LACK_STOCK);
//				}
//			}
//			// 다시 변경된 기준으로 조회
//			cartDataRequestDto.setArrivalScheduled(orderCreatePaymentRequestDto.getArrivalScheduled());
//			cartDataRequestDto.setBridgeYn("N");
//			cartDataDto = shoppingCartBiz.getCartData(cartDataRequestDto);
//		}

		CartSummaryDto cartSummaryDto = shoppingCartBiz.getCartDataSummary(cartDataDto, orderCreatePaymentRequestDto.getUsePoint());

		// 무료 결제 체크
		if (OrderEnums.PaymentType.FREE.getCode().equals(orderCreatePaymentRequestDto.getPsPayCd())
				&& cartSummaryDto.getPaymentPrice() != 0) {
			return ApiResult.result(ApplyPayment.FAIL_FREE);
		}

		PaymentType paymentType = PaymentType.findByCode(orderCreatePaymentRequestDto.getPsPayCd());

//		if (isMember && "Y".equals(orderCreatePaymentRequestDto.getSavePaymentMethodYn())) {
//			userBuyerBiz.putUserPaymentInfo(longUrUserId, paymentType.getCode(), orderCreatePaymentRequestDto.getCardCode());
//			// 가상계좌일때 환불계좌 정보 처리
//			if (PaymentType.VIRTUAL_BANK.getCode().equals(orderCreatePaymentRequestDto.getPsPayCd())) {
//				CommonSaveRefundBankRequestDto saveRefundBankRequestDto = new CommonSaveRefundBankRequestDto();
//				saveRefundBankRequestDto.setBankCode(orderCreatePaymentRequestDto.getBankCode());
//				saveRefundBankRequestDto.setAccountNumber(orderCreatePaymentRequestDto.getAccountNumber());
//				saveRefundBankRequestDto.setHolderName(orderCreatePaymentRequestDto.getHolderName());
//				saveRefundBankRequestDto.setUrUserId(urUserId);
//				CommonGetRefundBankRequestDto getRefundBankDto = new CommonGetRefundBankRequestDto();
//				getRefundBankDto.setUrUserId(urUserId);
//				CommonGetRefundBankResultVo refundBankVo = userBuyerBiz.getRefundBank(getRefundBankDto);
//				if (refundBankVo != null) {
//					saveRefundBankRequestDto.setUrRefundBankId(refundBankVo.getUrRefundBankId());
//					userBuyerBiz.putRefundBank(saveRefundBankRequestDto);
//				} else {
//					userBuyerBiz.addRefundBank(saveRefundBankRequestDto);
//				}
//			}
//		}

		// 임시 주문 생성 등록
		CartBuyerDto cartBuyerDto = new CartBuyerDto();
		cartBuyerDto.setOrderCreateYn("Y");
		if (isMember) {
			cartBuyerDto.setUrUserId(longUrUserId);

			cartBuyerDto.setUrGroupId(orderCreatePaymentRequestDto.getUrGroupId());
			cartBuyerDto.setUrEmployeeCd("");
			cartBuyerDto.setGuestCi("");
			cartBuyerDto.setBuyerName(orderCreatePaymentRequestDto.getUserName());
			cartBuyerDto.setBuyerMobile(orderCreatePaymentRequestDto.getUserMobile());
			cartBuyerDto.setBuyerEmail(orderCreatePaymentRequestDto.getUserEmail());
//			if (isEmployee) {
//				cartBuyerDto.setBuyerType(BuyerType.EMPLOYEE);
//			} else {
//				cartBuyerDto.setBuyerType(BuyerType.USER);
//			}
			cartBuyerDto.setBuyerType(BuyerType.USER);
		} else {
			cartBuyerDto.setUrUserId(0L);
			cartBuyerDto.setUrGroupId(0L);
			cartBuyerDto.setUrEmployeeCd("");
			cartBuyerDto.setGuestCi("");
			cartBuyerDto.setBuyerName(orderCreatePaymentRequestDto.getUserName());
			cartBuyerDto.setBuyerMobile(orderCreatePaymentRequestDto.getUserMobile());
			cartBuyerDto.setBuyerEmail(orderCreatePaymentRequestDto.getUserEmail());
			cartBuyerDto.setBuyerType(BuyerType.GUEST);
		}
		cartBuyerDto.setPaymentType(paymentType);
		cartBuyerDto.setAgentType(SystemEnums.AgentType.ADMIN);
		cartBuyerDto.setUrPcidCd(ORDER_CREATE_UR_PCID_CD);
		// 환불 계좌 정보 - 가상계좌 일때만
		cartBuyerDto.setBankCode(orderCreatePaymentRequestDto.getBankCode());
		cartBuyerDto.setAccountNumber(orderCreatePaymentRequestDto.getAccountNumber());
		cartBuyerDto.setHolderName(orderCreatePaymentRequestDto.getHolderName());

//		CartRegularDto cartRegularDto = new CartRegularDto();
//		cartRegularDto.setCycleType(orderCreatePaymentRequestDto.getRegularShippingCycleType());
//		cartRegularDto.setCycleTermType(orderCreatePaymentRequestDto.getRegularShippingCycleTermType());
//		cartRegularDto.setArrivalScheduledDate(orderCreatePaymentRequestDto.getRegularShippingArrivalScheduledDate());

		CreateOrderCartDto createOrderCartDto = new CreateOrderCartDto();
		createOrderCartDto.setBuyer(cartBuyerDto);
		createOrderCartDto.setShippingZone(shippingDto);
		createOrderCartDto.setCartList(cartDataDto);
		createOrderCartDto.setCartSummary(cartSummaryDto);
//		createOrderCartDto.setRegular(cartRegularDto);

		BasicDataResponseDto resDto = null;

//		if(ShoppingEnums.CartType.REGULAR.getCode().equals(ORDER_CREATE_CART_TYPE)) {
//			// 정기배송일때는 별도 로직 처리
//			ApplyRegularResponseDto applyRegularResponseDto = orderRegularBiz.applyRegular(createOrderCartDto);
//			if(applyRegularResponseDto.isResult()) {
//
//				// 장바구니 삭제
//				DelCartRequestDto delCartRequestDto = new DelCartRequestDto();
//				delCartRequestDto.setSpCartId(orderCreatePaymentRequestDto.getSpCartId());
//				shoppingCartBiz.delCartAndAddGoods(delCartRequestDto);
//
//				resDto = new BasicDataResponseDto();
//				resDto.setOdid(applyRegularResponseDto.getReqId());
//				resDto.setPaymentPrice(cartSummaryDto.getPaymentPrice());
//			} else {
//				return ApiResult.result(ApplyPayment.FAIL_CREATE_ORDER);
//			}
//		} else {
		List<OrderBindDto> orderBindList = cartOrderBindBiz.orderDataBind(createOrderCartDto);

		OrderRegistrationResponseDto orderRegistrationResponseDto = orderRegistrationBiz.createOrder(orderBindList, "N");

		if (!OrderEnums.OrderRegistrationResult.SUCCESS.getCode()
				.equals(orderRegistrationResponseDto.getOrderRegistrationResult().getCode())) {
			return ApiResult.result(ApplyPayment.FAIL_CREATE_ORDER);
		}

		String odid = orderRegistrationResponseDto.getOdids().split(",")[0];
		String odOrderId = orderRegistrationResponseDto.getOdOrderIds().split(",")[0];
		// 주문번호로 주문데이터 조회
		PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(odid);

		// 무료 결제
		if (OrderEnums.PaymentType.FREE.getCode().equals(orderCreatePaymentRequestDto.getPsPayCd())) {

			// 승인처리
			PgApprovalOrderPaymentDataDto paymentData = new PgApprovalOrderPaymentDataDto();
			paymentData.setApprovalDate(LocalDateTime.now());
			PutOrderPaymentCompleteResDto putResDto = orderProcessBiz.putOrderPaymentComplete(orderData, paymentData);

			if (!PgEnums.PgErrorType.SUCCESS.getCode().equals(putResDto.getResult().getCode())) {
				return ApiResult.result(ApplyPayment.FAIL_FREE_UPDATE);
			}

			// 장바구니 삭제
			DelCartRequestDto delCartRequestDto = new DelCartRequestDto();
			delCartRequestDto.setSpCartId(orderCreatePaymentRequestDto.getSpCartIdList());
			shoppingCartBiz.delCartAndAddGoods(delCartRequestDto);

			// 응답
			resDto = new BasicDataResponseDto();
			resDto.setOdid(odid);
			resDto.setPaymentPrice(cartSummaryDto.getPaymentPrice());

		// 신용카드 비인증 결제
		} else if (OrderEnums.PaymentType.CARD.getCode().equals(orderCreatePaymentRequestDto.getPsPayCd())) {

		// 직접 결제
		} else if (OrderEnums.PaymentType.DIRECT.getCode().equals(orderCreatePaymentRequestDto.getPsPayCd())) {

			// 쿠폰 사용 처리
			if (!orderData.getPmCouponIssueIds().isEmpty()) {
				for (Long pmCouponIssueId : orderData.getPmCouponIssueIds()) {
					ApiResult<?> couponRes =  promotionCouponBiz.useCoupon(orderData.getUrUserId(), pmCouponIssueId);
					if (!BaseEnums.Default.SUCCESS.getCode().equals(couponRes.getCode())) {
						throw new BaseException(couponRes.getMessageEnum());
					}
				}
			}

			// 적립금 사용 처리
			if (orderData.getPointPrice() > 0) {
				ApiResult<?> pointRes = pointBiz.redeemPoint(DepositPointDto.builder().urUserId(orderData.getUrUserId()).pmPointId(0L)
						.pointPaymentType(PointEnums.PointPayment.DEDUCTION)
						.amount(Long.valueOf((orderData.getPointPrice() * -1)))
						.pointProcessType(PointEnums.PointProcessType.WITHDRAW_POINT_PAYMENT).refNo1(orderData.getOdid())
						.build());
				if (!BaseEnums.Default.SUCCESS.getCode().equals(pointRes.getCode())) {
					throw new BaseException(pointRes.getMessageEnum());
				}
			}

			// 재고 차감
			List<StockOrderRequestDto> stockOrderReqDtoList = new ArrayList<StockOrderRequestDto>();
			StockOrderRequestDto stockOrderRequestDto = new StockOrderRequestDto();
			List<StockCheckOrderDetailDto> orderGoodsList = orderOrderBiz.getStockCheckOrderDetailList(orderData.getOdOrderId());
			for (StockCheckOrderDetailDto goods : orderGoodsList) {
				StockOrderRequestDto stockOrderReqDto = new StockOrderRequestDto();
				stockOrderReqDto.setIlGoodsId(goods.getIlGoodsId());
				stockOrderReqDto.setOrderQty(goods.getOrderCnt());
				stockOrderReqDto.setScheduleDt(StringUtil.nvl(goods.getShippingDt(), SCHEDULE_DATE));
				stockOrderReqDto.setOrderYn("Y");
				stockOrderReqDto.setStoreYn(GoodsEnums.GoodsDeliveryType.SHOP.getCode().equals(goods.getGoodsDeliveryType()) ? "Y" : "N");
				stockOrderReqDto.setMemo(String.valueOf(goods.getOdOrderDetlId()));
				stockOrderReqDtoList.add(stockOrderReqDto);
			}
			stockOrderRequestDto.setOrderList(stockOrderReqDtoList);
			ApiResult<?> stockRes = goodsStockOrderBiz.stockOrderHandle(stockOrderRequestDto);
			if (!stockRes.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
				return ApiResult.result(ApplyPayment.FAIL_UPDATE_STOCK);
			}

			orderRegistrationBiz.approvalDirectOrder(
					OrderApprovalDto.builder()
					.order(OrderVo.builder()
							.odOrderId(Long.parseLong(odOrderId))
							.orderPaymentType(OrderEnums.PaymentType.DIRECT.getCode())
							.orderYn("Y")
							.build()
							).build());

            // 출고처 일자별 출고수량 업데이트
            orderOrderBiz.putWarehouseDailyShippingCount(orderRegistrationResponseDto.getOdOrderIds());
		}
//			 else {
//				// PG
//				PgActiveRateVo pgActiveRateVo = pgBiz.getPgActiveRate(orderCreatePaymentRequestDto.getPsPayCd());
//				PgServiceType pgServiceType = PgServiceType.getProbabilityChoiceService(pgActiveRateVo.getKcpRate(), pgActiveRateVo.getInicisRate());
//				String pgBankCode = "";
//				if (paymentType.equals(PaymentType.CARD) && StringUtil.isNotEmpty(orderCreatePaymentRequestDto.getCardCode())) {
//					// 카드 코드
//					pgBankCode = pgBiz.getPgBankCode(pgServiceType.getCode(), paymentType.getCode(), orderCreatePaymentRequestDto.getCardCode());
//
//					// 은행을 사용안한다면 다른 PG로 재시도
//					if (pgBankCode == null) {
//						if (PgServiceType.KCP.getCode().equals(pgServiceType.getCode())) {
//							pgServiceType = PgServiceType.INICIS;
//						} else {
//							pgServiceType = PgServiceType.KCP;
//						}
//						pgBankCode = pgBiz.getPgBankCode(pgServiceType.getCode(), paymentType.getCode(), orderCreatePaymentRequestDto.getCardCode());
//						if (pgBankCode == null) {
//							pgBankCode = "";
//						}
//					}
//				}
//
//				PgAbstractService pgService = pgBiz.getService(pgServiceType);
//
//				// 기본 결제
//				BasicDataRequestDto paymentRequestFormDataDto = new BasicDataRequestDto();
//				paymentRequestFormDataDto.setOdid(odid);
//				paymentRequestFormDataDto.setPaymentType(paymentType);
//				paymentRequestFormDataDto.setPgBankCode(pgBankCode);
//				paymentRequestFormDataDto.setQuota(orderCreatePaymentRequestDto.getInstallmentPeriod());
//				paymentRequestFormDataDto.setGoodsName(cartSummaryDto.getGoodsSummaryName());
//				paymentRequestFormDataDto.setPaymentPrice(cartSummaryDto.getPaymentPrice());
//				paymentRequestFormDataDto.setTaxPaymentPrice(cartSummaryDto.getTaxPaymentPrice());
//				paymentRequestFormDataDto.setTaxFreePaymentPrice(cartSummaryDto.getTaxFreePaymentPrice());
//				paymentRequestFormDataDto.setBuyerName(cartBuyerDto.getBuyerName());
//				paymentRequestFormDataDto.setBuyerEmail(cartBuyerDto.getBuyerEmail());
//				paymentRequestFormDataDto.setBuyerMobile(cartBuyerDto.getBuyerMobile());
//				paymentRequestFormDataDto
//						.setLoginId(StringUtil.isNotEmpty(buyerVo.getLoginId()) ? buyerVo.getLoginId() : "비회원");
//				if (PaymentType.VIRTUAL_BANK.getCode().equals(reqDto.getPsPayCd())) {
//					paymentRequestFormDataDto.setVirtualAccountDateTime(pgBiz.getVirtualAccountDateTime());
//				}
//				EtcDataCartDto etcDataCartDto = new EtcDataCartDto();
//				etcDataCartDto.setOrderInputUrl(orderCreatePaymentRequestDto.getOrderInputUrl());
//				etcDataCartDto.setCartType(orderCreatePaymentRequestDto.getCartType());
//				etcDataCartDto.setOdid(odid);
//				etcDataCartDto.setSpCartId(orderCreatePaymentRequestDto.getSpCartId());
//				paymentRequestFormDataDto.setEtcData(pgService.toStringEtcData(etcDataCartDto));
//				paymentRequestFormDataDto.setCashReceipt(false);
//				paymentRequestFormDataDto.setCashReceiptEnum(OrderEnums.cashReceipt.DEDUCTION);
//				paymentRequestFormDataDto.setCashReceiptNumber("01038874023");
//
//				resDto = pgService.getBasicData(paymentRequestFormDataDto);
//			}
//		}

		// 증정품 지급상태
//		resDto.setGift(gift);
//		resDto.setGoodsLackStockNotiYn(goodsLackStockNotiDtoList.isEmpty() ? "N" : "Y");
//		resDto.setGoodsLackStockNoti(goodsLackStockNotiDtoList);
		resDto = new BasicDataResponseDto();
		resDto.setOdid(odid);
		resDto.setOdOrderId(odOrderId);
		resDto.setOdPaymentMasterId(orderRegistrationResponseDto.getOdPaymentMasterId());
		resDto.setPaymentPrice(cartSummaryDto.getPaymentPrice());
		return ApiResult.success(resDto);
	}

	/**
     * 주문생성 비인증 신용카드 결제
     *
     * @param orderCardPayRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
	@Override
	public ApiResult<?> cardPayOrderCreate(OrderCardPayRequestDto orderCardPayRequestDto) throws Exception {
		List<String> odIdList = getOdIdList(orderCardPayRequestDto.getOdid());
		orderCardPayRequestDto.setFindOdIdList(odIdList);
		orderCardPayRequestDto.setOdid(odIdList.get(0));
		log.debug("카드정보 orderCardPayRequestDto <{}>", orderCardPayRequestDto);

		OrderInfoDto orderInfoDto = getOrderInfo(orderCardPayRequestDto);
		log.debug("주문정보 orderInfoDto <{}>", orderInfoDto);

		PaymentInfoDto paymentInfoDto = getPaymentInfo(orderCardPayRequestDto.getOdPaymentMasterId());
		log.debug("결제금액정보 PaymentInfoDto1 <{}>", paymentInfoDto);

		// 주문번호로 주문데이터 조회
		PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderCreateDataByOdid(odIdList);

		// 신용카드 비인증 결제
		InicisNonAuthenticationCartPayRequestDto payReqDto = new InicisNonAuthenticationCartPayRequestDto();
		payReqDto.setOdid(orderCardPayRequestDto.getOdid());
		payReqDto.setGoodsName(orderInfoDto.getGoodsNm());
		payReqDto.setPaymentPrice(paymentInfoDto.getPaymentPrice());
		payReqDto.setTaxPaymentPrice(paymentInfoDto.getTaxablePrice());
		payReqDto.setTaxFreePaymentPrice(paymentInfoDto.getNonTaxablePrice());
		payReqDto.setBuyerName(orderInfoDto.getBuyerNm());
		payReqDto.setBuyerEmail(orderInfoDto.getBuyerMail());
		payReqDto.setBuyerMobile(orderInfoDto.getBuyerHp());
		payReqDto.setQuota(orderCardPayRequestDto.getPlanPeriod());
		payReqDto.setCardNumber(orderCardPayRequestDto.getCardNo());
		payReqDto.setCardExpire(orderCardPayRequestDto.getCardNumYy() + orderCardPayRequestDto.getCardNumMm());
		payReqDto.setRegNo(orderCardPayRequestDto.getAddInfoVal());
		payReqDto.setCardPw(orderCardPayRequestDto.getCardPass());

		log.debug("카드결제 payReqDto <{}>", payReqDto.toString());

		InicisNonAuthenticationCartPayResponseDto payResDto = inicisPgService.nonAuthenticationCartPay(payReqDto);

		log.debug("카드결제 결과 payResDto <{}>", payResDto.toString());

		log.debug("카드결제 결과 orderData <{}>", orderData.toString());

		OrderEnums.OrderRegistrationResult orderRegistrationResult = OrderEnums.OrderRegistrationResult.SUCCESS;
		//orderRegistrationResult = OrderEnums.OrderRegistrationResult.SUCCESS;
		if (payResDto.isSuccess()) {

			PutOrderPaymentCompleteResDto putResDto = null;

			int orderPaymentFailCount = 0;

			// 관리자에서 주문 생성시 주문번호 여러개 결제를 한번 할수 있는 케이스가 있어 PG 주문번호에 odPaymentMasterId 를 보냄
			List<PgApprovalOrderDataDto> orderDataList = orderOrderBiz.getPgApprovalOrderDataByOdPaymentMasterId(String.valueOf(orderCardPayRequestDto.getOdPaymentMasterId()));
			for (PgApprovalOrderDataDto pgApprovalOrderDataDto : orderDataList) {
				// 주문 입금 확인 처리
				PutOrderPaymentCompleteResDto putOrderPaymentCompleteResDto = orderOrderBiz.putOrderPaymentComplete(pgApprovalOrderDataDto, payReqDto, payResDto);
				if (!PgEnums.PgErrorType.SUCCESS.getCode().equals(putOrderPaymentCompleteResDto.getResult().getCode())) {
					orderPaymentFailCount++;
					putResDto = putOrderPaymentCompleteResDto;
				}
			}


			if (orderPaymentFailCount == 0) {
				orderRegistrationResult = OrderEnums.OrderRegistrationResult.SUCCESS;
			} else {


				// PG 취소
				CancelRequestDto cancelReqDto = new CancelRequestDto();
				cancelReqDto.setPartial(false);
				cancelReqDto.setPaymentType(PaymentType.findByCode(putResDto.getOrderData().getOrderPaymentType()));
				cancelReqDto.setTid(payResDto.getTid());
				cancelReqDto.setCancelMessage("가맹점 결과 처리 오류 - " + putResDto.getResult().getCodeName());
				cancelReqDto.setOdid(orderData.getOdid());

				inicisPgService.cancel(putResDto.getPaymentData().getPgAccountType().getCode(), cancelReqDto);

				orderRegistrationResult = OrderEnums.OrderRegistrationResult.FAIL;
			}
		} else {
			orderRegistrationResult = OrderEnums.OrderRegistrationResult.FAIL;
			//throw new BaseException(payResDto.getMessage());


			orderCreateService.putOrderFail(orderCardPayRequestDto.getOdOrderId());
		}

		OrderCardPayResponseDto orderCreateResponseDto = OrderCardPayResponseDto.builder()
        		.result(orderRegistrationResult)
        		.inicisDto(payResDto)
        		.message(payResDto.getMessage())
				.odid(orderCardPayRequestDto.getOdid())
                .build();

		return ApiResult.success(orderCreateResponseDto);
	}

	/**
	 * 주문연동 매출만연동시 결제완료시 처리
	 * @param odOrderId
	 */
	@Override
	public void orderCopySalIfExecute(long odOrderId, String orderCopySalIfYn, String orderCopyOdid) throws Exception  {
		orderCreateService.orderCopySalIfExecute(odOrderId, orderCopySalIfYn, orderCopyOdid);
	}

	@Override
	public void putOrderFail(Long odOrderId) {
		orderCreateService.putOrderFail(odOrderId);
	}

	@Override
	public void putOrderSuccess(Long odPaymentMasterId) {
		orderCreateService.putOrderSuccess(odPaymentMasterId);
	}

	/**
	 * 주문생성 > 일일상품 옵션변경 팝업 > 일일상품 정보 조회
	 * @param ilGoodsId
	 */
	@Override
	public ApiResult<?> getGoodsDailyCycleList(Long ilGoodsId, String recvBldNo, String zipCode) throws Exception{
		List<GoodsDailyCycleDto> goodsDailyCycleList = new ArrayList<>();

		GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder().ilGoodsId(ilGoodsId)
				.deviceInfo(ORDER_CREATE_DIR_INFO).isApp(ORDER_CREATE_IS_APP).isMember(true).isEmployee(ORDER_CREATE_IS_EMPLOYEE).isBosCreateOrder(true).build();

		BasicSelectGoodsVo goods = goodsGoodsBiz.getGoodsBasicInfo(goodsRequestDto);
		if (goods == null || !GoodsEnums.SaleType.DAILY.getCode().equals(goods.getSaleType())) {
			return ApiResult.result(ShoppingEnums.AddSimpleCart.NO_GOODS);
		}else{
			goodsDailyCycleList = goodsGoodsBiz.getGoodsDailyCycleList(ilGoodsId, goods.getGoodsDailyType(), zipCode, recvBldNo);
			for(GoodsDailyCycleDto dto : goodsDailyCycleList){
				dto.setGoodsDailyAllergyYn(goods.getGoodsDailyAllergyYn());
				dto.setGoodsDailyBulkYn(goods.getGoodsDailyBulkYn());
				dto.setGoodsDailyBulk(goodsGoodsBiz.getGoodsDailyBulkList(ilGoodsId));
			}
		}

		return ApiResult.success(goodsDailyCycleList);
	}

	/**
	 * 주문생성 > 상품삭제
	 * @param delCartRequestDto
	 */
	@Override
	public ApiResult<?> delCartAndAddGoods(DelCartRequestDto delCartRequestDto) throws Exception{

		// 상품삭제
		shoppingCartBiz.delCartAndAddGoods(delCartRequestDto);

		// 추가상품 삭제
		if(CollectionUtils.isNotEmpty(delCartRequestDto.getSpCartAddGoodsId())){
			DelCartAddGoodsRequestDto delCartAddGoodsRequestDto = new DelCartAddGoodsRequestDto();
			delCartAddGoodsRequestDto.setSpCartAddGoodsId(delCartRequestDto.getSpCartAddGoodsId());
			shoppingCartBiz.delCartAddGoods(delCartAddGoodsRequestDto);
		}

		return ApiResult.success();
	}

	/**
	 * 주문생성 > 장바구니 정보 조회
	 *
	 * @param getCartDataRequestDto
	 * @return ApiResult<?>
	 * @throws Exception
	 */
	@Override
	public List<CartDeliveryDto> getCartData(GetCartDataRequestDto getCartDataRequestDto) throws Exception {
		List<CartDeliveryDto> cartDeliveryDtoList = null;
		String urUserId = StringUtil.nvl(getCartDataRequestDto.getUrUserId());
		boolean isMember = StringUtil.isNotEmpty(urUserId);
		getCartDataRequestDto.setUrPcidCd(ORDER_CREATE_UR_PCID_CD);
		getCartDataRequestDto.setDeviceInfo(ORDER_CREATE_DIR_INFO);
		getCartDataRequestDto.setApp(ORDER_CREATE_IS_APP);
		getCartDataRequestDto.setMember(isMember);
		getCartDataRequestDto.setEmployee(ORDER_CREATE_IS_EMPLOYEE);
		getCartDataRequestDto.setUrErpEmployeeCode("");
		getCartDataRequestDto.setBridgeYn("Y");
		getCartDataRequestDto.setBosCreateOrder(true); // 판매대기상품, 관리자품절상품도 가능하게 하기위해

		// 장바구니 조회
		cartDeliveryDtoList = shoppingCartBiz.getCartData(getCartDataRequestDto);

		return cartDeliveryDtoList;
	}

	@Override
	public ApiResult<?> getAdditionalGoodsInfoList(long ilGoodsId) throws Exception{
		return ApiResult.success(goodsGoodsBiz.getAdditionalGoodsInfoList(ilGoodsId,true,false,false,null));
	}

}