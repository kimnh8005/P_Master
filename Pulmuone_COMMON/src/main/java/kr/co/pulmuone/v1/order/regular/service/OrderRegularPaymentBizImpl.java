package kr.co.pulmuone.v1.order.regular.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.PgEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.service.ClaimProcessBiz;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDtVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultPaymentListDto;
import kr.co.pulmuone.v1.order.regular.dto.vo.*;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpPaymentRegularBatchKeyRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpPaymentRegularBatchKeyResponseDto;
import kr.co.pulmuone.v1.pg.service.kcp.service.KcpPgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송주문결제 Biz OrderRegularPaymentBizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.09.23	  김명진           최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
public class OrderRegularPaymentBizImpl implements OrderRegularPaymentBiz {

	@Autowired
	private OrderRegularPaymentService orderRegularPaymentService;

	@Autowired
	private OrderRegularOrderCreateService orderRegularOrderCreateService;

	@Autowired
	private OrderRegularDetailBiz orderRegularDetailBiz;

	@Autowired
	private KcpPgService kcpPgService;

	@Autowired
	private ClaimProcessBiz claimProcessBiz;

	@Autowired
	private OrderEmailBiz orderEmailBiz;

	@Autowired
	private OrderEmailSendBiz orderEmailSendBiz;

	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 정기배송 주문 결제 대상 목록 조회
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<RegularResultPaymentListDto> getRegularOrderResultPaymentList() throws Exception {
		return orderRegularPaymentService.getRegularOrderResultPaymentList(OrderEnums.RegularStatusCd.CANCEL.getCode());
	}

	/**
	 * 정기배송 주문 결제 처리
	 * @param regularResultPaymentItem
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
	public OrderRegularResultVo procOdRegularPayment(RegularResultPaymentListDto regularResultPaymentItem) throws Exception {

		OrderRegularResultVo orderRegularResultVo = new OrderRegularResultVo();
		orderRegularResultVo.setIbFlag(false);

		KcpPaymentRegularBatchKeyRequestDto kcpPaymentRegularBatchKeyRequestDto = new KcpPaymentRegularBatchKeyRequestDto();
		kcpPaymentRegularBatchKeyRequestDto.setPaymentPrice(regularResultPaymentItem.getPaymentPrice());
		kcpPaymentRegularBatchKeyRequestDto.setTaxPaymentPrice(regularResultPaymentItem.getTaxablePrice());
		kcpPaymentRegularBatchKeyRequestDto.setTaxFreePaymentPrice(regularResultPaymentItem.getNonTaxablePrice());
		kcpPaymentRegularBatchKeyRequestDto.setOdid(regularResultPaymentItem.getOdid());
		kcpPaymentRegularBatchKeyRequestDto.setGoodsName(regularResultPaymentItem.getGoodsNm());
		kcpPaymentRegularBatchKeyRequestDto.setBuyerName(regularResultPaymentItem.getBuyerNm());
		kcpPaymentRegularBatchKeyRequestDto.setBuyerEmail(regularResultPaymentItem.getBuyerMail());
		kcpPaymentRegularBatchKeyRequestDto.setBuyerMobile(regularResultPaymentItem.getBuyerHp());
		kcpPaymentRegularBatchKeyRequestDto.setBatchKey(regularResultPaymentItem.getBatchKey());

		// 2. 계산된 금액으로 결제 처리
		KcpPaymentRegularBatchKeyResponseDto kcpPaymentRegularBatchKeyResponseDto = kcpPgService.paymentRegularBatchKey(kcpPaymentRegularBatchKeyRequestDto);
		int failCnt = 0;
		String regularReqGbnCd = OrderEnums.RegularReqGbnCd.IC.getCode();
		String paymentStatus = OrderEnums.OrderStatus.INCOM_COMPLETE.getCode();
		if (!"0000".equals(kcpPaymentRegularBatchKeyResponseDto.getRes_cd())) {
			failCnt++;
			regularReqGbnCd = OrderEnums.RegularReqGbnCd.IF.getCode();
			paymentStatus = OrderEnums.OrderStatus.INCOM_READY.getCode();
		}

		// 3. 주문결제마스터 정보 업데이트
		OrderPaymentMasterVo orderPaymentMasterVo = OrderPaymentMasterVo.builder()
																		.odPaymentMasterId(regularResultPaymentItem.getOdPaymentMasterId())
																		.status(paymentStatus)
																		.payTp(OrderEnums.PaymentType.CARD.getCode())
																		.pgService(PgEnums.PgAccountType.KCP_REGULAR.getCode())
																		.tid(kcpPaymentRegularBatchKeyResponseDto.getTno())
																		.info(kcpPaymentRegularBatchKeyResponseDto.getCard_name())
																		.authCd(kcpPaymentRegularBatchKeyResponseDto.getApp_no())
																		.cardNumber(regularResultPaymentItem.getCardMaskNumber())
																		.cardQuota(kcpPaymentRegularBatchKeyResponseDto.getQuota())
																		.responseData(objectMapper.writeValueAsString(kcpPaymentRegularBatchKeyResponseDto))
																		.build();

		String reqRoundYn = "N";
		// 결제가 정상적으로 되었을 경우
		if(failCnt < 1) {

			// 4. 주문날짜 테이블 UPDATE
			OrderDtVo orderDtVo = OrderDtVo.builder()
					.odOrderId(regularResultPaymentItem.getOdOrderId())
					.icId(Constants.BATCH_CREATE_USER_ID)
					.build();

			orderRegularPaymentService.putOrderDtInfo(orderDtVo);
			reqRoundYn = "Y";

			// 5. 주문상세 테이블 UPDATE
			OrderDetlVo orderDetlVo = OrderDetlVo.builder()
					.odOrderId(regularResultPaymentItem.getOdOrderId())
					.orderStatusCd(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode())
					.build();
			orderRegularPaymentService.putOrderDetlInfo(orderDetlVo);
		}

		// 6. 정기배송 결과 테이블 회차 완료 여부 'Y' UPDATE
		orderRegularResultVo.setOdRegularResultId(regularResultPaymentItem.getOdRegularResultId());
		orderRegularResultVo.setPaymentFailCnt(failCnt);
		orderRegularResultVo.setReqRoundYn(reqRoundYn);

		orderRegularPaymentService.putOrderRegularResultReqRoundYnInfo(orderRegularResultVo);

		// 7. 정기배송주문히스토리 테이블 INSERT
		StringBuffer regularReqCont = new StringBuffer();
		regularReqCont.append("결제 " + (OrderEnums.RegularReqGbnCd.IF.getCode().equals(regularReqGbnCd) ? "실패" : "완료"));
		regularReqCont.append("," + System.getProperty("line.separator"));
		regularReqCont.append("금액 " + StringUtil.numberFormat(regularResultPaymentItem.getPaymentPrice()) + " 원");
		//if(failCnt > 0) {
		if(OrderEnums.RegularReqGbnCd.IF.getCode().equals(regularReqGbnCd)) {
			regularReqCont.append("," + System.getProperty("line.separator"));
			regularReqCont.append("실패 사유[" + kcpPaymentRegularBatchKeyResponseDto.getRes_cd() + "][" + kcpPaymentRegularBatchKeyResponseDto.getRes_msg() + "]");
		}
		if(ObjectUtils.isNotEmpty(kcpPaymentRegularBatchKeyResponseDto.getApp_time())) {
			LocalDateTime localDateTime = LocalDateTime.parse(kcpPaymentRegularBatchKeyResponseDto.getApp_time(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
			String appTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			regularReqCont.append("," + System.getProperty("line.separator"));
			regularReqCont.append("결제 요청 시간[" + appTime + "]");
		}

		OrderRegularReqHistoryVo orderRegularReqHistoryVo = new OrderRegularReqHistoryVo();
		orderRegularReqHistoryVo.setOdRegularReqId(regularResultPaymentItem.getOdRegularReqId());
		orderRegularReqHistoryVo.setRegularReqGbnCd(regularReqGbnCd);
		orderRegularReqHistoryVo.setRegularReqStatusCd(OrderEnums.RegularReqStatusCd.PC.getCode());
		orderRegularReqHistoryVo.setRegularReqCont(regularReqCont.toString());
		orderRegularReqHistoryVo.setCreateId(Constants.BATCH_CREATE_USER_ID);

		orderRegularDetailBiz.putRegularOrderReqHistory(orderRegularReqHistoryVo);

		long odRegularReqId = regularResultPaymentItem.getOdRegularReqId();
		long odRegularResultId = regularResultPaymentItem.getOdRegularResultId();

		int failCntSum = orderRegularResultVo.getPaymentFailCnt() + regularResultPaymentItem.getPrevFailCnt();

		// 8. 4번 실패 시 정기배송 구독 해지
		if(failCntSum == 4) {

			// 9. 정기배송 구독 해지 처리
			// 현재일자와 결제 예정일이 같거나 큰 회차정보 상품 구독 해지 처리
			OrderRegularResultDetlVo orderRegularResultDetlVo = new OrderRegularResultDetlVo();
			orderRegularResultDetlVo.setReqDetailStatusCd(OrderEnums.RegularDetlStatusCd.CANCEL_BUYER.getCode());
			orderRegularResultDetlVo.setOdRegularResultId(odRegularResultId);
			orderRegularPaymentService.putOrderRegularDetlStatusCancel(orderRegularResultDetlVo);

			// 10.현재일자와 결제 예정일이 같거나 큰 회차정보 구독 해지 처리
			orderRegularResultVo = new OrderRegularResultVo();
			orderRegularResultVo.setRegularStatusCd(OrderEnums.RegularStatusCd.CANCEL.getCode());
			orderRegularResultVo.setOdRegularReqId(odRegularReqId);
			orderRegularPaymentService.putOrderRegularStatusCancel(orderRegularResultVo);

			// 11.정기배송 신청 상세 상품 구독 해지 처리
			OrderRegularReqOrderDetlVo orderRegularReqOrderDetlVo = new OrderRegularReqOrderDetlVo();
			orderRegularReqOrderDetlVo.setReqDetailStatusCd(OrderEnums.RegularDetlStatusCd.CANCEL_BUYER.getCode());
			orderRegularReqOrderDetlVo.setOdRegularReqId(odRegularReqId);
			orderRegularPaymentService.putOrderRegularReqDetlStatusCancel(orderRegularReqOrderDetlVo);

			// 12.정기배송 신청 정보 구독 해지 처리
			OrderRegularReqVo orderRegularReqVo = new OrderRegularReqVo();
			orderRegularReqVo.setRegularStatusCd(OrderEnums.RegularStatusCd.CANCEL.getCode());
			orderRegularReqVo.setOdRegularReqId(odRegularReqId);
			orderRegularPaymentService.putOrderRegularReqStatusCancel(orderRegularReqVo);

			// 13. 정기배송 결과 테이블 회차 완료 처리
			orderRegularResultVo.setOdRegularResultId(odRegularResultId);
			orderRegularResultVo.setReqRoundYn("Y");
			orderRegularPaymentService.putOrderRegularResultReqRoundYnInfo(orderRegularResultVo);

			// 14. 정기배송주문히스토리 테이블 INSERT
			orderRegularReqHistoryVo.setOdRegularReqId(odRegularReqId);
			orderRegularReqHistoryVo.setRegularReqGbnCd(OrderEnums.RegularReqGbnCd.GC.getCode());
			orderRegularReqHistoryVo.setRegularReqCont("결제 실패 연속 4회 발생. 상품 구독 해지");
			orderRegularDetailBiz.putRegularOrderReqHistory(orderRegularReqHistoryVo);

			// 15. 입금전 취소 대상 상품PK 목록 조회
			List<Long> ilGoodsIds = orderRegularPaymentService.getOdRegularOrderDetlGoodsList(odRegularResultId);
			// 16. 입금전 취소 처리
			orderRegularResultVo.setIbFlag(true);
			orderRegularResultVo.setIbIlGoodsIds(ilGoodsIds);
			orderPaymentMasterVo.setStatus(OrderEnums.OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode());
		}
		// 8. 2번 실패 시 해당 회차 건너뛰기
		else if(orderRegularResultVo.getPaymentFailCnt() == 2) {

			// 9. 정기배송 결과 상세 테이블 해당 회차 건너뛰기 처리
			OrderRegularResultDetlVo orderRegularResultDetlVo = new OrderRegularResultDetlVo();
			orderRegularResultDetlVo.setReqDetailStatusCd(OrderEnums.RegularDetlStatusCd.SKIP.getCode());
			orderRegularResultDetlVo.setOdRegularResultId(odRegularResultId);
			orderRegularPaymentService.putOrderRegularDetlStatusSkip(orderRegularResultDetlVo);

			orderRegularResultVo = new OrderRegularResultVo();
			// 10. 정기배송 결과 테이블 회차 완료 처리
			orderRegularResultVo.setOdRegularResultId(odRegularResultId);
			orderRegularResultVo.setReqRoundYn("Y");
			orderRegularPaymentService.putOrderRegularResultReqRoundYnInfo(orderRegularResultVo);

			// 11. 정기배송주문히스토리 테이블 INSERT
			orderRegularReqHistoryVo.setOdRegularReqId(odRegularReqId);
			orderRegularReqHistoryVo.setRegularReqGbnCd(OrderEnums.RegularReqGbnCd.GS.getCode());
			orderRegularReqHistoryVo.setRegularReqCont("결제 실패 연속 2회 발생. " + regularResultPaymentItem.getReqRound() + "회차 건너뛰기");
			orderRegularDetailBiz.putRegularOrderReqHistory(orderRegularReqHistoryVo);

			// 12. 입금전 취소 대상 상품PK 목록 조회
			List<Long> ilGoodsIds = orderRegularPaymentService.getOdRegularOrderDetlGoodsList(odRegularResultId);
			// 13. 입금전 취소 처리
			orderRegularResultVo.setIbFlag(true);
			orderRegularResultVo.setIbIlGoodsIds(ilGoodsIds);
			orderPaymentMasterVo.setStatus(OrderEnums.OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode());
		}

		// 15. 정기배송 자동메일 & SMS 발송
		try {
			OrderInfoForEmailResultDto orderInfoForEmailResultDto = null;
			if(failCntSum == 4) {
				orderInfoForEmailResultDto = orderEmailBiz.getOrderRegularInfoForEmailForPaymentFail(regularResultPaymentItem.getOdRegularResultId());
			}
			else {
				orderInfoForEmailResultDto = orderEmailBiz.getOrderRegularInfoForEmail(regularResultPaymentItem.getOdRegularResultId());
			}

			// 16. 정기배송 결제 실패
			if(failCnt > 0) {

				if(failCntSum == 4) {
					//16.1 정기배송 해지(정기배송 결제실패4차) -> SMS만 발송
					orderEmailSendBiz.orderRegularPaymentFailFourth(orderInfoForEmailResultDto);
				}
				else if(orderRegularResultVo.getPaymentFailCnt() == 1) {
					//16.2 결제실패1차 -> SMS만 발송
					orderEmailSendBiz.orderRegularPaymentFailFirst(orderInfoForEmailResultDto);
				}else if(orderRegularResultVo.getPaymentFailCnt() == 2){
					//16.3 결제실패2차 -> EMAIL,SMS 둘다 발송
					orderEmailSendBiz.orderRegularPaymentFailSecond(orderInfoForEmailResultDto);
				}

				// 17. 정기배송 결제 완료
			}else {
				orderEmailSendBiz.orderRegularPaymentComplete(orderInfoForEmailResultDto);
			}

			// 18. 정기배송 만료 예정 SMS
			// 정기배송 마지막 이전 회차 결제완료 직후
			if(regularResultPaymentItem.getTotCnt() == regularResultPaymentItem.getReqRound()+1) {
				orderEmailSendBiz.orderRegularExpireExpected(orderInfoForEmailResultDto);
			}

			// 입금전 취소처리 해야할 경우
			if (orderRegularResultVo.isIbFlag()) {
				try {
					// 입금전 취소 처리
					OrderClaimRegisterRequestDto orderClaimRegisterRequestDto = orderRegularOrderCreateService.putOdRegularIncomBeforeCancelComplete(orderRegularResultVo.getIbIlGoodsIds(), regularResultPaymentItem.getOdid(), OrderEnums.RegularOrderBatchTypeCd.REGULAR_PAYMENT.getCode());
					claimProcessBiz.addOrderClaimNonTransaction(orderClaimRegisterRequestDto);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		catch (Exception e) {
			log.error("ERROR ====== 정기배송 결제 자동메일 발송 오류 RegularResultPaymentListDto ::{}" , regularResultPaymentItem);
			log.error(e.getMessage());
		}

		orderRegularResultVo.setOrderPaymentMasterVo(orderPaymentMasterVo);

		return orderRegularResultVo;
	}
}
