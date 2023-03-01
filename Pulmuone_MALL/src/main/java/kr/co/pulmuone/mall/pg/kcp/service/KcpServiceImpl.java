package kr.co.pulmuone.mall.pg.kcp.service;

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
import kr.co.pulmuone.v1.comm.enums.PgEnums.KcpCode;
import kr.co.pulmuone.v1.comm.enums.PgEnums.KcpIllegalCode;
import kr.co.pulmuone.v1.comm.enums.PgEnums.PgAccountType;
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
import kr.co.pulmuone.v1.order.regular.dto.vo.OrderRegularPaymentKeyVo;
import kr.co.pulmuone.v1.order.regular.service.OrderRegularBiz;
import kr.co.pulmuone.v1.pg.dto.CancelRequestDto;
import kr.co.pulmuone.v1.pg.dto.CancelResponseDto;
import kr.co.pulmuone.v1.pg.dto.EtcDataCartDto;
import kr.co.pulmuone.v1.pg.service.PgBiz;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApplyRegularBatchKeyRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApplyRegularBatchKeyResponseDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApprovalRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApprovalResponseDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpVirtualAccountReturnRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.service.KcpPgService;
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
 *  1.0    20201005   	 홍진영            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class KcpServiceImpl implements KcpService {

	@Autowired
	PgBiz pgBiz;

	@Autowired
	KcpPgService kcpPgService;

	@Autowired
	OrderRegularBiz oirderRegularBiz;

	@Autowired
	ShoppingCartBiz shoppingCartBiz;

	@Autowired
	OrderOrderBiz orderOrderBiz;

	@Autowired
	ClaimRequestBiz claimRequestBiz;

	@Autowired
	ClaimProcessBiz claimProcessBiz;

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
	 * 승인 요청
	 */
	@Override
	public String approval(KcpApprovalRequestDto reqDto) throws Exception {

		EtcDataCartDto etcDataCartDto = kcpPgService.toDtoEtcData(reqDto.getParam_opt_1(), EtcDataCartDto.class);

		// PG 승인 요청 결과 확인
		if (!KcpCode.SUCCESS.getCode().equals(reqDto.getRes_cd())) {
			// 고객 취소 요청시 (3001 : 공통|사용자 취소, AC45 : 계좌이체|사용자 취소)
			if(KcpCode.USER_CANCEL.getCode().equals(reqDto.getRes_cd()) || KcpCode.USER_CANCEL_BANK.getCode().equals(reqDto.getRes_cd())) {
				return pgBiz.orderFail(etcDataCartDto.getOrderInputUrl());
			} else {
				return pgBiz.orderFail(reqDto.getRes_cd(), reqDto.getRes_msg());
			}
		}

		// 주문번호로 주문데이터 조회
		PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(reqDto.getOrdr_idxx());

		// 유호성 검증
		PgEnums.PgErrorType validation = orderOrderBiz.isPgApprovalOrderValidation(orderData);
		if (!PgEnums.PgErrorType.SUCCESS.getCode().equals(validation.getCode())) {
			return pgBiz.orderFail(validation.getCode(), validation.getCodeName());
		}

		// 금액 검증을 해 paymentPrice 세팅
		reqDto.setPaymentPrice(orderData.getPaymentPrice());

		KcpApprovalResponseDto approvalResDto = null;

		try {
			// PG 승인 처리
			approvalResDto = kcpPgService.approval(reqDto);

			// PG 승인 결과 확인
			if (!KcpCode.SUCCESS.getCode().equals(approvalResDto.getRes_cd())) {
				log.error("ERROR ====== KCP 주문 승인 approval orderFail - approvalResDto ::{}", approvalResDto);
				// 부정거래 방지 체크
				checkIllegal(approvalResDto.getRes_cd());
				return pgBiz.orderFail(approvalResDto.getRes_cd(), approvalResDto.getRes_msg());
			}

			// 주문 정보에 결제 타입 변경
			orderOrderBiz.convertPgApprovalOrderDataDto(orderData, etcDataCartDto);

			// 주문 데이터 처리
			PutOrderPaymentCompleteResDto putResDto = orderOrderBiz.putOrderPaymentComplete(orderData, approvalResDto);

			if (PgEnums.PgErrorType.SUCCESS.getCode().equals(putResDto.getResult().getCode())) {
				if (etcDataCartDto.getSpCartId() != null && !etcDataCartDto.getSpCartId().isEmpty()) {
					// 장바구니 삭제
					DelCartRequestDto delCartRequestDto = new DelCartRequestDto();
					delCartRequestDto.setSpCartId(etcDataCartDto.getSpCartId());
					shoppingCartBiz.delCartAndAddGoods(delCartRequestDto);
				}
				// 성공 처리
				return pgBiz.orderSuccess(approvalResDto.getOrdr_idxx(), etcDataCartDto.getCartType(), orderData.getOrderPaymentType(), orderData.getPresentYn());
			} else {
				// PG 취소
				CancelRequestDto cancelReqDto = new CancelRequestDto();
				cancelReqDto.setPartial(false);
				cancelReqDto.setTid(approvalResDto.getTno());
				cancelReqDto.setCancelMessage("가맹점 결과 처리 오류 - " + putResDto.getResult().getCodeName());
				cancelReqDto.setOdid(approvalResDto.getOrdr_idxx());

				kcpPgService.cancel(putResDto.getPaymentData().getPgAccountType().getCode(), cancelReqDto);

				// 실패처리
				return pgBiz.orderFail(putResDto.getResult().getCode(), putResDto.getResult().getCodeName());
			}
		} catch (Exception e) {
			log.error("ERROR ====== KCP 주문 승인 approval");
			log.error("ERROR ====== KCP 주문 승인 reqDto ::{}", reqDto);
			log.error("ERROR ====== KCP 주문 승인 orderData ::{}", orderData);
			log.error("ERROR ====== KCP 주문 승인 approvalResDto ::{}", approvalResDto);
			log.error("ERROR ====== KCP 주문 승인 Exception", e);

			// PG 취소
			CancelRequestDto cancelReqDto = new CancelRequestDto();
			cancelReqDto.setPartial(false);
			cancelReqDto.setTid(approvalResDto.getTno());
			cancelReqDto.setCancelMessage("가맹점 결과 처리 오류 - " + PgEnums.PgErrorType.FAIL_UPDATE_ERROR.getCodeName());
			cancelReqDto.setOdid(approvalResDto.getOrdr_idxx());

			PgEnums.PgAccountType pgAccountType = null;
			if (PaymentType.isSimplePay(orderData.getOrderPaymentType())) {
				pgAccountType = PgEnums.PgAccountType.KCP_SIMPLE;
			} else {
				pgAccountType = PgEnums.PgAccountType.KCP_BASIC;
			}

			kcpPgService.cancel(pgAccountType.getCode(), cancelReqDto);

			// 실패처리
			return pgBiz.orderFail(PgEnums.PgErrorType.FAIL_UPDATE_ERROR.getCode(), PgEnums.PgErrorType.FAIL_UPDATE_ERROR.getCodeName());
		}
	}

	/**
	 * 가상계좌 응답
	 */
	@Override
	public void virtualAccountReturn(HttpServletResponse httpServletResponse, KcpVirtualAccountReturnRequestDto reqDto)
			throws Exception {
		// 가상계좌 || 모바일 계좌이체 통보
		String resultCode = KcpCode.DEPOSIT_NOTI_FAIL.getCode();

		if (KcpCode.DEPOSIT_NOTI_PC.getCode().equals(reqDto.getTx_cd()) || KcpCode.DEPOSIT_NOTI_MOBILE.getCode().equals(reqDto.getTx_cd())) {
			// 관리자에서 주문 생성시 주문번호 여러게에 결제를 한번 할수 있는 케이스가 있어 PG 주문번호에 odPaymentMasterId 를 보냄
			if (orderOrderBiz.isOdidValuePaymentMasterId(reqDto.getOrder_no())) {
				String odPaymentMasterId = orderOrderBiz.getPaymentMasterIdByOdid(reqDto.getOrder_no());
				List<PgApprovalOrderDataDto> orderDataList = orderOrderBiz.getPgApprovalOrderDataByOdPaymentMasterId(odPaymentMasterId);
				if (reqDto.getTotl_mnyx().equals(String.valueOf(orderDataList.get(0).getPaymentPrice()))) {
					for (PgApprovalOrderDataDto orderData : orderDataList) {
						if (OrderEnums.OrderStatus.INCOM_READY.getCode().equals(orderData.getStatus())) {
							// 주문 입금 확인 처리
							orderOrderBiz.putVirtualBankOrderPaymentComplete(orderData, reqDto);
							resultCode = KcpCode.DEPOSIT_NOTI_SUCCESS.getCode();
						} else if (OrderEnums.OrderStatus.INCOM_COMPLETE.getCode().equals(orderData.getStatus())) {
							// 서버 이슈 및 결제 처리 이후 다시 API 시도시 성공으로 리턴
							resultCode = KcpCode.DEPOSIT_NOTI_SUCCESS.getCode();
						}
					}
				}
			} else {
				// 클레임 추가배송비 여부
				if(orderOrderBiz.isOdidValueAddPaymentMasterId(reqDto.getOrder_no())) {
					OrderClaimAddPaymentInfoDto claimAddPaymentInfo = claimProcessBiz.getAddPaymentClaimInfo(reqDto);
					// 클레임 추가배송비가 존재할 경우
					if(ObjectUtils.isNotEmpty(claimAddPaymentInfo)) {
						// 금액 비교 및 입금 예정 체크
						if (reqDto.getTotl_mnyx().equals(String.valueOf(claimAddPaymentInfo.getPaymentPrice()))) {
							if (OrderEnums.OrderStatus.INCOM_READY.getCode().equals(claimAddPaymentInfo.getStatus())) {
								// 주문 입금 확인 처리
								claimProcessBiz.putAddPaymentClaimInfo(claimAddPaymentInfo);
								resultCode = KcpCode.DEPOSIT_NOTI_SUCCESS.getCode();
							} else if (OrderEnums.OrderStatus.INCOM_COMPLETE.getCode().equals(claimAddPaymentInfo.getStatus())) {
								// 서버 이슈 및 결제 처리 이후 다시 API 시도시 성공으로 리턴
								resultCode = KcpCode.DEPOSIT_NOTI_SUCCESS.getCode();
							}
						}
					}
				}
				else {
					// 주문번호로 주문데이터 조회
					PgApprovalOrderDataDto orderData = orderOrderBiz.getPgApprovalOrderDataByOdid(reqDto.getOrder_no());

					// 금액 비교 및 입금 예정 체크
					if (reqDto.getTotl_mnyx().equals(String.valueOf(orderData.getPaymentPrice()))) {
						if (OrderEnums.OrderStatus.INCOM_READY.getCode().equals(orderData.getStatus())) {
							// 주문 입금 확인 처리
							orderOrderBiz.putVirtualBankOrderPaymentComplete(orderData, reqDto);
							resultCode = KcpCode.DEPOSIT_NOTI_SUCCESS.getCode();
						} else if (OrderEnums.OrderStatus.INCOM_COMPLETE.getCode().equals(orderData.getStatus())) {
							// 서버 이슈 및 결제 처리 이후 다시 API 시도시 성공으로 리턴
							resultCode = KcpCode.DEPOSIT_NOTI_SUCCESS.getCode();
						} else if(OrderEnums.OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode().equals(orderData.getStatus())) {
							// 입금전 취소일 경우 환불 프로세스 처리
							OrderAccountDto orderAccountDto = claimUtilRefundService.getRefundBank(orderData.getOdOrderId());

							if(ObjectUtils.isNotEmpty(orderAccountDto)){
								// PG 취소
								CancelRequestDto cancelReqDto = new CancelRequestDto();
								cancelReqDto.setPartial(false);
								cancelReqDto.setTid(reqDto.getTno());
								cancelReqDto.setPaymentType(PaymentType.VIRTUAL_BANK);
								cancelReqDto.setCancelMessage("입금전취소상태로 인한 환불");
								cancelReqDto.setOdid(orderData.getOdid());
								cancelReqDto.setRefundBankNumber(orderAccountDto.getAccountNumber());
								cancelReqDto.setRefundBankCode(pgBiz.getPgBankCode(PgServiceType.KCP.getCode(), PaymentType.VIRTUAL_BANK.getCode(), orderAccountDto.getBankCode()));
								cancelReqDto.setRefundBankName(orderAccountDto.getHolderName());

								CancelResponseDto cancelResponseDto = kcpPgService.cancel(PgAccountType.KCP_BASIC.getCode(), cancelReqDto);
								if(cancelResponseDto.isSuccess()) {
									resultCode = KcpCode.DEPOSIT_NOTI_SUCCESS.getCode();
								} else {
									log.error("========== 가상계좌 KCP 입금통보 입금전취소상태로 인한 환불실패 - cancelReqDto ::{}", cancelReqDto);
									log.error("========== 가상계좌 KCP 입금통보 입금전취소상태로 인한 환불실패 - cancelResponseDto ::{}", cancelResponseDto);
								}
							} else {
								log.error("========== 가상계좌 KCP 입금통보 입금전취소상태로 인한 환불실패 - 환불계좌 조회안됨 ::{}", orderData);
							}
						}
					}
				}
			}
		}

		try {
			httpServletResponse.setContentType("text/html;charset=UTF-8");

			PrintWriter writer = httpServletResponse.getWriter();
			writer.println("<html><body><form><input type=\"hidden\" name=\"result\" value=\"" + resultCode
					+ "\"></form></body></html>");
			writer.flush();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 승인 요청 응답
	 */
	@Override
	public void applyRegularBatchKey(HttpServletResponse httpServletResponse,
			KcpApplyRegularBatchKeyRequestDto kcpApplyRegularBatchKeyRequestDto) throws Exception {

		// 회원 유효성 검증

		// 배치 키 발급 승인
		KcpApplyRegularBatchKeyResponseDto resDto = kcpPgService
				.applyRegularBatchKey(kcpApplyRegularBatchKeyRequestDto);

		httpServletResponse.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = httpServletResponse.getWriter();

		// 데이터 등록
		if (KcpCode.SUCCESS.getCode().equals(resDto.getRes_cd())) {
			OrderRegularPaymentKeyVo orderRegularPaymentKeyVo = new OrderRegularPaymentKeyVo();
			orderRegularPaymentKeyVo.setUrUserId(Long.valueOf(resDto.getParam_opt_2()));
			orderRegularPaymentKeyVo.setBatchKey(resDto.getBatch_key());
			orderRegularPaymentKeyVo.setCardName(resDto.getCard_name());
			orderRegularPaymentKeyVo.setCardMaskNumber(resDto.getCard_mask_no());
			oirderRegularBiz.addRegularPaymentKey(orderRegularPaymentKeyVo);
		}

		writer.println("<html><header><script>"
				+ (!KcpCode.SUCCESS.getCode().equals(resDto.getRes_cd()) ? "alert('" + resDto.getRes_msg() + "');" : "")
				+ "document.location.href='" + kcpApplyRegularBatchKeyRequestDto.getParam_opt_1() + "';" + "</script></header></html>");
		writer.flush();
		writer.close();
	}

	/**
	 * 추가결제 승인 요청
	 */
	@Override
	public String addPayApproval(KcpApprovalRequestDto reqDto) throws Exception {
		reqDto.setAddApproval(true);
		log.debug("KCP 추가결제 승인요청 reqDto :: <{}>", reqDto);
		log.debug("KCP 추가결제 승인요청 reqDto.getRes_cd() :: <{}>", reqDto.getRes_cd());
		// PG 승인 요청 결과 확인
		if (!KcpCode.SUCCESS.getCode().equals(reqDto.getRes_cd())) {
			return pgBiz.orderFail(reqDto.getRes_cd(), reqDto.getRes_msg());
		}

		EtcDataClaimDto etcDataClaimDto = kcpPgService.toDtoEtcData(reqDto.getParam_opt_1(), EtcDataClaimDto.class);

		// 클레임 ID가 존재할 경우 추가배송비 직접결제
		if(etcDataClaimDto.getOdClaimId() > 0){

			// 1. 클래임PK 로 클래임 정보 조회
			OrderClaimRegisterRequestDto orderClaimRegisterRequestDto = new OrderClaimRegisterRequestDto();
			orderClaimRegisterRequestDto.setOdClaimId(etcDataClaimDto.getOdClaimId());
			MallOrderClaimAddPaymentResult mallOrderClaimAddPaymentResult = claimRequestBiz.getOrderClaimAddShippingPriceInfo(orderClaimRegisterRequestDto);

			reqDto.setPaymentPrice(mallOrderClaimAddPaymentResult.getAddPaymentShippingPrice());

			// 2. PG 승인 처리
			KcpApprovalResponseDto approvalResDto = kcpPgService.approval(reqDto);

			log.debug("KCP 승인처리 결과 :: <{}>", approvalResDto);
			log.debug("KCP 결과코드 res_cd :: <{}>", approvalResDto.getRes_cd());

			// 3. PG 승인 결과 확인
			if (!KcpCode.SUCCESS.getCode().equals(approvalResDto.getRes_cd())) {
				// 부정거래 방지 체크
				checkIllegal(approvalResDto.getRes_cd());
				return pgBiz.claimFail(approvalResDto.getRes_cd(), approvalResDto.getRes_msg());
			}

			// 4. 추가결제요청정보 조회
			OdAddPaymentReqInfo addPaymentReqInfo = claimProcessBiz.getOdAddPaymentReqInfo(etcDataClaimDto.getOdAddPaymentReqInfoId());
			// 5. 추가결제요청정보 데이터 JSON -> VO 변환
			OrderClaimRegisterRequestDto requestDto = kcpPgService.toDtoJsonString(addPaymentReqInfo.getReqJsonInfo(), OrderClaimRegisterRequestDto.class);

			// 6. PG 정보 PgApprovalOrderPaymentDataDto 로 변경
			PgApprovalOrderPaymentDataDto pgApprovalOrderPaymentDataDto = orderOrderBiz.convertPgApprovalOrderPaymentData(requestDto.getAddPaymentInfo().getPsPayCd(), approvalResDto);

			// 7. 클레임 상태 업데이트 : 클레임상태코드, 직접결제여부,
			mallOrderClaimAddPaymentResult.setUrUserId(Long.parseLong(requestDto.getUrUserId()));
			// 비회원CI 정보가 존재할 경우
			if(StringUtil.isNotEmpty(requestDto.getGuestCi())) {
				mallOrderClaimAddPaymentResult.setUrUserId(Constants.GUEST_CREATE_USER_ID);
			}
			OrderClaimRegisterResponseDto claimRegResDto = claimProcessBiz.putOrderClaimInfo(mallOrderClaimAddPaymentResult);

			log.debug("KCP claimRegResDto :: <{}>", claimRegResDto);
			log.debug("KCP pgApprovalOrderPaymentDataDto :: <{}>", pgApprovalOrderPaymentDataDto);

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
			} else {
				// PG 취소
				CancelRequestDto cancelReqDto = new CancelRequestDto();
				cancelReqDto.setPartial(false);
				cancelReqDto.setTid(approvalResDto.getTno());
				cancelReqDto.setCancelMessage("가맹점 결과 처리 오류 - " + claimRegResDto.getOrderRegistrationResult().getCodeName());
				cancelReqDto.setOdid(approvalResDto.getOrdr_idxx());

				kcpPgService.cancel(pgApprovalOrderPaymentDataDto.getPgAccountType().getCode(), cancelReqDto);

				// 실패처리
				return pgBiz.claimFail(claimRegResDto.getOrderRegistrationResult().getCode(), claimRegResDto.getOrderRegistrationResult().getCodeName());
			}

		} else {
//			log.debug("KCP 결제 받은 주문번호 ordr_idxx ::: <{}>", reqDto.getOrdr_idxx());
//			String[] ordrArray = reqDto.getOrdr_idxx().split("-");
//			long odAddPaymentReqInfoId = 0;
//			log.debug("KCP 결제 받은 주문번호 ordrArray 배열 존재 유무::: <{}>", ArrayUtils.isEmpty(ordrArray));
//
//			if (!ArrayUtils.isEmpty(ordrArray)) {
//				odAddPaymentReqInfoId = Long.parseLong(ordrArray[1]);
//				log.debug("KCP 결제 받은 주문번호 ordrArray 배열 값::: <{}>, <{}>, <{}>, <{}>", ordrArray, ordrArray[0], ordrArray[1], odAddPaymentReqInfoId);
//			}
			long odAddPaymentReqInfoId = etcDataClaimDto.getOdAddPaymentReqInfoId();

			// 추가결제요청정보PK로 요정정보 조회 데이터를 가지고 온다
			OdAddPaymentReqInfo addPaymentReqInfo = claimProcessBiz.getOdAddPaymentReqInfo(odAddPaymentReqInfoId);
			OrderClaimRegisterRequestDto requestDto = kcpPgService.toDtoJsonString(addPaymentReqInfo.getReqJsonInfo(), OrderClaimRegisterRequestDto.class);
			OrderClaimPriceInfoDto claimPriceInfoDto = claimRequestProcessBiz.getRefundPriceInfo(requestDto);

			log.debug("KCP 추가결제 조회 결과 ::: <{}>, <{}>", claimPriceInfoDto.getAddPaymentShippingPrice(), addPaymentReqInfo);
			// 금액 검증을 해 paymentPrice 세팅
			reqDto.setPaymentPrice(claimPriceInfoDto.getAddPaymentShippingPrice());

			// PG 승인 처리
			KcpApprovalResponseDto approvalResDto = kcpPgService.approval(reqDto);

			log.debug("KCP 승인처리 결과 :: <{}>", approvalResDto);
			log.debug("KCP 결과코드 res_cd :: <{}>", approvalResDto.getRes_cd());

			// PG 승인 결과 확인
			if (!KcpCode.SUCCESS.getCode().equals(approvalResDto.getRes_cd())) {
				return pgBiz.claimFail(approvalResDto.getRes_cd(), approvalResDto.getRes_msg());
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
			PgApprovalOrderPaymentDataDto pgApprovalOrderPaymentDataDto = orderOrderBiz.convertPgApprovalOrderPaymentData(requestDto.getAddPaymentInfo().getPsPayCd(), approvalResDto);

			log.debug("KCP claimRegResDto :: <{}>", claimRegResDto);
			log.debug("KCP pgApprovalOrderPaymentDataDto :: <{}>", pgApprovalOrderPaymentDataDto);

			if (OrderEnums.OrderRegistrationResult.SUCCESS.getCode().equals(claimRegResDto.getOrderRegistrationResult().getCode())) {
				// 클래임 결제 정보 등록 처리
				claimUtilProcessService.addClaimPaymentInfo(requestDto, claimPriceInfoDto, pgApprovalOrderPaymentDataDto);

				// 성공 처리
				return pgBiz.claimSuccess(claimRegResDto.getOdClaimId());
			} else {
				// PG 취소
				CancelRequestDto cancelReqDto = new CancelRequestDto();
				cancelReqDto.setPartial(false);
				cancelReqDto.setTid(approvalResDto.getTno());
				cancelReqDto.setCancelMessage("가맹점 결과 처리 오류 - " + claimRegResDto.getMessage());
				cancelReqDto.setOdid(approvalResDto.getOrdr_idxx());

				kcpPgService.cancel(pgApprovalOrderPaymentDataDto.getPgAccountType().getCode(), cancelReqDto);

				// 실패처리
				return pgBiz.claimFail(claimRegResDto.getOrderRegistrationResult().getCode(), claimRegResDto.getMessage());
			}
		}
	}

	/**
	 * 부정거래 체크
	 * @param code
	 */
	private void checkIllegal(String code) {
		try {
			BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
			String urPcidCd = CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY);

			Long urUserId = StringUtil.isNotEmpty(buyerVo.getUrUserId()) ? Long.valueOf(buyerVo.getUrUserId()) : 0L;

			// 도난 여부
			if(KcpIllegalCode.isStolenLostCard(code)) {
				systemLogBiz.addIllegalLogStolenLostCard(urPcidCd, urUserId);
			}
			// 불가 여부
			if(KcpIllegalCode.isTransactionNotCard(code)) {
				systemLogBiz.addIllegalLogTransactionNotCard(urPcidCd, urUserId);
			}
		} catch (Exception e) {
			log.error("ERROR ====== KCP 주문 부정거래 체크 ::{}", e);
		}
	}
}
