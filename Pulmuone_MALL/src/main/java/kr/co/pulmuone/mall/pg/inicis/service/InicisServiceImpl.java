package kr.co.pulmuone.mall.pg.inicis.service;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderRegistrationResult;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PaymentType;
import kr.co.pulmuone.v1.comm.enums.PgEnums;
import kr.co.pulmuone.v1.comm.enums.PgEnums.InicisCode;
import kr.co.pulmuone.v1.comm.enums.PgEnums.InicisMobileIllegalCode;
import kr.co.pulmuone.v1.comm.enums.PgEnums.InicisPcIllegalCode;
import kr.co.pulmuone.v1.comm.enums.PgEnums.PgAccountType;
import kr.co.pulmuone.v1.comm.enums.PgEnums.PgErrorType;
import kr.co.pulmuone.v1.comm.enums.PgEnums.PgServiceType;
import kr.co.pulmuone.v1.comm.util.CookieUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.order.claim.dto.MallOrderClaimAddPaymentResult;
import kr.co.pulmuone.v1.order.claim.dto.OrderAccountDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimAddPaymentInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimPriceInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimRegisterResponseDto;
import kr.co.pulmuone.v1.order.claim.dto.vo.OdAddPaymentReqInfo;
import kr.co.pulmuone.v1.order.claim.service.ClaimProcessBiz;
import kr.co.pulmuone.v1.order.claim.service.ClaimRequestBiz;
import kr.co.pulmuone.v1.order.claim.service.ClaimRequestProcessBiz;
import kr.co.pulmuone.v1.order.claim.service.ClaimUtilProcessService;
import kr.co.pulmuone.v1.order.claim.service.ClaimUtilRefundService;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderDataDto;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderPaymentDataDto;
import kr.co.pulmuone.v1.order.order.dto.PutOrderPaymentCompleteResDto;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.pg.dto.CancelRequestDto;
import kr.co.pulmuone.v1.pg.dto.CancelResponseDto;
import kr.co.pulmuone.v1.pg.dto.EtcDataCartDto;
import kr.co.pulmuone.v1.pg.service.PgBiz;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisApprovalRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisApprovalResponseDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisMobileApprovalRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisMobileApprovalResponseDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNotiRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisVirtualAccountReturnRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.service.InicisConfig;
import kr.co.pulmuone.v1.pg.service.inicis.service.InicisPgService;
import kr.co.pulmuone.v1.shopping.cart.dto.DelCartRequestDto;
import kr.co.pulmuone.v1.shopping.cart.dto.EtcDataClaimDto;
import kr.co.pulmuone.v1.shopping.cart.service.ShoppingCartBiz;
import kr.co.pulmuone.v1.system.log.service.SystemLogBiz;
import lombok.extern.slf4j.Slf4j;

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
 *  1.0    20201006   	 홍진영            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class InicisServiceImpl implements InicisService {

	@Autowired
	PgBiz pgBiz;

	@Autowired
	InicisConfig config;

	@Autowired
	InicisPgService inicisPgService;

	@Autowired
	ShoppingCartBiz shoppingCartBiz;

	@Autowired
	OrderOrderBiz orderOrderBiz;

	@Autowired
	ClaimProcessBiz claimProcessBiz;

	@Autowired
	ClaimRequestBiz claimRequestBiz;

	@Autowired
	ClaimRequestProcessBiz claimRequestProcessBiz;

	@Autowired
	ClaimUtilProcessService claimUtilProcessService;

	@Autowired
	ClaimUtilRefundService claimUtilRefundService;

	@Autowired
	SystemLogBiz systemLogBiz;

	@Autowired
    private HttpServletRequest request;

	/**
	 * 이니시스 레이아웃 닫기
	 */
	@Override
	public void close(HttpServletResponse httpServletResponse) throws Exception {
		httpServletResponse.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = httpServletResponse.getWriter();

		writer.println("<script language=\"javascript\" type=\"text/javascript\" src=\"" + config.getCloseScriptUrl()
				+ "\" charset=\"UTF-8\"></script>");
		writer.flush();
		writer.close();
	}

	/**
	 * 이니시스 승인 요청
	 */
	@Override
	public String result(InicisApprovalRequestDto reqDto) throws Exception {

		// PG 승인 요청 결과 확인
		if (!InicisCode.SUCCESS_PC.getCode().equals(reqDto.getResultCode())) {
			return pgBiz.orderFail(reqDto.getResultCode(), reqDto.getResultMsg());
		}

		// 주문번호로 주문데이터 조회
		PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(reqDto.getOrderNumber());

		// 유호성 검증
		PgEnums.PgErrorType validation = orderOrderBiz.isPgApprovalOrderValidation(orderData);
		if (!PgEnums.PgErrorType.SUCCESS.getCode().equals(validation.getCode())) {
			return pgBiz.orderFail(validation.getCode(), validation.getCodeName());
		}

		// 금액 검증을 해 paymentPrice 세팅
		reqDto.setPaymentPrice(orderData.getPaymentPrice());

		// PG 승인 처리
		InicisApprovalResponseDto resDto = inicisPgService.approval(reqDto);

		try {
			// PG 승인 결과 확인
			if (!InicisCode.SUCCESS_PC.getCode().equals(resDto.getResultCode())) {
				log.error("ERROR ====== 이니시스 주문 승인 result orderFail - resDto ::{}", resDto);
				// 부정거래 방지 체크
				checkIllegal(false, resDto.getResultCode());
				return pgBiz.orderFail(resDto.getResultCode(), resDto.getResultMsg());
			}

			// 주문서 쪽에서 넘긴 데이더 생성
			EtcDataCartDto etcDataCartDto = inicisPgService.toDtoEtcData(resDto.getEtcData(), EtcDataCartDto.class);

			// 주문 정보에 결제 타입 변경
			orderOrderBiz.convertPgApprovalOrderDataDto(orderData, etcDataCartDto);

			// 주문 데이터 처리
			PutOrderPaymentCompleteResDto putResDto = orderOrderBiz.putOrderPaymentComplete(orderData, resDto);
			if (PgEnums.PgErrorType.SUCCESS.getCode().equals(putResDto.getResult().getCode())) {
				if (etcDataCartDto.getSpCartId() != null && !etcDataCartDto.getSpCartId().isEmpty()) {
					// 장바구니 삭제
					DelCartRequestDto delCartRequestDto = new DelCartRequestDto();
					delCartRequestDto.setSpCartId(etcDataCartDto.getSpCartId());
					shoppingCartBiz.delCartAndAddGoods(delCartRequestDto);
				}
				return pgBiz.orderSuccess(resDto.getMoid(), etcDataCartDto.getCartType(), orderData.getOrderPaymentType(), orderData.getPresentYn());
			} else {
				// PG 망취소
				inicisPgService.netCancel(reqDto.getNetCancelUrl(), resDto.getAuthMap());

				// 실패처리
				return pgBiz.orderFail(putResDto.getResult().getCode(), putResDto.getResult().getCodeName());
			}
		} catch (Exception e) {
			log.error("ERROR ====== 이니시스 주문 승인 result");
			log.error("ERROR ====== 이니시스 주문 승인 reqDto ::{}", reqDto);
			log.error("ERROR ====== 이니시스 주문 승인 orderData ::{}", orderData);
			log.error("ERROR ====== 이니시스 주문 승인 approvalResDto ::{}", resDto);
			log.error("ERROR ====== 이니시스 주문 승인 Exception", e);

			// PG 망취소
			inicisPgService.netCancel(reqDto.getNetCancelUrl(), resDto.getAuthMap());

			// 실패처리
			return pgBiz.orderFail(PgEnums.PgErrorType.FAIL_UPDATE_ERROR.getCode(), PgEnums.PgErrorType.FAIL_UPDATE_ERROR.getCodeName());
		}
	}

	@Override
	public String next(InicisMobileApprovalRequestDto reqDto) throws Exception {

		// 모바일에서는 주문번호를 안주기 때문에 주문서 쪽에서 넘긴 데이더로 처리
		EtcDataCartDto etcDataCartDto = inicisPgService.toDtoEtcData(reqDto.getP_NOTI(), EtcDataCartDto.class);

		// PG 승인 요청 결과 확인
		if (!InicisCode.SUCCESS_MOBILE.getCode().equals(reqDto.getP_STATUS())) {
			// 고객 취소 요청시 (01 : 사용자가 결제를 취소 하였습니다.)
			if(InicisCode.USER_CANCEL.getCode().equals(reqDto.getP_STATUS())) {
				return pgBiz.orderFail(etcDataCartDto.getOrderInputUrl());
			} else {
				return pgBiz.orderFail(reqDto.getP_STATUS(), reqDto.getP_RMESG1());
			}
		}

		// 주문번호로 주문데이터 조회
		PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(etcDataCartDto.getOdid());

		// 유호성 검증
		PgEnums.PgErrorType validation = orderOrderBiz.isPgApprovalOrderValidation(orderData);
		if (!PgEnums.PgErrorType.SUCCESS.getCode().equals(validation.getCode())) {
			return pgBiz.orderFail(validation.getCode(), validation.getCodeName());
		}

		// 금액 검증
		if (!reqDto.getP_AMT().equals(String.valueOf(orderData.getPaymentPrice()))) {
			return pgBiz.orderFail(PgErrorType.FAIL_NOT_PAYMENT_PRICE.getCode(), PgErrorType.FAIL_NOT_PAYMENT_PRICE.getCodeName());
		}

		// PG 승인 처리
		InicisMobileApprovalResponseDto resDto = inicisPgService.mobileApproval(reqDto);

		try {
			// PG 승인 결과 확인
			if (!InicisCode.SUCCESS_MOBILE.getCode().equals(resDto.getP_STATUS())) {
				log.error("ERROR ====== 이니시스 주문 승인 next orderFail - resDto ::{}", resDto);
				// 부정거래 방지 체크
				checkIllegal(true, resDto.getP_STATUS());
				return pgBiz.orderFail(resDto.getP_STATUS(), resDto.getP_RMESG1());
			}

			// 주문 정보에 결제 타입 변경
			orderOrderBiz.convertPgApprovalOrderDataDto(orderData, etcDataCartDto);

			// 주문 데이터 처리
			PutOrderPaymentCompleteResDto putResDto = orderOrderBiz.putOrderPaymentComplete(orderData, resDto);
			if (PgEnums.PgErrorType.SUCCESS.getCode().equals(putResDto.getResult().getCode())) {
				if (etcDataCartDto.getSpCartId() != null && !etcDataCartDto.getSpCartId().isEmpty()) {
					// 장바구니 삭제
					DelCartRequestDto delCartRequestDto = new DelCartRequestDto();
					delCartRequestDto.setSpCartId(etcDataCartDto.getSpCartId());
					shoppingCartBiz.delCartAndAddGoods(delCartRequestDto);
				}
				return pgBiz.orderSuccess(resDto.getP_OID(), etcDataCartDto.getCartType(), orderData.getOrderPaymentType(), orderData.getPresentYn());
			} else {
				// PG 취소
				inicisPgService.mobileNetCancel(reqDto.getP_REQ_URL(), resDto.getNetCancelAuthMap());

				// 실패처리
				return pgBiz.orderFail(putResDto.getResult().getCode(), putResDto.getResult().getCodeName());
			}
		} catch (Exception e) {
			log.error("ERROR ====== 이니시스 주문 승인 next");
			log.error("ERROR ====== 이니시스 주문 승인 reqDto ::{}", reqDto);
			log.error("ERROR ====== 이니시스 주문 승인 orderData ::{}", orderData);
			log.error("ERROR ====== 이니시스 주문 승인 approvalResDto ::{}", resDto);
			log.error("ERROR ====== 이니시스 주문 승인 Exception", e);

			// PG 취소
			inicisPgService.mobileNetCancel(reqDto.getP_REQ_URL(), resDto.getNetCancelAuthMap());

			// 실패처리
			return pgBiz.orderFail(PgEnums.PgErrorType.FAIL_UPDATE_ERROR.getCode(), PgEnums.PgErrorType.FAIL_UPDATE_ERROR.getCodeName());
		}
	}

	@Override
	public void virtualAccountReturn(HttpServletResponse httpServletResponse,
			InicisVirtualAccountReturnRequestDto reqDto) throws Exception {
		// 가상계좌 PC 계좌이체 통보
		String resultCode = InicisCode.DEPOSIT_NOTI_FAIL.getCode();
		if (StringUtil.isNotEmpty(reqDto.getNo_tid()) && StringUtil.isNotEmpty(reqDto.getNo_oid())) {
			// 관리자에서 주문 생성시 주문번호 여러게에 결제를 한번 할수 있는 케이스가 있어 PG 주문번호에 odPaymentMasterId 를 보냄
			if (orderOrderBiz.isOdidValuePaymentMasterId(reqDto.getNo_oid())) {
				String odPaymentMasterId = orderOrderBiz.getPaymentMasterIdByOdid(reqDto.getNo_oid());
				List<PgApprovalOrderDataDto> orderDataList = orderOrderBiz.getPgApprovalOrderDataByOdPaymentMasterId(odPaymentMasterId);
				if (reqDto.getAmt_input().equals(String.valueOf(orderDataList.get(0).getPaymentPrice()))) {
					for (PgApprovalOrderDataDto orderData : orderDataList) {
						if (OrderEnums.OrderStatus.INCOM_READY.getCode().equals(orderData.getStatus())) {
							// 주문 입금 확인 처리
							orderOrderBiz.putVirtualBankOrderPaymentComplete(orderData, reqDto);
							resultCode = InicisCode.DEPOSIT_NOTI_SUCCESS.getCode();
						} else if (OrderEnums.OrderStatus.INCOM_COMPLETE.getCode().equals(orderData.getStatus())) {
							// 서버 이슈 및 결제 처리 이후 다시 API 시도시 성공으로 리턴
							resultCode = InicisCode.DEPOSIT_NOTI_SUCCESS.getCode();
						}
					}
				}
			} else {
				// 추가 결제 주문일 경우
				if(orderOrderBiz.isOdidValueAddPaymentMasterId(reqDto.getNo_oid())) {
					// 클레임 추가배송비 여부
					OrderClaimAddPaymentInfoDto claimAddPaymentInfo = claimProcessBiz.getAddPaymentClaimInfo(reqDto);
					// 클레임 추가배송비가 존재할 경우
					if(ObjectUtils.isNotEmpty(claimAddPaymentInfo)) {
						// 금액 비교 및 입금 예정 체크
						if (reqDto.getAmt_input().equals(String.valueOf(claimAddPaymentInfo.getPaymentPrice()))) {
							if (OrderEnums.OrderStatus.INCOM_READY.getCode().equals(claimAddPaymentInfo.getStatus())) {
								// 주문 입금 확인 처리
								claimProcessBiz.putAddPaymentClaimInfo(claimAddPaymentInfo);
								resultCode = InicisCode.DEPOSIT_NOTI_SUCCESS.getCode();
							} else if (OrderEnums.OrderStatus.INCOM_COMPLETE.getCode().equals(claimAddPaymentInfo.getStatus())) {
								// 서버 이슈 및 결제 처리 이후 다시 API 시도시 성공으로 리턴
								resultCode = InicisCode.DEPOSIT_NOTI_SUCCESS.getCode();
							}
						}
					}
				}
				else {
					// 주문번호로 주문데이터 조회
					PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(reqDto.getNo_oid());

					// 금액 비교 및 입금 예정 체크
					if (reqDto.getAmt_input().equals(String.valueOf(orderData.getPaymentPrice()))) {
						if (OrderEnums.OrderStatus.INCOM_READY.getCode().equals(orderData.getStatus())) {
							// 주문 입금 확인 처리
							orderOrderBiz.putVirtualBankOrderPaymentComplete(orderData, reqDto);
							resultCode = InicisCode.DEPOSIT_NOTI_SUCCESS.getCode();
						} else if (OrderEnums.OrderStatus.INCOM_COMPLETE.getCode().equals(orderData.getStatus())) {
							// 서버 이슈 및 결제 처리 이후 다시 API 시도시 성공으로 리턴
							resultCode = InicisCode.DEPOSIT_NOTI_SUCCESS.getCode();
						} else if(OrderEnums.OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode().equals(orderData.getStatus())) {
							// 입금전 취소일 경우 환불 프로세스 처리
							OrderAccountDto orderAccountDto = claimUtilRefundService.getRefundBank(orderData.getOdOrderId());

							if(ObjectUtils.isNotEmpty(orderAccountDto)){
								// PG 취소
								CancelRequestDto cancelReqDto = new CancelRequestDto();
								cancelReqDto.setPartial(false);
								cancelReqDto.setTid(reqDto.getNo_tid());
								cancelReqDto.setPaymentType(PaymentType.VIRTUAL_BANK);
								cancelReqDto.setCancelMessage("입금전취소상태로 인한 환불");
								cancelReqDto.setOdid(orderData.getOdid());
								cancelReqDto.setRefundBankNumber(orderAccountDto.getAccountNumber());
								cancelReqDto.setRefundBankCode(pgBiz.getPgBankCode(PgServiceType.INICIS.getCode(), PaymentType.VIRTUAL_BANK.getCode(), orderAccountDto.getBankCode()));
								cancelReqDto.setRefundBankName(orderAccountDto.getHolderName());

								CancelResponseDto cancelResponseDto = inicisPgService.cancel(PgAccountType.INICIS_BASIC.getCode(), cancelReqDto);
								if(cancelResponseDto.isSuccess()) {
									resultCode = InicisCode.DEPOSIT_NOTI_SUCCESS.getCode();
								} else {
									log.error("========== 가상계좌 이니시스 입금통보 입금전취소상태로 인한 환불실패 - cancelReqDto ::{}", cancelReqDto);
									log.error("========== 가상계좌 이니시스 입금통보 입금전취소상태로 인한 환불실패 - cancelResponseDto ::{}", cancelResponseDto);
								}
							} else {
								log.error("========== 가상계좌 이니시스 입금통보 입금전취소상태로 인한 환불실패 - 환불계좌 조회안됨 ::{}", orderData);
							}
						}
					}
				}
			}
		}

		try {
			httpServletResponse.setContentType("text/html;charset=UTF-8");

			PrintWriter writer = httpServletResponse.getWriter();
			writer.println(resultCode);
			writer.flush();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void noti(HttpServletResponse httpServletResponse, InicisNotiRequestDto reqDto) throws Exception {
		// 가상계좌 모바일 계좌이체 통보
		String resultCode = InicisCode.DEPOSIT_NOTI_FAIL.getCode();
		if (InicisCode.VIRTUAL_BANK.getCode().equals(reqDto.getP_TYPE())) {
			if (InicisCode.DEPOSIT_NOTI.getCode().equals(reqDto.getP_STATUS())) {
				// 관리자에서 주문 생성시 주문번호 여러게에 결제를 한번 할수 있는 케이스가 있어 PG 주문번호에 odPaymentMasterId 를 보냄
				if (orderOrderBiz.isOdidValuePaymentMasterId(reqDto.getP_OID())) {
					String odPaymentMasterId = orderOrderBiz.getPaymentMasterIdByOdid(reqDto.getP_OID());
					List<PgApprovalOrderDataDto> orderDataList = orderOrderBiz.getPgApprovalOrderDataByOdPaymentMasterId(odPaymentMasterId);
					if (reqDto.getP_AMT().equals(String.valueOf(orderDataList.get(0).getPaymentPrice()))) {
						for (PgApprovalOrderDataDto orderData : orderDataList) {
							if (OrderEnums.OrderStatus.INCOM_READY.getCode().equals(orderData.getStatus())) {
								// 주문 입금 확인 처리
								orderOrderBiz.putVirtualBankOrderPaymentComplete(orderData, reqDto);
								resultCode = InicisCode.DEPOSIT_NOTI_SUCCESS.getCode();
							} else if (OrderEnums.OrderStatus.INCOM_COMPLETE.getCode().equals(orderData.getStatus())) {
								// 서버 이슈 및 결제 처리 이후 다시 API 시도시 성공으로 리턴
								resultCode = InicisCode.DEPOSIT_NOTI_SUCCESS.getCode();
							}
						}
					}
				} else {
					// 추가 결제 주문일 경우
					if(orderOrderBiz.isOdidValueAddPaymentMasterId(reqDto.getP_OID())) {
						// 클레임 추가배송비 여부
						OrderClaimAddPaymentInfoDto claimAddPaymentInfo = claimProcessBiz.getAddPaymentClaimInfo(reqDto);
						// 클레임 추가배송비가 존재할 경우
						if(ObjectUtils.isNotEmpty(claimAddPaymentInfo)) {
							// 금액 비교 및 입금 예정 체크
							if (reqDto.getP_AMT().equals(String.valueOf(claimAddPaymentInfo.getPaymentPrice()))) {
								if (OrderEnums.OrderStatus.INCOM_READY.getCode().equals(claimAddPaymentInfo.getStatus())) {
									// 주문 입금 확인 처리
									claimProcessBiz.putAddPaymentClaimInfo(claimAddPaymentInfo);
									resultCode = InicisCode.DEPOSIT_NOTI_SUCCESS.getCode();
								} else if (OrderEnums.OrderStatus.INCOM_COMPLETE.getCode().equals(claimAddPaymentInfo.getStatus())) {
									// 서버 이슈 및 결제 처리 이후 다시 API 시도시 성공으로 리턴
									resultCode = InicisCode.DEPOSIT_NOTI_SUCCESS.getCode();
								}
							}
						}
					}
					else {
						// 주문번호로 주문데이터 조회
						PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(reqDto.getP_OID());

						// 금액 비교 및 입금 예정 체크
						if (reqDto.getP_AMT().equals(String.valueOf(orderData.getPaymentPrice()))) {
							if (OrderEnums.OrderStatus.INCOM_READY.getCode().equals(orderData.getStatus())) {
								// 주문 입금 확인 처리
								orderOrderBiz.putVirtualBankOrderPaymentComplete(orderData, reqDto);
								resultCode = InicisCode.DEPOSIT_NOTI_SUCCESS.getCode();
							} else if (OrderEnums.OrderStatus.INCOM_COMPLETE.getCode().equals(orderData.getStatus())) {
								// 서버 이슈 및 결제 처리 이후 다시 API 시도시 성공으로 리턴
								resultCode = InicisCode.DEPOSIT_NOTI_SUCCESS.getCode();
							} else if(OrderEnums.OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode().equals(orderData.getStatus())) {
								// 입금전 취소일 경우 환불 프로세스 처리
								OrderAccountDto orderAccountDto = claimUtilRefundService.getRefundBank(orderData.getOdOrderId());

								if(ObjectUtils.isNotEmpty(orderAccountDto)){
									// PG 취소
									CancelRequestDto cancelReqDto = new CancelRequestDto();
									cancelReqDto.setPartial(false);
									cancelReqDto.setTid(reqDto.getP_TID());
									cancelReqDto.setPaymentType(PaymentType.VIRTUAL_BANK);
									cancelReqDto.setCancelMessage("입금전취소상태로 인한 환불");
									cancelReqDto.setOdid(orderData.getOdid());
									cancelReqDto.setRefundBankNumber(orderAccountDto.getAccountNumber());
									cancelReqDto.setRefundBankCode(pgBiz.getPgBankCode(PgServiceType.INICIS.getCode(), PaymentType.VIRTUAL_BANK.getCode(), orderAccountDto.getBankCode()));
									cancelReqDto.setRefundBankName(orderAccountDto.getHolderName());

									CancelResponseDto cancelResponseDto = inicisPgService.cancel(PgAccountType.INICIS_BASIC.getCode(), cancelReqDto);
									if(cancelResponseDto.isSuccess()) {
										resultCode = InicisCode.DEPOSIT_NOTI_SUCCESS.getCode();
									} else {
										log.error("========== 가상계좌 이니시스 입금통보 입금전취소상태로 인한 환불실패 - cancelReqDto ::{}", cancelReqDto);
										log.error("========== 가상계좌 이니시스 입금통보 입금전취소상태로 인한 환불실패 - cancelResponseDto ::{}", cancelResponseDto);
									}
								} else {
									log.error("========== 가상계좌 이니시스 입금통보 입금전취소상태로 인한 환불실패 - 환불계좌 조회안됨 ::{}", orderData);
								}
							}
						}
					}
				}
			} else {
				// 입금통보 "02" 가 아니면(가상계좌 채번 : 00 또는 01 경우)
				resultCode = InicisCode.DEPOSIT_NOTI_SUCCESS.getCode();
			}
		}

		try {
			httpServletResponse.setContentType("text/html;charset=UTF-8");

			PrintWriter writer = httpServletResponse.getWriter();
			writer.println(resultCode);
			writer.flush();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 추가결제 이니시스 승인 요청
	 */
	@Override
	public String addPayResult(InicisApprovalRequestDto reqDto) throws Exception {
		log.debug("이니시스 승인요청 변수 reqDto :: <{}>", reqDto);
		log.debug("이니시스 결과   resultCode :: <{}>", reqDto.getResultCode());

		// PG 승인 요청 결과 확인
		if (!InicisCode.SUCCESS_PC.getCode().equals(reqDto.getResultCode())) {
			return pgBiz.claimFail(reqDto.getResultCode(), reqDto.getResultMsg());
		}

		EtcDataClaimDto etcDataClaimDto = inicisPgService.toDtoEtcData(reqDto.getMerchantData(), EtcDataClaimDto.class);

		// 클레임 ID가 존재할 경우 추가배송비 직접결제
		if(etcDataClaimDto.getOdClaimId() > 0){

			// 1. 클래임PK 로 클래임 정보 조회
			OrderClaimRegisterRequestDto orderClaimRegisterRequestDto = new OrderClaimRegisterRequestDto();
			orderClaimRegisterRequestDto.setOdClaimId(etcDataClaimDto.getOdClaimId());
			MallOrderClaimAddPaymentResult mallOrderClaimAddPaymentResult = claimRequestBiz.getOrderClaimAddShippingPriceInfo(orderClaimRegisterRequestDto);

			// 금액 검증을 위해 paymentPrice 세팅
			reqDto.setPaymentPrice(mallOrderClaimAddPaymentResult.getAddPaymentShippingPrice());

			// 2. PG 승인 처리
			InicisApprovalResponseDto resDto = inicisPgService.approval(reqDto);

			log.debug("클레임 이니시스 승인처리 결과 :: <{}>", resDto);
			log.debug("클레임 이니시스 결과코드 res_cd :: <{}>", resDto.getResultCode());

			// 3. PG 승인 결과 확인
			if (!InicisCode.SUCCESS_PC.getCode().equals(resDto.getResultCode())) {
				// 부정거래 방지 체크
				checkIllegal(false, resDto.getResultCode());
				return pgBiz.claimFail(resDto.getResultCode(), resDto.getResultMsg());
			}

			// 4. 추가결제요청정보 조회
			OdAddPaymentReqInfo addPaymentReqInfo = claimProcessBiz.getOdAddPaymentReqInfo(etcDataClaimDto.getOdAddPaymentReqInfoId());
			// 5. 추가결제요청정보 데이터 JSON -> VO 변환
			OrderClaimRegisterRequestDto requestDto = inicisPgService.toDtoJsonString(addPaymentReqInfo.getReqJsonInfo(), OrderClaimRegisterRequestDto.class);

			// 6. PG 정보 PgApprovalOrderPaymentDataDto 로 변경
			PgApprovalOrderPaymentDataDto pgApprovalOrderPaymentDataDto = orderOrderBiz.convertPgApprovalOrderPaymentData(requestDto.getAddPaymentInfo().getPsPayCd(), resDto);

			// 7. 클레임 상태 업데이트
			mallOrderClaimAddPaymentResult.setUrUserId(Long.parseLong(requestDto.getUrUserId()));

			// 비회원CI 정보가 존재할 경우
			if(StringUtil.isNotEmpty(requestDto.getGuestCi())) {
				mallOrderClaimAddPaymentResult.setUrUserId(Constants.GUEST_CREATE_USER_ID);
			}
			OrderClaimRegisterResponseDto claimRegResDto = claimProcessBiz.putOrderClaimInfo(mallOrderClaimAddPaymentResult);

			// 8. 클레임 업데이트가 성공일 경우
			if (OrderEnums.OrderRegistrationResult.SUCCESS.getCode().equals(claimRegResDto.getOrderRegistrationResult().getCode())) {

				// 취소완료나 반품 완료일 경우 환불 처리 및 환불 정보 생성
				if(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(mallOrderClaimAddPaymentResult.getClaimStatusCd()) ||
					OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(mallOrderClaimAddPaymentResult.getClaimStatusCd())) {
					claimProcessBiz.setClaimRefundPaymentInfo(mallOrderClaimAddPaymentResult.getOdOrderId(), mallOrderClaimAddPaymentResult.getOdClaimId(), mallOrderClaimAddPaymentResult.getClaimStatusCd());
				}

				OrderClaimPriceInfoDto claimPriceInfoDto = new OrderClaimPriceInfoDto();
				claimPriceInfoDto.setAddPaymentShippingPrice(mallOrderClaimAddPaymentResult.getAddPaymentShippingPrice());
				/// 클래임 결제 정보 등록 처리
				claimUtilProcessService.addClaimPaymentInfo(requestDto, claimPriceInfoDto, pgApprovalOrderPaymentDataDto);

				// 성공 처리
				return pgBiz.claimSuccess(etcDataClaimDto.getOdClaimId());
			}
			// 8-1. 클레임 업데이트 실패 시 취소 처리
			else {
				// PG 취소
				CancelRequestDto cancelReqDto = new CancelRequestDto();
				cancelReqDto.setPartial(false);
				cancelReqDto.setPaymentType(PaymentType.findByCode(requestDto.getAddPaymentInfo().getPsPayCd()));
				cancelReqDto.setTid(resDto.getTid());
				cancelReqDto.setCancelMessage("가맹점 결과 처리 오류 - " + claimRegResDto.getOrderRegistrationResult().getCodeName());
				cancelReqDto.setOdid(resDto.getMoid());

				inicisPgService.cancel(pgApprovalOrderPaymentDataDto.getPgAccountType().getCode(), cancelReqDto);

				// 실패처리
				return pgBiz.claimFail(claimRegResDto.getOrderRegistrationResult().getCode(), claimRegResDto.getOrderRegistrationResult().getCodeName());
			}

		} else {
//			log.debug("이니시스 결제 받은 주문번호 ordr_idxx ::: <{}>", reqDto.getOrderNumber());
//			String[] ordrArray = reqDto.getOrderNumber().split("-");
//			long odAddPaymentReqInfoId = 0;
//			log.debug("이니시스 결제 받은 주문번호 ordrArray 배열 존재 유무::: <{}>", ArrayUtils.isEmpty(ordrArray));

//			if (!ArrayUtils.isEmpty(ordrArray)) {
//				odAddPaymentReqInfoId = Long.parseLong(ordrArray[1]);
//				log.debug("이니시스 결제 받은 주문번호 ordrArray 배열 값::: <{}>, <{}>, <{}>, <{}>", ordrArray, ordrArray[0], ordrArray[1], odAddPaymentReqInfoId);
//			}
			long odAddPaymentReqInfoId = etcDataClaimDto.getOdAddPaymentReqInfoId();

			// 주문클레임PK로 결제 마스터에서 추가 결제 금액을 가져온다.
			OdAddPaymentReqInfo addPaymentReqInfo = claimProcessBiz.getOdAddPaymentReqInfo(odAddPaymentReqInfoId);
			OrderClaimRegisterRequestDto requestDto = inicisPgService.toDtoJsonString(addPaymentReqInfo.getReqJsonInfo(), OrderClaimRegisterRequestDto.class);
			OrderClaimPriceInfoDto claimPriceInfoDto = claimRequestProcessBiz.getRefundPriceInfo(requestDto);

			log.debug("이니시스 추가결제 정보 :: <{}>, <{}>", claimPriceInfoDto.getAddPaymentShippingPrice(), addPaymentReqInfo);
			// 금액 검증을 해 paymentPrice 세팅
			reqDto.setPaymentPrice(claimPriceInfoDto.getAddPaymentShippingPrice());

			// PG 승인 처리
			InicisApprovalResponseDto resDto = inicisPgService.approval(reqDto);

			log.debug("이니시스 승인처리 결과 :: <{}>", resDto);
			log.debug("이니시스 결과코드 res_cd :: <{}>", resDto.getResultCode());

			// PG 승인 결과 확인
			if (!InicisCode.SUCCESS_PC.getCode().equals(resDto.getResultCode())) {
				// 부정거래 방지 체크
				checkIllegal(false, resDto.getResultCode());
				return pgBiz.claimFail(resDto.getResultCode(), resDto.getResultMsg());
			}

			// 클레임 정보 등록
			ApiResult<OrderClaimRegisterResponseDto> addResDto = null;
			try {
				addResDto = (ApiResult<OrderClaimRegisterResponseDto>) claimProcessBiz.addOrderClaim(requestDto);
			} catch (Exception e) {
				OrderClaimRegisterResponseDto orderClaimRegisterResponseDto = OrderClaimRegisterResponseDto.builder()
						.orderRegistrationResult(OrderRegistrationResult.FAIL)
						.message(e.getMessage())
						.build();
				addResDto = ApiResult.success(orderClaimRegisterResponseDto);
			}

			OrderClaimRegisterResponseDto claimRegResDto = addResDto.getData();
			PgApprovalOrderPaymentDataDto pgApprovalOrderPaymentDataDto = orderOrderBiz.convertPgApprovalOrderPaymentData(requestDto.getAddPaymentInfo().getPsPayCd(), resDto);

			log.debug("이니시스 claimRegResDto :: <{}>", claimRegResDto);
			log.debug("이니시스 pgApprovalOrderPaymentDataDto :: <{}>", pgApprovalOrderPaymentDataDto);

			if (OrderEnums.OrderRegistrationResult.SUCCESS.getCode().equals(claimRegResDto.getOrderRegistrationResult().getCode())) {
				// 클래임 결제 정보 등록 처리
				claimUtilProcessService.addClaimPaymentInfo(requestDto, claimPriceInfoDto, pgApprovalOrderPaymentDataDto);

				// 성공 처리
				return pgBiz.claimSuccess(claimRegResDto.getOdClaimId());
			} else {
				// PG 취소
				CancelRequestDto cancelReqDto = new CancelRequestDto();
				cancelReqDto.setPartial(false);
				cancelReqDto.setPaymentType(PaymentType.findByCode(requestDto.getAddPaymentInfo().getPsPayCd()));
				cancelReqDto.setTid(resDto.getTid());
				cancelReqDto.setCancelMessage("가맹점 결과 처리 오류 - " + claimRegResDto.getMessage());
				cancelReqDto.setOdid(resDto.getMoid());

				inicisPgService.cancel(pgApprovalOrderPaymentDataDto.getPgAccountType().getCode(), cancelReqDto);

				// 실패처리
				return pgBiz.claimFail(claimRegResDto.getOrderRegistrationResult().getCode(), claimRegResDto.getMessage());
			}
		}
	}

	/**
	 * 추가결제 이니시스 모바일 인증결과수신
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public String addPayNext(InicisMobileApprovalRequestDto reqDto) throws Exception {
		log.debug("이니시스 모바일 인증결과 수신 변수 reqDto :: <{}>", reqDto);
		log.debug("이니시스 모바일 인증결과 수신 변수 p_status :: <{}>", reqDto.getP_STATUS());

		// PG 승인 요청 결과 확인
		if (!InicisCode.SUCCESS_MOBILE.getCode().equals(reqDto.getP_STATUS())) {
			return pgBiz.claimFail(reqDto.getP_STATUS(), reqDto.getP_RMESG1());
		}

		// 모바일에서는 주문번호를 안주기 때문에 주문서 쪽에서 넘긴 데이더로 처리
		EtcDataClaimDto etcDataClaimDto = inicisPgService.toDtoEtcData(reqDto.getP_NOTI(), EtcDataClaimDto.class);
		log.debug("EtcDataClaimDto :: <{}>, <{}>", etcDataClaimDto.getOdAddPaymentReqInfoId(), etcDataClaimDto);

		// 클레임 ID가 존재할 경우 추가배송비 직접결제
		if(etcDataClaimDto.getOdClaimId() > 0){

			// 1. 클래임PK 로 클래임 정보 조회
			OrderClaimRegisterRequestDto orderClaimRegisterRequestDto = new OrderClaimRegisterRequestDto();
			orderClaimRegisterRequestDto.setOdClaimId(etcDataClaimDto.getOdClaimId());
			MallOrderClaimAddPaymentResult mallOrderClaimAddPaymentResult = claimRequestBiz.getOrderClaimAddShippingPriceInfo(orderClaimRegisterRequestDto);

			// 2. 금액 검증
			if (!reqDto.getP_AMT().equals(String.valueOf(mallOrderClaimAddPaymentResult.getAddPaymentShippingPrice()))) {
				return pgBiz.claimFail(PgErrorType.FAIL_NOT_PAYMENT_PRICE.getCode(), PgErrorType.FAIL_NOT_PAYMENT_PRICE.getCodeName());
			}

			// 3. PG 승인 처리
			InicisMobileApprovalResponseDto resDto = inicisPgService.mobileApproval(reqDto);

			// 4. PG 승인 결과 확인
			if (!InicisCode.SUCCESS_MOBILE.getCode().equals(resDto.getP_STATUS())) {
				// 부정거래 방지 체크
				checkIllegal(true, resDto.getP_STATUS());
				return pgBiz.claimFail(resDto.getP_STATUS(), resDto.getP_RMESG1());
			}

			// 5. 추가결제요청정보 조회
			OdAddPaymentReqInfo addPaymentReqInfo = claimProcessBiz.getOdAddPaymentReqInfo(etcDataClaimDto.getOdAddPaymentReqInfoId());
			// 6. 추가결제요청정보 데이터 JSON -> VO 변환
			OrderClaimRegisterRequestDto requestDto = inicisPgService.toDtoJsonString(addPaymentReqInfo.getReqJsonInfo(), OrderClaimRegisterRequestDto.class);

			// 7. PG 정보 PgApprovalOrderPaymentDataDto 로 변경
			PgApprovalOrderPaymentDataDto pgApprovalOrderPaymentDataDto = orderOrderBiz.convertPgApprovalOrderPaymentData(requestDto.getAddPaymentInfo().getPsPayCd(), resDto);

			// 7. 클레임 상태 업데이트 : 클레임상태코드, 직접결제여부,
			mallOrderClaimAddPaymentResult.setUrUserId(Long.parseLong(requestDto.getUrUserId()));
			// 비회원CI 정보가 존재할 경우
			if(StringUtil.isNotEmpty(requestDto.getGuestCi())) {
				mallOrderClaimAddPaymentResult.setUrUserId(Constants.GUEST_CREATE_USER_ID);
			}
			OrderClaimRegisterResponseDto claimRegResDto = claimProcessBiz.putOrderClaimInfo(mallOrderClaimAddPaymentResult);

			// 9. 클레임 업데이트가 성공일 경우
			if (OrderEnums.OrderRegistrationResult.SUCCESS.getCode().equals(claimRegResDto.getOrderRegistrationResult().getCode())) {

				// 취소완료나 반품 완료일 경우 환불 처리 및 환불 정보 생성
				if(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(mallOrderClaimAddPaymentResult.getClaimStatusCd()) ||
					OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(mallOrderClaimAddPaymentResult.getClaimStatusCd())) {
					claimProcessBiz.setClaimRefundPaymentInfo(mallOrderClaimAddPaymentResult.getOdOrderId(), mallOrderClaimAddPaymentResult.getOdClaimId(), mallOrderClaimAddPaymentResult.getClaimStatusCd());
				}

				OrderClaimPriceInfoDto claimPriceInfoDto = new OrderClaimPriceInfoDto();
				claimPriceInfoDto.setAddPaymentShippingPrice(mallOrderClaimAddPaymentResult.getAddPaymentShippingPrice());

				// 클래임 결제 정보 등록 처리
				claimUtilProcessService.addClaimPaymentInfo(requestDto, claimPriceInfoDto, pgApprovalOrderPaymentDataDto);

				// 성공 처리
				return pgBiz.claimSuccess(etcDataClaimDto.getOdClaimId());
			}
			// 9-1. 클레임 업데이트 실패 시 취소 처리
			else {
				// PG 취소
				CancelRequestDto cancelReqDto = new CancelRequestDto();
				cancelReqDto.setPartial(false);
				cancelReqDto.setPaymentType(PaymentType.findByCode(requestDto.getAddPaymentInfo().getPsPayCd()));
				cancelReqDto.setTid(resDto.getP_TID());
				cancelReqDto.setCancelMessage("가맹점 결과 처리 오류 - " + claimRegResDto.getOrderRegistrationResult().getCodeName());
				cancelReqDto.setOdid(resDto.getP_OID());

				inicisPgService.cancel(pgApprovalOrderPaymentDataDto.getPgAccountType().getCode(), cancelReqDto);

				// 실패처리
				return pgBiz.claimFail(claimRegResDto.getOrderRegistrationResult().getCode(), claimRegResDto.getOrderRegistrationResult().getCodeName());
			}

		} else {

			long odAddPaymentReqInfoId = etcDataClaimDto.getOdAddPaymentReqInfoId();

			// 주문클레임PK로 결제 마스터에서 추가 결제 금액을 가져온다.
			OdAddPaymentReqInfo addPaymentReqInfo = claimProcessBiz.getOdAddPaymentReqInfo(odAddPaymentReqInfoId);
			OrderClaimRegisterRequestDto requestDto = inicisPgService.toDtoJsonString(addPaymentReqInfo.getReqJsonInfo(), OrderClaimRegisterRequestDto.class);
			OrderClaimPriceInfoDto claimPriceInfoDto = claimRequestProcessBiz.getRefundPriceInfo(requestDto);

			log.debug("이니시스 추가결제 정보 :: <{}>, <{}>", claimPriceInfoDto.getAddPaymentShippingPrice(), addPaymentReqInfo);
			log.debug("이니시스 모바일 orderData :: req :: <{}> == 조회금액 :: <{}>", reqDto.getP_AMT(), claimPriceInfoDto.getAddPaymentShippingPrice());
			// 금액 검증
			if (!reqDto.getP_AMT().equals(String.valueOf(claimPriceInfoDto.getAddPaymentShippingPrice()))) {
				return pgBiz.claimFail(PgErrorType.FAIL_NOT_PAYMENT_PRICE.getCode(), PgErrorType.FAIL_NOT_PAYMENT_PRICE.getCodeName());
			}

			// PG 승인 처리
			InicisMobileApprovalResponseDto resDto = inicisPgService.mobileApproval(reqDto);

			// PG 승인 결과 확인
			if (!InicisCode.SUCCESS_MOBILE.getCode().equals(resDto.getP_STATUS())) {
				// 부정거래 방지 체크
				checkIllegal(true, resDto.getP_STATUS());
				return pgBiz.claimFail(resDto.getP_STATUS(), resDto.getP_RMESG1());
			}

			// 클레임 정보 등록
			ApiResult<OrderClaimRegisterResponseDto> addResDto = null;
			try {
				addResDto = (ApiResult<OrderClaimRegisterResponseDto>) claimProcessBiz.addOrderClaim(requestDto);
			} catch (Exception e) {
				OrderClaimRegisterResponseDto orderClaimRegisterResponseDto = OrderClaimRegisterResponseDto.builder()
						.orderRegistrationResult(OrderRegistrationResult.FAIL)
						.message(e.getMessage())
						.build();
				addResDto = ApiResult.success(orderClaimRegisterResponseDto);
			}
			OrderClaimRegisterResponseDto claimRegResDto = addResDto.getData();
			PgApprovalOrderPaymentDataDto pgApprovalOrderPaymentDataDto = orderOrderBiz.convertPgApprovalOrderPaymentData(requestDto.getAddPaymentInfo().getPsPayCd(), resDto);

			log.debug("이니시스 claimRegResDto :: <{}>", claimRegResDto);
			log.debug("이니시스 pgApprovalOrderPaymentDataDto :: <{}>", pgApprovalOrderPaymentDataDto);

			if (OrderEnums.OrderRegistrationResult.SUCCESS.getCode().equals(claimRegResDto.getOrderRegistrationResult().getCode())) {
				// 클래임 결제 정보 등록 처리
				claimUtilProcessService.addClaimPaymentInfo(requestDto, claimPriceInfoDto, pgApprovalOrderPaymentDataDto);

				// 성공 처리
				return pgBiz.claimSuccess(claimRegResDto.getOdClaimId());
			} else {
				// PG 취소
				CancelRequestDto cancelReqDto = new CancelRequestDto();
				cancelReqDto.setPartial(false);
				cancelReqDto.setPaymentType(PaymentType.findByCode(requestDto.getAddPaymentInfo().getPsPayCd()));
				cancelReqDto.setTid(resDto.getP_TID());
				cancelReqDto.setCancelMessage("가맹점 결과 처리 오류 - " + claimRegResDto.getMessage());
				cancelReqDto.setOdid(resDto.getP_OID());

				inicisPgService.cancel(pgApprovalOrderPaymentDataDto.getPgAccountType().getCode(), cancelReqDto);

				// 실패처리
				return pgBiz.claimFail(claimRegResDto.getOrderRegistrationResult().getCode(), claimRegResDto.getMessage());
			}
		}
	}

	/**
	 * 부정거래 체크
	 * @param code
	 */
	private void checkIllegal(boolean isMobile, String code) {
		try {
			BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
			String urPcidCd = CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY);

			Long urUserId = StringUtil.isNotEmpty(buyerVo.getUrUserId()) ? Long.valueOf(buyerVo.getUrUserId()) : 0L;

			if(isMobile) {
				// 도난 여부
				if(InicisMobileIllegalCode.isStolenLostCard(code)) {
					systemLogBiz.addIllegalLogStolenLostCard(urPcidCd, urUserId);
				}
				// 불가 여부
				if(InicisMobileIllegalCode.isTransactionNotCard(code)) {
					systemLogBiz.addIllegalLogTransactionNotCard(urPcidCd, urUserId);
				}
			} else {
				// 도난 여부
				if(InicisPcIllegalCode.isStolenLostCard(code)) {
					systemLogBiz.addIllegalLogStolenLostCard(urPcidCd, urUserId);
				}
				// 불가 여부
				if(InicisPcIllegalCode.isTransactionNotCard(code)) {
					systemLogBiz.addIllegalLogTransactionNotCard(urPcidCd, urUserId);
				}
			}

		} catch (Exception e) {
			log.error("ERROR ====== 이니시스 주문 부정거래 체크 ::{}", e);
		}
	}
}
