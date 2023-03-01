package kr.co.pulmuone.v1.order.claim.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalCsRefundRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.order.claim.OrderClaimMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimDetlVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderCashReceiptVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentVo;
import kr.co.pulmuone.v1.order.registration.service.OrderRegistrationBiz;
import kr.co.pulmuone.v1.pg.dto.*;
import kr.co.pulmuone.v1.pg.service.PgAbstractService;
import kr.co.pulmuone.v1.pg.service.PgBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 생성, 수정, 삭제 관련 Service
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
@RequiredArgsConstructor
public class ClaimCompleteProcessService {

    private final OrderClaimMapper orderClaimMapper;

    @Autowired
    private ClaimUtilProcessService claimUtilProcessService;

    @Autowired
    private PgBiz pgBiz;

    @Autowired
    private OrderRegistrationBiz orderRegistrationBiz;

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 자기 자신 주문인지 체크
     * @param orderClaimRegisterRequestDto
     * @return int
     */
    protected int getOrderClaimUserOrderCnt(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) {
        return orderClaimMapper.getOrderClaimUserOrderCnt(orderClaimRegisterRequestDto);
    }

    /**
     * 클레임 상세 조회
     * @param odClaimId
     * @return List<OrderClaimRestoreDto>
     */
    protected List<OrderClaimRestoreDto> getOrderClaimDetlList(long odClaimId){
        return orderClaimMapper.getOrderClaimDetlList(odClaimId);
    }

    /**
     * 주문 상세 취소 가능 수량 계산
     * @param odClaimId
     * @param claimStatusCd
     * @return
     */
    protected int putOrderDetlCancelCnt(long odClaimId, String claimStatusCd) {
        return orderClaimMapper.putOrderDetlCancelCnt(odClaimId, claimStatusCd);
    }

    /**
     * 거부사유 등록
     * @param requestDto
     * @return int
     */
    protected int putRejectReasonMsg(OrderClaimRegisterRequestDto requestDto) {
        return orderClaimMapper.putRejectReasonMsg(requestDto);
    }

    /**
     * 주문상세 취소 미사용
     * @param odClaimId
     * @param claimStatusCd
     * @return
     */
    protected int putOdClaimDetlNoUse(long odClaimId, String claimStatusCd) {
        return orderClaimMapper.putOdClaimDetlNoUse(odClaimId, claimStatusCd);
    }

    /**
     * 주문 클레임 추가 결제 정보 마스터 조회
     * @param orderClaimRestoreRequestDto
     * @return
     */
    protected List<OrderClaimRestoreResultDto> getOrderClaimAddPaymentMaster(OrderClaimRestoreRequestDto orderClaimRestoreRequestDto) {
        return orderClaimMapper.getOrderClaimAddPaymentMaster(orderClaimRestoreRequestDto);
    }

    /**
     * 주문유형 조회
     * @param odOrderId
     * @return
     */
    protected OrderClaimOutmallPaymentInfoDto getOrderAgentType(long odOrderId) {
        return orderClaimMapper.getOrderAgentType(odOrderId);
    }

    /**
     * 주문 CS환불 승인 리스트
     * @param requestDto
     * @return
     */
    protected ApprovalCsRefundListResponseDto getApprovalCsRefundList(ApprovalCsRefundRequestDto requestDto) {

    	requestDto.setApprovalStatusArray(getSearchKeyToSearchKeyList(requestDto.getSearchApprovalStatus().replaceAll("ALL∀",""))); // CS환불 구분
    	requestDto.setCsRefundTpList(getSearchKeyToSearchKeyList(requestDto.getCsRefundTp().replaceAll("ALL∀",""))); // CS환불 구분

		ArrayList<String> orderCdArray;
       	if(requestDto.getSelectConditionType().equals("singleSection") && !StringUtil.isEmpty(requestDto.getSearchSingleType())) {
       		//화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
            String orderCodeListStr = requestDto.getCodeSearch().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
            orderCdArray = StringUtil.getArrayListComma(orderCodeListStr);
            requestDto.setCodeSearchList(orderCdArray);
       	}

        PageMethod.startPage(requestDto.getPage(), requestDto.getPageSize());
        Page<ApprovalCsRefundListDto> response = orderClaimMapper.getApprovalCsRefundList(requestDto);

        return ApprovalCsRefundListResponseDto.builder()
                .total(response.getTotal())
                .rows(response.getResult())
                .build();
    }

    private List<String> getSearchKeyToSearchKeyList(String searchKey) {
        List<String> searchKeyList = new ArrayList<>();
        if( StringUtils.isNotEmpty(searchKey) && !searchKey.contains("ALL")) {

            searchKeyList.addAll(Stream.of(searchKey.split(Constants.ARRAY_SEPARATORS))
                                       .map(String::trim)
                                       .filter(StringUtils::isNotEmpty)
                                       .collect(Collectors.toList()));
        }
        return searchKeyList;
    }

    /**
     * CS환불 승인 요청철회
     *
     * @param approvalVo
     * @return MessageCommEnum
     */
	protected MessageCommEnum putCancelRequestApprovalCsRefund(ApprovalStatusVo approvalVo) throws Exception {
		if (orderClaimMapper.putCancelRequestApprovalCsRefund(approvalVo) > 0 && this.addClaimStatusHistory(approvalVo) > 0) {
			return BaseEnums.Default.SUCCESS;
		} else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
	}

    /**
     * CS환불 승인처리
     *
     * @param approvalVo
     * @return MessageCommEnum
     */
	protected MessageCommEnum putApprovalProcessCsRefund(ApprovalStatusVo approvalVo) throws Exception {
		if (orderClaimMapper.putApprovalProcessCsRefund(approvalVo) > 0 && this.addClaimStatusHistory(approvalVo) > 0) {
			return BaseEnums.Default.SUCCESS;
		} else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
	}

    /**
     * CS환불 승인 폐기처리
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putDisposalApprovalCsRefund(ApprovalStatusVo approvalVo) throws Exception {
		if(orderClaimMapper.putDisposalApprovalCsRefund(approvalVo) > 0
			&& this.addClaimStatusHistory(approvalVo) > 0 ) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }

	/**
  	 * CS환불 승인 상태이력 등록
  	 * @param history
  	 * @return int
  	 */
  	protected int addClaimStatusHistory(ApprovalStatusVo history){
  		return orderClaimMapper.addClaimStatusHistory(history);
  	}

    /**
     * 반품 클레임 정보 업데이트
     * @param requestDto
     */
    protected int putOrderClaimTargetInfo(OrderClaimRegisterRequestDto requestDto) {
        log.debug("------------------ 반품 클레임 정보 업데이트");
        String targetTp = requestDto.getTargetTp();

        // 주문상태가 반품승인이고, 클레임상태가 반품완료일 경우 귀책구분은 업데이트 하지 않는다.
        if( OrderEnums.OrderStatus.RETURN_ING.getCode().equals(requestDto.getOrderStatusCd()) &&
            OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(requestDto.getClaimStatusCd())) {
            targetTp = "";
        }
        ClaimVo updateClaimVo = ClaimVo.builder()
                                        .odClaimId(requestDto.getOdClaimId())
                                        .targetTp(targetTp)
                                        .returnsYn(requestDto.getReturnsYn())
                                        .directPaymentYn(requestDto.getDirectPaymentYn())
                                        .addPaymentTp(requestDto.getAddPaymentTp())
                                        .refundPrice(requestDto.getRefundPrice())
                                        .refundPointPrice(requestDto.getRefundPointPrice())
                                        .build();
       return orderClaimMapper.putOrderClaimTargetTp(updateClaimVo);
    }

    /**
     * 취소 / 반품 배송비 환불 처리
     * @param orderClaimAddShippingPaymentInfoDto
     * @return
     * @throws Exception
     */
    protected int setOrderReturnsShippingPrice(OrderClaimAddShippingPaymentInfoDto orderClaimAddShippingPaymentInfoDto, int frontTp) throws Exception {
        log.debug("------------------ 취소 / 반품 배송비 환불 처리");

        UserVo adminUser = null;
        // 프론트 타입이 BOS일 경우에만
        if(frontTp == OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_BOS.getCodeValue()) {
            try {
                adminUser = SessionUtil.getBosUserVO();
            }
            catch (Exception e) {
                log.debug("refundPrice UserVo is Null :: <{}>", e.getMessage());
            }
        }

        int refundCnt = 0;

        boolean isPartial = false;
        if (orderClaimAddShippingPaymentInfoDto.getPaymentPrice() != orderClaimAddShippingPaymentInfoDto.getRefundRequestShippingPrice()) {
            isPartial = true;
        }
        PgEnums.PgAccountType pgAccountType = PgEnums.PgAccountType.findByCode(orderClaimAddShippingPaymentInfoDto.getPgService());
        PgEnums.PgServiceType pgServiceType = PgEnums.PgServiceType.findByCode(pgAccountType.getPgServiceType());
        PgAbstractService<?, ?> pgService = pgBiz.getService(pgServiceType);
        OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(orderClaimAddShippingPaymentInfoDto.getPayTp());

        CancelRequestDto cancelReqDto = new CancelRequestDto();
        cancelReqDto.setPartial(isPartial);                                             //부분취소 여부
        cancelReqDto.setCancelMessage("추가 결제 취소");                                  //취소사유
        cancelReqDto.setOdid(orderClaimAddShippingPaymentInfoDto.getOdid());            //추문번호
        cancelReqDto.setCancelPrice(orderClaimAddShippingPaymentInfoDto.getRefundRequestShippingPrice());       //취소금액
        cancelReqDto.setTid(orderClaimAddShippingPaymentInfoDto.getTid());              //거래번호
        cancelReqDto.setPaymentType(paymentType);                                       //취소지불수단
        cancelReqDto.setTaxCancelPrice(orderClaimAddShippingPaymentInfoDto.getRefundRequestShippingPrice());    //취소 과세 금액
        cancelReqDto.setTaxFreecancelPrice(0);                                          //취소 비과세 금액
        cancelReqDto.setExpectedRestPrice(orderClaimAddShippingPaymentInfoDto.getPaymentPrice() - orderClaimAddShippingPaymentInfoDto.getRefundRequestShippingPrice());       //취소후 남은 금액 (부분취소시 필수)
        cancelReqDto.setEscrowYn(orderClaimAddShippingPaymentInfoDto.getEscrowYn());    //에스크로결제여부
        log.debug("환불할 정보 ::: <{}>", cancelReqDto);
        log.debug("PG TYPE :: <{}>", pgAccountType.getCode());

        CancelResponseDto cardDto = pgService.cancel(pgAccountType.getCode(), cancelReqDto);

        log.info("카드 취소 결과 ::: <{}>", cardDto);

        if (cardDto.isSuccess()) {
            OrderClaimRegisterRequestDto paymentReqDt = new OrderClaimRegisterRequestDto();
            paymentReqDt.setOdOrderId(orderClaimAddShippingPaymentInfoDto.getOdOrderId());
            paymentReqDt.setOdClaimId(orderClaimAddShippingPaymentInfoDto.getOdClaimId());
            paymentReqDt.setShippingPrice(orderClaimAddShippingPaymentInfoDto.getRefundRequestShippingPrice());
            paymentReqDt.setTaxablePrice(orderClaimAddShippingPaymentInfoDto.getRefundRequestShippingPrice());
            paymentReqDt.setRefundPrice(orderClaimAddShippingPaymentInfoDto.getRefundRequestShippingPrice());
            paymentReqDt.setType(OrderEnums.PayType.F.getCode());
            paymentReqDt.setPayTp(orderClaimAddShippingPaymentInfoDto.getPayTp());
            paymentReqDt.setPgService(pgServiceType.getCode());
            paymentReqDt.setTid(orderClaimAddShippingPaymentInfoDto.getTid());
            paymentReqDt.setPartCancelYn(orderClaimAddShippingPaymentInfoDto.getPartCancelYn());
            paymentReqDt.setEscrowYn(orderClaimAddShippingPaymentInfoDto.getEscrowYn());
            paymentReqDt.setResponseData(objectMapper.writeValueAsString(cardDto));
            claimUtilProcessService.putOrderPaymentInfo(paymentReqDt);
            refundCnt++;
        } else {
            // BOS 사용자 이고, 관리자_LEVEL_1 권한일 경우
            if(ObjectUtils.isNotEmpty(adminUser) && adminUser.getListRoleId().contains(Constants.ADMIN_LEVEL_1_AUTH_ST_ROLE_TP_ID)) {
                log.debug("====================== 관리자_LEVEL_1 권한 정상 처리");
                // 실패 여부와 상관없이 정상 처리
                OrderClaimRegisterRequestDto paymentReqDt = new OrderClaimRegisterRequestDto();
                paymentReqDt.setOdOrderId(orderClaimAddShippingPaymentInfoDto.getOdOrderId());
                paymentReqDt.setOdClaimId(orderClaimAddShippingPaymentInfoDto.getOdClaimId());
                paymentReqDt.setShippingPrice(orderClaimAddShippingPaymentInfoDto.getRefundRequestShippingPrice());
                paymentReqDt.setTaxablePrice(orderClaimAddShippingPaymentInfoDto.getRefundRequestShippingPrice());
                paymentReqDt.setRefundPrice(orderClaimAddShippingPaymentInfoDto.getRefundRequestShippingPrice());
                paymentReqDt.setType(OrderEnums.PayType.F.getCode());
                paymentReqDt.setPayTp(orderClaimAddShippingPaymentInfoDto.getPayTp());
                paymentReqDt.setPgService(pgServiceType.getCode());
                paymentReqDt.setTid(orderClaimAddShippingPaymentInfoDto.getTid());
                paymentReqDt.setPartCancelYn(orderClaimAddShippingPaymentInfoDto.getPartCancelYn());
                paymentReqDt.setEscrowYn(orderClaimAddShippingPaymentInfoDto.getEscrowYn());
                paymentReqDt.setResponseData(objectMapper.writeValueAsString(cardDto));
                claimUtilProcessService.putOrderPaymentInfo(paymentReqDt);
                refundCnt++;
            }
            else {
                throw new BaseException(cardDto.getMessage());
            }
        }

        return refundCnt;
    }

    /**
     * 현금영수증 발행정보 조회(관리자가 BOS에서 발급한 건에 한함)
     * @param odOrderId
     * @return OrderClaimCashReceiptDto
     */
    protected OrderClaimCashReceiptDto getOrderCashReceiptByBos(long odOrderId){
        return orderClaimMapper.getOrderCashReceiptByBos(odOrderId);
    }

    /**
     * 현금영수증 재발행 금액 조회
     * @param odOrderId
     * @param tid
     * @return OrderPaymentVo
     */
    protected OrderPaymentVo getOrderCashReceiptReissuePrice(long odOrderId, String tid){
        return orderClaimMapper.getOrderCashReceiptReissuePrice(odOrderId, tid);
    }

    /**
     * 현금영수증 취소
     * @param cashReceiptDto
     * @return OrderClaimCashReceiptDto
     */
    protected void receiptCancel(OrderClaimCashReceiptDto cashReceiptDto) throws Exception{

        // PG 서비스 조회
        PgEnums.PgAccountType pgAccountType = PgEnums.PgAccountType.findByCode(cashReceiptDto.getPgService());
        PgEnums.PgServiceType pgServiceType = PgEnums.PgServiceType.findByCode(pgAccountType.getPgServiceType());
        PgAbstractService<?, ?> pgService = pgBiz.getService(pgServiceType);

        // 현금영수증 취소
        ReceiptCancelResponseDto receiptCancelResponseDto = pgService.receiptCancel(cashReceiptDto.getCashReceiptAuthNo());

        // 현금영수증 정보 테이블 취소 업데이트
        if(receiptCancelResponseDto.isSuccess()){
            orderClaimMapper.putOrderCashReceiptCancelYn(cashReceiptDto.getOdOrderCashReceiptId());
        }else{
            throw new BaseException(receiptCancelResponseDto.getMessage());
        }
    }

    /**
     * 현금영수증 재발행
     * @param cashReceiptDto
     * @param reissuePriceVo
     * @return OrderClaimCashReceiptDto
     */
    protected void receiptReissue(OrderClaimCashReceiptDto cashReceiptDto, OrderPaymentVo reissuePriceVo) throws Exception{

        // PG 서비스 조회
        PgEnums.PgAccountType pgAccountType = PgEnums.PgAccountType.findByCode(cashReceiptDto.getPgService());
        PgEnums.PgServiceType pgServiceType = PgEnums.PgServiceType.findByCode(pgAccountType.getPgServiceType());
        PgAbstractService<?, ?> pgService = pgBiz.getService(pgServiceType);

        // 현금영수증 발급 요청 파라미터 세팅
        ReceiptIssueRequestDto receiptIssueRequestDto = new ReceiptIssueRequestDto();
        receiptIssueRequestDto.setOdid(cashReceiptDto.getOdid());
        receiptIssueRequestDto.setGoodsName(cashReceiptDto.getGoodsNm());
        receiptIssueRequestDto.setReceiptType(OrderEnums.CashReceipt.findByCode(cashReceiptDto.getCashReceiptType()));
        receiptIssueRequestDto.setRegNumber(cashReceiptDto.getCashNo());
        receiptIssueRequestDto.setTotalPrice(reissuePriceVo.getPaymentPrice());
        // 공급가액 = ROUND(과세 금액 / 1.1)
        int supplyAmount = Math.round(Float.valueOf(reissuePriceVo.getTaxablePrice()) / new Float(1.1));
        receiptIssueRequestDto.setSupPrice(supplyAmount);
        //부가세 = 과세금액 - 공급가액
        receiptIssueRequestDto.setTax(reissuePriceVo.getTaxablePrice() - supplyAmount);
        receiptIssueRequestDto.setSrcvPrice(0);	//봉사료
        receiptIssueRequestDto.setBuyerName(cashReceiptDto.getBuyerNm());
        receiptIssueRequestDto.setBuyerEmail(cashReceiptDto.getBuyerMail());
        receiptIssueRequestDto.setBuyerMobile(cashReceiptDto.getBuyerHp());


        // 현금영수증 발급
        ReceiptIssueResponseDto receiptIssueResponseDto = pgService.receiptIssue(receiptIssueRequestDto);

        if(receiptIssueResponseDto.isSuccess()){
            // 현금영수증 발급 정보 저장
            OrderCashReceiptVo orderCashReceiptVo = OrderCashReceiptVo.builder()
                    .odOrderId(cashReceiptDto.getOdOrderId())
                    .odPaymentMasterId(cashReceiptDto.getOdPaymentMasterId())
                    .cashReceiptNo(receiptIssueResponseDto.getReceiptNo())
                    .cashReceiptAuthNo(receiptIssueResponseDto.getTid())
                    .cashReceiptType(cashReceiptDto.getCashReceiptType())
                    .cashPrice(reissuePriceVo.getPaymentPrice())
                    .cashNo(cashReceiptDto.getCashNo())
                    .build();
            orderRegistrationBiz.addOrderCashReceipt(orderCashReceiptVo);
        }else{
            throw new BaseException(receiptIssueResponseDto.getMessage());
        }

    }
}


