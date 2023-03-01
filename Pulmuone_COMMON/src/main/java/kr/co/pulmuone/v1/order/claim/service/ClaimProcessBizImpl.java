package kr.co.pulmuone.v1.order.claim.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import kr.co.pulmuone.v1.api.hitok.service.HitokApiBiz;
import kr.co.pulmuone.v1.api.storedelivery.service.StoreDeliveryApiBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlHistVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.OdAddPaymentReqInfo;
import kr.co.pulmuone.v1.order.create.dto.OrderClaimCardPayRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderClaimCardPayResponseDto;
import kr.co.pulmuone.v1.order.create.dto.OrderInfoDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderCashReceiptVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentVo;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationBiz;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.pg.dto.CancelRequestDto;
import kr.co.pulmuone.v1.pg.dto.CancelResponseDto;
import kr.co.pulmuone.v1.pg.service.PgAbstractService;
import kr.co.pulmuone.v1.pg.service.PgBiz;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 저장,수정,삭제 Implements
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
public class ClaimProcessBizImpl implements ClaimProcessBiz {

    @Autowired
    private ClaimProcessService claimProcessService;

	@Autowired
    private ClaimRequestProcessService claimRequestProcessService;

	@Autowired
	private ClaimValidationBiz ClaimValidationBiz;

	@Autowired
	private ClaimRequestProcessBiz claimRequestProcessBiz;

	@Autowired
	private ClaimCompleteProcessBiz claimCompleteProcessBiz;

	@Autowired
	private ClaimUtilProcessService claimUtilProcessService;

	@Autowired
	private ClaimUtilRefundService claimUtilRefundService;

	@Autowired
	private OrderRegistrationBiz orderRegistrationBiz;

	@Autowired
	private PgBiz pgBiz;

	@Autowired
	private HitokApiBiz hitokApiBiz;

	@Autowired
	private ClaimRequestBiz claimRequestBiz;

	@Autowired
	private ClaimCompleteProcessService claimCompleteProcessService;

	@Autowired
	private ClaimRequestService claimRequestService;

	@Autowired
	private PointBiz pointBiz;

	@Autowired
	private StoreDeliveryApiBiz storeDeliveryApiBiz;

	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 1. 클레임정보에서 클레임 처리가능 상태값
	 *                  	클레임요청처리    	                   	클레임완료처리
	 *  (1) 취소
	 *   - 취소완료 : 	클레임요청처리 패스 		 		취소/반품 클레임완료 처리프로세스
	 *   - 취소거부 : 	거부 클레임요청 처리프로세스 		취소/반품 클레임완료 이외 처리프로세스
	 *  (2) 반품
	 *   - 반품승인 : 	클레임요청처리 패스				승인 클레임완료 처리프로세스
	 *   - 반품거부 : 	거부 클레임요청 처리프로세스		취소/반품 클레임완료 이외 처리프로세스
	 *   - 반품보류 : 	클레임요청처리 패스				취소/반품 클레임완료 이외 처리프로세스
	 *   - 반품완료 : 	클레임요청처리 패스				취소/반품 클레임완료 처리프로세스
	 *
	 * 2. 주문정보에서 클레임 처리가능 상태값
	 *						클레임요청처리       	              클레임완료처리
	 *  (1) 취소
	 *   - 취소요청 : 	공통 클레임요청 처리프로세스 		없음
	 *   - 취소완료 : 	공통 클레임요청 처리프로세스 		취소/반품 클레임완료 처리프로세스
	 *   - 입금전취소:	공통 클레임요청 처리프로세스		취소/반품 클레임완료 이외 처리프로세스
	 *  (2) 반품
	 *   - 반품요청 : 	공통 클레임요청 처리프로세스      없음
	 *   - 반품승인 : 	공통 클레임요청 처리프로세스		승인 클레임완료 처리프로세스
	 *   - 반품완료 : 	공통 클레임요청 처리프로세스		취소/반품 클레임완료 처리프로세스
	 *  (3) 재배송  :	공통 클레임요청 처리프로세스		재배송 클레임완료 처리프로세스
	 *
	 * 3. 철회
	 *  (1) 취소철회 : 철회 클레임완료 처리프로세스
	 *  (2) 반품철회 : 철회 클레임완료 처리프로세스
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
	public ApiResult<?> addOrderClaim(OrderClaimRegisterRequestDto requestDto) throws Exception {
		return this.addOrderClaimNonTransaction(requestDto);
	}

    /**
     * 취소/반품 처리 - Non Transaction
     * @param requestDto
     * @return
     * @throws Exception
     */
	@Override
	public ApiResult<?> addOrderClaimNonTransaction(OrderClaimRegisterRequestDto requestDto) throws Exception {

		String claimStatusCd = requestDto.getClaimStatusCd();	// 변경대상 클레임 상태
		String claimStatusTp = requestDto.getClaimStatusTp();
		long odClaimId = requestDto.getOdClaimId();
		log.debug("=============================클레임 상태: "+claimStatusCd);

		// 클레임 저장 유무
		boolean isClaimSave = requestDto.getOdClaimId() > 0 ? true : false;

		// 프론트에서 재발급 요청값으로 쿠폰정보가 제대로 넘어왔는지 확인
		if(requestDto.getFrontTp() == OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_FRONT.getCodeValue()){
			isPossibleRefundCoupon(requestDto);
		}

		// 배치 클레임 생성 요청이 아니고 직접 결제 여부가 Y가 아니고 추가결제방법이 가상계좌가 아닐경우에만 유효성 체크
		if(requestDto.getFrontTp() < OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_BATCH.getCodeValue() &&
			!OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode().equals(requestDto.getDirectPaymentYn()) &&
			!OrderClaimEnums.ClaimAddPaymentTp.VIRTUAL.getCode().equals(requestDto.getAddPaymentTp())) {
			// 클레임 validation 처리
			ClaimValidationDto claimValidationDto = ClaimValidationBiz.claimValidationProcess(claimStatusCd, requestDto);

			if (!"SUCCESS".equals(claimValidationDto.getClaimResult().getCode()))
				return ApiResult.result(claimValidationDto.getClaimResult());
		}

		// 클레임 저장일 경우 - 이미 클레임 신청된 정보일 경우(클레임정보탭으로 진입시) isClaimSave : true,
		//                    새로운 클레임 신청일 경우(상품/결제정보탭으로 진입시 또는 MALL에서 신청시) isClaimSave : false
		if(isClaimSave) {
			// 이미 신청 된 클레임의 일부 취소일 경우 : 해당 클레임번호 전체를 취소 / 반품 거부 처리 후 새로운 클레임으로 등록처리 해야 하므로 거부 처리 후 isClaimSave 는 false로 변경 됨
			isClaimSave = this.checkClaimDetlRequestData(requestDto, isClaimSave, requestDto.getFrontTp());
		}

		// 클레임 요청 처리
		ClaimRequestProcessDto claimRequestProcessDto = claimRequestProcessBiz.claimRequestProcess(claimStatusTp, claimStatusCd, requestDto, isClaimSave);
		OrderEnums.OrderRegistrationResult claimResult = claimRequestProcessDto.getClaimResult();
		log.debug("=============================클레임 요청 처리 결과: "+claimResult.getCode());

		// 요청성공일경우
		if (OrderEnums.OrderRegistrationResult.SUCCESS.getCode().equals(claimResult.getCode())) {
			// 클레임 완료 처리
			ClaimCompleteProcessDto claimCompleteProcessDto = claimCompleteProcessBiz.claimCompleteProcess(claimStatusTp, claimStatusCd, requestDto, isClaimSave);
			claimResult = claimCompleteProcessDto.getClaimResult();
			log.debug("=============================클레임 완료 처리 결과: "+claimResult.getCode());

			// 취소완료,반품완료일 경우 -> 관리자가 BOS에서 현금영수증발행한 경우 -> 현금영수증 취소(부분취소시 재발행)
			if(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(claimStatusCd) || OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(claimStatusCd)){
				claimCompleteProcessBiz.receiptCancel(requestDto);
			}
		}

		// 매장배송클레임 API 송신
		boolean isShop = requestDto.getGoodsInfoList().stream().filter(vo -> GoodsEnums.GoodsDeliveryType.SHOP.getCode().equals(vo.getGoodsDeliveryType())).count() > 0 ? true : false;
		// 클레임 정보가 존재할 경우 기존 클레임 번호로 Set 해준다.
		// - 클레임 요청 시 클레임 건수와 클레임 완료 시 클레임 건수가 다를 경우, 클레임 완료 건을 제외한 나머지 건에 대해서는 거부 및 클레임 재등록 처리가 되어 신규 클레임 번호가 채번 되므로, 기존 클레임 요청 건에 대한 매장배송 취소 처리가 불가능 함.
		if(isClaimSave) {requestDto.setOdClaimId(odClaimId);}
		if(isShop) shopClaimApiSend(requestDto);

		OrderClaimRegisterResponseDto orderClaimRegisterResponseDto = OrderClaimRegisterResponseDto.builder()
									.orderRegistrationResult(claimResult)
									.message(claimResult.getCodeName())
									.odClaimId(claimRequestProcessDto.getOdClaimId())
									.build();

		return ApiResult.success(orderClaimRegisterResponseDto);
	}

	/**
	 * 매장배송클레임 API 송신
	 * @param requestDto
	 * @throws Exception
	 */
	private void shopClaimApiSend(OrderClaimRegisterRequestDto requestDto) throws Exception {

		boolean isSuccess = true;
		String orderStatusCd = requestDto.getOrderStatusCd();
		String claimStatusCd = requestDto.getClaimStatusCd();
		long odOrderId = requestDto.getOdOrderId();
		long odClaimId = requestDto.getOdClaimId();

		// 취소요청 -> 취소완료
		if(OrderEnums.OrderStatus.CANCEL_APPLY.getCode().equals(orderStatusCd) && OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(claimStatusCd)) {
			// 매장배송 취소 API 송신
			isSuccess = storeDeliveryApiBiz.addStoreDeliveryCancelApiSend(odOrderId, odClaimId);
		}
		// 반품완료
		else if(OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(claimStatusCd)) {
			// 매장배송 반품 API 송신
			isSuccess = storeDeliveryApiBiz.addStoreDeliveryReturnApiSend(odOrderId, odClaimId);
		}
		// 재배송
		else if(OrderEnums.OrderStatus.EXCHANGE_COMPLETE.getCode().equals(claimStatusCd)) {
			// 매장배송 반품 API 송신
			isSuccess = storeDeliveryApiBiz.addStoreDeliveryRedeliveryApiSend(odOrderId, odClaimId);
		}

		log.debug("SHOP ERP RESULT -- <{}> --", isSuccess);
		if(!isSuccess) {
			throw new BaseException(OrderClaimEnums.GreenJuiceRegistError.SHOP_ERP_IF_FAIL); // 매장배송 API 응답 실패
		}
	}

	/**
	 * 추가 결제 후 취소 / 반품 처리
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
	public ApiResult<?> addAddPaymentAfterOrderClaimRegister(OrderClaimRegisterRequestDto requestDto) throws Exception {

		log.debug("--------------- addAddPaymentAfterOrderClaimRegister In");
		// 추가 결제 요청 정보 조회
		OdAddPaymentReqInfo odAddPaymentReqInfo = claimProcessService.getOdAddPaymentReqInfo(requestDto.getOdAddPaymentReqInfoId());
		if(ObjectUtils.isEmpty(odAddPaymentReqInfo)) {
			return ApiResult.result(OrderClaimEnums.AddPaymentClaimInfoError.NONE_ADD_PAYMENT_INFO);
		}

		// 추가 결제 마스터 정보 변환
		OrderClaimPaymentMasterDto orderClaimPaymentMasterVo = objectMapper.readValue(odAddPaymentReqInfo.getReqJsonInfo(), OrderClaimPaymentMasterDto.class);

		boolean isClaimSave = requestDto.getOdClaimId() > 0 ? true : false;

		// 클레임 번호가 존재할 경우
		if(isClaimSave) {
			isClaimSave = this.checkClaimDetlRequestData(requestDto, isClaimSave, requestDto.getFrontTp());
		}

		// 클레임 정보 등록
		// 클레임 요청 처리
		ClaimRequestProcessDto claimRequestProcessDto = claimRequestProcessBiz.claimRequestProcess(requestDto.getClaimStatusTp(), requestDto.getClaimStatusCd(), requestDto, isClaimSave);
		OrderEnums.OrderRegistrationResult claimResult = claimRequestProcessDto.getClaimResult();
		log.debug("=============================클레임 요청 처리 결과: "+claimResult.getCode());

		// 요청성공일경우
		if (OrderEnums.OrderRegistrationResult.SUCCESS.getCode().equals(claimRequestProcessDto.getClaimResult().getCode())){
			// 클레임 완료 처리
			ClaimCompleteProcessDto claimCompleteProcessDto = claimCompleteProcessBiz.claimCompleteProcess(requestDto.getClaimStatusTp(), requestDto.getClaimStatusCd(), requestDto, isClaimSave);
			claimResult = claimCompleteProcessDto.getClaimResult();
			log.debug("=============================클레임 완료 처리 결과: "+claimResult.getCode());
		}

		// 클레임 정보 정상 등록 이면 추가 결제 정보 등록
		if(OrderEnums.OrderRegistrationResult.SUCCESS.getCode().equals(claimResult.getCode())) {

			//결제 정보 입력
			OrderPaymentVo orderPaymentVo = OrderPaymentVo.builder()
					.odOrderId(requestDto.getOdOrderId())								//주문PK
					.odClaimId(requestDto.getOdClaimId())								//주문클레임 PK
					.odPaymentMasterId(orderClaimPaymentMasterVo.getOdPaymentMasterId())//주문결제마스터 PK
					.salePrice(orderClaimPaymentMasterVo.getSalePrice())				//판매가
					.cartCouponPrice(orderClaimPaymentMasterVo.getCartCouponPrice()) 	//장바구니 쿠폰 할인금액
					.goodsCouponPrice(orderClaimPaymentMasterVo.getGoodsCouponPrice()) 	//상품 쿠폰 할인금액
					.directPrice(orderClaimPaymentMasterVo.getDirectPrice()) 			//즉시 할인금액
					.paidPrice(orderClaimPaymentMasterVo.getPaidPrice()) 				//결제금액금액
					.shippingPrice(orderClaimPaymentMasterVo.getShippingPrice()) 		//배송비합
					.taxablePrice(orderClaimPaymentMasterVo.getTaxablePrice()) 			//과세결제금액
					.nonTaxablePrice(orderClaimPaymentMasterVo.getNonTaxablePrice())  	//비과세결제금액
					.paymentPrice(orderClaimPaymentMasterVo.getPaymentPrice()) 			//결제금액
					.pointPrice(orderClaimPaymentMasterVo.getPointPrice()) 				//사용적립금금액
					.build();

			log.debug("결제 테이블 변수 [OD_PAYMENT] <{}>", orderPaymentVo);
			orderRegistrationBiz.addPayment(orderPaymentVo);

			LocalDateTime approvalDt = LocalDateTime.parse(
														orderClaimPaymentMasterVo.getApprovalDt().substring(0, 4) + "-" +
															orderClaimPaymentMasterVo.getApprovalDt().substring(4, 6) + "-" +
															orderClaimPaymentMasterVo.getApprovalDt().substring(6, 8) + " " +
															orderClaimPaymentMasterVo.getApprovalDt().substring(8, 10) + ":" +
															orderClaimPaymentMasterVo.getApprovalDt().substring(10, 12) + ":" +
															orderClaimPaymentMasterVo.getApprovalDt().substring(12, 14),
															DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			LocalDateTime paidDueDt = null;

			if(!ObjectUtils.isEmpty(orderClaimPaymentMasterVo.getPaidDueDt())) {
				paidDueDt = LocalDateTime.parse(
											orderClaimPaymentMasterVo.getPaidDueDt().substring(0, 4) + "-" +
												orderClaimPaymentMasterVo.getPaidDueDt().substring(4, 6) + "-" +
												orderClaimPaymentMasterVo.getPaidDueDt().substring(6, 8) + " " +
												orderClaimPaymentMasterVo.getPaidDueDt().substring(8, 10) + ":" +
												orderClaimPaymentMasterVo.getPaidDueDt().substring(10, 12) + ":" +
												orderClaimPaymentMasterVo.getPaidDueDt().substring(12, 14),
												DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			}
			log.debug("approvalDt :: <{}>", approvalDt);

			//결제 정보 입력
			OrderPaymentMasterVo orderPaymentMasterVo = OrderPaymentMasterVo.builder()
					.odPaymentMasterId(orderClaimPaymentMasterVo.getOdPaymentMasterId())		//주문 결제 마스터 PK
					.type(orderClaimPaymentMasterVo.getType())									//결제타입 (G : 결제, F : 환불 , A : 추가)
					.status(orderClaimPaymentMasterVo.getStatus())								//결제상태(IR:입금예정,IC:입금완료,IB:입금전취소)
					.payTp(orderClaimPaymentMasterVo.getPayTp())								//결제방법 공통코드(PAY_TP)
					.pgService(orderClaimPaymentMasterVo.getPgService()) 						//PG 종류 공통코드(PG_ACCOUNT_TYPE)
					.tid(orderClaimPaymentMasterVo.getTid()) 									//거래 ID
					.authCd(orderClaimPaymentMasterVo.getAuthCd()) 								//승인코드
					.cardNumber(orderClaimPaymentMasterVo.getCardNumber()) 						//카드번호
					.cardQuotaInterest(orderClaimPaymentMasterVo.getCardQuotaInterest()) 		//카드 무이자 구분
					.cardQuota(orderClaimPaymentMasterVo.getCardQuota()) 						//카드 할부기간
					.virtualAccountNumber(orderClaimPaymentMasterVo.getVirtualAccountNumber())  //가상계좌 번호
					.bankNm(orderClaimPaymentMasterVo.getBankNm()) 								//입금은행명
					.info(orderClaimPaymentMasterVo.getInfo()) 									//결제 정보
					.paidDueDt(paidDueDt) 														//입금기한
					.paidHolder(orderClaimPaymentMasterVo.getPaidHolder()) 						//입금자명
					.partCancelYn(orderClaimPaymentMasterVo.getPartCancelYn()) 					//부분취소 가능 여부
					.escrowYn(orderClaimPaymentMasterVo.getEscrowYn()) 							//에스크로 결제 여부
					.salePrice(orderClaimPaymentMasterVo.getSalePrice()) 						//에스크로 통신 완료 여부
					.cartCouponPrice(orderClaimPaymentMasterVo.getCartCouponPrice()) 			//판매가
					.goodsCouponPrice(orderClaimPaymentMasterVo.getGoodsCouponPrice()) 			//장바구니쿠폰할인금액
					.directPrice(orderClaimPaymentMasterVo.getDirectPrice()) 					//상품쿠폰할인금액
					.paidPrice(orderClaimPaymentMasterVo.getPaidPrice()) 						//상품,장바구니쿠폰 할인 제외한 할인금액
					.shippingPrice(orderClaimPaymentMasterVo.getShippingPrice()) 				//결제금액
					.taxablePrice(orderClaimPaymentMasterVo.getTaxablePrice()) 					//배송비합
					.nonTaxablePrice(orderClaimPaymentMasterVo.getNonTaxablePrice()) 			//과세결제금액
					.paymentPrice(orderClaimPaymentMasterVo.getPaymentPrice()) 					//비과세결제금액
					.pointPrice(orderClaimPaymentMasterVo.getPointPrice()) 						//결제금액
					.setlNo(orderClaimPaymentMasterVo.getSetlNo()) 								//사용적립금
					.responseData(orderClaimPaymentMasterVo.getResponseData()) 					//정산번호
					.approvalDt(approvalDt) 													//응답데이터
					.build();
			//결제 마스터 정보 입력
			log.debug("결제 마스터 테이블 변수 [OD_PAYMENT_MASTER] <{}>", orderPaymentVo);

			orderRegistrationBiz.addPaymentMaster(orderPaymentMasterVo);

			// 추가배송비 결제 가상계좌정보 SMS 발송
			claimProcessService.payAdditionalShippingPriceSendSms(orderClaimPaymentMasterVo);
		}
		// 클레임 정보가 정상이 아닐 경우 추가 결제 내역 취소 처리
		else {

			OrderEnums.PaymentType paymentType = null;

			// 카드 결제 일 때만 취소 처리
			if (OrderEnums.PaymentType.CARD.getCode().equals(orderClaimPaymentMasterVo.getPayTp())) {
				paymentType = OrderEnums.PaymentType.CARD;

				PgEnums.PgAccountType pgAccountType = PgEnums.PgAccountType.findByCode(orderClaimPaymentMasterVo.getPgService());
				PgEnums.PgServiceType pgServiceType = PgEnums.PgServiceType.findByCode(pgAccountType.getPgServiceType());
				PgAbstractService<?, ?> pgService = pgBiz.getService(pgServiceType);

				// 비인증 카드 결제 일 경우에만 취소 처리
				CancelRequestDto cancelReqDto = new CancelRequestDto();
				cancelReqDto.setPartial(false);                                                 //부분취소 여부
				cancelReqDto.setCancelMessage("클레임 등록 실패");                                //취소사유
				cancelReqDto.setOdid(String.valueOf(cancelReqDto.getOdid()));                   //추문번호
				cancelReqDto.setCancelPrice(orderClaimPaymentMasterVo.getPaymentPrice());       //취소금액
				cancelReqDto.setTid(orderClaimPaymentMasterVo.getTid());                        //거래번호
				cancelReqDto.setPaymentType(paymentType);                                       //취소지불수단
				cancelReqDto.setTaxCancelPrice(orderClaimPaymentMasterVo.getTaxablePrice());    //취소 과세 금액
				cancelReqDto.setTaxFreecancelPrice(orderClaimPaymentMasterVo.getNonTaxablePrice());//취소 비과세 금액
				cancelReqDto.setExpectedRestPrice(0);                                           //취소후 남은 금액 (부분취소시 필수)
				cancelReqDto.setRefundBankNumber("");                                           //환불계좌번호 (가상계좌 환불 필수)
				cancelReqDto.setRefundBankCode("");                                             //환불계좌은행코드 - PG 은행 코드 (가상계좌 환불 필수)
				cancelReqDto.setRefundBankName("");                                             //환불계좌 예금주명 (가상계좌 환불 필수)
				cancelReqDto.setEscrowYn(orderClaimPaymentMasterVo.getEscrowYn());              //에스크로결제여부
				log.debug("환불할 정보 ::: <{}>", cancelReqDto);
				log.debug("PG TYPE :: <{}>", orderClaimPaymentMasterVo.getPgService());

				CancelResponseDto cardDto = pgService.cancel(pgAccountType.getCode(), cancelReqDto);

				log.info("카드 취소 결과 ::: <{}>", cardDto);
			}
		}

		OrderClaimRegisterResponseDto orderClaimRegisterResponseDto = OrderClaimRegisterResponseDto.builder()
				.orderRegistrationResult(claimResult)
				.message(claimResult.getCodeName())
				.odClaimId(claimRequestProcessDto.getOdClaimId())
				.build();

		return ApiResult.success(orderClaimRegisterResponseDto);
	}

	/**
	 * 클레임 상세 신청 데이터 건 수 비교 및 거부 처리
	 * @param requestDto
	 * @param isClaimSave
	 * @return
	 * @throws Exception
	 */
	private boolean checkClaimDetlRequestData(OrderClaimRegisterRequestDto requestDto, boolean isClaimSave, int frontTp) throws Exception {

		log.debug("======================= checkClaimDetlRequestData 클레임 상세 신청 데이터 건 수 비교 START");
		String claimStatusTp = requestDto.getClaimStatusTp();
		String claimStatusCd = requestDto.getClaimStatusCd();

		// 취소 거부 / 반품 거부, 취소 철회 / 반품 철회 일 경우 PASS
		if(OrderEnums.OrderStatus.CANCEL_DENY_DEFE.getCode().equals(claimStatusCd) || OrderEnums.OrderStatus.RETURN_DENY_DEFER.getCode().equals(claimStatusCd) ||
			OrderEnums.OrderStatus.CANCEL_WITHDRAWAL.getCode().equals(claimStatusCd) || OrderEnums.OrderStatus.RETURN_WITHDRAWAL.getCode().equals(claimStatusCd)) {
			return isClaimSave;
		}

		// 클레임 상세 건수 조회
		OrderClaimPriceInfoDto refundInfoDto = claimRequestService.getClaimRefundInfo(requestDto.getOdClaimId());
		if(!ObjectUtils.isEmpty(refundInfoDto)) {
			List<OrderClaimGoodsInfoDto> goodsList = requestDto.getGoodsInfoList();
			// 취소 일 경우
			if (OrderClaimEnums.ClaimStatusTp.CANCEL.getCode().equals(claimStatusTp)) {
				// 현재 클레임 신청 건수와 이전 신청 건수가 동일하지 않을 경우
				// 이전 클레임은 전체 거부 처리 및 클레임 신규 생성 처리
				if (refundInfoDto.getClaimDetlCnt() != goodsList.size()) {
					isClaimSave = this.procClaimDenyDefer(claimStatusTp, claimStatusCd, requestDto, isClaimSave);
				}
				// 현재 클레임 신청 건수와 이전 클레임 신청 건수가 같고
				// 이전 주문상태가 CA 취소요청, 클레임상태가 CC 취소완료 이고
				// 귀책 구분이 다를 경우
				else if(refundInfoDto.getClaimDetlCnt() == goodsList.size() &&
						(OrderEnums.OrderStatus.CANCEL_APPLY.getCode().equals(requestDto.getOrderStatusCd()) &&
						OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(requestDto.getClaimStatusCd())) &&
						!refundInfoDto.getTargetTp().equals(requestDto.getTargetTp())
				) {
					// 해당 클레임 번호의 이전 추가 결제 배송비 존재 여부 확인
					List<OrderClaimAddShippingPaymentInfoDto> addShippingPaymentList = claimRequestService.getOrderReturnsShippingPrice(requestDto.getOdClaimId());
					// 추가 결제한 배송비가 존재할 경우
					if(!addShippingPaymentList.isEmpty()) {
						// 현재 계산 된 배송비가 이전 추가 결제한 배송비 보다 작을 경우 부분 환불 처리
						// 현재 배송비 계산
						OrderClaimPriceInfoDto reAccountShippingPriceInfo = claimRequestProcessBiz.getRefundPriceInfo(requestDto);
						// 현재 추가 배송비 얻기
						int refundShippingPrice = reAccountShippingPriceInfo.getAddPaymentShippingPrice();
						for(OrderClaimAddShippingPaymentInfoDto addShippingPaymentInfo : addShippingPaymentList) {
							// 이전 결제 배송비가 현재 배송비보다 클 경우
							if(addShippingPaymentInfo.getPaymentPrice() > refundShippingPrice) {
								refundShippingPrice = (refundShippingPrice == 0) ? addShippingPaymentInfo.getPaymentPrice() : refundShippingPrice;
								addShippingPaymentInfo.setRefundRequestShippingPrice(refundShippingPrice);
								addShippingPaymentInfo.setOdid(requestDto.getOdid());
								claimCompleteProcessService.setOrderReturnsShippingPrice(addShippingPaymentInfo, frontTp);
								break;
							}
						}
					}
					isClaimSave = true;
				}
			}
			// 반품일 경우
			else if(OrderClaimEnums.ClaimStatusTp.RETURN.getCode().equals(claimStatusTp)) {
				// 반품 신청 상품 수와 현재 상품수가 동일한데, 귀책구분 / 회수여부가 다를 경우
				if (refundInfoDto.getClaimDetlCnt() == goodsList.size() && (!refundInfoDto.getTargetTp().equals(requestDto.getTargetTp()) ||
																			!refundInfoDto.getReturnsYn().equals(requestDto.getReturnsYn()))) {
					// 이전 주문상태가 RA 이고 현재 클레임상태가 RC 일 경우 - 반품 요청 -> 반품 완료 (회수 -> 회수안함, 귀책사유 변경) ||
					// 이전 주문상태가 RA 이고 현재 클레임상태가 RI 일 경우 - 반품 요청 -> 반품 승인 (회수함 - 귀책사유 변경)
					if((OrderEnums.OrderStatus.RETURN_APPLY.getCode().equals(requestDto.getOrderStatusCd()) && OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(requestDto.getClaimStatusCd())) ||
						(OrderEnums.OrderStatus.RETURN_APPLY.getCode().equals(requestDto.getOrderStatusCd()) && OrderEnums.OrderStatus.RETURN_ING.getCode().equals(requestDto.getClaimStatusCd()))) {
						// 이전 추가 결제 배송비 보다 현재 배송비가 작으면 부분환불 처리
						// 해당 클레임 번호의 이전 추가 결제 배송비 존재 여부 확인
						List<OrderClaimAddShippingPaymentInfoDto> addShippingPaymentList = claimRequestService.getOrderReturnsShippingPrice(requestDto.getOdClaimId());
						// 추가 결제한 배송비가 존재할 경우
						if(!addShippingPaymentList.isEmpty()) {
							// 현재 계산 된 배송비가 이전 추가 결제한 배송비 보다 작을 경우 부분 환불 처리
							// 현재 배송비 계산
							OrderClaimPriceInfoDto reAccountShippingPriceInfo = claimRequestProcessBiz.getRefundPriceInfo(requestDto);
							// 현재 추가 배송비 얻기
							int refundShippingPrice = reAccountShippingPriceInfo.getAddPaymentShippingPrice();
							for(OrderClaimAddShippingPaymentInfoDto addShippingPaymentInfo : addShippingPaymentList) {
								// 이전 결제 배송비가 현재 배송비보다 클 경우
								if(addShippingPaymentInfo.getPaymentPrice() > refundShippingPrice) {
									refundShippingPrice = (refundShippingPrice == 0) ? addShippingPaymentInfo.getPaymentPrice() : refundShippingPrice;
									addShippingPaymentInfo.setRefundRequestShippingPrice(refundShippingPrice);
									addShippingPaymentInfo.setOdid(requestDto.getOdid());
									claimCompleteProcessService.setOrderReturnsShippingPrice(addShippingPaymentInfo, frontTp);
									break;
								}
							}
						}
						isClaimSave = true;
					}
				}
				// 반품 신청 상품 수와 현재 상품수가 동일하지 않을 경우
				else if (refundInfoDto.getClaimDetlCnt() != goodsList.size()) {
					// 2. 반품 거부 처리
					isClaimSave = this.procClaimDenyDefer(claimStatusTp, claimStatusCd, requestDto, isClaimSave);
				}
			}
		}
		log.debug("======================= checkClaimDetlRequestData 클레임 상세 신청 데이터 건 수 비교 END");
		return isClaimSave;
	}

	/**
	 * 클레임 거부 처리
	 * @param claimStatusTp
	 * @param claimStatusCd
	 * @param requestDto
	 * @param isClaimSave
	 * @return
	 * @throws Exception
	 */
	public boolean procClaimDenyDefer(String claimStatusTp, String claimStatusCd, OrderClaimRegisterRequestDto requestDto, boolean isClaimSave) throws Exception {

		ClaimRequestProcessDto claimRequestProcessDto = null;
		OrderEnums.OrderRegistrationResult claimResult = null;

		// 기본은 취소거부
		String chgClaimStatusCd = OrderEnums.OrderStatus.CANCEL_DENY_DEFE.getCode();
		if (OrderClaimEnums.ClaimStatusTp.RETURN.getCode().equals(claimStatusTp)) {
			// 반품일 경우 반품거부
			chgClaimStatusCd = OrderEnums.OrderStatus.RETURN_DENY_DEFER.getCode();
		}
		//requestDto.setRejectReasonMsg("");
		requestDto.setClaimStatusCd(chgClaimStatusCd);

		// 거부 처리
		claimRequestProcessDto = claimRequestProcessBiz.claimRequestProcess(claimStatusTp, chgClaimStatusCd, requestDto, true);
		claimResult = claimRequestProcessDto.getClaimResult();

		// 요청성공일경우
		if (OrderEnums.OrderRegistrationResult.SUCCESS.getCode().equals(claimResult.getCode())) {
			// 클레임 완료 처리
			ClaimCompleteProcessDto claimCompleteProcessDto = claimCompleteProcessBiz.claimCompleteProcess(claimStatusTp, chgClaimStatusCd, requestDto, true);
			claimResult = claimCompleteProcessDto.getClaimResult();
			log.debug("=============================클레임 거부 처리 결과: " + claimResult.getCode());

			requestDto.setOdClaimId(0);
			isClaimSave = false;
		}
		requestDto.setClaimStatusCd(claimStatusCd);
		return isClaimSave;
	}

	/**
	 * 프론트 화면에서 주문취소 또는 부분취소를 할 때
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
	public ApiResult<?> orderClaimInfo(OrderClaimRegisterRequestDto requestDto) throws Exception {


		OrderClaimRegisterResponseDto orderClaimRegisterResponseDto = OrderClaimRegisterResponseDto.builder()
				.orderRegistrationResult(OrderEnums.OrderRegistrationResult.SUCCESS)
				.build();
		return ApiResult.success(orderClaimRegisterResponseDto);
	}


	/**
	 * 프론트 화면에서 추가 결제 할 때
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
	public ApiResult<?> addPayment(OrderClaimRegisterRequestDto requestDto) throws Exception {
		log.debug("프론트 추가결제 파라미터 :: <{}>", requestDto);

		if(!requestDto.isNonMember()) {
			BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
			log.debug("urUserId :: <{}>", buyerVo.getUrUserId());
			requestDto.setUrUserId(buyerVo.getUrUserId());
			requestDto.setCustomUrUserId(buyerVo.getUrUserId());
			requestDto.setLoginId(buyerVo.getLoginId());
		}
		requestDto.setReturnsYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode());
		// 1. 추가 결제 데이터 테이블 내 JSON 형태 저장 처리
		String reqJson = new Gson().toJson(requestDto);
		log.debug("reqJson :: <{}>", reqJson);
		OdAddPaymentReqInfo odAddPaymentReqInfo = OdAddPaymentReqInfo.builder()
																		.reqJsonInfo(reqJson)
																		.build();
		claimProcessService.addOdAddPaymentReqInfo(odAddPaymentReqInfo);
		log.debug("추가결제 요청 정보 PK :: <{}>", odAddPaymentReqInfo.getOdAddPaymentReqInfoId());
		requestDto.setOdAddPaymentReqInfoId(odAddPaymentReqInfo.getOdAddPaymentReqInfoId());
		// 2. 해당 키값으로 PG 정보 Set
		BasicDataResponseDto resDto = claimUtilRefundService.addPayment(requestDto, false);
		return ApiResult.success(resDto);
	}

	/**
	 * 비인증 결제 처리
	 * @param orderClaimCardPayRequestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResult<?> addCardPayOrderCreate(OrderClaimCardPayRequestDto orderClaimCardPayRequestDto) throws Exception {

		// 1. 주문번호, 주문상세번호 목록으로 주문자 정보 조회 및 상품 정보 조회
		OrderInfoDto orderInfoDto = claimProcessService.getOrderBuyerInfo(orderClaimCardPayRequestDto);
		// 주문자 정보가 존재하지 않을 경우 오류 return
		if(ObjectUtils.isEmpty(orderInfoDto)) {
			return ApiResult.fail();
		}
		orderClaimCardPayRequestDto.setOdid(orderInfoDto.getOdid());			// 주문번호
		orderClaimCardPayRequestDto.setBuyerNm(orderInfoDto.getBuyerNm());		// 주문자명
		orderClaimCardPayRequestDto.setBuyerMail(orderInfoDto.getBuyerMail());	// 주문자메일
		orderClaimCardPayRequestDto.setBuyerHp(orderInfoDto.getBuyerHp());		// 주문자연락처
		orderClaimCardPayRequestDto.setGoodsNm(orderInfoDto.getGoodsNm());		// 상품명
		// 2. 결제 처리
		OrderClaimCardPayResponseDto orderClaimCardPayResponseDto = claimUtilProcessService.addCardPayOrderCreate(orderClaimCardPayRequestDto);
		// 3. 추가결제 요청 정보 Key값 return
		return ApiResult.success(orderClaimCardPayResponseDto);
	}

	/**
	 * 가상계좌 입금
	 * @param orderClaimCardPayRequestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResult<?> addBankBookOrderCreate(OrderClaimCardPayRequestDto orderClaimCardPayRequestDto) throws Exception {
		// 1. 주문번호, 주문상세번호 목록으로 주문자 정보 조회 및 상품 정보 조회
		OrderInfoDto orderInfoDto = claimProcessService.getOrderBuyerInfo(orderClaimCardPayRequestDto);
		// 주문자 정보가 존재하지 않을 경우 오류 return
		if(ObjectUtils.isEmpty(orderInfoDto)) {
			return ApiResult.fail();
		}
		orderClaimCardPayRequestDto.setBuyerNm(orderInfoDto.getBuyerNm());		// 주문자명
		orderClaimCardPayRequestDto.setBuyerMail(orderInfoDto.getBuyerMail());	// 주문자메일
		orderClaimCardPayRequestDto.setBuyerHp(orderInfoDto.getBuyerHp());		// 주문자연락처
		orderClaimCardPayRequestDto.setGoodsNm(orderInfoDto.getGoodsNm());		// 상품명
		orderClaimCardPayRequestDto.setLoginId(orderInfoDto.getLoginId());		// 로그인ID
		// 2. 가상계좌 채번 처리
		OrderClaimCardPayResponseDto orderClaimCardPayResponseDto = claimUtilProcessService.addBankBookOrderCreate(orderClaimCardPayRequestDto);
		// 3. 추가결제 요청 정보 Key값 return
		return ApiResult.success(orderClaimCardPayResponseDto);
	}

	/**
	 * 주문PK, 쿠폰발급PK로 주문클레임 상세 할인금액 조회
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @param odClaimDetlIds
	 * @return int
	 */
	@Override
	public int getOrderClaimDetlDiscountPrice(Long odOrderId, Long pmCouponIssueId, Long odClaimId, List<Long> odClaimDetlIds) {
		return claimProcessService.getOrderClaimDetlDiscountPrice(odOrderId, pmCouponIssueId, odClaimId, odClaimDetlIds);
	}

	/**
	 * 추가 결제 정보 조회
	 * @param odAddPaymentReqInfoId
	 * @return
	 */
	@Override
	public OdAddPaymentReqInfo getOdAddPaymentReqInfo(long odAddPaymentReqInfoId) {
		return claimProcessService.getOdAddPaymentReqInfo(odAddPaymentReqInfoId);
	}

	/**
	 * 녹즙 취소/반품 처리
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> addOrderClaimGreenJuice(OrderClaimRegisterRequestDto requestDto) throws Exception {

		claimUtilProcessService.getUserInfo(requestDto);

		// request객체 변환
		OrderClaimViewRequestDto claimViewReqDto = claimUtilProcessService.getOrderClaimViewReq(requestDto);

		// 상품목록 얻기
		List<OrderClaimGoodsInfoDto> goodsList = claimRequestService.getGreenJuiceGoodInfoList(claimViewReqDto);

		// 환불 정보 얻기
		OrderClaimPriceInfoDto refundInfoDto = claimRequestService.getGreenJuiceRefundPriceInfo(claimViewReqDto, goodsList);

		// 환불 정보 Set
		claimProcessService.setRequestInfoFromRefundPrice(requestDto, refundInfoDto);

		// 녹즙 스케쥴 정보 Set
		claimRequestService.setOrderClaimGreenJuiceScheduleList(requestDto, claimViewReqDto);

		// 녹즙 스케쥴 정보 업데이트
		int updateCnt = claimProcessService.putOdOrderDetlDailySch(requestDto);
		if(updateCnt < 1) {
			throw new BaseException(OrderClaimEnums.GreenJuiceRegistError.GOODS_SCHEDULE_NONE);	// 취소 가능 스케쥴 정보 미존재
		}

		// 클레임 정보 생성
		ClaimVo claimVo = claimUtilProcessService.setClaimVo(requestDto, goodsList);
		claimProcessService.addOrderClaim(claimVo);
		requestDto.setOdClaimId(claimVo.getOdClaimId());

		// 녹즙 클레임 상세 정보 생성
		claimProcessService.createGreenJuiceOrderClaimDetlInfo(requestDto, refundInfoDto);

		// 환불 처리
		claimUtilProcessService.setGreenJuiceStatusCompleteProcess(requestDto, true);

		UserVo userVo = SessionUtil.getBosUserVO();
		requestDto.setUrUserId(userVo.getUserId());
		// BOS 클레임 사유 변경 이력 저장
		claimProcessService.addOrderClaimBosReasonHist(requestDto);
		
		
		//하이톡 클레임 중계서버 인터페이스 막음
		// 정보 등록 / 수정 후 API연동 처리
		/*
		boolean isSuccess = hitokApiBiz.addHitokReturnOrderIfCustordInpByErp(claimProcessService.getErpServiceType(requestDto), requestDto.getOdClaimId());
		log.debug("HI TOK ERP RESULT -- <{}> --", isSuccess);
		if(!isSuccess) {
			throw new BaseException(OrderClaimEnums.GreenJuiceRegistError.ERP_IF_FAIL);	// 하이톡 API 응답 실패
		}
		*/
		
		//하이톡 클레임 중계서버 인터페이스 막음으로 클레임 완료처리 만 활성화(취소/반품 인터페이스 오픈시 이 부분은 삭제)
		hitokApiBiz.addHitokReturnOrderClaimComplete(requestDto.getOdClaimId());

		
		// 주문 상세 정보 클레임 수량 업데이트 - 재배송이 아닐 경우에만 처리
		// 하이톡 연동 후 주문 상세 취소 수량을 업데이트 쳐 주어야 함
		if(!OrderEnums.OrderStatus.EXCHANGE_COMPLETE.getCode().equals(requestDto.getClaimStatusCd())) {
			claimProcessService.putOrderDetlCancelCnt(requestDto);
		}

		OrderClaimRegisterResponseDto orderClaimRegisterResponseDto = OrderClaimRegisterResponseDto.builder()
																									.orderRegistrationResult(OrderEnums.OrderRegistrationResult.SUCCESS)
																									.build();
		return ApiResult.success(orderClaimRegisterResponseDto);
	}

	/**
	 * 주문 상세 클레임 상품 정보 > BOS 클레임 사유 변경
	 * @param orderClaimRegisterRequestDto
	 * @return
	 * @throws Exception
	 */
    @Override
    public ApiResult<?> putOrderClaimDetlBosClaimReason(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception{
		int result = claimProcessService.putOrderClaimDetlBosClaimReason(orderClaimRegisterRequestDto);
		if(result > 0) {
			//세션 정보 불러오기
			UserVo userVo = SessionUtil.getBosUserVO();
			orderClaimRegisterRequestDto.setUrUserId(userVo.getUserId());

			claimProcessService.addOrderClaimBosReasonHist(orderClaimRegisterRequestDto);
			
			// BOS클레임사유 이력 등록 (테이블 : OD_CLAIM_DETL_HIST)
			List<OrderClaimGoodsInfoDto> list = orderClaimRegisterRequestDto.getGoodsInfoList();

			for (OrderClaimGoodsInfoDto dto : list) {
				ClaimDetlHistVo claimDetlHistVo = ClaimDetlHistVo.builder()
						.odClaimDetlId(dto.getOdClaimDetlId())	// 클레임상세PK
						.statusCd(dto.getClaimStatusCd())		// 변경상태값
						.histMsg(dto.getHistMsg())				// 처리이력내용
						.createId(Long.parseLong(userVo.getUserId()))	// 등록자
						.build();

				claimProcessService.putClaimDetailStatusHist(claimDetlHistVo);
			}
			
			return ApiResult.success();
		}else {
			return ApiResult.fail();
		}
    }

	/**
	 * MALL > 상담접수상태의 렌탈상품 취소
	 * @param orderClaimRegisterRequestDto
	 * @return
	 * @throws Exception
	 */
    @Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> addOrderClaimRental(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception{

    	// 1. 주문취소 대상 상품 목록 조회 requestDto set
		OrderClaimViewRequestDto orderClaimViewRequestDto = claimProcessService.setOrderClaimInfoRequestDto(orderClaimRegisterRequestDto);

		// 2. 주문취소 대상 상품 목록 조회
		ApiResult<?> result = claimRequestBiz.getOrderClaimInfo(orderClaimViewRequestDto);
		OrderClaimViewResponseDto orderClaimResDto = (OrderClaimViewResponseDto)result.getData();

		// 3. 주문취소 requestDto set
		claimProcessService.setOrderClaimRequestDto(orderClaimRegisterRequestDto,orderClaimResDto);

		// 4. 주문취소
    	return addOrderClaim(orderClaimRegisterRequestDto);
	}

	/**
	 * 주문클레임정보 업데이트 처리
	 * @param mallOrderClaimAddPaymentResult
	 * @return
	 */
	public OrderClaimRegisterResponseDto putOrderClaimInfo(MallOrderClaimAddPaymentResult mallOrderClaimAddPaymentResult) throws Exception {
		return claimProcessService.putOrderClaimInfo(mallOrderClaimAddPaymentResult);
	}

	/**
	 * CS 환불 정보 등록
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
	public ApiResult<?> addOrderCSRefundRegister(OrderCSRefundRegisterRequestDto reqDto) throws Exception {
		log.debug("--------------------------- CS환불 정보 등록 START");
		log.debug("--------------------------- 1. 유효성체크");
		claimProcessService.validCSRefundData(reqDto);

		// Mall클레임사유가 존재할 경우
		if(!StringUtil.isEmpty(reqDto.getPsClaimMallId())) {
			log.debug("--------------------------- 2. BOS클레임 정보 조회");
			// 2. psClaimMallId로 BOS클레임 정보 조회
//			PolicyClaimMallVo policyClaimMallVo = new PolicyClaimMallVo();
//			policyClaimMallVo.setPsClaimMallId(Long.parseLong(reqDto.getPsClaimMallId()));
//			PolicyClaimMallDetailResponseDto resultResponseDto = (PolicyClaimMallDetailResponseDto) policyClaimBiz.getFrontPolicyClaimMall(policyClaimMallVo).getData();
//			PolicyClaimMallVo claimBosresultVo = resultResponseDto.getRows();
//			if (!ObjectUtils.isEmpty(claimBosresultVo)) {
//				reqDto.setTargetTp(claimBosresultVo.getTargetType());
//				// 상품 목록 내 BOS클레임사유 Set
//				claimProcessService.setBosClaimReasonByGoodsInfoList(reqDto, claimBosresultVo);
//			}
		}

		log.debug("--------------------------- 3. CS환불정보 등록");
		// 1. CS환불정보 등록
		claimProcessService.addOrderCSRefundInfo(reqDto);

		log.debug("--------------------------- 4. CS환불상세정보 등록");
		// 3. CS환불상세정보 등록
		claimProcessService.addOrderCSRefundInfoDetl(reqDto);

		// 5. 예치금환불일 경우 CS환불계좌정보 등록
		log.debug("--------------------------- 5. CS환불 환불계좌 정보 등록");
		if (OrderCsEnums.CsRefundTp.PAYMENT.getCode().equals(reqDto.getCsRefundTp())) {
			claimProcessService.addOrderCSRefundAccountInfo(reqDto);
		}

		// CS환불 금액 환불 처리
		this.procCSRefund(reqDto);

		log.debug("--------------------------- 8. CS환불 상세이력 정보 등록");
		// 8. CS환불상세이력정보 등록
		claimProcessService.addOrderCSRefundInfoDetlHist(reqDto);

		return ApiResult.success();
	}

	/**
	 * 주문 클레임 CS환불승인상태 변경
	 * @param reqDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
	public ApiResult<?> putOrderClaimCsRefundApproveCd(OrderCSRefundRegisterRequestDto reqDto) throws Exception {
		UserVo userVo = SessionUtil.getBosUserVO();
		long userId = Long.parseLong(userVo.getUserId());
		reqDto.setUrUserId(userId);

		// 1. 클레임 정보 수정
		claimProcessService.putOrderCSRefundInfo(reqDto);

		// 2. CS환불 정보 조회
		OrderCSRefundInfoDto csRefundInfo = claimProcessService.getCSRefundInfo(reqDto);
		reqDto.setCsRefundTp(csRefundInfo.getCsRefundTp());

		// 3. 클레임 상세 정보 조회
		List<OrderCSRefundGoodsInfoDto> goodsInfoList = claimProcessService.getCSRefundInfoDetl(reqDto);
		reqDto.setGoodsInfoList(goodsInfoList);

		// 4. CS환불상세이력정보 등록
		claimProcessService.addOrderCSRefundInfoDetlHist(reqDto);

		return ApiResult.success();
	}

	/**
	 * CS 환불 승인 처리
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
	public ApiResult<?> procOrderCSRefundApprove(OrderCSRefundRegisterRequestDto reqDto) throws Exception {
		log.debug("--------------------------- CS환불 승인 처리 START");

		// 1. CS환불 정보 조회
		OrderCSRefundInfoDto csRefundInfo = claimProcessService.getCSRefundInfo(reqDto);
		reqDto.setOdOrderId(csRefundInfo.getOdOrderId());
		reqDto.setOdid(csRefundInfo.getOdid());
		reqDto.setCsRefundTp(csRefundInfo.getCsRefundTp());
		reqDto.setRefundPrice(csRefundInfo.getRefundPrice());
		reqDto.setBankCd(csRefundInfo.getBankCd());
		reqDto.setAccountHolder(csRefundInfo.getAccountHolder());
		reqDto.setAccountNumber(csRefundInfo.getAccountNumber());

		// 2. 클레임 상세 정보 조회
		List<OrderCSRefundGoodsInfoDto> goodsInfoList = claimProcessService.getCSRefundInfoDetl(reqDto);
		reqDto.setGoodsInfoList(goodsInfoList);

		log.debug("--------------------------- 3. CS환불정보 수정");
		// 3. CS환불정보 수정
//		claimProcessService.putOrderCSRefundInfo(reqDto);

		// CS환불 금액 환불 처리
		this.procCSRefund(reqDto);

		log.debug("--------------------------- 8. CS환불 상세이력 정보 등록");
		// 8. CS환불상세이력정보 등록
		claimProcessService.addOrderCSRefundInfoDetlHist(reqDto);

		return ApiResult.success();
	}

	/**
	 * CS환불 금액 환불 처리
	 * @param reqDto
	 * @throws Exception
	 */
	private void procCSRefund(OrderCSRefundRegisterRequestDto reqDto) throws Exception {

		// CS환불 승인 상태가 승인완료 일 경우
		if(OrderCsEnums.CsRefundApprCd.APPROVED.getCode().equals(reqDto.getCsRefundApproveCd())) {
			// 6. 예치금환불일 경우
			if (OrderCsEnums.CsRefundTp.PAYMENT.getCode().equals(reqDto.getCsRefundTp())) {
				log.debug("--------------------------- 6. CS환불 환불금액 송금 처리");
				claimUtilRefundService.csRefundKcpBankSendProc(reqDto);
			}
			// 6. 적립금환불일 경우
			else {
				log.debug("--------------------------- 6. CS환불 적립금지급 처리");
				// 주문 사용자 정보 조회
				String customUserId = String.valueOf(claimRequestService.getOrderUrUserId(reqDto.getOdOrderId()));
				// 로그인 사용자 회계 코드 조회
				OrderCSUseFinInfoDto finInfo = claimProcessService.getUserFinInfoByLoginId(reqDto.getUrUserId());
				boolean isCsRoleFlag = !ObjectUtils.isEmpty(finInfo);
				String finOrganizationCd = isCsRoleFlag ? finInfo.getFinOrganizationCd() : "0";
				ApiResult result = pointBiz.depositCsRefundOrderPoint(Long.parseLong(customUserId), 								// 주문자 회원PK
																		reqDto.getOdid(), 											// 주문번호
																		Long.parseLong(String.valueOf(reqDto.getRefundPrice())), 	// 적립금액
																		isCsRoleFlag, 												// CS환불 역할그룹 여부
																		finOrganizationCd,											// 회계코드
																		reqDto.getCsPointValidityDay());                            // CS 환불 적립금 유효일
				// 포인트 적립 실패일 경우
				if(!BaseEnums.Default.SUCCESS.getCode().equals(result.getCode())){
					throw new BaseException(result.getMessage());
				}
			}

			log.debug("--------------------------- 7. CS환불PG정보 등록");
			// 7. PG정보 등록
			claimProcessService.addOrderCSRefundPGInfo(reqDto);
		}
	}

	/**
	 * 클레임 OD_PAYMENT 환불 정보 Set
	 * @param odOrderId
	 * @param odClaimId
	 * @throws Exception
	 */
	public void setClaimRefundPaymentInfo(long odOrderId, long odClaimId, String claimStatusCd) throws Exception {
		log.debug("-------------------------- 추가 배송비 직접 결제 환불 처리 START");
		String customUrUserId = String.valueOf(claimRequestService.getOrderUrUserId(odOrderId));
		OrderClaimRegisterRequestDto claimViewReqDto = claimProcessService.getClaimRefundPaymentInfo(odOrderId, odClaimId);
		claimViewReqDto.setFrontTp(OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_FRONT.getCodeValue());
		claimViewReqDto.setClaimStatusCd(claimStatusCd);
		claimViewReqDto.setUrUserId(customUrUserId);
		claimViewReqDto.setCustomUrUserId(customUrUserId);
		claimViewReqDto.setStatus(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode());
		List<OrderClaimGoodsInfoDto> goodsInfoList = claimProcessService.getClaimDetlRefundPaymentInfo(odOrderId, odClaimId);
		claimViewReqDto.setGoodsInfoList(goodsInfoList);

		OrderClaimPriceInfoDto refundInfoDto = claimRequestProcessBiz.getRefundPriceInfo(claimViewReqDto);

		claimUtilProcessService.setStatusAddPaymentCompleteProcess(claimViewReqDto, refundInfoDto);
		log.debug("-------------------------- 추가 배송비 직접 결제 환불 처리 END");
	}

	/**
	 * 추가 결제 클레임 정보 조회
	 * @param pgObject
	 * @return
	 */
	public OrderClaimAddPaymentInfoDto getAddPaymentClaimInfo(Object pgObject) throws Exception {
		// PG사별 데이터 변환 DTO 얻기
		OrderClaimAddPaymentPgInfoDto pgIntegratedInfo = claimProcessService.getChgClaimAddPaymentPgIntegrated(pgObject);
		// 추가 결제 정보 조회
		OrderClaimAddPaymentInfoDto claimAddPaymentInfo = claimProcessService.getAddPaymentClaimInfo(pgIntegratedInfo);
		// 추가 결제 정보가 존재할 경우에만 PG사별 변환 데이터 Set
		if(!ObjectUtils.isEmpty(claimAddPaymentInfo)) {
			claimAddPaymentInfo.setPgIntegratedInfo(pgIntegratedInfo);
		}
		return claimAddPaymentInfo;
	}

	/**
	 * 추가 결제 클레임 정보 업데이트
	 * @param orderClaimAddPaymentInfoDto
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
	public void putAddPaymentClaimInfo(OrderClaimAddPaymentInfoDto orderClaimAddPaymentInfoDto) throws Exception {
		log.debug("-------------------------- 추가 배송비 가상 계좌 입금처리 START");
		// PG 정보 가져오기
		OrderClaimAddPaymentPgInfoDto pgIntegratedInfo = orderClaimAddPaymentInfoDto.getPgIntegratedInfo();

		String claimStatusCd = OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode();
		// 주문 클레임 상태 구분이 반품일 경우
		if(OrderClaimEnums.ClaimStatusTp.RETURN.getCode().equals(orderClaimAddPaymentInfoDto.getClaimStatusTp())) {
			claimStatusCd = OrderEnums.OrderStatus.RETURN_COMPLETE.getCode();
			// 회수 여부가 Y 일 경우 반품 승인
			if(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode().equals(orderClaimAddPaymentInfoDto.getReturnsYn())) {
				claimStatusCd = OrderEnums.OrderStatus.RETURN_ING.getCode();
			}
		}

		// 1. 등록된 환불 금액 환불 처리
		log.debug("---------------------------- 등록된 환불 금액 환불 처리");
		this.setClaimRefundPaymentInfo(orderClaimAddPaymentInfoDto.getOdOrderId(), orderClaimAddPaymentInfoDto.getOdClaimId(), claimStatusCd);
		// 2. 클레임 상세 정보 업데이트
		ClaimDetlVo claimDetlVo = ClaimDetlVo.builder()
											.odClaimId(orderClaimAddPaymentInfoDto.getOdClaimId())	// 주문클레임PK
											.claimStatusCd(claimStatusCd)							// 클레임상태코드
											.ccId(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(claimStatusCd)	? Constants.VIRTUAL_ACCOUNT_USER_ID : 0)	// 취소완료ID
											.riId(OrderEnums.OrderStatus.RETURN_ING.getCode().equals(claimStatusCd)			? Constants.VIRTUAL_ACCOUNT_USER_ID : 0)	// 반품승인ID
											.rcId(OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(claimStatusCd)	? Constants.VIRTUAL_ACCOUNT_USER_ID : 0)	// 반품완료ID
											.build();
		claimProcessService.putOrderClaimDetlDirectPaymentClaimStatus(claimDetlVo);
		// 3. 결제 마스터 정보 업데이트 : IR -> IC
		log.debug("---------------------------- 결제 마스터 정보 업데이트 처리");
		OrderPaymentMasterVo orderPaymentMasterVo = OrderPaymentMasterVo.builder()
																		.odPaymentMasterId(orderClaimAddPaymentInfoDto.getOdPaymentMasterId())
																		.status(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode())
																		.approvalDt(pgIntegratedInfo.getApprovalDate())
																		.build();
		claimProcessService.putAddPaymentPayStatus(orderPaymentMasterVo);
		// 4. 클레임이력 등록
		log.debug("---------------------------- 클레임이력 등록 처리");
		ClaimDetlHistVo claimDetlHistVo = ClaimDetlHistVo.builder()
															.odClaimId(orderClaimAddPaymentInfoDto.getOdClaimId())
															.statusCd(claimStatusCd)
															.histMsg("추가 배송비 가상계좌 입금 확인")
															.createId(Constants.VIRTUAL_ACCOUNT_USER_ID)
															.build();
		claimProcessService.addOdClaimDetlHistByOdClaimId(claimDetlHistVo);
		// 5. 현금영수증 등록 처리 (?)
		log.debug("---------------------------- 현금영수증 등록 처리");
		if (OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode().equals(pgIntegratedInfo.getCashReceiptYn())) {
			OrderCashReceiptVo orderCashReceiptVo = OrderCashReceiptVo.builder()
																		.odOrderId(orderClaimAddPaymentInfoDto.getOdOrderId())
																		.odPaymentMasterId(orderClaimAddPaymentInfoDto.getOdPaymentMasterId())
																		.cashReceiptType(pgIntegratedInfo.getCashReceiptType())
																		.cashReceiptNo(pgIntegratedInfo.getCashReceiptNo())
																		.cashReceiptAuthNo(pgIntegratedInfo.getCashReceiptAuthNo())
																		.build();
			orderRegistrationBiz.addOrderCashReceipt(orderCashReceiptVo);
		}
		log.debug("-------------------------- 추가 배송비 가상 계좌 입금처리 END");
	}

	/**
	 * 쿠폰 정보 조회
	 * @param goodsCouponDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<OrderClaimCouponInfoDto> getCouponInfoList(OrderClaimViewRequestDto goodsCouponDto) throws Exception {
		return claimRequestService.getCouponInfoList(goodsCouponDto);
	}

	/**
	 * 프론트 취소일 경우 쿠폰 재발급 요청값 확인
	 * @param requestDto
	 * @throws Exception
	 */
	private void isPossibleRefundCoupon(OrderClaimRegisterRequestDto requestDto) throws Exception {

		if(CollectionUtils.isNotEmpty(requestDto.getGoodsInfoList())){

			// 1. 상품쿠폰
			List<OrderClaimSearchGoodsDto> goodSearchListForGoodsCoupon = new ArrayList<>();
			for(OrderClaimGoodsInfoDto goodsDto : requestDto.getGoodsInfoList()){
				// 수량 전체 취소일 경우만
				if(goodsDto.getOrderCnt() - goodsDto.getClaimCnt() - goodsDto.getCancelCnt() == 0){
					OrderClaimSearchGoodsDto searchGoodsDto = new OrderClaimSearchGoodsDto();
					searchGoodsDto.setOdOrderDetlId(goodsDto.getOdOrderDetlId());
					goodSearchListForGoodsCoupon.add(searchGoodsDto);
				}
			}
			
			// 주문상품에 쓰인 상품쿠폰 조회
			if(CollectionUtils.isNotEmpty(goodSearchListForGoodsCoupon)){

				OrderClaimViewRequestDto goodsCouponDto = new OrderClaimViewRequestDto();
				goodsCouponDto.setCouponTp(OrderClaimEnums.ClaimCouponTp.COUPON_GOODS.getCode());
				goodsCouponDto.setGoodSearchList(goodSearchListForGoodsCoupon);
				goodsCouponDto.setGoodsChange(Integer.parseInt(ClaimEnums.ClaimGoodsChangeType.ALL_CANCEL.getCode()));
				goodsCouponDto.setOdOrderId(requestDto.getOdOrderId());
				List<OrderClaimCouponInfoDto> goodsCouponList = claimRequestService.getCouponInfoList(goodsCouponDto);

				if(CollectionUtils.isNotEmpty(goodsCouponList)){

					if(CollectionUtils.isEmpty(requestDto.getGoodsCouponInfoList())){
						throw new BaseException(OrderClaimEnums.RefundCouponError.FAIL);
					}

					List<Long> goodsCouponOdOrderDetlIdList = goodsCouponList.stream().map(m->m.getOdOrderDetlId()).collect(Collectors.toList());
					List<Long> reqGoodsCouponOdOrderDetlIdList = requestDto.getGoodsCouponInfoList().stream().map(m->m.getOdOrderDetlId()).collect(Collectors.toList());

					boolean goodsCouponResult = reqGoodsCouponOdOrderDetlIdList.containsAll(goodsCouponOdOrderDetlIdList);

					// 재발급 가능한 쿠폰 있는데 프론트에서 요청값으로 쿠폰정보가 넘어오지 않은 경우
					if(!goodsCouponResult){
						throw new BaseException(OrderClaimEnums.RefundCouponError.FAIL);
					}
				}
			}


			// 2. 장바구니 쿠폰
			List<OrderClaimSearchGoodsDto> goodSearchListForCartCoupon = new ArrayList<>();
			for(OrderClaimGoodsInfoDto goodsDto : requestDto.getGoodsInfoList()){
				OrderClaimSearchGoodsDto searchGoodsDto = new OrderClaimSearchGoodsDto();
				searchGoodsDto.setOdOrderDetlId(goodsDto.getOdOrderDetlId());
				goodSearchListForCartCoupon.add(searchGoodsDto);
			}

			// 주문상품에 쓰인 장바구니쿠폰 조회
			OrderClaimViewRequestDto cartCouponDto = new OrderClaimViewRequestDto();
			cartCouponDto.setCouponTp(OrderClaimEnums.ClaimCouponTp.COUPON_CART.getCode());
			cartCouponDto.setGoodSearchList(goodSearchListForCartCoupon);
			cartCouponDto.setGoodsChange(Integer.parseInt(ClaimEnums.ClaimGoodsChangeType.ALL_CANCEL.getCode()));
			cartCouponDto.setOdOrderId(requestDto.getOdOrderId());
			List<OrderClaimCouponInfoDto> cartCouponList = claimRequestService.getCouponInfoList(cartCouponDto);

			if(CollectionUtils.isNotEmpty(cartCouponList)){

				if(CollectionUtils.isEmpty(requestDto.getCartCouponInfoList())) {
					throw new BaseException(OrderClaimEnums.RefundCouponError.FAIL);
				}

				List<Long> cartCouponOdOrderDetlIdList = cartCouponList.stream().map(m->m.getOdOrderDetlId()).collect(Collectors.toList());
				List<Long> reqCartCouponOdOrderDetlIdList = requestDto.getCartCouponInfoList().stream().map(m->m.getOdOrderDetlId()).collect(Collectors.toList());

				boolean cartCouponResult = cartCouponOdOrderDetlIdList.containsAll(reqCartCouponOdOrderDetlIdList);

				// 재발급 가능한 쿠폰 있는데 프론트에서 요청값으로 쿠폰정보가 넘어오지 않은 경우
				if(!cartCouponResult){
					throw new BaseException(OrderClaimEnums.RefundCouponError.FAIL);
				}
			}

		}

	}

	/**
	 * 주문클레임 상세 할인정보 시퀀스 조회
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
	public long getOdClaimDetlDiscountId() throws Exception {
		return claimProcessService.getOdClaimDetlDiscountId();
	}
}

