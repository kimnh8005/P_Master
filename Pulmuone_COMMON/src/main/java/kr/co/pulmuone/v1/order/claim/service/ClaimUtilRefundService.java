package kr.co.pulmuone.v1.order.claim.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.order.claim.ClaimProcessMapper;
import kr.co.pulmuone.v1.comm.mapper.order.claim.ClaimRequestMapper;
import kr.co.pulmuone.v1.comm.util.BeanUtils;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.pg.dto.BasicDataRequestDto;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.pg.dto.CancelRequestDto;
import kr.co.pulmuone.v1.pg.dto.CancelResponseDto;
import kr.co.pulmuone.v1.pg.service.PgAbstractService;
import kr.co.pulmuone.v1.pg.service.PgBiz;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisApprovalResponseDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisMobileApprovalResponseDto;
import kr.co.pulmuone.v1.pg.service.inicis.service.InicisPgService;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpApprovalResponseDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpRemitRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpRemitResponseDto;
import kr.co.pulmuone.v1.pg.service.kcp.service.KcpPgService;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.point.dto.PointRefundRequestDto;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import kr.co.pulmuone.v1.shopping.cart.dto.EtcDataClaimDto;
import kr.co.pulmuone.v1.user.buyer.dto.CommonGetRefundBankRequestDto;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.user.company.dto.BusinessInformationResponseDto;
import kr.co.pulmuone.v1.user.company.dto.vo.BusinessInformationVo;
import kr.co.pulmuone.v1.user.company.service.UserCompanyBiz;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ClaimUtilRefundService {

    @Autowired
    private final ClaimProcessMapper claimProcessMapper;

    @Autowired
    private final ClaimRequestMapper claimRequestMapper;

    @Autowired
    private final PromotionCouponBiz promotionCouponBiz;

    @Autowired
    private PgBiz pgBiz;

    @Autowired
    private ClaimUtilProcessService claimUtilProcessService;

    @Autowired
    private PointBiz pointBiz;

    @Autowired
    private InicisPgService inicisPgService;

    @Autowired
    private KcpPgService kcpPgService;

    @Autowired
    private UserCompanyBiz userCompanyBiz;

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 적립금을 환불 시킨다.
     * @param reqDto
     * @return
     */
    public void refundPointPrice(OrderClaimRegisterRequestDto reqDto) throws Exception {
        log.debug("적립금 환불 시킨다  회원 :: <{}>, 임직원 :: <{}>, 비회원 :: <{}> ", reqDto.getUrUserId(), reqDto.getUrEmployeeCd(), reqDto.getGuestCi());
        log.debug("적립금 환불 시킨다  주문번호 :: <{}>, 환불금액 :: <{}>", reqDto.getOdid(), reqDto.getRefundPointPrice());

        if (StringUtil.isNotEmpty(reqDto.getCustomUrUserId())) {
            ClaimEnums.ReasonAttributableType reasonType;

            if (ClaimEnums.ReasonAttributableType.BUYER.getType().equals(reqDto.getTargetTp()))
                reasonType = ClaimEnums.ReasonAttributableType.BUYER;
            else
                reasonType = ClaimEnums.ReasonAttributableType.COMPANY;
            PointRefundRequestDto refundDto = PointRefundRequestDto.builder()
                    .urUserId(Long.parseLong(reqDto.getCustomUrUserId()))	//회원 ID
                    .orderNo(String.valueOf(reqDto.getOdid())) 		//주문번호
                    .odClaimId(String.valueOf(reqDto.getOdClaimId())) // 클레임 PK
                    .amount((long)reqDto.getRefundPointPrice())		//환불 금액
                    .reasonAttributableType(reasonType) 			//귀책 사유
                    .build();
            log.debug("적립금 환불 refundDto :: <{}> ", refundDto);

            pointBiz.refundPoint(refundDto);
            pointBiz.expireImmediateRefundPoint(refundDto);
        }
    }

    /**
     * 면세 / 과세 금액 계산
     * @param refundInfoDto
     * @param reqDto
     * @throws Exception
     */
    public void setTaxNonTaxPriceByRefundInfo(OrderClaimPriceInfoDto refundInfoDto, OrderClaimRegisterRequestDto reqDto) throws Exception {

        CancelPriceInfoDto cancelPayDto = null;
        reqDto.setType(OrderEnums.PayType.G.getCode());
        reqDto.setStatus(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode());
        reqDto.setReType(OrderEnums.PayType.F.getCode());
        RefundPaymentInfoDto payInfoDto = claimProcessMapper.getRefundPaymentInfo(reqDto);
        log.debug("setTaxNonTaxPriceByRefundInfo 환불결제정보 payInfoDto :: <{}> ", payInfoDto);

        if (CollectionUtils.isNotEmpty(reqDto.getGoodsInfoList())) {
            List<OrderClaimGoodsInfoDto> goodsInfoList = reqDto.getGoodsInfoList();
            long nonClaimCnt = goodsInfoList.stream().filter(x -> x.getClaimCnt() == 0).count();
            if(goodsInfoList.size() != nonClaimCnt) {
                cancelPayDto = claimProcessMapper.getCancelPriceInfo(reqDto);
            }
        }
        log.debug("setTaxNonTaxPriceByRefundInfo 취소결제금액정보 cancelPayDto :: <{}> ", cancelPayDto);

        if(ObjectUtils.isEmpty(payInfoDto) || ObjectUtils.isEmpty(cancelPayDto)) {
            //throw new BaseException(BaseEnums.Default.EXCEPTION_ISSUED);
            return;
        }

        reqDto.setRefundPrice(refundInfoDto.getRefundPrice());				//환불금액
        reqDto.setRefundPointPrice(refundInfoDto.getRefundPointPrice()); 	//환불적립금액
        reqDto.setOrderShippingPrice(refundInfoDto.getOrderShippingPrice());//주문시부과된배송비
        reqDto.setTaxableOrderShippingPrice(refundInfoDto.getTaxableOrderShippingPrice());//주문시부과된과세배송비
        reqDto.setTaxablePrice(refundInfoDto.getTaxablePrice());
        reqDto.setNonTaxablePrice(refundInfoDto.getNonTaxablePrice());

        this.setTaxNonTaxPrice(payInfoDto, cancelPayDto, reqDto);

        refundInfoDto.setRefundPrice(reqDto.getRefundPrice());              //환불금액
        refundInfoDto.setRefundPointPrice(reqDto.getRefundPointPrice());    //환불적립금액
        refundInfoDto.setTaxablePrice(reqDto.getTaxablePrice());            //과세환불금액
        refundInfoDto.setNonTaxablePrice(reqDto.getNonTaxablePrice());      //면세환불금액
    }

    /**
     * 과세 / 면세 관련 카드 취소 금액을 Set
     * @param payInfoDto
     * @param cancelPayDto
     * @param reqDto
     * @throws Exception
     */
    public void setTaxNonTaxPrice(RefundPaymentInfoDto payInfoDto, CancelPriceInfoDto cancelPayDto, OrderClaimRegisterRequestDto reqDto) throws Exception {
        log.debug("--- 1. 과세 / 면세 금액 설정 START ---");
        log.debug("잔여 과세금액 :: <{}>, 잔여 면세금액 :: <{}>, 신청 과세금액 :: <{}>, 신청 면세금액 :: <{}>", payInfoDto.getRemaindTaxablePrice(), payInfoDto.getRemaindNonTaxablePrice(), cancelPayDto.getTaxPrice(), cancelPayDto.getFreePrice());
        log.debug("환불예정금액 :: <{}>, 환불예정배송비 :: <{}>, 환불예정포인트 :: <{}>", reqDto.getRefundPrice(), reqDto.getShippingPrice(), reqDto.getRefundPointPrice());
        int refundPrice = reqDto.getRefundPrice();
        int pointPrice = reqDto.getRefundPointPrice();
        //int taxablePrice = (payInfoDto.getRemaindTaxablePrice() + reqDto.getTaxableOrderShippingPrice());
        int taxablePrice = (reqDto.getTaxablePrice() + reqDto.getTaxableOrderShippingPrice());
        int nonTaxablePrice = reqDto.getNonTaxablePrice();
        log.debug("taxablePrice :: <{}>", taxablePrice);
        log.debug("payInfoDto.getRemaindTaxablePrice() :: <{}>", payInfoDto.getRemaindTaxablePrice());
        log.debug("nonTaxablePrice :: <{}>", nonTaxablePrice);
        log.debug("payInfoDto.getRemaindNonTaxablePrice() :: <{}>", payInfoDto.getRemaindNonTaxablePrice());
        log.debug("pointPrice :: <{}>", pointPrice);
        // 잔여 과세금액이 신청 과세금액보다 작을 경우, 과세금액은 잔여과세금액으로 설정
        if(taxablePrice > payInfoDto.getRemaindTaxablePrice() && payInfoDto.getPaymentPrice() > 0) {
            nonTaxablePrice += (taxablePrice - payInfoDto.getRemaindTaxablePrice());
            taxablePrice = payInfoDto.getRemaindTaxablePrice();
        }
        // 잔여 비과세금액이 신청 비과세금액보다 작을 경우, 비과세금액은 잔여비과세금액으로 설정
        log.debug("taxablePrice 2 :: <{}>", taxablePrice);
        log.debug("nonTaxablePrice 2 :: <{}>", nonTaxablePrice);
        if(nonTaxablePrice > payInfoDto.getRemaindNonTaxablePrice() && payInfoDto.getPaymentPrice() > 0) {
            nonTaxablePrice = payInfoDto.getRemaindNonTaxablePrice();
        }
        log.debug("pointPrice 2 :: <{}>", pointPrice);
        log.debug("nonTaxablePrice 3 :: <{}>", nonTaxablePrice);
        // 복합과세
        if(taxablePrice > 0 && nonTaxablePrice > 0) {
            log.debug("2. 복합과세 금액 설정");

            // 취소 대상 금액이 존재하지 않을 경우
            if(cancelPayDto.getTaxPrice() == 0) {
                // 배송비만 과세금액으로 설정 한다
                reqDto.setTaxablePrice(reqDto.getTaxableOrderShippingPrice());
                refundPrice -= reqDto.getTaxableOrderShippingPrice();
            }
            else {
                // 신청 금액이 과세금액보다 클 경우
                if(taxablePrice < refundPrice) {
                    log.debug("2-1. 복합과세 신청금액이 잔여 과세금액보다 클 경우");
                    // 잔여 과세금액을 환불 과세금액으로 설정
                    reqDto.setTaxablePrice(taxablePrice);
                    // 신청 과세금액 - 잔여 과세금액을 환불 요청 금액으로
                    refundPrice = refundPrice - taxablePrice;
                }
                else {
                    log.debug("2-2. 복합과세 신청금액이 잔여 과세금액보다 작거나 같을 경우");
                    reqDto.setTaxablePrice(refundPrice);
                    refundPrice = 0;
                }
            }
            // 신청 금액이 잔여 면세금액보다 클 경우
            if(nonTaxablePrice < refundPrice) {
                log.debug("2-3. 복합과세 신청금액이 잔여 면세금액보다 클 경우");
                // 잔여 면세금액을 환불 면세금액으로 설정
                reqDto.setNonTaxablePrice(nonTaxablePrice);
                // 신청 면세금액 - 잔여 면세금액을 환불 요청 금액으로
                refundPrice = refundPrice - nonTaxablePrice;
            }
            else {
                log.debug("2-4. 복합과세 신청금액이 잔여 면세금액보다 작거나 같을 경우");
                reqDto.setNonTaxablePrice(refundPrice);
                refundPrice = 0;
            }
            // 환불 요청 금액이 남아 있을 경우 포인트 환불 금액에 합산
            if(payInfoDto.getPointPrice() > 0) {
                if (refundPrice > 0) {
                    log.debug("2-5. 환불 요청 금액이 남아 있을 경우 포인트 환불 금액에 합산");
                    pointPrice += refundPrice;
                }
            }
        }
        // 과세
        else if(taxablePrice > 0) {
            log.debug("2. 과세 금액 설정");
            // 신청 과세금액이 잔여 과세금액보다 클 경우
            if(taxablePrice < refundPrice) {
                log.debug("2-1. 신청 과세금액이 잔여 과세금액보다 클 경우");
                // 잔여 과세금액을 환불 과세금액으로 설정
                reqDto.setTaxablePrice(taxablePrice);
                if(payInfoDto.getPointPrice() > 0) {
                    // 신청 과세금액 - 잔여 과세금액을 포인트로 환급
                    pointPrice += refundPrice - taxablePrice;
                }
            }
            else {
                log.debug("2-2. 신청 과세금액이 잔여 과세금액보다 작거나 같을 경우");
                // 신청 과세금액을 환불 과세금액으로 설정
                reqDto.setTaxablePrice(refundPrice);
            }

            reqDto.setNonTaxablePrice(nonTaxablePrice);
        }
        // 면세
        else if(nonTaxablePrice > 0) {
            log.debug("2. 면세 금액 설정");
            // 배송비가 존재할 경우
            if(reqDto.getTaxableOrderShippingPrice() > 0) {
                log.debug("2-1. 면세 금액 배송비 존재 설정");
                // 배송비만 과세금액으로 설정 한다
                reqDto.setTaxablePrice(reqDto.getTaxableOrderShippingPrice());
                refundPrice -= reqDto.getTaxableOrderShippingPrice();
            }
            // 신청 면세금액이 잔여 면세금액보다 클 경우
            if(nonTaxablePrice < refundPrice) {
                log.debug("2-2. 신청 면세금액이 잔여 면세금액보다 클 경우");
                // 잔여 면세금액을 환불 면세금액으로 설정
                reqDto.setNonTaxablePrice(nonTaxablePrice);
                if(payInfoDto.getPointPrice() > 0) {
                    // 신청 면세금액 - 잔여 면세금액을 포인트로 환급
                    pointPrice += refundPrice - nonTaxablePrice;
                }
            }
            else {
                log.debug("2-3. 신청 면세금액이 잔여 면세금액보다 작거나 같을 경우");
                // 신청 면세금액을 환불 면세금액으로 설정
                reqDto.setNonTaxablePrice(refundPrice);
            }

            reqDto.setTaxablePrice(taxablePrice);
        }
        // 배송비만 존재
        else if(reqDto.getTaxableOrderShippingPrice() > 0) {
            log.debug("3. 배송비만 존재");
            if(OrderEnums.PaymentType.FREE.getCode().equals(payInfoDto.getPayTp())) {
                log.debug("3-1. 적립금 결제");
                //pointPrice = reqDto.getOrderShippingPrice();
            }
            else {
                log.debug("3-2. 적립금 결제 아님");
                reqDto.setTaxablePrice(reqDto.getTaxableOrderShippingPrice());
//                pointPrice = reqDto.getOrderShippingPrice() - payInfoDto.getPaymentPrice();
//                if(pointPrice < 0) {
//                    pointPrice = 0;
//                }
            }
        }
        reqDto.setRefundPrice(reqDto.getTaxablePrice() + reqDto.getNonTaxablePrice());
        reqDto.setRefundPointPrice(pointPrice);
        log.debug("환불 신청 금액 :: <{}>, 환불 신청 포인트 금액 :: <{}>", reqDto.getRefundPrice(), reqDto.getRefundPointPrice());
        log.debug("환불예정 과세금액 :: <{}>, 환불예정 면세금액 :: <{}>, 환불예정 포인트금액 :: <{}>", reqDto.getTaxablePrice(), reqDto.getNonTaxablePrice(), reqDto.getRefundPointPrice());
        log.debug("환불 잔액 :: <{}>, 환불 잔액 - 환불 금액 :: <{}>", reqDto.getRemaindPrice(), (reqDto.getRemaindPrice() - reqDto.getRefundPrice()));
        log.debug("--- 과세 / 면세 금액 설정 END ---");
    }

    /**
     * 결제 금액을 환불 시킨다.
     * @param reqDto
     */
    public void refundPrice(OrderClaimRegisterRequestDto reqDto) throws Exception {
        log.debug("결제금액 환불 시킨다 ");
        UserVo adminUser = null;
        // 배치 처리가 아닐 경우에만
        if(!String.valueOf(Constants.BATCH_CREATE_USER_ID).equals(reqDto.getUrUserId())) {
            try {
                adminUser = SessionUtil.getBosUserVO();
            }
            catch (Exception e) {
                log.debug("refundPrice UserVo is Null :: <{}>", e.getMessage());
            }
        }

        OrderAccountDto refundInfo = null;
        CancelPriceInfoDto cancelPayDto = null;
        String refundBankNumber = "";
        String refundBankCode = "";
        String refundBankName = "";

        reqDto.setType(OrderEnums.PayType.G.getCode());
        reqDto.setStatus(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode());
        reqDto.setReType(OrderEnums.PayType.F.getCode());
        RefundPaymentInfoDto payInfoDto = claimProcessMapper.getRefundPaymentInfo(reqDto);
        log.debug("refundPrice 환불결제정보 payInfoDto :: <{}> ", payInfoDto);

        if (CollectionUtils.isNotEmpty(reqDto.getGoodsInfoList())) {
            cancelPayDto = claimProcessMapper.getCancelPriceInfo(reqDto);
        }
        log.debug("refundPrice 취소결제금액정보 cancelPayDto :: <{}> ", cancelPayDto);

        if (payInfoDto != null && cancelPayDto != null) {

            // 과세 / 면세 금액 Set
            //this.setTaxNonTaxPrice(payInfoDto, cancelPayDto, reqDto);

            PgEnums.PgAccountType pgAccountType = PgEnums.PgAccountType.findByCode(payInfoDto.getPgService());
            PgEnums.PgServiceType pgServiceType = PgEnums.PgServiceType.findByCode(pgAccountType.getPgServiceType());
            PgAbstractService<?, ?> pgService = pgBiz.getService(pgServiceType);
            OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(payInfoDto.getPayTp());

            if (OrderEnums.PaymentType.VIRTUAL_BANK.getCode().equals(payInfoDto.getPayTp())) {
                if(StringUtil.isEmpty(reqDto.getBankCd())
                        && StringUtil.isEmpty(reqDto.getAccountHolder())
                        && StringUtil.isEmpty(reqDto.getAccountNumber())) {
                    String urUserId = null;
                    if (StringUtil.isNotEmpty(reqDto.getUrUserId())) urUserId = reqDto.getUrUserId();
                    if (StringUtil.isNotEmpty(reqDto.getGuestCi())) urUserId = reqDto.getGuestCi();

                    UserBuyerBiz userBuyerBiz = BeanUtils.getBeanByClass(UserBuyerBiz.class);

                    CommonGetRefundBankRequestDto dto = new CommonGetRefundBankRequestDto();
                    dto.setUrUserId(urUserId);
                    //refundInfo = userBuyerBiz.getRefundBank(dto);
                    refundInfo = claimProcessMapper.getRefundBank(reqDto.getOdOrderId());
                    log.debug("환불 계좌 정보 :: <{}>", refundInfo.toString());
                    refundBankNumber = refundInfo.getAccountNumber();
                    refundBankName = refundInfo.getHolderName();
                    refundBankCode = refundInfo.getBankCode();
                } else {
                    refundBankNumber = reqDto.getAccountNumber();
                    refundBankName = reqDto.getAccountHolder();
                    refundBankCode = reqDto.getBankCd();
                }

                log.debug("refundBankCode1 로컬 ::: <{}>", refundBankCode);
                refundBankCode = pgBiz.getPgBankCode(pgServiceType.getCode(), OrderEnums.PaymentType.VIRTUAL_BANK.getCode(), refundBankCode);
                log.debug("refundBankCode2 PG ::: <{}>", refundBankCode);
                log.debug("refundBankNumber PG ::: <{}>", refundBankNumber);
                log.debug("refundBankName PG ::: <{}>", refundBankName);
            }

            if (paymentType == null) {
                throw new BaseException(BaseEnums.Default.EXCEPTION_ISSUED);
            } else {
                boolean isPartial = false;
                // 결제 금액과 환불 요청금액이 상이할 경우 부분취소
                if(payInfoDto.getPaymentPrice() != reqDto.getRefundPrice()) { isPartial = true; }

                // 부분취소 && 부분취소불가 && 결제방법이 CARD 일 경우
                if(isPartial && OrderEnums.PartCancelYn.PART_CANCEL_N.equals(payInfoDto.getPartCancelYn()) && OrderEnums.PaymentType.CARD.getCode().equals(payInfoDto.getPayTp())) {
                    this.kcpBankSendProc(reqDto, payInfoDto);
                }
                else {
                    CancelRequestDto cancelReqDto = new CancelRequestDto();
                    cancelReqDto.setPartial(isPartial);                                             //부분취소 여부
                    cancelReqDto.setCancelMessage(String.valueOf(reqDto.getPsClaimMallId()));       //취소사유
                    cancelReqDto.setOdid(String.valueOf(reqDto.getOdid()));                         //추문번호
                    cancelReqDto.setCancelPrice(reqDto.getRefundPrice());                           //취소금액
                    cancelReqDto.setTid(payInfoDto.getTid());                                       //거래번호
                    cancelReqDto.setPaymentType(paymentType);                                       //취소지불수단
                    cancelReqDto.setTaxCancelPrice(reqDto.getTaxablePrice());                       //취소 과세 금액
                    cancelReqDto.setTaxFreecancelPrice(reqDto.getNonTaxablePrice());                //취소 비과세 금액
                    cancelReqDto.setExpectedRestPrice(reqDto.getRemaindPrice());                    //취소후 남은 금액 (부분취소시 필수)
                    cancelReqDto.setRefundBankNumber(refundBankNumber);                             //환불계좌번호 (가상계좌 환불 필수)
                    cancelReqDto.setRefundBankCode(refundBankCode);                                 //환불계좌은행코드 - PG 은행 코드 (가상계좌 환불 필수)
                    cancelReqDto.setRefundBankName(refundBankName);                                 //환불계좌 예금주명 (가상계좌 환불 필수)
                    cancelReqDto.setEscrowYn(payInfoDto.getEscrowYn());                             //에스크로결제여부
                    log.debug("환불할 정보 ::: <{}>", cancelReqDto);
                    log.debug("PG TYPE :: <{}>", pgAccountType.getCode());

                    CancelResponseDto cardDto = pgService.cancel(pgAccountType.getCode(), cancelReqDto);

                    log.info("카드 취소 결과 ::: <{}>", cardDto);

                    if (cardDto.isSuccess()) {
                        reqDto.setTaxablePrice(reqDto.getTaxablePrice());
                        reqDto.setNonTaxablePrice(reqDto.getNonTaxablePrice());
                        reqDto.setResponseData(objectMapper.writeValueAsString(cardDto));
                        reqDto.setPgService(payInfoDto.getPgService());
                        reqDto.setPayTp(payInfoDto.getPayTp());
                        reqDto.setTid(payInfoDto.getTid());
                    } else {
                        // BOS 사용자 이고, 관리자_LEVEL_1 권한일 경우
                        if(ObjectUtils.isNotEmpty(adminUser) && adminUser.getListRoleId().contains(Constants.ADMIN_LEVEL_1_AUTH_ST_ROLE_TP_ID)) {
                            log.debug("====================== 관리자_LEVEL_1 권한 정상 처리");
                            // 실패 여부와 상관없이 정상 처리
                            reqDto.setTaxablePrice(reqDto.getTaxablePrice());
                            reqDto.setNonTaxablePrice(reqDto.getNonTaxablePrice());
                            reqDto.setResponseData(objectMapper.writeValueAsString(cardDto));
                            reqDto.setPgService(payInfoDto.getPgService());
                            reqDto.setPayTp(payInfoDto.getPayTp());
                            reqDto.setTid(payInfoDto.getTid());
                        }
                        else {
                            // BOS 사용자 일 경우 오류 처리
                            if(ObjectUtils.isNotEmpty(adminUser)) {
                                throw new BaseException("PG " + pgServiceType.getCodeName() + " : " + cardDto.getMessage());
                            }
                            else {
                                throw new BaseException(cardDto.getMessage());
                            }
                        }
                    }
                }
            }
        } else {
            throw new BaseException(BaseEnums.Default.EXCEPTION_ISSUED);
        }
    }

    /**
     * KCP 무통장 입금 처리
     * @param reqDto
     */
    private void kcpBankSendProc(OrderClaimRegisterRequestDto reqDto, RefundPaymentInfoDto payInfoDto) throws Exception {
        // 계좌정보가 존재하지 않을 경우
        if(StringUtil.isEmpty(reqDto.getBankCd())
                && StringUtil.isEmpty(reqDto.getAccountHolder())
                && StringUtil.isEmpty(reqDto.getAccountNumber())) {
            throw new BaseException(OrderClaimEnums.PartCancelError.ACCOUNT_EMPTY_ERROR.getMessage());
        }

        ApiResult<?> companyResult = userCompanyBiz.getBizInfo();
        BusinessInformationResponseDto businessResponse = (BusinessInformationResponseDto) companyResult.getData();
        BusinessInformationVo bizInfo = businessResponse.getBizInfo();

        KcpRemitRequestDto kcpRemitRequestDto = new KcpRemitRequestDto();
        kcpRemitRequestDto.setPaymentPrice(reqDto.getRefundPrice());
        kcpRemitRequestDto.setRefundBankCode(pgBiz.getPgBankCode(PgEnums.PgServiceType.KCP.getCode(), OrderEnums.PaymentType.VIRTUAL_BANK.getCode(), reqDto.getBankCd()));
        kcpRemitRequestDto.setRefundBankNumber(reqDto.getAccountNumber());
        kcpRemitRequestDto.setSendName(bizInfo.getBusinessName());
        KcpRemitResponseDto resDto = kcpPgService.remit(kcpRemitRequestDto);

        log.info("무통장 입금 처리 결과 ::: <{}>", resDto);

        // 성공 시
        if(resDto.isSuccess()) {
            reqDto.setTaxablePrice(reqDto.getTaxablePrice());
            reqDto.setNonTaxablePrice(reqDto.getNonTaxablePrice());
            reqDto.setResponseData(objectMapper.writeValueAsString(resDto));
            reqDto.setPgService(payInfoDto.getPgService());
            reqDto.setPayTp(payInfoDto.getPayTp());
        }
        else {
            throw new BaseException(resDto.getMessage());
        }
    }

    /**
     * CS환불 KCP 무통장 입금 처리
     * @param reqDto
     */
    protected void csRefundKcpBankSendProc(OrderCSRefundRegisterRequestDto reqDto) throws Exception {

        ApiResult<?> companyResult = userCompanyBiz.getBizInfo();
        BusinessInformationResponseDto businessResponse = (BusinessInformationResponseDto) companyResult.getData();
        BusinessInformationVo bizInfo = businessResponse.getBizInfo();

        KcpRemitRequestDto kcpRemitRequestDto = new KcpRemitRequestDto();
        kcpRemitRequestDto.setPaymentPrice(reqDto.getRefundPrice());
        kcpRemitRequestDto.setRefundBankCode(pgBiz.getPgBankCode(PgEnums.PgServiceType.KCP.getCode(), OrderEnums.PaymentType.VIRTUAL_BANK.getCode(), reqDto.getBankCd()));
        kcpRemitRequestDto.setRefundBankNumber(reqDto.getAccountNumber());
        kcpRemitRequestDto.setSendName(bizInfo.getBusinessName());
        KcpRemitResponseDto resDto = kcpPgService.remit(kcpRemitRequestDto);

        log.info("--------------------------- CS환불 무통장 입금 처리 결과 ::: <{}>", resDto);
        // 성공 시
        if(resDto.isSuccess()) {
            reqDto.setResponseData(objectMapper.writeValueAsString(resDto));
            reqDto.setTid(resDto.getTrade_seq());
        }
        else {
            throw new BaseException(OrderCsEnums.CsRefundError.PAYMENT_FAIL.getMessage() + " [" + resDto.getMessage() + "]");
        }
    }

    /**
     * 쿠폰을 재발급한다.
     *
     * @param reqDto
     * @throws Exception
     */
    public void putRefundCoupon(OrderClaimRegisterRequestDto reqDto) throws Exception {
        log.debug("쿠폰 재발급 시작 !! <{}>", reqDto);

        //장바구니 쿠폰 재발급한다.
        if (CollectionUtils.isNotEmpty(reqDto.getCartCouponInfoList())) {
            long pmCouponIssueId = 0;
            int i = 0;

            for (OrderClaimCouponInfoDto cartDto : reqDto.getCartCouponInfoList()) {
                log.debug("장바구니 쿠폰 재발급 주문번호 PK :: <{}>, 쿠폰번호PK :: <{}>, 주문클레임PK ::: <{}>", cartDto.getOdOrderId(), cartDto.getPmCouponIssueId(), reqDto.getOdClaimId());
                if (i == 0 || pmCouponIssueId != cartDto.getPmCouponIssueId()) {
                    promotionCouponBiz.reissueCoupon(cartDto.getOdOrderId(), cartDto.getPmCouponIssueId(), reqDto.getOdClaimId(), reqDto.getGoodsInfoList(), reqDto.getCustomUrUserId());
                }
                pmCouponIssueId = cartDto.getPmCouponIssueId();
                i = i + 1;
            }
        }

        //상품 쿠폰 재발급한다.
        if (CollectionUtils.isNotEmpty(reqDto.getGoodsCouponInfoList())) {
            long pmCouponIssueId = 0;
            int i = 0;
            for (OrderClaimCouponInfoDto goodsDto : reqDto.getGoodsCouponInfoList()) {
                log.debug("상품쿠폰 재발급 주문번호 PK :: <{}>, 쿠폰번호PK :: <{}>, 주문클레임PK ::: <{}>", goodsDto.getOdOrderId(), goodsDto.getPmCouponIssueId(), reqDto.getOdClaimId());
                if (i == 0 || pmCouponIssueId != goodsDto.getPmCouponIssueId()) {
                    promotionCouponBiz.reissueCoupon(goodsDto.getOdOrderId(), goodsDto.getPmCouponIssueId(), reqDto.getOdClaimId(), null, reqDto.getCustomUrUserId());
                }
                pmCouponIssueId = goodsDto.getPmCouponIssueId();
                i = i + 1;
            }
        }

        // 배송비쿠폰 재발급한다.
        if (CollectionUtils.isNotEmpty(reqDto.getDeliveryCouponList())) {
            long pmCouponIssueId = 0;
            int i = 0;
            for (OrderClaimCouponInfoDto deliveryDto : reqDto.getDeliveryCouponList()) {
                log.debug("배송비쿠폰 재발급 주문번호 PK :: <{}>, 쿠폰번호PK :: <{}>, 주문클레임PK ::: <{}>", deliveryDto.getOdOrderId(), deliveryDto.getPmCouponIssueId(), reqDto.getOdClaimId());
                if (i == 0 || pmCouponIssueId != deliveryDto.getPmCouponIssueId()) {
                    promotionCouponBiz.reissueCoupon(deliveryDto.getOdOrderId(), deliveryDto.getPmCouponIssueId(), reqDto.getOdClaimId(), null, reqDto.getCustomUrUserId());
                }
                pmCouponIssueId = deliveryDto.getPmCouponIssueId();
                i = i + 1;
            }
        }
    }


    /**
     * 취소완료, 취소요청, 반품 신청 할 때 추가 결제 할 때
     * @param reqDto
     * @return
     */
    public BasicDataResponseDto addPayment(OrderClaimRegisterRequestDto reqDto, boolean directPayFlag) throws Exception {

        log.debug("추가 결제 일 때 psPayCd ::: <{}>, cardCode :: <{}>", reqDto.getAddPaymentInfo().getPsPayCd(), reqDto.getAddPaymentInfo().getCardCode());

        OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(reqDto.getAddPaymentInfo().getPsPayCd());
		log.debug("paymentType :: <{}>", paymentType);
		PgAbstractService<?, ?> pgService = pgBiz.getService(paymentType, reqDto.getAddPaymentInfo().getCardCode());
		log.debug("pgServiceType :: <{}>", pgService.getServiceType());
		String pgBankCode = pgBiz.getPgBankCode(pgService.getServiceType().getCode(), paymentType.getCode(), reqDto.getAddPaymentInfo().getCardCode());
		log.debug("pagBankCode :: <{}>", pgBankCode);

        // 기본 결제 필수
        BasicDataRequestDto paymentRequestFormDataDto = new BasicDataRequestDto();
        paymentRequestFormDataDto.setAddPay(true);
        paymentRequestFormDataDto.setOdid(reqDto.getOdid() + "-" + (directPayFlag ? "C" + reqDto.getOdClaimId() : reqDto.getOdAddPaymentReqInfoId())); // 즉시결제여부가 true일 경우 클레임 아이디 추가
        paymentRequestFormDataDto.setPaymentType(paymentType);									//구분(카드 또는 실시간계좌)
        paymentRequestFormDataDto.setPgBankCode(pgBankCode);									//카드사코드
        paymentRequestFormDataDto.setQuota(reqDto.getAddPaymentInfo().getInstallmentPeriod());	//할부기간
        paymentRequestFormDataDto.setGoodsName(reqDto.getGoodsNm());							//상품명
        paymentRequestFormDataDto.setPaymentPrice(reqDto.getRefundPrice());						//결제금액
        paymentRequestFormDataDto.setTaxPaymentPrice(reqDto.getRefundPrice());					//과세금액
        paymentRequestFormDataDto.setTaxFreePaymentPrice(0);									//비과세금액
        paymentRequestFormDataDto.setBuyerName(reqDto.getBuyerNm());							//주문자명
        paymentRequestFormDataDto.setBuyerEmail(reqDto.getBuyerMail());							//주문자이메일
        paymentRequestFormDataDto.setBuyerMobile(reqDto.getBuyerHp());							//주문자휴대폰
        paymentRequestFormDataDto.setLoginId(StringUtil.isNotEmpty(reqDto.getCustomUrUserId()) ? reqDto.getLoginId() : "비회원");
        if (OrderEnums.PaymentType.VIRTUAL_BANK.getCode().equals(reqDto.getAddPaymentInfo().getPsPayCd())) {
            paymentRequestFormDataDto.setVirtualAccountDateTime(pgBiz.getVirtualAccountDateTime());
        }

        EtcDataClaimDto etcDataClaimDto = EtcDataClaimDto.builder()
                .odOrderId(reqDto.getOdOrderId())
                .odid(reqDto.getOdid())
                .odAddPaymentReqInfoId(reqDto.getOdAddPaymentReqInfoId())
                .paymentType(paymentType.getCode())
                .build();
        // 즉시 결제 여부가 true일 경우 클레임 아이디 추가
        if(directPayFlag) {
            etcDataClaimDto.setOdClaimId(reqDto.getOdClaimId());
        }

        paymentRequestFormDataDto.setEtcData(pgService.toStringEtcData(etcDataClaimDto));
        log.debug("추가결제 데이터 <{}>", paymentRequestFormDataDto);

        return pgService.getBasicData(paymentRequestFormDataDto);
    }

    /**
     * 환불 금액 정보 SET
     * @param orderClaimViewRequestDto
     * @param paymentInfoDto
     * @param refundInfoDto
     * @param refundTotalPrice
     * @param totTaxParice
     * @param totNonTaxParice
     */
    public void setRefundPriceInfo(OrderClaimViewRequestDto orderClaimViewRequestDto, OrderClaimPaymentInfoDto paymentInfoDto, OrderClaimPriceInfoDto refundInfoDto, int refundTotalPrice, int totTaxParice, int totNonTaxParice) {

        // 추가 배송비 결제 정보 조회
        OrderClaimPriceInfoDto addPaymentShippingPriceInfo = claimRequestMapper.getOrderClaimAddPaymentShippingPrice(orderClaimViewRequestDto);

        // 이전 결제 정보가 존재할 경우
        if(refundInfoDto.getPrevAddPaymentShippingPrice() > 0) {
            // PAYMENT_MASTER 정보 내 추가로 결제한 내역이 존재할 경우
            if(addPaymentShippingPriceInfo.getPrevAddPaymentShippingPrice() > 0) {
                int paymentAddShippingPrice = addPaymentShippingPriceInfo.getPrevAddPaymentShippingPrice();
                // 환불 금액정보로 Set
                refundInfoDto.setRefundRegPrice(paymentAddShippingPrice);
                refundTotalPrice += (refundInfoDto.getPrevAddPaymentShippingPrice() - paymentAddShippingPrice);
            }
            // PAYMENT_MASTER 정보 내 추가로 결제한 내역이 존재하지 않을 경우 원 결제수단 환불
            else {
                refundTotalPrice += refundInfoDto.getPrevAddPaymentShippingPrice();
            }
        }

        int taxOrderShippingPrice = refundInfoDto.getOrderShippingPrice();    // 과세 배송비
        refundInfoDto.setTaxableOrderShippingPrice(taxOrderShippingPrice);
        refundInfoDto.setTaxablePrice(totTaxParice);        // 과세금액
        refundInfoDto.setNonTaxablePrice(totNonTaxParice);  // 면세금액

        int refundPrice = 0;		//환불금액
        int remainPaymentPrice = 0; //잔여결제금액
        int refundPointPrice = 0;	//환불적립금액
        int remainPointPrice = 0;	//잔여적립금액

        /** 과세 / 비과세 정보에 따라 결제금액 / 포인트 환불 수정 **/
        // 결제금액과 환불금액이 동일할 경우
        if (paymentInfoDto.getPaymentPrice() == refundTotalPrice) {
            log.debug("-- 결제금액과 환불금액이 동일");
            refundPrice = refundTotalPrice;
            remainPaymentPrice = 0;
            refundPointPrice = 0;
            remainPointPrice = paymentInfoDto.getPointPrice();
        }
        // 환불금액과 결제금액이 동일하지 않을 경우
        else {
            log.debug("-- 결제금액과 환불금액이 동일하지 않음");
            // 결제 금액이 존재할 경우 과세 / 비과세 체크
            if(paymentInfoDto.getPaymentPrice() > 0) {
                log.debug("-- 결제금액 존재");
                // 환불 요청 금액이 존재하지 않을 경우
                if(refundTotalPrice == 0) {
                    log.debug("-- 환불 요청 금액이 존재하지 않음");
                    refundPrice = 0;
                    remainPaymentPrice = paymentInfoDto.getPaymentPrice();
                    refundPointPrice = 0;
                    remainPointPrice = paymentInfoDto.getPointPrice();
                }
                else {
                    /**
                     *  적립금을 사용했을 경우 배송비가 존재하면, 배송비에서 적립금을 우선 차감한다
                     *  ex)
                     *  상품 3000 배송비 3000 적립금 100 결제 5900
                     *  상품 3000 배송비 2900 적립금 0   결제 5900 - 배송비에서 적립금 100원 우선 차감
                     */
                    log.debug("-- 환불 요청 금액이 존재");
                    int sumGoodsPrice = totTaxParice + totNonTaxParice;
                    // 선택 상품 금액이 존재하지 않을 경우
                    if(sumGoodsPrice == 0) {
                        log.debug("-- 선택 상품 금액이 존재하지 않음");
                        if(refundTotalPrice < paymentInfoDto.getPaymentPrice()) {
                            refundPrice = refundTotalPrice;
                            remainPaymentPrice = paymentInfoDto.getPaymentPrice() - refundTotalPrice;
                            refundPointPrice = 0;
                            remainPointPrice = paymentInfoDto.getPointPrice();
                        }
                        else {
                            refundPrice = paymentInfoDto.getPaymentPrice();
                            remainPaymentPrice = 0;
                            refundPointPrice = refundTotalPrice - paymentInfoDto.getPaymentPrice();
                            remainPointPrice = paymentInfoDto.getPointPrice() - refundPointPrice;
                        }
                        if(taxOrderShippingPrice > 0 && refundPointPrice > 0) {
                            taxOrderShippingPrice = (refundPointPrice < taxOrderShippingPrice ? taxOrderShippingPrice - refundPointPrice : 0);
                        }
                        refundInfoDto.setTaxableOrderShippingPrice(taxOrderShippingPrice);
                    }
                    else {
                        log.debug("-- 선택 상품 금액이 존재");
                        // (결제 과세금액이 존재할때 && 현재 신청 금액에 과세금액이 존재할때) || (결제 비과세금액이 존재할때 && 현재 신청 금액에 비과세금액이 존재할때)
                        if((paymentInfoDto.getTaxablePrice() > 0 && totTaxParice > 0) || (paymentInfoDto.getNonTaxablePrice() > 0 && totNonTaxParice > 0)) {
                            log.debug("-- (결제 과세금액이 존재 && 현재 신청 금액에 과세금액이 존재) || (결제 비과세금액이 존재 && 현재 신청 금액에 비과세금액이 존재)");
                            int paymentPrice = paymentInfoDto.getPaymentPrice();    // 결제금액
                            int pointPrice = paymentInfoDto.getPointPrice();        // 포인트금액
                            log.debug("paymentPrice :: <{}>, pointPrice :: <{}>, taxOrderShippingPrice :: <{}>", paymentPrice, pointPrice, taxOrderShippingPrice);

                            // 환불 전체 금액에서 주문시 부과된 배송비를 제외한다
                            if(pointPrice > 0) {
                                refundTotalPrice -= taxOrderShippingPrice;
                            }

                            // refundPrice 처리
                            // 결제수단으로 결제한 금액이 총 환불 금액보다 작을 경우
                            if (paymentInfoDto.getPaymentPrice() < refundTotalPrice) {
                                refundPrice = paymentPrice;
                                remainPaymentPrice = 0;
                                refundPointPrice = refundTotalPrice - paymentPrice;
                                remainPointPrice = pointPrice - (refundTotalPrice - paymentPrice);
                            }
                            // 결제수단으로 결제한 금액이 총 환불 금액보다 크거나 같을 경우
                            else {
                                refundPrice = refundTotalPrice;
                                remainPaymentPrice = paymentPrice - refundTotalPrice;
                                refundPointPrice = 0;
                                remainPointPrice = pointPrice;
                            }

                            // 포인트 사용금액이 존재할 경우
                            if(pointPrice > 0) {
                                // 주문시 배송비가 포인트 사용금액보다 클 경우
                                if(taxOrderShippingPrice > pointPrice) {
                                    taxOrderShippingPrice -= pointPrice;
                                    refundPrice += taxOrderShippingPrice;
                                    remainPaymentPrice -= taxOrderShippingPrice;
                                    refundPointPrice += pointPrice;
                                    remainPointPrice -= pointPrice;
                                }
                                // 주문시 배송비가 포인트 사용금액보다 작을 경우
                                else {
                                    refundPointPrice += taxOrderShippingPrice;
                                    remainPointPrice -= taxOrderShippingPrice;
                                    taxOrderShippingPrice = 0;
                                }
                            }

                            refundInfoDto.setTaxableOrderShippingPrice(taxOrderShippingPrice);
                        }
                        else {
                            log.debug("-- ! (결제 과세금액이 존재 && 현재 신청 금액에 과세금액이 존재) || (결제 비과세금액이 존재 && 현재 신청 금액에 비과세금액이 존재)");
                            // 포인트로 환불 처리
                            refundPrice = 0;
                            remainPaymentPrice = paymentInfoDto.getPaymentPrice();
                            refundPointPrice = refundTotalPrice;
                            remainPointPrice = paymentInfoDto.getPointPrice() - refundPointPrice;
                            refundInfoDto.setTaxableOrderShippingPrice(0);
                        }
                    }
                }
            }
            // 결제 금액이 존재하지 않을 경우 포인트 환불
            else {
                refundPrice = 0;
                remainPaymentPrice = 0;
                refundPointPrice = refundTotalPrice;
                remainPointPrice = paymentInfoDto.getPointPrice() - refundTotalPrice;
                refundInfoDto.setTaxableOrderShippingPrice(0);
            }
        }

        /** 2021.04.21 추가 START */
        log.debug("환불 금액 :: <{}>, 환불 포인트 :: <{}>, 전체 환불 금액 :: <{}>", refundPrice, refundPointPrice, refundTotalPrice);
        log.debug("환불 금액 잔액 :: <{}>, 환불 포인트 잔액 :: <{}>", remainPaymentPrice, remainPointPrice);
        if(remainPaymentPrice < 0) {
            refundPrice = refundPrice + remainPaymentPrice;
        }
        if(remainPointPrice < 0) {
            refundPointPrice = refundPointPrice + remainPointPrice;
        }
        /** 2021.04.21 추가 END */

        log.debug("refundPrice :: <{}>, remainPaymentPrice :: <{}>, refundPointPrice ::: <{}>, remainPointPrice :: <{}>",refundPrice, remainPaymentPrice, refundPointPrice, remainPointPrice);

        /** 2021.04.21 추가 START */
        // 추가 배송비 여부가 Y가 아닐 경우
        if(!OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode().equals(refundInfoDto.getAddPaymentShippingPriceYn())) {
            refundInfoDto.setRefundTotalPrice(refundPrice + refundPointPrice);    // 총환불금액
        }
        /** 2021.04.21 추가 END */
        refundInfoDto.setRefundPrice(refundPrice);					//환불금액
        refundInfoDto.setRemainPaymentPrice(remainPaymentPrice);	//잔여결제금액
        refundInfoDto.setRefundPointPrice(refundPointPrice); 		//환불적립금액
        refundInfoDto.setRemainPointPrice(remainPointPrice);		//잔여적립금액
    }

    /**
     * 주문 환불계좌 조회
     * @param odOrderId
     * @return OrderAccountDto
     */
    public OrderAccountDto getRefundBank(Long odOrderId) {
        return claimProcessMapper.getRefundBank(odOrderId);
    }

    /**
     * 가상계좌 망취소 처리
     *
     * @param reqDto
     * @throws Exception
     */
    public void putVirtualAccountCancel(OrderClaimRegisterRequestDto reqDto) throws Exception {
        // 가상계좌 결제정보 조회
        OrderClaimVirtualPaymentInfoDto orderClaimVirtualPaymentInfoDto = claimProcessMapper.getOrderClaimVirtualPaymentInfo(reqDto.getOdOrderId());
        if(ObjectUtils.isNotEmpty(orderClaimVirtualPaymentInfoDto)) {
            PgEnums.PgAccountType pgAccountType = PgEnums.PgAccountType.findByCode(orderClaimVirtualPaymentInfoDto.getPgService());
            PgEnums.PgServiceType pgServiceType = PgEnums.PgServiceType.findByCode(pgAccountType.getPgServiceType());
            PgAbstractService<?, ?> pgService = pgBiz.getService(pgServiceType);
            // KCP 일 경우
            if(PgEnums.PgServiceType.KCP.getCode().equals(pgAccountType.getPgServiceType())){
                KcpApprovalResponseDto kcpApprovalResponseDto = pgService.toDtoJsonString(orderClaimVirtualPaymentInfoDto.getResponseData(), KcpApprovalResponseDto.class);
                if(ObjectUtils.isNotEmpty(kcpApprovalResponseDto) && StringUtils.isNotEmpty(kcpApprovalResponseDto.getTno()) && StringUtils.isNotEmpty(kcpApprovalResponseDto.getOrdr_idxx())) {
                    CancelRequestDto cancelReqDto = new CancelRequestDto();
                    cancelReqDto.setPartial(false);
                    cancelReqDto.setTid(kcpApprovalResponseDto.getTno());
                    cancelReqDto.setCancelMessage("가상계좌 입금 전 취소");
                    cancelReqDto.setOdid(kcpApprovalResponseDto.getOrdr_idxx());
                    kcpPgService.cancel(pgAccountType.getCode(), cancelReqDto);
                }
            }
            // INICIS 일 경우
            else {
                // APP 또는 모바일일 경우
                if(SystemEnums.AgentType.APP.getCode().equals(orderClaimVirtualPaymentInfoDto.getAgentTypeCd()) ||
                    SystemEnums.AgentType.MOBILE.getCode().equals(orderClaimVirtualPaymentInfoDto.getAgentTypeCd())) {
                    InicisMobileApprovalResponseDto resDto = pgService.toDtoJsonString(orderClaimVirtualPaymentInfoDto.getResponseData(), InicisMobileApprovalResponseDto.class);
                    if(ObjectUtils.isNotEmpty(resDto) && StringUtils.isNotEmpty(resDto.getP_REQ_URL()) && ObjectUtils.isNotEmpty(resDto.getNetCancelAuthMap())) {
                        // 가상계좌 결제정보 존재 시 가상계좌 망취소 처리
                        // 모바일 일경우 망취소 가능시간이 승인TID 기준 1분, 인증TID 기준 10분 이내에만 가능 하여 사실상 무의미
                        // inicisPgService.mobileNetCancel(resDto.getP_REQ_URL(), resDto.getNetCancelAuthMap());
                    }
                }
                // PC일 경우
                else if(SystemEnums.AgentType.PC.getCode().equals(orderClaimVirtualPaymentInfoDto.getAgentTypeCd())) {
                    InicisApprovalResponseDto resDto = pgService.toDtoJsonString(orderClaimVirtualPaymentInfoDto.getResponseData(), InicisApprovalResponseDto.class);
                    if(ObjectUtils.isNotEmpty(resDto) && StringUtils.isNotEmpty(resDto.getNetCancelUrl()) && ObjectUtils.isNotEmpty(resDto.getAuthMap())) {
                        // 가상계좌 결제정보 존재 시 가상계좌 망취소 처리
                        inicisPgService.netCancel(resDto.getNetCancelUrl(), resDto.getAuthMap());
                    }
                }
            }
        }
    }

    /**
     * OD_PAYMENT_MASTER 입금전 취소 업데이트
     *
     * @param reqDto
     * @throws Exception
     */
    public void putPaymentMasterStatus(OrderClaimRegisterRequestDto reqDto) throws Exception {
        claimProcessMapper.putPaymentMasterStatus(reqDto);
    }

    /**
     * 주문 I/F여부 체크
     * @param odOrderDetlId
     * @return
     */
    public String getOrderIsInterfaceCheck(long odOrderDetlId) {
        return claimProcessMapper.getOrderIsInterfaceCheck(odOrderDetlId);
    }

    /**
     * 출고처 I/F여부 체크
     * @param odOrderDetlId
     * @return
     */
    public int getWarehouseIsInterfaceYnCheck(long odOrderDetlId, String[] urWarehouseIds) {
        return claimProcessMapper.getWarehouseIsInterfaceYnCheck(odOrderDetlId, urWarehouseIds);
    }

    /**
     * 추가 배송비 존재 시 환불 처리
     * @param reqDto
     * @throws Exception
     */
    public int putOrderClaimAddShippingPrice(OrderClaimRegisterRequestDto reqDto) throws Exception {
        log.debug("===== 추가 배송비 결제 정보 조회");
        int updateCnt = 0;
        List<OrderClaimAddShippingPaymentInfoDto> paymentList = claimProcessMapper.putOrderClaimAddShippingPrice(reqDto);
        if(paymentList.isEmpty()) {
            log.debug("===== 추가 배송비 결제 정보 미존재");
            return updateCnt;
        }

        log.debug("===== 추가 배송비 결제 정보 존재");
        Map<Long, List<OrderClaimAddShippingPaymentInfoDto>> paymentGroup = paymentList.stream().collect(Collectors.groupingBy(OrderClaimAddShippingPaymentInfoDto::getOdPaymentMasterId, LinkedHashMap::new, Collectors.toList()));
        for(long odPaymentMasterId : paymentGroup.keySet()) {
            List<OrderClaimAddShippingPaymentInfoDto> paymentItems = paymentGroup.get(odPaymentMasterId);
            int sumAddShippingPrice = paymentItems.stream().mapToInt(OrderClaimAddShippingPaymentInfoDto::getAddPaymentShippingPrice).sum();    // 총 추가배송비
            OrderClaimAddShippingPaymentInfoDto paymentInfo = paymentItems.stream().filter(x -> x.getOdPaymentMasterId() == odPaymentMasterId).findAny().orElse(null);  // PAYMENT_MASTER 정보
            if(ObjectUtils.isEmpty(paymentInfo)) {
                continue;
            }

            boolean isPartial = false;
            if(sumAddShippingPrice != paymentInfo.getPaymentPrice()) {
                isPartial = true;
            }
            PgEnums.PgAccountType pgAccountType = PgEnums.PgAccountType.findByCode(paymentInfo.getPgService());
            PgEnums.PgServiceType pgServiceType = PgEnums.PgServiceType.findByCode(pgAccountType.getPgServiceType());
            PgAbstractService<?, ?> pgService = pgBiz.getService(pgServiceType);
            OrderEnums.PaymentType paymentType = OrderEnums.PaymentType.findByCode(paymentInfo.getPayTp());

            CancelRequestDto cancelReqDto = new CancelRequestDto();
            cancelReqDto.setPartial(isPartial);                                             //부분취소 여부
            cancelReqDto.setCancelMessage(String.valueOf(reqDto.getPsClaimMallId()));       //취소사유
            cancelReqDto.setOdid(String.valueOf(reqDto.getOdid()));                         //추문번호
            cancelReqDto.setCancelPrice(sumAddShippingPrice);                               //취소금액
            cancelReqDto.setTid(paymentInfo.getTid());                                      //거래번호
            cancelReqDto.setPaymentType(paymentType);                                       //취소지불수단
            cancelReqDto.setTaxCancelPrice(sumAddShippingPrice);                            //취소 과세 금액
            cancelReqDto.setTaxFreecancelPrice(0);                                          //취소 비과세 금액
            cancelReqDto.setExpectedRestPrice(0);                                           //취소후 남은 금액 (부분취소시 필수)
//        cancelReqDto.setRefundBankNumber(refundBankNumber);                             //환불계좌번호 (가상계좌 환불 필수)
//        cancelReqDto.setRefundBankCode(refundBankCode);                                 //환불계좌은행코드 - PG 은행 코드 (가상계좌 환불 필수)
//        cancelReqDto.setRefundBankName(refundBankName);                                 //환불계좌 예금주명 (가상계좌 환불 필수)
            cancelReqDto.setEscrowYn(paymentInfo.getEscrowYn());                            //에스크로결제여부
            log.debug("환불할 정보 ::: <{}>", cancelReqDto);
            log.debug("PG TYPE :: <{}>", pgAccountType.getCode());

            CancelResponseDto cardDto = pgService.cancel(pgAccountType.getCode(), cancelReqDto);

            log.info("카드 취소 결과 ::: <{}>", cardDto);

            if (cardDto.isSuccess()) {
                OrderClaimRegisterRequestDto paymentReqDt = new OrderClaimRegisterRequestDto();
                paymentReqDt.setOdOrderId(reqDto.getOdOrderId());
                paymentReqDt.setOdClaimId((reqDto.getOdClaimId()));
                paymentReqDt.setShippingPrice(sumAddShippingPrice);
                paymentReqDt.setTaxablePrice(sumAddShippingPrice);
                paymentReqDt.setRefundPrice(sumAddShippingPrice);
                paymentReqDt.setType(OrderEnums.PayType.F.getCode());
                paymentReqDt.setPayTp(paymentInfo.getPayTp());
                paymentReqDt.setPgService(paymentInfo.getPgService());
                paymentReqDt.setTid(paymentInfo.getTid());
                paymentReqDt.setPartCancelYn(paymentInfo.getPartCancelYn());
                paymentReqDt.setEscrowYn(paymentInfo.getEscrowYn());
                paymentReqDt.setResponseData(objectMapper.writeValueAsString(cardDto));
                claimUtilProcessService.putOrderPaymentInfo(paymentReqDt);
            }
            else {
                throw new BaseException(cardDto.getMessage());
            }
            log.debug("OD_PAYMENT_MASTER_ID :: <{}>, ADD_SHIPPING_PRICE :: <{}>", odPaymentMasterId, sumAddShippingPrice);
            updateCnt++;
        }
        return updateCnt;
    }
}
